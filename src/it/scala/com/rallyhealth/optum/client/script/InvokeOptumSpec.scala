package com.rallyhealth.optum.client.script

import com.rallyhealth.optum.client.v8.auth.ws.WSOptumOauthClient
import com.rallyhealth.optum.client.v8.member.util.SampleResponseGenerator
import com.rallyhealth.optum.client.v8.member.ws.WSWithOAuthMemberProductEligibilityClient
import com.rallyhealth.optum.client.v8.auth.model.OptumAccessTokenRequest
import com.rallyhealth.optum.client.v8.auth.ws.{CachedOptumOauthClient, WSOptumOauthClient}
import com.rallyhealth.optum.client.v8.common.OptumClientRallyConfig
import com.rallyhealth.optum.client.v8.member.model._
import com.rallyhealth.optum.client.v8.member.ws.{WSMemberProductEligibilityClient, WSWithOAuthMemberProductEligibilityClient}
import com.rallyhealth.rq.v1.logging.LoggingRqBackend
import com.rallyhealth.rq.v1.play25.Play25RqBackend
import com.rallyhealth.rq.v1.stats.StatsRqBackend
import com.rallyhealth.rq.v1.RqClient
import net.sf.ehcache.CacheManager
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Seconds, Span}
import org.scalatest.FlatSpec
import play.api.cache.EhCacheApi
import com.rallyhealth.spartan.v2.config.RallyConfig
import com.rallyhealth.optum.client.v8.member.model._
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.rallyhealth.optum.client.v8.common.model.OptumResponseSuccess
import org.slf4j.LoggerFactory
import play.api.libs.json.Json
import play.api.libs.ws.ahc.AhcWSClient

import scala.concurrent.ExecutionContext.Implicits._
import com.rallyhealth.rq.v1._
import com.rallyhealth.stats.v3.{HttpClientStats, KeyPrefixedStats, Stats}

class InvokeOptumSpec
  extends FlatSpec
  with ScalaFutures {

  it should "invoke optum and return a 200 with the expected response body" in {

    import com.rallyhealth.optum.client.script.AuthClientSetup._

    val mpClient = new WSMemberProductEligibilityClient(optumClientRallyMemPeConfig, rqClient, None)
    val ompClient = new WSWithOAuthMemberProductEligibilityClient(cachedOAuthClient, mpClient)

    val req = PersonIdContractNumberMemPERequest("21027150", Seq("06G9048"))
    val meta = OptumRequestMetaData(
      applicationName = "RALLY",
      transactionId = "1a",
      searchType = All,
      filteringRules = FilteringRules(applyPolicyFilters = true, includeExtendedAttribtues = false))
    val expectedResponse = OptumResponseSuccess(200, Map.empty, Json.parse(SampleResponseGenerator.integrationTestExpectedSuccessResponse))

    whenReady(ompClient.findMemberProductEligibility(req, meta), timeout(Span(60, Seconds))) {
      res =>
        assert(res.isRight)
        assert(res.right.get.body === expectedResponse.body)
    }
  }
}
