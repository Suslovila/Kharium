package com.suslovila.kharium.common.block

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.common.block.tileEntity.rune.TileRune
import com.suslovila.kharium.common.block.tileEntity.rune.TileStabiliserRune
import com.suslovila.sus_multi_blocked.api.multiblock.block.ITileMultiStructureElement
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.Block
import net.minecraft.block.BlockContainer
import net.minecraft.block.material.Material
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection

class BlockRune(name : String) : BlockContainer(Material.iron) {
    val runes = arrayListOf<()->TileRune>(
        ::TileStabiliserRune
    )
    init {
        this.setHardness(3.0F)
        this.setResistance(10.0F)
        this.setCreativeTab(Kharium.tab)
        GameRegistry.registerBlock(this, name)

    }
    override fun onNeighborBlockChange(world: World?, x: Int, y: Int, z: Int, neighborBlock: Block?) {
        if (world == null) return
        val foundTile = world.getTileEntity(x, y, z)
        if (foundTile is TileRune) {

        }
    }
    override fun createNewTileEntity(world: World?, meta: Int) = runes[meta].invoke()
    override fun isOpaqueCube() = false

    override fun renderAsNormalBlock() = false

    override fun getRenderType() = -1

    override fun isSideSolid(world: IBlockAccess?, x: Int, y: Int, z: Int, side: ForgeDirection?) = false

    override fun getRenderBlockPass() = 1
}