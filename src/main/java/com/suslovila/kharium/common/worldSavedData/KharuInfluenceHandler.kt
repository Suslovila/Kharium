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
    fun handlePlayerThaumcraftDirectInfluence(event: LivingEvent.LivingUpdateEvent) {
        with(event) {
            val world = event.entity.worldObj
            if (world.isRemote || entity !is EntityLivingBase) return

            // handling radiation adding
            if ((entity.ticksExisted % 20) == 0) {
                Hooks.hooks.forEach {it.tickKharuInfluence(entity as? EntityLivingBase)}
            }
        }
    }

    private fun addKharuFromSpace(world: World, player: EntityPlayerMP) {
        val hotbed = getKharuHotbed(world, player.getPosition()) ?: return
        player.addKharu(getKharuPerCheckWithReduce(world, hotbed))
        run anchor@{
            player.inventory.mainInventory.forEach { itemStack ->
                itemStack ?: return@forEach
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

        return taken
    }

    private fun influenceWitchery(world: World, player: EntityPlayerMP) {
        val infusion = Infusion.Registry.instance().get(Infusion.getInfusionID(player))
        if (infusion != Infusion.DEFUSED) {

        }
    }


    private fun vanillaItemKharuInfluence(player: EntityPlayerMP) {
        player.inventory.mainInventory.forEachIndexed { index, itemStack ->
            itemStack ?: return@forEachIndexed
            val kharuDestroyPercent = itemStack.getKharuAmountPercentInfluence(8.0)
            // enchantments
            itemStack.enchantmentTagList?.tagList?.removeAll {
                SusMathHelper.tryWithPercentChance(kharuDestroyPercent)
            }
            if (itemStack.enchantmentTagList?.tagList?.isEmpty() == true) {
                itemStack.stackTagCompound.removeTag("ench")
            }
            // potions
            val successForPotion = SusMathHelper.tryWithPercentChance(kharuDestroyPercent)

            if (itemStack.item is ItemPotion && successForPotion) {
                player.inventory.mainInventory[index] = ItemStack(Items.glass_bottle)
            }
        }
        // potion effects
        player.activePotionEffects.forEach { effect ->
//            val successForEffect = SusMathHelper.tryWithPercentChance()

        }
    }

    private fun thaumcraftItemKharuInfluence(player: EntityPlayerMP) {
        player.inventory.mainInventory.forEach { itemStack ->
            itemStack ?: return@forEach
            val wand = itemStack.item as? ItemWandCasting ?: return@forEach
            if (SusMathHelper.tryWithPercentChance(
                    chance = itemStack.getKharuAmountPercentInfluence(2.0)
                )
            ) {
                wand.consumeAllVisCrafting(
                    itemStack,
                    player,
                    AspectList().add(
                        basicAspects[SusUtils.random.nextInt(basicAspects.size)],
                        SusUtils.random.nextInt(10)
                    ),
                    true
                )
            }
        }
    }

    private fun botaniaItemKharuInfluence(player: EntityPlayerMP) {
        player.inventory.mainInventory.forEach { itemStack ->
            val manaItem = itemStack?.item as? IManaItem ?: return@forEach
            if (SusMathHelper.tryWithPercentChance(
                    chance = itemStack.getKharuAmountPercentInfluence(2.0)
                )
            ) {
                var toDecrease = (itemStack.getKharuLinearAmountPercent() * Config.maxManaReduce).toInt()
                val manaPrevious = manaItem.getMana(itemStack)
                if (toDecrease > manaPrevious) toDecrease = manaPrevious
                manaItem.addMana(
                    itemStack,
                    -toDecrease
                )
            }
        }
    }


    fun ItemStack.getKharuAmount(): Int {
        val tag = getOrCreateTag()
        if (tag.hasKey(AMOUNT_NBT)) return tag.getInteger(AMOUNT_NBT)
        tag.setInteger(AMOUNT_NBT, 0)
        return 0
    }

    fun Entity.getKharuAmount(): Int {
        val tag = entityData
        if (tag.hasKey(AMOUNT_NBT)) return tag.getInteger(AMOUNT_NBT)
        tag.setInteger(AMOUNT_NBT, 0)
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

    fun ItemStack.getKharuAmountPercentInfluence(degree: Double): Double {
        val percent = getKharuAmount().toDouble() / Config.kharuItemDestructionBorder
        // in order to make non-linear influence we can use pow
        return percent.coerceAtMost(1.0).pow(degree)
    }

    fun TileEntity.getKharuAmountPercent(): Double {
        return getKharuLevel(world, this.getPosDouble())
            .toDouble() / Config.kharuDestroyingBorder
    }


    private fun clearInfusion(player: EntityPlayer) {
        if (!player.worldObj.isRemote) {
            val nbt = Infusion.getNBT(player)
            nbt.removeTag("witcheryInfusionCharges")
            Infusion.syncPlayer(player.worldObj, player)
        }
    }

    fun Infusion.consumeEnergy(world: World, player: EntityPlayer, cost: Int, playFailSound: Boolean): Boolean {
        if (player.capabilities.isCreativeMode) {
            return true
        }
        val charges = Infusion.getCurrentEnergy(player)
        if (charges - cost < 0) {
            world.playSoundAtEntity(
                player as Entity,
                "note.snare",
                0.5f,
                0.4f / (Math.random().toFloat() * 0.4f + 0.8f)
            )
            clearInfusion(player)
            return false
        }
        Infusion.setCurrentEnergy(player, charges - cost)
        return true
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
