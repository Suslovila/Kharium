package com.suslovila.kharium.api.fuel

import net.minecraft.entity.player.EntityPlayer

class FuelComposite(
    val fuels: List<MagicFuel>
) {
    fun hasPlayerEnough(player: EntityPlayer): Boolean =
        fuels.all { it.hasPlayerEnough(player) }

    // returns missing amount
    fun takeFrom(player: EntityPlayer): FuelComposite =
        FuelComposite(ArrayList(fuels).onEach { it.takeFrom(player) })

    // returns overlapped amount
    fun addTo(player: EntityPlayer): FuelComposite =
        FuelComposite(ArrayList(fuels).onEach { it.addTo(player) })

    fun isEmpty(): Boolean =
        fuels.all { it.isEmpty() }

    fun getNotEnoughMessage(): String =
        String().apply {
            fuels.forEach {fuel ->
                this.plus(fuel.getNotEnoughMessage())
            }
        }
}