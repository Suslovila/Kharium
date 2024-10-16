package com.suslovila.kharium.client.gui

import com.suslovila.kharium.common.container.ContainerImplantHolder
import com.suslovila.kharium.common.container.ContainerKharuContainer
import com.suslovila.kharium.common.container.ContainerRuneInstaller
import com.suslovila.kharium.common.item.ItemConfigurator
import com.suslovila.kharium.common.item.ItemPortableAspectContainer
import com.suslovila.kharium.common.multiStructure.kharuContainer.TileKharuContainer
import com.suslovila.kharium.common.multiStructure.kharuSnare.TileKharuSnare
import com.suslovila.kharium.common.multiStructure.runeInstaller.TileRuneInstaller
import com.suslovila.kharium.common.multiStructure.synthesizer.TileAdvancedSynthesizerCore
import cpw.mods.fml.common.network.IGuiHandler
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World

class GuiHandler : IGuiHandler {
    override fun getServerGuiElement(id: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Any? {
        if (!world.blockExists(x, y, z)) {
            return null
        }
        val tile = world.getTileEntity(x, y, z)
        val gui = KhariumGui.values()[id]

        when (gui) {

            KhariumGui.IMPLANT_INSTALLER -> {
                return ContainerImplantHolder(player)
            }

            KhariumGui.RUNE_INSTALLER -> {
                return if (tile !is TileRuneInstaller) {
                    null
                } else ContainerRuneInstaller(tile, player)
            }

            KhariumGui.KHARU_CONTAINER -> {
                return if (tile !is TileKharuContainer) {
                    null
                } else ContainerKharuContainer(tile, player)
            }
            else -> {}
        }
        return null
    }

    override fun getClientGuiElement(id: Int, player: EntityPlayer, world: World, x: Int, y: Int, z: Int): Any? {
        if (!world.blockExists(x, y, z)) {
            return null
        }
        val tile = world.getTileEntity(x, y, z)
        val gui = KhariumGui.values()[id]
        return when (gui) {
            KhariumGui.SYNTHESIZER -> {
                if (tile !is TileKharuSnare) {
                    null
                } else GuiKharuSnare(player.inventory, tile)
            }

            KhariumGui.IMPLANT_INSTALLER -> {
                GuiImplantInstaller(player)
            }

            KhariumGui.RUNE_INSTALLER -> {
                if (tile !is TileRuneInstaller)
                    null
                else GuiRuneInstaller(tile, player)
            }

            KhariumGui.KHARU_CONTAINER -> {
                if (tile !is TileKharuContainer) {
                    null
                } else GuiKharuContainer(tile, player)
            }
            KhariumGui.ITEM_KHARU_NET_HANDLER -> {
                if (player.heldItem.item !is ItemConfigurator) {
                    null
                } else GuiItemKharuNetConfigurator(player.heldItem)
            }
            KhariumGui.ITEM_ASPECT_HOLDER -> {
                if (player.heldItem.item !is ItemPortableAspectContainer) {
                    null
                } else GuiItemPortableContainer(player.heldItem)
            }
            KhariumGui.KHARU_SNARE -> {
                if (tile !is TileKharuSnare) {
                    null
                } else GuiKharuSnare(player.inventory, tile)
            }
            KhariumGui.KHARU_NET_HANDLER -> {

            }

            KhariumGui.ADVANCED_SYNTHESIZER -> {
                if (tile !is TileAdvancedSynthesizerCore) {
                    null
                } else GuiAdvancedSynthesizer(tile)
            }

            else -> {
                null
            }
        }

        return null
    }
}
