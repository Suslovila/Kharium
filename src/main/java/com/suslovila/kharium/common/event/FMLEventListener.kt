package com.suslovila.kharium.common.event

import com.emoniph.witchery.common.ExtendedPlayer
import com.emoniph.witchery.common.ExtendedVillager
import com.suslovila.kharium.common.item.ItemCrystallizedAntiMatter
import com.suslovila.kharium.common.item.ItemCrystallizedAntiMatter.Companion.globalOwnerName
import com.suslovila.kharium.common.item.ModItems
import com.suslovila.kharium.common.multiStructure.kharuSnare.MultiStructureKharuSnare
import com.suslovila.kharium.common.worldSavedData.CustomWorldData.Companion.customData
import com.suslovila.kharium.extendedData.KhariumPlayerExtendedData
import com.suslovila.kharium.research.ACAspect
import com.suslovila.kharium.utils.SusMathHelper
import com.suslovila.kharium.utils.SusNBTHelper.getOrCreateTag
import com.suslovila.kharium.utils.SusUtils
import com.suslovila.kharium.utils.ThaumcraftIntegrator.completeNormalResearch
import com.suslovila.sus_multi_blocked.utils.Position
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemPickupEvent
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent
import cpw.mods.fml.common.gameevent.TickEvent
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.passive.EntityVillager
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing
import net.minecraftforge.event.entity.living.LivingDropsEvent
import net.minecraftforge.event.entity.player.PlayerEvent.Clone
import net.minecraftforge.event.entity.player.PlayerInteractEvent
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
            (nodeJar.item as ItemJarNode).setAspects(
                nodeJar,
                AspectList().add(Aspect.HUNGER, 100).add(Aspect.WATER, 100).add(Aspect.ELDRITCH, 100)
                    .add(ACAspect.HUMILITAS, 100)
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

    @SubscribeEvent
    fun construct(event: PlayerInteractEvent) {
        with(event) {
            if (action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
                MultiStructureKharuSnare.tryConstruct(world, Position(x, y, z), entityPlayer as? EntityPlayerMP)
            }
        }
    }

    @SubscribeEvent
    fun expandKharuHotbeds(event: WorldTickEvent) {
        if (event.side.isServer && event.phase == TickEvent.Phase.END) {
            with(event) {
                world.customData.kharuHotbeds.forEach {
                    if (SusUtils.random.nextInt(200) == 1) {
                        it.zone = it.zone.expand(
                            SusMathHelper.nextDouble(-0.01, 0.01),
                            SusMathHelper.nextDouble(-0.01, 0.01),
                            SusMathHelper.nextDouble(-0.01, 0.01)
                        )
                    }
                }
                world.customData.markDirty()
            }
        }
    }

    @SubscribeEvent
    fun onPlayerCloneEvent(event: Clone) {
        val oldPlayerNBT = NBTTagCompound()
        val oldPlayerEx = KhariumPlayerExtendedData.get(event.original)
        oldPlayerEx?.saveNBTData(oldPlayerNBT)

        val newPlayerEx = KhariumPlayerExtendedData.get(event.entityPlayer)
        newPlayerEx?.loadNBTData(oldPlayerNBT)
    }

    @SubscribeEvent
    fun onEntityConstructing(event: EntityConstructing) {
        if (event.entity is EntityPlayer && KhariumPlayerExtendedData.get(event.entity as EntityPlayer) == null) {
            KhariumPlayerExtendedData.register(event.entity as EntityPlayer)
        }
    }
}