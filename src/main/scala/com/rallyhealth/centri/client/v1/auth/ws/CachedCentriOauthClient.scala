package com.rallyhealth.centri.client.v1.auth.ws

import com.rallyhealth.centri.client.v1.auth.OAuthClient
import com.rallyhealth.centri.client.v1.auth.model.{CentriAccessTokenResponse, CentriOAuthException}
import play.api.cache._

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.language.postfixOps

/**
  * Uses the "expires_in" in the OAuth response on a call to cache the token. Returns the cached token
  * if usable else calls the real Optum endpoint.
  *
  * @param underlying [[OAuthClient]] that finds [[CentriAccessTokenResponse]]
  */
class CachedCentriOauthClient(
                              underlying: OAuthClient,
                              cache: CacheApi
                            )(implicit ec: ExecutionContext) extends OAuthClient {
  val cacheKey: String = "optum.oauth.token"

  /**
    *
    * @return @see [[CentriAccessTokenResponse]]
    */
  override def getAccessToken: Future[Either[CentriOAuthException, CentriAccessTokenResponse]] = {
    val lastToken = cache.get[CentriAccessTokenResponse](cacheKey)
    if (lastToken.exists(_.expiresAt.isAfterNow)) {
      Future.successful(Right(lastToken.get))
    } else {
      underlying.getAccessToken.map {
        case Right(token) =>
          cache.set(cacheKey, token, token.expiresIn seconds)
          Right(token)
        case Left(error) =>
          Left(error)
      }
    }
  }

}
