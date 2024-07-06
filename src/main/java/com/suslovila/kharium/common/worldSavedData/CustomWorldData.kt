package com.suslovila.kharium.common.worldSavedData

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.utils.SusNBTHelper
import com.suslovila.kharium.utils.SusVec3
import com.suslovila.kharium.common.sync.KhariumPacketHandler
import com.suslovila.kharium.common.sync.PacketKharuHotbeds
import com.suslovila.kharium.common.sync.PacketPrimordialExplosions
import com.suslovila.kharium.common.sync.PacketSyncSingleKharuHotbed
import com.suslovila.kharium.utils.SusNBTHelper.forEach
import com.suslovila.kharium.utils.SusUtils
import io.netty.buffer.ByteBuf
import net.minecraft.nbt.*
import net.minecraft.util.AxisAlignedBB
import net.minecraft.world.*
import java.util.*


class CustomWorldData(datakey: String) : WorldSavedData(datakey) {

    companion object {
        const val TAG_KHARU_DATA = "kharuData"
        const val TAG_EXPLOSION_DATA = "explosionData"

        val World.customData: CustomWorldData
            get() {
                val dimensionId = provider.dimensionId
                val name = "${Kharium.MOD_ID}_$dimensionId"
                var data = perWorldStorage.loadData(CustomWorldData::class.java, name) as? CustomWorldData
                if (data == null) {
                    data = CustomWorldData(name)
                    data.markDirty()
                    perWorldStorage.setData(name, data)
                }
                return data
            }
    }

    val explosions: LinkedList<Explosion> = LinkedList()
    val kharuHotbeds = arrayListOf<KharuHotbed>()

    override fun readFromNBT(tag: NBTTagCompound?) {
        val explosionList = tag?.getTagList(TAG_EXPLOSION_DATA, SusNBTHelper.TAG_COMPOUND) ?: return
        explosions.clear()
        explosionList.forEach { explosionTag ->
            with(explosionTag) {
                val remainingBlocksToDestroy = LinkedList<SusVec3>()
                getTagList("blocks", SusNBTHelper.TAG_COMPOUND).forEach { pos ->
                    remainingBlocksToDestroy.add(
                        SusVec3(
                            pos.getInteger("x"),
                            pos.getInteger("y"),
                            pos.getInteger("z")
                        )
                    )
                }
                val pos = SusVec3(getDouble("x"), getDouble("y"), getDouble("z"))
                explosions.add(
                    Explosion(
                        pos = pos,
                        radius = getDouble("radius"),
                        timer = getInteger("timer"),
                        remainingBlocksToDestroy = remainingBlocksToDestroy
                    )
                )
            }
        }
        val kharuInfo = tag.getTagList(TAG_KHARU_DATA, SusNBTHelper.TAG_COMPOUND) ?: return
        kharuHotbeds.clear()
        kharuInfo.forEach { hotbedTag ->
            val hotbed = KharuHotbed.readFrom(hotbedTag) ?: return@forEach
            kharuHotbeds.add(hotbed)
        }
    }

    override fun writeToNBT(rootTag: NBTTagCompound?) {
        rootTag ?: return
        var explosionTaglist = NBTTagList()
        explosions.forEach { explosion ->
            val explosionTag = NBTTagCompound()
            explosionTag.setDouble("x", explosion.pos.x)
            explosionTag.setDouble("y", explosion.pos.y)
            explosionTag.setDouble("z", explosion.pos.z)
            explosionTag.setDouble("radius", explosion.radius)
            explosionTag.setInteger("timer", explosion.timer)

            val listBlocks = NBTTagList()
            explosion.remainingBlocksToDestroy.forEach { blockpos ->
                val blockTag = NBTTagCompound()
                blockTag.setDouble("x", blockpos.x)
                blockTag.setDouble("y", blockpos.y)
                blockTag.setDouble("z", blockpos.z)
                listBlocks.appendTag(blockTag)
            }
            explosionTag.setTag("blocks", listBlocks)
            explosionTaglist.appendTag(explosionTag)
        }
        rootTag.setTag(TAG_EXPLOSION_DATA, explosionTaglist)

        explosionTaglist = NBTTagList()
        kharuHotbeds.forEach { hotbed ->
            val newTag = NBTTagCompound()
            hotbed.writeTo(newTag)
            explosionTaglist.appendTag(newTag)
        }
        rootTag.setTag(TAG_KHARU_DATA, explosionTaglist)
    }

