package com.rallyhealth.optum.client.v8.config.ws

import com.rallyhealth.optum.client.v8.auth.OAuthClient
import com.rallyhealth.optum.client.v8.auth.model.{OptumAccessTokenResponse, OptumOAuthException}
import com.rallyhealth.optum.client.v8.common.model.OptumResponseSuccess
import com.rallyhealth.optum.client.v8.config.ConfigIntakeClient
import com.rallyhealth.optum.client.v8.config.util.SampleConfigIntakeRequestGenerator
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{FlatSpec, LoneElement, Matchers, OptionValues}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class WSWithOAuthConfigIntakeClientSpec
  extends FlatSpec
  with Matchers
  with TypeCheckedTripleEquals
  with OptionValues
  with MockitoSugar
  with LoneElement
  with ScalaFutures {

  class Fixture {
    val oAuthClient = mock[OAuthClient]
    val configIntakeClient = mock[ConfigIntakeClient]
    val client = new WSWithOAuthConfigIntakeClient(oAuthClient, configIntakeClient)
    var generator = new SampleConfigIntakeRequestGenerator

    implicit val token = OptumAccessTokenResponse("test-token", 3600)
    def setupMockConfig: Map[String, String] = {
      Map(
        "optum.client.baseUrl" -> "http://test",
        "optum.client.actor" -> "TEST",
        "optum.client.scope" -> "read",
        "optum.client.oauthBaseUrl" -> "http://test-auth",
        "optum.client.requesttimeout" -> "60"
      )
    }
  }

  classOf[WSWithOAuthConfigIntakeClient].getSimpleName should "make a config intake post request and handle success response" in {
    val f = new Fixture
    import f._
    val expectedResponse = OptumResponseSuccess(202, Map.empty, generator.responseSuccess)
    when(oAuthClient.getAccessToken).thenReturn(Future.successful(Right(token)))
    when(configIntakeClient.postClientConfiguration(generator.request)(token)).thenReturn(Future.successful(Right(expectedResponse)))
    whenReady(client.postClientConfiguration(generator.request)) { result =>
      assert(result.isRight)
      assert(result.right.get === expectedResponse)
    }
  }

  it should "make a config intake post request and handle error response from OAuth" in {
    val f = new Fixture
    import f._
    val expectedResponse = OptumResponseSuccess(202, Map.empty, generator.responseSuccess)
    val oauthEx = OptumOAuthException(500, None, None)
    when(oAuthClient.getAccessToken).thenReturn(Future.successful(Left(oauthEx)))
    when(configIntakeClient.postClientConfiguration(generator.request)(token)).thenReturn(Future.successful(Right(expectedResponse)))
    whenReady(client.postClientConfiguration(generator.request)) { result =>
      assert(result.isLeft)
      assert(result.left.get === oauthEx)
      verify(configIntakeClient, times(0)).postClientConfiguration(any())(any())
    }
  }

}
