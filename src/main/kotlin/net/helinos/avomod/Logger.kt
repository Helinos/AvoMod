package net.helinos.avomod

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger as ApacheLogger

object Logger : ApacheLogger by LogManager.getLogger(AvoMod.NAME)
