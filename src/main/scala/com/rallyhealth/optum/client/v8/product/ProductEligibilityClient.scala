
package com.rallyhealth.optum.client.v8.product

import com.rallyhealth.optum.client.v8.auth.model.OptumAccessTokenResponse
import com.rallyhealth.optum.client.v8.common.model.{BaseOptumResponseError, OptumResponseSuccess}
import com.rallyhealth.optum.client.v8.product.model.{OptumProductEligibilityRequest, OptumProductEligibilityRequestMeta}

import scala.concurrent.Future

/**
  * The client that talks to Optum's product eligibility endpoint.
  */
trait ProductEligibilityClient {

  /**
    * Finds product eligibility with given requested parameters
    *
    * http://wiki.audaxhealth.com/display/SPECS/Optum+Eligibility+APIs+Overview
    *
    * @param request The Product Eligibility API request
    * @param meta    Meta data needed to call Optum API
    * @param token   OAuth2 Access token needed to call Optum generated using client_credentials.
    * @return Either [[BaseOptumResponseError]] or [[OptumResponseSuccess]]
    */
  def findProductEligibility(
    request: OptumProductEligibilityRequest,
    meta: OptumProductEligibilityRequestMeta
  )(implicit token: OptumAccessTokenResponse): Future[Either[BaseOptumResponseError, OptumResponseSuccess]]
}
