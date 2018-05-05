package com.rallyhealth.centri.client.v1.auth

import com.rallyhealth.centri.client.v1.auth.model.CentriOAuthException
import com.rallyhealth.centri.client.v1.auth.model.{CentriAccessTokenResponse, CentriOAuthException, CentriOAuthResponse}

import scala.concurrent.Future

trait OAuthClient {
    /**
      *
      * @return @see [[CentriOAuthResponse]]
      */
    def getAccessToken: Future[Either[CentriOAuthException, CentriAccessTokenResponse]]
}
