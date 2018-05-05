package com.rallyhealth.optum.client.v8.product

import com.rallyhealth.optum.client.v8.common.model.{BaseOptumResponseError, OptumResponseSuccess}
import com.rallyhealth.optum.client.v8.product.model.{OptumProductEligibilityRequest, OptumProductEligibilityRequestMeta}

import scala.concurrent.{ExecutionContext, Future}

/**
  * The client that talks to Optum's product eligibility endpoint. This interface interacts will all possible combination's on the
  * Optum's endpoint. This interface also expects OAuth token to be handled.
  */
trait WithOAuthProductEligibilityClient {

  /**
    * Finds member product eligibility with given requested parameters by invoking Optum OAuth endpoint for
    * access token and ProductEligibility API endpoint using that access token.
    *
    * @param request The ProductEligibility API request
    * @param meta    Meta data needed to call Optum API
    * @return Either [[BaseOptumResponseError]] or [[OptumResponseSuccess]]
    */
  def findProductEligibility(
    request: OptumProductEligibilityRequest,
    meta: OptumProductEligibilityRequestMeta
  )(implicit ec: ExecutionContext): Future[Either[BaseOptumResponseError, OptumResponseSuccess]]
}
