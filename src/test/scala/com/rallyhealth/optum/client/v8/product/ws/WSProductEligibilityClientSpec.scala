package com.rallyhealth.optum.client.v8.product.ws
import com.rallyhealth.optum.client.v8.auth.model.OptumAccessTokenResponse
import com.rallyhealth.optum.client.v8.common.model.{BaseOptumResponseError, OptumResponseAppError, OptumResponseError, OptumResponseSuccess}
import com.rallyhealth.optum.client.v8.common.{HttpUtil, OptumClientConfig, OptumClientRallyConfig}
import com.rallyhealth.optum.client.v8.product.model._
import com.rallyhealth.optum.client.v8.product.util.SampleProductEligibilityResponse
import com.rallyhealth.rq.v1.handler.HandledRqRequest
import com.rallyhealth.rq.v1.{MemoryRqResponse, RqBody, RqClient}
import com.rallyhealth.spartan.v2.config.{MemoryRallyConfig, RallyConfig}
import org.joda.time.DateTime
import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{FlatSpec, LoneElement, Matchers, OptionValues}
import org.scalatest.mockito.MockitoSugar
import play.api.libs.json.Json
import com.rallyhealth.optum.client.v8.common.HttpUtil.Header._
import com.rallyhealth.optum.client.v8.member.util.SampleResponseGenerator
import com.rallyhealth.optum.client.v8.product.config.ProductEligibilityRallyConfig
import com.rallyhealth.spartan.v2.datetime.DateTimeHelpers
import org.mockito.Matchers._
import org.mockito.Mockito._

import scala.concurrent.Future

