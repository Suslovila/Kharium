package com.suslovila.common.worldSavedData

import com.suslovila.utils.SusNBTHelper
import com.suslovila.utils.SusVec3
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraft.world.World
import net.minecraft.world.WorldSavedData
import java.lang.Exception


//class KharuLevelChunkData : WorldSavedData(dataName) {
//    private val kharuStats: HashMap<Pair<Int, Int>, Int> = HashMap()
//
//    companion object {
//        @JvmStatic
//        val dataName = "kharuLevel"
//
//        @JvmStatic
//        fun get(world: World): KharuLevelChunkData {
//            if (world.isRemote) throw RuntimeException("Don't access WorldSavedData on client-side!")
//            val storage = world.mapStorage ?: throw RuntimeException("Can't get world's map Storage")
//            with(storage) {
//                val kharuWSD = loadData(KharuLevelChunkData::class.java, dataName) as? KharuLevelChunkData ?: run {
//                    val newData = KharuLevelChunkData()
//                    setData(dataName, newData)
//                    newData
//                }
//                return@get kharuWSD
//            }
//        }
//    }
//    override fun readFromNBT(tag: NBTTagCompound?) {
//        val chunkList = tag?.getTagList(dataName, SusNBTHelper.TAG_COMPOUND) ?: return
//        val length = chunkList.tagCount()
//        for (i in 0 until length) {
//            val chunkData = chunkList.getCompoundTagAt(i) ?: throw Exception("Nbt tag list was changed while iteration")
//            val chunkPos = Pair(chunkData.getInteger("x"), chunkData.getInteger("z"))
//            kharuStats[chunkPos] = chunkData.getInteger("amount")
//        }
//    }
//
//    override fun writeToNBT(tag: NBTTagCompound?) {
//        tag ?: return
//        val list = NBTTagList()
//        kharuStats.forEach { (pos, amount) ->
//            val newTag = NBTTagCompound()
//            newTag.setInteger("x", pos.first)
//            newTag.setInteger("z", pos.second)
//            newTag.setInteger("amount", amount)
//            list.appendTag(newTag)
//        }
//        tag.setTag(PrimordialExplosionData.dataName, list)
//    }
//
//    fun addKharuToChunkByBlockPos(pos: SusVec3, amount: Int) {
//        val chunkPos = Pair(pos.x.toInt(), pos.z.toInt())
//        val prevAmount = kharuStats[chunkPos]
//        if (prevAmount != null) kharuStats[chunkPos] = (prevAmount + amount).coerceAtLeast(0)
//        else kharuStats[chunkPos] = amount
//        markDirty()
//    }
//
//    fun setKharuToChunkByBlockPos(pos: SusVec3, amount: Int) {
//        kharuStats[Pair(pos.x.toInt(), pos.z.toInt())] = amount
//        markDirty()
//    }
//
//    fun getKharuLevelByBlockPos(pos: SusVec3) {
//        kharuStats[Pair(pos.x.toInt(), pos.z.toInt())] ?: 0
//        markDirty()
//    }
//}