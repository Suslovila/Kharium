package com.suslovila.kharium.integration

import com.emoniph.witchery.util.Config
import com.suslovila.kharium.Kharium
import cpw.mods.fml.common.FMLCommonHandler
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager

object KhariumLog {
    val logger = LogManager.getLogger(getModPrefix() + FMLCommonHandler.instance().effectiveSide)


    fun getModPrefix(): String {
        return "${Kharium.MOD_ID}: "
    }

    fun warning(msg: String) {
        logger.log(Level.WARN, getModPrefix() + msg)
    }

    fun warning(exception: Throwable, msg: String) {
        logger.log(Level.WARN, getModPrefix() + msg)
        exception.printStackTrace()
    }

    fun debug(msg: String) {
        if (Config.instance().isDebugging) {
            logger.log(Level.INFO, getModPrefix() + msg)
        }
    }

    fun traceRite(msg: String) {
        if (Config.instance().traceRites()) logger.log(Level.INFO, getModPrefix() + msg)
    }
}