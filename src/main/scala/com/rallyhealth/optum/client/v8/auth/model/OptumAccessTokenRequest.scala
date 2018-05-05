package com.rallyhealth.optum.client.v8.auth.model

import java.util.Base64
import com.rallyhealth.optum.client.v8.common.HttpUtil
import HttpUtil.Header._

/**
  * @param clientId The clientId in client credentials to use for OAuth2
  * @param clientSecret The client secret in client credentials to use for OAtuh2
  * @param grantType The type of grant requested in OAuth2. Default is client_credentials
  */
case class OptumAccessTokenRequest(clientId: String, clientSecret: String, grantType: String = ClientCredentials) {
  lazy val basicAuthToken = Base64.getEncoder.encodeToString(s"$clientId:$clientSecret".getBytes)
  lazy val authHeader = Authorization -> s"Basic $basicAuthToken"
  lazy val authBody = Map(GrantType -> Seq(grantType))
}
