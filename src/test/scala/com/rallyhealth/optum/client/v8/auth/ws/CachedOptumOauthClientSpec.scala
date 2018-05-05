package com.rallyhealth.optum.client.v8.auth.ws

import com.rallyhealth.optum.client.v8.auth.OAuthClient
import com.rallyhealth.optum.client.v8.auth.model.{OptumAccessTokenRequest, OptumAccessTokenResponse, OptumOAuthException}
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{FlatSpec, LoneElement, Matchers, OptionValues}
import play.api.cache.CacheApi

import scala.concurrent.Future

class CachedOptumOauthClientSpec
  extends FlatSpec
  with Matchers
  with TypeCheckedTripleEquals
  with OptionValues
  with MockitoSugar
  with LoneElement
  with ScalaFutures {
  import scala.concurrent.ExecutionContext.Implicits.global

  class Fixture {
    val tokenReq = OptumAccessTokenRequest("X", "Y")
    val tokenRes = OptumAccessTokenResponse("s8KYRJGTO0rWMy0zz1CCSCwsSesDyDlbNdZoRqVR", 3600)
    val expectedError = OptumOAuthException(401, Some("invalid_client"), None)
    val authClient: OAuthClient = mock[OAuthClient]
    val cacheApi: CacheApi = mock[CacheApi]
    val client = new CachedOptumOauthClient(authClient, cacheApi)
  }

  classOf[CachedOptumOauthClient].getSimpleName should "make an auth request if cache is empty" in {
    val f = new Fixture
    import f._
    when(authClient.getAccessToken).thenReturn(Future.successful(Right(tokenRes)))
    when(cacheApi.get(any())(any())).thenReturn(None)
    whenReady(client.getAccessToken) { resp =>
      resp should be(Right(tokenRes))
      verify(authClient, times(1)).getAccessToken
    }
  }

  classOf[CachedOptumOauthClient].getSimpleName should "make an auth request if cache is expired" in {
    val f = new Fixture
    import f._
    val expected = tokenRes.copy(expiresIn = 4800)
    when(authClient.getAccessToken).thenReturn(Future.successful(Right(expected)))
    when(cacheApi.get[OptumAccessTokenResponse](any())(any())).thenReturn(Some(tokenRes.copy(expiresIn = -3600)))
    whenReady(client.getAccessToken) { resp =>
      resp should be(Right(expected))
      verify(authClient, times(1)).getAccessToken
      verify(cacheApi, times(1)).set(any(), any(), any())
    }
  }

  classOf[CachedOptumOauthClient].getSimpleName should "do not make an auth request if cache is not expired" in {
    val f = new Fixture
    import f._
    val expected = tokenRes.copy(expiresIn = 4800)
    when(cacheApi.get[OptumAccessTokenResponse](any())(any())).thenReturn(Some(expected))
    whenReady(client.getAccessToken) { resp =>
      resp should be(Right(expected))
      verify(authClient, times(0)).getAccessToken
    }
  }

  classOf[CachedOptumOauthClient].getSimpleName should "returns error if underlying returns error" in {
    val f = new Fixture
    import f._
    when(cacheApi.get[OptumAccessTokenResponse](any())(any())).thenReturn(None)
    when(authClient.getAccessToken).thenReturn(Future.successful(Left(expectedError)))
    whenReady(client.getAccessToken) { resp =>
      resp should be(Left(expectedError))
      verify(authClient, times(1)).getAccessToken
    }
  }

}
