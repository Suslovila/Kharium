package com.suslovila.kharium.common.multiStructure.kharuContainer

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.client.gui.KhariumGui
import com.suslovila.sus_multi_blocked.api.multiblock.MultiStructure
import com.suslovila.sus_multi_blocked.api.multiblock.block.MultiStructureBlock
import com.suslovila.sus_multi_blocked.api.multiblock.block.TileDefaultMultiStructureElement
import com.suslovila.sus_multi_blocked.utils.getTile
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection

class BlockKharuContainer(name: String
) : MultiStructureBlock<KharuContainerAdditionalData, KharuContainerElement>() {
    override val multiStructure: MultiStructure<KharuContainerAdditionalData, KharuContainerElement> by lazy { MultiStructureKharuContainer }

    init {
        this.setHardness(3.0F)
        this.setResistance(10.0F)
        this.setCreativeTab(Kharium.tab)
        GameRegistry.registerBlock(this, name)

    }

    override fun onBlockActivated(
        world: World,
        x: Int,
        y: Int,
        z: Int,
        player: EntityPlayer?,
        side: Int,
        clickX: Float,
        clickY: Float,
        clickZ: Float
    ): Boolean {
        if (player == null) return false
        val tile = world.getTileEntity(x, y, z) as? TileDefaultMultiStructureElement ?: return false
        val tileContainer = (world.getTile(tile.structureMasterPos))

        if(tileContainer !is TileKharuContainer) return false
        if (!player.isSneaking) {
                player.openGui(Kharium.instance, KhariumGui.KHARU_CONTAINER.ordinal, world, tileContainer.xCoord, tileContainer.yCoord, tileContainer.zCoord)
                return false
        }
        return false
    }
    override fun createNewTileEntity(world: World?, meta: Int) =
        MultiStructureKharuContainer.possibleTilesByMeta[meta].invoke()
    override fun isOpaqueCube() = false

    override fun renderAsNormalBlock() = false

    override fun getRenderType() = -1

    override fun isSideSolid(world: IBlockAccess?, x: Int, y: Int, z: Int, side: ForgeDirection?) = false

    override fun getRenderBlockPass() = 1


}
