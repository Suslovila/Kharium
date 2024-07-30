package com.suslovila.kharium.client.implantInfluence

import com.suslovila.kharium.utils.SusGraphicHelper
import com.suslovila.kharium.utils.SusVec3
import com.suslovila.kharium.utils.getPosition
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import io.netty.buffer.ByteBuf
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.entity.EntityLivingBase
import net.minecraftforge.client.event.RenderWorldLastEvent
import org.lwjgl.opengl.GL11.*
import java.util.*


// used by clone implant to create illusions
object IllusionRenderer {
    // player's id's and timer for each
    var illusions = mutableListOf<Illusion>()

    @SubscribeEvent
    fun renderIllusions(event: RenderWorldLastEvent) {
        val player = Minecraft.getMinecraft()?.thePlayer ?: return

        illusions.forEach { it.timer++ }
        illusions.removeIf { it.timer > it.duration }
        illusions.forEach { hackerObj ->
            val hacker = player.worldObj.getPlayerEntityByUUID(hackerObj.uuid) ?: return@forEach
            glPushMatrix()
            SusGraphicHelper.translateFromPlayerTo(hacker.getPosition(), event.partialTicks)
            val vecFromPlayerToHacker = hacker.getPosition().subtract(player.getPosition())
            val zVec3 = SusVec3(0, 0, 1)
            val angle = (SusVec3.angleBetweenVec3(
                zVec3,
                vecFromPlayerToHacker
            ) * 180.0 / Math.PI) * if (vecFromPlayerToHacker.x > 0) 1 else -1
            glRotated(angle, 0.0, 1.0, 0.0)
            for (i in 0 until hackerObj.illusionAmount) {
                glPushMatrix()

                glTranslated(0.0, 0.0, vecFromPlayerToHacker.length())
                glRotated(180.0, 0.0, 1.0, 0.0)
                drawEntityOrthogonalToZAxis(1, 1, 1, 0.0f, 0.0f, hacker)
                glPopMatrix()
            }

            glPopMatrix()
        }
    }

    // used by inventory renderers
    fun drawEntityOrthogonalToZAxis(
        scaleX: Int,
        scaleY: Int,
        scaleZ: Int,
        x: Float,
        y: Float,
        entity: EntityLivingBase
    ) {
        glPushAttrib(GL_COLOR_MATERIAL)
        glEnable(GL_COLOR_MATERIAL)
        glPushMatrix()
        glTranslatef(scaleX.toFloat(), scaleY.toFloat(), 50.0f)
        glScalef((-scaleZ).toFloat(), scaleZ.toFloat(), scaleZ.toFloat())
        glRotatef(180.0f, 0.0f, 0.0f, 1.0f)
        val f2 = entity.renderYawOffset
        val f3 = entity.rotationYaw
        val f4 = entity.rotationPitch
        val f5 = entity.prevRotationYawHead
        val f6 = entity.rotationYawHead
        glRotatef(135.0f, 0.0f, 1.0f, 0.0f)
        RenderHelper.enableStandardItemLighting()
        glRotatef(-135.0f, 0.0f, 1.0f, 0.0f)
        glRotatef(-Math.atan((y / 40.0f).toDouble()).toFloat() * 20.0f, 1.0f, 0.0f, 0.0f)
        entity.renderYawOffset = Math.atan((x / 40.0f).toDouble()).toFloat() * 20.0f
        entity.rotationYaw = Math.atan((x / 40.0f).toDouble()).toFloat() * 40.0f
        entity.rotationPitch = -Math.atan((y / 40.0f).toDouble()).toFloat() * 20.0f
        entity.rotationYawHead = entity.rotationYaw
        entity.prevRotationYawHead = entity.rotationYaw
        glTranslatef(0.0f, entity.yOffset, 0.0f)
        RenderManager.instance.playerViewY = 180.0f
        RenderManager.instance.renderEntityWithPosYaw(entity, 0.0, 0.0, 0.0, 0.0f, 1.0f)
        entity.renderYawOffset = f2
        entity.rotationYaw = f3
        entity.rotationPitch = f4
        entity.prevRotationYawHead = f5
        entity.rotationYawHead = f6
        glPopMatrix()
        glPopAttrib()
    }
}

class Illusion(
    val uuid: UUID,
    val duration: Int,
    val illusionAmount: Int,
    var timer: Int = 0
) {
    companion object {
        fun readFrom(byteBuf: ByteBuf): Illusion {
            val mostBits = byteBuf.readLong()
            val leastBits = byteBuf.readLong()
            val uuid = UUID(mostBits, leastBits)
            val duration = byteBuf.readInt()
            val illusionAmount = byteBuf.readInt()
            val timer = byteBuf.readInt()

            return Illusion(uuid, duration, illusionAmount, timer)
        }
    }

    fun writeTo(byteBuf: ByteBuf) {
        byteBuf.writeLong(uuid.mostSignificantBits)
        byteBuf.writeLong(uuid.leastSignificantBits)

        byteBuf.writeInt(duration)
        byteBuf.writeInt(illusionAmount)
        byteBuf.writeInt(timer)

    }
}