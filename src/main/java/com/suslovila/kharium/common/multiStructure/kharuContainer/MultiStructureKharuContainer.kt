package com.suslovila.kharium.common.multiStructure.kharuContainer

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.common.block.ModBlocks
import com.suslovila.kharium.common.multiStructure.TileFilling
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
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.World
import net.minecraftforge.common.util.ForgeDirection
import thaumcraft.api.aspects.Aspect
import thaumcraft.api.aspects.AspectList
import thaumcraft.common.Thaumcraft
import thaumcraft.common.items.wands.ItemWandCasting

object MultiStructureKharuContainer : MultiStructure<KharuContainerAdditionalData, KharuContainerElement>(
    "/assets/${Kharium.MOD_ID}/structures/kharu_container.json",
    availableFacings = arrayListOf(
        ForgeDirection.UP
    ),
    dataClass = KharuContainerElement::class.java,
    rotatable = false,
    validationType = VALIDATION_TYPE.EACH_BLOCK
) {
    val possibleTilesByMeta = listOf<() -> TileEntity>(
        ::TileFilling,
        ::TileKharuContainer,
    )


    override fun <T : TileEntity> render(tile: T, playersOffset: SusVec3, partialTicks: Float) {

    }

    override fun tryConstruct(world: World, clickedPosition: Position, player: EntityPlayer?): Boolean {
        val wand = player?.heldItem?.item as? ItemWandCasting ?: return false
        val hasEnoughVis = wand.consumeAllVisCrafting(
            player.heldItem,
            player,
            AspectList().add(Aspect.FIRE, 1).add(Aspect.EARTH, 1).add(Aspect.ORDER, 1).add(Aspect.AIR, 1)
                .add(Aspect.ENTROPY, 1).add(Aspect.WATER, 1),
            false
        )
        if (hasEnoughVis) {
            val basicSuccess = super.tryConstruct(world, clickedPosition, player)
            if (basicSuccess) {
                wand.consumeAllVisCrafting(
                    player.heldItem,
                    player,
                    AspectList().add(Aspect.FIRE, 70).add(Aspect.EARTH, 70).add(Aspect.ORDER, 70).add(Aspect.AIR, 70)
                        .add(Aspect.ENTROPY, 70).add(Aspect.WATER, 70),
                    true
                )
                return true
            }
        }
        return false
    }

    override fun finaliseConstruction(
        world: World,
        masterPosition: Position,
        facing: ForgeDirection,
        rotationAngle: Int,
        player: EntityPlayer?
    ) {
        super.finaliseConstruction(world, masterPosition, facing, rotationAngle, player)

        world.playSoundEffect(
            masterPosition.x + 0.5,
            masterPosition.y + 0.5,
            masterPosition.z + 0.5,
            "thaumcraft:wand",
            1.0f,
            1.0f
        )
    }
}

class KharuContainerElement(
    x: Int,
    y: Int,
    z: Int,
    storedBlock: String,
    meta: Int,
    val tileEntityByMeta: Int
) : MultiStructureElement<KharuContainerAdditionalData>(
    x, y, z, storedBlock, meta
) {
    override fun putAdditionalData() {
        this.additionalData = KharuContainerAdditionalData()
    }

    override fun construct(
        world: World,
        masterWorldPosition: Position,
        facing: ForgeDirection,
        angle: Int,
        index: Int,
        player: EntityPlayer?
    ) {
        val realPos = masterWorldPosition + getRealOffset(facing, angle)

        world.setBlock(realPos, additionalData.fillingBlock, tileEntityByMeta, 2)
        val tile = (world.getTile(realPos) as? ITileMultiStructureElement) ?: return
        tile.setMasterPos(masterWorldPosition)
        tile.setFacing(facing)
        tile.setRotationAngle(angle)
        tile.setElementIndex(index)

        Thaumcraft.proxy.blockSparkle(world, realPos.x, realPos.y, realPos.z, 16736256, 5)

    }

}

class KharuContainerAdditionalData() : AdditionalData() {
    override val fillingBlock: Block = ModBlocks.KHARU_CONTAINER
}