package com.suslovila.kharium.api.fuel

import cpw.mods.fml.common.network.ByteBufUtils
import io.netty.buffer.ByteBuf
import io.netty.buffer.ByteBufUtil
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
) {
    fun writeTo(byteBuf: ByteBuf) {
        ByteBufUtils.writeUTF8String(byteBuf, text)
        byteBuf.writeBoolean(texture != null)
        texture?.let {
            ByteBufUtils.writeUTF8String(byteBuf, texture.resourceDomain)
            ByteBufUtils.writeUTF8String(byteBuf, texture.resourcePath)
        }
        byteBuf.writeInt(color)
    }
    companion object {
        fun readFrom(byteBuf: ByteBuf): NotEnoughFuelNotification {
            val text = ByteBufUtils.readUTF8String(byteBuf)
            val texture = if(byteBuf.readBoolean()) {
                val domain = ByteBufUtils.readUTF8String(byteBuf)
                val path = ByteBufUtils.readUTF8String(byteBuf)
                ResourceLocation(
                    domain,
                    path
                )
            }
            else null
            val color = byteBuf.readInt()
            return NotEnoughFuelNotification(text, texture, color)
        }
    }
}