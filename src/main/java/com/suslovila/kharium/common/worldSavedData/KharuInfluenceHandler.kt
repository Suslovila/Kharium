package com.suslovila.kharium.common.worldSavedData

import com.emoniph.witchery.infusion.Infusion
import com.suslovila.kharium.Kharium
import com.suslovila.kharium.common.worldSavedData.AxisWrapper.writeTo
import com.suslovila.kharium.common.worldSavedData.CustomWorldData.Companion.customData
import com.suslovila.kharium.common.worldSavedData.KharuInfluenceHandler.AMOUNT_NBT
import com.suslovila.kharium.integration.hooks.Hooks
import com.suslovila.kharium.utils.*
import com.suslovila.kharium.utils.SusNBTHelper.getOrCreateTag
import com.suslovila.kharium.utils.config.Config
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import io.netty.buffer.ByteBuf
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.init.Items
import net.minecraft.item.ItemPotion
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.AxisAlignedBB
import net.minecraft.util.DamageSource
import net.minecraft.world.World
import net.minecraftforge.event.entity.living.LivingEvent
import thaumcraft.api.aspects.Aspect
import thaumcraft.api.aspects.AspectList
import thaumcraft.common.items.wands.ItemWandCasting
import vazkii.botania.api.mana.IManaItem
import kotlin.math.pow

object KharuInfluenceHandler {

    val AMOUNT_NBT = Kharium.prefixAppender.doAndGet("amount")
    val ZONE_NBT = Kharium.prefixAppender.doAndGet("axisAlignedAABB")

    val basicAspects =
        arrayListOf<Aspect>(Aspect.FIRE, Aspect.EARTH, Aspect.ORDER, Aspect.AIR, Aspect.ENTROPY, Aspect.WATER)

    fun getKharuLevel(world: World, position: SusVec3): Int =
        getKharuHotbed(world, position)?.amount ?: 0


    fun getKharuHotbed(world: World, position: SusVec3): KharuHotbed? {
        val possibleKharuHotbeds = world.customData.kharuHotbeds.filter { it.zone.isVecInside(position) }
        if (possibleKharuHotbeds.size <= 1) {
            return possibleKharuHotbeds.firstOrNull()
        } else {
            val united = possibleKharuHotbeds.reduce { accumulatedZone, kharuHotbed ->
                KharuHotbed(
                    accumulatedZone.zone.union(kharuHotbed.zone),
                    accumulatedZone.amount + kharuHotbed.amount
                )
            }
            possibleKharuHotbeds.forEach { kharuHotbed -> world.customData.removeKharuHotbed(kharuHotbed, world) }
            world.customData.addKharuHotbed(united, world)
            return united
        }
    }

    @SubscribeEvent
    fun handleKharuInfluence(event: LivingEvent.LivingUpdateEvent) {
        with(event) {
            val possiblePlayer = event.entity
            val world = possiblePlayer.worldObj
            if (world.isRemote || possiblePlayer !is EntityLivingBase) return

            // handling radiation adding
            if ((possiblePlayer.ticksExisted % 20) == 0) {
                if (possiblePlayer is EntityPlayerMP) {
                    addKharuFromSpace(world, possiblePlayer)
                }
                Hooks.hooks.forEach { it.tickKharuInfluence(possiblePlayer) }
            }
        }
    }

    private fun addKharuFromSpace(world: World, player: EntityPlayerMP) {
        val hotbed = getKharuHotbed(world, player.getPosition()) ?: return
        player.addKharu(getKharuPerCheckWithReduce(world, hotbed))
        run anchor@{
            player.inventory.mainInventory.forEach { itemStack ->
                if (itemStack?.maxStackSize != 1) return@forEach
                itemStack.addKharu(getKharuPerCheckWithReduce(world, hotbed))
            }
        }
    }

    fun getKharuPerCheckWithReduce(world: World, hotbed: KharuHotbed): Int {
        var taken = hotbed.amountPerTime
        if (taken > hotbed.amount) {
            taken = hotbed.amount
            world.customData.removeKharuHotbed(hotbed, world)
        }
        hotbed.amount -= taken
        world.customData.markDirty()

        return taken
    }



    fun ItemStack.getKharuAmount(): Int {
        val tag = getOrCreateTag()
        if (tag.hasKey(AMOUNT_NBT)) return tag.getInteger(AMOUNT_NBT)
        return 0
    }

    fun Entity.getKharuAmount(): Int {
        val tag = entityData
        if (tag.hasKey(AMOUNT_NBT)) return tag.getInteger(AMOUNT_NBT)
        return 0
    }

    fun Entity.addKharu(amount: Int) {
        entityData.setInteger(AMOUNT_NBT, getKharuAmount() + amount)
    }

    fun ItemStack.addKharu(amount: Int) {
        getOrCreateTag().setInteger(AMOUNT_NBT, getKharuAmount() + amount)
    }

    fun ItemStack.getKharuLinearAmountPercent(): Double {
        val percent = getKharuAmount().toDouble() / Config.kharuItemDestructionBorder
        // in order to make non-linear influence we can use pow
        return percent.coerceAtMost(1.0)
    }

    fun Entity.getKharuLinearAmountPercent(): Double {
        val percent = getKharuAmount().toDouble() / Config.kharuItemDestructionBorder
        // in order to make non-linear influence we can use pow
        return percent.coerceAtMost(1.0)
    }

    fun ItemStack.getKharuAmountPercentInfluence(degree: Double): Double {
        val percent = getKharuAmount().toDouble() / Config.kharuItemDestructionBorder
        // in order to make non-linear influence we can use pow
        return percent.coerceAtMost(1.0).pow(degree)
    }

    fun Entity.getKharuAmountPercentInfluence(degree: Double): Double {
        val percent = getKharuAmount().toDouble() / Config.kharuDestroyingBorder
        // in order to make non-linear influence we can use pow
        return percent.coerceAtMost(1.0).pow(degree)
    }


    fun TileEntity.getKharuAmountPercent(): Double {
        return getKharuLevel(world, this.getPosDouble())
            .toDouble() / Config.kharuDestroyingBorder
    }
}


class KharuHotbed(
    var zone: AxisAlignedBB,
    var amount: Int
) {
    fun writeTo(rootTag: NBTTagCompound) {
        zone.writeTo(rootTag)
        rootTag.setInteger(AMOUNT_NBT, amount)
    }

    fun writeTo(byteBuf: ByteBuf) {
        zone.writeTo(byteBuf)
        byteBuf.writeInt(amount)
    }

    val amountPerTime: Int
        get() {
            return Config.maxRadiationPerTime * (1 + (amount / Config.kharuDestroyingBorder))
        }

    companion object {
        fun readFrom(rootTag: NBTTagCompound): KharuHotbed? {
            val zone = AxisWrapper.readFrom(rootTag) ?: return null
            if (!rootTag.hasKey(AMOUNT_NBT)) return null
            return KharuHotbed(
                zone,
                rootTag.getInteger(AMOUNT_NBT)
            )
        }

        fun readFrom(buf: ByteBuf): KharuHotbed {
            val zone = AxisWrapper.readFrom(buf)
            val amount = buf.readInt()
            return KharuHotbed(
                zone,
                amount
            )
        }
    }
}

fun AxisAlignedBB.isVecInside(vec: SusVec3): Boolean {
    return vec.x in minX..maxX && vec.y >= minY && vec.y <= maxY && vec.z >= minZ && vec.z <= maxZ
}

object PrimordialDamage : DamageSource("primordial") {
    init {
        this.setMagicDamage()
        this.setDamageIsAbsolute()
        this.setDamageBypassesArmor()
    }
}
