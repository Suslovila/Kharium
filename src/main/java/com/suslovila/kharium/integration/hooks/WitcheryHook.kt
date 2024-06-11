package com.suslovila.kharium.integration.hooks

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.integration.ModHook
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayerMP

object WitcheryHook : ModHook() {
    override val modID: String = "Witchery"
    override fun doInit() {
    }

    override fun doPostInit() {
    }

    override fun reducePlayerMagicPower(entity: EntityLivingBase?) {
        if (entity !is EntityPlayerMP) return

    }
}