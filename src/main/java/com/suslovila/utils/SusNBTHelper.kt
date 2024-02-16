package com.suslovila.utils

import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList

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
}