package com.rallyhealth.optum.client.v8.member.ws

import com.rallyhealth.optum.client.v8.auth.OAuthClient
import com.rallyhealth.optum.client.v8.auth.model.{OptumAccessTokenResponse, OptumOAuthException}
import com.rallyhealth.optum.client.v8.common.model.OptumResponseSuccess
import com.rallyhealth.optum.client.v8.member.MemberProductEligibilityClient
import com.rallyhealth.optum.client.v8.member.model._
import com.rallyhealth.optum.client.v8.member.util.SampleResponseGenerator
import com.rallyhealth.optum.client.v8.member.ws.MemPERequestBodyBuilder._
import com.rallyhealth.spartan.v2.datetime.DateTimeHelpers
import org.mockito.Mockito._
import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{FlatSpec, LoneElement, Matchers, OptionValues}
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class WSWithOAuthMemberProductEligibilityClientSpec
  extends FlatSpec
  with Matchers
  with TypeCheckedTripleEquals
  with OptionValues
  with MockitoSugar
  with LoneElement
  with ScalaFutures {

  class Fixture {
    val oAuthClient = mock[OAuthClient]
    val memPeClient = mock[MemberProductEligibilityClient]
    val client = new WSWithOAuthMemberProductEligibilityClient(oAuthClient, memPeClient)
    val reqPid = PersonIdMemPERequest("454234")
    val requestHeader = RequestHeader("test", "testid")
    val filteringAttributes = FilteringAttributes(false, true)
    val requestDetails = RequestDetails("PERSONID_PLUS_CONTRACTNUMBER", "ALL")
    val idSet = IdSet(1241123)
    val consumerDetails = ConsumerDetails

    val eligibilityRequest = MemberProductEligibilityRequest(requestHeader, filteringAttributes, requestDetails, Some(idSet), None)

    implicit val token = OptumAccessTokenResponse("test-token", 3600)
    implicit val maybeToken = Some(token)
    val meta = OptumRequestMetaData("test", DateTimeHelpers.now.getMillis.toString, All, None, None, FilteringRules(true, false))
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

  classOf[WSWithOAuthMemberProductEligibilityClient].getSimpleName should "make a personId request and handle success response" in {
    val f = new Fixture
    import f._
    val expectedResponse = OptumResponseSuccess(200, Map.empty, Json.parse(SampleResponseGenerator.memPEResponseAll))
    when(oAuthClient.getAccessToken).thenReturn(Future.successful(Right(token)))
    when(memPeClient.findMemberProductEligibility(reqPid, meta)(maybeToken)).thenReturn(Future.successful(Right(expectedResponse)))
    whenReady(client.findMemberProductEligibility(reqPid, meta)) { result =>
      assert(result.isRight)
      assert(result.right.get === expectedResponse)
    }
  }

  classOf[WSWithOAuthMemberProductEligibilityClient].getSimpleName should "make a personId request with MPER and handle success response" in {
    val f = new Fixture
    import f._
    val expectedResponse = OptumResponseSuccess(200, Map.empty, Json.parse(SampleResponseGenerator.memPEResponseAll))
    when(oAuthClient.getAccessToken).thenReturn(Future.successful(Right(token)))
    when(memPeClient.findByMemberProductEligibilityRequest(eligibilityRequest, Some("corrId"))(maybeToken)).thenReturn(Future.successful(Right(expectedResponse)))
    whenReady(client.findByMemberProductEligibilityRequest(eligibilityRequest, Some("corrId"))) { result =>
      assert(result.isRight)
      assert(result.right.get === expectedResponse)
    }
  }

  it should "make a personId request and handle error response from OAuth" in {
    val f = new Fixture
    import f._
    val expectedResponse = OptumResponseSuccess(200, Map.empty, Json.parse(SampleResponseGenerator.memPEResponseAll))
    val oauthEx = OptumOAuthException(500, None, None)
    when(oAuthClient.getAccessToken).thenReturn(Future.successful(Left(oauthEx)))
    when(memPeClient.findMemberProductEligibility(reqPid, meta)(maybeToken)).thenReturn(Future.successful(Right(expectedResponse)))
    whenReady(client.findMemberProductEligibility(reqPid, meta)) { result =>
      assert(result.isLeft)
      assert(result.left.get === oauthEx)
      verify(memPeClient, times(0)).findMemberProductEligibility(reqPid, meta)(maybeToken)
    }
  }

}
