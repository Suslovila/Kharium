package com.suslovila.kharium.api.fuel

import net.minecraft.entity.player.EntityPlayer

// when using methods, you should NOT change the IMagicFuel object!
interface MagicFuel {
    fun hasPlayerEnough(player: EntityPlayer): Boolean

    // returns missing amount
    fun takeFrom(player: EntityPlayer): MagicFuel

    // returns overlapped amount
    fun addTo(player: EntityPlayer): MagicFuel

    fun isEmpty(): Boolean

    fun getNotEnoughMessage(): String

}