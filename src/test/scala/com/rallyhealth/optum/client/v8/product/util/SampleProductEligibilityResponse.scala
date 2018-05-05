package com.rallyhealth.optum.client.v8.product.util

object SampleProductEligibilityResponse {

  val sampleResponseWithPATCHProducts =
    """{
      |	"ProductEligibilityResponse": {
      |		"responseHeader": {
      |			"transactionId": "RALLY022020179"
      |		},
      |		"consumerDetails": {
      |			"populationDetails": {
      |				"populationEffectiveDate": "2017-04-01T00:00:00Z",
      |				"populationCancelDate": "9999-12-31T00:00:00Z",
      |				"populationId": "POP43851",
      |				"populationBrandingType": "Rally + Optum",
      |				"populationBrandingEffectiveDate": "2017-01-01T00:00:00"
      |			},
      |			"productDetails": {
      |				"product": [{
      |						"source": "Wellness, Inc",
      |						"productEvent1": "Online Coaching",
      |						"productEvent2": "Sleep Well",
      |						"productEffectiveDate": "2017-01-01T00:00:00Z",
      |						"productTerminationDate": "2199-12-31T00:00:00Z"
      |
      |					},
      |					{
      |						"source": "Optum",
      |						"productEvent1": "Online Coaching",
      |						"productEvent2": "Sleep Well",
      |						"productEffectiveDate": "2017-01-01T00:00:00Z",
      |						"productTerminationDate": "2199-12-31T00:00:00Z",
      |						"productBranding": [{
      |							"productBrandingType": " BACKGROUND | UHC"
      |						}]
      |
      |					},
      |					{
      |						"source": "Optum",
      |						"productEvent1": "Online Coaching",
      |						"productEvent2": "Manage Your Mindset",
      |						"productEffectiveDate": "2017-01-01T00:00:00Z",
      |						"productTerminationDate": "2199-12-31T00:00:00Z",
      |						"productBranding": [{
      |							"productBrandingType": " BACKGROUND | UHC"
      |						}]
      |					},
      |					{
      |						"source": "Optum",
      |						"productEvent1": "Online Coaching",
      |						"productEvent2": "Eat Smart",
      |						"productEffectiveDate": "2017-01-01T00:00:00Z",
      |						"productTerminationDate": "2199-12-31T00:00:00Z",
      |						"productBranding": [{
      |							"productBrandingType": " BACKGROUND | UHC"
      |						}]
      |					},
      |					{
      |						"source": "Paquin",
      |						"productEvent1": "Fitness Reimbursement",
      |						"productEffectiveDate": "2016-04-01T00:00:00Z",
      |						"productTerminationDate": "2199-12-31T00:00:00Z"
      |
      |					},
      |					{
      |						"source": "Optum",
      |						"productEvent1": "Telephonic Wellness Coaching",
      |						"productEvent2": "Tobacco Cessation",
      |						"productEffectiveDate": "2016-04-01T00:00:00Z",
      |						"productTerminationDate": "2199-12-31T00:00:00Z"
      |
      |					},
      |					{
      |						"source": "Optum",
      |						"productEvent1": "Telephonic Wellness Coaching",
      |						"productEvent2": "Heart Healthy Lifestyle",
      |						"productEffectiveDate": "2016-04-01T00:00:00Z",
      |						"productTerminationDate": "2199-12-31T00:00:00Z"
      |
      |					},
      |					{
      |						"source": "Optum",
      |						"productEvent1": "Telephonic Wellness Coaching",
      |						"productEvent2": "Exercise",
      |						"productEffectiveDate": "2016-04-01T00:00:00Z",
      |						"productTerminationDate": "2199-12-31T00:00:00Z"
      |
      |					},
      |					{
      |						"source": "Optum",
      |						"productEvent1": "Online Coaching",
      |						"productEvent2": "Stress Less",
      |						"productEffectiveDate": "2017-01-01T00:00:00Z",
      |						"productTerminationDate": "2199-12-31T00:00:00Z",
      |						"productBranding": [{
      |							"productBrandingType": " BACKGROUND | UHC"
      |						}]
      |
      |					},
      |					{
      |						"source": "Optum",
      |						"productEvent1": "Telephonic Wellness Coaching",
      |						"productEvent2": "Weight Management",
      |						"productEffectiveDate": "2016-04-01T00:00:00Z",
      |						"productTerminationDate": "2199-12-31T00:00:00Z"
      |					},
      |					{
      |						"source": "Optum",
      |						"productEvent1": "Telephonic Wellness Coaching",
      |						"productEvent2": "Stress Management",
      |						"productEffectiveDate": "2016-04-01T00:00:00Z",
      |						"productTerminationDate": "2199-12-31T00:00:00Z"
      |
      |					},
      |					{
      |						"source": "Optum",
      |						"productEvent1": "Telephonic Wellness Coaching",
      |						"productEvent2": "Diabetes Lifestyle",
      |						"productEffectiveDate": "2016-04-01T00:00:00Z",
      |						"productTerminationDate": "2199-12-31T00:00:00Z"
      |
      |					},
      |					{
      |						"source": "Optum",
      |						"productEvent1": "Telephonic Wellness Coaching",
      |						"productEvent2": "Nutrition",
      |						"productEffectiveDate": "2016-04-01T00:00:00Z",
      |						"productTerminationDate": "2199-12-31T00:00:00Z"
      |
      |					},
      |					{
      |						"source": "Optum",
      |						"productEvent1": "Online Coaching",
      |						"productEvent2": "Quit Tobacco",
      |						"productEffectiveDate": "2017-01-01T00:00:00Z",
      |						"productTerminationDate": "2199-12-31T00:00:00Z",
      |						"productBranding": [{
      |							"productBrandingType": " BACKGROUND | UHC"
      |						}]
      |					},
      |					{
      |						"source": "Optum",
      |						"productEvent1": "Online Coaching",
      |						"productEvent2": "Find Your Joy",
      |						"productEffectiveDate": "2017-01-01T00:00:00Z",
      |						"productTerminationDate": "2199-12-31T00:00:00Z",
      |						"productBranding": [{
      |							"productBrandingType": " BACKGROUND | UHC"
      |						}]
      |
      |					},
      |					{
      |						"source": "Zipongo",
      |						"productEvent1": "Digital Nutrition",
      |						"productEffectiveDate": "2017-01-01T00:00:00Z",
      |						"productTerminationDate": "2199-12-31T00:00:00Z"
      |					}
      |				]
      |			}
      |		}
      |	}
      |}
      | """.stripMargin

