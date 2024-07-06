package com.suslovila.kharium.api.fuel

import net.minecraft.item.ItemStack
import thaumcraft.api.aspects.Aspect
import thaumcraft.api.aspects.AspectList

class EssentiaHolderItemWrapper(
    val itemType: IEssentiaHolderItem,
    val itemStackIn: ItemStack
) : IEssentiaHolder {
    override fun getStoredEssentia(): AspectList =
        itemType.getStoredAspects(itemStackIn)

    override fun setStoredAspects(aspects: AspectList) {
        itemType.setStoredAspects(itemStackIn, aspects)
    }





    override fun add(aspect: Aspect, amount: Int): Int =
        itemType.addAspect(itemStackIn, aspect, amount)

    override fun getMaxAmount(): Int {
        itemType.getMaxAmount(itemStackIn)
    }
}