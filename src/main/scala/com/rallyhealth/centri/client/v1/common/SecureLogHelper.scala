package com.rallyhealth.centri.client.v1.common

import com.rallyhealth.enigma.v4.logging.SecureLogger
import org.slf4j.{Logger, LoggerFactory}

/**
  * Helps to setup secure logging
  */
trait SecureLogHelper {

    lazy val secureLogger: SecureLogger = SecureLogger.slf4jLggerToSlf4jSecureLogger(LoggerFactory.getLogger(getClass))
}
