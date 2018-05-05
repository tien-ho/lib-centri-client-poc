package com.rallyhealth.optum.client.v8.product.util

object SampleProductEligibilityRequests {

  val requestBodyWithPopulationIdContractNumberAndRelationShip =
    """{
      |	"ProductEligibilityRequest": {
      |		"requestHeader": {
      |			"applicationName": "RALLY",
      |			"transactionId": "RALLY022020179"
      |		},
      |		"consumerDetails": {
      |			"populationId": "POP317",
      |			"contractNumber": "0191690",
      |			"relationship": "EE",
      |			"dateOfBirth": "1970-06-14T00:00:00Z"
      |		},
      |		"requestDetails": {
      |			"eventDate": "2017-05-02T00:00:00Z"
      |		}
      |	}
      |}
      | """.stripMargin

  val requestBodyWithPopulationIdContractNumberAndRelationShipDescription =
    """{
      |	"ProductEligibilityRequest": {
      |		"requestHeader": {
      |			"applicationName": "RALLY",
      |			"transactionId": "RALLY022020179"
      |		},
      |		"consumerDetails": {
      |			"populationId": "POP317",
      |			"contractNumber": "0191690",
      |			"relationshipDescription": "SUBSCRIBER/RECIPIENT",
      |			"dateOfBirth": "1970-06-14T00:00:00Z"
      |		},
      |		"requestDetails": {
      |			"eventDate": "2017-05-02T00:00:00Z"
      |		}
      |	}
      |}
      | """.stripMargin

  val requestBodyWithContractNumberNoEventDate =
    """{
      |	"ProductEligibilityRequest": {
      |		"requestHeader": {
      |			"applicationName": "RALLY",
      |			"transactionId": "RALLY022020179"
      |		},
      |		"consumerDetails": {
      |			"populationId": "POP317",
      |			"contractNumber": "0191690",
      |			"relationship": "EE",
      |			"dateOfBirth": "1970-06-14T00:00:00Z"
      |		}
      |	}
      |}
      | """.stripMargin

  val requestBodyWithoutPopulationId =
    """{
      |	"ProductEligibilityRequest": {
      |		"requestHeader": {
      |			"applicationName": "RALLY",
      |			"transactionId": "RALLY022020179"
      |		},
      |		"consumerDetails": {
      |			"contractNumber": "0191690",
      |			"relationship": "EE",
      |			"dateOfBirth": "1970-06-14T00:00:00Z"
      |		},
      |		"requestDetails": {
      |			"eventDate": "2017-05-02T00:00:00Z"
      |		}
      |	}
      |}
      | """.stripMargin
}
