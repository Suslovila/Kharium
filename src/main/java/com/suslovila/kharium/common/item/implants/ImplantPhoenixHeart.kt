package com.suslovila.kharium.common.item.implants

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.api.damage.DamageSourceEnergy
import com.suslovila.kharium.api.fuel.FuelComposite
import com.suslovila.kharium.api.fuel.FuelEssentia
import com.suslovila.kharium.api.fuel.MagicFuel
import com.suslovila.kharium.api.implants.*
import com.suslovila.kharium.api.implants.RuneUsingItem.Companion.getRuneAmountOfType
import com.suslovila.kharium.api.rune.RuneType
import com.suslovila.kharium.client.render.item.ItemSpaceDividerRenderer
import com.suslovila.kharium.common.worldSavedData.KharuInfluenceHandler.addKharu
import net.minecraft.entity.EntityLiving
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.RenderHandEvent
import net.minecraftforge.client.event.RenderPlayerEvent
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.event.entity.player.AttackEntityEvent
import org.lwjgl.opengl.GL11.*
import kotlin.random.Random

object ImplantPhoenixHeart : ItemImplant(ImplantType.HEART) {
    const val name = "item_implant_phoenix_heart"
    override val abilities: ArrayList<Ability> =
        arrayListOf(
            object : AbilityPassive("resurrection") {
                override fun getFuelConsumeOnActivation(implant: ItemStack): MagicFuel? = null

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

                override fun onPlayerDeathEvent(event: LivingDeathEvent, index: Int, stack: ItemStack) {
                    (event.entityLiving as? EntityPlayer)?.let { player ->
                        val world = event.entityLiving.worldObj
                        val hasEnough = FuelComposite(
                            arrayListOf(

                            )
                        ).takeFrom(player).isEmpty()
                        if (hasEnough) {
                            world.playSound(
                                player.posX,
                                player.posY + 0.5,
                                player.posZ,
                                Kharium.MOD_ID + ":ability_phoenix_heart",
                                1.0f,
                                1.4f + world.rand.nextFloat() * 0.2f,
                                false
                            )
                        }
                    }
                }
            }
        )

    init {
        unlocalizedName = name
        setTextureName(Kharium.MOD_ID + ":diary")
        creativeTab = Kharium.tab
    }


    override fun getMaxRuneAmount(): Int {
        return 9
    }
}