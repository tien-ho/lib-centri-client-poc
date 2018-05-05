package com.rallyhealth.optum.client.v8.auth

import com.rallyhealth.optum.client.v8.auth.model.OptumOAuthException
import com.rallyhealth.optum.client.v8.auth.model.{OptumAccessTokenResponse, OptumOAuthException, OptumOAuthResponse}

import scala.concurrent.Future

trait OAuthClient {
  /**
    *
    * @return @see [[OptumOAuthResponse]]
    */
  def getAccessToken: Future[Either[OptumOAuthException, OptumAccessTokenResponse]]
}
