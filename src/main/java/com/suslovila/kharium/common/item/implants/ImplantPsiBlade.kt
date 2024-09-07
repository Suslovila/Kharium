package com.suslovila.kharium.common.item.implants

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.api.damage.DamageSourceEnergy
import com.suslovila.kharium.api.fuel.FuelComposite
import com.suslovila.kharium.api.fuel.FuelEssentia
import com.suslovila.kharium.api.fuel.FuelKharu
import com.suslovila.kharium.api.fuel.MagicFuel
import com.suslovila.kharium.api.implants.*
import com.suslovila.kharium.api.implants.RuneUsingItem.Companion.getRuneAmountOfType
import com.suslovila.kharium.api.rune.RuneType
import com.suslovila.kharium.client.render.item.ItemSpaceDividerRenderer
import com.suslovila.kharium.common.worldSavedData.KharuInfluenceHandler.addKharu
import com.suslovila.kharium.research.KhariumAspect
import com.suslovila.kharium.utils.SusGraphicHelper
import net.minecraft.entity.EntityLiving
import net.minecraft.item.ItemStack
import net.minecraftforge.client.event.RenderHandEvent
import net.minecraftforge.event.entity.player.AttackEntityEvent
import org.lwjgl.opengl.GL11.*
import thaumcraft.api.aspects.AspectList
import kotlin.random.Random

object ImplantPsiBlade : ItemImplant(ImplantType.HEART) {
    const val name = "psi_blade"
    override val abilities: ArrayList<Ability> =
        arrayListOf(
            object : AbilityPassive("dissolate") {
                override fun getFuelConsumeOnActivation(implant: ItemStack): FuelComposite? = null

                override fun getFuelConsumePerSecond(implant: ItemStack): FuelComposite? = null
                override fun getKharuEmissionOnActivation(implant: ItemStack): Int {
                    return 0
                }

                override fun getKharuEmissionPerSecond(implant: ItemStack): Int {
                    return 0
                }

                override fun getCooldownTotal(implant: ItemStack): Int {
                    return 40 / (1 + getRuneAmountOfType(implant, RuneType.OVERCLOCK))
                }

                override fun onPlayerAttackEntityEvent(event: AttackEntityEvent, index: Int, implant: ItemStack) {
                    println("bbb")
                    // both sides for bolt render
                    val basicDamage = 4
                    if (!isOnCooldown(implant) && isActive(implant)) {
                        val requiredFuel =
                            FuelComposite(
                            arrayListOf(
                                FuelKharu(
                                    50
                                ),
                                FuelEssentia(
                                    AspectList().add(KhariumAspect.HUMILITAS, 5)
                                )
                            )
                        )
                        val hasEnoughFuel = requiredFuel.tryTakeFuelFromPlayer(event.entityPlayer)
                        if(!hasEnoughFuel) return
                        (event.target as? EntityLiving)?.run {
                            this.attackEntityFrom(
                                DamageSourceEnergy,
                                (basicDamage + 3 * getRuneAmountOfType(implant, RuneType.EXPANSION)).toFloat()
                            )

                            repeat(2) {
                                val lightningOffset = 0.5
                                Kharium.proxy.nodeAntiBolt(
                                    worldObj,
                                    x = (posX + Random.nextDouble(
                                        -lightningOffset,
                                        lightningOffset
                                    )).toFloat(),
                                    y = (posY).toFloat(),
                                    z = (posZ + Random.nextDouble(
                                        -lightningOffset,
                                        lightningOffset
                                    )).toFloat(),
                                    x2 = (posX + Random.nextDouble(
                                        -lightningOffset,
                                        lightningOffset
                                    )).toFloat(),
                                    y2 = (posY + this.height).toFloat(),
                                    z2 = (posZ + Random.nextDouble(
                                        -lightningOffset,
                                        lightningOffset
                                    )).toFloat(),
                                )
                            }
                            worldObj.playSound(
                                this.posX,
                                this.posY + 0.5,
                                this.posZ,
                                Kharium.MOD_ID + ":psi_blade",
                                1.0f,
                                1.4f + worldObj.rand.nextFloat() * 0.2f,
                                false
                            )


                            sendToCooldown(implant)
                            event.entityPlayer.addKharu(getKharuEmissionOnSpecial(implant))
                        }
                    }
                }

                override fun onRenderHandEvent(event: RenderHandEvent, index: Int, implant: ItemStack) {
                    if (!isActive(implant)) return
                    glPushMatrix()

                    glTranslated(0.5, 0.0, 0.0)
                    SusGraphicHelper.drawGuideArrows()
                    glPopMatrix()
                }
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