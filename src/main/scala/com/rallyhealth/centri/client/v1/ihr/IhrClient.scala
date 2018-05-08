package com.rallyhealth.centri.client.v1.ihr

import com.rallyhealth.centri.client.v1.auth.model.CentriAccessTokenResponse
import com.rallyhealth.centri.client.v1.common.model.{BaseCentriResponseError, CentriResponseSuccess}
import com.rallyhealth.centri.client.v1.ihr.model.{IhrRequest}
import com.rallyhealth.centri.client.v1.ihr.ws.IhrRequestBodyBuilder.

import scala.concurrent.Future

/**
  * The client that talks to Optum's member product eligibility endpoint. The same endpoint in Optum can handle many
  * different request types and search types. This interface interacts will all possible combination's on the
  * Optum's endpoint.
  */
trait IhrClient {

  /**
    * Finds member product eligibility with given requested parameters
    *
    * @param request The MemPE API request
    * @param meta Meta data needed to call Optum API
    * @param token OAuth2 Access token needed to call Optum generated using client_credentials.
    * @return Either [[BaseCentriResponseError]] or [[OptumResponseSuccess]]
    */
  def findHealthCheck(
                                    request: OptumMemPERequest,
                                    meta: OptumRequestMetaData
                                  )(implicit token: Option[OptumAccessTokenResponse]): Future[Either[BaseOptumResponseError, OptumResponseSuccess]]

  /**
    * Finds member product eligibility with given requested parameters
    *
    * @param request The MemberProductEligibilityRequest
    * @param correlationId An optional correlationId to include in the header to Optum
    * @param token OAuth2 Access token needed to call Optum generated using client_credentials.
    * @return Either [[BaseOptumResponseError]] or [[OptumResponseSuccess]]
    */
  def findByMemberProductEligibilityRequest(
                                             request: MemberProductEligibilityRequest,
                                             correlationId: Option[String]
                                           )(implicit token: Option[OptumAccessTokenResponse]): Future[Either[BaseOptumResponseError, OptumResponseSuccess]]
}
