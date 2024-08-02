package com.suslovila.kharium.api.fuel

import com.suslovila.kharium.common.sync.KhariumPacketHandler
import com.suslovila.kharium.common.sync.PacketLackNotifications
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import thaumcraft.client.lib.PlayerNotifications

class FuelComposite(
    val fuels: List<MagicFuel>
) {
    fun hasPlayerEnough(player: EntityPlayer): Boolean =
        fuels.all { it.hasPlayerEnough(player) }

    // returns missing amount (should check if player has enough and do not take if not enough)
    fun takeFrom(player: EntityPlayer): FuelComposite =
        FuelComposite(fuels.map { it.takeFrom(player) })

    //I recommend write takeFrom() method, and then split it in getLack() and forceTakeFrom()
    fun forceTakeFrom(player: EntityPlayer) {
        fuels.forEach { it.forceTakeFrom(player) }
    }

    // returns not enough amount
    fun getLack(player: EntityPlayer): FuelComposite =
        FuelComposite(fuels.map { it.getLack(player) })

    // returns overlapped amount
    fun addTo(player: EntityPlayer): FuelComposite =
        FuelComposite(fuels.map { it.addTo(player) })

    fun isEmpty(): Boolean =
        fuels.all { it.isEmpty() }

    fun getNotEnoughMessages(): List<NotEnoughFuelNotification> =
        fuels.flatMap { it.getNotEnoughMessages() }.distinct()

    fun notifyPlayerAboutLack() {
        getNotEnoughMessages().forEach {
            PlayerNotifications.addNotification(it.text, it.texture, it.color)
        }
    }

    fun notifyClientAboutLack(player: EntityPlayerMP) {
        KhariumPacketHandler.INSTANCE.sendTo(PacketLackNotifications(getNotEnoughMessages()), player)
    }

    fun tryTakeFuelFromPlayerWithPacket(player: EntityPlayer): Boolean {
        val amountLeft = this.getLack(player)
        val hasEnough = amountLeft.isEmpty()
        if (hasEnough) {
            this.forceTakeFrom(player)
        }
        else {
            (player as? EntityPlayerMP)?.let {
                amountLeft.notifyClientAboutLack(it)
            }
        }

        return hasEnough
    }

    fun tryTakeFuelFromPlayer(player: EntityPlayer): Boolean {
        val amountLeft = this.getLack(player)
        val hasEnough = amountLeft.isEmpty()
        if (hasEnough) {
            this.forceTakeFrom(player)
        }
        else {
            if(player.worldObj.isRemote) {
                amountLeft.notifyPlayerAboutLack()
            }
        }

        return hasEnough
    }
}
