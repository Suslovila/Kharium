package com.suslovila.kharium.client.render.tile.tileAntiNodeController

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.client.particles.FXKharu
import com.suslovila.kharium.client.render.tile.SusTileRenderer
import com.suslovila.kharium.common.block.tileEntity.KharuTail
import com.suslovila.kharium.common.block.tileEntity.TileAntiNode
import com.suslovila.kharium.utils.SusUtils
import com.suslovila.kharium.utils.SusUtils.antiNodeAppearanceTime
import com.suslovila.kharium.utils.SusUtils.random
import com.suslovila.kharium.utils.SusVec3
import com.suslovila.kharium.utils.getPosDouble
import net.minecraft.client.Minecraft
import net.minecraft.entity.EntityLivingBase
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.MathHelper
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import org.lwjgl.opengl.GL11
import thaumcraft.client.fx.ParticleEngine
import thaumcraft.client.lib.UtilsFX
import kotlin.random.Random.Default.nextDouble
import kotlin.random.Random.Default.nextInt

object TileAntiNodeRenderer : SusTileRenderer<TileAntiNode>() {

    override fun renderTileEntityAt(antiNode: TileEntity?, x: Double, y: Double, z: Double, partialTicks: Float) {
        super.renderTileEntityAt(antiNode, x, y, z, partialTicks)
        if (antiNode !is TileAntiNode) return
        val viewDistance = 48.0
        val viewer = Minecraft.getMinecraft().renderViewEntity
        val distance = viewer.getDistance(
            antiNode.xCoord.toDouble() + 0.5,
            antiNode.yCoord.toDouble() + 0.5,
            antiNode.zCoord.toDouble() + 0.5
        )
        if (distance <= viewDistance) {
            renderAntiNode(
                viewer,
                viewDistance,
                depthIgnore = false,
                size = 1.0f,
                antiNode.xCoord,
                antiNode.yCoord,
                antiNode.zCoord,
                partialTicks,
                sizeOfNode = 0.5f * Math.min(
                    antiNode.tickExisted.toFloat() / 120, 1f
                )
            )
            if (antiNode.tickExisted > antiNodeAppearanceTime / 2) {
//                spawnKharuParticles(
//                    antiNode,
//                    viewer.worldObj,
//                    maxTailsAmount = 5,
//                    minTailSpeed = 2,
//                    maxTailSpeed = 3,
//                    deleteAndAddTailChanceFactor = 3,
//                    minRadius = 1.28,
//                    maxRadius = 1.33,
//                    radiusChangeMinSpeed = 0.003,
//                    radiusChangeMaxSpeed = 0.005,
//                    particleLifeTime = 13,
//                    particleSize = 0.8f
//                )
            }

            //handling anti-node bolt
            if (!Minecraft.getMinecraft().isGamePaused) {
                if (random.nextInt(9) == 6) {
                    val lightningLength = 2f
                    Kharium.proxy.nodeAntiBolt(
                        viewer.worldObj,
                        x = antiNode.xCoord + 0.5f,
                        y = antiNode.yCoord + 0.5f,
                        z = antiNode.zCoord + 0.5f,
                        x2 = (antiNode.xCoord + nextDouble(
                            -lightningLength.toDouble(),
                            lightningLength.toDouble()
                        )).toFloat(),
                        y2 = (antiNode.yCoord + nextDouble(
                            -lightningLength.toDouble(),
                            lightningLength.toDouble()
                        )).toFloat(),
                        z2 = (antiNode.zCoord + nextDouble(
                            -lightningLength.toDouble(),
                            lightningLength.toDouble()
                        )).toFloat()
                    )
                }
            }
        }
    }

    override fun render(tile: TileAntiNode, partialTicks: Float) {
    }

