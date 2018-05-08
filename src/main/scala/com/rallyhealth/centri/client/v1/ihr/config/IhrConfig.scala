package com.rallyhealth.centri.client.v1.ihr.config

import com.rallyhealth.centri.client.v1.common.{CentriClientConfig, CentriClientRallyConfig}
import com.rallyhealth.spartan.v2.config.RallyConfig

trait IhrConfig extends CentriClientConfig {

  /**
    * The path to invoke member product eligibility endpoint excluding the {{{baseUrl}}} at which
    * the path is hosted.
    */
  val ihrPath: String
}

class MemberProductEligibilityRallyConfig(underlying: RallyConfig)
  extends CentriClientRallyConfig(underlying)
    with IhrConfig {

  override val ihrPath: String = config.get("member.product.eligibility.path", "/member/product/eligibility/v2.0")
}
