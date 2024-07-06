package com.suslovila.kharium.common.item

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.api.fuel.IEssentiaHolderItem
import com.suslovila.kharium.api.fuel.IKharuHolderItem
import com.suslovila.kharium.api.implants.RuneUsingItem
import com.suslovila.kharium.utils.SusNBTHelper.getOrCreateInteger
import com.suslovila.kharium.utils.SusNBTHelper.getOrCreateTag
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.StatCollector
import net.minecraft.world.World
import thaumcraft.api.aspects.Aspect
import thaumcraft.api.aspects.AspectList
import thaumcraft.api.aspects.IEssentiaContainerItem
import thaumcraft.common.Thaumcraft


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
        val aspects: AspectList? = getAspects(stack)
        if (aspects != null && aspects.size() > 0) {
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

    override fun getAspects(itemstack: ItemStack): AspectList? {
        if (itemstack.hasTagCompound()) {
            val aspects = AspectList()
            aspects.readFromNBT(itemstack.tagCompound)
            return if (aspects.size() > 0) aspects else null
        }
        return null
    }

    override fun setAspects(stack: ItemStack, list: AspectList?) {
        list?.writeToNBT(stack.getOrCreateTag())
    }

    override fun getStoredKharu(stack: ItemStack) =
        stack.getOrCreateTag().getOrCreateInteger(KHARU_NBT, 0)


    override fun setStoredKharu(stack: ItemStack, amount: Int) {
        stack.getOrCreateTag().setInteger(KHARU_NBT, amount)
    }

    override fun getMaxAmount(stack: ItemStack): Int =
        stack.getOrCreateTag().


    override fun getStoredAspects(stack: ItemStack): AspectList {
        if (stack.hasTagCompound()) {
            val aspects = AspectList()
            aspects.readFromNBT(stack.tagCompound)
            return if (aspects.size() > 0) aspects else null
        }
        return null
    }

    override fun setStoredAspects(stack: ItemStack, aspects: AspectList): AspectList {
        aspects.writeToNBT(stack.getOrCreateTag())

    }

    override fun addAspect(stack: ItemStack, aspect: Aspect, amount: Int): Int {

    }

    override fun getMaxRuneAmount(): Int {
        TODO("Not yet implemented")
    }

}
