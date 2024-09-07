package com.suslovila.kharium.common.event

import com.suslovila.kharium.api.fuel.IKharuHolderItem
import com.suslovila.kharium.api.implants.ImplantType
import com.suslovila.kharium.client.KeyHandler
import com.suslovila.kharium.client.gui.GuiImplants
import com.suslovila.kharium.client.gui.GuiMessageNotEnoughFuel
import com.suslovila.kharium.common.item.ItemCrystallizedAntiMatter
import com.suslovila.kharium.common.item.ItemCrystallizedAntiMatter.Companion.globalOwnerName
import com.suslovila.kharium.common.item.ItemPortableAspectContainer
import com.suslovila.kharium.common.item.ModItems
import com.suslovila.kharium.common.multiStructure.kharuContainer.MultiStructureKharuContainer
import com.suslovila.kharium.common.multiStructure.kharuNetHandler.MultiStructureNetHandler
import com.suslovila.kharium.common.multiStructure.kharuSnare.MultiStructureKharuSnare
import com.suslovila.kharium.common.sync.KhariumPacketHandler
import com.suslovila.kharium.common.sync.implant.PacketAllExtendedPlayerSync
import com.suslovila.kharium.common.sync.implant.PacketOneExtendedPlayerSync
import com.suslovila.kharium.common.worldSavedData.CustomWorldData.Companion.customData
import com.suslovila.kharium.extendedData.KhariumPlayerExtendedData
import com.suslovila.kharium.research.KhariumAspect
import com.suslovila.kharium.utils.SusMathHelper
import com.suslovila.kharium.utils.SusNBTHelper.getOrCreateTag
import com.suslovila.kharium.utils.SusUtils
import com.suslovila.kharium.utils.ThaumcraftIntegrator.completeNormalResearch
import com.suslovila.sus_multi_blocked.utils.Position
import cpw.mods.fml.common.eventhandler.EventPriority
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemPickupEvent
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent
import cpw.mods.fml.common.gameevent.TickEvent
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent
import cpw.mods.fml.relauncher.Side
import net.minecraft.client.Minecraft
import net.minecraft.entity.item.EntityItem
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.world.WorldServer
import net.minecraftforge.event.entity.EntityEvent
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing
import net.minecraftforge.event.entity.EntityJoinWorldEvent
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
import javax.vecmath.Matrix4f

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
                    .add(KhariumAspect.HUMILITAS, 100)
            )
            (nodeJar.item as ItemJarNode).setNodeAttributes(nodeJar, NodeType.HUNGRY, NodeModifier.BRIGHT, "")
            event.player.worldObj.spawnEntityInWorld(
                EntityItem(event.player.worldObj, player.posX, player.posY, player.posZ, nodeJar)
            )

            val container = ItemStack(ItemPortableAspectContainer)
            (container.item as IKharuHolderItem).setStoredKharu(container,10_000)
            event.player.worldObj.spawnEntityInWorld(
                EntityItem(event.player.worldObj, player.posX, player.posY, player.posZ,
                    container)
            )
        }
    }

    @SubscribeEvent
    fun addDiaryIntoDrops(event: LivingDropsEvent) {
        if (event.entity is EntityEldritchWarden) event.entity.entityDropItem(ItemStack(ModItems.crystallizedKharu), 1.5f)
    }

    @SubscribeEvent
    fun onPlayerPickedUpCrystallizedKharu(event: ItemPickupEvent) {
        val player = event.player as? EntityPlayerMP ?: return
        if (event.pickedUp.entityItem.item is ItemCrystallizedAntiMatter) {
            event.pickedUp.entityItem.getOrCreateTag().removeTag(globalOwnerName)
            if (!ResearchManager.isResearchComplete(event.player.commandSenderName, "CRYSTALLIZED_KHARU")) {
                completeNormalResearch("CRYSTALLIZED_KHARU", player, event.player.worldObj)
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    fun constructMultiStructures(event: PlayerInteractEvent) {
        with(event) {
            if (action == PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) {
                MultiStructureKharuContainer.tryConstruct(world, Position(x, y, z), entityPlayer)
                MultiStructureKharuSnare.tryConstruct(world, Position(x, y, z), entityPlayer)
                MultiStructureNetHandler.tryConstruct(world, Position(x, y, z), entityPlayer)

            }
        }
    }

    @SubscribeEvent
    fun expandKharuHotbeds(event: WorldTickEvent) {
        if (event.side.isServer && event.phase == TickEvent.Phase.END && !event.world.isRemote && event.side == Side.SERVER && event.world.provider.dimensionId == 0) {
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
    fun onEntityConstructing(event: EntityConstructing) {
        if (event.entity is EntityPlayer && KhariumPlayerExtendedData.get(event.entity as EntityPlayer) == null) {
            KhariumPlayerExtendedData.register(event.entity as EntityPlayer)
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    fun onPlayerCloneEvent(event: Clone) {
        val oldPlayerNBT = NBTTagCompound()
        val oldPlayerEx = KhariumPlayerExtendedData.get(event.original)
        oldPlayerEx?.saveNBTData(oldPlayerNBT)

        val newPlayerEx = KhariumPlayerExtendedData.get(event.entityPlayer)
        newPlayerEx?.loadNBTData(oldPlayerNBT)

    }

    @SubscribeEvent
    fun onEntityJoinWorld(event: EntityJoinWorldEvent) {
        val entity = event.entity ?: return
        if (!entity.worldObj.isRemote && entity is EntityPlayerMP) {
            val server = entity.worldObj as? WorldServer ?: return
            val playersData =
                server.playerEntities.map { KhariumPlayerExtendedData.get(it as EntityPlayer) }.filterNotNull()
            KhariumPacketHandler.INSTANCE.sendTo(PacketAllExtendedPlayerSync(playersData), entity)
            KhariumPlayerExtendedData.get(entity).let {
                KhariumPacketHandler.INSTANCE.sendToAll(PacketOneExtendedPlayerSync(it, entity))
            }
        }
        if (entity.worldObj.isRemote && entity == Minecraft.getMinecraft().thePlayer) {
            KhariumPlayerExtendedData.get(Minecraft.getMinecraft().thePlayer)?.let {
                KeyHandler.setNextImplant(it, Array(ImplantType.slotAmount) { index -> index }.toMutableList())
            }
        }

//        @SubscribeEvent
//        fun onEntityJoinWorld(event: EntityJoinWorldEvent) {
//            if (entity.worldObj.isRemote && entity == Minecraft.getMinecraft().thePlayer) {
//                KhariumPlayerExtendedData.get(Minecraft.getMinecraft().thePlayer)?.let {
//                    KeyHandler.setNextImplant(it, Array(ImplantType.slotAmount) { index -> index }.toMutableList())
//                }
//            }
//        }
    }
}