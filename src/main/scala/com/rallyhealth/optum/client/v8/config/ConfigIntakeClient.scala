package com.rallyhealth.optum.client.v8.config

import com.rallyhealth.optum.client.v8.auth.model.OptumAccessTokenResponse
import com.rallyhealth.optum.client.v8.common.model.{BaseOptumResponseError, OptumResponseSuccess}
import play.api.libs.json.JsValue

import scala.concurrent.Future

/**
  * The client that talks to Optum's Config Intake endpoint.
  */
trait ConfigIntakeClient {

  /**
    * Submits Client Configuration
    *
    * @param request The Client Config Intake request
    * @param token OAuth2 Access token needed to call Optum generated using client_credentials.
    * @return Either [[BaseOptumResponseError]] or [[OptumResponseSuccess]]
    */
  def postClientConfiguration(
    request: JsValue
  )(implicit token: OptumAccessTokenResponse): Future[Either[BaseOptumResponseError, OptumResponseSuccess]]
}