class WSProductEligibilityClientSpec
  extends FlatSpec
  with Matchers
  with TypeCheckedTripleEquals
  with OptionValues
  with MockitoSugar
  with LoneElement
  with ScalaFutures {

  class Fixture {

    val rconfig: RallyConfig = new MemoryRallyConfig(setupMockConfig)
    val oconfig: ProductEligibilityRallyConfig = new ProductEligibilityRallyConfig(rconfig)
    val rqClient: RqClient = mock[RqClient]
    val client = new WSProductEligibilityClient(oconfig, rqClient)

    val optumProductEligibilityRequestWithRelationshipCode = OptumProductEligibilityRequestWithRelationshipCode(
      contractNumber = "0191690",
      relationshipCode = "EE",
      dateOfBirth = DateTime.parse("1970-06-14T00:00:00Z")
    )

    val optumProductEligibilityRequestWithPopulationIdAndRelationshipCode = OptumProductEligibilityRequestWithPopulationIdAndRelationshipCode(
      populationId = "POP317",
      contractNumber = "0191690",
      relationshipCode = "EE",
      dateOfBirth = DateTime.parse("1970-06-14T00:00:00Z")
    )

    val optumProductEligibilityRequestWithPopulationIdAndRelationshipDescription = OptumProductEligibilityRequestWithPopulationIdAndRelationshipDescription(
      populationId = "POP317",
      contractNumber = "0191690",
      relationshipDescription = "SUBSCRIBER/RECIPIENT",
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

  classOf[WSProductEligibilityClient].getSimpleName should "make a optumProductEligibilityRequestWithRelationshipCode  and handle success response" in {
    val f = new Fixture
    import f._
    val responseBody = RqBody(SampleProductEligibilityResponse.responseWithoutAnyProduct.getBytes)
    val rqResponse = MemoryRqResponse(200, body = responseBody)
    val expectedResponse = OptumResponseSuccess(200, Map.empty, Json.parse(SampleProductEligibilityResponse.responseWithoutAnyProduct))

    val handler: HandledRqRequest[Either[BaseOptumResponseError, OptumResponseSuccess]] = client.buildRequestWithHandler(optumProductEligibilityRequestWithRelationshipCode, requestMeta)
    assert(handler.request.url == "http://test/product/eligibility/v1.0")
    assert(handler.request.method == HttpUtil.Method.Post)
    assert(handler.request.headers.get(Authorization).value.loneElement === "Bearer test-token")
    assert(handler.request.headers.get(ContentType).value.loneElement contains ContentTypeJson)
    assert(handler.request.headers.get(Timestamp).value.size === 1)
    assert(handler.request.headers.get(Scope).value.loneElement === "read")
    assert(handler.request.headers.get(Actor).value.loneElement === "TEST")
    assert(handler.request.headers.get(OptumCorelationId).isDefined)
    val actualRespons: Either[BaseOptumResponseError, OptumResponseSuccess] = handler.handler(rqResponse)
    assert(actualRespons === Right(expectedResponse))
  }

  classOf[WSProductEligibilityClient].getSimpleName should "make a optumProductEligibilityRequestWithPopulationIdAndRelationshipCode and handle success response" in {
    val f = new Fixture
    import f._
    val responseBody = RqBody(SampleProductEligibilityResponse.sampleResponseWithPATCHProducts.getBytes)
    val rqResponse = MemoryRqResponse(200, body = responseBody)
    val expectedResponse = OptumResponseSuccess(200, Map.empty, Json.parse(SampleProductEligibilityResponse.sampleResponseWithPATCHProducts))

    val handler: HandledRqRequest[Either[BaseOptumResponseError, OptumResponseSuccess]] = client.buildRequestWithHandler(optumProductEligibilityRequestWithPopulationIdAndRelationshipCode, requestMeta)
    assert(handler.request.url == "http://test/product/eligibility/v1.0")
    assert(handler.request.method == HttpUtil.Method.Post)
    assert(handler.request.headers.get(Authorization).value.loneElement === "Bearer test-token")
    assert(handler.request.headers.get(ContentType).value.loneElement contains ContentTypeJson)
    assert(handler.request.headers.get(Timestamp).value.size === 1)
    assert(handler.request.headers.get(Scope).value.loneElement === "read")
    assert(handler.request.headers.get(Actor).value.loneElement === "TEST")
    assert(handler.request.headers.get(OptumCorelationId).isDefined)
    val actualRespons: Either[BaseOptumResponseError, OptumResponseSuccess] = handler.handler(rqResponse)
    assert(actualRespons === Right(expectedResponse))
  }

  classOf[WSProductEligibilityClient].getSimpleName should "make a optumProductEligibilityRequestWithPopulationIdAndRelationshipDescription and handle success response" in {
    val f = new Fixture
    import f._
    val responseBody = RqBody(SampleProductEligibilityResponse.sampleResponseWithPATCHProducts.getBytes)
    val rqResponse = MemoryRqResponse(200, body = responseBody)
    val expectedResponse = OptumResponseSuccess(200, Map.empty, Json.parse(SampleProductEligibilityResponse.sampleResponseWithPATCHProducts))

    val handler: HandledRqRequest[Either[BaseOptumResponseError, OptumResponseSuccess]] = client.buildRequestWithHandler(optumProductEligibilityRequestWithPopulationIdAndRelationshipDescription, requestMeta)
    assert(handler.request.url == "http://test/product/eligibility/v1.0")
    assert(handler.request.method == HttpUtil.Method.Post)
    assert(handler.request.headers.get(Authorization).value.loneElement === "Bearer test-token")
    assert(handler.request.headers.get(ContentType).value.loneElement contains ContentTypeJson)
    assert(handler.request.headers.get(Timestamp).value.size === 1)
    assert(handler.request.headers.get(Scope).value.loneElement === "read")
    assert(handler.request.headers.get(Actor).value.loneElement === "TEST")
    assert(handler.request.headers.get(OptumCorelationId).isDefined)
    val actualRespons: Either[BaseOptumResponseError, OptumResponseSuccess] = handler.handler(rqResponse)
    assert(actualRespons === Right(expectedResponse))
  }

  classOf[WSProductEligibilityClient].getSimpleName should "make a optumProductEligibilityRequestWithPopulationIdAndRelationshipDescription and without eventDate should handle success response" in {
    val f = new Fixture
    import f._
    val requestMeta = OptumProductEligibilityRequestMeta(applicationName = "RALLY", transactionId = "RALLY022020179", eventDate = None)
    val responseBody = RqBody(SampleProductEligibilityResponse.sampleResponseWithPATCHProducts.getBytes)
    val rqResponse = MemoryRqResponse(200, body = responseBody)
    val expectedResponse = OptumResponseSuccess(200, Map.empty, Json.parse(SampleProductEligibilityResponse.sampleResponseWithPATCHProducts))

    val handler: HandledRqRequest[Either[BaseOptumResponseError, OptumResponseSuccess]] = client.buildRequestWithHandler(optumProductEligibilityRequestWithPopulationIdAndRelationshipDescription, requestMeta)
    assert(handler.request.url == "http://test/product/eligibility/v1.0")
    assert(handler.request.method == HttpUtil.Method.Post)
    assert(handler.request.headers.get(Authorization).value.loneElement === "Bearer test-token")
    assert(handler.request.headers.get(ContentType).value.loneElement contains ContentTypeJson)
    assert(handler.request.headers.get(Timestamp).value.size === 1)
    assert(handler.request.headers.get(Scope).value.loneElement === "read")
    assert(handler.request.headers.get(Actor).value.loneElement === "TEST")
    assert(handler.request.headers.get(OptumCorelationId).isDefined)
    val actualRespons: Either[BaseOptumResponseError, OptumResponseSuccess] = handler.handler(rqResponse)
    assert(actualRespons === Right(expectedResponse))
  }

  it should "make a optumProductEligibilityRequestWithPopulationIdAndRelationshipCode and handle error response" in {
    val f = new Fixture
    import f._
    val responseBody = RqBody(SampleProductEligibilityResponse.errorResponse.getBytes)
    val rqResponse = MemoryRqResponse(403, body = responseBody)
    val expectedResponse = OptumResponseAppError(403, Map.empty, "153", Json.parse(SampleProductEligibilityResponse.errorResponse))

    val handler: HandledRqRequest[Either[BaseOptumResponseError, OptumResponseSuccess]] = client.buildRequestWithHandler(optumProductEligibilityRequestWithPopulationIdAndRelationshipCode, requestMeta)
    assert(handler.request.url == "http://test/product/eligibility/v1.0")
    assert(handler.request.method == HttpUtil.Method.Post)
    assert(handler.request.headers.get(Authorization).value.loneElement === "Bearer test-token")
    assert(handler.request.headers.get(ContentType).value.loneElement contains ContentTypeJson)
    assert(handler.request.headers.get(Timestamp).value.size === 1)
    assert(handler.request.headers.get(Scope).value.loneElement === "read")
    assert(handler.request.headers.get(Actor).value.loneElement === "TEST")
    assert(handler.request.headers.get(OptumCorelationId).isDefined)
    val actualResponse: Either[BaseOptumResponseError, OptumResponseSuccess] = handler.handler(rqResponse)
    assert(actualResponse === Left(expectedResponse))
  }

  it should "make a optumProductEligibilityRequestWithPopulationIdAndRelationshipCode and handle error response without Json body" in {
    val f = new Fixture
    import f._
    val bodyData: String = "{fked}"
    val responseBody = RqBody(bodyData.getBytes)
    val rqResponse = MemoryRqResponse(500, body = responseBody)
    val expectedResponse = OptumResponseError(500, Map.empty, Some(bodyData))

    val handler: HandledRqRequest[Either[BaseOptumResponseError, OptumResponseSuccess]] =
      client.buildRequestWithHandler(optumProductEligibilityRequestWithPopulationIdAndRelationshipCode, requestMeta.copy(eventDate = Some(DateTimeHelpers.now)))
    assert(handler.request.url == "http://test/product/eligibility/v1.0")
    assert(handler.request.method == HttpUtil.Method.Post)
    assert(handler.request.headers.get(Authorization).value.loneElement === "Bearer test-token")
    assert(handler.request.headers.get(ContentType).value.loneElement contains ContentTypeJson)
    assert(handler.request.headers.get(Timestamp).value.size === 1)
    assert(handler.request.headers.get(Scope).value.loneElement === "read")
    assert(handler.request.headers.get(Actor).value.loneElement === "TEST")
    assert(handler.request.headers.get(OptumCorelationId).isDefined)
    val actualResponse: Either[BaseOptumResponseError, OptumResponseSuccess] = handler.handler(rqResponse)
    assert(actualResponse === Left(expectedResponse))
  }

  it should "call ws client execute" in {
    val f = new Fixture
    import f._
    val expectedResponse = OptumResponseSuccess(200, Map.empty, Json.parse(SampleProductEligibilityResponse.sampleResponseWithPATCHProducts))
    when(rqClient.execute[Either[BaseOptumResponseError, OptumResponseSuccess]](any())(any())).thenReturn(Future.successful(Right(expectedResponse)))

    whenReady(client.findProductEligibility(optumProductEligibilityRequestWithPopulationIdAndRelationshipDescription, requestMeta)) { res =>
      assert(res.isRight)
      assert(res.right.get === expectedResponse)
    }
  }
}
