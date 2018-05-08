package com.rallyhealth.centri.client.v1.config


import com.rallyhealth.centri.client.v1.auth.model.CentriAccessTokenResponse
import com.rallyhealth.centri.client.v1.common.model.{BaseCentriResponseError, CentriResponseSuccess}
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
    * @param token OAuth2 Access token needed to call Centri generated using client_credentials.
    * @return Either [[BaseCentriResponseError]] or [[CentriResponseSuccess]]
    */
  def postClientConfiguration(request: JsValue
                             )(implicit token: CentriAccessTokenResponse): Future[Either[BaseCentriResponseError, CentriResponseSuccess]]
}
