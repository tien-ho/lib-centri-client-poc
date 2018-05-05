package com.rallyhealth.optum.client.v8.product.config

import com.rallyhealth.optum.client.v8.common.{OptumClientConfig, OptumClientRallyConfig}
import com.rallyhealth.spartan.v2.config.RallyConfig

trait ProductEligibilityConfig extends OptumClientConfig {

  /**
    * The path to invoke product eligibility endpoint excluding the {{{baseUrl}}} at which
    * the path is hosted.
    */
  val productEligibilityPath: String
}

class ProductEligibilityRallyConfig(underlying: RallyConfig)
  extends OptumClientRallyConfig(underlying)
  with ProductEligibilityConfig {

  override val productEligibilityPath: String = config.get("product.eligibility.path", "/product/eligibility/v1.0")
}
