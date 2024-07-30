package com.suslovila.kharium.common.event

import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.client.event.RenderWorldLastEvent

abstract class GlobalEvent(
    var x: Double,
    var y: Double,
    var z: Double,
    var duration: Int,
    var timer: Int,

    var expired: Boolean = false
) {
    fun tick() {
        if(expired) return
        timer += 1
        if(timer >= duration) {
            onExpired()
            expired = true
        }
    }

    abstract fun onCreated()
    abstract fun onUpdate(event: WorldTickEvent)
    abstract fun onRendered(event: RenderWorldLastEvent)

    abstract fun onExpired()


    open fun writeToNbt(nbt: NBTTagCompound) {
        nbt.setDouble("x", x)
        nbt.setDouble("y", y)
        nbt.setDouble("z", z)

        nbt.setInteger("timer", timer)
        nbt.setInteger("duration", duration)
        nbt.setBoolean("expired", expired)
    }


    open fun readFromNbt(nbt: NBTTagCompound)  {
        nbt.setDouble("x", x)
        nbt.setDouble("y", y)
        nbt.setDouble("z", z)

        nbt.setInteger("timer", timer)
        nbt.setInteger("duration", duration)
        nbt.setBoolean("expired", expired)
    }
}