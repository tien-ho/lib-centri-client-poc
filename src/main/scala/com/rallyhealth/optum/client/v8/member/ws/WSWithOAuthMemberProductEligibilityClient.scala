package com.rallyhealth.optum.client.v8.member.ws

import com.rallyhealth.optum.client.v8.auth.OAuthClient
import com.rallyhealth.optum.client.v8.auth.model.OptumAccessTokenResponse
import com.rallyhealth.optum.client.v8.common.model.{BaseOptumResponseError, OptumResponseSuccess}
import com.rallyhealth.optum.client.v8.member.{MemberProductEligibilityClient, WithOAuthMemberProductEligibilityClient}
import com.rallyhealth.optum.client.v8.member.model.{OptumMemPERequest, OptumRequestMetaData}
import com.rallyhealth.optum.client.v8.member.ws.MemPERequestBodyBuilder.MemberProductEligibilityRequest

import scala.concurrent.{ExecutionContext, Future}

class WSWithOAuthMemberProductEligibilityClient(
  oAuthClient: OAuthClient,
  memPeClient: MemberProductEligibilityClient
) extends WithOAuthMemberProductEligibilityClient {
  /**
    * Finds member product eligibility with given requested parameters by invoking Optum OAuth endpoint for
    * access token and MemPE API endpoint using that access token.
    *
    * @param request The MemPE API request
    * @param meta Meta data needed to call Optum API
    * @return Either [[BaseOptumResponseError]] or [[OptumResponseSuccess]]
    */
  override def findMemberProductEligibility(
    request: OptumMemPERequest,
    meta: OptumRequestMetaData
  )(implicit ec: ExecutionContext): Future[Either[BaseOptumResponseError, OptumResponseSuccess]] = {
    oAuthClient.getAccessToken.flatMap {
      case Left(err) => Future.successful(Left(err))
      case Right(resp: OptumAccessTokenResponse) =>
        memPeClient.findMemberProductEligibility(request, meta)(Some(resp))
    }
  }

  /**
    * Finds member product eligibility with given requested parameters by invoking Optum OAuth endpoint for
    * access token and MemPE API endpoint using that access token.
    *
    * @param request The MemberProductEligibilityRequest
    * @param correlationId The correlationId to pass through to Optum
    * @return Either [[BaseOptumResponseError]] or [[OptumResponseSuccess]]
    */
  override def findByMemberProductEligibilityRequest(
    request: MemberProductEligibilityRequest,
    correlationId: Option[String]
  )(implicit ec: ExecutionContext): Future[Either[BaseOptumResponseError, OptumResponseSuccess]] = {
    oAuthClient.getAccessToken.flatMap {
      case Left(err) => Future.successful(Left(err))
      case Right(resp: OptumAccessTokenResponse) =>
        memPeClient.findByMemberProductEligibilityRequest(request, correlationId)(Some(resp))
    }
  }
}
