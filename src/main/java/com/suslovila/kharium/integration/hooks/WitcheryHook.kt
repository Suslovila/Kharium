package com.suslovila.kharium.integration.hooks

import com.emoniph.witchery.infusion.Infusion
import com.suslovila.kharium.Kharium
import com.suslovila.kharium.common.worldSavedData.KharuInfluenceHandler
import com.suslovila.kharium.common.worldSavedData.KharuInfluenceHandler.getKharuAmount
import com.suslovila.kharium.common.worldSavedData.KharuInfluenceHandler.getKharuAmountPercentInfluence
import com.suslovila.kharium.common.worldSavedData.KharuInfluenceHandler.getKharuLinearAmountPercent
import com.suslovila.kharium.integration.ModHook
import com.suslovila.kharium.utils.SusMathHelper
import com.suslovila.kharium.utils.config.Config
import com.suslovila.kharium.utils.config.ConfigWitchery
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.world.World

object WitcheryHook : ModHook() {
    override val modID: String = "witchery"
    override fun doInit() {
    }

    override fun doPostInit() {
    }

    override fun reducePlayerMagicPower(entity: EntityLivingBase?) {
        if (entity !is EntityPlayerMP) return
        val kharuDestroyPercent = entity.getKharuAmountPercentInfluence(3.0)
        val chance = SusMathHelper.tryWithPercentChance(kharuDestroyPercent)
        if (chance) {
            val consumed = (ConfigWitchery.kharuMaxInfluenceOnInfusionPerCheck * entity.getKharuLinearAmountPercent()).toInt()
            Infusion.Registry.instance().get(entity)
                ?.consumeEnergy(entity.worldObj, entity, consumed, false)
        }
    }

    fun Infusion.consumeEnergy(world: World, player: EntityPlayer, cost: Int, playFailSound: Boolean): Boolean {
        if (player.capabilities.isCreativeMode) {
            return true
        }
        val charges = Infusion.getCurrentEnergy(player)
        if (charges - cost <= 0) {
            clearInfusion(player)
            return false
        }
        Infusion.setCurrentEnergy(player, charges - cost)
        return true
    }
    private fun clearInfusion(player: EntityPlayer) {
        if (!player.worldObj.isRemote) {
            val nbt = Infusion.getNBT(player)
            nbt.removeTag("witcheryInfusionCharges")
            nbt.removeTag("witcheryInfusionID")
            nbt.removeTag("witcheryInfusionChargesMax")
            Infusion.syncPlayer(player.worldObj, player)
        }
    }
}