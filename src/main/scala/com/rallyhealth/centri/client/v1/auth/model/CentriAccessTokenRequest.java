package com.rallyhealth.centri.client.v1.auth.model;

/**
  * @param clientId The clientId in client credentials to use for OAuth2
  * @param clientSecret The client secret in client credentials to use for OAtuh2
  * @param grantType The type of grant requested in OAuth2. Default is client_credentials
  */
case class CentriAccessTokenRequest(clientId: String, clientSecret: String, grantType: String = ClientCredentials) {
    lazy val basicAuthToken = Base64.getEncoder.encodeToString(s"$clientId:$clientSecret".getBytes)
    lazy val authHeader = Authorization -> s"Basic $basicAuthToken"
    lazy val authBody = Map(GrantType -> Seq(grantType))
}
