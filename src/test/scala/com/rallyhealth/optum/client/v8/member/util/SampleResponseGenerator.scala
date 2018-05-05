package com.rallyhealth.optum.client.v8.member.util

object SampleResponseGenerator {

  final val memPEResponseInvokeOptum =
    """ |{
      |   "MemberProductEligibilityResponse":{
      |      "responseHeader":{
      |         "transactionId":"1a"
      |      },
      |      "consumerDetails":[
      |         {
      |            "demographics":{
      |               "firstName":"JAMES",
      |               "lastName":"CREGAN",
      |               "dateOfBirth":"1968-07-22T00:00:00Z",
      |               "gender":"M",
      |               "relationship":"EE",
      |               "relationshipDescription":"SUBSCRIBER/RECIPIENT"
      |            },
      |            "contactDetails":{
      |               "email":"JIMCREGAN@COMCAST.NET",
      |               "phoneNumber":"3012931912",
      |               "addressLine1":"11030 BALTIMORE NATIONAL PIKE",
      |               "city":"MYERSVILLE",
      |               "state":"MD",
      |               "zipCode":"21773"
      |            },
      |            "idSet":{
      |               "personId":21027150,
      |               "subscriberSSN":"155765700",
      |               "memberSSN":"155765700",
      |               "subscriberID":"00155765700",
      |               "alternateId":"950545881",
      |               "xrefId":"0021705276",
      |               "xrefPartitionNumber":"00016"
      |            },
      |            "populationDetails":{
      |               "populationEffectiveDate":"2016-11-28T00:00:00Z",
      |               "populationCancelDate":"9999-12-31T00:00:00Z",
      |               "populationId":"POP44052",
      |               "populationDateAssigned":"2016-11-28T00:00:00Z"
      |            },
      |            "coverageDetails":{
      |               "recordType":"HEALTH_COVERAGE",
      |               "employeeStatus":"A",
      |               "contractNumber":"06G9048",
      |               "eligibilitySourceSystem":"CS",
      |               "planVariation":"0001",
      |               "reportingCode":"0001",
      |               "customerName":"RELIANT DRYWALL, INC.",
      |               "coverageType":"M",
      |               "coverageEffectiveDate":"2016-03-01T00:00:00Z",
      |               "individualGroupTypeCode":"SB",
      |               "hireDate":"2007-05-01T00:00:00Z",
      |               "stateOfIssue":"MD"
      |            },
      |            "productDetails":{
      |               "product":[
      |                  {
      |                     "source":"Optum",
      |                     "productEvent1":"Telephonic Wellness Coaching",
      |                     "productEvent2":"Heart Healthy Lifestyle",
      |                     "productEffectiveDate":"2016-04-01T00:00:00Z",
      |                     "productTerminationDate":"2199-12-31T00:00:00Z",
      |                     "productBranding":[
      |
      |                     ]
      |                  },
      |                  {
      |                     "source":"Optum",
      |                     "productEvent1":"Telephonic Wellness Coaching",
      |                     "productEvent2":"Exercise",
      |                     "productEffectiveDate":"2016-04-01T00:00:00Z",
      |                     "productTerminationDate":"2199-12-31T00:00:00Z",
      |                     "productBranding":[
      |
      |                     ]
      |                  },
      |                  {
      |                     "source":"Optum",
      |                     "productEvent1":"Telephonic Wellness Coaching",
      |                     "productEvent2":"Weight Management",
      |                     "productEffectiveDate":"2016-04-01T00:00:00Z",
      |                     "productTerminationDate":"2199-12-31T00:00:00Z",
      |                     "productBranding":[
      |
      |                     ]
      |                  },
      |                  {
      |                     "source":"Optum",
      |                     "productEvent1":"Telephonic Wellness Coaching",
      |                     "productEvent2":"Stress Management",
      |                     "productEffectiveDate":"2016-04-01T00:00:00Z",
      |                     "productTerminationDate":"2199-12-31T00:00:00Z",
      |                     "productBranding":[
      |
      |                     ]
      |                  },
      |                  {
      |                     "source":"Optum",
      |                     "productEvent1":"Telephonic Wellness Coaching",
      |                     "productEvent2":"Diabetes Lifestyle",
      |                     "productEffectiveDate":"2016-04-01T00:00:00Z",
      |                     "productTerminationDate":"2199-12-31T00:00:00Z",
      |                     "productBranding":[
      |
      |                     ]
      |                  },
      |                  {
      |                     "source":"Optum",
      |                     "productEvent1":"Telephonic Wellness Coaching",
      |                     "productEvent2":"Nutrition",
      |                     "productEffectiveDate":"2016-04-01T00:00:00Z",
      |                     "productTerminationDate":"2199-12-31T00:00:00Z",
      |                     "productBranding":[
      |
      |                     ]
      |                  },
      |                  {
      |                     "source":"Optum",
      |                     "productEvent1":"Telephonic Wellness Coaching",
      |                     "productEvent2":"Tobacco Cessation",
      |                     "productEffectiveDate":"2016-04-01T00:00:00Z",
      |                     "productTerminationDate":"2199-12-31T00:00:00Z",
      |                     "productBranding":[
      |
      |                     ]
      |                  }
      |               ]
      |            }
      |         }
      |      ]
      |   }
      |}""".stripMargin

