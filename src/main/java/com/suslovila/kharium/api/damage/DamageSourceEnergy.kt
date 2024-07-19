package com.suslovila.kharium.api.damage

import com.suslovila.kharium.Kharium
import net.minecraft.util.DamageSource

object DamageSourceEnergy : DamageSource(Kharium.prefixAppender.doAndGet("energy")) {
    init {
        setDamageBypassesArmor()
        setDamageIsAbsolute()
        setDamageAllowedInCreativeMode()
    }
}