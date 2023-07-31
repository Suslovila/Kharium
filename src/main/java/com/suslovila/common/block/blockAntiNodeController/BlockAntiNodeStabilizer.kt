package com.suslovila.common.block.blockAntiNodeController

import com.suslovila.ExampleMod
import com.suslovila.common.block.tileEntity.tileAntiNodeController.TileAntiNodeControllerBase
import com.suslovila.common.block.tileEntity.tileAntiNodeController.TileAntiNodeStabilizer
import net.minecraft.block.BlockContainer
import net.minecraft.block.material.Material
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection

class BlockAntiNodeStabilizer() : BlockContainer(Material.iron) {
    init {
        this.setHardness(3.0F)
        this.setResistance(10.0F)
        this.setCreativeTab(ExampleMod.tab)

    }
    override fun createNewTileEntity(p_149915_1_: World?, p_149915_2_: Int) = TileAntiNodeStabilizer()
    override fun isOpaqueCube() = false

    override fun renderAsNormalBlock() = false

    override fun getRenderType() =  -1


    override fun isSideSolid(world: IBlockAccess?, x: Int, y: Int, z: Int, side: ForgeDirection?) = false

    override fun getRenderBlockPass() = 1
}