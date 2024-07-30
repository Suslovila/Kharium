package com.suslovila.kharium.client.gui

import com.suslovila.kharium.common.block.container.ContainerKharuSnare
import com.suslovila.kharium.common.container.ContainerImplantHolder
import com.suslovila.kharium.common.container.ContainerKharuContainer
import com.suslovila.kharium.common.container.ContainerRuneInstaller
import com.suslovila.kharium.common.item.ItemKharuNetConfigurator
import com.suslovila.kharium.common.item.ItemPortableAspectContainer
import com.suslovila.kharium.common.multiStructure.kharuContainer.TileKharuContainer
import com.suslovila.kharium.common.multiStructure.kharuSnare.TileKharuSnare
import com.suslovila.kharium.common.multiStructure.runeInstaller.TileRuneInstaller
import com.suslovila.sus_multi_blocked.client.gui.GuiMultiBlockFormer
import com.suslovila.sus_multi_blocked.common.item.ItemMultiBlockFormer
import com.suslovila.sus_multi_blocked.utils.Position
import cpw.mods.fml.common.network.IGuiHandler
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World

class GuiHandler : IGuiHandler {
    override fun getServerGuiElement(id: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Any? {
        if (!world.blockExists(x, y, z)) {
            return null
        }
        val tile = world.getTileEntity(x, y, z)
        when (id) {
            GuiIds.KHARU_SNARE -> {
                return if (tile !is TileKharuSnare) {
                    null
                } else ContainerKharuSnare(player.inventory, tile)
            }

            GuiIds.IMPLANT_INSTALLER -> {
                return ContainerImplantHolder(player)
            }

            GuiIds.RUNE_INSTALLER -> {
                return if (tile !is TileRuneInstaller) {
                    null
                } else ContainerRuneInstaller(tile, player)
            }

            GuiIds.KHARU_CONTAINER -> {
                return if (tile !is TileKharuContainer) {
                    null
                } else ContainerKharuContainer(tile, player)
            }
        }
        return null
    }

    override fun getClientGuiElement(id: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Any? {
        if (!world.blockExists(x, y, z)) {
            return null
        }
        val tile = world.getTileEntity(x, y, z)
        when (id) {
            GuiIds.KHARU_SNARE -> {
                return if (tile !is TileKharuSnare) {
                    null
                } else GuiKharuSnare(player.inventory, tile)
            }

            GuiIds.IMPLANT_INSTALLER -> {
                return GuiImplantInstaller(player)
            }

            GuiIds.RUNE_INSTALLER -> {
                return if (tile !is TileRuneInstaller)
                    null
                else GuiRuneInstaller(tile, player)
            }

            GuiIds.KHARU_CONTAINER -> {
                return if (tile !is TileKharuContainer) {
                    null
                } else GuiKharuContainer(tile, player)
            }
            GuiIds.ITEM_KHARU_NET_HANDLER -> {
                return if (player.heldItem.item !is ItemKharuNetConfigurator) {
                    null
                } else GuiItemKharuNetConfigurator(player.heldItem)
            }
            GuiIds.ITEM_ASPECT_HOLDER -> {
                return if (player.heldItem.item !is ItemPortableAspectContainer) {
                    null
                } else GuiItemPortableContainer(player.heldItem)
            }
        }

        return null
    }
}
