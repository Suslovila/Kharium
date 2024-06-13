package com.suslovila.kharium.integration.hooks

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.common.worldSavedData.KharuInfluenceHandler.getKharuAmountPercentInfluence
import com.suslovila.kharium.integration.ModHook
import com.suslovila.kharium.utils.SusMathHelper
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.init.Items
import net.minecraft.item.ItemPotion
import net.minecraft.item.ItemStack

object VanillaHook : ModHook() {
    override val modID: String = Kharium.MOD_ID
    override fun doInit() {
    }

    override fun doPostInit() {
    }

    override fun reducePlayerMagicPower(entity: EntityLivingBase?) {
        val player = entity as? EntityPlayerMP ?: return
        player.inventory.mainInventory.forEachIndexed { index, itemStack ->
            itemStack ?: return@forEachIndexed
            val kharuDestroyPercent = itemStack.getKharuAmountPercentInfluence(8.0)
            // enchantments
            itemStack.enchantmentTagList?.tagList?.removeAll {
                SusMathHelper.tryWithPercentChance(kharuDestroyPercent)
            }
            if (itemStack.enchantmentTagList?.tagList?.isEmpty() == true) {
                itemStack.stackTagCompound.removeTag("ench")
            }
            // potions
            val successForPotion = SusMathHelper.tryWithPercentChance(kharuDestroyPercent)

            if (itemStack.item is ItemPotion && successForPotion) {
                player.inventory.mainInventory[index] = ItemStack(Items.glass_bottle)
            }
        }
        // potion effects
        player.activePotionEffects.forEach { effect ->

        }
    }
}