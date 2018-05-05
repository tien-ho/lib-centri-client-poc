package com.rallyhealth.optum.client.v8.product.ws

import com.rallyhealth.optum.client.v8.auth.OAuthClient
import com.rallyhealth.optum.client.v8.auth.model.OptumAccessTokenResponse
import com.rallyhealth.optum.client.v8.common.model.{BaseOptumResponseError, OptumResponseSuccess}
import com.rallyhealth.optum.client.v8.product.model.{OptumProductEligibilityRequest, OptumProductEligibilityRequestMeta}
import com.rallyhealth.optum.client.v8.product.{ProductEligibilityClient, WithOAuthProductEligibilityClient}

import scala.concurrent.{ExecutionContext, Future}

class WSWithOAuthProductEligibilityClient(
  oAuthClient: OAuthClient,
  productEligibilityClient: ProductEligibilityClient
) extends WithOAuthProductEligibilityClient {

  /**
    * Finds product eligibility with given requested parameters by invoking Optum OAuth endpoint for
    * access token and Product Eligibility API endpoint using that access token.
    *
    * @param request The Product Eligibility API request
    * @param meta    Meta data needed to call Optum API
    * @return Either [[BaseOptumResponseError]] or [[OptumResponseSuccess]]
    */
  def findProductEligibility(
    request: OptumProductEligibilityRequest,
    meta: OptumProductEligibilityRequestMeta
  )(implicit ec: ExecutionContext): Future[Either[BaseOptumResponseError, OptumResponseSuccess]] = {
    oAuthClient.getAccessToken.flatMap {
      case Left(err) => Future.successful(Left(err))
      case Right(resp: OptumAccessTokenResponse) =>
        productEligibilityClient.findProductEligibility(request, meta)(resp)
    }
  }
}
