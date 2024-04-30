package com.suslovila.kharium.common.block.tileEntity.rune

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.common.block.tileEntity.TileAntiNode
import com.suslovila.kharium.common.multiStructure.kharuSnare.TileKharuSnare
import com.suslovila.kharium.research.ACAspect
import com.suslovila.kharium.utils.SusGraphicHelper
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.AdvancedModelLoader
import net.minecraftforge.client.model.IModelCustom

class TileStabiliserRune(
) : TileRune() {

    override val disabled: ResourceLocation =
        ResourceLocation(Kharium.MOD_ID, "textures/blocks/runes/stabilisation/disabled.png")

    override val straightSleepCoreTexture: ResourceLocation = SusGraphicHelper.whiteBlank

    //        ResourceLocation(Kharium.MOD_ID, "textures/blocks/runes/stabilisation/stabilisation_rune_straight_sleep_core.png")
    override val straightSleepGlowingTexture: ResourceLocation = SusGraphicHelper.whiteBlank

    //    ResourceLocation(Kharium.MOD_ID, "textures/blocks/runes/stabilisation/stabilisation_rune_straight_sleep_glowing.png")
    override val straightActiveCoreTexture: ResourceLocation = SusGraphicHelper.whiteBlank

    //    ResourceLocation(Kharium.MOD_ID, "textures/blocks/runes/stabilisation/stabilisation_rune_straight_active_core.png")
    override val straightActiveGlowingTexture: ResourceLocation = SusGraphicHelper.whiteBlank
//    ResourceLocation(Kharium.MOD_ID, "textures/blocks/runes/stabilisation/stabilisation_rune_straight_active_glowing.png")

    override val cornerSleepCoreTexture: ResourceLocation = SusGraphicHelper.whiteBlank

    //    ResourceLocation(Kharium.MOD_ID, "textures/blocks/runes/stabilisation/stabilisation_rune_corner_sleep_core.png")
    override val cornerSleepGlowingTexture: ResourceLocation = SusGraphicHelper.whiteBlank

    //    ResourceLocation(Kharium.MOD_ID, "textures/blocks/runes/stabilisation/stabilisation_rune_corner_sleep_glowing.png")
    override val cornerActiveCoreTexture: ResourceLocation =
        ResourceLocation(
            Kharium.MOD_ID,
            "textures/blocks/runes/stabilisation/stabilisation_rune_corner_active_core.png"
        )
    override val cornerActiveGlowingTexture: ResourceLocation = SusGraphicHelper.whiteBlank
//    ResourceLocation(Kharium.MOD_ID, "textures/blocks/runes/stabilisation/stabilisation_rune_corner_active_glowing.png")

    val stabilisationFactor = 1
    override fun onRegularSnareCheck(snare: TileKharuSnare, antiNode: TileAntiNode) {
        antiNode.stabilisation += stabilisationFactor
//        snare.aspects.add(ACAspect.HUMILITAS, 1)
    }


}