package com.suslovila.kharium.utils.config.multistructures

import com.suslovila.kharium.api.rune.RuneType
import net.minecraftforge.common.config.Configuration
import java.io.File
import kotlin.properties.Delegates

object ConfigKharuContainer {

    var basicCapacity by Delegates.notNull<Int>()

    lateinit var runeInfluence: Array<Double>
    fun registerServerConfig(modCfg: File?) {
        val cfg = Configuration(modCfg)
        try {
            basicCapacity = cfg.getInt(
                "antiNode Sustain Decrease Per Check",
                "multiStructures",
                10_000,
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


