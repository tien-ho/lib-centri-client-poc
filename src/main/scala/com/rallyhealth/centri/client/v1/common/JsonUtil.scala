package com.rallyhealth.centri.client.v1.common

import com.rallyhealth.rq.v1.RqResponse
import play.api.libs.json.{JsObject, JsValue, Json}
import com.rallyhealth.optum.client.v8.common.ApiConstant._
import com.rallyhealth.rq.v1._

import scala.util.Try

object JsonUtil {
    def isJson(in: String): Boolean = Try(Json.parse(in)).isSuccess

    def hasError(rsp: RqResponse): Boolean = {
        (rsp.body.as[JsValue] \ ErrorCode1).asOpt[String].isDefined ||
            (rsp.body.as[JsValue] \ ExceptionDetail).asOpt[JsObject].isDefined ||
            (rsp.body.as[JsValue] \ ErrorCode2).asOpt[String].isDefined
    }
}
