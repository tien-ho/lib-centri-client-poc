package com.rallyhealth.optum.client.v8.product.model

import org.joda.time.DateTime

sealed trait OptumProductEligibilityRequest

/**
  * @note
  * Couldn't able to find clear difference between relationshipCode and relationshipDescription
  * As pre optum Document:
  * RelationshipCode or relationship: Code that identifies member’s relationship to the subscriber.
  *
  * Situational.Required if relationship description is not provided.
  *
  * When neither Relationship(RelationshipCode) nor Relationship Description is passed in request, 400 Error, BAD REQUEST is returned.
  *
  * When both Relationship(RelationshipCode) and Relationship Description are passed in request, 400 Error, BAD REQUEST is returned
  *
  * Relationship will be used to determine if member is eligible for the products with relationship restrictions.
  *
  * Relationship Code -> Value
  * EE -> Subscriber/Recipient
  * RR -> Retiree
  * SP -> Spouse
  * DP -> Domestic Partner
  * SS -> Surviving Spouse
  * CD -> Collateral Dependent (adult dependents such as a parent or domestic partner, if DP is not used by the group)
  * CH -> Child
  * NB -> Newborn
  * HC -> Disabled (Handicapped) Child
  * ST -> Student
  * SC -> Stepchild
  * SD -> Sponsored Dependent (children that are in a parent/child relationship with the enrollee, but are not a natural, adopted or stepchild i.e. niece)
  * OT -> Other
  * ------------------------------------------------------------------
  * Relationship Description : Description of the member’s relationship to subscriber
  *
  * Situational. Required if relationship is not provided.
  *
  * When neither Relationship nor Relationship Description is passed in request, 400 Error, BAD
  *
  * RelationshipDescription -> Value
  * SUBSCRIBER/RECIPIENT -> Subscriber/Recipient, Retiree
  * SPOUSE/DOMESTIC -> Spouse, Domestic Partner, Surviving Spouse
  * CHILD/OTHER -> Collateral Dependent, Child, Newborn, Disabled Child, Student, Stepchild, Sponsored Dependent, Other
  *
  */

/**
  * Model to make optum product eligibility request without populationId
  * @param contractNumber Member’sContract/PolicyNumber
  * @param relationshipCode Code that identifies member’s relationship to the subscriber
  * @param dateOfBirth Member’s Date of Birth format e.g.2014-04-25T00:00:00Z.T00:00:00Z will need tobe entered for the time.
  */
case class OptumProductEligibilityRequestWithRelationshipCode(
  contractNumber: String,
  relationshipCode: String,
  dateOfBirth: DateTime
) extends OptumProductEligibilityRequest

/**
  * Model to make optum product eligibility request with population id and relationship code
  * @param contractNumber  Member’sContract/PolicyNumber
  * @param populationId Member’s populationID
  * @param relationshipCode Code that identifies member’s relationship to the subscriber
  * @param dateOfBirth Member’s Date of Birth format e.g.2014-04-25T00:00:00Z.T00:00:00Z will need tobe entered for the time.
  */
case class OptumProductEligibilityRequestWithPopulationIdAndRelationshipCode(
  contractNumber: String,
  populationId: String,
  relationshipCode: String,
  dateOfBirth: DateTime
) extends OptumProductEligibilityRequest

/**
  *
  * @param contractNumber Member’sContract/PolicyNumber
  * @param populationId Member’s populationID
  * @param relationshipDescription Description of the member’s relationship to subscriber
  * @param dateOfBirth Member’s Date of Birth format e.g.2014-04-25T00:00:00Z.T00:00:00Z will need tobe entered for the time.
  */
case class OptumProductEligibilityRequestWithPopulationIdAndRelationshipDescription(
  contractNumber: String,
  populationId: String,
  relationshipDescription: String,
  dateOfBirth: DateTime
) extends OptumProductEligibilityRequest

/**
  * The meta in the Optums's request sent to ProductEligibility API endpoint. Anything which is not directly related
  * to user details that is being requested like filter, eventDate, transactionId etc lands here.
  *
  * @param applicationName The name of the application making the request
  * @param transactionId   Unique transaction ID to be passed for every product eligibility request call
  *                        from API consumers. This field will be a string with no spaces.  T
  *                        his unique ID is used for request/response traceability.
  *                        No special characters are allowed for this field. Max 60 char
  * @param eventDate       Effective Date attribute will be used to provide point in time member coverage information
  *                        and product capabilities.
  */
case class OptumProductEligibilityRequestMeta(
  applicationName: String,
  transactionId: String,
  eventDate: Option[DateTime] = None
)
