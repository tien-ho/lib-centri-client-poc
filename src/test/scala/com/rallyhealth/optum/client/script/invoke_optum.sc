import com.rallyhealth.optum.client.v8.common._
import com.rallyhealth.optum.client.v8.auth.model.OptumAccessTokenRequest
import com.rallyhealth.optum.client.v8.auth.ws.{WSOptumOauthClient, CachedOptumOauthClient}
import com.rallyhealth.optum.client.v8.common.OptumClientRallyConfig
import com.rallyhealth.optum.client.v8.member.model.{OptumRequestMetaData, FilteringRules, All, Big5MemPERequest}
import com.rallyhealth.optum.client.v8.member.ws.{WSWithOAuthMemberProductEligibilityClient, WSMemberProductEligibilityClient}
import com.rallyhealth.spartan.v2.config.RallyConfig
import com.rallyhealth.optum.client.v8.auth.model._
import com.rallyhealth.optum.client.v8.member.ws._
import com.rallyhealth.optum.client.v8.member._
import com.rallyhealth.optum.client.v8.member.model._
import com.rallyhealth.rq.v1.logging._
import com.rallyhealth.rq.v1.stats._
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import org.slf4j.LoggerFactory
import play.api.libs.ws.ahc.AhcWSClient
import scala.concurrent.ExecutionContext.Implicits._
import com.rallyhealth.rq.v1.play25._
import com.rallyhealth.optum.client.v8.auth.ws._
import com.rallyhealth.rq.v1._
import net.sf.ehcache._
import play.api.cache._
import com.rallyhealth.stats.v3.{Stats, KeyPrefixedStats, HttpClientStats}

implicit val system = ActorSystem()
implicit val materializer = ActorMaterializer()

val rc = RallyConfig("program.conf")
val oc = new OptumClientRallyConfig(rc)
val tr = OptumAccessTokenRequest("l7xxdc3f1a43be1a4373ad43073bb85f4539","c1059417b3264915a60e271f1f5e5731")
val wsClient = AhcWSClient()
val stats = HttpClientStats.build(new KeyPrefixedStats(Stats, "optum.client"), LoggerFactory.getLogger("optum-script"))
val playBkEnd = new StatsRqBackend(stats)(new LoggingRqBackend("optum",1000)(new Play25RqBackend(wsClient)))
val rqClient = new RqClient(playBkEnd)
val cm = CacheManager.create()
cm.addCache("oauth")
val capi = new EhCacheApi(cm.getCache("oauth"))

val client = new WSOptumOauthClient(oc, tr, rqClient)
val cclient = new CachedOptumOauthClient(client, capi)
val mpClient = new WSMemberProductEligibilityClient(oc, rqClient)
val ompClient = new WSWithOAuthMemberProductEligibilityClient(cclient, mpClient)

//val req = PersonIdContractNumberMemPERequest("21027150", "06G9048")
val req = Big5MemPERequest("JAMES", "CREGAN", "1968-07-22T00:00:00Z", "00155765700","06G90485")
val meta = OptumRequestMetaData(applicationName = "RALLY", transactionId = "1a", searchType = All, filteringRules = FilteringRules(applyPolicyFilters = false, includeExtendedAttribtues = false))

ompClient.findMemberProductEligibility(req, meta)
