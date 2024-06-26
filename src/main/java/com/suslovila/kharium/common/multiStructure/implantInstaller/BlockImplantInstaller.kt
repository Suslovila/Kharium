package com.suslovila.kharium.common.multiStructure.implantInstaller

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.client.gui.GuiIds
import com.suslovila.sus_multi_blocked.api.multiblock.MultiStructure
import com.suslovila.sus_multi_blocked.api.multiblock.block.MultiStructureBlock
import com.suslovila.sus_multi_blocked.api.multiblock.block.TileDefaultMultiStructureElement
import com.suslovila.sus_multi_blocked.utils.getTile
import cpw.mods.fml.common.registry.GameRegistry
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection

class BlockImplantInstaller(name: String
) : Block(Material.iron) {

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

        if (!player.isSneaking) {
                player.openGui(Kharium.instance, GuiIds.IMPLANT_INSTALLER, world, x, y, z)
                return true
        }
        return false
    }

    override fun isOpaqueCube() = false

    override fun renderAsNormalBlock() = false

    override fun getRenderType() = -1

    override fun isSideSolid(world: IBlockAccess?, x: Int, y: Int, z: Int, side: ForgeDirection?) = false

    override fun getRenderBlockPass() = 1


}
