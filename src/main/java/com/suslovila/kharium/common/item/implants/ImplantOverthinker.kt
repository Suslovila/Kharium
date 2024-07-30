package com.suslovila.kharium.common.item.implants

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.api.fuel.FuelComposite
import com.suslovila.kharium.api.implants.Ability
import com.suslovila.kharium.api.implants.AbilityPassive
import com.suslovila.kharium.api.implants.ImplantType
import com.suslovila.kharium.api.implants.ItemImplant
import com.suslovila.kharium.api.implants.RuneUsingItem.Companion.getRuneAmountOfType
import com.suslovila.kharium.api.rune.RuneType
import com.suslovila.kharium.extendedData.KhariumPlayerExtendedData.Companion.get
import com.suslovila.kharium.research.AntiCraftResearchRegistry
import com.suslovila.kharium.utils.SusMathHelper.tryWithPercentChance
import com.suslovila.kharium.utils.ThaumcraftIntegrator
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import thaumcraft.api.ThaumcraftApi
import thaumcraft.api.research.ResearchCategories
import thaumcraft.api.research.ResearchItem
import thaumcraft.common.Thaumcraft
import thaumcraft.common.config.Config
import thaumcraft.common.lib.research.ResearchManager

// temporary disabled due to problems with thaumcraft researches

object ImplantOverthinker : ItemImplant(ImplantType.OCULAR_SYSTEM) {

    const val name = "overthinker"
    override val abilities: ArrayList<Ability> =
        arrayListOf(
            object : AbilityPassive("overthink") {
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
                    return 400
                }

                override fun getFuelConsumePerSecond(implant: ItemStack): FuelComposite? {
                    return null
                }

                override fun getKharuEmissionPerSecond(implant: ItemStack): Int {
                    return 0
                }
            }
        )

    init {
        unlocalizedName = name
        setTextureName(Kharium.MOD_ID + ":diary")
        setMaxStackSize(1)
        creativeTab = Kharium.tab
    }

    fun getChance(implant: ItemStack) =
        1.0

    @JvmStatic
    fun overThink(player: EntityPlayerMP, index: Int, implant: ItemStack, research: ResearchItem) {
        val overthinkAbility = abilities[0]
        val hasPlayerFuel = overthinkAbility.getFuelConsumeOnActivation(implant)?.tryTakeFuelFromPlayer(player) ?: true
        if(!hasPlayerFuel) return

        val success = tryWithPercentChance(getChance(implant))
        if (success && overthinkAbility.isActive(implant) && !overthinkAbility.isOnCooldown(implant) && !research.isAutoUnlock) {
            var hasAlreadyOpenedOne = false
            AntiCraftResearchRegistry.researchItemChildren[research]?.forEach { child ->
                if (hasAlreadyOpenedOne || ResearchManager.isResearchComplete(
                        player.commandSenderName,
                        child.key
                    )
                ) return@forEach
                val hasPlayerAllOtherParentsCompleted = child.parents?.all { parent ->
                    ResearchManager.isResearchComplete(
                        player.commandSenderName,
                        parent
                    )
                } ?: true

                if (hasPlayerAllOtherParentsCompleted) {
                    completeResearchWithoutChildOpening(player, child.key)
                    hasAlreadyOpenedOne = true
                    overthinkAbility.sendToCooldown(implant)
                    overthinkAbility.notifyClient(player, index, implant)
                }
            }
        }
    }

    fun completeResearchWithoutChildOpening(player: EntityPlayer, key: String?) {
        if (ResearchManager.completeResearchUnsaved(player.commandSenderName, key)) {
            val warp = ThaumcraftApi.getWarp(key)
            if (warp > 0 && !Config.wuss && !player.worldObj.isRemote) {
                if (warp > 1) {
                    val halfWarp = warp / 2
                    if (warp - halfWarp > 0) {
                        Thaumcraft.addWarpToPlayer(player, warp - halfWarp, false)
                    }
                    if (halfWarp > 0) {
                        Thaumcraft.addStickyWarpToPlayer(player, halfWarp)
                    }
                } else {
                    Thaumcraft.addWarpToPlayer(player, warp, false)
                }
            }

            ResearchManager.scheduleSave(player)
        }
    }

    override fun getMaxRuneAmount(): Int {
        return 9
    }
}