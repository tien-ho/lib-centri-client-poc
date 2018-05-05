package com.rallyhealth.optum.client.v8.product.ws

import com.rallyhealth.optum.client.v8.product.model.{OptumProductEligibilityRequestMeta, OptumProductEligibilityRequestWithPopulationIdAndRelationshipCode, OptumProductEligibilityRequestWithPopulationIdAndRelationshipDescription, OptumProductEligibilityRequestWithRelationshipCode}
import com.rallyhealth.optum.client.v8.product.util.SampleProductEligibilityRequests
import org.joda.time.DateTime
import org.scalatest.FunSpec
import play.api.libs.json.Json

class ProductEligibilityRequestBodyBuilderSpec extends FunSpec {

  describe("ProductEligibilityRequestBodyBuilder") {
    it("should convert optumProductEligibilityRequestWithRelationshipCode to correctJsonFormat") {

      val requestMeta = OptumProductEligibilityRequestMeta(applicationName = "RALLY", transactionId = "RALLY022020179", eventDate = Some(DateTime.parse("2017-05-02T00:00:00Z")))
      val optumProductEligibilityRequestWithRelationshipCode = OptumProductEligibilityRequestWithRelationshipCode(
        contractNumber = "0191690",
        relationshipCode = "EE",
        dateOfBirth = DateTime.parse("1970-06-14T00:00:00Z")
      )

      val generetedRequestBody = ProductEligibilityRequestBodyBuilder.buildJsonRequestBody(optumProductEligibilityRequestWithRelationshipCode, requestMeta)
      assert(Json.parse(SampleProductEligibilityRequests.requestBodyWithoutPopulationId).toString() == Json.parse(generetedRequestBody).toString())
    }
  }

  describe("ProductEligibilityRequestBodyBuilder") {
    it("should convert OptumProductEligibilityRequestWithPopulationIdAndRelationshipCode to correctJsonFormat") {

      val requestMeta = OptumProductEligibilityRequestMeta(applicationName = "RALLY", transactionId = "RALLY022020179", eventDate = Some(DateTime.parse("2017-05-02T00:00:00Z")))
      val optumProductEligibilityRequestWithPopulationIdAndRelationshipCode = OptumProductEligibilityRequestWithPopulationIdAndRelationshipCode(
        populationId = "POP317",
        contractNumber = "0191690",
        relationshipCode = "EE",
        dateOfBirth = DateTime.parse("1970-06-14T00:00:00Z")
      )

      val generetedRequestBody = ProductEligibilityRequestBodyBuilder.buildJsonRequestBody(optumProductEligibilityRequestWithPopulationIdAndRelationshipCode, requestMeta)
      assert(Json.parse(SampleProductEligibilityRequests.requestBodyWithPopulationIdContractNumberAndRelationShip).toString() == Json.parse(generetedRequestBody).toString())
    }
  }

  describe("ProductEligibilityRequestBodyBuilder") {
    it("should convert OptumProductEligibilityRequestWithPopulationIdAndRelationshipDescription to correctJsonFormat") {

      val requestMeta = OptumProductEligibilityRequestMeta(applicationName = "RALLY", transactionId = "RALLY022020179", eventDate = Some(DateTime.parse("2017-05-02T00:00:00Z")))
      val optumProductEligibilityRequestWithPopulationIdAndRelationshipDescription = OptumProductEligibilityRequestWithPopulationIdAndRelationshipDescription(
        populationId = "POP317",
        contractNumber = "0191690",
        relationshipDescription = "SUBSCRIBER/RECIPIENT",
        dateOfBirth = DateTime.parse("1970-06-14T00:00:00Z")
      )

      val generetedRequestBody = ProductEligibilityRequestBodyBuilder.buildJsonRequestBody(optumProductEligibilityRequestWithPopulationIdAndRelationshipDescription, requestMeta)
      assert(Json.parse(SampleProductEligibilityRequests.requestBodyWithPopulationIdContractNumberAndRelationShipDescription).toString() == Json.parse(generetedRequestBody).toString())
    }
  }

  describe("ProductEligibilityRequestBodyBuilder") {
    it("should convert requestBodyWithContractNumberNoEventDate to correctJsonFormat") {

      val requestMeta = OptumProductEligibilityRequestMeta(applicationName = "RALLY", transactionId = "RALLY022020179", None)
      val optumProductEligibilityRequestWithPopulationIdAndRelationshipDescription = OptumProductEligibilityRequestWithPopulationIdAndRelationshipCode(
        populationId = "POP317",
        contractNumber = "0191690",
        relationshipCode = "EE",
        dateOfBirth = DateTime.parse("1970-06-14T00:00:00Z")
      )

      val generetedRequestBody = ProductEligibilityRequestBodyBuilder.buildJsonRequestBody(optumProductEligibilityRequestWithPopulationIdAndRelationshipDescription, requestMeta)
      assert(Json.parse(SampleProductEligibilityRequests.requestBodyWithContractNumberNoEventDate).toString() == Json.parse(generetedRequestBody).toString())
    }
  }
}
