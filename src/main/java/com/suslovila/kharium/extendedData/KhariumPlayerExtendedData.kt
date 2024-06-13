package com.suslovila.kharium.extendedData

import com.emoniph.witchery.Witchery
import com.emoniph.witchery.common.ExtendedPlayer
import com.suslovila.kharium.api.implants.ImplantStorage
import com.suslovila.kharium.common.sync.PacketKhariumPlayerExtended
import cpw.mods.fml.common.network.simpleimpl.IMessage
import io.netty.buffer.ByteBuf
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.world.World
import net.minecraftforge.common.IExtendedEntityProperties

class KhariumPlayerExtendedData(
    val player: EntityPlayer,
    public var implantStorage: ImplantStorage = ImplantStorage(),
    public var kharuAmount: Int = 0
) : IExtendedEntityProperties {

    companion object {
        private const val EXT_PROP_NAME = "KhariumExtendedPlayer"
        private const val KHARU_NBT = "KharuNbt"

        fun get(player: EntityPlayer): KhariumPlayerExtendedData? {
            return player.getExtendedProperties(EXT_PROP_NAME) as? KhariumPlayerExtendedData
        }

        fun register(player: EntityPlayer) {
            player.registerExtendedProperties(EXT_PROP_NAME, KhariumPlayerExtendedData(player))
        }
    }


    override fun init(entity: Entity?, world: World?) {
    }

    override fun saveNBTData(rootTag: NBTTagCompound) {
        val properties = NBTTagCompound()
        implantStorage.writeToNBT(rootTag)
        rootTag.setInteger(KHARU_NBT, kharuAmount)

        rootTag.setTag(EXT_PROP_NAME, properties)

    }

    override fun loadNBTData(rootTag: NBTTagCompound) {
        if (rootTag.hasKey(EXT_PROP_NAME)) {
            kharuAmount = rootTag.getInteger(KHARU_NBT)
            implantStorage.readFromNBT(rootTag)
        }
    }

    fun writeTo(buf: ByteBuf) {
        buf.writeInt()
    }
    fun readFrom(buf: ByteBuf) {

    }

    fun sync() {
        if (!player.worldObj.isRemote) Witchery.packetPipeline.sendTo(
            PacketKhariumPlayerExtended(this),
            player
        )
    }

    fun loadProxyData(player: EntityPlayer?) {
        if (player != null) {
            val playerEx = ExtendedPlayer.get(player)
            playerEx.sync()
        }
    }
}