package com.suslovila.kharium.utils.config.multistructures

import net.minecraftforge.common.config.Configuration
import java.io.File
import kotlin.properties.Delegates

object ConfigSynthesizer {

    var kharuCostPerCompositionAction by Delegates.notNull<Int>()

    lateinit var runeInfluence: Array<Double>
    fun registerServerConfig(modCfg: File?) {
        val cfg = Configuration(modCfg)
        try {
            kharuCostPerCompositionAction = cfg.getInt(
                "kharu Cost PerComposition Action",
                "multiStructures",
                500,
                0, Int.MAX_VALUE,
                ""
            )

        } catch (exception: Exception) {
            println("config died :(")
        } finally {
            cfg.save()
        }
    }
}


