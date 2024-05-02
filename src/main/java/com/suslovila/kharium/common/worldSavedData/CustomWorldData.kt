package com.suslovila.kharium.common.worldSavedData

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.utils.SusNBTHelper
import com.suslovila.kharium.utils.SusVec3
import com.suslovila.kharium.common.sync.PacketHandler
import com.suslovila.kharium.common.sync.PacketPrimordialExplosions
import com.suslovila.kharium.utils.SusNBTHelper.forEach
import com.suslovila.kharium.utils.SusUtils
import net.minecraft.nbt.*
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
    val kharuStats = arrayListOf<KharuHotbed>()

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
        kharuStats.clear()
        kharuInfo.forEach { hotbedTag ->
            with(hotbedTag) {
                val zone = Pair
                kharuStats[zone] = getInteger("amount")
            }
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
        kharuStats.forEach { (pos, amount) ->
            val newTag = NBTTagCompound()
            newTag.setInteger("x", pos.first)
            newTag.setInteger("z", pos.second)
            newTag.setInteger("amount", amount)
            explosionTaglist.appendTag(newTag)
        }
        rootTag.setTag(TAG_KHARU_DATA, explosionTaglist)
    }

    fun addExplosion(world: World, pos: SusVec3, radius: Double) {
        val blocks = SusUtils.getSphereShapeCords(pos, radius).sortedWith(compareBy { it.distanceTo(pos) })
        explosions.add(Explosion(pos, radius, 0, LinkedList(blocks)))
        markDirty()
        PacketHandler.INSTANCE.sendToDimension(PacketPrimordialExplosions(explosions), world.provider.dimensionId)
    }

    fun syncExplosions(world: World) {
        PacketHandler.INSTANCE.sendToDimension(PacketPrimordialExplosions(explosions), world.provider.dimensionId)
    }

    fun addKharuToChunkByBlockPos(pos: SusVec3, amount: Int) {
        val chunkPos = Pair(pos.x.toInt(), pos.z.toInt())
        val prevAmount = kharuStats[chunkPos]
        if (prevAmount != null) kharuStats[chunkPos] = (prevAmount + amount).coerceAtLeast(0)
        else kharuStats[chunkPos] = amount
        markDirty()
    }

    fun setKharuToChunkByBlockPos(pos: SusVec3, amount: Int) {
        kharuStats[Pair(pos.x.toInt(), pos.z.toInt())] = amount
        markDirty()
    }

    fun getKharuLevelByBlockPos(pos: SusVec3) {
        kharuStats[Pair(pos.x.toInt(), pos.z.toInt())] ?: 0
        markDirty()
    }
}

class Explosion(val pos: SusVec3, val radius: Double, var timer: Int, var remainingBlocksToDestroy: LinkedList<SusVec3>)