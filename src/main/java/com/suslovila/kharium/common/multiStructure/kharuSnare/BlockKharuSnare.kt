package com.suslovila.kharium.common.multiStructure.kharuSnare

import com.suslovila.kharium.Kharium
import com.suslovila.sus_multi_blocked.api.multiblock.MultiStructure
import com.suslovila.sus_multi_blocked.api.multiblock.block.MultiStructureBlock
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.BlockContainer
import net.minecraft.block.material.Material
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection

class BlockKharuSnare(name: String
) : MultiStructureBlock<KharuSnareAdditionalData, KharuSnareElement>() {
    override val multiStructure: MultiStructure<KharuSnareAdditionalData, KharuSnareElement> by lazy { MultiStructureKharuSnare }

    init {
        this.setHardness(3.0F)
        this.setResistance(10.0F)
        this.setCreativeTab(Kharium.tab)
        GameRegistry.registerBlock(this, name)

    }

    override fun createNewTileEntity(world: World?, meta: Int) =
        MultiStructureKharuSnare.possibleTilesByMeta[meta].invoke()
    override fun isOpaqueCube() = false

    override fun renderAsNormalBlock() = false

    override fun getRenderType() = -1

    override fun isSideSolid(world: IBlockAccess?, x: Int, y: Int, z: Int, side: ForgeDirection?) = false

    override fun getRenderBlockPass() = 1
}
