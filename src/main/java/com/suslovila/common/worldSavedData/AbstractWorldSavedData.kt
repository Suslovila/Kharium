package com.suslovila.common.worldSavedData

import net.minecraft.world.World
import net.minecraft.world.WorldSavedData

//abstract class AbstractWorldSavedData<T : AbstractWorldSavedData<T>> : WorldSavedData() {
//    abstract val dataName : String
//    fun get(world: World): T {
//        if (world.isRemote) throw RuntimeException("Don't access WorldSavedData on client-side!")
//        val storage = world.mapStorage ?: throw RuntimeException("Can't get world's map Storage")
//        with(storage) {
//            val kharuWSD = loadData(T::class.java, T.dataName) as? T ?: run {
//                val newData = T()
//                setData(T.dataName, newData)
//                newData
//            }
//            return@get kharuWSD
//        }
//    }
//}