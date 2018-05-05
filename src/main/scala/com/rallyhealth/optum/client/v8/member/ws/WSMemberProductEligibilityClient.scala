package com.rallyhealth.optum.client.v8.member.ws

import java.time.Instant

import com.rallyhealth.argosy.correlationId.CorrelationId
import com.rallyhealth.enigma.v4.NeoEncryptionService
import com.rallyhealth.optum.client.v8.auth.model.OptumAccessTokenResponse
import com.rallyhealth.optum.client.v8.common.ApiConstant._
import com.rallyhealth.optum.client.v8.common.HttpUtil.Header._
import com.rallyhealth.optum.client.v8.common.HttpUtil.Method._
import com.rallyhealth.optum.client.v8.common.JsonUtil._
import com.rallyhealth.optum.client.v8.common._
import com.rallyhealth.optum.client.v8.common.model.{BaseOptumResponseError, OptumResponseAppError, OptumResponseError, OptumResponseSuccess}
import com.rallyhealth.optum.client.v8.member.MemberProductEligibilityClient
import com.rallyhealth.optum.client.v8.member.config.MemberProductEligibilityRallyConfig
import com.rallyhealth.optum.client.v8.member.model.{RequestType, _}
import com.rallyhealth.optum.client.v8.member.ws.MemPERequestBodyBuilder._
import com.rallyhealth.rq.v1.handler.HandledRqRequest
import com.rallyhealth.rq.v1.handler.RqResponseHandlers.ResponseHandler
import com.rallyhealth.rq.v1.{Rq, RqClient, RqRequest, RqResponse, _}
import com.rallyhealth.spartan.v2.datetime.DateTimeHelpers
import play.api.libs.json.{JsLookupResult, JsValue, Json}

import scala.concurrent.Future

