package com.suslovila.kharium.utils

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import scala.actors.threadpool.AtomicInteger
import thaumcraft.api.ThaumcraftApiHelper
import thaumcraft.api.aspects.Aspect
import thaumcraft.api.aspects.AspectList
import thaumcraft.api.aspects.IAspectContainer
import thaumcraft.api.research.ResearchCategories
import thaumcraft.api.research.ResearchItem
import thaumcraft.common.Thaumcraft
import thaumcraft.common.lib.network.PacketHandler
import thaumcraft.common.lib.network.playerdata.PacketResearchComplete
import kotlin.math.min

object ThaumcraftIntegrator {

    fun completeNormalResearch(researchName: String, player: EntityPlayerMP, world: World) {

        PacketHandler.INSTANCE.sendTo(PacketResearchComplete(researchName), player)
        Thaumcraft.proxy.getResearchManager().completeResearch(player, researchName)
        world.playSoundAtEntity(player, "thaumcraft:learn", 0.75f, 1.0f)

    }

    fun completeResearchSilently(researchName: String?, player: EntityPlayer?) =
        Thaumcraft.proxy.getResearchManager().completeResearch(player, researchName)

    fun getAspectList(stack: ItemStack?) = ThaumcraftApiHelper.getObjectAspects(stack) ?: AspectList()

    // Aspect class does not override equals function, but each aspect has only one instance, so link comparisation will succeed
    val componentsToAspect: Map<Pair<Aspect, Aspect>, Aspect> by lazy {
        Aspect.aspects.values.toList().associate {
            val firstComponent = it.components[0]!!
            val secondComponent = it.components[1]!!
            Pair(firstComponent, secondComponent) to it!!
        }
    }
    val compositionAmountToAspect: Map<Aspect, Int> by lazy {
        Aspect.aspects.values.associate {
            val compositionActionAmount = AtomicInteger(0)
            getAspectCompositionActionsRecursively(compositionActionAmount, it.components)
            it to compositionActionAmount.get()
        }
    }

    private fun getAspectCompositionActionsRecursively(accumulatedAmount: AtomicInteger, components: Array<Aspect>) {
        accumulatedAmount.addAndGet(1)
        components.forEach { getAspectCompositionActionsRecursively(accumulatedAmount, it.components) }
    }

    fun tryTakeFromContainers(
        aspectHolders: Collection<IAspectContainer>,
        aspects: AspectList,
        simulate: Boolean
    ): Boolean {
        val requiredAspectsCopy = aspects.copy()
        aspectHolders.forEach { aspectHolder ->
            aspectHolder.aspects.aspects.forEach { aspect, amount ->
                val requiredAmount = requiredAspectsCopy.getAmount(aspect)
                val toTake = min(requiredAmount, amount)
                requiredAspectsCopy.remove(aspect, toTake)

                if (!simulate) {
                    aspectHolder.takeFromContainer(aspect, toTake)
                }
            }
        }

        val allAspectsZero = requiredAspectsCopy.aspects.filter { pair -> pair.value != 0 }.isEmpty()
        return allAspectsZero
    }
}