package com.rallyhealth.optum.client.v8.auth.ws

import com.rallyhealth.optum.client.v8.auth.model.{OptumAccessTokenRequest, OptumAccessTokenResponse, OptumOAuthException}
import com.rallyhealth.optum.client.v8.common.{OptumClientConfig, OptumClientRallyConfig}
import com.rallyhealth.rq.v1.handler.HandledRqRequest
import com.rallyhealth.rq.v1.{MemoryRqResponse, RqBody, RqClient}
import com.rallyhealth.spartan.v2.config.{MemoryRallyConfig, RallyConfig}
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{FlatSpec, LoneElement, Matchers, OptionValues}

import scala.concurrent.Future

class WSOptumOauthClientSpec
  extends FlatSpec
  with Matchers
  with TypeCheckedTripleEquals
  with OptionValues
  with MockitoSugar
  with LoneElement
  with ScalaFutures {

  class Fixture {
    val rconfig: RallyConfig = new MemoryRallyConfig(setupMockConfig)
    val oconfig: OptumClientConfig = new OptumClientRallyConfig(rconfig)
    val rqClient: RqClient = mock[RqClient]
    val tokenReq = OptumAccessTokenRequest("X", "Y")
    val client = new WSOptumOauthClient(oconfig, tokenReq, rqClient)

    def setupMockConfig: Map[String, String] = {
      Map(
        "optum.client.baseUrl" -> "http://test/",
        "optum.client.actor" -> "TEST",
        "optum.client.scope" -> "read",
        "optum.client.oauthBaseUrl" -> "http://test-auth",
        "optum.client.requesttimeout" -> "60"
      )
    }
  }

  classOf[WSOptumOauthClient].getSimpleName should "make an auth request and handle success response" in {
    val f = new Fixture
    import f._
    val responseBody = RqBody(
      """{
          |    "access_token": "s8KYRJGTO0rWMy0zz1CCSCwsSesDyDlbNdZoRqVR",
          |    "token_type": "bearer",
          |    "expires": 1393350569,
          |    "expires_in": 3600
          |}
        """.stripMargin.getBytes
    )
    val rqResponse = MemoryRqResponse(200, body = responseBody)
    val expectedToken = OptumAccessTokenResponse("s8KYRJGTO0rWMy0zz1CCSCwsSesDyDlbNdZoRqVR", 3600)

    val handler: HandledRqRequest[Either[OptumOAuthException, OptumAccessTokenResponse]] = client.buildOAuthRequestWithHandler
    assert(handler.request.url == "http://test-auth/oauth/v2/token")
    assert(handler.request.method == "POST")
    assert(handler.request.headers.get("Authorization").value.loneElement === "Basic WDpZ")
    assert(handler.request.headers.get("Content-Type").value.loneElement contains "application/x-www-form-urlencoded")
    assert(handler.request.body.string === "grant_type=client_credentials")
    val actualResponse: Either[OptumOAuthException, OptumAccessTokenResponse] = handler.handler(rqResponse)
    assert(actualResponse === Right(expectedToken))
  }

  it should "make an auth request and handle unauth response" in {
    val f = new Fixture
    import f._
    val responseBody = RqBody(
      """{
          |  "error": "invalid_client",
          |  "error_description": "Client authentication failed"
          |}
        """.stripMargin.getBytes
    )
    val rqResponse = MemoryRqResponse(401, body = responseBody)
    val expectedError = OptumOAuthException(401, Some("invalid_client"), None)

    val actualResponse: Either[OptumOAuthException, OptumAccessTokenResponse] = client.buildOAuthRequestWithHandler.handler(rqResponse)
    assert(actualResponse.left.get.copy(body = None) === expectedError)
  }

  it should "make an auth request and handle invalid response" in {
    val f = new Fixture
    import f._

    val responseBody = RqBody(
      """{
          |  "error": "invalid_request",
          |  "error_description": "Missing grant_type"
          |}
        """.stripMargin.getBytes
    )
    val rqResponse = MemoryRqResponse(400, body = responseBody)
    val expectedError = OptumOAuthException(400, Some("invalid_request"), None)

    val actualResponse: Either[OptumOAuthException, OptumAccessTokenResponse] = client.buildOAuthRequestWithHandler.handler(rqResponse)
    assert(actualResponse.left.get.copy(body = None) === expectedError)
  }

  it should "make an auth request and handle non-json response" in {
    val f = new Fixture
    import f._

    val responseBody = RqBody(
      """Fucked
        """.stripMargin.getBytes
    )
    val rqResponse = MemoryRqResponse(400, body = responseBody)
    val expectedError = OptumOAuthException(400, None, None)

    val actualResponse: Either[OptumOAuthException, OptumAccessTokenResponse] = client.buildOAuthRequestWithHandler.handler(rqResponse)
    assert(actualResponse.left.get.copy(body = None) === expectedError)
  }

  it should "handle execute" in {
    val f = new Fixture
    import f._
    val expectedToken = OptumAccessTokenResponse("s8KYRJGTO0rWMy0zz1CCSCwsSesDyDlbNdZoRqVR", 3600)
    when(rqClient.execute[Either[OptumOAuthException, OptumAccessTokenResponse]](any())(any())).thenReturn(Future.successful(Right(expectedToken)))
    whenReady(client.getAccessToken) { response =>
      assert(response == Right(expectedToken))
    }
  }
}
