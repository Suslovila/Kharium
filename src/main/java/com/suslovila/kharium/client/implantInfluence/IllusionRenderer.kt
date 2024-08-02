package com.suslovila.kharium.client.implantInfluence

import com.suslovila.kharium.utils.SusGraphicHelper
import com.suslovila.kharium.utils.SusGraphicHelper.getRenderPos
import com.suslovila.kharium.utils.SusVec3
import com.suslovila.kharium.utils.getPosition
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import io.netty.buffer.ByteBuf
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.entity.EntityLivingBase
import net.minecraftforge.client.event.RenderWorldLastEvent
import org.lwjgl.opengl.GL11.*
import thaumcraft.client.lib.UtilsFX
import java.util.*


// used by clone implant to create illusions
object IllusionRenderer {
    // player's id's and timer for each
    var illusions = mutableListOf<Illusion>()

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    fun renderIllusions(event: RenderWorldLastEvent) {
        val player = Minecraft.getMinecraft()?.thePlayer ?: return
        println("started render")

        illusions.forEach { it.timer++ }
        illusions.removeIf { it.timer > it.duration }
        illusions.forEach { hackerObj ->
            println("some hacker is handled")
            val hacker = player.worldObj.getPlayerEntityByUUID(hackerObj.uuid) ?: return@forEach
            println("got the player")
            glPushMatrix()
//            SusGraphicHelper.translateFromPlayerTo(hacker.getPosition(), event.partialTicks)
            val vecFromPlayerToHacker = hacker.getRenderPos(event.partialTicks).subtract(player.getRenderPos(event.partialTicks))
            val zVec3 = SusVec3(0, 0, 1.0)
            val angle = (SusVec3.angleBetweenVec3(
                zVec3,
                vecFromPlayerToHacker
            ) * 180.0 / Math.PI) * (if (vecFromPlayerToHacker.x > 0) 1 else -1)
            println("the anlge is: $angle")
            glRotated(angle, 0.0, 1.0, 0.0)

            glTranslatef(0.0f, -hacker.height / 2 - 0.2f, 0.0f)
            println(hackerObj.illusionAmount)
            for (i in 0 until hackerObj.illusionAmount) {
                glRotated(-360.0 / (1 + hackerObj.illusionAmount), 0.0, 1.0, 0.0)
                glPushMatrix()
                println("Drawing illusionassssssssssss")
                // idk why, but for correct work we need to substract 1.3 :/
                glTranslated(0.0, 0.0, vecFromPlayerToHacker.length())
                glRotated(90.0, 0.0, 1.0, 0.0)
                drawEntityOrthogonalToZAxis(hacker)
//                glDisable(GL_CULL_FACE)
//                UtilsFX.bindTexture(SusGraphicHelper.whiteBlank)
//                SusGraphicHelper.cubeModel.renderAll()
                glPopMatrix()

            }

            glPopMatrix()

        }
    }

    // used by inventory renderers
    fun drawEntityOrthogonalToZAxis(
        entity: EntityLivingBase
    ) {
        glPushAttrib(GL_COLOR_MATERIAL)
        glEnable(GL_COLOR_MATERIAL)
        glPushMatrix()
        RenderHelper.enableStandardItemLighting()
        RenderManager.instance.renderEntityWithPosYaw(entity, 0.0, 0.0, 0.0, 0.0f, 1.0f)
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