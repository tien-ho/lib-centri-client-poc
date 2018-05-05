package com.rallyhealth.optum.client.v8.auth.ws

import com.rallyhealth.optum.client.v8.auth.model.OptumOAuthException
import com.rallyhealth.optum.client.v8.auth.OAuthClient
import com.rallyhealth.optum.client.v8.auth.model.{OptumAccessTokenResponse, OptumOAuthException}
import play.api.cache._

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.language.postfixOps

/**
  * Uses the "expires_in" in the OAuth response on a call to cache the token. Returns the cached token
  * if usable else calls the real Optum endpoint.
  *
  * @param underlying [[OAuthClient]] that finds [[OptumAccessTokenResponse]]
  */
class CachedOptumOauthClient(
  underlying: OAuthClient,
  cache: CacheApi
)(implicit ec: ExecutionContext) extends OAuthClient {
  val cacheKey: String = "optum.oauth.token"

  /**
    *
    * @return @see [[OptumAccessTokenResponse]]
    */
  override def getAccessToken: Future[Either[OptumOAuthException, OptumAccessTokenResponse]] = {
    val lastToken = cache.get[OptumAccessTokenResponse](cacheKey)
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
