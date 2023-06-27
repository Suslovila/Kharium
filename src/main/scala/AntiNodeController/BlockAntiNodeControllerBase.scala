package AntiNodeController

import net.minecraft.block.BlockContainer
import net.minecraft.block.material.Material
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World

class BlockAntiNodeControllerBase() extends BlockContainer(Material.air){
  override def createNewTileEntity(p_149915_1_ : World, p_149915_2_ : Int): TileEntity = null
}
