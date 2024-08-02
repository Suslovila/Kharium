package com.suslovila.kharium.common.item.implants

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.api.fuel.FuelComposite
import com.suslovila.kharium.api.fuel.FuelEssentia
import com.suslovila.kharium.api.fuel.FuelKharu
import com.suslovila.kharium.api.implants.*
import com.suslovila.kharium.api.implants.RuneUsingItem.Companion.getRuneAmountOfType
import com.suslovila.kharium.api.rune.RuneType
import com.suslovila.kharium.client.implantInfluence.Illusion
import com.suslovila.kharium.common.sync.KhariumPacketHandler
import com.suslovila.kharium.common.sync.PacketAddIllusion
import com.suslovila.kharium.common.worldSavedData.KharuInfluenceHandler.addKharu
import com.suslovila.kharium.utils.SusNBTHelper.getOrCreateTag
import com.suslovila.kharium.utils.SusNBTHelper.setUUID
import com.suslovila.kharium.utils.SusVec3
import com.suslovila.kharium.utils.SusWorldHelper
import com.suslovila.kharium.utils.getPosition
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.MovingObjectPosition
import org.lwjgl.opengl.GL11.*
import thaumcraft.api.aspects.Aspect
import thaumcraft.api.aspects.AspectList

object ImplantMindBreaker : ItemImplant(ImplantType.OCULAR_SYSTEM) {

    const val name = "mind_breaker"
    override val abilities: ArrayList<Ability> =
        arrayListOf(
            object : AbilityInstant("illusions") {
                override fun onActivated(player: EntityPlayer, index: Int, implant: ItemStack) {
                    println("entering button")
                    if (!isOnCooldown(implant) && !player.worldObj.isRemote) {
                        val hitResult = SusWorldHelper.raytraceEntities(player.worldObj, player, 20.0)
                        val hitEntity = hitResult?.entityHit
                        if (hitResult?.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY ||
                            hitEntity == null
                        ) {
                            return
                        }
                        println("Entity not null")
                        if (hitEntity is EntityPlayerMP) {
                            println("entity is player. Sending packet")
                            KhariumPacketHandler.INSTANCE.sendTo(
                                PacketAddIllusion(
                                    Illusion(player.persistentID, duration = 2000, illusionAmount = 3)
                                ),
                                hitEntity
                            )
                            sendToCooldown(implant)
                            notifyClient(player, index, implant)
                        }
                    }
                }

                override fun getFuelConsumeOnActivation(implant: ItemStack): FuelComposite? =
                    null

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


    fun getKharuEmissionOnSpecial(implant: ItemStack): Int {
        return 100 / (1 + getRuneAmountOfType(implant, RuneType.STABILISATION))
    }


    override fun getMaxRuneAmount(): Int {
        return 9
    }
}