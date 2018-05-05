package com.rallyhealth.optum.client.v8.common.model

import play.api.libs.json.{Json, Format, JsValue}

/**
  * The common base trait for all the Optum responses.
  */
sealed trait OptumResponse {
  def httpStatus: Int
  def httpHeaders: Map[String, Seq[String]]
}

/**
  * Represents Optum's response that is an error
  */
trait BaseOptumResponseError

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
case class OptumResponseSuccess(
  httpStatus: Int,
  httpHeaders: Map[String, Seq[String]],
  body: JsValue
) extends OptumResponse

object OptumResponseSuccess {

  implicit val format: Format[OptumResponseSuccess] = Json.format[OptumResponseSuccess]
}

/**
  * Represents a HTTP error response from Optum.
  *
  * @param httpStatus The HTTP status on the received response
  * @param httpHeaders All the HTTP headers received in the Optums response
  * @param body The body of the error response if any in [[String]] format
  */
case class OptumResponseError(
  httpStatus: Int,
  httpHeaders: Map[String, Seq[String]],
  body: Option[String]
) extends RuntimeException(s"httpStatus=$httpStatus")
  with BaseOptumResponseError

object OptumResponseError {

  implicit val format: Format[OptumResponseError] = Json.format[OptumResponseError]
}

/**
  * Represents a HTTP success response but with an application error code from Optum.
  *
  * @param httpStatus The HTTP status on the received response
  * @param httpHeaders All the HTTP headers received in the Optums response
  * @param appErrorCode The application level error code that Optum responds
  * @param body The body of the error response if any in Json format
  */
case class OptumResponseAppError(
  httpStatus: Int,
  httpHeaders: Map[String, Seq[String]],
  appErrorCode: String,
  body: JsValue
) extends RuntimeException(s"httpStatus=$httpStatus appErrorCode=$appErrorCode") with BaseOptumResponseError

object OptumResponseAppError {

  implicit val format: Format[OptumResponseAppError] = Json.format[OptumResponseAppError]
}
