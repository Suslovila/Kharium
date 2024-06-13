package com.suslovila.kharium.integration.potion

import net.minecraft.item.ItemStack
import net.minecraft.potion.PotionEffect

abstract class PotionWrapper {
    abstract fun addEffect(itemStack: ItemStack, effect : PotionEffect)
    abstract fun getEffects(itemStack: ItemStack)
    abstract fun removeEffect(itemStack: ItemStack, effect : PotionEffect)

}