    fun addExplosion(world: World, pos: SusVec3, radius: Double) {
        val blocks = SusUtils.getSphereShapeCords(pos, radius).sortedWith(compareBy { it.distanceTo(pos) })
        explosions.add(Explosion(pos, radius, 0, LinkedList(blocks)))
        markDirty()
        KhariumPacketHandler.INSTANCE.sendToDimension(PacketPrimordialExplosions(explosions), world.provider.dimensionId)
    }

    fun syncExplosions(world: World) {
        KhariumPacketHandler.INSTANCE.sendToDimension(PacketPrimordialExplosions(explosions), world.provider.dimensionId)
    }

    fun syncAllHotbeds(world: World) {
        KhariumPacketHandler.INSTANCE.sendToDimension(PacketKharuHotbeds(kharuHotbeds), world.provider.dimensionId)
    }

    fun syncAddHotbed(world: World, hotbed: KharuHotbed) {
        KhariumPacketHandler.INSTANCE.sendToDimension(
            PacketSyncSingleKharuHotbed(
                hotbed,
                PacketSyncSingleKharuHotbed.SYNC_TYPE.ADD
            ), world.provider.dimensionId
        )
    }

    fun syncRemoveHotbed(world: World, hotbed: KharuHotbed) {
        KhariumPacketHandler.INSTANCE.sendToDimension(
            PacketSyncSingleKharuHotbed(
                hotbed,
                PacketSyncSingleKharuHotbed.SYNC_TYPE.REMOVE
            ), world.provider.dimensionId
        )
    }

    fun addKharuHotbed(hotbed: KharuHotbed, world: World) {
        kharuHotbeds.add(hotbed)
        markDirty()
        syncAddHotbed(world, hotbed)
    }

    fun removeKharuHotbed(hotbed: KharuHotbed, world: World) {
        kharuHotbeds.remove(hotbed)
        markDirty()
        syncRemoveHotbed(world, hotbed)
    }
}

class Explosion(val pos: SusVec3, val radius: Double, var timer: Int, var remainingBlocksToDestroy: LinkedList<SusVec3>)


object AxisWrapper {
    fun AxisAlignedBB.writeTo(rootNbt: NBTTagCompound) {
        val tag = NBTTagCompound()
        tag.setDouble("minX", minX)
        tag.setDouble("minY", minX)
        tag.setDouble("minZ", minX)
        tag.setDouble("maxX", maxX)
        tag.setDouble("maxY", maxY)
        tag.setDouble("maxZ", maxZ)

        rootNbt.setTag(KharuInfluenceHandler.ZONE_NBT, tag)
    }

    fun AxisAlignedBB.writeTo(buf: ByteBuf) {
        buf.writeDouble(minX)
        buf.writeDouble(minY)
        buf.writeDouble(minZ)
        buf.writeDouble(maxX)
        buf.writeDouble(maxY)
        buf.writeDouble(maxZ)

    }

    fun readFrom(rootNbt: NBTTagCompound): AxisAlignedBB? {
        val tag = rootNbt.getCompoundTag(KharuInfluenceHandler.ZONE_NBT) ?: return null
        if (
            !tag.hasKey("minX") ||
            !tag.hasKey("minY") ||
            !tag.hasKey("minZ") ||
            !tag.hasKey("maxX") ||
            !tag.hasKey("maxY") ||
            !tag.hasKey("maxZ")
        ) return null
        return AxisAlignedBB.getBoundingBox(
            tag.getDouble("minX"),
            tag.getDouble("minY"),
            tag.getDouble("minZ"),
            tag.getDouble("maxX"),
            tag.getDouble("maxY"),
            tag.getDouble("maxZ")
        )
    }

    fun readFrom(buf: ByteBuf): AxisAlignedBB {
        val minX = buf.readDouble()
        val minY = buf.readDouble()
        val minZ = buf.readDouble()
        val maxX = buf.readDouble()
        val maxY = buf.readDouble()
        val maxZ = buf.readDouble()
        return AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ)
    }

    val AxisAlignedBB.center: SusVec3
        get() {
            return SusVec3(
                minX + (maxX - minX) / 2,
                minY + (maxY - minY) / 2,
                minZ + (maxZ - minZ) / 2
            )
        }
}