  val sampleResponseWithoutPATCHProducts =
    """{
      |	"productEligibilityResponse": {
      |		"responseHeader": {
      |			"transactionId": "1a"
      |		},
      |		"consumerDetails": {
      |			"populationDetails": {
      |				"populationEffectiveDate": "2010-01-01T00:00:00Z",
      |				"populationId": "POP317",
      |				"populationBrandingType": "Rally + Optum",
      |				"populationBrandingEffectiveDate": "2017-01-01T00:00:00Z"
      |			},
      |			"productDetails": {
      |				"product": [{
      |					"source": "Optum",
      |					"productEvent1": "Online Coaching",
      |					"productEvent2": "Sleep Well",
      |					"productEffectiveDate": "2017-01-01T00:00:00Z",
      |					"productTerminationDate": "2199-12-31T00:00:00Z",
      |					"productBranding": [{
      |						"productBrandingType": "WEBSITE|OPTUM"
      |					}]
      |				}, {
      |					"source": "Optum",
      |					"productEvent1": "Online Coaching",
      |					"productEvent2": "Eat Smart",
      |					"productEffectiveDate": "2017-01-01T00:00:00Z",
      |					"productTerminationDate": "2199-12-31T00:00:00Z",
      |					"productBranding": [{
      |						"productBrandingType": "WEBSITE|OPTUM"
      |					}]
      |				}, {
      |					"source": "Optum",
      |					"productEvent1": "Fitness Reimbursement",
      |					"productEffectiveDate": "2017-01-01T00:00:00Z",
      |					"productTerminationDate": "2017-12-31T00:00:00Z"
      |				}, {
      |					"source": "Optum",
      |					"productEvent1": "Telephonic Wellness Coaching",
      |					"productEvent2": "Healthy Weight",
      |					"productEffectiveDate": "2010-01-01T00:00:00Z",
      |					"productTerminationDate": "2199-12-31T00:00:00Z"
      |				}, {
      |					"source": "Optum",
      |					"productEvent1": "Telephonic Wellness Coaching",
      |					"productEvent2": "Heart Healthy Lifestyle",
      |					"productEffectiveDate": "2010-01-01T00:00:00Z",
      |					"productTerminationDate": "2199-12-31T00:00:00Z"
      |				}, {
      |					"source": "Optum",
      |					"productEvent1": "Telephonic Wellness Coaching",
      |					"productEvent2": "Exercise",
      |					"productEffectiveDate": "2010-01-01T00:00:00Z",
      |					"productTerminationDate": "2199-12-31T00:00:00Z"
      |				}, {
      |					"source": "Optum",
      |					"productEvent1": "Online Coaching",
      |					"productEvent2": "Stress Less",
      |					"productEffectiveDate": "2017-01-01T00:00:00Z",
      |					"productTerminationDate": "2199-12-31T00:00:00Z",
      |					"productBranding": [{
      |						"productBrandingType": "WEBSITE|OPTUM"
      |					}]
      |				}, {
      |					"source": "QUEST",
      |					"productEvent1": "Biometrics",
      |					"productEffectiveDate": "2017-01-01T00:00:00Z",
      |					"productTerminationDate": "2199-12-31T00:00:00Z"
      |				}, {
      |					"source": "Optum",
      |					"productEvent1": "Telephonic Wellness Coaching",
      |					"productEvent2": "Stress Management",
      |					"productEffectiveDate": "2010-01-01T00:00:00Z",
      |					"productTerminationDate": "2199-12-31T00:00:00Z"
      |				}, {
      |					"source": "Optum",
      |					"productEvent1": "Telephonic Wellness Coaching",
      |					"productEvent2": "Diabetes Lifestyle",
      |					"productEffectiveDate": "2010-01-01T00:00:00Z",
      |					"productTerminationDate": "2199-12-31T00:00:00Z"
      |				}, {
      |					"source": "Optum",
      |					"productEvent1": "Telephonic Wellness Coaching",
      |					"productEvent2": "Nutrition",
      |					"productEffectiveDate": "2010-01-01T00:00:00Z",
      |					"productTerminationDate": "2199-12-31T00:00:00Z"
      |				}, {
      |					"source": "Optum",
      |					"productEvent1": "Online Coaching",
      |					"productEvent2": "Meditation",
      |					"productEffectiveDate": "2017-01-01T00:00:00Z",
      |					"productTerminationDate": "2199-12-31T00:00:00Z",
      |					"productBranding": [{
      |						"productBrandingType": "WEBSITE|OPTUM"
      |					}]
      |				}, {
      |					"source": "Optum",
      |					"productEvent1": "Online Coaching",
      |					"productEvent2": "Fit for Life",
      |					"productEffectiveDate": "2017-01-01T00:00:00Z",
      |					"productTerminationDate": "2199-12-31T00:00:00Z",
      |					"productBranding": [{
      |						"productBrandingType": "WEBSITE|OPTUM"
      |					}]
      |				}, {
      |					"source": "Optum",
      |					"productEvent1": "Online Coaching",
      |					"productEvent2": "Quit Tobacco",
      |					"productEffectiveDate": "2017-01-01T00:00:00Z",
      |					"productTerminationDate": "2199-12-31T00:00:00Z",
      |					"productBranding": [{
      |						"productBrandingType": "WEBSITE|OPTUM"
      |					}]
      |				}, {
      |					"source": "Optum",
      |					"productEvent1": "Online Coaching",
      |					"productEvent2": "Happiness",
      |					"productEffectiveDate": "2017-01-01T00:00:00Z",
      |					"productTerminationDate": "2199-12-31T00:00:00Z",
      |					"productBranding": [{
      |						"productBrandingType": "WEBSITE|OPTUM"
      |					}]
      |				}]
      |			}
      |		}
      |	}
      |}""".stripMargin

