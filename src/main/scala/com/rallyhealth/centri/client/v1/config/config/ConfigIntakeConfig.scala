package com.rallyhealth.centri.client.v1.config.config


import com.rallyhealth.centri.client.v1.common.{CentriClientConfig, CentriClientRallyConfig}
import com.rallyhealth.spartan.v2.config.RallyConfig

trait ConfigIntakeConfig extends CentriClientConfig {

  /**
    * The path to invoke config intake endpoint excluding the {{{baseUrl}}} at which
    * the path is hosted.
    */
  val configIntakePath: String
}

class ConfigIntakeRallyConfig(underlying: RallyConfig)
  extends CentriClientRallyConfig(underlying)
    with ConfigIntakeConfig {

  override val configIntakePath: String = config.get("config.intake.path", "/config-intake/custom/v1.0")
}
