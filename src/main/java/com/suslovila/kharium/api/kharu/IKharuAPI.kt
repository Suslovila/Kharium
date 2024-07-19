package com.suslovila.kharium.api.kharu

import net.minecraft.item.ItemStack


// plays role of consumer and supplier in one
interface IKharuContainer {
    fun getStoredKharuAmount(): Int
    fun getCapacity(): Int

    fun setKharuAmount(amount: Int)

    // returns amount which was successfully taken
    fun takeKharu(amount: Int): Int

    // returns exclusive amount that was not put
    fun putKharu(amount: Int): Int

    // how many kharu does it need right now
    fun getRequiredAmount(): Int

    // how many kharu can it take or output per second
    fun getConduction(): Int
}

