package com.rallyhealth.centri.client.v1.common

import com.rallyhealth.spartan.v2.config.RallyConfig

import scala.concurrent.duration.{FiniteDuration, Duration}

/**
  * Common configuration for all optum clients.
  *
  * All parameters loaded in this config are applicable to almost all optum apis.
  *
  */
trait CentriClientConfig {

    /**
      * Optum MemPE endpoint base URL
      */
    val baseUrl: String

    /**
      * Optum Config-Intake endpoint URL.
      * A separate base URL config. is needed for the Config-Intake Optum endpoint,
      * because we want to be able to configure them separately
      * (for example, we may use different MemPE and Config-Intake end-points (stage vs test) in the DL integration env.)
      */
    val configIntakeBaseUrl: String

    /**
      * Required field for hitting the optum api. It must be set, but can be any value. If it is not set a 401 is returned.
      *
      * Recommend that you set this to the ClientId you used during oauth setup
      */
    val actor: String

    /**
      * The OAuth-like scope necessary for interacting with the optum API. Only known value is 'read'
      */
    val scope: String

    /**
      * Optum OAuth Authority URL
      */
    val oauthBaseUrl: String

    /**
      * Optum OAuth Path following the base url oauthBaseUrl
      */
    val oauthPath: String

    /**
      * Duration for requests when talking to Optum
      */
    val requestTimeout: FiniteDuration

    val encryptedPayloadKey: String = "encryptedPayload"
}

class CentriClientRallyConfig(underlying: RallyConfig) extends CentriClientConfig {
    import scala.concurrent.duration._

    protected val config = underlying.sub("optum.client")

    override val baseUrl: String = config.get("baseUrl", "https://api-stg.optum.com:8443/api/wpi/stage")

    override val configIntakeBaseUrl: String = config.get("configIntakeBaseUrl", "https://api-stg.optum.com:8443/api/wpi/test")

    override val actor = config.get("actor", "MPE")

    override val scope = config.get("scope", "read")

    override val oauthBaseUrl: String = config.get("oauthBaseUrl", "https://api-stg.optum.com:8443/auth")

    override val oauthPath: String = config.get("oauthPath", "/oauth/v2/token")

    override val requestTimeout: FiniteDuration = {
        FiniteDuration(config.get("requesttimeout", "60").toLong, SECONDS)
    }
}

