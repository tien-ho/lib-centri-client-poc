package com.rallyhealth.optum.client.v8.config.ws

import com.rallyhealth.optum.client.v8.auth.OAuthClient
import com.rallyhealth.optum.client.v8.auth.model.OptumAccessTokenResponse
import com.rallyhealth.optum.client.v8.common.model.{BaseOptumResponseError, OptumResponseSuccess}
import com.rallyhealth.optum.client.v8.config.{ConfigIntakeClient, WithOAuthConfigIntakeClient}
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
    * @return Either [[BaseOptumResponseError]] or [[OptumResponseSuccess]]
    */
  override def postClientConfiguration(
    request: JsValue
  )(implicit ec: ExecutionContext): Future[Either[BaseOptumResponseError, OptumResponseSuccess]] = {
    oAuthClient.getAccessToken.flatMap {
      case Left(err) => Future.successful(Left(err))
      case Right(resp: OptumAccessTokenResponse) =>
        configIntakeClient.postClientConfiguration(request)(resp)
    }
  }
}
