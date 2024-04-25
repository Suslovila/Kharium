package com.suslovila.kharium.common.block

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.common.block.tileEntity.TileRestrainedGlass
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.BlockContainer
import net.minecraft.block.material.Material
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection

class BlockRestrainedGlass(name: String) : BlockContainer(Material.glass) {
    init {
        this.setHardness(3.0F)
        this.setResistance(10.0F)
        this.setCreativeTab(Kharium.tab)
        GameRegistry.registerBlock(this, name)

    }

    override fun createNewTileEntity(p_149915_1_: World?, p_149915_2_: Int) = TileRestrainedGlass()
    override fun isOpaqueCube() = false

    override fun renderAsNormalBlock() = false

    override fun getRenderType() = -1

    override fun isSideSolid(world: IBlockAccess?, x: Int, y: Int, z: Int, side: ForgeDirection?) = false

    override fun getRenderBlockPass() = 1
}
