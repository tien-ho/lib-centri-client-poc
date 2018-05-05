package com.rallyhealth.optum.client.v8.product.ws

import com.rallyhealth.optum.client.v8.product.model._
import com.rallyhealth.spartan.v2.datetime.DateTimeHelpers
import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}
import play.api.libs.json.Json

object ProductEligibilityRequestBodyBuilder {

  private val printFormat: DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'00:00:00'Z'")

  private case class RequestHeader(applicationName: String, transactionId: String)
  private implicit val requestHeadJsonFormat = Json.format[RequestHeader]

  private case class ConsumerDetails(
    populationId: Option[String],
    contractNumber: String,
    relationship: Option[String],
    relationshipDescription: Option[String],
    dateOfBirth: String
  )
  private implicit val consumerDetailsJsonFormat = Json.format[ConsumerDetails]

  private case class RequestDetails(eventDate: String)
  private implicit val requestDetailsJsonFormat = Json.format[RequestDetails]

  private case class ProductEligibilityRequest(
    requestHeader: RequestHeader,
    consumerDetails: ConsumerDetails,
    requestDetails: Option[RequestDetails]
  )
  private implicit val productEligibilityRequestJsonFormat = Json.format[ProductEligibilityRequest]

  private case class RequestBody(ProductEligibilityRequest: ProductEligibilityRequest)
  private implicit val requestBody = Json.format[RequestBody]

  /**
    * Method to convert productEligibilityRequest to optum friendly jsonRequest body
    */
  def buildJsonRequestBody(productEligibilityRequest: OptumProductEligibilityRequest, requestMetadata: OptumProductEligibilityRequestMeta): String = {
    val requestHeader = RequestHeader(requestMetadata.applicationName, requestMetadata.transactionId)
    val requestDetails = requestMetadata.eventDate.map(date => RequestDetails(DateTimeHelpers.stringify(date, printFormat)))
    val consumerDetails = productEligibilityRequest match {
      case OptumProductEligibilityRequestWithRelationshipCode(contractNumber, relationshipCode, dateOfBirth) =>
        ConsumerDetails(None, contractNumber, relationship = Some(relationshipCode), None, DateTimeHelpers.stringify(dateOfBirth, printFormat))
      case OptumProductEligibilityRequestWithPopulationIdAndRelationshipCode(contractNumber, populationId, relationshipCode, dateOfBirth) =>
        ConsumerDetails(Some(populationId), contractNumber, relationship = Some(relationshipCode), None, DateTimeHelpers.stringify(dateOfBirth, printFormat))
      case OptumProductEligibilityRequestWithPopulationIdAndRelationshipDescription(contractNumber, populationId, relationshipDescription, dateOfBirth) =>
        ConsumerDetails(Some(populationId), contractNumber, None, relationshipDescription = Some(relationshipDescription), DateTimeHelpers.stringify(dateOfBirth, printFormat))
    }
    val eligibilityRequest = ProductEligibilityRequest(requestHeader, consumerDetails, requestDetails)
    val requestBody = RequestBody(eligibilityRequest)
    Json.toJson(requestBody).toString()
  }
}
