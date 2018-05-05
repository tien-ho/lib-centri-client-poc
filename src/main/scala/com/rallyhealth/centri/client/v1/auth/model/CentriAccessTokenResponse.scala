package com.rallyhealth.centri.client.v1.auth.model

import com.rallyhealth.centri.client.v1.common.ApiConstant
import ApiConstant._
import com.rallyhealth.centri.client.v1.common.model.BaseCentriResponseError
import com.rallyhealth.spartan.v2.datetime.DateTimeHelpers
import org.joda.time.DateTime
import play.api.libs.functional.syntax._
import play.api.libs.json._

/**
  * Represents Optum's OAuth responses
  */
sealed trait CentriOAuthResponse

/**
  * The OAuth2 access token response from Optum API.
  *
  * @param token Access token string
  * @param expiresIn Number of seconds after which the token will expire.
  */
case class CentriAccessTokenResponse(token: String, expiresIn: Int) extends CentriOAuthResponse {
    //Time at which token expires. A 10 sec threshhold to expire early.
    val expiresAt: DateTime = DateTimeHelpers.now.plusSeconds(expiresIn - OAuthTokenThresholdSec)
}

object CentriAccessTokenResponse {

    implicit val centriAccessTokenResponseReads: Reads[CentriAccessTokenResponse] = (
        (__ \ AccessToken).read[String] and
            (__ \ ExpiresIn).read[Int]
        )(CentriAccessTokenResponse.apply _)

}

/**
  * Exception thrown when Optum responds with either HTTP error or an application error when talking to
  * Optum API for OAuth access token.
  *
  * @param httpStatus The HTTP status on the response
  * @param appError The application error if any sent in the body of the response by Optum
  * @param body The body in the response as String if any
  */
case class CentriOAuthException(httpStatus: Int, appError: Option[String], body: Option[String])
    extends RuntimeException(s"httpStatus=$httpStatus appError=$appError")
        with CentriOAuthResponse
        with BaseCentriResponseError

