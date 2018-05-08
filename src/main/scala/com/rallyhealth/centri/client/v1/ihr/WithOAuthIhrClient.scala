package com.rallyhealth.centri.client.v1.ihr

import com.rallyhealth.centri.client.v1.common.model.{BaseCentriResponseError, CentriResponseSuccess}
import com.rallyhealth.centri.client.v1.ihr.model.{OptumMemPERequest, OptumRequestMetaData}
import com.rallyhealth.centri.client.v1.ihr.ws.MemPERequestBodyBuilder.MemberProductEligibilityRequest

import scala.concurrent.{ExecutionContext, Future}

/**
  * The client that talks to Optum's member product eligibility endpoint. The same endpoint in Optum can handle many
  * different request types and search types. This interface interacts will all possible combination's on the
  * Optum's endpoint. This interface also expects OAuth token to be handled.
  */
trait WithOAuthIhrClient {

  /**
    * Finds member product eligibility with given requested parameters by invoking Optum OAuth endpoint for
    * access token and MemPE API endpoint using that access token.
    *
    * @param request The MemPE API request
    * @param meta Meta data needed to call Optum API
    * @return Either [[BaseOptumResponseError]] or [[OptumResponseSuccess]]
    */
  def findMemberProductEligibility(
                                    request: OptumMemPERequest,
                                    meta: OptumRequestMetaData
                                  )(implicit ec: ExecutionContext): Future[Either[BaseOptumResponseError, OptumResponseSuccess]]

  /**
    * Finds member product eligibility with given requested parameters
    *
    * @param request The MemberProductEligibilityRequest
    * @param correlationId The correlationId to pass through to Optum
    * @return Either [[BaseOptumResponseError]] or [[OptumResponseSuccess]]
    */
  def findByMemberProductEligibilityRequest(
                                             request: MemberProductEligibilityRequest,
                                             correlationId: Option[String]
                                           )(implicit ec: ExecutionContext): Future[Either[BaseOptumResponseError, OptumResponseSuccess]]
}
