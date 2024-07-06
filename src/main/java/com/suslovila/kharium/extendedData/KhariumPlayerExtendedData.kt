package com.suslovila.kharium.extendedData

import com.suslovila.kharium.api.implants.ImplantStorage
import com.suslovila.kharium.common.sync.KhariumPacketHandler
import com.suslovila.kharium.common.sync.PacketKhariumPlayerExtended
import io.netty.buffer.ByteBuf
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.world.World
import net.minecraftforge.common.IExtendedEntityProperties
class KhariumDataForSync (
    val playerId: Int,
    var implantStorage: ImplantStorage = ImplantStorage(),
    var kharuAmount: Int = 0
)
class KhariumPlayerExtendedData(
    val player: EntityPlayer,
    var implantStorage: ImplantStorage = ImplantStorage(),
    var kharuAmount: Int = 0
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

        fun loadProxyData(player: EntityPlayer?) {
            if (player != null) {
                val playerEx = get(player)
                playerEx?.sync()
            }
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
        implantStorage.writeTo(buf)
        buf.writeInt(kharuAmount)

    }


    fun sync() {
        if (!player.worldObj.isRemote) KhariumPacketHandler.INSTANCE.sendToAll(
            PacketKhariumPlayerExtended(this)
        )
    }
}