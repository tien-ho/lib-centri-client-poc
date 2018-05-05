package com.rallyhealth.optum.client.v8.product.ws

import com.rallyhealth.optum.client.v8.auth.OAuthClient
import com.rallyhealth.optum.client.v8.auth.model.{OptumAccessTokenResponse, OptumOAuthException}
import com.rallyhealth.optum.client.v8.common.model.OptumResponseSuccess
import com.rallyhealth.optum.client.v8.product.model._
import org.joda.time.DateTime
import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{FlatSpec, LoneElement, Matchers, OptionValues}
import org.scalatest.mockito.MockitoSugar
import play.api.libs.json.Json
import com.rallyhealth.optum.client.v8.member.util.SampleResponseGenerator
import com.rallyhealth.optum.client.v8.product.ProductEligibilityClient
import com.rallyhealth.optum.client.v8.product.util.SampleProductEligibilityResponse
import org.mockito.Matchers._
import org.mockito.Mockito._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class WSWithOAuthProductEligibilityClientSpec
  extends FlatSpec
  with Matchers
  with TypeCheckedTripleEquals
  with OptionValues
  with MockitoSugar
  with LoneElement
  with ScalaFutures {

  class Fixture {
    val oAuthClient = mock[OAuthClient]
    val productEligibilityClient = mock[ProductEligibilityClient]
    val client = new WSWithOAuthProductEligibilityClient(oAuthClient, productEligibilityClient)

    val optumProductEligibilityRequestWithPopulationIdAndRelationshipCode = OptumProductEligibilityRequestWithPopulationIdAndRelationshipCode(
      populationId = "POP317",
      contractNumber = "0191690",
      relationshipCode = "EE",
      dateOfBirth = DateTime.parse("1970-06-14T00:00:00Z")
    )

    implicit val token = OptumAccessTokenResponse("test-token", 3600)
    val requestMeta = OptumProductEligibilityRequestMeta(applicationName = "RALLY", transactionId = "RALLY022020179", eventDate = Some(DateTime.parse("2017-05-02T00:00:00Z")))
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

  classOf[WSWithOAuthProductEligibilityClient].getSimpleName should "make a personId request and handle success response" in {
    val f = new Fixture
    import f._
    val expectedResponse = OptumResponseSuccess(200, Map.empty, Json.parse(SampleProductEligibilityResponse.sampleResponseWithPATCHProducts))
    when(oAuthClient.getAccessToken).thenReturn(Future.successful(Right(token)))
    when(productEligibilityClient.findProductEligibility(optumProductEligibilityRequestWithPopulationIdAndRelationshipCode, requestMeta)(token)).thenReturn(Future.successful(Right(expectedResponse)))
    whenReady(client.findProductEligibility(optumProductEligibilityRequestWithPopulationIdAndRelationshipCode, requestMeta)) { result =>
      assert(result.isRight)
      assert(result.right.get === expectedResponse)
    }
  }

  it should "make a personId request and handle error response from OAuth" in {
    val f = new Fixture
    import f._
    val expectedResponse = OptumResponseSuccess(200, Map.empty, Json.parse(SampleProductEligibilityResponse.sampleResponseWithPATCHProducts))
    val oauthEx = OptumOAuthException(500, None, None)
    when(oAuthClient.getAccessToken).thenReturn(Future.successful(Left(oauthEx)))
    when(productEligibilityClient.findProductEligibility(optumProductEligibilityRequestWithPopulationIdAndRelationshipCode, requestMeta)(token)).thenReturn(Future.successful(Right(expectedResponse)))
    whenReady(client.findProductEligibility(optumProductEligibilityRequestWithPopulationIdAndRelationshipCode, requestMeta)) { result =>
      assert(result.isLeft)
      assert(result.left.get === oauthEx)
      verify(productEligibilityClient, times(0)).findProductEligibility(any(), any())(any())
    }
  }

}
