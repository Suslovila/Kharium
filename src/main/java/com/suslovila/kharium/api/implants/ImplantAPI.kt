package com.suslovila.kharium.api.implants

import com.suslovila.kharium.utils.SusNBTHelper.getOrCreateTag

import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.IIcon
import net.minecraftforge.client.event.RenderHandEvent
import net.minecraftforge.client.event.RenderPlayerEvent
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.event.entity.living.*
import net.minecraftforge.event.entity.player.AttackEntityEvent
import java.time.temporal.TemporalAmount

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

// all events have "normal" priority by default
// if any other needed, you should implement all logic by yourself

enum class RuneType {
    OVERCLOCK,
    EXPANSION,
    STABILISATION,
    CONTAINMENT
}


abstract class RuneUsingItem : Item() {
    companion object {
        fun getRuneAmountOfType(stack: ItemStack, type: RuneType) =
            if (stack.item is RuneUsingItem)
                stack.getOrCreateTag().getInteger(type.toString())
            else 0

        fun setRuneAmountOfType(stack: ItemStack, type: RuneType, amount: Int) {
            if (stack.item is RuneUsingItem)
                stack.getOrCreateTag().setInteger(type.toString(), amount)
        }
    }

}


abstract class ItemImplant(val implantType: ImplantType) : Item() {

    init {
        maxStackSize = 1
    }

    open fun onRenderWorldLastEvent(event: RenderWorldLastEvent, stack: ItemStack) {}
    open fun onRenderHandEvent(event: RenderHandEvent, stack: ItemStack) {}
    open fun onRenderPlayerEvent(event: RenderPlayerEvent, stack: ItemStack) {}
    open fun onPlayerAttackEntityEvent(event: AttackEntityEvent, stack: ItemStack) {}
    open fun onPlayerHealEvent(event: LivingHealEvent, stack: ItemStack) {}
    open fun onPlayerUpdateEvent(event: LivingEvent.LivingUpdateEvent, stack: ItemStack) {}
    open fun onPlayerHurtEvent(event: LivingHurtEvent, stack: ItemStack) {}
    open fun onPlayerDeathEvent(event: LivingDeathEvent, stack: ItemStack) {}
    open fun onPlayerBeingAttackedEvent(event: LivingAttackEvent, stack: ItemStack) {}
    open fun onPlayerSetAttackTargetEvent(event: LivingSetAttackTargetEvent, stack: ItemStack) {}
}