package com.suslovila.kharium.utils.config

import com.suslovila.kharium.api.implants.ImplantType
import net.minecraftforge.common.config.Configuration
import java.io.File
import kotlin.properties.Delegates

object ConfigImlants {

    fun registerServerConfig(modCfg: File?) {
        val cfg = Configuration(modCfg)
        try {
            for (type in ImplantType.values()) {
                ImplantType.slotAmounts.add(
                    type.ordinal, cfg.getInt(
                        "implant $type slot amount",
                        "Implants",
                        3,
                        1, Int.MAX_VALUE,
                        ""
                    )
                )
            }
        } catch (exception: Exception) {
            println("config died :(")
        } finally {
            cfg.save()
        }
    }
}


