package com.suslovila.kharium.common.item

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.api.fuel.IEssentiaHolderItem
import com.suslovila.kharium.api.fuel.IKharuHolderItem
import com.suslovila.kharium.api.implants.RuneUsingItem
import com.suslovila.kharium.api.rune.RuneType
import com.suslovila.kharium.utils.SusNBTHelper.getOrCreateInteger
import com.suslovila.kharium.utils.SusNBTHelper.getOrCreateTag
import com.suslovila.kharium.utils.config.ConfigFuelHolders
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.StatCollector
import net.minecraft.world.World
import thaumcraft.api.aspects.Aspect
import thaumcraft.api.aspects.AspectList
import thaumcraft.common.Thaumcraft
import kotlin.math.min


object ItemPortableAspectContainer : Item(), RuneUsingItem, IEssentiaHolderItem, IKharuHolderItem {
    const val name = "aspect_holder"

    val KHARU_NBT = Kharium.prefixAppender.doAndGet("kharu_stored")

    init {
        unlocalizedName = name
        setTextureName(Kharium.MOD_ID + ":" + name)
        setMaxStackSize(1)
        creativeTab = Kharium.tab

    }


    override fun onItemRightClick(itemStackIn: ItemStack?, worldIn: World?, player: EntityPlayer): ItemStack? {
        return itemStackIn
    }

    override fun addInformation(
        stack: ItemStack,
        player: EntityPlayer,
        list: MutableList<Any?>,
        wtfIsThisVariable: Boolean
    ) {
        val aspects: AspectList = getStoredAspects(stack)
        if (aspects.size() > 0) {
            for (aspect in aspects.aspectsSorted) {
                if (Thaumcraft.proxy.playerKnowledge.hasDiscoveredAspect(player.commandSenderName, aspect)) {
                    list.add(aspect.name + " : " + aspects.getAmount(aspect))
                } else {
                    list.add(StatCollector.translateToLocal("tc.aspect.unknown"))
                }
            }
        }
        super.addInformation(stack, player, list, wtfIsThisVariable)
    }


    override fun getStoredKharu(stack: ItemStack) =
        stack.getOrCreateTag().getOrCreateInteger(KHARU_NBT, 0)


    override fun setStoredKharu(stack: ItemStack, amount: Int) {
        stack.getOrCreateTag().setInteger(KHARU_NBT, amount.coerceAtMost(getMaxKharuAmount(stack)))
    }

    override fun getMaxKharuAmount(stack: ItemStack): Int =
        (1 + RuneUsingItem.getRuneAmountOfType(stack, RuneType.OVERCLOCK)) *
                ConfigFuelHolders.basicContainerKharuCapacity

    override fun getStoredAspects(stack: ItemStack): AspectList {
        if (stack.hasTagCompound()) {
            val aspects = AspectList()
            aspects.readFromNBT(stack.tagCompound)
            return aspects
        }
        return AspectList()
    }

    override fun setStoredAspects(stack: ItemStack, aspects: AspectList) {
        aspects.writeToNBT(stack.getOrCreateTag())

    }

    override fun addAspect(stack: ItemStack, aspect: Aspect, amount: Int): Int {
        val aspects = getStoredAspects(stack)
        val emptySpace = getMaxAspectAmount(stack) - aspects.getAmount(aspect)
        val toAdd = min(emptySpace, amount)
        aspects.add(aspect, toAdd)
        aspects.writeToNBT(stack.getOrCreateTag())

        return amount - toAdd
    }

    override fun getMaxAspectAmount(stack: ItemStack): Int =
        (1 + RuneUsingItem.getRuneAmountOfType(stack, RuneType.EXPANSION)) *
                ConfigFuelHolders.basicContainerAspectCapacity

    override fun getMaxRuneAmount(): Int {
        TODO("Not yet implemented")
    }

}
