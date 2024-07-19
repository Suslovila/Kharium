//package com.suslovila.kharium.api.essentia
//
//import baubles.api.BaublesApi
//import com.suslovila.kharium.api.fuel.MagicFuel
//import com.suslovila.kharium.api.kharu.IKharuHolder
//import net.minecraft.entity.player.EntityPlayer
//import net.minecraft.item.ItemStack
//import thaumcraft.api.aspects.AspectList
//import thaumcraft.api.aspects.IEssentiaContainerItem
//import kotlin.math.min
//
//object EssentiaHelper {
//
//    val essentiaHolderProviders = mutableListOf<(EntityPlayer) -> MutableList<ItemStack>>()
//    val kharuHolderProviders = mutableListOf<(EntityPlayer) -> MutableList<ItemStack>>()
//
//    fun getPlacesToCheckForEssentia(player: EntityPlayer) =
//        mutableListOf<ItemStack>().apply {
//            essentiaHolderProviders.forEach { additionalHolder -> this.addAll(additionalHolder(player).filter { it.item !is IEssentiaContainerItem }) }
//
//            val baubles = BaublesApi.getBaubles(player)
//            for (a in 0 until 4) {
//                val stack = baubles.getStackInSlot(a) ?: continue
//                if (stack.item is IEssentiaContainerItem) {
//                    this.add(stack)
//                }
//            }
//
//            for (stack in player.inventory.mainInventory) {
//                stack ?: continue
//                if (stack.item is IEssentiaContainerItem) {
//                    this.add(stack)
//                }
//            }
//        }
//
//    fun getPlacesToCheckForKharu(player: EntityPlayer) =
//        mutableListOf<ItemStack>().apply {
//            kharuHolderProviders.forEach { additionalHolder -> this.addAll(additionalHolder(player).filter { it.item is IKharuHolder }) }
//            val baubles = BaublesApi.getBaubles(player)
//            for (a in 0 until 4) {
//                val stack = baubles.getStackInSlot(a) ?: continue
//                if (stack.item is IKharuHolder) {
//                    this.add(stack)
//                }
//            }
//
//            for (stack in player.inventory.mainInventory) {
//                stack ?: continue
//                if (stack.item is IKharuHolder) {
//                    this.add(stack)
//                }
//            }
//        }
//
//    fun takeEssentiaFromPlayer(player: EntityPlayer, aspectList: AspectList, doIt: Boolean): Boolean {
//        val requiredEssentia = copyAspectList(aspectList)
//        val playerEssentiaHolders = getPlacesToCheckForEssentia(player)
//        playerEssentiaHolders.forEach { holder ->
//            val essentiaInHolder =
//                (holder.item as? IEssentiaContainerItem)?.getAspects(holder) ?: return@forEach
//            essentiaInHolder.aspects.forEach { (aspectType, amount) ->
//                requiredEssentia.remove(aspectType, amount)
//            }
//        }
//        val hasEnoughEssentia = requiredEssentia.aspects.count { entry -> entry.value > 0 } == 0
//        if (hasEnoughEssentia && doIt) {
//            val copyForDecreasing = copyAspectList(aspectList)
//            playerEssentiaHolders.forEach { holder ->
//                val essentiaInHolder = (holder.item as? IEssentiaContainerItem)?.getAspects(holder) ?: return@forEach
//                for (aspect in copyForDecreasing.aspects.keys) {
//                    val requiredAmount = copyForDecreasing.aspects[aspect] ?: continue
//                    val availableAmount = essentiaInHolder.aspects[aspect] ?: continue
//                    val toTake = min(availableAmount, requiredAmount)
//                    essentiaInHolder.remove(aspect, availableAmount - toTake)
//                    copyForDecreasing.remove(aspect, requiredAmount - toTake)
//                }
//                (holder.item as? IEssentiaContainerItem)?.setAspects(holder, essentiaInHolder)
//            }
//            return true
//        }
//        return hasEnoughEssentia
//    }
//
//    fun takeStoredKharuFromPlayer(player: EntityPlayer, amount: Int, doIt: Boolean): Boolean {
//        val kharuStorages = getPlacesToCheckForKharu(player)
//        val hasEnoughKharu =
//            kharuStorages.sumOf { stack -> (stack.item as? IKharuHolder)?.getStoredKharu(stack) ?: 0 } >= amount
//        if (hasEnoughKharu && doIt) {
//            var amountLeft = amount
//            kharuStorages.forEach { stack ->
//                var stored = (stack.item as? IKharuHolder)?.getStoredKharu(stack) ?: 0
//                val taken = min(stored, amountLeft)
//                amountLeft -= taken
//                stored -= taken
//                (stack.item as? IKharuHolder)?.setStoredKharu(stack, stored)
//            }
//            return true
//        }
//        return hasEnoughKharu
//    }
//
//    // optimisation purposes: function is combined from previous ones
//    fun takeMagicFuelFromPlayer(player: EntityPlayer, fuel: MagicFuel): Boolean {
//        val kharuStorages = getPlacesToCheckForKharu(player)
//        val hasEnoughKharu = kharuStorages.sumOf { stack ->
//            (stack.item as? IKharuHolder)?.getStoredKharu(stack) ?: 0
//        } >= fuel.kharuAmount
//
//        // essentia
//        val requiredEssentia = copyAspectList(fuel.aspects)
//        val playerEssentiaHolders = getPlacesToCheckForEssentia(player)
//        playerEssentiaHolders.forEach { holder ->
//            val essentiaInHolder =
//                (holder.item as? IEssentiaContainerItem)?.getAspects(holder) ?: return@forEach
//            essentiaInHolder.aspects.forEach { (aspectType, amount) ->
//                requiredEssentia.remove(aspectType, amount)
//            }
//        }
//        val hasEnoughEssentia = requiredEssentia.aspects.count { entry -> entry.value > 0 } == 0
//
//        if (hasEnoughEssentia && hasEnoughKharu) {
//            // essentia
//            val copyForDecreasing = copyAspectList(fuel.aspects)
//            playerEssentiaHolders.forEach { holder ->
//                val essentiaInHolder = (holder.item as? IEssentiaContainerItem)?.getAspects(holder) ?: return@forEach
//                for (aspect in copyForDecreasing.aspects.keys) {
//                    val requiredAmount = copyForDecreasing.aspects[aspect] ?: continue
//                    val availableAmount = essentiaInHolder.aspects[aspect] ?: continue
//                    val toTake = min(availableAmount, requiredAmount)
//                    essentiaInHolder.remove(aspect, availableAmount - toTake)
//                    copyForDecreasing.remove(aspect, requiredAmount - toTake)
//                }
//                (holder.item as? IEssentiaContainerItem)?.setAspects(holder, essentiaInHolder)
//            }
//
//            // kharu
//            var amountLeft = fuel.kharuAmount
//            kharuStorages.forEach { stack ->
//                var stored = (stack.item as? IKharuHolder)?.getStoredKharu(stack) ?: 0
//                val taken = min(stored, amountLeft)
//                amountLeft -= taken
//                stored -= taken
//                (stack.item as? IKharuHolder)?.setStoredKharu(stack, stored)
//            }
//            return true
//        }
//        return false
//    }
//
//    fun getPlayerMagicFuel(player: EntityPlayer): MagicFuel {
//        val fuel = MagicFuel(AspectList(), 0)
//        getPlacesToCheckForEssentia(player).forEach { place ->
//            val essentiaInHolder =
//                (place.item as? IEssentiaContainerItem)?.getAspects(place) ?: return@forEach
//            essentiaInHolder.aspects.forEach { (aspectType, amount) ->
//                fuel.aspects.add(aspectType, amount)
//            }
//        }
//        getPlacesToCheckForKharu(player).forEach { place ->
//            val stored = (place.item as? IKharuHolder)?.getStoredKharu(place) ?: 0
//            fuel.kharuAmount += stored
//        }
//        return fuel
//    }
//
//    fun copyAspectList(aspectList: AspectList) =
//        AspectList().apply {
//            aspectList.aspects.forEach { (aspectType, amount) ->
//                this.add(aspectType, amount)
//            }
//        }
//}