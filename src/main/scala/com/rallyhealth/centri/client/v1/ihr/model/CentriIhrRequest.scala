package com.rallyhealth.centri.client.v1.ihr.model


import org.joda.time.DateTime
import play.api.libs.json.{Format, Json}

/**
  * A request that can be logged
  */

trait LoggableRequest

/**
  * Represents the request object to invoke Optums Member Product Eligibility API.
  */
sealed trait CentriIhrRequest extends LoggableRequest {

}

/**
  *
  * @param test
  */
case class IhrHealthCheckRequest(test: String,
                                  correlationId: String
                                ) extends CentriIhrRequest {
}

object IhrHealthCheckRequest {
  implicit val format: Format[IhrHealthCheckRequest] = Json.format[IhrHealthCheckRequest]
}


case class RequestMetaData(applicationName: String,
                           transactionId: String,
                           correlationId: Option[String] = None)