  final val memPEResponseAll =
    """
      |{"MemberProductEligibilityResponse": {
      |   "responseHeader": {"transactionId": "DWC30811"},
      |   "consumerDetails": [   {
      |      "demographics":       {
      |         "firstName": "DONNA",
      |         "lastName": "FORTEAU",
      |         "dateOfBirth": "1969-09-10T00:00:00Z",
      |         "gender": "F",
      |         "relationship": "EE",
      |         "relationshipDescription":"SUBSCRIBER/RECIPIENT"
      |      },
      |      "contactDetails":       {
      |         "email": "DONNA39MIKA@YAHOO.COM",
      |         "addressLine1": "10327 SUMMER AZURE DR",
      |         "city": "RIVERVIEW",
      |         "state": "FL",
      |         "zipCode": "33578"
      |      },
      |      "idSet":       {
      |         "personId": 21026070,
      |         "subscriberSSN": "113808284",
      |         "memberSSN": "113808284",
      |         "subscriberID": "00113808284",
      |         "alternateId": "938484204"
      |      },
      |      "populationDetails":       {
      |         "populationEffectiveDate": "2016-09-09T00:00:00Z",
      |         "populationCancelDate": "9999-12-31T00:00:00Z",
      |         "populationId": "POP44242",
      |         "populationDateAssigned": "2016-09-05T00:00:00Z"
      |      },
      |      "coverageDetails":       {
      |         "employeeStatus": "A",
      |         "contractNumber": "0704778",
      |         "planVariation": "0003",
      |         "reportingCode": "0003",
      |         "customerName": "HILLSBOROUGH CTY TAX COLLECTOR",
      |         "coverageType": "M",
      |         "coverageEffectiveDate": "2016-05-22T00:00:00Z",
      |         "coverageTerminationDate": "9999-12-31T00:00:00Z",
      |         "hireDate": "1999-09-20T00:00:00Z",
      |         "stateOfIssue": "FL"
      |      },
      |      "extendedAttributes":       {
      |         "ecExtended": [],
      |         "elExtended": [],
      |         "euExtended":          [
      |                        {
      |               "typeCode": "EU3",
      |               "value": "0009452",
      |               "effectiveDate": "2016-05-22T00:00:00Z",
      |               "cancelDate": "9999-12-31T00:00:00Z"
      |            },
      |                        {
      |               "typeCode": "EU3",
      |               "value": "0009452",
      |               "effectiveDate": "2016-05-22T00:00:00Z",
      |               "cancelDate": "9999-12-31T00:00:00Z"
      |            }
      |         ]
      |      },
      |      "productDetails": {"product":       [
      |                  {
      |            "source": "Optum",
      |            "productEvent1": "Online Coaching",
      |            "productEvent2": "Eat Smart",
      |            "productEffectiveDate": "2017-01-01T00:00:00Z",
      |            "productTerminationDate": "2199-12-31T00:00:00Z"
      |         },
      |                  {
      |            "source": "Optum",
      |            "productEvent1": "Online Coaching",
      |            "productEvent2": "Manage Your Mindset",
      |            "productEffectiveDate": "2017-01-01T00:00:00Z",
      |            "productTerminationDate": "2199-12-31T00:00:00Z"
      |         },
      |                  {
      |            "source": "Optum",
      |            "productEvent1": "Online Coaching",
      |            "productEvent2": "Fit for Life",
      |            "productEffectiveDate": "2017-01-01T00:00:00Z",
      |            "productTerminationDate": "2199-12-31T00:00:00Z"
      |         },
      |                  {
      |            "source": "Optum",
      |            "productEvent1": "Online Coaching",
      |            "productEvent2": "Find Your Joy",
      |            "productEffectiveDate": "2017-01-01T00:00:00Z",
      |            "productTerminationDate": "2199-12-31T00:00:00Z"
      |         }
      |      ]}
      |   }]
      |}}""".stripMargin

