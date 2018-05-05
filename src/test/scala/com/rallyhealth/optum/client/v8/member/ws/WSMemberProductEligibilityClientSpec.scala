package com.rallyhealth.optum.client.v8.member.ws

import javax.xml.bind.DatatypeConverter

import com.rallyhealth.argosy.correlationId.CorrelationId
import com.rallyhealth.argosy.mdc.ArgosyMDCAdapter
import com.rallyhealth.enigma.v4.{GCMCipherProvider, NeoEncryptionService, NeoEncryptionServiceImpl}
import com.rallyhealth.optum.client.v8.auth.model.OptumAccessTokenResponse
import com.rallyhealth.optum.client.v8.common.HttpUtil
import com.rallyhealth.optum.client.v8.common.HttpUtil.Header._
import com.rallyhealth.optum.client.v8.common.model.{BaseOptumResponseError, OptumResponseAppError, OptumResponseError, OptumResponseSuccess}
import com.rallyhealth.optum.client.v8.member.config.MemberProductEligibilityRallyConfig
import com.rallyhealth.optum.client.v8.member.model._
import com.rallyhealth.optum.client.v8.member.util.SampleResponseGenerator
import com.rallyhealth.optum.client.v8.member.ws.MemPERequestBodyBuilder._
import com.rallyhealth.rq.v1.handler.HandledRqRequest
import com.rallyhealth.rq.v1.handler.exception.HandlerException
import com.rallyhealth.rq.v1.{MemoryRqResponse, RqBody, RqClient}
import com.rallyhealth.spartan.v2.config.{MemoryRallyConfig, RallyConfig}
import com.rallyhealth.spartan.v2.datetime.DateTimeHelpers
import org.bouncycastle.crypto.params.KeyParameter
import org.mockito.Matchers._
import org.mockito.Mockito._
import org.scalactic.TypeCheckedTripleEquals
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.mockito.MockitoSugar
import org.scalatest.{FlatSpec, LoneElement, Matchers, OptionValues}
import play.api.libs.json.Json

import scala.concurrent.Future

