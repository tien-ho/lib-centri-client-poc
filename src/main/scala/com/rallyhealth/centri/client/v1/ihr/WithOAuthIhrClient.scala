package com.rallyhealth.centri.client.v1.ihr

import com.rallyhealth.centri.client.v1.common.model.{BaseCentriResponseError, CentriResponseSuccess}
import com.rallyhealth.centri.client.v1.ihr.model.{CentriIhrRequest, RequestMetaData}


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
    * @return Either [[BaseCentriResponseError]] or [[CentriResponseSuccess]]
    */
  def findIhrHealthCheck(request: CentriIhrRequest,
                            meta: RequestMetaData
                           )(implicit ec: ExecutionContext): Future[Either[BaseCentriResponseError, CentriResponseSuccess]]


}
