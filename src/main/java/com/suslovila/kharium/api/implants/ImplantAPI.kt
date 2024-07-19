package com.suslovila.kharium.api.implants

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.api.rune.RuneType
import com.suslovila.kharium.utils.SusNBTHelper.getOrCreateInteger
import com.suslovila.kharium.utils.SusNBTHelper.getOrCreateTag

import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.IIcon
import net.minecraftforge.client.event.RenderHandEvent
import net.minecraftforge.client.event.RenderPlayerEvent
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.event.entity.living.*
import net.minecraftforge.event.entity.player.AttackEntityEvent
import thaumcraft.api.aspects.AspectList

enum class ImplantType(
) {
    HEAD,
    BRAIN,
    SKELETON,
    HAND,
    LEGS,
    FOOT,
    HEART,
    CIRCULATORY_SYSTEM,
    OCULAR_SYSTEM,
    NERVOUS_SYSTEM,
    SKIN,
    LUNGS;

    companion object {
        val slotAmounts = mutableListOf<Int>()
        val iconLocations = mutableListOf<IIcon>()
        val ImplantType.slotAmount: Int
            get() = slotAmounts[this.ordinal]

        val ImplantType.icon: IIcon
            get() = iconLocations[this.ordinal]

        val typeAmount = ImplantType.values().size

        val firstIndexes by lazy {
            val array = Array(typeAmount) { _ -> 0 }
            for (i in 1 until typeAmount) {
                val previousType = (ImplantType.values()[i - 1])
                array[i] = array[i - 1] + previousType.slotAmount
            }
            array
        }

        val slotAmount by lazy { firstIndexes.last() + ImplantType.values().last().slotAmount }

        fun getFirstSlotIndexOf(type: ImplantType) =
            firstIndexes[type.ordinal]

        fun getTypeForSlotWithIndex(slotIndex: Int): ImplantType? {
            for (typeIndex in 1 until typeAmount) {
                val isRequiredSlotBetween =
                    slotIndex < firstIndexes[typeIndex] && slotIndex >= firstIndexes[typeIndex - 1]
                if (isRequiredSlotBetween) {
                    return ImplantType.values()[typeIndex - 1]
                }

                if (slotIndex >= firstIndexes.last() && slotIndex < slotAmount) {
                    return ImplantType.values().last()
                }
            }
            return null
        }
    }

}


interface RuneUsingItem {
    fun getMaxRuneAmount(): Int

    companion object {
        fun getRuneAmountOfType(stack: ItemStack, type: RuneType) =
            if (stack.item is RuneUsingItem) {
                stack.getOrCreateTag()
                    .getOrCreateInteger(Kharium.MOD_ID + ":rune_amount_of_type: " + type.toString(), 0)
            } else {
                0
            }

        fun setRuneAmountOfType(stack: ItemStack, type: RuneType, amount: Int) {
            val runeClass = stack.item
            if (runeClass is RuneUsingItem) {
                stack.getOrCreateTag().setInteger(
                    Kharium.MOD_ID + ":rune_amount_of_type: " + type.toString(),
                    amount.coerceAtMost(runeClass.getMaxRuneAmount())
                )
            }
        }
    }
}


abstract class ItemImplant(val implantType: ImplantType) : Item(), RuneUsingItem {
    abstract val abilities: ArrayList<Ability>

    init {
        maxStackSize = 1
    }

    open fun onRenderWorldLastEvent(event: RenderWorldLastEvent, index: Int, implant: ItemStack) {
        abilities.forEach { it.onRenderWorldLastEvent(event, index, implant) }
    }

    open fun onRenderHandEvent(event: RenderHandEvent, index: Int, implant: ItemStack) {
        abilities.forEach { it.onRenderHandEvent(event, index, implant) }
    }

    open fun onRenderPlayerEvent(event: RenderPlayerEvent.Post, index: Int, implant: ItemStack) {
        abilities.forEach { it.onRenderPlayerEvent(event, index, implant) }
    }

    open fun onPlayerAttackEntityEvent(event: AttackEntityEvent, index: Int, implant: ItemStack) {
        abilities.forEach { it.onPlayerAttackEntityEvent(event, index, implant) }
    }

    open fun onPlayerHealEvent(event: LivingHealEvent, index: Int, implant: ItemStack) {
        abilities.forEach { it.onPlayerHealEvent(event, index, implant) }
    }

    open fun onPlayerUpdateEvent(event: LivingEvent.LivingUpdateEvent, index: Int, implant: ItemStack) {
        abilities.forEach { it.onPlayerUpdateEvent(event, index, implant) }
    }

    open fun onPlayerHurtEvent(event: LivingHurtEvent, index: Int, implant: ItemStack) {
        abilities.forEach { it.onPlayerHurtEvent(event, index, implant) }
    }

    open fun onPlayerDeathEvent(event: LivingDeathEvent, index: Int, implant: ItemStack) {
        abilities.forEach { it.onPlayerDeathEvent(event, index, implant) }
    }

    open fun onPlayerBeingAttackedEvent(event: LivingAttackEvent, index: Int, implant: ItemStack) {
        abilities.forEach { it.onPlayerBeingAttackedEvent(event, index, implant) }
    }

    open fun onPlayerSetAttackTargetEvent(event: LivingSetAttackTargetEvent, index: Int, implant: ItemStack) {
        abilities.forEach { it.onPlayerSetAttackTargetEvent(event, index, implant) }
    }

}
