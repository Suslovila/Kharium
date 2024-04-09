package com.suslovila.kharium.common.event

import com.suslovila.kharium.common.item.ItemCrystallizedAntiMatter
import com.suslovila.kharium.common.item.ItemCrystallizedAntiMatter.Companion.globalOwnerName
import com.suslovila.kharium.common.item.ModItems
import com.suslovila.kharium.research.ACAspect
import com.suslovila.kharium.utils.SusNBTHelper.getOrCreateTag
import com.suslovila.kharium.utils.ThaumcraftIntegrator.completeNormalResearch
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemPickupEvent
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraftforge.event.entity.living.LivingDropsEvent
import thaumcraft.api.aspects.Aspect
import thaumcraft.api.aspects.AspectList
import thaumcraft.api.nodes.NodeModifier
import thaumcraft.api.nodes.NodeType
import thaumcraft.common.Thaumcraft
import thaumcraft.common.blocks.ItemJarNode
import thaumcraft.common.config.ConfigItems
import thaumcraft.common.entities.monster.boss.EntityEldritchWarden
import thaumcraft.common.lib.network.PacketHandler
import thaumcraft.common.lib.network.playerdata.PacketSyncAspects
import thaumcraft.common.lib.research.ResearchManager

object FMLEventListener {
//event class
    @SubscribeEvent
    fun giveAllAspects(event: PlayerLoggedInEvent) {
        if (!event.player.worldObj.isRemote) {
            val player = event.player
            for (aspect in Aspect.aspects.values) {
                Thaumcraft.proxy.playerKnowledge.addAspectPool(player.commandSenderName, aspect, 10.toShort())
            }
            ResearchManager.scheduleSave(player)
            PacketHandler.INSTANCE.sendTo(PacketSyncAspects(player), player as EntityPlayerMP)
            val nodeJar = ItemStack(ConfigItems.itemJarNode)
            (nodeJar.item as ItemJarNode).setAspects(nodeJar,
                AspectList().
                add(Aspect.HUNGER, 100).
                add(Aspect.WATER, 100).
                add(Aspect.ELDRITCH, 100).
                add(ACAspect.HUMILITAS, 100)
            )
            (nodeJar.item as ItemJarNode).setNodeAttributes(nodeJar, NodeType.HUNGRY, NodeModifier.BRIGHT, "")
            event.player.worldObj.spawnEntityInWorld(
                EntityItem(event.player.worldObj, player.posX, player.posY, player.posZ, nodeJar)
            )
        }
    }

    @SubscribeEvent
    fun addDiaryIntoDrops(event: LivingDropsEvent) {
        if (event.entity is EntityEldritchWarden) event.entity.entityDropItem(ItemStack(ModItems.diary), 1.5f)
    }

    @SubscribeEvent
    fun onPlayerPickedUpCrystallizedKharu(event: ItemPickupEvent) {
        if (event.pickedUp.entityItem.item is ItemCrystallizedAntiMatter) {
            event.pickedUp.entityItem.getOrCreateTag().removeTag(globalOwnerName)
            if (!ResearchManager.isResearchComplete(event.player.commandSenderName, "CRYSTALLIZED_KHARU")) {
                completeNormalResearch("CRYSTALLIZED_KHARU", event.player, event.player.worldObj)
            }
        }
    }
}