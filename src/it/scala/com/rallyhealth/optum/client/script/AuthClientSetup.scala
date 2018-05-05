package com.rallyhealth.optum.client.script

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import com.rallyhealth.optum.client.v8.auth.model.OptumAccessTokenRequest
import com.rallyhealth.optum.client.v8.auth.ws.{CachedOptumOauthClient, WSOptumOauthClient}
import com.rallyhealth.optum.client.v8.common.OptumClientRallyConfig
import com.rallyhealth.optum.client.v8.config.config.ConfigIntakeRallyConfig
import com.rallyhealth.optum.client.v8.member.config.MemberProductEligibilityRallyConfig
import com.rallyhealth.optum.client.v8.product.config.ProductEligibilityRallyConfig
import com.rallyhealth.rq.v1.RqClient
import com.rallyhealth.rq.v1.logging.LoggingRqBackend
import com.rallyhealth.rq.v1.play25.Play25RqBackend
import com.rallyhealth.rq.v1.stats.StatsRqBackend
import com.rallyhealth.spartan.v2.config.RallyConfig
import com.rallyhealth.stats.v3.{HttpClientStats, KeyPrefixedStats, Stats}
import net.sf.ehcache.CacheManager
import org.slf4j.LoggerFactory
import play.api.cache.EhCacheApi
import play.api.libs.ws.ahc.AhcWSClient

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Random

object AuthClientSetup {
  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  val rallyConfig = RallyConfig("program.conf")
  val optumClientRallyConfig = new OptumClientRallyConfig(rallyConfig)
  val optumClientRallyMemPeConfig = new MemberProductEligibilityRallyConfig(rallyConfig)
  val optumClientRallyPeConfig = new ProductEligibilityRallyConfig(rallyConfig)
  val optumConfigIntakeConfig = new ConfigIntakeRallyConfig(rallyConfig)
  val tockenRequest = OptumAccessTokenRequest("l7xxdc3f1a43be1a4373ad43073bb85f4539", "c1059417b3264915a60e271f1f5e5731")
  val wsClient = AhcWSClient()
  val stats = HttpClientStats.build(new KeyPrefixedStats(Stats, "optum.client"), LoggerFactory.getLogger("optum-script"))
  val playBkEnd = new StatsRqBackend(stats)(new LoggingRqBackend("optum", 1000)(new Play25RqBackend(wsClient)))
  val rqClient = new RqClient(playBkEnd)
  val cacheManager = CacheManager.create()
  private val cacheName = s"oauth-int-${Random.nextInt(20000)}"
  cacheManager.addCache(cacheName)

  val cacheApi = new EhCacheApi(cacheManager.getCache(cacheName))
  val optumOAuthClient = new WSOptumOauthClient(optumClientRallyConfig, tockenRequest, rqClient)
  val cachedOAuthClient = new CachedOptumOauthClient(optumOAuthClient, cacheApi)
}
