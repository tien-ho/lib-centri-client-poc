package com.rallyhealth.centri.client.v1.ihr.ws

import java.time.Instant

import com.rallyhealth.argosy.correlationId.CorrelationId
import com.rallyhealth.enigma.v4.NeoEncryptionService
import com.rallyhealth.centri.client.v1.auth.model.CentriAccessTokenResponse
import com.rallyhealth.centri.client.v1.common.ApiConstant._
import com.rallyhealth.centri.client.v1.common.HttpUtil.Header._
import com.rallyhealth.centri.client.v1.common.HttpUtil.Method._
import com.rallyhealth.centri.client.v1.common.JsonUtil._
import com.rallyhealth.centri.client.v1.common._
import com.rallyhealth.centri.client.v1.common.model.{BaseCentriResponseError, CentriResponseAppError, CentriResponseError, CentriResponseSuccess}
import com.rallyhealth.centri.client.v1.ihr.IhrClient
import com.rallyhealth.centri.client.v1.ihr.config.IhrConfig
import com.rallyhealth.centri.client.v1.ihr.model.{CentriIhrRequest, IhrHealthCheckRequest, RequestMetaData}
import com.rallyhealth.centri.client.v1.ihr.model._
import com.rallyhealth.centri.client.v1.ihr.ws.IhrRequestBodyBuilder._

import com.rallyhealth.rq.v1.handler.HandledRqRequest
import com.rallyhealth.rq.v1.handler.RqResponseHandlers.ResponseHandler
import com.rallyhealth.rq.v1.{Rq, RqClient, RqRequest, RqResponse, _}
import com.rallyhealth.spartan.v2.datetime.DateTimeHelpers
import play.api.libs.json.{JsLookupResult, JsValue, Json}

import scala.concurrent.Future

class WSIhrClient(ihrConfig: IhrConfig,
                                        wsClient: RqClient,
                                        neoEncryptionService: Option[NeoEncryptionService]
                                      ) extends IhrClient
  with SecureLogHelper
  with StatsHelper {

  final val memPeUrl = s"${ihrConfig.baseUrl}${ihrConfig.ihrPath}"
  override val statPath: Seq[String] = Seq("member", "product", "eligibility", "v2")

  /**
    * Finds member product eligibility with given requested parameters
    *
    * @param request The MemPE API request
    * @param token OAuth2 Access token needed to call Optum generated using client_credentials.
    * @return Either [[BaseCentriResponseError]] or [[CentriResponseSuccess]]
    */
  override def findIhrHealthCheck(request: CentriIhrRequest,
                                  meta: RequestMetaData
                                 )(implicit token: Option[CentriAccessTokenResponse]): Future[Either[BaseCentriResponseError, CentriResponseSuccess]] = {
    val wsReq = buildRequestWithHandler(request, meta)
    wsClient.execute(wsReq)
  }


  def buildRequestWithHandler(request: CentriIhrRequest,
                              meta: RequestMetaData
                             )(implicit token: Option[CentriAccessTokenResponse]): HandledRqRequest[Either[BaseCentriResponseError, CentriResponseSuccess]] = {
    request match {
      case req: IhrHealthCheckRequest =>
        val requestBody: String = neoEncryptionService match {
          case Some(encryptionService) =>
            encryptPayload(healthCheckRequestBody(req, meta), encryptionService)
          case _ =>
            healthCheckRequestBody(req, meta)
        }
        logRequest(requestBody)
        buildBaseRequest(meta.correlationId)
          .withBody(requestBody)
          .withHandler(responseHandler(request, Some(meta)))

    }
  }

  def buildBaseRequest(corrId: Option[String])(implicit token: Option[CentriAccessTokenResponse]): RqRequest = {
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
        (Scope, s"${ihrConfig.scope}"),
        (Actor, s"${ihrConfig.actor}"),
        (ContentType, ContentTypeJson),
        (OptumCorelationId, correlationId),
        (CorrelationId.HeaderName, correlationId)
      )
      .withRequestTimeout(ihrConfig.requestTimeout)
  }

  def responseHandler(request: LoggableRequest,
                       meta: Option[RequestMetaData],
                       startedAt: Long = Instant.now().toEpochMilli
                     ): ResponseHandler[Either[BaseCentriResponseError, CentriResponseSuccess]] = {

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
          val response = CentriResponseSuccess(rsp.status, rsp.headers, responseJson)
          secureLogger.secureInfo("Optum MemPE Success Response", response)
          emitSuccessStats(rsp, startedAt)
          Right(response)

        case rsp if isJson(rsp.body.string) && hasError(rsp) =>
          val responseJson = rsp.body.as[JsValue]
          val errorCode = (responseJson \ ErrorCode2).asOpt[String]
            .getOrElse((responseJson \\ ErrorCode1).head.as[String])
          val errorResponse = CentriResponseAppError(rsp.status, rsp.headers, errorCode, responseJson)
          secureLogger.secureError(errorResponse.getMessage, Seq(meta, request, responseJson))
          emitErrorStats(rsp, startedAt)
          Left(errorResponse)

        case rsp =>
          val errorResponse = CentriResponseError(rsp.status, rsp.headers, Some(rsp.body.string))
          secureLogger.secureError(errorResponse.getMessage, Seq(meta, request, rsp.body.string))
          emitErrorStats(rsp, startedAt)
          Left(errorResponse)
      }
  }

  def emitErrorStats(rsp: RqResponse, startedAt: Long): Unit =
    emitErrorStatsWithTiming(rsp, startedAt)

  def emitSuccessStats(rsp: RqResponse, startedAt: Long): Unit =
    emitSuccessStatsWithTiming(rsp, startedAt)

  def logRequest(requestBody: String): Unit = {
    secureLogger.secureInfo(s"Calling Optum MemPE endpoint (${CorrelationId.extractCorrelationIdAsString()}})", requestBody)
  }

  private def encryptPayload(payload: String, encryptionService: NeoEncryptionService): String = {
    val encrypted: String = encryptionService.encryptString(payload)
    Json.toJson(Map(ihrConfig.encryptedPayloadKey -> encrypted)).toString
  }

  private def decryptPayload(response: RqResponse, encryptionService: NeoEncryptionService): RqResponse = {
    val status: Int = response.status
    val headers: Map[String, Seq[String]] = response.headers
    val encryptedPayload: JsLookupResult = response.body.as[JsValue] \ ihrConfig.encryptedPayloadKey
    val decrypted: String = encryptionService.decryptString(encryptedPayload.getOrElse(
      throw new IllegalArgumentException("Empty JSON Payload")).as[String])
    val jsonPayload = Json.parse(decrypted)
    val rqBody = RqBody(jsonPayload.toString().getBytes)
    RqResponse(status, headers, rqBody)
  }
}
