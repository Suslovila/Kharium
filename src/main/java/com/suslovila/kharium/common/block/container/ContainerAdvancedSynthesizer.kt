package com.suslovila.kharium.common.block.container

import com.suslovila.kharium.common.multiStructure.kharuSnare.TileKharuSnare
import com.suslovila.kharium.common.multiStructure.synthesizer.advancedSynthesizer.TileAdvancedSynthesizerCore
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.inventory.Slot

class ContainerAdvancedSynthesizer(val inventoryPlayer: InventoryPlayer, val core: TileAdvancedSynthesizerCore) : DefaultContainer() {

    init {

    }

    override fun canInteractWith(player: EntityPlayer): Boolean {
        return true
    }
}
