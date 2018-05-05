package com.rallyhealth.optum.client.v8.config

import com.rallyhealth.optum.client.v8.common.model.{BaseOptumResponseError, OptumResponseSuccess}
import play.api.libs.json.JsValue

import scala.concurrent.{ExecutionContext, Future}

/**
  * The client that talks to Optum's Config Intake endpoint. This interface also expects OAuth token to be handled.
  */
trait WithOAuthConfigIntakeClient {

  /**
    * Submits Client Configuration with given requested parameters by invoking Optum OAuth endpoint for
    * access token and Config Intake endpoint using that access token.
    *
    * @param request The Client Config Intake request
    * @return Either [[BaseOptumResponseError]] or [[OptumResponseSuccess]]
    */
  def postClientConfiguration(
    request: JsValue
  )(implicit ec: ExecutionContext): Future[Either[BaseOptumResponseError, OptumResponseSuccess]]
}
