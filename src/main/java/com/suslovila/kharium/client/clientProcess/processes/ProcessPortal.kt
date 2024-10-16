package com.suslovila.kharium.client.clientProcess.processes

import com.suslovila.kharium.api.event.ClientProcess
import com.suslovila.kharium.api.fuel.FuelKharu
import com.suslovila.kharium.utils.SusGraphicHelper
import com.suslovila.kharium.utils.SusGraphicHelper.getRenderPos
import com.suslovila.kharium.utils.SusVec3
import com.suslovila.kharium.utils.getPosition
import cpw.mods.fml.common.gameevent.TickEvent
import net.minecraft.client.Minecraft
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.RenderWorldLastEvent
import org.lwjgl.opengl.GL11
import org.lwjgl.util.vector.Matrix4f
import org.lwjgl.util.vector.Vector3f
import thaumcraft.common.Thaumcraft
import java.nio.ByteBuffer
import java.nio.FloatBuffer

class ProcessPortal(
    x: Double,
    y: Double,
    z: Double,
    duration: Int
) : ClientProcess(
    x,
    y,
    z,
    duration
) {
    override fun tick(event: TickEvent.WorldTickEvent) {
        timer++
    }

    override fun render(event: RenderWorldLastEvent) {
        GL11.glPushMatrix()
        val player = Minecraft.getMinecraft().thePlayer
        val lookVec = player.getRenderPos(event.partialTicks).add(0.0, player.eyeHeight.toDouble(), 0.0).subtract(SusVec3(x,y,z))
        SusGraphicHelper.translateFromPlayerTo(SusVec3(x, y, z), event.partialTicks)
        val targetVector = SusVec3(1.0f, 2.0f, 3.0f).normalize()
        val currentZ = SusVec3(0.0f, 0.0f, 1.0f)

        val rotationAxis = targetVector.cross(currentZ)

        val angle = Math.acos(currentZ.dot(targetVector)).toFloat()

        val rotationMatrix = (Matrix4f().setIdentity() as Matrix4f).rotate(
            angle,
            Vector3f(
                rotationAxis.x.toFloat(),
                rotationAxis.y.toFloat(),
                rotationAxis.z.toFloat()
            )
        )


        with(rotationMatrix) {
            val floatBuffer = ByteBuffer.allocateDirect(16 * java.lang.Float.BYTES).asFloatBuffer()
            floatBuffer.put(0, m00)
            floatBuffer.put(1, m10)
            floatBuffer.put(2, m20)
            floatBuffer.put(3, m30)
            floatBuffer.put(4, m01)
            floatBuffer.put(5, m11)
            floatBuffer.put(6, m21)
            floatBuffer.put(7, m31)
            floatBuffer.put(8, m02)
            floatBuffer.put(9, m12)
            floatBuffer.put(10, m22)
            floatBuffer.put(11, m32)
            floatBuffer.put(12, m03)
            floatBuffer.put(13, m13)
            floatBuffer.put(14, m23)
            floatBuffer.put(15, m33)
            GL11.glLoadMatrix(floatBuffer)
        }

        SusGraphicHelper.renderTextureOrth(
            FuelKharu.texture,
            1.0,
            1.0,
            1.0,
            lookVec
        )
        GL11.glPopMatrix()
    }
}