class WSMemberProductEligibilityClient(
  optumConfig: MemberProductEligibilityRallyConfig,
  wsClient: RqClient,
  neoEncryptionService: Option[NeoEncryptionService]
) extends MemberProductEligibilityClient
  with SecureLogHelper
  with StatsHelper {

  final val memPeUrl = s"${optumConfig.baseUrl}${optumConfig.memberProductEligibilityPath}"
  override val statPath: Seq[String] = Seq("member", "product", "eligibility", "v2")

  /**
    * Finds member product eligibility with given requested parameters
    *
    * @param request The MemPE API request
    * @param meta Meta data needed to call Optum API
    * @param token OAuth2 Access token needed to call Optum generated using client_credentials.
    * @return Either [[BaseOptumResponseError]] or [[OptumResponseSuccess]]
    */
  override def findMemberProductEligibility(
    request: OptumMemPERequest,
    meta: OptumRequestMetaData
  )(implicit token: Option[OptumAccessTokenResponse]): Future[Either[BaseOptumResponseError, OptumResponseSuccess]] = {
    val wsReq = buildRequestWithHandler(request, meta)
    wsClient.execute(wsReq)
  }

  override def findByMemberProductEligibilityRequest(
    request: MemberProductEligibilityRequest,
    correlationId: Option[String]
  )(implicit token: Option[OptumAccessTokenResponse]): Future[Either[BaseOptumResponseError, OptumResponseSuccess]] = {
    val wsReq = buildRequestFromMPEWithHandler(request, correlationId)
    wsClient.execute(wsReq)
  }

  def buildRequestFromMPEWithHandler(
    request: MemberProductEligibilityRequest,
    correlationId: Option[String]
  )(implicit token: Option[OptumAccessTokenResponse]): HandledRqRequest[Either[BaseOptumResponseError, OptumResponseSuccess]] = {
    val requestBody: String = memberProductEligibilityRequestBody(request)
    logRequest(requestBody)
    buildBaseRequest(correlationId).withBody(requestBody).withHandler(
      responseHandler(request, None, new RequestType(request.requestDetails.requestType), request.requestDetails.searchType))
  }

  def buildRequestWithHandler(
    request: OptumMemPERequest,
    meta: OptumRequestMetaData
  )(implicit token: Option[OptumAccessTokenResponse]): HandledRqRequest[Either[BaseOptumResponseError, OptumResponseSuccess]] = {
    request match {
      case req: PersonIdMemPERequest =>
        val requestBody: String = neoEncryptionService match {
          case Some(encryptionService) =>
            encryptPayload(personIdRequestBody(meta, req), encryptionService)
          case _ =>
            personIdRequestBody(meta, req)
        }
        logRequest(requestBody)
        buildBaseRequest(meta.correlationId)
          .withBody(requestBody)
          .withHandler(responseHandler(request, Some(meta), request.requestType, meta.searchType.reqString))
      case req: PersonIdContractNumberMemPERequest =>
        val requestBody: String = neoEncryptionService match {
          case Some(encryptionService) =>
            encryptPayload(personIdContractNumberRequestBody(meta, req), encryptionService)
          case _ =>
            personIdContractNumberRequestBody(meta, req)
        }
        logRequest(requestBody)
        buildBaseRequest(meta.correlationId)
          .withBody(requestBody)
          .withHandler(responseHandler(request, Some(meta), request.requestType, meta.searchType.reqString))
      case req: Big5MemPERequest =>
        val requestBody: String = neoEncryptionService match {
          case Some(encryptionService) =>
            encryptPayload(big5RequestBody(meta, req), encryptionService)
          case _ =>
            big5RequestBody(meta, req)
        }
        logRequest(requestBody)
        buildBaseRequest(meta.correlationId)
          .withBody(requestBody)
          .withHandler(responseHandler(request, Some(meta), request.requestType, meta.searchType.reqString))
    }
  }

  def buildBaseRequest(corrId: Option[String])(implicit token: Option[OptumAccessTokenResponse]): RqRequest = {
    val correlationId = corrId match {
      case Some(correlationId) => correlationId
      case _ => CorrelationId.extractCorrelationIdAsString()
    }

    val tokenString = token match {
      case Some(tok) => tok.token
      case _ => "Empty"
    }

    Rq.url(memPeUrl)
      .withMethod(Post)
      .withHeaders(
        (Authorization, s"Bearer ${tokenString}"),
        (Timestamp, s"${DateTimeHelpers.now.getMillis}"),
        (Scope, s"${optumConfig.scope}"),
        (Actor, s"${optumConfig.actor}"),
        (ContentType, ContentTypeJson),
        (OptumCorelationId, correlationId),
        (CorrelationId.HeaderName, correlationId)
      )
      .withRequestTimeout(optumConfig.requestTimeout)
  }

  def responseHandler(
    request: LoggableRequest,
    meta: Option[OptumRequestMetaData],
    requestType: RequestType,
    searchType: String,
    startedAt: Long = Instant.now().toEpochMilli
  ): ResponseHandler[Either[BaseOptumResponseError, OptumResponseSuccess]] = {

    case response: RqResponse =>
      val rsp: RqResponse = neoEncryptionService match {
        case Some(encryptionService) =>
          decryptPayload(response, encryptionService)
        case _ =>
          response
      }
      rsp match {
        case rsp if rsp.status == 200 && !hasError(rsp) =>
          val responseJson = rsp.body.as[JsValue]
          val response = OptumResponseSuccess(rsp.status, rsp.headers, responseJson)
          secureLogger.secureInfo("Optum MemPE Success Response", response)
          emitSuccessStats(requestType, searchType, rsp, startedAt)
          Right(response)

        case rsp if isJson(rsp.body.string) && hasError(rsp) =>
          val responseJson = rsp.body.as[JsValue]
          val errorCode = (responseJson \ ErrorCode2).asOpt[String]
            .getOrElse((responseJson \\ ErrorCode1).head.as[String])
          val errorResponse = OptumResponseAppError(rsp.status, rsp.headers, errorCode, responseJson)
          secureLogger.secureError(errorResponse.getMessage, Seq(meta, request, responseJson))
          emitErrorStats(requestType, searchType, rsp, startedAt)
          Left(errorResponse)

        case rsp =>
          val errorResponse = OptumResponseError(rsp.status, rsp.headers, Some(rsp.body.string))
          secureLogger.secureError(errorResponse.getMessage, Seq(meta, request, rsp.body.string))
          emitErrorStats(requestType, searchType, rsp, startedAt)
          Left(errorResponse)
      }
  }

  def emitErrorStats(requestType: RequestType, searchType: String, rsp: RqResponse, startedAt: Long): Unit =
    emitErrorStatsWithTiming(Seq(requestType.toStatString, searchType), rsp, startedAt)

  def emitSuccessStats(requestType: RequestType, searchType: String, rsp: RqResponse, startedAt: Long): Unit =
    emitSuccessStatsWithTiming(Seq(requestType.toStatString, searchType), rsp, startedAt)

  def logRequest(requestBody: String): Unit = {
    secureLogger.secureInfo(s"Calling Optum MemPE endpoint (${CorrelationId.extractCorrelationIdAsString()}})", requestBody)
  }

  private def encryptPayload(payload: String, encryptionService: NeoEncryptionService): String = {
    val encrypted: String = encryptionService.encryptString(payload)
    Json.toJson(Map(optumConfig.encryptedPayloadKey -> encrypted)).toString
  }

  private def decryptPayload(response: RqResponse, encryptionService: NeoEncryptionService): RqResponse = {
    val status: Int = response.status
    val headers: Map[String, Seq[String]] = response.headers
    val encryptedPayload: JsLookupResult = response.body.as[JsValue] \ optumConfig.encryptedPayloadKey
    val decrypted: String = encryptionService.decryptString(encryptedPayload.getOrElse(
      throw new IllegalArgumentException("Empty JSON Payload")).as[String])
    val jsonPayload = Json.parse(decrypted)
    val rqBody = RqBody(jsonPayload.toString().getBytes)
    RqResponse(status, headers, rqBody)
  }
}
