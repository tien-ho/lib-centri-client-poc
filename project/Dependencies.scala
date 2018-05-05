import com.rallyhealth.sbt.shading.ShadingImplicits._
import sbt._

object Dependencies {

  val libRqClientVersion = "1.1.0"
  val libSpartanVersion = "2.3.0"

  object Rally {
    // LIBRARY DEPENDENCIES | Dos and Don'ts: https://wiki.audaxhealth.com/x/Wp_8AQ
    // internal dependencies, keep alpha
    val libCarestats = "com.rallyhealth.core" %% "lib-carestats-core" % "4.1.0" shaded
    val libEnigmaPlay25 = "com.rallyhealth.core" %% "lib-enigma-play25" % "4.1.0" shaded
    val libRallyRqClientApi = "com.rallyhealth.rq" %% "lib-rq-client-api" % libRqClientVersion shaded
    val libRallyRqClientBuilder = "com.rallyhealth.rq" %% "lib-rq-client-builder" % libRqClientVersion shaded
    val libRallyRqClientLogging = "com.rallyhealth.rq" %% "lib-rq-client-logging" % libRqClientVersion shaded
    val libRallyRqClientPlay25 = "com.rallyhealth.rq" %% "lib-rq-client-play25" % libRqClientVersion shaded
    val libRallyRqClientTest = "com.rallyhealth.rq" %% "lib-rq-client-testkit" % libRqClientVersion shaded
    val libSpartanPlay25Json = "com.rallyhealth.core" %% "lib-spartan-play25-json" % "2.4.0" shaded
    val playJsonOps = "com.rallyhealth" %% "play-json-ops-25" % "1.5.0"
    val playJsonTests = "com.rallyhealth" %% "play-json-tests-25" % "1.5.0" % "test"
    val scalacheckOps = "com.rallyhealth" %% "scalacheck-ops" % "1.5.0" % "test"

  }

  object Ext {
    // external dependencies, keep alpha
    val apacheCommons = "org.apache.commons" % "commons-lang3" % "3.5.0"
    val logback = "ch.qos.logback" % "logback-classic" % "1.2.2" // logback-mesos.xml makes use of the newer features.
    val mockito = "org.mockito" % "mockito-all" % "1.10.19" % "test"
    val playCache = "com.typesafe.play" %% "play-cache" % "2.5.13"
    val scalaCheck = "org.scalacheck" %% "scalacheck" % "1.13.4" % "test"
    val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0"
    val scalaTestPlay25 = "org.scalatest" %% "scalatest" % "3.0.1" % "test"
    val scalaTestPlus = "org.scalatestplus" %% "play" % "1.4.0" % "test"
  }

}
