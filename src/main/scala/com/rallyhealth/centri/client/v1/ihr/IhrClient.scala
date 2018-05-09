package com.rallyhealth.centri.client.v1.ihr

import com.rallyhealth.centri.client.v1.auth.model.CentriAccessTokenResponse
import com.rallyhealth.centri.client.v1.common.model.{BaseCentriResponseError, CentriResponseSuccess}
import com.rallyhealth.centri.client.v1.ihr.model.{CentriIhrRequest, RequestMetaData}
import com.rallyhealth.centri.client.v1.ihr.ws.IhrRequestBodyBuilder._

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
    * @return Either [[BaseCentriResponseError]] or [[CentriResponseSuccess]]
    */
  def findIhrHealthCheck(request: CentriIhrRequest,
                         meta: RequestMetaData
                        )(implicit token: Option[CentriAccessTokenResponse]): Future[Either[BaseCentriResponseError, CentriResponseSuccess]]



}
