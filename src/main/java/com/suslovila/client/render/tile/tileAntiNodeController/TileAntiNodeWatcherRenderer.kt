package com.suslovila.client.render.tile.tileAntiNodeController

import com.suslovila.ExampleMod;
import com.suslovila.client.render.tile.SusTileRenderer
import com.suslovila.utils.SusMathHelper
import com.suslovila.common.block.tileEntity.TileAntiNodeWatcher;
import com.suslovila.utils.SusUtils.random
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11.*
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.codechicken.lib.math.MathHelper
import kotlin.math.abs

object TileAntiNodeWatcherRenderer : SusTileRenderer<TileAntiNodeWatcher>() {
    private val watcher: IModelCustom

    init {
        watcher = AdvancedModelLoader.loadModel(ResourceLocation(ExampleMod.MOD_ID, "models/blocks/watcher.obj"));
    }

    override fun render(tile: TileAntiNodeWatcher, partialTicks: Float) {
        this.renderLenses(tile, partialTicks);
    }

    private fun renderLenses(tile: TileAntiNodeWatcher, partialTicks: Float) {
        var playermp = Minecraft.getMinecraft().thePlayer
        glPushMatrix()

        UtilsFX.bindTexture(ExampleMod.MOD_ID, "textures/blocks/scanner2.png");
        //starting rotation
        glRotated(90.0, 1.0, 0.0, 0.0)
        glScaled(0.7, 0.7, 0.7)
        tile.lenses.forEach { lens ->
            with(lens) {
                if (turningSide == 0) {
                    if (random.nextInt(120) == 50) {
                        turningSide = SusMathHelper.randomSign()
                        speedDelta = abs(defaultSpeedDelta)
                        spinningSpeed = 0.0
                    }
                }
            }
        }

        glPushMatrix()
        glScaled(0.7, 8.0, 0.7)
        watcher.renderOnly("scanner")
        glPopMatrix()

        glTranslated(0.0, -tile.wholeLength / 2, 0.0)

        tile.lenses.forEach {
            with(it) {
                glTranslated(0.0, width / 2, 0.0)
                glPushMatrix()

                spinningSpeed = Math.min(spinningSpeed + speedDelta, 1.0)
                speedDelta = Math.min(speedDelta + 0.01 * if (speedDelta > 0) 1 else -1, defaultSpeedDelta)
                if (spinningSpeed <= 0) turningSide = 0
                if (random.nextInt(80) == 30) speedDelta *= -1
                if (turningSide != 0) angle = (angle + (spinningSpeed * turningSide)) % 360
                glRotatef(angle.toFloat(), 0F, 1F, 0F)

                for (i in 1..6) watcher.renderOnly("inner$i")
                watcher.renderOnly("scanner")
                renderOuterCrystals()
                renderGlasses(playermp)

                glPopMatrix()

                glTranslated(0.0, width / 2, 0.0)
                UtilsFX.bindTexture(ExampleMod.MOD_ID, "textures/blocks/scanner2.png");

            }
        }
        UtilsFX.bindTexture(TextureMap.locationBlocksTexture);
        glPopMatrix()
    }

    private fun renderOuterCrystals() {
        glDepthMask(false)
        glDisable(GL_CULL_FACE)
        glDisable(GL_ALPHA_TEST)
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
        glDisable(GL_LIGHTING)

        glColor4f(1f, 1f, 1f, 0.3f)
        for (i in 1..6) watcher.renderOnly("outer$i")
        glEnable(GL_CULL_FACE)
        glEnable(GL_ALPHA_TEST)
        glDisable(GL_BLEND)
        glEnable(GL_LIGHTING)
        glDepthMask(true)

    }

    private fun renderGlasses(player: EntityPlayer) {
        glDepthMask(false)
        glPushMatrix()
        glRotated(30.0, 0.0, 1.0, 0.0)
        glRotated(90.0, 1.0, 0.0, 0.0)
        glPushMatrix()
        UtilsFX.renderQuadCenteredFromTexture(
            ResourceLocation("thaumcraft", "textures/models/scanscreen.png"),
            2.5F,
            1.0F,
            1.0F,
            1.0F,
            (190.0F + MathHelper.sin(((player.ticksExisted - player.worldObj.rand.nextInt(2)).toDouble())) * 10.0F + 10.0F).toInt(),
            771,
            1.0F
        )
        glPopMatrix()
        glRotated(180.0, 1.0, 0.0, 0.0)
        UtilsFX.renderQuadCenteredFromTexture(
            ResourceLocation("thaumcraft", "textures/models/scanscreen.png"),
            2.5F,
            1.0F,
            1.0F,
            1.0F,
            (190.0F + MathHelper.sin(((player.ticksExisted - player.worldObj.rand.nextInt(2)).toDouble())) * 10.0F + 10.0F).toInt(),
            771,
            1.0F
        )
        glPopMatrix()
        glDepthMask(true)

    }

}
