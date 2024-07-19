package com.suslovila.kharium.integration.hooks

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.common.worldSavedData.KharuInfluenceHandler.getKharuAmountPercentInfluence
import com.suslovila.kharium.common.worldSavedData.KharuInfluenceHandler.getKharuLinearAmountPercent
import com.suslovila.kharium.integration.ModHook
import com.suslovila.kharium.utils.SusMathHelper
import com.suslovila.kharium.utils.config.Config
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.init.Items
import net.minecraft.item.ItemPotion
import net.minecraft.item.ItemStack
import vazkii.botania.api.mana.IManaItem

object BotaniaHook : ModHook() {
    override val modID: String = "Botania"
    override fun doInit() {
    }

    override fun doPostInit() {
    }

    override fun reducePlayerMagicPower(entity: EntityLivingBase?) {
        val player = entity as? EntityPlayerMP ?: return
        player.inventory.mainInventory.forEach { itemStack ->
            itemStack ?: return
            if(itemStack.maxStackSize != 1) return@forEach
            val manaItem = itemStack.item as? IManaItem ?: return@forEach
            if (SusMathHelper.tryWithPercentChance(
                    chance = itemStack.getKharuAmountPercentInfluence(2.0)
                )
            ) {
                var toDecrease = (itemStack.getKharuLinearAmountPercent() * Config.maxManaReduce).toInt()
                val manaPrevious = manaItem.getMana(itemStack)
                if (toDecrease > manaPrevious) toDecrease = manaPrevious
                manaItem.addMana(
                    itemStack,
                    -toDecrease
                )
            }
        }
    }
}