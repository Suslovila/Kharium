package com.suslovila.kharium.utils.config

import net.minecraftforge.common.config.Configuration
import java.io.File
import kotlin.properties.Delegates

object ConfigWitchery {
    var kharuMaxInfluenceOnInfusionPerCheck by Delegates.notNull<Int>()
    fun registerServerConfig(modCfg: File?) {
        val cfg = Configuration(modCfg)
        try {
            kharuMaxInfluenceOnInfusionPerCheck = cfg.getInt(
                "kharu max Infusion Influence per tick",
                "General",
                20,
                1, Int.MAX_VALUE,
                "how much infusion energy can be consumed"
            )
        } catch (var8: Exception) {
            println("пизда рулям (конфигу)")
        } finally {
            cfg.save()
        }
    }
}


