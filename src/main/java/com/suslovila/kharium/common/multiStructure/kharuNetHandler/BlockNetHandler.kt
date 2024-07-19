package com.suslovila.kharium.common.multiStructure.kharuNetHandler

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.client.gui.GuiIds
import com.suslovila.sus_multi_blocked.api.multiblock.MultiStructure
import com.suslovila.sus_multi_blocked.api.multiblock.block.MultiStructureBlock
import com.suslovila.sus_multi_blocked.api.multiblock.block.TileDefaultMultiStructureElement
import com.suslovila.sus_multi_blocked.utils.getTile
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection

class BlockNetHandler(name: String
) : MultiStructureBlock<NetHandlerAdditionalDta, NetHandlerElement>() {
    override val multiStructure: MultiStructure<NetHandlerAdditionalDta, NetHandlerElement> by lazy { MultiStructureNetHandler }

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
        if (world.isRemote || player == null) return true
        val block = world.getBlock(x, y, z)
        val tile = world.getTileEntity(x, y, z) as? TileDefaultMultiStructureElement ?: return false
        val netHandler = (world.getTile(tile.structureMasterPos))

        if(netHandler !is TileNetHandler) return false
        if (!player.isSneaking) {
                player.openGui(Kharium.instance, GuiIds.KHARU_NET_HANDLER, world, netHandler.xCoord, netHandler.yCoord, netHandler.zCoord)
                return true
        }
        return false
    }
    override fun createNewTileEntity(world: World?, meta: Int) =
        MultiStructureNetHandler.possibleTilesByMeta[meta].invoke()
    override fun isOpaqueCube() = false

    override fun renderAsNormalBlock() = false

    override fun getRenderType() = -1

    override fun isSideSolid(world: IBlockAccess?, x: Int, y: Int, z: Int, side: ForgeDirection?) = false

    override fun getRenderBlockPass() = 1


}
