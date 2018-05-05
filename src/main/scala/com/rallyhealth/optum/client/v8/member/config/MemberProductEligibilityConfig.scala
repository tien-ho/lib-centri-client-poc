package com.rallyhealth.optum.client.v8.member.config

import com.rallyhealth.optum.client.v8.common.{OptumClientConfig, OptumClientRallyConfig}
import com.rallyhealth.spartan.v2.config.RallyConfig

trait MemberProductEligibilityConfig extends OptumClientConfig {

  /**
    * The path to invoke member product eligibility endpoint excluding the {{{baseUrl}}} at which
    * the path is hosted.
    */
  val memberProductEligibilityPath: String
}

class MemberProductEligibilityRallyConfig(underlying: RallyConfig)
  extends OptumClientRallyConfig(underlying)
  with MemberProductEligibilityConfig {

  override val memberProductEligibilityPath: String = config.get("member.product.eligibility.path", "/member/product/eligibility/v2.0")
}
