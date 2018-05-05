package com.rallyhealth.optum.client.v8.config.util

import play.api.libs.json.{JsValue, Json}

import scala.util.Random

class SampleConfigIntakeRequestGenerator {

  private val randomNumber = Random.nextInt(99999999)

  val request: JsValue = Json.parse(s"""{
                                      |	"request_header": {
                                      |		"transaction_id": "RALLY${randomNumber}",
                                      |		"transaction_date": "2017-05-31",
                                      |		"existing_config_term_date": "2016-05-31"
                                      |	},
                                      |	"purchaser_code": "TARGET",
                                      |	"purchaser_name": "Target Corporation",
                                      |	"rally_client_name": "target",
                                      |	"client_legal_name": "Target Corporation",
                                      |	"client_url_path": "target.werally.com",
                                      |	"policy_details": [{
                                      |		"policy_number": "0185002",
                                      |		"policy_default_ind": false,
                                      |		"policy_src_code": "CS"
                                      |	}, {
                                      |		"policy_number": "00003692",
                                      |		"policy_default_ind": true,
                                      |		"policy_src_code": "OP"
                                      |	}, {
                                      |		"policy_number": "00003267",
                                      |		"policy_default_ind": false,
                                      |		"policy_src_code": "OP"
                                      |	}, {
                                      |		"policy_number": "00001358",
                                      |		"policy_default_ind": false,
                                      |		"policy_src_code": "OP"
                                      |	}, {
                                      |		"policy_number": "00004260",
                                      |		"policy_default_ind": false,
                                      |		"policy_src_code": "OP"
                                      |	}, {
                                      |		"policy_number": "00004922",
                                      |		"policy_default_ind": false,
                                      |		"policy_src_code": "OP"
                                      |	}, {
                                      |		"policy_number": "00005768",
                                      |		"policy_default_ind": false,
                                      |		"policy_src_code": "OP"
                                      |	}, {
                                      |		"policy_number": "00005769",
                                      |		"policy_default_ind": false,
                                      |		"policy_src_code": "OP"
                                      |	}],
                                      |	"configuration_details": {
                                      |		"effective_start_date": "2017-01-01",
                                      |		"effective_end_date": "2999-12-31",
                                      |		"capabilities": [{
                                      |			"name": "RALLY ",
                                      |			"capability_start_date": "2017-01-01",
                                      |			"capability_end_date": "2999-12-31"
                                      |		}],
                                      |		"utility_fields": [{
                                      |			"field": "CU1",
                                      |			"name": "MED_PLN_ORG_ID",
                                      |			"facets_field_name": "COBR",
                                      |			"eligibility_source": "OP"
                                      |		}, {
                                      |			"field": "EC1",
                                      |			"name": "CBR_TXT",
                                      |			"facets_field_name": "MCOV",
                                      |			"eligibility_source": "OP"
                                      |		}, {
                                      |			"field": "EC2",
                                      |			"name": "HLTH_PLN_PRDCT_CD",
                                      |			"facets_field_name": "BRCD",
                                      |			"eligibility_source": "OP"
                                      |		}, {
                                      |			"field": "EC3",
                                      |			"name": "DIV_NM",
                                      |			"facets_field_name": "MPID",
                                      |			"eligibility_source": "OP"
                                      |		}, {
                                      |			"field": "EL1",
                                      |			"name": "LN_OF_BUS_NM",
                                      |			"facets_field_name": "LOB",
                                      |			"eligibility_source": "OP"
                                      |		}, {
                                      |			"field": "EL2",
                                      |			"name": "MED_COV_TXT",
                                      |			"facets_field_name": "POLI",
                                      |			"eligibility_source": "OP"
                                      |		}, {
                                      |			"field": "EL3",
                                      |			"name": "MKT_SEG_NM",
                                      |			"facets_field_name": "WRKL",
                                      |			"eligibility_source": "OP"
                                      |		}, {
                                      |			"field": "EU1",
                                      |			"name": "WRK_LOC_NM",
                                      |			"facets_field_name": "MSEG",
                                      |			"eligibility_source": "OP"
                                      |		}, {
                                      |			"field": "EU3",
                                      |			"name": "UNION_STS",
                                      |			"facets_field_name": "CUST",
                                      |			"eligibility_source": "OP"
                                      |		}, {
                                      |			"field": "SU1",
                                      |			"name": "LGL_ENTY_CD",
                                      |			"facets_field_name": "USVP",
                                      |			"eligibility_source": "OP"
                                      |		}],
                                      |		"ranking_fields": {
                                      |			"relationship_ranking": [{
                                      |				"relationship": "Spouse/Domestic Partner",
                                      |				"rank": 1
                                      |			}, {
                                      |				"relationship": "Subscriber/Recipient",
                                      |				"rank": 2
                                      |			}, {
                                      |				"relationship": "Child/Other (i.e. non-spousal dependent)",
                                      |				"rank": 3
                                      |			}],
                                      |			"eligibility_source_sys": [{
                                      |				"source_system": "OP",
                                      |				"rank": 1
                                      |			}]
                                      |		},
                                      |		"segmentations": [{
                                      |			"configuration_population_name": "PREM_DIRECT_MYREW_TARGET_UHC_PLANS_HSA",
                                      |			"configuration_population_desc": "UHC Plans",
                                      |			"configuration_population_default_ind": true,
                                      |			"capabilities": [{
                                      |				"name": "HA_PAPER_HA",
                                      |				"capability_start_date": "2017-01-01"
                                      |			}, {
                                      |				"name": "OP_PREVENTIVE",
                                      |				"capability_start_date": "2017-01-01"
                                      |			}],
                                      |			"attributes": [{
                                      |				"name": "ELIG_SRC_SYS",
                                      |				"value": ["OP"],
                                      |				"allow_null": false
                                      |			}, {
                                      |				"name": "REL_ID",
                                      |				"value": ["Subscriber/Recipient", "Spouse/Domestic Partner"],
                                      |				"allow_null": true
                                      |			}, {
                                      |				"name": "EE_STS",
                                      |				"value": ["A", "L"],
                                      |				"allow_null": false
                                      |			}, {
                                      |				"name": "ST_CD",
                                      |				"value": ["IL"],
                                      |				"allow_null": false
                                      |			}, {
                                      |				"name": "PVRC_CD",
                                      |				"value": ["00010001"],
                                      |				"allow_null": false
                                      |			}, {
                                      |				"name": "UNION_STS",
                                      |				"value": ["3561", "3571"],
                                      |				"allow_null": false
                                      |			}, {
                                      |				"name": "EE_STS",
                                      |				"value": ["01", "02", "04"],
                                      |				"allow_null": false
                                      |			}]
                                      |		}, {
                                      |			"configuration_population_name": "PREM_DIRECT_MYREW_TARGET_NONUNET_GIFT",
                                      |			"configuration_population_desc": "Non-UHC Plans",
                                      |			"configuration_population_default_ind": false,
                                      |			"capabilities": [{
                                      |				"name": "INCT_ACT_TRACK",
                                      |				"capability_start_date": "2017-01-01",
                                      |				"capability_end_date": "2999-12-31"
                                      |			}],
                                      |			"attributes": [{
                                      |				"name": "ST_CD",
                                      |				"value": ["IL"],
                                      |				"allow_null": false
                                      |			}, {
                                      |				"name": "PVRC_CD",
                                      |				"value": ["00010001"],
                                      |				"allow_null": false
                                      |			}, {
                                      |				"name": "UNION_STS",
                                      |				"value": ["3561", "3571"],
                                      |				"allow_null": false
                                      |			}, {
                                      |				"name": "EE_STS",
                                      |				"value": ["01", "02", "04"],
                                      |				"allow_null": false
                                      |			}]
                                      |		}, {
                                      |			"configuration_population_name": "PREM_DIRECT_MYREW_TARGET_NONUNET_GIFT",
                                      |			"configuration_population_desc": "Non-UHC Plans",
                                      |			"configuration_population_default_ind": false,
                                      |			"capabilities": [{
                                      |				"name": "INCT_ACT_TRACK",
                                      |				"capability_start_date": "2017-01-01",
                                      |				"capability_end_date": "2999-12-31"
                                      |			}],
                                      |			"attributes": [{
                                      |				"name": "ELIG_SRC_SYS",
                                      |				"value": ["OP"],
                                      |				"allow_null": false
                                      |			}, {
                                      |				"name": "REL_ID",
                                      |				"value": ["Child/Other (i.e. non-spousal dependent)", "Spouse/Domestic Partner"],
                                      |				"allow_null": true
                                      |			}, {
                                      |				"name": "EE_STS",
                                      |				"value": ["R"],
                                      |				"allow_null": false
                                      |			}]
                                      |		}]
                                      |	}
                                      |}""".stripMargin)

