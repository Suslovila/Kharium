package com.suslovila.kharium.common.item.implants

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.api.fuel.FuelComposite
import com.suslovila.kharium.api.fuel.FuelEssentia
import com.suslovila.kharium.api.fuel.FuelKharu
import com.suslovila.kharium.api.implants.*
import com.suslovila.kharium.api.implants.RuneUsingItem.Companion.getRuneAmountOfType
import com.suslovila.kharium.api.rune.RuneType
import com.suslovila.kharium.client.clientProcess.ClientProcessHandler
import com.suslovila.kharium.client.clientProcess.processes.ProcessPortal
import com.suslovila.kharium.common.worldSavedData.KharuInfluenceHandler.addKharu
import com.suslovila.kharium.utils.SusNBTHelper.getOrCreateTag
import com.suslovila.kharium.utils.SusVec3
import com.suslovila.kharium.utils.SusWorldHelper
import com.suslovila.kharium.utils.getPosition
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import org.lwjgl.opengl.GL11.*
import thaumcraft.api.aspects.Aspect
import thaumcraft.api.aspects.AspectList

object ImplantSpaceDiver : ItemImplant(ImplantType.OCULAR_SYSTEM) {

    const val name = "space_diver"
    override val abilities: ArrayList<Ability> =
        arrayListOf(
            object : AbilityInstant("swap_places") {
                override fun onEnableButtonClicked(player: EntityPlayer, index: Int, implant: ItemStack) {
                    if(isOnCooldown(implant)) return
                    val requiredFuel = getFuelConsumeOnActivation(implant)
                    val foundEntity = SusWorldHelper.raytraceEntities(player.worldObj, player, getMaxReachDistance(implant).toDouble())
                    val lack = requiredFuel.getLack(player)
                    val hasEnough = lack.isEmpty()
                    if (hasEnough && foundEntity?.entityHit != null) {
                        player.worldObj.playSoundAtEntity(
                            player,
                            Kharium.MOD_ID + ":swap_places",
                            1.0f,
                            1.4f + player.worldObj.rand.nextFloat() * 0.2f,
                        )
                        val position = player.getPosition()
                        val entityPos = foundEntity.entityHit.getPosition()
                        SusWorldHelper.teleportEntity(player, foundEntity.entityHit.getPosition())
                        SusWorldHelper.teleportEntity(foundEntity.entityHit, position)
                        requiredFuel.forceTakeFrom(player)
                        player.addKharu(getKharuEmissionOnActivation(implant))
                        sendToCooldown(implant)
                        player.worldObj.playSoundAtEntity(
                            player,
                            Kharium.MOD_ID + ":swap_places",
                            1.0f,
                            1.4f + player.worldObj.rand.nextFloat() * 0.2f,
                        )

                        if(player.worldObj.isRemote) {
                            ClientProcessHandler.processes.add(
                                ProcessPortal(position.x, position.y, position.z, 2000)
                            )

                            ClientProcessHandler.processes.add(
                                ProcessPortal(entityPos.x, entityPos.y, entityPos.z, 2000)
                            )
                        }
                    } else {
                        lack.notifyPlayerAboutLack()
                    }
                }

                override fun onActivated(player: EntityPlayer, index: Int, implant: ItemStack) {
                }

                override fun getFuelConsumeOnActivation(implant: ItemStack): FuelComposite =
                    FuelComposite(
                        arrayListOf(
//                            FuelEssentia(
//                                AspectList().add(
//                                    Aspect.VOID,
//                                    16 - getRuneAmountOfType(implant, RuneType.STABILISATION)
//                                ).add(
//                                    Aspect.TRAVEL,
//                                    16 - getRuneAmountOfType(implant, RuneType.STABILISATION)
//                                )
//                            ),
//                            FuelKharu(100)
                        )
                    )

                override fun getKharuEmissionOnActivation(implant: ItemStack): Int {
                    return 30 - 3 * getRuneAmountOfType(implant, RuneType.CONTAINMENT)
                }

                override fun getCooldownTotal(implant: ItemStack): Int {
                    return 30
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

    fun getMaxReachDistance(stack: ItemStack) =
        10 * (1 + getRuneAmountOfType(stack, RuneType.EXPANSION))

    override fun getMaxRuneAmount(): Int {
        return 9
    }
}