package com.suslovila.kharium.common.block.multiblocks

import com.suslovila.kharium.Kharium.MOD_ID
import com.suslovila.kharium.Kharium.tab
import com.suslovila.kharium.common.block.multiblocks.tile.TileSynthesizer
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.BlockContainer
import net.minecraft.block.material.Material
import net.minecraft.world.World

class BlockSynthesizer(name: String) : BlockContainer(Material.iron) {
    init {
        this.setHardness(3.0F)
        this.setResistance(10.0F)
        this.unlocalizedName = name
        this.setCreativeTab(tab)
        this.textureName = "$MOD_ID:synthesizer"

        GameRegistry.registerBlock(this, name)
    }

    override fun createNewTileEntity(p0: World?, p1: Int) = TileSynthesizer()
}