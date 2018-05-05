package com.rallyhealth.optum.client.v8.auth.model

import com.rallyhealth.optum.client.v8.common.ApiConstant
import ApiConstant._
import com.rallyhealth.optum.client.v8.common.model.BaseOptumResponseError
import com.rallyhealth.spartan.v2.datetime.DateTimeHelpers
import org.joda.time.DateTime
import play.api.libs.functional.syntax._
import play.api.libs.json._

/**
  * Represents Optum's OAuth responses
  */
sealed trait OptumOAuthResponse

/**
  * The OAuth2 access token response from Optum API.
  *
  * @param token Access token string
  * @param expiresIn Number of seconds after which the token will expire.
  */
case class OptumAccessTokenResponse(token: String, expiresIn: Int) extends OptumOAuthResponse {
  //Time at which token expires. A 10 sec threshhold to expire early.
  val expiresAt: DateTime = DateTimeHelpers.now.plusSeconds(expiresIn - OAuthTokenThresholdSec)
}

object OptumAccessTokenResponse {

  implicit val optumAccessTokenResponseReads: Reads[OptumAccessTokenResponse] = (
    (__ \ AccessToken).read[String] and
    (__ \ ExpiresIn).read[Int]
  )(OptumAccessTokenResponse.apply _)

}

/**
  * Exception thrown when Optum responds with either HTTP error or an application error when talking to
  * Optum API for OAuth access token.
  *
  * @param httpStatus The HTTP status on the response
  * @param appError The application error if any sent in the body of the response by Optum
  * @param body The body in the response as String if any
  */
case class OptumOAuthException(httpStatus: Int, appError: Option[String], body: Option[String])
  extends RuntimeException(s"httpStatus=$httpStatus appError=$appError")
  with OptumOAuthResponse
  with BaseOptumResponseError
