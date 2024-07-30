package com.suslovila.kharium.api.fuel

import baubles.api.BaublesApi
import com.suslovila.kharium.Kharium
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import thaumcraft.api.aspects.Aspect
import thaumcraft.api.aspects.AspectList
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.min

class FuelEssentia(
    val aspects: AspectList,
) : MagicFuel {

    companion object {
        val essentiaHolderProviders = mutableListOf<(EntityPlayer) -> MutableList<IEssentiaHolder>>()

        fun getPlacesToCheckForEssentia(player: EntityPlayer) =
            mutableListOf<IEssentiaHolder>().apply {
                essentiaHolderProviders.forEach { additionalHolder -> this.addAll(additionalHolder(player)) }

                val baubles = BaublesApi.getBaubles(player)
                for (a in 0 until 4) {
                    val stack = baubles.getStackInSlot(a) ?: continue
                    val itemType = stack.item
                    if (itemType is IEssentiaHolderItem) {
                        this.add(EssentiaHolderItemWrapper(itemType, stack))
                    }
                }

                for (stack in player.inventory.mainInventory) {
                    stack ?: continue
                    val itemType = stack.item
                    if (itemType is IEssentiaHolderItem) {
                        this.add(EssentiaHolderItemWrapper(itemType, stack))
                    }
                }
            }
    }

    override fun hasPlayerEnough(player: EntityPlayer): Boolean {
        val requiredEssentia = copyAspectList(this.aspects)
        val playerEssentiaHolders = getPlacesToCheckForEssentia(player)
        playerEssentiaHolders.forEach { holder ->
            val essentiaInHolder = holder.getStoredEssentia()
            essentiaInHolder.aspects.forEach { (aspectType, amount) ->
                requiredEssentia.remove(aspectType, amount)
            }
        }

        return requiredEssentia.aspects.count { entry -> entry.value > 0 } == 0
    }

    override fun takeFrom(player: EntityPlayer): MagicFuel {
        val requiredEssentia = copyAspectList(this.aspects)
        val playerEssentiaHolders = getPlacesToCheckForEssentia(player)
        playerEssentiaHolders.forEach { holder ->
            val essentiaInHolder = holder.getStoredEssentia()
            essentiaInHolder.aspects.forEach { (aspectType, amount) ->
                requiredEssentia.remove(aspectType, amount)
            }
        }

        val hasEnoughEssentia = requiredEssentia.aspects.count { entry -> entry.value > 0 } == 0
        if (hasEnoughEssentia) {
            val copyAspects = ConcurrentHashMap(copyAspectList(this.aspects).aspects)
            playerEssentiaHolders.forEach { holder ->
                val essentiaInHolder = copyAspectList(holder.getStoredEssentia())
                for (aspect in copyAspects.keys) {
                    val requiredAmount = copyAspects[aspect] ?: continue
                    val availableAmount = essentiaInHolder.aspects[aspect] ?: continue
                    val toTake = min(availableAmount, requiredAmount)
                    essentiaInHolder.remove(aspect, toTake)
                    copyAspects.remove(aspect, toTake)
                }
                holder.setStoredAspects(essentiaInHolder)
            }
            return FuelEssentia(AspectList())
        }
        return FuelEssentia(requiredEssentia)
    }

    override fun forceTakeFrom(player: EntityPlayer) {
        val playerEssentiaHolders = getPlacesToCheckForEssentia(player)
        val copyAspects = ConcurrentHashMap(copyAspectList(this.aspects).aspects)
        playerEssentiaHolders.forEach { holder ->
            val essentiaInHolder = copyAspectList(holder.getStoredEssentia())
            for (aspect in copyAspects.keys) {
                val requiredAmount = copyAspects[aspect] ?: continue
                val availableAmount = essentiaInHolder.aspects[aspect] ?: continue
                val toTake = min(availableAmount, requiredAmount)
                essentiaInHolder.remove(aspect, toTake)
                copyAspects.remove(aspect, toTake)
            }
            holder.setStoredAspects(essentiaInHolder)
        }
    }

    override fun getLack(player: EntityPlayer): MagicFuel {
        val requiredEssentia = copyAspectList(this.aspects)
        val playerEssentiaHolders = getPlacesToCheckForEssentia(player)
        playerEssentiaHolders.forEach { holder ->
            val essentiaInHolder = holder.getStoredEssentia()
            essentiaInHolder.aspects.forEach { (aspectType, amount) ->
                requiredEssentia.remove(aspectType, amount)
            }
        }
        return FuelEssentia(requiredEssentia)
    }

    fun copyAspectList(aspectList: AspectList) =
        AspectList().apply {
            aspectList.aspects.forEach { (aspectType, amount) ->
                this.add(aspectType, amount)
            }
        }

    // because of thaumcraft's aspectList encapsulation, I can't use iterator for deleting elements
    // the only variant is to use concurrent map

    override fun addTo(player: EntityPlayer): MagicFuel {
        val essentiaToAdd = ConcurrentHashMap(copyAspectList(this.aspects).aspects)
        getPlacesToCheckForEssentia(player).forEach { iEssentiaHolder ->
            essentiaToAdd.forEach { (aspect, amount) ->
                val emptySpace =
                    iEssentiaHolder.getMaxAmount() - (iEssentiaHolder.getStoredEssentia().aspects[aspect] ?: 0)
                val amountToAdd = min(emptySpace, amount)
                iEssentiaHolder.add(aspect, amountToAdd)
                essentiaToAdd.remove(aspect, amountToAdd)
            }
        }
        return FuelEssentia(AspectList().apply { this.aspects = LinkedHashMap(essentiaToAdd) })
    }

    override fun isEmpty(): Boolean =
        this.aspects.aspects.filter { entry -> entry.value != 0 }.isEmpty()

    override fun getNotEnoughMessages(): ArrayList<NotEnoughFuelNotification> {
        if(this.aspects.aspects.filter { it.value != 0 }.isEmpty()) return arrayListOf()
        val messages = arrayListOf<NotEnoughFuelNotification>()
        val notEmptyAspects = this.aspects.aspects.filter { entry -> entry.value != 0 }
        for (aspect in notEmptyAspects) {
            messages.add(
                NotEnoughFuelNotification(
                    "Not enough aspect ${aspect.key.name}: ${aspect.value}",
                    aspect.key.image,
                    aspect.key.color
                )
            )
        }
        return messages
    }
}

// aspect list objects should not be modified in outer scope

interface IEssentiaHolder {
    fun getStoredEssentia(): AspectList

    // returns the overlapping aspects
    fun setStoredAspects(aspects: AspectList)

    // returns overlapping amount
    fun add(aspect: Aspect, amount: Int): Int

    fun getMaxAmount(): Int
}

interface IEssentiaHolderItem {
    fun getStoredAspects(stack: ItemStack): AspectList

    fun setStoredAspects(stack: ItemStack, aspects: AspectList)

    fun addAspect(stack: ItemStack, aspect: Aspect, amount: Int): Int

    // returns max amount for each aspect
    fun getMaxAspectAmount(stack: ItemStack): Int
}