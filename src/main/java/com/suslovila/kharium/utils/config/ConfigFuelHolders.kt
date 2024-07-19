package com.suslovila.kharium.utils.config

import net.minecraftforge.common.config.Configuration
import java.io.File
import kotlin.properties.Delegates

object ConfigFuelHolders {
    var basicContainerAspectCapacity by Delegates.notNull<Int>()
    var basicContainerKharuCapacity by Delegates.notNull<Int>()

    fun registerServerConfig(modCfg: File?) {
        val cfg = Configuration(modCfg)
        try {
            basicContainerAspectCapacity = cfg.getInt(
                "basic container aspect capacity",
                "Fuel Holders",
                32,
                1, Int.MAX_VALUE / 8,
                ""
            )
            basicContainerKharuCapacity = cfg.getInt(
                "basic container kharu capacity",
                "Fuel Holders",
                10_000,
                1, Int.MAX_VALUE / 8,
                ""
            )
        } catch (exception: Exception) {
            println("config died :(")
        } finally {
            cfg.save()
        }
    }
}