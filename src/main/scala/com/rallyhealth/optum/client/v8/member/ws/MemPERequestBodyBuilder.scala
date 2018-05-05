package com.rallyhealth.optum.client.v8.member.ws

import com.rallyhealth.optum.client.v8.member.model._
import com.rallyhealth.optum.client.v8.member.model._
import com.rallyhealth.spartan.v2.datetime.DateTimeHelpers
import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}
import play.api.libs.json.{Format, Json}

/**
  * Responsible for building the Json requests sent to Optum endpoint.
  */
object MemPERequestBodyBuilder {
  private val printFormat: DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'00:00:00'Z'")

  /*
   * Case classes to build the Optum request Json. Refer document to understand
   * definitions: https://docs.google.com/document/d/11USBFMOarBuB1E5oGsVZbSkP4T1rH15T64dGx05ZON4/edit#
   *
   */
  private case class MemPeBuilder(`MemberProductEligibilityRequest`: MemberProductEligibilityRequest)
  private object MemPeBuilder {
    implicit val format: Format[MemPeBuilder] = Json.format[MemPeBuilder]
  }

  case class MemberProductEligibilityRequest(
    requestHeader: RequestHeader,
    filteringAttributes: FilteringAttributes,
    requestDetails: RequestDetails,
    idSet: Option[IdSet] = None,
    consumerDetails: Option[ConsumerDetails] = None
  ) extends LoggableRequest

  object MemberProductEligibilityRequest {
    implicit val format: Format[MemberProductEligibilityRequest] = Json.format[MemberProductEligibilityRequest]
  }

  case class RequestHeader(applicationName: String, transactionId: String)
  object RequestHeader {
    implicit val format: Format[RequestHeader] = Json.format[RequestHeader]
  }

  case class FilteringAttributes(includeExtendedAttributes: Boolean, applyFilters: Boolean)
  object FilteringAttributes {
    implicit val format: Format[FilteringAttributes] = Json.format[FilteringAttributes]
  }

  case class RequestDetails(requestType: String, searchType: String, eventDate: Option[String] = None)
  object RequestDetails {
    implicit val format: Format[RequestDetails] = Json.format[RequestDetails]
  }

  case class IdSet(personId: Long)
  object IdSet {
    implicit val format: Format[IdSet] = Json.format[IdSet]
  }

  case class ConsumerDetails(
    firstName: Option[String] = None,
    lastName: Option[String] = None,
    dateOfBirth: Option[String] = None,
    searchId: Option[String] = None,
    contractNumbers: Option[Seq[String]] = None,
    zipCode: Option[String] = None
  )
  object ConsumerDetails {
    implicit val format: Format[ConsumerDetails] = Json.format[ConsumerDetails]
  }

  def memberProductEligibilityRequestBody(memberProductEligibilityRequest: MemberProductEligibilityRequest): String = Json.toJson(
    MemPeBuilder(memberProductEligibilityRequest)
  ).toString()

  /**
    * Builds the Big5 request and returns a JSON representation
    */
  def big5RequestBody(meta: OptumRequestMetaData, req: Big5MemPERequest): String = Json.toJson(
    {
      val baseRequest = buildBaseMemPeBuilder(meta, req.requestType)
      baseRequest.copy(
        `MemberProductEligibilityRequest` = baseRequest.`MemberProductEligibilityRequest`.copy(
          consumerDetails = Some(ConsumerDetails(
            firstName = Some(req.firstName),
            lastName = Some(req.lastName),
            dateOfBirth = Some(req.dateOfBirth),
            searchId = Some(req.searchId),
            contractNumbers = Some(req.contractNumbers)
          ))
        ))
    }
  ).toString()

  /**
    * Builds PersonId only request and returns string representation of Json.
    */
  def personIdRequestBody(meta: OptumRequestMetaData, req: PersonIdMemPERequest): String = Json.toJson(
    {
      val baseRequest = buildBaseMemPeBuilder(meta, req.requestType)
      baseRequest.copy(
        `MemberProductEligibilityRequest` = baseRequest.`MemberProductEligibilityRequest`.copy(
          idSet = Some(IdSet(personId = req.personId.toLong))
        ))
    }
  ).toString()

  /**
    * Builds PersonId and contractNumber request and returns string representation of Json.
    */
  def personIdContractNumberRequestBody(meta: OptumRequestMetaData, req: PersonIdContractNumberMemPERequest): String = Json.toJson(
    {
      val baseRequest = buildBaseMemPeBuilder(meta, req.requestType)
      baseRequest.copy(
        `MemberProductEligibilityRequest` = baseRequest.`MemberProductEligibilityRequest`.copy(
          idSet = Some(IdSet(personId = req.personId.toLong)),
          consumerDetails = Some(ConsumerDetails(contractNumbers = Some(req.contractNumbers)))
        ))
    }
  ).toString()

  private def buildBaseMemPeBuilder(meta: OptumRequestMetaData, rtype: RequestType): MemPeBuilder =
    MemPeBuilder(MemberProductEligibilityRequest(
      RequestHeader(
        applicationName = meta.applicationName,
        transactionId = meta.transactionId
      ),
      FilteringAttributes(
        includeExtendedAttributes = meta.filteringRules.includeExtendedAttribtues,
        applyFilters = meta.filteringRules.applyPolicyFilters
      ),
      RequestDetails(
        requestType = rtype.toString,
        searchType = meta.searchType.reqString,
        eventDate = meta.eventDate.map(DateTimeHelpers.stringify(_, printFormat))
      )
    ))

}
