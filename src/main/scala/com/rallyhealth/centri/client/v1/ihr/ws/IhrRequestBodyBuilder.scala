package com.rallyhealth.centri.client.v1.ihr.ws


import com.rallyhealth.argosy.correlationId.CorrelationId
import com.rallyhealth.centri.client.v1.ihr.model.{CentriIhrRequest, _}

import com.rallyhealth.spartan.v2.datetime.DateTimeHelpers
import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}
import play.api.libs.json.{Format, Json}

/**
  * Responsible for building the Json requests sent to Optum endpoint.
  */
object IhrRequestBodyBuilder {
  private val printFormat: DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'00:00:00'Z'")


  private case class IhrMessageBuilder(`IhrRequest`: IhrRequest)
  private object IhrMessageBuilder {
    implicit val format: Format[IhrMessageBuilder] = Json.format[IhrMessageBuilder]
  }


  case class IhrRequest(test: String,
                       correlationId: String
                       ) extends LoggableRequest

  object IhrRequest {
    implicit val format: Format[IhrRequest] = Json.format[IhrRequest]
  }


  /**
    * Builds the health check request and returns a JSON representation
    */
  def healthCheckRequestBody(ihrRequest: IhrRequest): String = Json.toJson(
    {
      ihrRequest
    }
  ).toString()


  def healthCheckRequestBody(meta: RequestMetaData, ihrRequest: IhrRequest): String = Json.toJson(
    {
      val baseRequest = buildBaseIhrMessageBuilder(meta, ihrRequest)

    }
  ).toString()




  private def buildBaseIhrMessageBuilder(meta: RequestMetaData, ihrRequest: IhrRequest): IhrMessageBuilder =
    IhrMessageBuilder(IhrRequest(
        "testing",
      meta.correlationId.get
    ))


}
