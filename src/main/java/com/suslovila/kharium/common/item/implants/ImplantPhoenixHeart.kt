package com.suslovila.kharium.common.item.implants

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.api.fuel.FuelComposite
import com.suslovila.kharium.api.fuel.FuelKharu
import com.suslovila.kharium.api.fuel.MagicFuel
import com.suslovila.kharium.api.implants.*
import com.suslovila.kharium.api.implants.RuneUsingItem.Companion.getRuneAmountOfType
import com.suslovila.kharium.api.rune.RuneType
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraftforge.event.entity.living.LivingDeathEvent

object ImplantPhoenixHeart : ItemImplant(ImplantType.HEART) {
    const val name = "phoenix_heart"
    override val abilities: ArrayList<Ability> =
        arrayListOf(
            object : AbilityPassive("resurrection") {
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

                override fun onPlayerDeathEvent(event: LivingDeathEvent, index: Int, stack: ItemStack) {
                    (event.entityLiving as? EntityPlayer)?.let { player ->
                        val world = event.entityLiving.worldObj
                        if (isOnCooldown(stack) || !isActive(stack) || event.isCanceled) return
                        val amountLeft = FuelComposite(
                            arrayListOf(
                                FuelKharu(
                                    10
                                )
                            )
                        ).getLack(player)
                        val hasEnough = amountLeft.isEmpty()
                        if (hasEnough) {
                            event.isCanceled = true
                            player.health = 4f
                            world.playSoundAtEntity(
                                player,
                                Kharium.MOD_ID + ":ability_phoenix_heart",
                                1.5f,
                                1.4f + world.rand.nextFloat() * 0.2f,
                            )

                            sendToCooldown(stack)
                            notifyClient(player, index, stack)
                        }
                        else {
                            amountLeft.notifyPlayerAboutLack()

                        }
                    }
                }
            }
        )

    init {
        unlocalizedName = name
        setTextureName(Kharium.MOD_ID + ":phoenix_heart")
        creativeTab = Kharium.tab
    }


    override fun getMaxRuneAmount(): Int {
        return 9
    }
}