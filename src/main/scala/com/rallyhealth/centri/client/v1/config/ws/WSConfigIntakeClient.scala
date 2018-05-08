package com.rallyhealth.centri.client.v1.config.ws

import java.time.Instant

import com.rallyhealth.argosy.correlationId.CorrelationId
import com.rallyhealth.centri.client.v1.auth.model.CentriAccessTokenResponse
import com.rallyhealth.centri.client.v1.common.ApiConstant._
import com.rallyhealth.centri.client.v1.common.HttpUtil.Header._
import com.rallyhealth.centri.client.v1.common.HttpUtil.Method._
import com.rallyhealth.centri.client.v1.common.JsonUtil._
import com.rallyhealth.centri.client.v1.common.{CentriClientConfig, _}
import com.rallyhealth.centri.client.v1.common.model.{BaseCentriResponseError, CentriResponseAppError, CentriResponseError, CentriResponseSuccess}
import com.rallyhealth.centri.client.v1.config.ConfigIntakeClient
import com.rallyhealth.centri.client.v1.config.config.ConfigIntakeRallyConfig
import com.rallyhealth.rq.v1.handler.HandledRqRequest
import com.rallyhealth.rq.v1.handler.RqResponseHandlers.ResponseHandler
import com.rallyhealth.rq.v1.{Rq, RqClient, RqRequest, _}
import com.rallyhealth.spartan.v2.datetime.DateTimeHelpers
import play.api.libs.json.{JsValue, Json}

import scala.concurrent.Future

class WSConfigIntakeClient(
                            centriConfig: ConfigIntakeRallyConfig,
                            wsClient: RqClient
                          ) extends ConfigIntakeClient
  with SecureLogHelper
  with StatsHelper {

  final val configIntakeUrl = s"${centriConfig.configIntakeBaseUrl}${centriConfig.configIntakePath}"
  override val statPath: Seq[String] = Seq("optum", "config-intake", "v1")

  /**
    * Submits Client Configuration
    *
    * @param request The Client Config Intake request
    * @param token OAuth2 Access token needed to call Optum generated using client_credentials.
    * @return Either [[BaseCentriResponseError]] or [[CentriResponseSuccess]]
    */
  override def postClientConfiguration(
                                        request: JsValue
                                      )(implicit token: CentriAccessTokenResponse): Future[Either[BaseCentriResponseError, CentriResponseSuccess]] = {
    val wsReq = buildRequestWithHandler(request)
    wsClient.execute(wsReq)
  }

  def buildRequestWithHandler(
                               request: JsValue
                             )(implicit token: CentriAccessTokenResponse): HandledRqRequest[Either[BaseCentriResponseError, CentriResponseSuccess]] = {
    logRequest(Json.stringify(request))
    buildBaseRequest()
      .withBody(Json.stringify(request))
      .withHandler(responseHandler(request))
  }

  def buildBaseRequest()(implicit token: CentriAccessTokenResponse): RqRequest = {
    Rq.url(configIntakeUrl)
      .withMethod(Post)
      .withHeaders(
        (Authorization, s"Bearer ${token.token}"),
        (ContentType, ContentTypeJson),
        (Timestamp, s"${DateTimeHelpers.now.getMillis}"),
        (OptumCorelationId, CorrelationId.extractCorrelationIdAsString())
      )
      .withRequestTimeout(centriConfig.requestTimeout)
  }

  def responseHandler(
                       request: JsValue,
                       startedAt: Long = Instant.now().toEpochMilli
                     ): ResponseHandler[Either[BaseCentriResponseError, CentriResponseSuccess]] = {
    case rsp if rsp.status >= 200 && rsp.status < 300 && !hasError(rsp) =>
      val responseJson = rsp.body.as[JsValue]
      val response = CentriResponseSuccess(rsp.status, rsp.headers, responseJson)
      secureLogger.secureInfo("Optum Config-Intake Success Response", response)
      emitSuccessStats(rsp, startedAt)
      Right(response)

    case rsp if isJson(rsp.body.string) =>
      val responseJson = rsp.body.as[JsValue]
      val errorResponse = CentriResponseAppError(rsp.status, rsp.headers, findErrorCode(responseJson), responseJson)
      secureLogger.secureError("Optum Config-Intake Error Response. " + errorResponse.getMessage, Seq(request, responseJson))
      emitErrorStats(rsp, startedAt)
      Left(errorResponse)

    case rsp =>
      val errorResponse = CentriResponseError(rsp.status, rsp.headers, Some(rsp.body.string))
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
