package com.rallyhealth.optum.client.v8.member.model

import org.joda.time.DateTime
import play.api.libs.json.{Json, Format}

/**
  * A request that can be logged
  */

trait LoggableRequest

/**
  * Represents the request object to invoke Optums Member Product Eligibility API.
  */
sealed trait OptumMemPERequest extends LoggableRequest {
  def requestType: RequestType
}

/**
  *
  * @param firstName Member's first name
  * @param lastName Member's last name
  * @param dateOfBirth Member's Date of Birth (e.g. e.g.2014-04-25T00:00:00Z)
  * @param searchId Unique identifier (Can be Subscriber ID, Social Security Number or AlternateID).
  * @param contractNumbers Identifies list of insurance company contract numbers for client that member is associated with.
  */
case class Big5MemPERequest(
  firstName: String,
  lastName: String,
  dateOfBirth: String,
  searchId: String,
  contractNumbers: Seq[String]
) extends OptumMemPERequest {
  override def requestType: RequestType = Big5
}

object Big5MemPERequest {

  implicit val format: Format[Big5MemPERequest] = Json.format[Big5MemPERequest]
}

/**
  * @param personId The personId of the user for with MemPE response is needed
  */
case class PersonIdMemPERequest(
  personId: String
) extends OptumMemPERequest {
  override def requestType: RequestType = PersonId
}

object PersonIdMemPERequest {

  implicit val format: Format[PersonIdMemPERequest] = Json.format[PersonIdMemPERequest]
}

/**
  * @param personId The personId of the user for with MemPE response is needed
  * @param contractNumbers List of policyNumbers that the user belongs to
  */
case class PersonIdContractNumberMemPERequest(
  personId: String,
  contractNumbers: Seq[String]
) extends OptumMemPERequest {
  override def requestType: RequestType = PersonIdPlusContractNumber
}

object PersonIdContractNumberMemPERequest {

  implicit val format: Format[PersonIdContractNumberMemPERequest] = Json.format[PersonIdContractNumberMemPERequest]
}

/**
  * Enumerations of different request types supported by Optum.
  * Ref: https://docs.google.com/document/d/11USBFMOarBuB1E5oGsVZbSkP4T1rH15T64dGx05ZON4/edit#
  *
  * @param reqString The String that will be used in the JSON request to Optum endpoint
  */
class RequestType(reqString: String) {
  override def toString: String = reqString
  def toStatString: String = reqString.replaceAll("_", "").toLowerCase
}
case object Big5 extends RequestType("BIG5")
case object NamePlusZip extends RequestType("NAME_SEARCH_PLUS_ZIP")
case object PersonId extends RequestType("PERSONID")
case object PersonIdPlusContractNumber extends RequestType("PERSONID_PLUS_CONTRACTNUMBER")

/**
  * Enumerations of different search types supported by Optum.
  * Ref: https://docs.google.com/document/d/11USBFMOarBuB1E5oGsVZbSkP4T1rH15T64dGx05ZON4/edit#
  *
  * @param reqString The String that will be used in the JSON request to Optum endpoint
  */
sealed abstract class SearchType(val reqString: String)
case object All extends SearchType("ALL")
case object Member extends SearchType("MEMBER")
case object Product extends SearchType("PRODUCT")

/**
  * Filtering rules supported by Optum.
  * Ref: https://docs.google.com/document/d/11USBFMOarBuB1E5oGsVZbSkP4T1rH15T64dGx05ZON4/edit#
  *
  * @param applyPolicyFilters uses the policy number to filter the golden record
  * @param includeExtendedAttribtues gets extended attributes from CDB
  */
case class FilteringRules(applyPolicyFilters: Boolean, includeExtendedAttribtues: Boolean)

/**
  * The meta in the Optums's request sent to MemPE API endpoint. Anything which is not directly related
  * to user details that is being requested like filter, eventDate, transactionId etc lands here.
  *
  * @param applicationName The name of the application making the request
  * @param transactionId Unique transaction ID to be passed for every product eligibility request call
  *                      from API consumers. This field will be a string with no spaces.  T
  *                      his unique ID is used for request/response traceability.
  *                      No special characters are allowed for this field. Max 60 char
  * @param searchType Type of data returned for user.
  * @param eventDate Effective Date attribute will be used to provide point in time member coverage information
  *                  and product capabilities.
  * @param filteringRules Various filtering rules allowed by Optum.
  */
case class OptumRequestMetaData(
  applicationName: String,
  transactionId: String,
  searchType: SearchType,
  eventDate: Option[DateTime] = None,
  correlationId: Option[String] = None,
  filteringRules: FilteringRules = FilteringRules(applyPolicyFilters = false, includeExtendedAttribtues = false)
)
