package com.suslovila.kharium.common.item.implants

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.api.fuel.FuelComposite
import com.suslovila.kharium.api.fuel.MagicFuel
import com.suslovila.kharium.api.implants.*
import com.suslovila.kharium.api.implants.RuneUsingItem.Companion.getRuneAmountOfType
import com.suslovila.kharium.api.rune.RuneType
import com.suslovila.kharium.client.render.item.ItemSpaceDividerRenderer
import com.suslovila.kharium.common.worldSavedData.KharuInfluenceHandler.addKharu
import com.suslovila.kharium.utils.SusGraphicHelper
import net.minecraft.client.Minecraft
import net.minecraft.entity.EntityLiving
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraftforge.client.event.RenderHandEvent
import net.minecraftforge.client.event.RenderPlayerEvent
import net.minecraftforge.event.entity.player.AttackEntityEvent
import org.lwjgl.opengl.GL11.*
import thaumcraft.api.aspects.Aspect
import thaumcraft.api.aspects.AspectList
import thaumcraft.client.lib.UtilsFX

class ImplantPsiBlade() : ItemImplant(ImplantType.HAND) {
    override val abilities: ArrayList<AbilityPassive> =
        arrayListOf(
            object : AbilityPassive("dissolate") {
                override fun getFuelConsumeOnActivation(implant: ItemStack): MagicFuel? = null

                override fun getFuelConsumePerSecond(implant: ItemStack): FuelComposite? = null
                override fun getKharuEmissionOnActivation(implant: ItemStack): Int {
                    return 0
                }

                override fun getKharuEmissionPerSecond(implant: ItemStack): Int {
                    return 0
                }

                override fun getCooldownTotal(implant: ItemStack): Int {
                    return 40
                }

                override fun onPlayerAttackEntityEvent(event: AttackEntityEvent, index: Int, implant: ItemStack) {
                    if (event.entityPlayer.worldObj.isRemote) return
                    val basicDamage = 4
                    if (!isOnCooldown(implant) && isEnabled(implant)) {
//                       val damage = basicDamage * (1 + getRuneAmountOfType(implant, RuneType.EXPANSION).toFloat() / 2)

                        (event.target as? EntityLiving)?.run {
                            worldObj.createExplosion(
                                null,
                                this.posX + 0.5,
                                this.posY + 0.5,
                                this.posZ + 0.5,
                                1.0f,
                                false
                            )

                            sendToCooldown(implant)
                            event.entityPlayer.addKharu(getKharuEmissionOnSpecial(implant))
                            notifyClient(event.entityPlayer, index, implant)
                        }
                    }
                }

                override fun onRenderPlayerEvent(event: RenderPlayerEvent.Post, index: Int, implant: ItemStack) {
                    if (!isEnabled(implant)) return
                    glPushMatrix()
                    val viewingPlayer: EntityPlayer = Minecraft.getMinecraft().thePlayer
                    if (viewingPlayer !== event.entityPlayer) {
                        val translationXLT: Double = event.entityPlayer.prevPosX - viewingPlayer.prevPosX
                        val translationYLT: Double = event.entityPlayer.prevPosY - viewingPlayer.prevPosY
                        val translationZLT: Double = event.entityPlayer.prevPosZ - viewingPlayer.prevPosZ
                        val translationX: Double =
                            translationXLT + (event.entityPlayer.posX - viewingPlayer.posX - translationXLT) * event.partialRenderTick
                        val translationY: Double =
                            translationYLT + (event.entityPlayer.posY - viewingPlayer.posY - translationYLT) * event.partialRenderTick
                        val translationZ: Double =
                            translationZLT + (event.entityPlayer.posZ - viewingPlayer.posZ - translationZLT) * event.partialRenderTick
                        glTranslated(translationX, translationY + 1.1, translationZ)
                    } else {
                        glTranslated(0.0, -0.5, 0.0)
                    }
                    glEnable(GL_CULL_FACE)
                    glDisable(GL_BLEND)
                    glDisable(GL_ALPHA_TEST)
                    UtilsFX.bindTexture(SusGraphicHelper.whiteBlank)
                    glColor4f(1f, 1f, 1f, 1f)

                    glScaled(1.0, 1.5, 1.0)

//                   KharuTickHandler.model.renderAll()

                    glPopMatrix()
                }

                override fun onRenderHandEvent(event: RenderHandEvent, index: Int, implant: ItemStack) {
                    if (!isEnabled(implant)) return
                    glPushMatrix()

                    glTranslated(0.5, 0.0, 0.0)
                    ItemSpaceDividerRenderer.runeModel.renderAll()

                    glPopMatrix()
                }
            }
        )

    init {
        unlocalizedName = "blade"
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