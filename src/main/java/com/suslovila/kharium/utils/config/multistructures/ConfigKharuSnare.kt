package com.suslovila.kharium.utils.config.multistructures

import com.suslovila.kharium.api.implants.ImplantType
import com.suslovila.kharium.api.rune.RuneType
import net.minecraftforge.common.config.Configuration
import java.io.File
import kotlin.properties.Delegates

object ConfigKharuSnare {

    var antiNodeSustainDecreasePerCheck by Delegates.notNull<Double>()

    lateinit var runeInfluence: Array<Double>
    fun registerServerConfig(modCfg: File?) {
        val cfg = Configuration(modCfg)
        try {
            antiNodeSustainDecreasePerCheck = cfg.getFloat(
                "antiNode Sustain Decrease Per Check",
                "multiStructures",
                -0.05f,
                -1f, 0f,
                ""
            ).toDouble()

            runeInfluence = Array(RuneType.values().size){index ->
                cfg.getFloat(
                    "antiNode ${RuneType.values()[index]} rune influence per check",
                    "multiStructures",
                    0.05f,
                    0f, 1f,
                    ""
                ).toDouble()
            }
        } catch (exception: Exception) {
            println("config died :(")
        } finally {
            cfg.save()
        }
    }
}


