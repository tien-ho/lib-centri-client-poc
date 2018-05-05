package com.rallyhealth.optum.client.v8.common

/**
  * HTTP relates helper functions and constants
  */
object HttpUtil {

  /**
    * HTTP methods used in lib-rq-client
    */
  object Method {

    val Post = "POST"
    val Get = "GET"
  }

  /**
    * Http Headers
    */
  object Header {
    val Actor = "actor"
    val Authorization = "Authorization"
    val ClientCredentials = "client_credentials"
    val ContentType = "Content-Type"
    val ContentTypeJson = "application/json"
    val GrantType = "grant_type"
    val OptumCorelationId = "Optum_CID_Ext"
    val Scope = "scope"
    val Timestamp = "timestamp"
  }
}
