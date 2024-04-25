package com.suslovila.kharium.common.multiStructure.kharuSnare

import com.suslovila.kharium.common.block.ModBlocks
import com.suslovila.sus_multi_blocked.api.multiblock.AdditionalData
import com.suslovila.sus_multi_blocked.api.multiblock.MultiStructure
import com.suslovila.sus_multi_blocked.api.multiblock.MultiStructureElement
import com.suslovila.sus_multi_blocked.api.multiblock.VALIDATION_TYPE
import com.suslovila.sus_multi_blocked.api.multiblock.block.ITileMultiStructureElement
import com.suslovila.sus_multi_blocked.utils.Position
import com.suslovila.sus_multi_blocked.utils.SusVec3
import com.suslovila.sus_multi_blocked.utils.getTile
import com.suslovila.sus_multi_blocked.utils.setBlock
import net.minecraft.block.Block
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection
import thaumcraft.common.Thaumcraft
import java.nio.file.Paths

object MultiStructureKharuSnare : MultiStructure<KharuSnareAdditionalData, KharuSnareElement>(
    Paths.get(".").toAbsolutePath().toString() + "/sus_multi_blocked/kharu_snare.json",
    availableFacings = arrayListOf(
        ForgeDirection.UP
    ),
    dataClass = KharuSnareElement::class.java,
    rotatable = false,
    validationType = VALIDATION_TYPE.EACH_BLOCK
) {
    val possibleTilesByMeta = listOf<() -> TileEntity>(
        ::TileKharuSnareFilling,
        ::TileKharuSnare,
        ::TileKharuSnareFilling
    )

    override fun onCreated(
        world: World,
        masterWorldPosition: Position,
        facing: ForgeDirection,
        angle: Int,
        player: EntityPlayer?
    ) {
        super.onCreated(world, masterWorldPosition, facing, angle, player)
    }


    override fun <T : TileEntity> render(tile: T, playersOffset: SusVec3, partialTicks: Float) {

    }

    override fun tryConstruct(world: World, clickedPosition: Position, player: EntityPlayer?): Boolean {
        val basicSuccess = return super.tryConstruct(world, clickedPosition, player)
        
    }
}

class KharuSnareElement(
    x: Int,
    y: Int,
    z: Int,
    storedBlock: String,
    meta: Int,
    val tileEntityByMeta: Int
) : MultiStructureElement<KharuSnareAdditionalData>(
    x, y, z, storedBlock, meta
) {
    override fun putAdditionalData() {
        this.additionalData = KharuSnareAdditionalData()
    }

    override fun construct(
        world: World,
        masterWorldPosition: Position,
        facing: ForgeDirection,
        angle: Int,
        player: EntityPlayer?
    ) {
        val realPos = masterWorldPosition + getRealOffset(facing, angle)

        if(!world.isRemote) {
            world.setBlock(realPos, additionalData.fillingBlock, tileEntityByMeta, 2)
            val tile = (world.getTile(realPos) as? ITileMultiStructureElement) ?: return
            tile.setMasterPos(masterWorldPosition)
            tile.setFacing(facing)
            tile.setRotationAngle(angle)
        }
        Thaumcraft.proxy.blockSparkle(world, realPos.x, realPos.y, realPos.z, 16736256, 5)

    }

}

class KharuSnareAdditionalData() : AdditionalData() {
    override val fillingBlock: Block = ModBlocks.KHARU_SNARE
}