package com.suslovila.kharium.utils.config

import net.minecraftforge.common.config.Configuration
import thaumcraft.api.aspects.Aspect
import java.io.File
import kotlin.properties.Delegates

object ConfigPortableContainer {
    var basicContainerAspectCapacity by Delegates.notNull<Int>()
    var basicContainerKharuCapacity by Delegates.notNull<Int>()
    var basicContainerAspectTypeAmount by Delegates.notNull<Int>()

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

            basicContainerAspectTypeAmount = cfg.getInt(
                "basic container aspect amount",
                "Fuel Holders",
                4,
                // to be honest, there should be max amount of Aspects, but they are not initialised
                // by this moment I guess
                1, 100,
                ""
            )
        } catch (exception: Exception) {
            println("config died :(")
        } finally {
            cfg.save()
        }
    }
}