class WSMemberProductEligibilityClientSpec
  extends FlatSpec
  with Matchers
  with TypeCheckedTripleEquals
  with OptionValues
  with MockitoSugar
  with LoneElement
  with ScalaFutures {

  class Fixture {

    val serviceEncryptKey = "A3 5D 53 39 2A 05 31 86 40 62 54 0A 32 84 97 B5 98 A2 65 E0 70 89 AA EC E5 8E 60 71 39 87 DE 17"

    lazy val keyParameter: KeyParameter = {
      val encryptKey = DatatypeConverter.parseHexBinary(serviceEncryptKey.replaceAll("\\s", ""))
      new KeyParameter(encryptKey)
    }

    lazy val neoEncryptionService: NeoEncryptionService = {
      val cipherProvider = new GCMCipherProvider(keyParameter)
      new NeoEncryptionServiceImpl(cipherProvider)
    }

    val rconfig: RallyConfig = new MemoryRallyConfig(setupMockConfig)
    val oconfig: MemberProductEligibilityRallyConfig = new MemberProductEligibilityRallyConfig(rconfig)
    val rqClient: RqClient = mock[RqClient]
    val client = new WSMemberProductEligibilityClient(oconfig, rqClient, None)
    val clientWithEncryption = new WSMemberProductEligibilityClient(oconfig, rqClient, Some(neoEncryptionService))
    val reqPidCn = PersonIdContractNumberMemPERequest("34523", Seq("ctest1"))
    val reqPid = PersonIdMemPERequest("454234")
    val reqBig5 = Big5MemPERequest("testFirst", "testLast", "1968-07-22T00:00:00Z", "123", Seq("456"))

    val requestHeader = RequestHeader("test", "testid")
    val filteringAttributes = FilteringAttributes(false, true)
    val requestDetails = RequestDetails("PERSONID_PLUS_CONTRACTNUMBER", "ALL")
    val idSet = IdSet(1241123)
    val consumerDetails = ConsumerDetails

    val eligibilityRequest = MemberProductEligibilityRequest(requestHeader, filteringAttributes, requestDetails, Some(idSet), None)

    implicit val maybeToken = Some(OptumAccessTokenResponse("test-token", 3600))
    val meta = OptumRequestMetaData("test", DateTimeHelpers.now.getMillis.toString, All, None, Some("corrId"), FilteringRules(true, false))
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

  classOf[WSMemberProductEligibilityClient].getSimpleName should "make a personId request and handle success response" in {
    val f = new Fixture
    import f._
    val responseBody = RqBody(SampleResponseGenerator.memPEResponseAll.getBytes)
    val rqResponse = MemoryRqResponse(200, body = responseBody)
    val expectedResponse = OptumResponseSuccess(200, Map.empty, Json.parse(SampleResponseGenerator.memPEResponseAll))

    val handler: HandledRqRequest[Either[BaseOptumResponseError, OptumResponseSuccess]] = client.buildRequestWithHandler(reqPid, meta)
    assert(handler.request.url == "http://test/member/product/eligibility/v2.0")
    assert(handler.request.method == HttpUtil.Method.Post)
    assert(handler.request.headers.get(Authorization).value.loneElement === "Bearer test-token")
    assert(handler.request.headers.get(ContentType).value.loneElement contains ContentTypeJson)
    assert(handler.request.headers.get(Timestamp).value.size === 1)
    assert(handler.request.headers.get(Scope).value.loneElement === "read")
    assert(handler.request.headers.get(Actor).value.loneElement === "TEST")
    assert(handler.request.headers.get(OptumCorelationId).value.loneElement == "corrId")
    assert(handler.request.headers.get(CorrelationId.HeaderName).value.loneElement == "corrId")
    val actualResponse: Either[BaseOptumResponseError, OptumResponseSuccess] = handler.handler(rqResponse)
    assert(actualResponse === Right(expectedResponse))
  }

  it should "make an encrypted personId request and handle an encrypted success response" in {
    val f = new Fixture
    import f._
    val sampleResponse = SampleResponseGenerator.memPEResponseAll
    val encryptedResponse = Json.toJson(Map(oconfig.encryptedPayloadKey -> neoEncryptionService.encryptString(sampleResponse))).toString
    val responseBody = RqBody(encryptedResponse.getBytes)
    val rqResponse = MemoryRqResponse(200, body = responseBody)
    val expectedResponse = OptumResponseSuccess(200, Map.empty, Json.parse(sampleResponse))

    val handler: HandledRqRequest[Either[BaseOptumResponseError, OptumResponseSuccess]] =
      clientWithEncryption.buildRequestWithHandler(reqPid, meta)(None)
    assert(handler.request.url == "http://test/member/product/eligibility/v2.0")
    assert(handler.request.method == HttpUtil.Method.Post)
    assert(handler.request.headers.get(Authorization).value.loneElement === "Bearer Empty")
    assert(handler.request.headers.get(ContentType).value.loneElement contains ContentTypeJson)
    assert(handler.request.headers.get(Timestamp).value.size === 1)
    assert(handler.request.headers.get(Scope).value.loneElement === "read")
    assert(handler.request.headers.get(Actor).value.loneElement === "TEST")
    assert(handler.request.headers.get(OptumCorelationId).value.loneElement == "corrId")
    assert(handler.request.headers.get(CorrelationId.HeaderName).value.loneElement == "corrId")
    val actualResponse: Either[BaseOptumResponseError, OptumResponseSuccess] = handler.handler(rqResponse)
    assert(actualResponse === Right(expectedResponse))
  }

  it should "make a big5 request and handle success response" in {
    val f = new Fixture
    import f._
    val responseBody = RqBody(SampleResponseGenerator.memPEResponseAll.getBytes)
    val rqResponse = MemoryRqResponse(200, body = responseBody)
    val expectedResponse = OptumResponseSuccess(200, Map.empty, Json.parse(SampleResponseGenerator.memPEResponseAll))

    val handler: HandledRqRequest[Either[BaseOptumResponseError, OptumResponseSuccess]] = client.buildRequestWithHandler(reqBig5, meta)
    assert(handler.request.url == "http://test/member/product/eligibility/v2.0")
    assert(handler.request.method == HttpUtil.Method.Post)
    assert(handler.request.headers.get(Authorization).value.loneElement === "Bearer test-token")
    assert(handler.request.headers.get(ContentType).value.loneElement contains ContentTypeJson)
    assert(handler.request.headers.get(Timestamp).value.size === 1)
    assert(handler.request.headers.get(Scope).value.loneElement === "read")
    assert(handler.request.headers.get(Actor).value.loneElement === "TEST")
    assert(handler.request.headers.get(OptumCorelationId).value.loneElement == "corrId")
    assert(handler.request.headers.get(CorrelationId.HeaderName).value.loneElement == "corrId")
    val actualResponse: Either[BaseOptumResponseError, OptumResponseSuccess] = handler.handler(rqResponse)
    assert(actualResponse === Right(expectedResponse))
  }

  it should "make an encrypted big5 request and handle encrypted success response" in {
    val f = new Fixture
    import f._
    val sampleResponse = SampleResponseGenerator.memPEResponseAll
    val encryptedResponse = Json.toJson(Map(oconfig.encryptedPayloadKey -> neoEncryptionService.encryptString(sampleResponse))).toString
    val responseBody = RqBody(encryptedResponse.getBytes)
    val rqResponse = MemoryRqResponse(200, body = responseBody)
    val expectedResponse = OptumResponseSuccess(200, Map.empty, Json.parse(sampleResponse))

    val handler: HandledRqRequest[Either[BaseOptumResponseError, OptumResponseSuccess]] =
      clientWithEncryption.buildRequestWithHandler(reqBig5, meta)(None)
    assert(handler.request.url == "http://test/member/product/eligibility/v2.0")
    assert(handler.request.method == HttpUtil.Method.Post)
    assert(handler.request.headers.get(Authorization).value.loneElement === "Bearer Empty")
    assert(handler.request.headers.get(ContentType).value.loneElement contains ContentTypeJson)
    assert(handler.request.headers.get(Timestamp).value.size === 1)
    assert(handler.request.headers.get(Scope).value.loneElement === "read")
    assert(handler.request.headers.get(Actor).value.loneElement === "TEST")
    assert(handler.request.headers.get(OptumCorelationId).value.loneElement == "corrId")
    assert(handler.request.headers.get(CorrelationId.HeaderName).value.loneElement == "corrId")
    val actualResponse: Either[BaseOptumResponseError, OptumResponseSuccess] = handler.handler(rqResponse)
    assert(actualResponse === Right(expectedResponse))
  }

  it should "make a personId & contractNumber request and handle success response" in {
    val f = new Fixture
    import f._
    val responseBody = RqBody(SampleResponseGenerator.memPEResponseAll.getBytes)
    val rqResponse = MemoryRqResponse(200, body = responseBody)
    val expectedResponse = OptumResponseSuccess(200, Map.empty, Json.parse(SampleResponseGenerator.memPEResponseAll))

    val handler: HandledRqRequest[Either[BaseOptumResponseError, OptumResponseSuccess]] = client.buildRequestWithHandler(reqPidCn, meta)
    assert(handler.request.url == "http://test/member/product/eligibility/v2.0")
    assert(handler.request.method == HttpUtil.Method.Post)
    assert(handler.request.headers.get(Authorization).value.loneElement === "Bearer test-token")
    assert(handler.request.headers.get(ContentType).value.loneElement contains ContentTypeJson)
    assert(handler.request.headers.get(Timestamp).value.size === 1)
    assert(handler.request.headers.get(Scope).value.loneElement === "read")
    assert(handler.request.headers.get(Actor).value.loneElement === "TEST")
    assert(handler.request.headers.get(OptumCorelationId).value.loneElement == "corrId")
    assert(handler.request.headers.get(CorrelationId.HeaderName).value.loneElement == "corrId")
    val actualResponse: Either[BaseOptumResponseError, OptumResponseSuccess] = handler.handler(rqResponse)
    assert(actualResponse === Right(expectedResponse))
  }

  it should "make a personId & contractNumber request and handle error response" in {
    val f = new Fixture
    import f._
    val responseBody = RqBody(SampleResponseGenerator.errorResponse.getBytes)
    val rqResponse = MemoryRqResponse(403, body = responseBody)
    val expectedResponse = OptumResponseAppError(403, Map.empty, "154", Json.parse(SampleResponseGenerator.errorResponse))

    val handler: HandledRqRequest[Either[BaseOptumResponseError, OptumResponseSuccess]] = client.buildRequestWithHandler(reqPidCn, meta)
    assert(handler.request.url == "http://test/member/product/eligibility/v2.0")
    assert(handler.request.method == HttpUtil.Method.Post)
    assert(handler.request.headers.get(Authorization).value.loneElement === "Bearer test-token")
    assert(handler.request.headers.get(ContentType).value.loneElement contains ContentTypeJson)
    assert(handler.request.headers.get(Timestamp).value.size === 1)
    assert(handler.request.headers.get(Scope).value.loneElement === "read")
    assert(handler.request.headers.get(Actor).value.loneElement === "TEST")
    assert(handler.request.headers.get(OptumCorelationId).value.loneElement == "corrId")
    assert(handler.request.headers.get(CorrelationId.HeaderName).value.loneElement == "corrId")
    val actualResponse: Either[BaseOptumResponseError, OptumResponseSuccess] = handler.handler(rqResponse)
    assert(actualResponse === Left(expectedResponse))
  }

  it should "make a personId & contractNumber request and handle error advice response" in {
    val f = new Fixture
    import f._
    val responseBody = RqBody(SampleResponseGenerator.errorAdviceResponse.getBytes)
    val rqResponse = MemoryRqResponse(403, body = responseBody)
    val expectedResponse = OptumResponseAppError(403, Map.empty, "4.2", Json.parse(SampleResponseGenerator.errorAdviceResponse))

    val handler: HandledRqRequest[Either[BaseOptumResponseError, OptumResponseSuccess]] = client.buildRequestWithHandler(reqPidCn, meta)
    assert(handler.request.url == "http://test/member/product/eligibility/v2.0")
    assert(handler.request.method == HttpUtil.Method.Post)
    assert(handler.request.headers.get(Authorization).value.loneElement === "Bearer test-token")
    assert(handler.request.headers.get(ContentType).value.loneElement contains ContentTypeJson)
    assert(handler.request.headers.get(Timestamp).value.size === 1)
    assert(handler.request.headers.get(Scope).value.loneElement === "read")
    assert(handler.request.headers.get(Actor).value.loneElement === "TEST")
    assert(handler.request.headers.get(OptumCorelationId).value.loneElement == "corrId")
    assert(handler.request.headers.get(CorrelationId.HeaderName).value.loneElement == "corrId")
    val actualResponse: Either[BaseOptumResponseError, OptumResponseSuccess] = handler.handler(rqResponse)
    assert(actualResponse === Left(expectedResponse))
  }

  it should "make a personId & contractNumber request and handle exception response" in {
    val f = new Fixture
    import f._
    val responseBody = RqBody(SampleResponseGenerator.exceptionResponse.getBytes)
    val rqResponse = MemoryRqResponse(501, body = responseBody)
    val expectedResponse = OptumResponseAppError(501, Map.empty, "307", Json.parse(SampleResponseGenerator.exceptionResponse))

    val handler: HandledRqRequest[Either[BaseOptumResponseError, OptumResponseSuccess]] = client.buildRequestWithHandler(reqPidCn, meta)
    assert(handler.request.url == "http://test/member/product/eligibility/v2.0")
    assert(handler.request.method == HttpUtil.Method.Post)
    assert(handler.request.headers.get(Authorization).value.loneElement === "Bearer test-token")
    assert(handler.request.headers.get(ContentType).value.loneElement contains ContentTypeJson)
    assert(handler.request.headers.get(Timestamp).value.size === 1)
    assert(handler.request.headers.get(Scope).value.loneElement === "read")
    assert(handler.request.headers.get(Actor).value.loneElement === "TEST")
    assert(handler.request.headers.get(OptumCorelationId).value.loneElement == "corrId")
    assert(handler.request.headers.get(CorrelationId.HeaderName).value.loneElement == "corrId")
    val actualResponse: Either[BaseOptumResponseError, OptumResponseSuccess] = handler.handler(rqResponse)
    assert(actualResponse === Left(expectedResponse))
  }

  it should "make a personId & contractNumber request and handle error response without Json body" in {
    val f = new Fixture
    import f._
    val bodyData: String = "{fked}"
    val responseBody = RqBody(bodyData.getBytes)
    val rqResponse = MemoryRqResponse(500, body = responseBody)
    val expectedResponse = OptumResponseError(500, Map.empty, Some(bodyData))

    val handler: HandledRqRequest[Either[BaseOptumResponseError, OptumResponseSuccess]] =
      client.buildRequestWithHandler(reqPidCn, meta.copy(eventDate = Some(DateTimeHelpers.now)))
    assert(handler.request.url == "http://test/member/product/eligibility/v2.0")
    assert(handler.request.method == HttpUtil.Method.Post)
    assert(handler.request.headers.get(Authorization).value.loneElement === "Bearer test-token")
    assert(handler.request.headers.get(ContentType).value.loneElement contains ContentTypeJson)
    assert(handler.request.headers.get(Timestamp).value.size === 1)
    assert(handler.request.headers.get(Scope).value.loneElement === "read")
    assert(handler.request.headers.get(Actor).value.loneElement === "TEST")
    assert(handler.request.headers.get(OptumCorelationId).value.loneElement == "corrId")
    assert(handler.request.headers.get(CorrelationId.HeaderName).value.loneElement == "corrId")
    val actualResponse: Either[BaseOptumResponseError, OptumResponseSuccess] = handler.handler(rqResponse)
    assert(actualResponse === Left(expectedResponse))
  }

  it should "should get the correlationId from the argosy context if not passed in the meta-data" in {
    val f = new Fixture
    import f._

    ArgosyMDCAdapter.context.set(Some(Map(CorrelationId.HeaderName -> "ContextCorrId")))

    val handler: HandledRqRequest[Either[BaseOptumResponseError, OptumResponseSuccess]] =
      client.buildRequestWithHandler(reqPidCn, meta.copy(eventDate = Some(DateTimeHelpers.now), correlationId = None))
    assert(handler.request.headers.get(OptumCorelationId).value.loneElement == "ContextCorrId")
    assert(handler.request.headers.get(CorrelationId.HeaderName).value.loneElement == "ContextCorrId")

  }

  it should "call ws client execute" in {
    val f = new Fixture
    import f._
    val expectedResponse = OptumResponseSuccess(200, Map.empty, Json.parse(SampleResponseGenerator.memPEResponseAll))
    when(rqClient.execute[Either[BaseOptumResponseError, OptumResponseSuccess]](any())(any())).thenReturn(Future.successful(Right(expectedResponse)))

    whenReady(client.findMemberProductEligibility(reqPidCn, meta)) { res =>
      assert(res.isRight)
      assert(res.right.get === expectedResponse)
    }
  }

  it should "call ws client execute with encrypted payload" in {
    val f = new Fixture
    import f._
    val expectedResponse = OptumResponseSuccess(200, Map.empty, Json.parse(SampleResponseGenerator.memPEResponseAll))
    when(rqClient.execute[Either[BaseOptumResponseError, OptumResponseSuccess]](any())(any())).thenReturn(Future.successful(Right(expectedResponse)))

    whenReady(clientWithEncryption.findMemberProductEligibility(reqPidCn, meta)) { res =>
      assert(res.isRight)
      assert(res.right.get === expectedResponse)
    }
  }

  it should "call ws client execute with MemberProductEligibilityRequest" in {
    val f = new Fixture
    import f._
    val expectedResponse = OptumResponseSuccess(200, Map.empty, Json.parse(SampleResponseGenerator.memPEResponseAll))
    when(rqClient.execute[Either[BaseOptumResponseError, OptumResponseSuccess]](any())(any())).thenReturn(Future.successful(Right(expectedResponse)))

    whenReady(client.findByMemberProductEligibilityRequest(eligibilityRequest, Some("corrId"))) { res =>
      assert(res.isRight)
      assert(res.right.get === expectedResponse)
    }
  }

  it should "throw an exception when it receives an empty encrypted payload" in {
    val f = new Fixture
    import f._
    val responseBody = RqBody(SampleResponseGenerator.memPEResponseAll.getBytes)
    val rqResponse = MemoryRqResponse(200, body = responseBody)
    val expectedResponse = OptumResponseSuccess(200, Map.empty, Json.parse(SampleResponseGenerator.memPEResponseAll))

    val handler: HandledRqRequest[Either[BaseOptumResponseError, OptumResponseSuccess]] =
      clientWithEncryption.buildRequestWithHandler(reqPid, meta)(None)
    assertThrows[HandlerException] {
      handler.handler(rqResponse)
    }
  }
}
