package com.rallyhealth.optum.client.script

import com.rallyhealth.optum.client.v8.common.model.OptumResponseSuccess
import com.rallyhealth.optum.client.v8.product.model.{OptumProductEligibilityRequestMeta, OptumProductEligibilityRequestWithPopulationIdAndRelationshipCode}
import com.rallyhealth.optum.client.v8.product.util.SampleProductEligibilityResponse
import com.rallyhealth.optum.client.v8.product.ws.{WSProductEligibilityClient, WSWithOAuthProductEligibilityClient}
import org.joda.time.DateTime
import org.scalatest.FlatSpec
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Seconds, Span}
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits._

class ProductEligibilitySpec
  extends FlatSpec
  with ScalaFutures {

  it should "invoke product eligibility api and return a 200 with the expected response body" in {

    import com.rallyhealth.optum.client.script.AuthClientSetup._

    val mpClient = new WSProductEligibilityClient(optumClientRallyPeConfig, rqClient)
    val ompClient = new WSWithOAuthProductEligibilityClient(cachedOAuthClient, mpClient)

    val req = OptumProductEligibilityRequestWithPopulationIdAndRelationshipCode(
      populationId = "POP317",
      contractNumber = "0168504",
      relationshipCode = "EE",
      dateOfBirth = DateTime.parse("1986-10-26T00:00:00Z")
    )
    val meta = OptumProductEligibilityRequestMeta(
      applicationName = "RALLY",
      transactionId = "1a")
    val expectedResponse = OptumResponseSuccess(200, Map.empty, Json.parse(SampleProductEligibilityResponse.integrationTestSuccessResponse))

    whenReady(ompClient.findProductEligibility(req, meta), timeout(Span(60, Seconds))) {
      res =>
        assert(res.isRight)
        assert(res.right.get.body === expectedResponse.body)
    }
  }
}