  final val errorResponse =
    """
      |{
      |  "errorCode": "154",
      |  "exceptionMessage": "Unable to identify consumer: IdentityServicesInternalError"
      |}""".stripMargin

  final val errorAdviceResponse =
    """
      |{
      |  "ErrorID": "4f90ebb4-0428-4570-ad97-6c9186b30986",
      |  "ErrorCode": "4.2",
      |  "ErrorMessage": "Error",
      |  "Advice": "If problem persists, please contact API team"
      |}""".stripMargin

  final val exceptionResponse =
    """
      |{
      |  "exceptionDetail": {
      |    "errorCode": "307",
      |    "exceptionMessage": "Request Type Not supported"
      |  }
      |}""".stripMargin

  final val integrationTestExpectedSuccessResponse =
    """
      {
 |    "MemberProductEligibilityResponse": {
 |        "consumerDetails": [
 |            {
 |                "contactDetails": {
 |                    "addressLine1": "11030 BALTIMORE NATIONAL PIKE",
 |                    "city": "MYERSVILLE",
 |                    "countryCode": "US",
 |                    "email": "jimcregan@comcast.net",
 |                    "phoneNumber": "3012931912",
 |                    "state": "MD",
 |                    "zipCode": "21773"
 |                },
 |                "coverageDetails": {
 |                    "contractNumber": "06G9048",
 |                    "coverageEffectiveDate": "2017-03-01T00:00:00Z",
 |                    "coverageTerminationDate": "2017-07-31T00:00:00Z",
 |                    "coverageType": "M",
 |                    "customerName": "RELIANT DRYWALL, INC.",
 |                    "eligibilitySourceSystem": "CS",
 |                    "employeeStatus": "A",
 |                    "hireDate": "2007-05-01T00:00:00Z",
 |                    "legalEntity1": "58010",
 |                    "marketSite": "0019493",
 |                    "planVariation": "0001",
 |                    "recordType": "HEALTH_COVERAGE",
 |                    "reportingCode": "0001",
 |                    "stateOfIssue": "MD"
 |                },
 |                "demographics": {
 |                    "dateOfBirth": "1968-07-22T00:00:00Z",
 |                    "firstName": "JAMES",
 |                    "gender": "M",
 |                    "lastName": "CREGAN",
 |                    "maritalStatus": "U",
 |                    "middleName": "T",
 |                    "relationship": "EE",
 |                    "relationshipDescription": "SUBSCRIBER/RECIPIENT"
 |                },
 |                "idSet": {
 |                    "alternateId": "950545881",
 |                    "familyId": "177414096",
 |                    "memberSSN": "155765700",
 |                    "personId": 21027150,
 |                    "subscriberID": "00155765700",
 |                    "subscriberSSN": "155765700",
 |                    "xrefId": "0314550543",
 |                    "xrefPartitionNumber": "00243"
 |                },
 |                "populationDetails": {
 |                },
 |                "productDetails": {
 |                    "product": [
 |                    ]
 |                }
 |            }
 |        ],
 |        "responseHeader": {
 |            "transactionId": "1a"
 |        }
 |    }
 |}
    """.stripMargin
}
