package com.suslovila.kharium.common.event.events

import com.suslovila.kharium.common.event.GlobalEvent
import cpw.mods.fml.common.gameevent.TickEvent
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.ActiveRenderInfo
import net.minecraft.client.renderer.Tessellator
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.Vec3
import net.minecraftforge.client.event.RenderWorldLastEvent
import org.lwjgl.opengl.GL11
import thaumcraft.client.lib.UtilsFX
import thaumcraft.client.renderers.tile.TileEldritchPortalRenderer
import thaumcraft.common.tiles.TileEldritchPortal

class GlobalEventTeleportation(
    x: Double,
    y: Double,
    z: Double,
    duration: Int,
    timer: Int,
    expired: Boolean = false,

    var isOpening: Boolean
) : GlobalEvent(
    x,
    y,
    z,
    duration,
    timer,
    expired
) {


    private fun renderPortal(event: RenderWorldLastEvent) {
        val nt = System.nanoTime()
        val time = nt / 50000000L
        val renderTimer = if(isOpening) timer else duration - timer
        val c = Math.min(30.0f, renderTimer + event.partialTicks).toInt()
        val e = Math.min(5.0f, renderTimer + event.partialTicks).toInt()
        val scale = e / 5.0f
        val scaley = c / 30.0f
        UtilsFX.bindTexture(TileEldritchPortalRenderer.portaltex)
        GL11.glPushMatrix()
        GL11.glDepthMask(false)
        GL11.glEnable(3042)
        GL11.glBlendFunc(770, 771)
        GL11.glColor4f(1.0f, 0.0f, 1.0f, 1.0f)
        if (Minecraft.getMinecraft().renderViewEntity is EntityPlayer) {
            val tessellator = Tessellator.instance
            val arX = ActiveRenderInfo.rotationX
            val arZ = ActiveRenderInfo.rotationZ
            val arYZ = ActiveRenderInfo.rotationYZ
            val arXY = ActiveRenderInfo.rotationXY
            val arXZ = ActiveRenderInfo.rotationXZ
            val player = Minecraft.getMinecraft().renderViewEntity as EntityPlayer
            val iPX = player.prevPosX + (player.posX - player.prevPosX) * event.partialTicks
            val iPY = player.prevPosY + (player.posY - player.prevPosY) * event.partialTicks
            val iPZ = player.prevPosZ + (player.posZ - player.prevPosZ) * event.partialTicks
            tessellator.startDrawingQuads()
            tessellator.setBrightness(220)
            tessellator.setColorRGBA_F(1.0f, 1.0f, 1.0f, 1.0f)
            val px = x + 0.5
            val py = y + 0.5
            val pz = z + 0.5
            val v1 = Vec3.createVectorHelper((-arX - arYZ).toDouble(), -arXZ.toDouble(), (-arZ - arXY).toDouble())
            val v2 = Vec3.createVectorHelper((-arX + arYZ).toDouble(), arXZ.toDouble(), (-arZ + arXY).toDouble())
            val v3 = Vec3.createVectorHelper((arX + arYZ).toDouble(), arXZ.toDouble(), (arZ + arXY).toDouble())
            val v4 = Vec3.createVectorHelper((arX - arYZ).toDouble(), -arXZ.toDouble(), (arZ - arXY).toDouble())
            val frame = time.toInt() % 16
            val f2 = frame / 16.0f
            val f3 = f2 + 0.0625f
            val f4 = 0.0f
            val f5 = 1.0f
            tessellator.setNormal(0.0f, 0.0f, -1.0f)
            tessellator.addVertexWithUV(
                px + v1.xCoord * scale,
                py + v1.yCoord * scaley,
                pz + v1.zCoord * scale,
                f2.toDouble(),
                f5.toDouble()
            )
            tessellator.addVertexWithUV(
                px + v2.xCoord * scale,
                py + v2.yCoord * scaley,
                pz + v2.zCoord * scale,
                f3.toDouble(),
                f5.toDouble()
            )
            tessellator.addVertexWithUV(
                px + v3.xCoord * scale,
                py + v3.yCoord * scaley,
                pz + v3.zCoord * scale,
                f3.toDouble(),
                f4.toDouble()
            )
            tessellator.addVertexWithUV(
                px + v4.xCoord * scale,
                py + v4.yCoord * scaley,
                pz + v4.zCoord * scale,
                f2.toDouble(),
                f4.toDouble()
            )
            tessellator.draw()
        }
        GL11.glDisable(3042)
        GL11.glDepthMask(true)
        GL11.glPopMatrix()
    }

    override fun onCreated() {
    }

    override fun onUpdate(event: TickEvent.WorldTickEvent) {

    }

    override fun onRendered(event: RenderWorldLastEvent) {

    }

    override fun onExpired() {

    }

}