    //TAKEN FROM THAUMCRAFT
    fun renderAntiNode(
        viewer: EntityLivingBase,
        viewDistance: Double,
        depthIgnore: Boolean,
        size: Float,
        x: Int,
        y: Int,
        z: Int,
        partialTicks: Float,
        sizeOfNode: Float
    ) {
        UtilsFX.bindTexture(antiNodeTexture)
        val distance = viewer.getDistance(x.toDouble() + 0.5, y.toDouble() + 0.5, z.toDouble() + 0.5)
        var alpha = ((viewDistance - distance) / viewDistance).toFloat()
        GL11.glPushMatrix()
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569f)
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569f)
        GL11.glDepthMask(false)
        if (depthIgnore) GL11.glDisable(GL11.GL_DEPTH_TEST)
        GL11.glDisable(GL11.GL_CULL_FACE)
        val bscale = 0.25f
        GL11.glPushMatrix()
        GL11.glColor4f(1.0f, 0.0f, 0.0f, alpha)
        var count = 0
        var scale: Float
        var average = 0.0f
        for (j in 0..1) {
            //first - "perditio" color(because it fits the theme of node), second - fictive(terra color, does not matter, bacause we only draw red color)
            if (j == 0) alpha = (alpha.toDouble() * 1.5).toFloat()
            average += (if (j == 0) 25 else 50).toFloat()
            GL11.glPushMatrix()
            GL11.glEnable(GL11.GL_BLEND)
            GL11.glBlendFunc(770, if (j == 0) 771 else 1)
            scale =
                MathHelper.sin(viewer.ticksExisted.toFloat() / (14.0f - count.toFloat())) * bscale + bscale * 2.0f
            scale = 0.2f + scale * ((if (j == 0) 25 else 50).toFloat() / 50.0f)
            scale *= size
            UtilsFX.renderFacingStrip(
                x.toDouble() + 0.5,
                y.toDouble() + 0.5,
                z.toDouble() + 0.5,
                0f,
                scale * (if (j == 1) 0.65f else 0.7f) * sizeOfNode,
                alpha,
                1,
                0,
                0,
                partialTicks,
                if (j == 0) 4210752 else 5685248
            )
            GL11.glDisable(GL11.GL_BLEND)
            GL11.glPopMatrix()
            ++count
            if (j == 0) alpha = (alpha.toDouble() / 1.5).toFloat()
        }
        average *= sizeOfNode
        GL11.glPushMatrix()
        GL11.glEnable(GL11.GL_BLEND)
        scale = 0.1f + average / 150.0f
        scale *= size
        GL11.glColor4f(1.0f, 0.0f, 0.0f, alpha)
        UtilsFX.renderFacingStrip(
            x.toDouble() + 0.5,
            y.toDouble() + 0.5,
            z.toDouble() + 0.5,
            0f,
            scale * sizeOfNode,
            alpha,
            1,
            1,
            0,
            partialTicks,
            16777215
        )
        GL11.glDisable(GL11.GL_BLEND)
        GL11.glPopMatrix()
        GL11.glPopMatrix()
        GL11.glEnable(GL11.GL_CULL_FACE)
        if (depthIgnore) GL11.glEnable(GL11.GL_DEPTH_TEST)
        GL11.glDepthMask(true)
        GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f)
        GL11.glPopMatrix()
    }

    private fun spawnKharuParticles(
        antiNode: TileAntiNode,
        world: World,
        maxTailsAmount: Int,
        minTailSpeed: Int,
        maxTailSpeed: Int,
        deleteAndAddTailChanceFactor: Int,
        minRadius: Double,
        maxRadius: Double,
        radiusChangeMinSpeed: Double,
        radiusChangeMaxSpeed: Double,
        particleLifeTime: Int,
        particleSize: Float
    ) {

        for (hl in 0 until deleteAndAddTailChanceFactor) {
            if (random.nextInt(50) == 37 && antiNode.kharuTails.size < maxTailsAmount) {
                antiNode.kharuTails.add(
                    KharuTail(
                        homePos = antiNode.getPosDouble().add(0.5, 0.5, 0.5),
                        tailSpeed = nextInt(minTailSpeed, maxTailSpeed),
                        maxRadius = nextDouble(minRadius, maxRadius),
                        radiusChangePerFrame = nextDouble(radiusChangeMinSpeed, radiusChangeMaxSpeed),
                        aimVec3 = SusVec3(
                            nextDouble(-1.0, 1.0),
                            nextDouble(-1.0, 1.0),
                            nextDouble(-1.0, 1.0)
                        ).normalize()
                    )
                )
            }
        }
        antiNode.kharuTails.forEach { kharuTail ->
            var h = 0
            while (h < kharuTail.tailSpeed && antiNode.kharuTails.contains(kharuTail)) {
                val m = SusUtils.getOrthogonalVec3(kharuTail.aimVec3).scale(kharuTail.actualRadius)
                val k = kharuTail.aimVec3.cross(m).normalize().scale(kharuTail.actualRadius)
                val offsetFromHome = m.scale(Math.sin(kharuTail.timer * Math.PI / 90))
                    .add(k.scale(Math.cos(kharuTail.timer * Math.PI / 90)))
                kharuTail.timer++
                val particlePosition = kharuTail.homePos.add(offsetFromHome)
                val particle = FXKharu(
                    world,
                    particlePosition.x,
                    particlePosition.y,
                    particlePosition.z,
                    0.0,
                    0.0,
                    0.0,
                    particleLifeTime,
                    particleSize,
                    true
                )
                ParticleEngine.instance.addEffect(world, particle)
                if (random.nextInt(300) == 37 && antiNode.kharuTails.size >= maxTailsAmount) {
                    antiNode.kharuTails.remove(kharuTail)
                }
                h++
            }
        }
    }


    val antiNodeTexture = ResourceLocation(Kharium.MOD_ID, "textures/antinode/antinodetexture.png")

}