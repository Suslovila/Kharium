package com.suslovila.common.block.multiblocks

import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import com.suslovila.ExampleMod.tab
import com.suslovila.common.block.multiblocks.tile.TileSynthesizer

class Synthesizer(val name: String) extends Block(Material.air) {
	this.setHardness(3.0F)
	this.setResistance(10.0F)
	this.setBlockName(name)
	this.setCreativeTab(tab)

	GameRegistry.registerBlock(this, name)

	override def createTileEntity(world: World, metadata: Int): TileEntity = new TileSynthesizer()
}