  val responseStringSuccess = s"""{
                          |	"successful_transaction": {
                          |		"transaction_id": "RALLY${randomNumber}"
                          |	}
                          |}""".stripMargin

  val responseSuccess: JsValue = Json.parse(responseStringSuccess)

  val responseStringError = """{
                              |	"error_details": [{
                              |		"error_code": "1001",
                              |		"error_message": "existing_config_term_date need to be before the configuration effective date"
                              |	}]
                              |}""".stripMargin

  val responseError: JsValue = Json.parse(responseStringError)

  val responseStringErrorNoCode = """{
                              |	"error_details": [{
                              |		"error_message": "existing_config_term_date need to be before the configuration effective date"
                              |	}]
                              |}""".stripMargin

  val responseErrorNoCode: JsValue = Json.parse(responseStringErrorNoCode)

  val responseStringErrorTwoCodes = """{
                              |	"error_details": [{
                              |		"error_code": "1",
                              |		"error_message": "existing_config_term_date need to be before the configuration effective date"
                              |	},
                              | {
                              |		"error_code": "2",
                              |		"error_message": "some error message"
                              |	}]
                              |}""".stripMargin

  val responseErrorTwoCodes: JsValue = Json.parse(responseStringErrorTwoCodes)

  val responseStringErrorNoDetails = """{
                              |}""".stripMargin

  val responseErrorNoDetails: JsValue = Json.parse(responseStringErrorNoDetails)

  val responseStringErrorNoDetailsEmptyList = """{
                              | "error_details": [
                              |]}""".stripMargin

  val responseErrorNoDetailsEmptyList: JsValue = Json.parse(responseStringErrorNoDetailsEmptyList)
}
