
package com.suslovila.kharium.api.fuel

import net.minecraft.item.ItemStack

class KharuHolderItemWrapper(
    val itemType: IKharuHolderItem,
    val itemStackIn: ItemStack
) : IKharuHolder {
    override fun getStoredKharu(): Int =
        itemType.getStoredKharu(itemStackIn)

    override fun setStoredKharu(amount: Int): Int =
        itemType.setStoredKharu(itemStackIn, amount)

    override fun getMaxAmount(): Int =
        itemType.getMaxAmount(itemStackIn)

}