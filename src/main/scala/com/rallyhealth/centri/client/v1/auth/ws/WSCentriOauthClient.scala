package com.rallyhealth.centri.client.v1.auth.ws

import com.rallyhealth.centri.client.v1.auth.model.CentriAccessTokenRequest
import com.rallyhealth.centri.client.v1.auth.OAuthClient
import com.rallyhealth.centri.client.v1.common.{HttpUtil, StatsHelper, SecureLogHelper, CentriClientConfig}
import HttpUtil.Method._
import com.rallyhealth.centri.client.v1.common.CentriClientConfig
import com.rallyhealth.centri.client.v1.auth.model.{CentriAccessTokenResponse, CentriOAuthException, CentriAccessTokenRequest}
import com.rallyhealth.centri.client.v1.common.{StatsHelper, SecureLogHelper, CentriClientConfig}
import com.rallyhealth.rq.v1.handler.HandledRqRequest
import com.rallyhealth.rq.v1.{Rq, RqClient, _}
import play.api.libs.json.{JsValue, Json}

import scala.concurrent.Future
import scala.util.control.NonFatal

/**
  * @see [[OAuthClient]]
  */
class WSCentriOauthClient(
                            centriConfig: CentriClientConfig,
                            tokenRequest: CentriAccessTokenRequest,
                            wsClient: RqClient
                        ) extends OAuthClient
    with SecureLogHelper
    with StatsHelper {

    val oauthUrl: String = s"${centriConfig.oauthBaseUrl}${centriConfig.oauthPath}"
    val statPath = Seq("oauth", "v2", "token")

    override def getAccessToken: Future[Either[CentriOAuthException, CentriAccessTokenResponse]] = {
        secureLogger.secureTrace("Invoking Optum OAuth with credentials", tokenRequest)
        val request: HandledRqRequest[Either[CentriOAuthException, CentriAccessTokenResponse]] = buildOAuthRequestWithHandler
        wsClient.execute(request)
    }

    def buildOAuthRequestWithHandler: HandledRqRequest[Either[CentriOAuthException, CentriAccessTokenResponse]] = {
        buildOAuthRequest
            .withHandler[Either[CentriOAuthException, CentriAccessTokenResponse]] {
            case rsp if rsp.status == 200 &&
                (rsp.body.as[JsValue] \ "access_token").asOpt[String].isDefined =>
                val tokenResponse = rsp.body.as[CentriAccessTokenResponse]
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
                val centriOAuthException = CentriOAuthException(rsp.status, errorDetail, Some(rsp.body.string))
                secureLogger.secureError(centriOAuthException.getMessage, Seq(tokenRequest, rsp.body.string))
                emitError(rsp.status)
                Left(centriOAuthException)
        }
    }

    def buildOAuthRequest: RqRequest = {
        Rq.url(oauthUrl)
            .withMethod(Post)
            .withBody(tokenRequest.authBody)
            .withHeaders(tokenRequest.authHeader)
            .withRequestTimeout(centriConfig.requestTimeout)
    }
}
