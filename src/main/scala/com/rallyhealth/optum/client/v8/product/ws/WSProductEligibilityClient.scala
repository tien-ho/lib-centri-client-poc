package com.rallyhealth.optum.client.v8.product.ws

import java.time.Instant

import com.rallyhealth.argosy.correlationId.CorrelationId
import com.rallyhealth.optum.client.v8.auth.model.OptumAccessTokenResponse
import com.rallyhealth.optum.client.v8.common.ApiConstant._
import com.rallyhealth.optum.client.v8.common.HttpUtil.Header._
import com.rallyhealth.optum.client.v8.common.HttpUtil.Method._
import com.rallyhealth.optum.client.v8.common.model.{BaseOptumResponseError, OptumResponseAppError, OptumResponseError, OptumResponseSuccess}
import com.rallyhealth.optum.client.v8.common.{OptumClientConfig, _}
import com.rallyhealth.optum.client.v8.product.ProductEligibilityClient
import com.rallyhealth.optum.client.v8.product.model._
import com.rallyhealth.rq.v1.{Rq, RqClient, RqRequest, RqResponse}
import com.rallyhealth.rq.v1.handler.HandledRqRequest
import com.rallyhealth.rq.v1.handler.RqResponseHandlers.ResponseHandler
import com.rallyhealth.spartan.v2.datetime.DateTimeHelpers
import play.api.libs.json.{JsObject, JsValue, Json}
import com.rallyhealth.rq.v1._
import com.rallyhealth.optum.client.v8.common.JsonUtil._
import com.rallyhealth.optum.client.v8.product.config.ProductEligibilityConfig

import scala.concurrent.Future
import scala.util.Try

class WSProductEligibilityClient(
  optumConfig: ProductEligibilityConfig,
  wsClient: RqClient
) extends ProductEligibilityClient
  with SecureLogHelper
  with StatsHelper {

  final val productEligibilityUrl = s"${optumConfig.baseUrl}${optumConfig.productEligibilityPath}"
  override val statPath: Seq[String] = Seq("optum", "product", "eligibility", "v1")

  /**
    * Finds product eligibility with given requested parameters
    *
    * @param request The Product Eligibility API request
    * @param meta    Meta data needed to call Optum API
    * @param token   OAuth2 Access token needed to call Optum generated using client_credentials.
    * @return Either [[BaseOptumResponseError]] or [[com.rallyhealth.optum.client.v8.common.model.OptumResponseSuccess]]
    */
  override def findProductEligibility(
    request: OptumProductEligibilityRequest,
    meta: OptumProductEligibilityRequestMeta
  )(implicit token: OptumAccessTokenResponse): Future[Either[BaseOptumResponseError, OptumResponseSuccess]] = {
    val wsReq = buildRequestWithHandler(request, meta)
    wsClient.execute(wsReq)
  }

  def buildRequestWithHandler(
    request: OptumProductEligibilityRequest,
    meta: OptumProductEligibilityRequestMeta
  )(implicit token: OptumAccessTokenResponse): HandledRqRequest[Either[BaseOptumResponseError, OptumResponseSuccess]] =
    {
      val requestBody = ProductEligibilityRequestBodyBuilder.buildJsonRequestBody(request, meta)
      logRequest(requestBody)
      buildBaseRequest()
        .withBody(requestBody)
        .withHandler(responseHandler(request, meta))
    }

  def buildBaseRequest()(implicit token: OptumAccessTokenResponse): RqRequest = {
    Rq.url(productEligibilityUrl)
      .withMethod(Post)
      .withHeaders(
        (Authorization, s"Bearer ${token.token}"),
        (Timestamp, s"${DateTimeHelpers.now.getMillis}"),
        (Scope, s"${optumConfig.scope}"),
        (Actor, s"${optumConfig.actor}"),
        (ContentType, ContentTypeJson),
        (OptumCorelationId, CorrelationId.extractCorrelationIdAsString())
      )
      .withRequestTimeout(optumConfig.requestTimeout)
  }

  def responseHandler(
    request: OptumProductEligibilityRequest,
    meta: OptumProductEligibilityRequestMeta,
    startedAt: Long = Instant.now().toEpochMilli
  ): ResponseHandler[Either[BaseOptumResponseError, OptumResponseSuccess]] = {
    case rsp if rsp.status == 200 && !hasError(rsp) =>
      val responseJson = rsp.body.as[JsValue]
      val response = OptumResponseSuccess(rsp.status, rsp.headers, responseJson)
      secureLogger.secureInfo("Optum ProductEligibility Success Response", response)
      emitSuccessStats(request.getClass.getName, rsp, startedAt)
      Right(response)

    case rsp if isJson(rsp.body.string) && hasError(rsp) =>
      val responseJson = rsp.body.as[JsValue]
      val errorCode = (responseJson \ ErrorCode2).asOpt[String]
        .getOrElse((responseJson \\ ErrorCode1).head.as[String])
      val errorResponse = OptumResponseAppError(rsp.status, rsp.headers, errorCode, responseJson)
      secureLogger.secureError(errorResponse.getMessage, Seq(meta, request, responseJson))
      emitErrorStats(request.getClass.getName, rsp, startedAt)
      Left(errorResponse)

    case rsp =>
      val errorResponse = OptumResponseError(rsp.status, rsp.headers, Some(rsp.body.string))
      secureLogger.secureError(errorResponse.getMessage, Seq(meta, request, rsp.body.string))
      emitErrorStats(request.getClass.getName, rsp, startedAt)
      Left(errorResponse)
  }

  def emitErrorStats(requestType: String, rsp: RqResponse, startedAt: Long): Unit =
    emitErrorStatsWithTiming(Seq(requestType), rsp, startedAt)

  def emitSuccessStats(requestType: String, rsp: RqResponse, startedAt: Long): Unit =
    emitSuccessStatsWithTiming(Seq(requestType), rsp, startedAt)

  def logRequest(requestBody: String): Unit = {
    secureLogger
      .secureInfo(s"Calling Optum Product Eligibility endpoint (${CorrelationId.extractCorrelationIdAsString()}})", requestBody)
  }
}
