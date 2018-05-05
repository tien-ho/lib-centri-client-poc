package com.rallyhealth.optum.client.v8.config.ws

import java.time.Instant

import com.rallyhealth.argosy.correlationId.CorrelationId
import com.rallyhealth.optum.client.v8.auth.model.OptumAccessTokenResponse
import com.rallyhealth.optum.client.v8.common.ApiConstant._
import com.rallyhealth.optum.client.v8.common.HttpUtil.Header._
import com.rallyhealth.optum.client.v8.common.HttpUtil.Method._
import com.rallyhealth.optum.client.v8.common.JsonUtil._
import com.rallyhealth.optum.client.v8.common.{OptumClientConfig, _}
import com.rallyhealth.optum.client.v8.common.model.{BaseOptumResponseError, OptumResponseAppError, OptumResponseError, OptumResponseSuccess}
import com.rallyhealth.optum.client.v8.config.ConfigIntakeClient
import com.rallyhealth.optum.client.v8.config.config.ConfigIntakeRallyConfig
import com.rallyhealth.rq.v1.handler.HandledRqRequest
import com.rallyhealth.rq.v1.handler.RqResponseHandlers.ResponseHandler
import com.rallyhealth.rq.v1.{Rq, RqClient, RqRequest, _}
import com.rallyhealth.spartan.v2.datetime.DateTimeHelpers
import play.api.libs.json.{JsValue, Json}

import scala.concurrent.Future

class WSConfigIntakeClient(
  optumConfig: ConfigIntakeRallyConfig,
  wsClient: RqClient
) extends ConfigIntakeClient
  with SecureLogHelper
  with StatsHelper {

  final val configIntakeUrl = s"${optumConfig.configIntakeBaseUrl}${optumConfig.configIntakePath}"
  override val statPath: Seq[String] = Seq("optum", "config-intake", "v1")

  /**
    * Submits Client Configuration
    *
    * @param request The Client Config Intake request
    * @param token OAuth2 Access token needed to call Optum generated using client_credentials.
    * @return Either [[BaseOptumResponseError]] or [[OptumResponseSuccess]]
    */
  override def postClientConfiguration(
    request: JsValue
  )(implicit token: OptumAccessTokenResponse): Future[Either[BaseOptumResponseError, OptumResponseSuccess]] = {
    val wsReq = buildRequestWithHandler(request)
    wsClient.execute(wsReq)
  }

  def buildRequestWithHandler(
    request: JsValue
  )(implicit token: OptumAccessTokenResponse): HandledRqRequest[Either[BaseOptumResponseError, OptumResponseSuccess]] = {
    logRequest(Json.stringify(request))
    buildBaseRequest()
      .withBody(Json.stringify(request))
      .withHandler(responseHandler(request))
  }

  def buildBaseRequest()(implicit token: OptumAccessTokenResponse): RqRequest = {
    Rq.url(configIntakeUrl)
      .withMethod(Post)
      .withHeaders(
        (Authorization, s"Bearer ${token.token}"),
        (ContentType, ContentTypeJson),
        (Timestamp, s"${DateTimeHelpers.now.getMillis}"),
        (OptumCorelationId, CorrelationId.extractCorrelationIdAsString())
      )
      .withRequestTimeout(optumConfig.requestTimeout)
  }

  def responseHandler(
    request: JsValue,
    startedAt: Long = Instant.now().toEpochMilli
  ): ResponseHandler[Either[BaseOptumResponseError, OptumResponseSuccess]] = {
    case rsp if rsp.status >= 200 && rsp.status < 300 && !hasError(rsp) =>
      val responseJson = rsp.body.as[JsValue]
      val response = OptumResponseSuccess(rsp.status, rsp.headers, responseJson)
      secureLogger.secureInfo("Optum Config-Intake Success Response", response)
      emitSuccessStats(rsp, startedAt)
      Right(response)

    case rsp if isJson(rsp.body.string) =>
      val responseJson = rsp.body.as[JsValue]
      val errorResponse = OptumResponseAppError(rsp.status, rsp.headers, findErrorCode(responseJson), responseJson)
      secureLogger.secureError("Optum Config-Intake Error Response. " + errorResponse.getMessage, Seq(request, responseJson))
      emitErrorStats(rsp, startedAt)
      Left(errorResponse)

    case rsp =>
      val errorResponse = OptumResponseError(rsp.status, rsp.headers, Some(rsp.body.string))
      secureLogger.secureError("Optum Config-Intake Error Response. " + errorResponse.getMessage, Seq(request, rsp.body.string))
      emitErrorStats(rsp, startedAt)
      Left(errorResponse)
  }

  def emitErrorStats(rsp: RqResponse, startedAt: Long): Unit =
    emitErrorStatsWithTiming(statPath, rsp, startedAt)

  def emitSuccessStats(rsp: RqResponse, startedAt: Long): Unit =
    emitSuccessStatsWithTiming(statPath, rsp, startedAt)

  def logRequest(requestBody: String): Unit = {
    secureLogger.secureInfo(s"Calling Optum Config-Intake endpoint, corId=${CorrelationId.extractCorrelationIdAsString()}", requestBody)
  }

  private def findErrorCode(body: JsValue) = {
    val errors = (body \ ErrorDetails).asOpt[Seq[Map[String, String]]]
    errors match {
      case Some(seq) => seq.headOption match {
        case Some(map) => map.getOrElse(ErrorDetailsCode, "")
        case None => ""
      }
      case None => ""
    }
  }
}
