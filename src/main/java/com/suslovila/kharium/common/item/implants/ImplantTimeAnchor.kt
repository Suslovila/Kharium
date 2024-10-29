package com.suslovila.kharium.common.item.implants

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.api.fuel.FuelComposite
import com.suslovila.kharium.api.fuel.FuelEssentia
import com.suslovila.kharium.api.fuel.FuelKharu
import com.suslovila.kharium.api.implants.*
import com.suslovila.kharium.api.implants.RuneUsingItem.Companion.getRuneAmountOfType
import com.suslovila.kharium.api.rune.RuneType
import com.suslovila.kharium.common.worldSavedData.KharuInfluenceHandler.addKharu
import com.suslovila.kharium.utils.KhariumSusNBTHelper.getOrCreateTag
import com.suslovila.kharium.utils.SusVec3
import com.suslovila.kharium.utils.SusWorldHelper
import com.suslovila.kharium.utils.getPosition
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import thaumcraft.api.aspects.Aspect
import thaumcraft.api.aspects.AspectList

object ImplantTimeAnchor : ItemImplant(ImplantType.OCULAR_SYSTEM) {
    val storedHealthNbt = Kharium.prefixAppender.doAndGet("stored_health")
    val storedPosNbt = Kharium.prefixAppender.doAndGet("stored_pos")

    const val name = "time_anchor"
    override val abilities: ArrayList<Ability> =
        arrayListOf(
            object : AbilityInstant("create_time_mark") {
                override fun onActivated(player: EntityPlayer, index: Int, implant: ItemStack) {
                    implant.getOrCreateTag().setFloat(storedHealthNbt, player.health)
                    val posNbt = NBTTagCompound()
                    player.getPosition().writeTo(posNbt)
                    implant.getOrCreateTag().setTag(storedPosNbt, posNbt)
                }

                override fun getFuelConsumeOnActivation(implant: ItemStack): FuelComposite? =
                    null

                override fun getKharuEmissionOnActivation(implant: ItemStack): Int {
                    return 30 - 3 * getRuneAmountOfType(implant, RuneType.CONTAINMENT)
                }

                override fun getCooldownTotal(implant: ItemStack): Int {
                    return 0
                }

                override fun isActive(implant: ItemStack): Boolean =
                    implant.getOrCreateTag().hasKey(storedHealthNbt)
            },

            object : AbilityInstant("travel_through_time") {
                override fun onEnableButtonClicked(player: EntityPlayer, index: Int, implant: ItemStack) {
                    if (implant.getOrCreateTag().hasKey(storedPosNbt)) {
                        val requiredFuel = getFuelConsumeOnActivation(implant)
                        val lack = requiredFuel.getLack(player)
                        val hasEnough = lack.isEmpty()
                        if (hasEnough) {
                            player.worldObj.playSoundAtEntity(
                                player,
                                Kharium.MOD_ID + ":travel_through_time",
                                1.0f,
                                1.4f + player.worldObj.rand.nextFloat() * 0.2f,
                            )
                            onActivated(player, index, implant)
                            requiredFuel.forceTakeFrom(player)
                            player.addKharu(getKharuEmissionOnActivation(implant))
                            sendToCooldown(implant)
                            notifyClient(player, index, implant)
                            player.worldObj.playSoundAtEntity(
                                player,
                                Kharium.MOD_ID + ":travel_through_time",
                                1.0f,
                                1.4f + player.worldObj.rand.nextFloat() * 0.2f,
                                )
                        } else {
                                lack.notifyPlayerAboutLack()
                        }
                    }
                }

                override fun onActivated(player: EntityPlayer, index: Int, implant: ItemStack) {
                    val tag = implant.getOrCreateTag()
                    SusWorldHelper.teleportEntity(player, SusVec3.readFrom(tag.getCompoundTag(storedPosNbt)))
                    player.health = tag.getFloat(storedHealthNbt)
                }

                override fun getFuelConsumeOnActivation(implant: ItemStack): FuelComposite =
                    FuelComposite(
                        arrayListOf(
                            FuelEssentia(
                                AspectList().add(
                                    Aspect.VOID,
                                    16 - getRuneAmountOfType(implant, RuneType.STABILISATION)
                                ).add(
                                    Aspect.TRAVEL,
                                    16 - getRuneAmountOfType(implant, RuneType.STABILISATION)
                                )
                            ),
                            FuelKharu(100)
                        )
                    )

                override fun getKharuEmissionOnActivation(implant: ItemStack): Int {
                    return 30 - 3 * getRuneAmountOfType(implant, RuneType.CONTAINMENT)
                }

                override fun getCooldownTotal(implant: ItemStack): Int {
                    return 0
                }

                override fun isActive(implant: ItemStack): Boolean =
                    false
            }
        )

    init {
        unlocalizedName = name
        setTextureName(Kharium.MOD_ID + ":diary")
        setMaxStackSize(1)
        creativeTab = Kharium.tab
    }


    fun getKharuEmissionOnSpecial(implant: ItemStack): Int {
        return 100 / (1 + getRuneAmountOfType(implant, RuneType.STABILISATION))
    }


    override fun getMaxRuneAmount(): Int {
        return 9
    }
}