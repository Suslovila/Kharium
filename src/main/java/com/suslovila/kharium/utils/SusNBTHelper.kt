package com.suslovila.kharium.utils

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.utils.SusNBTHelper.getOrCreateUUID
import com.suslovila.kharium.utils.SusNBTHelper.setUUID
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import java.util.*

object SusNBTHelper {
    const val OBJECT_HEADER = 64
    const val ARRAY_HEADER = 96
    const val OBJECT_REFERENCE = 32
    const val STRING_SIZE = 224
    const val TAG_END = 0
    const val TAG_BYTE = 1
    const val TAG_SHORT = 2
    const val TAG_INT = 3
    const val TAG_LONG = 4
    const val TAG_FLOAT = 5
    const val TAG_DOUBLE = 6
    const val TAG_BYTE_ARRAY = 7
    const val TAG_STRING = 8
    const val TAG_LIST = 9
    const val TAG_COMPOUND = 10
    const val TAG_INT_ARRAY = 11
    const val TAG_LONG_ARRAY = 12
    const val TAG_ANY_NUMERIC = 99
    const val MAX_DEPTH = 512

    fun ItemStack.getOrCreateTag(): NBTTagCompound {
        if (!hasTagCompound()) tagCompound = NBTTagCompound()
        return tagCompound
    }

    inline fun NBTTagList.forEach(block: (NBTTagCompound) -> Unit) {
        for (i in 0 until tagCount()) {
            val tag = getCompoundTagAt(i)
            block(tag)
        }
    }

    fun Int.writeTo(rootNbt: NBTTagCompound, key: String) {
        rootNbt.setInteger(key, this)
    }

    fun Double.writeTo(rootNbt: NBTTagCompound, key: String) {
        rootNbt.setDouble(key, this)
    }

    fun String.writeTo(rootNbt: NBTTagCompound, key: String) {
        rootNbt.setString(key, this)
    }

    fun Float.writeTo(rootNbt: NBTTagCompound, key: String) {
        rootNbt.setFloat(key, this)
    }

    fun Boolean.writeTo(rootNbt: NBTTagCompound, key: String) {
        rootNbt.setBoolean(key, this)
    }


    fun NBTTagCompound.getOrCreateInteger(key: String, defaultValue: Int): Int {
        if (!hasKey(key)) {
            setInteger(key, defaultValue)
            return defaultValue
        }
        return getInteger(key)
    }


    fun NBTTagCompound.getOrCreateString(key: String, defaultValue: String): String {
        if (!hasKey(key)) {
            setString(key, defaultValue)
            return defaultValue
        }
        return getString(key)
    }

    fun NBTTagCompound.getOrCreateBoolean(key: String, defaultValue: Boolean): Boolean {
        if (!hasKey(key)) {
            setBoolean(key, defaultValue)
            return defaultValue
        }
        return getBoolean(key)
    }

    fun NBTTagCompound.getOrCreateFloat(key: String, defaultValue: Float): Float {
        if (!hasKey(key)) {
            setFloat(key, defaultValue)
            return defaultValue
        }
        return getFloat(key)
    }

    fun NBTTagCompound.getOrCreateDouble(key: String, defaultValue: Double): Double {
        if (!hasKey(key)) {
            setDouble(key, defaultValue)
            return defaultValue
        }
        return getDouble(key)
    }

    val UUID_LEAST_NBT = Kharium.prefixAppender.doAndGet("UUIDLeast")
    val UUID_MOST_NBT = Kharium.prefixAppender.doAndGet("UUIDMost")

    fun NBTTagCompound.setUUID(key: String, value: UUID) {
        val innerTag = NBTTagCompound()
        innerTag.setLong(UUID_LEAST_NBT, value.leastSignificantBits)
        innerTag.setLong(UUID_MOST_NBT, value.mostSignificantBits)
        this.setTag(key, innerTag)
    }

    fun NBTTagCompound.getOrCreateUUID(key: String, defaultValue: UUID): UUID {
        if (!hasKey(key)) {
            val innerTag = NBTTagCompound()
            innerTag.setLong(UUID_MOST_NBT, defaultValue.mostSignificantBits)
            innerTag.setLong(UUID_LEAST_NBT, defaultValue.leastSignificantBits)
            this.setTag(key, innerTag)
            return defaultValue
        }
        return getUUID(key)
    }

    fun NBTTagCompound.getUUIDOrNull(key: String): UUID? =
        if(hasKey(key)) getUUID(key) else null
    fun NBTTagCompound.getUUID(key: String): UUID {
        val innerTag = getCompoundTag(key)
        return UUID(innerTag.getLong(UUID_MOST_NBT), innerTag.getLong(UUID_LEAST_NBT))
    }

}