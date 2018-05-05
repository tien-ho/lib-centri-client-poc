package com.rallyhealth.centri.client.v1.common.model

import play.api.libs.json.{Json, Format, JsValue}

/**
  * The common base trait for all the Optum responses.
  */
sealed trait CentriResponse {
    def httpStatus: Int
    def httpHeaders: Map[String, Seq[String]]
}

/**
  * Represents Optum's response that is an error
  */
trait BaseCentriResponseError

/**
  * The response model that is parsed from Optum's API. The
  * returned response is converted to JSON in this model and kept as is. This response and model will be
  * used in two different service - Programs & Affiliation Calculation Service differently with different
  * parsing and validation needs. Hence, the parsing logic and validation is not present in this lib.
  *
  *
  * @param httpStatus The HTTP status on the received response
  * @param httpHeaders All the HTTP headers received in the Optums response
  * @param body The parsed JSON if response is parseable. JsNull if there is no body.
  */
case class CentriResponseSuccess(
                                   httpStatus: Int,
                                   httpHeaders: Map[String, Seq[String]],
                                   body: JsValue
                               ) extends CentriResponse

object CentriResponseSuccess {

    implicit val format: Format[CentriResponseSuccess] = Json.format[CentriResponseSuccess]
}

/**
  * Represents a HTTP error response from Optum.
  *
  * @param httpStatus The HTTP status on the received response
  * @param httpHeaders All the HTTP headers received in the Optums response
  * @param body The body of the error response if any in [[String]] format
  */
case class CentriResponseError(
                                 httpStatus: Int,
                                 httpHeaders: Map[String, Seq[String]],
                                 body: Option[String]
                             ) extends RuntimeException(s"httpStatus=$httpStatus")
    with BaseCentriResponseError

object CentriResponseError {

    implicit val format: Format[CentriResponseError] = Json.format[CentriResponseError]
}

/**
  * Represents a HTTP success response but with an application error code from Optum.
  *
  * @param httpStatus The HTTP status on the received response
  * @param httpHeaders All the HTTP headers received in the Optums response
  * @param appErrorCode The application level error code that Optum responds
  * @param body The body of the error response if any in Json format
  */
case class CentriResponseAppError(
                                    httpStatus: Int,
                                    httpHeaders: Map[String, Seq[String]],
                                    appErrorCode: String,
                                    body: JsValue
                                ) extends RuntimeException(s"httpStatus=$httpStatus appErrorCode=$appErrorCode") with BaseCentriResponseError

object CentriResponseAppError {

    implicit val format: Format[CentriResponseAppError] = Json.format[CentriResponseAppError]
}
