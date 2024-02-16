package com.suslovila.common.worldSavedData
//
//import com.suslovila.utils.SusNBTHelper
//import com.suslovila.utils.SusVec3
//import com.suslovila.common.sync.PacketHandler
//import com.suslovila.common.sync.PacketPrimordialExplosions
//import net.minecraft.nbt.NBTTagCompound
//import net.minecraft.nbt.NBTTagList
//import net.minecraft.world.World
//import net.minecraft.world.WorldSavedData
//import java.lang.Exception
//
//class PrimordialExplosionData : WorldSavedData(dataName){
//        val explosions = HashMap<SusVec3, Pair<Double, Int>>()
//    companion object {
//        @JvmStatic
//        val dataName = "PrimordialExplosions"
//        @JvmStatic
//        fun get(world: World): PrimordialExplosionData {
//            if (world.isRemote) throw RuntimeException("Don't access WorldSavedData on client-side!")
//            val storage = world.perWorldStorage ?: throw RuntimeException("Can't get world's map Storage")
//            with(storage) {
//                val kharuWSD = loadData(PrimordialExplosionData::class.java, dataName) as? PrimordialExplosionData ?: run {
//                        val newData = PrimordialExplosionData()
//                        newData.markDirty()
//                        setData(dataName, newData)
//                        newData
//                    }
//                return@get kharuWSD
//            }
//        }
//    }
//    init {
//        markDirty()
//    }
//    override fun readFromNBT(tag: NBTTagCompound?) {
//        val chunkList = tag?.getTagList(dataName, SusNBTHelper.TAG_COMPOUND) ?: return
//        val length = chunkList.tagCount()
//        for (i in 0 until length) {
//            val explosion = chunkList.getCompoundTagAt(i) ?: throw Exception("Nbt tag list was changed while iteration")
//            with(explosion) {
//                val info = Pair(getDouble("radius"), getInteger("timer"))
//                val pos = SusVec3(getDouble("x"), getDouble("y"), getDouble("z"))
//                explosions[pos] = info
//            }
//        }
//    }
//
//    override fun writeToNBT(tag: NBTTagCompound?) {
//        tag ?: return
//        val list = NBTTagList()
//        explosions.forEach { (pos, info) ->
//            val newTag = NBTTagCompound()
//            newTag.setDouble("x", pos.x)
//            newTag.setDouble("y", pos.y)
//            newTag.setDouble("z", pos.z)
//            newTag.setDouble("radius", info.first)
//            newTag.setInteger("timer", info.second)
//            list.appendTag(newTag)
//        }
//        tag.setTag(dataName, list)
//    }
//
//    fun addExplosion(world: World, pos : SusVec3, radius : Double){
//        explosions[pos] = Pair(radius, 0)
//        markDirty()
//        PacketHandler.INSTANCE.sendToDimension(
//            PacketPrimordialExplosions(
//                pos,
//                radius,
//                0
//            ), world.provider.dimensionId)
//    }
//}