  val responseWithoutAnyProduct =
    """{
      |	"ProductEligibilityResponse": {
      |		"responseHeader": {
      |			"transactionId": "RALLY022020179"
      |		},
      |		"consumerDetails": {
      |			"productDetails": {
      |				"product": []
      |			}
      |		}
      |	}
      |}""".stripMargin

  val errorResponse =
    """{
      |   "errorCode": "153",
      |   "exceptionMessage": "Event Date in the future"
      |}
      | """.stripMargin

  val integrationTestSuccessResponse =
    """{
      |   "productEligibilityResponse":{
      |      "responseHeader":{
      |         "transactionId":"1a"
      |      },
      |      "consumerDetails":{
      |         "populationDetails":{
      |            "populationEffectiveDate":"2010-01-01T00:00:00Z",
      |            "populationId":"POP317",
      |            "populationBrandingType":"Optum",
      |            "populationBrandingEffectiveDate":"2010-01-01T00:00:00Z"
      |         },
      |         "productDetails":{
      |            "product":[
      |               {
      |                  "source":"Optum",
      |                  "productEvent1":"Personal Coaching",
      |                  "productEvent2":"Eat Smart",
      |                  "productEffectiveDate":"2017-07-12T00:00:00Z",
      |                  "productTerminationDate":"2199-12-31T00:00:00Z",
      |                  "productBranding":[
      |                     {
      |                        "productBrandingType":""
      |                     },
      |                     {
      |                        "productBrandingType":"WELLNESS COACHING WEBSITE|UHC"
      |                     }
      |                  ]
      |               },
      |               {
      |                  "source":"Optum",
      |                  "productEvent1":"Personal Coaching",
      |                  "productEvent2":"Diabetes Lifestyle",
      |                  "productEffectiveDate":"2017-07-12T00:00:00Z",
      |                  "productTerminationDate":"2199-12-31T00:00:00Z",
      |                  "productBranding":[
      |                     {
      |                        "productBrandingType":""
      |                     },
      |                     {
      |                        "productBrandingType":"WELLNESS COACHING WEBSITE|UHC"
      |                     }
      |                  ]
      |               },
      |               {
      |                  "source":"Optum",
      |                  "productEvent1":"Quit for Life",
      |                  "productEffectiveDate":"2017-01-01T00:00:00Z",
      |                  "productTerminationDate":"2199-12-31T00:00:00Z"
      |               },
      |               {
      |                  "source":"Optum",
      |                  "productEvent1":"Online Coaching",
      |                  "productEvent2":"Sleep Well",
      |                  "productEffectiveDate":"2017-07-12T00:00:00Z",
      |                  "productTerminationDate":"2199-12-31T00:00:00Z",
      |                  "productBranding":[
      |                     {
      |                        "productBrandingType":""
      |                     },
      |                     {
      |                        "productBrandingType":"WELLNESS COACHING WEBSITE|UHC"
      |                     }
      |                  ]
      |               },
      |               {
      |                  "source":"Optum",
      |                  "productEvent1":"Online Coaching",
      |                  "productEvent2":"Eat Smart",
      |                  "productEffectiveDate":"2017-07-12T00:00:00Z",
      |                  "productTerminationDate":"2199-12-31T00:00:00Z",
      |                  "productBranding":[
      |                     {
      |                        "productBrandingType":""
      |                     },
      |                     {
      |                        "productBrandingType":"WELLNESS COACHING WEBSITE|UHC"
      |                     }
      |                  ]
      |               },
      |               {
      |                  "source":"Optum",
      |                  "productEvent1":"Telephonic Wellness Coaching",
      |                  "productEvent2":"Healthy Weight",
      |                  "productEffectiveDate":"2010-01-01T00:00:00Z",
      |                  "productTerminationDate":"2199-12-31T00:00:00Z"
      |               },
      |               {
      |                  "source":"Optum",
      |                  "productEvent1":"Personal Coaching",
      |                  "productEvent2":"Fit for Life",
      |                  "productEffectiveDate":"2017-07-12T00:00:00Z",
      |                  "productTerminationDate":"2199-12-31T00:00:00Z",
      |                  "productBranding":[
      |                     {
      |                        "productBrandingType":""
      |                     },
      |                     {
      |                        "productBrandingType":"WELLNESS COACHING WEBSITE|UHC"
      |                     }
      |                  ]
      |               },
      |               {
      |                  "source":"Optum",
      |                  "productEvent1":"Personal Coaching",
      |                  "productEvent2":"Healthy Heart",
      |                  "productEffectiveDate":"2017-07-12T00:00:00Z",
      |                  "productTerminationDate":"2199-12-31T00:00:00Z",
      |                  "productBranding":[
      |                     {
      |                        "productBrandingType":""
      |                     },
      |                     {
      |                        "productBrandingType":"WELLNESS COACHING WEBSITE|UHC"
      |                     }
      |                  ]
      |               },
      |               {
      |                  "source":"Optum",
      |                  "productEvent1":"Personal Coaching",
      |                  "productEvent2":"Sleep Well",
      |                  "productEffectiveDate":"2017-07-12T00:00:00Z",
      |                  "productTerminationDate":"2199-12-31T00:00:00Z",
      |                  "productBranding":[
      |                     {
      |                        "productBrandingType":""
      |                     },
      |                     {
      |                        "productBrandingType":"WELLNESS COACHING WEBSITE|UHC"
      |                     }
      |                  ]
      |               },
      |               {
      |                  "source":"Optum",
      |                  "productEvent1":"Personal Coaching",
      |                  "productEvent2":"Weight and Wellness",
      |                  "productEffectiveDate":"2017-07-12T00:00:00Z",
      |                  "productTerminationDate":"2199-12-31T00:00:00Z",
      |                  "productBranding":[
      |                     {
      |                        "productBrandingType":""
      |                     },
      |                     {
      |                        "productBrandingType":"WELLNESS COACHING WEBSITE|UHC"
      |                     }
      |                  ]
      |               },
      |               {
      |                  "source":"Optum",
      |                  "productEvent1":"Telephonic Wellness Coaching",
      |                  "productEvent2":"Heart Healthy Lifestyle",
      |                  "productEffectiveDate":"2010-01-01T00:00:00Z",
      |                  "productTerminationDate":"2199-12-31T00:00:00Z"
      |               },
      |               {
      |                  "source":"Optum",
      |                  "productEvent1":"Telephonic Wellness Coaching",
      |                  "productEvent2":"Exercise",
      |                  "productEffectiveDate":"2010-01-01T00:00:00Z",
      |                  "productTerminationDate":"2199-12-31T00:00:00Z"
      |               },
      |               {
      |                  "source":"Optum",
      |                  "productEvent1":"Personal Coaching",
      |                  "productEvent2":"General Wellness",
      |                  "productEffectiveDate":"2017-07-12T00:00:00Z",
      |                  "productTerminationDate":"2199-12-31T00:00:00Z",
      |                  "productBranding":[
      |                     {
      |                        "productBrandingType":""
      |                     },
      |                     {
      |                        "productBrandingType":"WELLNESS COACHING WEBSITE|UHC"
      |                     }
      |                  ]
      |               },
      |               {
      |                  "source":"Optum",
      |                  "productEvent1":"Telephonic Wellness Coaching",
      |                  "productEvent2":"Stress Management",
      |                  "productEffectiveDate":"2010-01-01T00:00:00Z",
      |                  "productTerminationDate":"2199-12-31T00:00:00Z"
      |               },
      |               {
      |                  "source":"Optum",
      |                  "productEvent1":"Telephonic Wellness Coaching",
      |                  "productEvent2":"Diabetes Lifestyle",
      |                  "productEffectiveDate":"2010-01-01T00:00:00Z",
      |                  "productTerminationDate":"2199-12-31T00:00:00Z"
      |               },
      |               {
      |                  "source":"Optum",
      |                  "productEvent1":"Telephonic Wellness Coaching",
      |                  "productEvent2":"Nutrition",
      |                  "productEffectiveDate":"2010-01-01T00:00:00Z",
      |                  "productTerminationDate":"2199-12-31T00:00:00Z"
      |               },
      |               {
      |                  "source":"Optum",
      |                  "productEvent1":"Biometrics",
      |                  "productEffectiveDate":"2017-01-01T00:00:00Z",
      |                  "productTerminationDate":"2199-12-31T00:00:00Z"
      |               },
      |               {
      |                  "source":"Optum",
      |                  "productEvent1":"EAP for Care24",
      |                  "productEffectiveDate":"2010-01-01T00:00:00Z",
      |                  "productTerminationDate":"2199-12-31T00:00:00Z"
      |               },
      |               {
      |                  "source":"Optum",
      |                  "productEvent1":"Online Coaching",
      |                  "productEvent2":"Fit for Life",
      |                  "productEffectiveDate":"2017-07-12T00:00:00Z",
      |                  "productTerminationDate":"2199-12-31T00:00:00Z",
      |                  "productBranding":[
      |                     {
      |                        "productBrandingType":""
      |                     },
      |                     {
      |                        "productBrandingType":"WELLNESS COACHING WEBSITE|UHC"
      |                     }
      |                  ]
      |               },
      |               {
      |                  "source":"Optum",
      |                  "productEvent1":"Online Coaching",
      |                  "productEvent2":"Meditation",
      |                  "productEffectiveDate":"2017-07-12T00:00:00Z",
      |                  "productTerminationDate":"2199-12-31T00:00:00Z",
      |                  "productBranding":[
      |                     {
      |                        "productBrandingType":""
      |                     },
      |                     {
      |                        "productBrandingType":"WELLNESS COACHING WEBSITE|UHC"
      |                     }
      |                  ]
      |               },
      |               {
      |                  "source":"Optum",
      |                  "productEvent1":"Personal Coaching",
      |                  "productEvent2":"Stress Less",
      |                  "productEffectiveDate":"2017-07-12T00:00:00Z",
      |                  "productTerminationDate":"2199-12-31T00:00:00Z",
      |                  "productBranding":[
      |                     {
      |                        "productBrandingType":""
      |                     },
      |                     {
      |                        "productBrandingType":"WELLNESS COACHING WEBSITE|UHC"
      |                     }
      |                  ]
      |               },
      |               {
      |                  "source":"Optum",
      |                  "productEvent1":"Personal Coaching",
      |                  "productEvent2":"Quit Tobacco",
      |                  "productEffectiveDate":"2017-07-12T00:00:00Z",
      |                  "productTerminationDate":"2199-12-31T00:00:00Z",
      |                  "productBranding":[
      |                     {
      |                        "productBrandingType":""
      |                     },
      |                     {
      |                        "productBrandingType":"WELLNESS COACHING WEBSITE|UHC"
      |                     }
      |                  ]
      |               },
      |               {
      |                  "source":"Optum",
      |                  "productEvent1":"Personal Coaching",
      |                  "productEvent2":"Family Wellness",
      |                  "productEffectiveDate":"2017-07-12T00:00:00Z",
      |                  "productTerminationDate":"2199-12-31T00:00:00Z",
      |                  "productBranding":[
      |                     {
      |                        "productBrandingType":""
      |                     },
      |                     {
      |                        "productBrandingType":"WELLNESS COACHING WEBSITE|UHC"
      |                     }
      |                  ]
      |               },
      |               {
      |                  "source":"Optum",
      |                  "productEvent1":"Real Appeal",
      |                  "productEffectiveDate":"2017-10-26T00:00:00Z",
      |                  "productTerminationDate":"2199-12-31T00:00:00Z"
      |               }
      |            ]
      |         }
      |      }
      |   }
      |}""".stripMargin
}
