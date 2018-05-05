package com.rallyhealth.centri.client.v1.common

import java.time.Instant

import com.rallyhealth.rq.v1.RqResponse
import com.rallyhealth.stats.v4.{KeyPrefixedStats, Stats}

/**
  * Helps with building statistics
  */
trait StatsHelper {
    private val stats: Stats = KeyPrefixedStats(Stats, "optum.client")
    def statPath: Seq[String]

    def statIt(key: String) = stats.inc(key)

    def statTiming(key: String, duration: Int) = stats.timing(key, duration)

    def emitError(errorCode: Int) =
        statIt(stringify(combine(statPath, Error) :+ errorCode.toString))

    def emitError(path: Seq[String], errorCode: Int) =
        statIt(stringify(combine(statPath ++ path, Error) :+ errorCode.toString))

    def emitSuccess(successCode: Int) =
        statIt(stringify(combine(statPath, Success) :+ successCode.toString))

    def emitSuccess(path: Seq[String], successCode: Int) =
        statIt(stringify(combine(statPath ++ path, Success) :+ successCode.toString))

    def emitTiming(path: Seq[String], start: Long) =
        statTiming(stringify(statPath ++ path ++ Seq("receive")), (Instant.now().toEpochMilli - start).toInt)

    def emitErrorStatsWithTiming(path: Seq[String], rsp: RqResponse, startedAt: Long): Unit = {
        emitTiming(path, startedAt)
        emitError(rsp.status)
        emitError(path, rsp.status)
    }

    def emitSuccessStatsWithTiming(path: Seq[String], rsp: RqResponse, startedAt: Long): Unit = {
        emitTiming(path, startedAt)
        emitSuccess(rsp.status)
        emitSuccess(path, rsp.status)
    }

    private def combine(path: Seq[String], `type`: StatType): Seq[String] = {
        path :+ `type`.toString
    }

    private def stringify(path: Seq[String]) = path.map(_.trim.toLowerCase).mkString(".")
}

sealed trait StatType

case object Success extends StatType
case object Error extends StatType
