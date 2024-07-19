package com.suslovila.kharium.integration.hooks

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.common.worldSavedData.KharuInfluenceHandler
import com.suslovila.kharium.common.worldSavedData.KharuInfluenceHandler.getKharuAmountPercentInfluence
import com.suslovila.kharium.integration.ModHook
import com.suslovila.kharium.utils.SusMathHelper
import com.suslovila.kharium.utils.SusUtils
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayerMP
import thaumcraft.api.aspects.AspectList
import thaumcraft.common.items.wands.ItemWandCasting

object ThaumcraftHook : ModHook() {
    override val modID: String = "Thaumcraft"
    override fun doInit() {
    }

    override fun doPostInit() {
    }

    override fun reducePlayerMagicPower(entity: EntityLivingBase?) {
        val player = entity as? EntityPlayerMP ?: return
        val world = entity.worldObj

        player.inventory.mainInventory.forEach { itemStack ->
            itemStack ?: return@forEach
            if(itemStack.maxStackSize != 1) return@forEach
            val wand = itemStack.item as? ItemWandCasting ?: return@forEach
            if (SusMathHelper.tryWithPercentChance(
                    chance = itemStack.getKharuAmountPercentInfluence(2.0)
                )
            ) {
                wand.consumeAllVisCrafting(
                    itemStack,
                    player,
                    AspectList().add(
                        KharuInfluenceHandler.basicAspects[SusUtils.random.nextInt(KharuInfluenceHandler.basicAspects.size)],
                        SusUtils.random.nextInt(10)
                    ),
                    true
                )
            }
        }

    }
}
