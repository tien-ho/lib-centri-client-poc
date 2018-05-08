package com.rallyhealth.centri.client.v1.config.ws

import com.rallyhealth.centri.client.v1.auth.OAuthClient
import com.rallyhealth.centri.client.v1.auth.model.CentriAccessTokenResponse
import com.rallyhealth.centri.client.v1.common.model.{BaseCentriResponseError, CentriResponseSuccess}
import com.rallyhealth.centri.client.v1.config.{ConfigIntakeClient, WithOAuthConfigIntakeClient}
import play.api.libs.json.JsValue

import scala.concurrent.{ExecutionContext, Future}

class WSWithOAuthConfigIntakeClient(
                                     oAuthClient: OAuthClient,
                                     configIntakeClient: ConfigIntakeClient
                                   ) extends WithOAuthConfigIntakeClient {

  /**
    * Submits Client Configuration with given requested parameters by invoking Optum OAuth endpoint for
    * access token and Config Intake endpoint using that access token.
    *
    * @param request The Client Config Intake request
    * @return Either [[BaseCentriResponseError]] or [[CentriResponseSuccess]]
    */
  override def postClientConfiguration(
                                        request: JsValue
                                      )(implicit ec: ExecutionContext): Future[Either[BaseCentriResponseError, CentriResponseSuccess]] = {
    oAuthClient.getAccessToken.flatMap {
      case Left(err) => Future.successful(Left(err))
      case Right(resp: CentriAccessTokenResponse) =>
        configIntakeClient.postClientConfiguration(request)(resp)
    }
  }
}

