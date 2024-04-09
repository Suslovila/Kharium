package com.suslovila.kharium.common.block.container

import com.suslovila.kharium.common.block.tileEntity.tileAntiNodeController.TileKharuExtractor
import net.minecraft.block.BlockContainer
import net.minecraft.block.material.Material
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World

//By meaning there is no such block in the world; But the whole anti-node stabilization construction is made of these blocks
class BlockKharuExtractor : BlockContainer(Material.iron){
    override fun createTileEntity(world : World?, metadata: Int) : TileEntity {
        return TileKharuExtractor()
    }

    override fun createNewTileEntity(world: World?, p_149915_2_: Int): TileEntity? {
        return null;
    }
}