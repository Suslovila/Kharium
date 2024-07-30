package com.suslovila.kharium.api.fuel

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.ResourceLocation
import java.awt.Color

// when using methods, you should NOT change the IMagicFuel object!
interface MagicFuel {
    fun hasPlayerEnough(player: EntityPlayer): Boolean

    // returns missing amount
    fun takeFrom(player: EntityPlayer): MagicFuel

    // takes fuel with NO CHECKS. You must be sure that player has enough when firing this
    fun forceTakeFrom(player: EntityPlayer)

    //returns not enough amount and do not take fuel even if has enough
    fun getLack(player: EntityPlayer): MagicFuel
    // returns overlapped amount
    fun addTo(player: EntityPlayer): MagicFuel

    fun isEmpty(): Boolean
    // to prevent messages like "not enough 0 amount" you should check here if amount of your fuel is zero
    fun getNotEnoughMessages(): ArrayList<NotEnoughFuelNotification>

}

class NotEnoughFuelNotification(
    val text: String,
    val texture: ResourceLocation?,
    val color: Int = Color.white.rgb
)