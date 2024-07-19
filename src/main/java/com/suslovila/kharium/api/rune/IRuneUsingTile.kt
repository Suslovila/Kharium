package com.suslovila.kharium.api.rune

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.utils.Percentage
import com.suslovila.kharium.utils.SusNBTHelper.getOrCreateDouble
import net.minecraft.nbt.NBTTagCompound
import com.suslovila.kharium.utils.plusAssign

interface IRuneUsingTile {
    companion object {
        val runeKeys = Array(RuneType.values().size) { index ->
            Kharium.prefixAppender.doAndGet("${RuneType.values()[index]}Sustain")
        }
    }

    fun getPropertyByRune(rune: RuneType) = runeFactorSustains[rune.ordinal]
    val runeFactorSustains: Array<Percentage>
    val runeFactorChangePerCheck: Array<Percentage>

    fun takeInfluence(runeType: RuneType) {
        runeFactorSustains[runeType.ordinal] += runeFactorChangePerCheck[runeType.ordinal]
    }

    fun writeRuneInfluenceStateTo(nbt: NBTTagCompound) {
        for (index in runeFactorSustains.indices) {
            nbt.setDouble(runeKeys[index], runeFactorSustains[index].value)
        }
    }

    fun readRuneInfluenceFrom(nbt: NBTTagCompound) {
        for (index in runeFactorSustains.indices) {
            runeFactorSustains[index].value = nbt.getOrCreateDouble(runeKeys[index], 1.0)
        }
    }

    fun defaultPercentage(): Array<Percentage> {
        return Array(RuneType.values().size){_ -> Percentage(1.0)}
    }
}