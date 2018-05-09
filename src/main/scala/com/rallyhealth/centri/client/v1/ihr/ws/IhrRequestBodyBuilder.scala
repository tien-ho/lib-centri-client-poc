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


  case class IhrRequest(applicationName: String,
                                   message: String) extends LoggableRequest
  object IhrRequest {
    implicit val format: Format[IhrRequest] = Json.format[IhrRequest]
  }



  def healthCheckRequestBody(request: IhrHealthCheckRequest, meta: RequestMetaData): String = Json.toJson(
    {
      val baseRequest = buildBaseIhrMessageBuilder(request, meta)
      baseRequest.copy()
    }
  ).toString()




  private def buildBaseIhrMessageBuilder(request: IhrHealthCheckRequest, meta: RequestMetaData): IhrMessageBuilder =
    IhrMessageBuilder(IhrRequest(
      meta.applicationName,
      request.test
    ))


}
