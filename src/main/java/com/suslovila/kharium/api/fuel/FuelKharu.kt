package com.suslovila.kharium.api.fuel

import baubles.api.BaublesApi
import com.suslovila.kharium.Kharium
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import kotlin.math.min

class FuelKharu(val amount: Int) : MagicFuel {
    companion object {
        val texture = ResourceLocation(Kharium.MOD_ID, "textures/misc/antinode.png")

        val kharuHolderProviders = mutableListOf<(EntityPlayer) -> MutableList<IKharuHolder>>()
        fun getPlacesToCheckForKharu(player: EntityPlayer) =
            mutableListOf<IKharuHolder>().apply {
                kharuHolderProviders.forEach { additionalHolder -> this.addAll(additionalHolder(player)) }
                val baubles = BaublesApi.getBaubles(player)
                for (a in 0 until 4) {
                    val stack = baubles.getStackInSlot(a) ?: continue
                    val itemType = stack.item
                    if (itemType is IKharuHolderItem) {
                        this.add(KharuHolderItemWrapper(itemType, stack))
                    }
                }

                for (stack in player.inventory.mainInventory) {
                    stack ?: continue
                    val itemType = stack.item
                    if (itemType is IKharuHolderItem) {
                        this.add(KharuHolderItemWrapper(itemType, stack))
                    }
                }
            }
    }

    override fun hasPlayerEnough(player: EntityPlayer): Boolean =
        getPlacesToCheckForKharu(player).sumOf { holder ->
            holder.getStoredKharu()
        } >= this.amount


    override fun takeFrom(player: EntityPlayer): MagicFuel {
        val kharuStorages = getPlacesToCheckForKharu(player)
        var storedInPlayer = kharuStorages.sumOf { storage -> storage.getStoredKharu() }
        val hasEnoughKharu = storedInPlayer >= this.amount
        if (hasEnoughKharu) {
            var amountLeft = this.amount
            kharuStorages.forEach { storage ->
                val taken = min(storage.getStoredKharu(), amountLeft)
                amountLeft -= taken
                storage.setStoredKharu(storage.getStoredKharu() - taken)
            }
            return FuelKharu(0)
        }
        return FuelKharu(this.amount - storedInPlayer)
    }

    override fun forceTakeFrom(player: EntityPlayer) {
        val kharuStorages = getPlacesToCheckForKharu(player)
        var amountLeft = this.amount
        kharuStorages.forEach { storage ->
            val taken = min(storage.getStoredKharu(), amountLeft)
            amountLeft -= taken
            storage.setStoredKharu(storage.getStoredKharu() - taken)
        }
    }


    override fun getLack(player: EntityPlayer): MagicFuel {
        val kharuStorages = getPlacesToCheckForKharu(player)
        var storedInPlayer = kharuStorages.sumOf { storage -> storage.getStoredKharu() }
        return FuelKharu((this.amount - storedInPlayer).coerceAtLeast(0))
    }

    override fun addTo(player: EntityPlayer): MagicFuel {
        var left = this.amount
        getPlacesToCheckForKharu(player).forEach { storage ->
            val currentAmount = storage.getStoredKharu()
            val maxAmount = storage.getMaxAmount()
            val toPut = min(maxAmount - currentAmount, left).coerceAtLeast(0)
            left -= toPut
            storage.setStoredKharu(currentAmount + toPut)
        }

        return FuelKharu(left)
    }

    override fun isEmpty(): Boolean =
        amount == 0

    override fun getNotEnoughMessages(): ArrayList<NotEnoughFuelNotification> =
        if (amount != 0)
            arrayListOf(NotEnoughFuelNotification("not enough kharu: $amount", texture))
        else arrayListOf()
}

interface IKharuHolder {
    fun getStoredKharu(): Int

    // returns the overlapping amount
    fun setStoredKharu(amount: Int)

    fun getMaxAmount(): Int
}

interface IKharuHolderItem {
    fun getStoredKharu(stack: ItemStack): Int

    fun setStoredKharu(stack: ItemStack, amount: Int)

    fun getMaxKharuAmount(stack: ItemStack): Int
}