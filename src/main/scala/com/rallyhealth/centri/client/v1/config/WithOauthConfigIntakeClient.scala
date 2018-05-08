package com.rallyhealth.centri.client.v1.config



import com.rallyhealth.centri.client.v1.common.model.{BaseCentriResponseError, CentriResponseSuccess}
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
    * @return Either [[BaseCentriResponseError]] or [[CentriResponseSuccess]]
    */
  def postClientConfiguration(request: JsValue)(implicit ec: ExecutionContext): Future[Either[BaseCentriResponseError, CentriResponseSuccess]]
}