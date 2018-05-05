package com.rallyhealth.optum.client.v8.config.ws

import com.rallyhealth.optum.client.v8.auth.model.OptumAccessTokenResponse
import com.rallyhealth.optum.client.v8.common.HttpUtil.Header._
import com.rallyhealth.optum.client.v8.common.model.{BaseOptumResponseError, OptumResponseAppError, OptumResponseError, OptumResponseSuccess}
import com.rallyhealth.optum.client.v8.common.{HttpUtil, OptumClientConfig, OptumClientRallyConfig}
import com.rallyhealth.optum.client.v8.config.config.ConfigIntakeRallyConfig
import com.rallyhealth.optum.client.v8.config.util.SampleConfigIntakeRequestGenerator
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

class WSConfigIntakeClientSpec
  extends FlatSpec
  with Matchers
  with TypeCheckedTripleEquals
  with OptionValues
  with MockitoSugar
  with LoneElement
  with ScalaFutures {

  class Fixture {

    val rconfig: RallyConfig = new MemoryRallyConfig(setupMockConfig)
    val oconfig: ConfigIntakeRallyConfig = new ConfigIntakeRallyConfig(rconfig)
    val rqClient: RqClient = mock[RqClient]
    val client = new WSConfigIntakeClient(oconfig, rqClient)
    val generator = new SampleConfigIntakeRequestGenerator

    implicit val token = OptumAccessTokenResponse("test-token", 3600)

    def setupMockConfig: Map[String, String] = {
      Map(
        "optum.client.configIntakeBaseUrl" -> "http://test",
        "optum.client.actor" -> "TEST",
        "optum.client.scope" -> "read",
        "optum.client.oauthBaseUrl" -> "http://test-auth",
        "optum.client.requesttimeout" -> "60"
      )
    }
  }

  classOf[WSConfigIntakeClient].getSimpleName should "make a postClientConfiguration call and handle success response" in {
    val f = new Fixture
    import f._
    val responseBody = RqBody(generator.responseStringSuccess.getBytes)
    val rqResponse = MemoryRqResponse(202, body = responseBody)
    val expectedResponse = OptumResponseSuccess(202, Map.empty, generator.responseSuccess)

    val handler: HandledRqRequest[Either[BaseOptumResponseError, OptumResponseSuccess]] = client.buildRequestWithHandler(generator.request)
    assert(handler.request.url == "http://test/config-intake/custom/v1.0")
    assert(handler.request.method == HttpUtil.Method.Post)
    assert(handler.request.headers.get(Authorization).value.loneElement === "Bearer test-token")
    assert(handler.request.headers.get(ContentType).value.loneElement contains ContentTypeJson)
    assert(handler.request.headers.get(Timestamp).value.size === 1)
    assert(handler.request.headers.get(OptumCorelationId).isDefined)
    val actualResponse: Either[BaseOptumResponseError, OptumResponseSuccess] = handler.handler(rqResponse)
    assert(actualResponse === Right(expectedResponse))
  }

  it should "make a postClientConfiguration and handle error response" in {
    val f = new Fixture
    import f._
    val responseBody = RqBody(generator.responseStringError.getBytes)
    val rqResponse = MemoryRqResponse(400, body = responseBody)
    val expectedResponse = OptumResponseAppError(400, Map.empty, "1001", generator.responseError)

    val handler: HandledRqRequest[Either[BaseOptumResponseError, OptumResponseSuccess]] = client.buildRequestWithHandler(generator.request)
    assert(handler.request.url == "http://test/config-intake/custom/v1.0")
    assert(handler.request.method == HttpUtil.Method.Post)
    assert(handler.request.headers.get(Authorization).value.loneElement === "Bearer test-token")
    assert(handler.request.headers.get(ContentType).value.loneElement contains ContentTypeJson)
    assert(handler.request.headers.get(Timestamp).value.size === 1)
    assert(handler.request.headers.get(OptumCorelationId).isDefined)
    val actualResponse: Either[BaseOptumResponseError, OptumResponseSuccess] = handler.handler(rqResponse)
    assert(actualResponse === Left(expectedResponse))
  }

  it should "make a postClientConfiguration and handle error response with two errors" in {
    val f = new Fixture
    import f._
    val responseBody = RqBody(generator.responseStringErrorTwoCodes.getBytes)
    val rqResponse = MemoryRqResponse(400, body = responseBody)
    val expectedResponse = OptumResponseAppError(400, Map.empty, "1", generator.responseErrorTwoCodes)

    val handler: HandledRqRequest[Either[BaseOptumResponseError, OptumResponseSuccess]] = client.buildRequestWithHandler(generator.request)
    assert(handler.request.url == "http://test/config-intake/custom/v1.0")
    assert(handler.request.method == HttpUtil.Method.Post)
    assert(handler.request.headers.get(Authorization).value.loneElement === "Bearer test-token")
    assert(handler.request.headers.get(ContentType).value.loneElement contains ContentTypeJson)
    assert(handler.request.headers.get(Timestamp).value.size === 1)
    assert(handler.request.headers.get(OptumCorelationId).isDefined)
    val actualResponse: Either[BaseOptumResponseError, OptumResponseSuccess] = handler.handler(rqResponse)
    assert(actualResponse === Left(expectedResponse))
  }

  it should "make a postClientConfiguration and handle error response with no error details" in {
    val f = new Fixture
    import f._
    val responseBody = RqBody(generator.responseStringErrorNoDetails.getBytes)
    val rqResponse = MemoryRqResponse(400, body = responseBody)
    val expectedResponse = OptumResponseAppError(400, Map.empty, "", generator.responseErrorNoDetails)

    val handler: HandledRqRequest[Either[BaseOptumResponseError, OptumResponseSuccess]] = client.buildRequestWithHandler(generator.request)
    assert(handler.request.url == "http://test/config-intake/custom/v1.0")
    assert(handler.request.method == HttpUtil.Method.Post)
    assert(handler.request.headers.get(Authorization).value.loneElement === "Bearer test-token")
    assert(handler.request.headers.get(ContentType).value.loneElement contains ContentTypeJson)
    assert(handler.request.headers.get(Timestamp).value.size === 1)
    assert(handler.request.headers.get(OptumCorelationId).isDefined)
    val actualResponse: Either[BaseOptumResponseError, OptumResponseSuccess] = handler.handler(rqResponse)
    assert(actualResponse === Left(expectedResponse))
  }

  it should "make a postClientConfiguration and handle error response with no error details due to empty error list" in {
    val f = new Fixture
    import f._
    val responseBody = RqBody(generator.responseStringErrorNoDetailsEmptyList.getBytes)
    val rqResponse = MemoryRqResponse(400, body = responseBody)
    val expectedResponse = OptumResponseAppError(400, Map.empty, "", generator.responseErrorNoDetailsEmptyList)

    val handler: HandledRqRequest[Either[BaseOptumResponseError, OptumResponseSuccess]] = client.buildRequestWithHandler(generator.request)
    assert(handler.request.url == "http://test/config-intake/custom/v1.0")
    assert(handler.request.method == HttpUtil.Method.Post)
    assert(handler.request.headers.get(Authorization).value.loneElement === "Bearer test-token")
    assert(handler.request.headers.get(ContentType).value.loneElement contains ContentTypeJson)
    assert(handler.request.headers.get(Timestamp).value.size === 1)
    assert(handler.request.headers.get(OptumCorelationId).isDefined)
    val actualResponse: Either[BaseOptumResponseError, OptumResponseSuccess] = handler.handler(rqResponse)
    assert(actualResponse === Left(expectedResponse))
  }

  it should "make a postClientConfiguration and handle error response with no code" in {
    val f = new Fixture
    import f._
    val responseBody = RqBody(generator.responseStringErrorNoCode.getBytes)
    val rqResponse = MemoryRqResponse(400, body = responseBody)
    val expectedResponse = OptumResponseAppError(400, Map.empty, "", generator.responseErrorNoCode)

    val handler: HandledRqRequest[Either[BaseOptumResponseError, OptumResponseSuccess]] = client.buildRequestWithHandler(generator.request)
    assert(handler.request.url == "http://test/config-intake/custom/v1.0")
    assert(handler.request.method == HttpUtil.Method.Post)
    assert(handler.request.headers.get(Authorization).value.loneElement === "Bearer test-token")
    assert(handler.request.headers.get(ContentType).value.loneElement contains ContentTypeJson)
    assert(handler.request.headers.get(Timestamp).value.size === 1)
    assert(handler.request.headers.get(OptumCorelationId).isDefined)
    val actualResponse: Either[BaseOptumResponseError, OptumResponseSuccess] = handler.handler(rqResponse)
    assert(actualResponse === Left(expectedResponse))
  }

  it should "make a postClientConfiguration and handle error response without Json body" in {
    val f = new Fixture
    import f._
    val bodyData: String = "{fked}"
    val responseBody = RqBody(bodyData.getBytes)
    val rqResponse = MemoryRqResponse(500, body = responseBody)
    val expectedResponse = OptumResponseError(500, Map.empty, Some(bodyData))

    val handler: HandledRqRequest[Either[BaseOptumResponseError, OptumResponseSuccess]] = client.buildRequestWithHandler(generator.request)
    assert(handler.request.url == "http://test/config-intake/custom/v1.0")
    assert(handler.request.method == HttpUtil.Method.Post)
    assert(handler.request.headers.get(Authorization).value.loneElement === "Bearer test-token")
    assert(handler.request.headers.get(ContentType).value.loneElement contains ContentTypeJson)
    assert(handler.request.headers.get(Timestamp).value.size === 1)
    assert(handler.request.headers.get(OptumCorelationId).isDefined)
    val actualResponse: Either[BaseOptumResponseError, OptumResponseSuccess] = handler.handler(rqResponse)
    assert(actualResponse === Left(expectedResponse))
  }

  it should "call ws client execute" in {
    val f = new Fixture
    import f._
    val expectedResponse = OptumResponseSuccess(202, Map.empty, generator.request)
    when(rqClient.execute[Either[BaseOptumResponseError, OptumResponseSuccess]](any())(any())).thenReturn(Future.successful(Right(expectedResponse)))

    whenReady(client.postClientConfiguration(generator.request)) { res =>
      assert(res.isRight)
      assert(res.right.get === expectedResponse)
    }
  }
}
