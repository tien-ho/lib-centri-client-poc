package com.rallyhealth.optum.client.v8.auth.ws

import com.rallyhealth.optum.client.v8.auth.model.OptumAccessTokenRequest
import com.rallyhealth.optum.client.v8.auth.OAuthClient
import com.rallyhealth.optum.client.v8.common.{HttpUtil, StatsHelper, SecureLogHelper, OptumClientConfig}
import HttpUtil.Method._
import com.rallyhealth.optum.client.v8.common.OptumClientConfig
import com.rallyhealth.optum.client.v8.auth.model.{OptumAccessTokenResponse, OptumOAuthException, OptumAccessTokenRequest}
import com.rallyhealth.optum.client.v8.common.{StatsHelper, SecureLogHelper, OptumClientConfig}
import com.rallyhealth.rq.v1.handler.HandledRqRequest
import com.rallyhealth.rq.v1.{Rq, RqClient, _}
import play.api.libs.json.{JsValue, Json}

import scala.concurrent.Future
import scala.util.control.NonFatal

/**
  * @see [[OAuthClient]]
  */
class WSOptumOauthClient(
  optumConfig: OptumClientConfig,
  tokenRequest: OptumAccessTokenRequest,
  wsClient: RqClient
) extends OAuthClient
  with SecureLogHelper
  with StatsHelper {

  val oauthUrl: String = s"${optumConfig.oauthBaseUrl}${optumConfig.oauthPath}"
  val statPath = Seq("oauth", "v2", "token")

  override def getAccessToken: Future[Either[OptumOAuthException, OptumAccessTokenResponse]] = {
    secureLogger.secureTrace("Invoking Optum OAuth with credentials", tokenRequest)
    val request: HandledRqRequest[Either[OptumOAuthException, OptumAccessTokenResponse]] = buildOAuthRequestWithHandler
    wsClient.execute(request)
  }

  def buildOAuthRequestWithHandler: HandledRqRequest[Either[OptumOAuthException, OptumAccessTokenResponse]] = {
    buildOAuthRequest
      .withHandler[Either[OptumOAuthException, OptumAccessTokenResponse]] {
        case rsp if rsp.status == 200 &&
          (rsp.body.as[JsValue] \ "access_token").asOpt[String].isDefined =>
          val tokenResponse = rsp.body.as[OptumAccessTokenResponse]
          secureLogger.secureInfo("Optum OAuth Success Response", tokenResponse)
          emitSuccess(rsp.status)
          Right(tokenResponse)
        case rsp =>
          val errorDetail = try {
            val errorJson = Json.parse(rsp.body.string)
            (errorJson \ "error").asOpt[String]
          } catch {
            case NonFatal(e) =>
              None
          }
          val optumOAuthException = OptumOAuthException(rsp.status, errorDetail, Some(rsp.body.string))
          secureLogger.secureError(optumOAuthException.getMessage, Seq(tokenRequest, rsp.body.string))
          emitError(rsp.status)
          Left(optumOAuthException)
      }
  }

  def buildOAuthRequest: RqRequest = {
    Rq.url(oauthUrl)
      .withMethod(Post)
      .withBody(tokenRequest.authBody)
      .withHeaders(tokenRequest.authHeader)
      .withRequestTimeout(optumConfig.requestTimeout)
  }
}
