package com.rallyhealth.optum.client.v8.config.config

import com.rallyhealth.optum.client.v8.common.{OptumClientConfig, OptumClientRallyConfig}
import com.rallyhealth.spartan.v2.config.RallyConfig

trait ConfigIntakeConfig extends OptumClientConfig {

  /**
    * The path to invoke config intake endpoint excluding the {{{baseUrl}}} at which
    * the path is hosted.
    */
  val configIntakePath: String
}

class ConfigIntakeRallyConfig(underlying: RallyConfig)
  extends OptumClientRallyConfig(underlying)
  with ConfigIntakeConfig {

  override val configIntakePath: String = config.get("config.intake.path", "/config-intake/custom/v1.0")
}
