package com.rallyhealth.optum.client.script

import com.rallyhealth.optum.client.v8.common.OptumClientRallyConfig
import com.rallyhealth.optum.client.v8.common.model.OptumResponseSuccess
import com.rallyhealth.optum.client.v8.config.util.SampleConfigIntakeRequestGenerator
import com.rallyhealth.optum.client.v8.config.ws.{WSConfigIntakeClient, WSWithOAuthConfigIntakeClient}
import org.scalatest.FlatSpec
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Seconds, Span}

import scala.concurrent.ExecutionContext.Implicits._

class InvokeConfigIntakeSpec
  extends FlatSpec
  with ScalaFutures {

  it should "invoke config intake api and return a 202 with the expected response body" in {

    import com.rallyhealth.optum.client.script.AuthClientSetup._

    val optumClientRallyConfig = new OptumClientRallyConfig(rallyConfig)
    val configIntakeClient = new WSConfigIntakeClient(optumConfigIntakeConfig, rqClient)
    val oConfigIntakeClient = new WSWithOAuthConfigIntakeClient(cachedOAuthClient, configIntakeClient)
    val generator = new SampleConfigIntakeRequestGenerator
    val expectedResponse = OptumResponseSuccess(202, Map.empty, generator.responseSuccess)

    whenReady(oConfigIntakeClient.postClientConfiguration(generator.request), timeout(Span(60, Seconds))) {
      res =>
        assert(res.isRight)
        assert(res.right.get.body === expectedResponse.body)
    }
  }

}
