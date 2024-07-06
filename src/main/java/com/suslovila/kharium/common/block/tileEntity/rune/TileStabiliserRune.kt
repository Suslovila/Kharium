package com.suslovila.kharium.common.block.tileEntity.rune

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.api.rune.RuneType
import com.suslovila.kharium.common.block.tileEntity.TileAntiNode
import com.suslovila.kharium.common.multiStructure.kharuSnare.TileKharuSnare
import com.suslovila.kharium.research.KhariumAspect
import net.minecraft.util.ResourceLocation
import thaumcraft.api.aspects.AspectList

class TileStabiliserRune(
) : TileRune() {
    override val runeType = RuneType.STABILISATION
    override val waveColor : Int = KhariumAspect.HUMILITAS.color
    override val disabled: ResourceLocation =
        ResourceLocation(Kharium.MOD_ID, "textures/blocks/runes/stabilisation/cub_core.png")

    override val cubeCoreTexture: ResourceLocation = ResourceLocation(Kharium.MOD_ID, "textures/blocks/runes/stabilisation/cube_core.png")
    override val cubeGlowingTexture: ResourceLocation = ResourceLocation(Kharium.MOD_ID, "textures/blocks/runes/stabilisation/cube_glowing.png")

    override val straightCoreTexture: ResourceLocation = ResourceLocation(Kharium.MOD_ID, "textures/blocks/runes/stabilisation/straight_core.png")
    override val straightGlowingTexture: ResourceLocation = ResourceLocation(Kharium.MOD_ID, "textures/blocks/runes/stabilisation/straight_glowing.png")

    override val cornerCoreTexture: ResourceLocation = ResourceLocation(Kharium.MOD_ID, "textures/blocks/runes/stabilisation/corner_core.png")
    override val cornerGlowingTexture: ResourceLocation = ResourceLocation(Kharium.MOD_ID, "textures/blocks/runes/stabilisation/corner_glowing.png")

}