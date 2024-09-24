package com.suslovila.kharium.common.item

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.api.fuel.IEssentiaHolderItem
import com.suslovila.kharium.api.fuel.IKharuHolderItem
import com.suslovila.kharium.api.implants.RuneUsingItem
import com.suslovila.kharium.api.rune.RuneType
import com.suslovila.kharium.client.gui.KhariumGui
import com.suslovila.kharium.utils.SusNBTHelper.getOrCreateInteger
import com.suslovila.kharium.utils.SusNBTHelper.getOrCreateTag
import com.suslovila.kharium.utils.config.ConfigPortableContainer
import com.suslovila.sus_multi_blocked.utils.Position
import com.suslovila.sus_multi_blocked.utils.getTile
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.StatCollector
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import thaumcraft.api.aspects.Aspect
import thaumcraft.api.aspects.AspectList
import thaumcraft.api.aspects.IAspectContainer
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
        list.add("kharu amount: ${getStoredKharu(stack)}")
        super.addInformation(stack, player, list, wtfIsThisVariable)
    }


    override fun getStoredKharu(stack: ItemStack) =
        stack.getOrCreateTag().getOrCreateInteger(KHARU_NBT, 0)


    override fun setStoredKharu(stack: ItemStack, amount: Int) {
        stack.getOrCreateTag().setInteger(KHARU_NBT, amount.coerceAtMost(getMaxKharuAmount(stack)))
    }

    override fun getMaxKharuAmount(stack: ItemStack): Int =
        (1 + RuneUsingItem.getRuneAmountOfType(stack, RuneType.OVERCLOCK)) *
                ConfigPortableContainer.basicContainerKharuCapacity

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
                ConfigPortableContainer.basicContainerAspectCapacity

    override fun getMaxRuneAmount(): Int {
        return 5
    }


    fun getRequiredAspect(stack: ItemStack): Aspect? {
        val tag = stack.getOrCreateTag()
        if (!tag.hasKey(REQUIRED_ASPECT_NBT)) return null
        return Aspect.getAspect(tag.getString(REQUIRED_ASPECT_NBT))
    }

    fun getRequiredAmount(stack: ItemStack): Int {
        val tag = stack.getOrCreateTag()
        if (!tag.hasKey(REQUIRED_ASPECT_AMOUNT_NBT)) return 0
        return tag.getInteger(REQUIRED_ASPECT_AMOUNT_NBT)
    }

    @SubscribeEvent
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_AIR) {
            val stack = event.entityPlayer.heldItem ?: return
            if (stack.item is ItemPortableAspectContainer) {
                event.entityPlayer.openGui(
                    Kharium.MOD_ID,
                    KhariumGui.ITEM_ASPECT_HOLDER.ordinal,
                    event.world,
                    event.entityPlayer.posX.toInt(),
                    event.entityPlayer.posY.toInt(),
                    event.entityPlayer.posZ.toInt()
                )
            }
        }
        if (event.action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
            val stack = event.entityPlayer.heldItem ?: return
            (event.world.getTile(
                Position(
                    event.x,
                    event.y,
                    event.z
                )
            ) as? IAspectContainer)?.let { blockAspectContainer ->
                val requiredAspect = getRequiredAspect(stack)
                val requiredAmount = getRequiredAmount(stack)
                val stored = this.getStoredAspects(stack)
                val hasEnough = blockAspectContainer.doesContainerContainAmount(requiredAspect, requiredAmount)
                val hasSpace = stored.aspects.filter { it.value != 0 }.size <= getMaxAspectTypeAmount(stack)

                if (hasEnough && hasSpace) {
                    blockAspectContainer.takeFromContainer(requiredAspect, requiredAmount)
                    val result = this.getStoredAspects(stack).add(requiredAspect, requiredAmount)
                    setStoredAspects(stack, result)
                }
            }
        }
    }
    fun getMaxAspectTypeAmount(stack: ItemStack) : Int =
        (1 + RuneUsingItem.getRuneAmountOfType(stack, RuneType.OVERCLOCK)) *
                ConfigPortableContainer.basicContainerAspectTypeAmount


    val REQUIRED_ASPECT_NBT = Kharium.prefixAppender.doAndGet("required_aspects")
    val REQUIRED_ASPECT_AMOUNT_NBT = Kharium.prefixAppender.doAndGet("required_amount")

}
