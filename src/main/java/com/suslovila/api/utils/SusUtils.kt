package com.suslovila.api.utils

import com.suslovila.client.particles.FXAntiNode
import net.minecraft.client.Minecraft
import net.minecraft.client.particle.EntityFX
import net.minecraft.client.renderer.Tessellator
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.MathHelper
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL11.glTranslated
import thaumcraft.api.ThaumcraftApiHelper
import thaumcraft.api.aspects.AspectList
import thaumcraft.client.lib.UtilsFX.getBrightnessForRender
import thaumcraft.common.Thaumcraft
import thaumcraft.common.lib.network.PacketHandler
import thaumcraft.common.lib.network.playerdata.PacketResearchComplete
import java.util.*
import java.util.concurrent.ThreadLocalRandom

object SUSUtils {
    //some static fields and functions to make life easier ;)

    var random = Random()
    const val aspectReducePerTick = 3
    const val antiNodeAppearanceTime = 120
    const val humilitasColor = 16727457
    fun glTranslateRandomWithEqualD(factor: Double) =
        glTranslated(nextDouble(factor), nextDouble(factor), nextDouble(factor))


    fun nextDouble(n1: Double, n2: Double) = ThreadLocalRandom.current().nextDouble(n1, n2)


    fun nextDouble(n1: Double): Double =
        if (Math.abs(n1) == 0.0) 0.0 else ThreadLocalRandom.current().nextDouble(-n1, n1)


    fun wrapDegrees(p_14178_: Float): Float {
        var f = p_14178_ % 360.0f
        if (f >= 180.0f) {
            f -= 360.0f
        }
        if (f < -180.0f) {
            f += 360.0f
        }
        return f
    }

    fun getAspectList(stack: ItemStack?) = ThaumcraftApiHelper.getObjectAspects(stack) ?: AspectList()


    fun randomSign() = if (random.nextBoolean()) 1 else -1

    fun completeNormalResearch(researchName: String?, player: EntityPlayer?, world: World) {

        PacketHandler.INSTANCE.sendTo(PacketResearchComplete(researchName), player as EntityPlayerMP?)
        Thaumcraft.proxy.getResearchManager().completeResearch(player, researchName)
        world.playSoundAtEntity(player, "thaumcraft:learn", 0.75f, 1.0f)

    }

    fun completeResearchSilently(researchName: String?, player: EntityPlayer?) =
        Thaumcraft.proxy.getResearchManager().completeResearch(player, researchName)


    fun ItemStack.getOrCreateTag(): NBTTagCompound {
        if (!hasTagCompound()) tagCompound = NBTTagCompound()
        return tagCompound
    }

    enum class BasicDirection(val vec3: SusVec3) {
        NORTH(SusVec3(0, 0, -1)),
        SOUTH(SusVec3(0, 0, 1)),
        EAST(SusVec3(1, 0, 0)),
        WEST(SusVec3(-1, 0, 0)),
        UP(SusVec3(0, 1, 0)),
        DOWN(SusVec3(0, -1, 0))

    }
//    fun renderBasicFX(tessellator : Tessellator, partialTick : Int, texture : ResourceLocation, particle : EntityFX) {
//        GL11.glPushMatrix()
//        with(particle) {
//            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1f)
//            Minecraft.getMinecraft().renderEngine.bindTexture(texture)
//            GL11.glEnable(3042)
//            GL11.glBlendFunc(770, 771)
//            GL11.glColor4f(1.0f, 1.0f, 1.0f, 1f)
//            GL11.glAlphaFunc(GL11.GL_GREATER, 0.003921569f)
//            GL11.glAlphaFunc(GL11.GL_GREATER, 0.6f - (this.particleAge.toFloat() + partialTick - 1.0f) * 0.25f * 0.5f)
//
//            val f10: Float = 0.1f * this.particleScale
//            val f11: Float =
//                (this.prevPosX + (this.posX - this.prevPosX) * partialTick.toDouble() - EntityFX.interpPosX).toFloat()
//            val f12: Float =
//                (this.prevPosY + (this.posY - this.prevPosY) * partialTick.toDouble() - EntityFX.interpPosY).toFloat()
//            val f13: Float =
//                (this.prevPosZ + (this.posZ - this.prevPosZ) * partialTick.toDouble() - EntityFX.interpPosZ).toFloat()
//
//            tessellator.setBrightness(getBrightnessForRender(partialTick))
//
//            tessellator.addVertexWithUV(
//                (f11 - x * f10 - u * f10).toDouble(),
//                (f12 - y * f10).toDouble(),
//                (f13 - z * f10 - v * f10).toDouble(),
//                0.0,
//                0.0
//            )
//            tessellator.addVertexWithUV(
//                (f11 - x * f10 + u * f10).toDouble(),
//                (f12 + y * f10).toDouble(),
//                (f13 - z * f10 + v * f10).toDouble(),
//                0.0,
//                1.0
//            )
//            tessellator.addVertexWithUV(
//                (f11 + x * f10 + u * f10).toDouble(),
//                (f12 + y * f10).toDouble(),
//                (f13 + z * f10 + v * f10).toDouble(),
//                1.0,
//                1.0
//            )
//            tessellator.addVertexWithUV(
//                (f11 + x * f10 - u * f10).toDouble(),
//                (f12 - y * f10).toDouble(),
//                (f13 + z * f10 - v * f10).toDouble(),
//                1.0,
//                0.0
//            )
//
//            GL11.glDisable(GL11.GL_BLEND)
//            GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f)
//            GL11.glPopMatrix()
//        }
//    }
}
fun sin(value : Double) = MathHelper.sin(value.toFloat())
fun cos(value: Double) = MathHelper.cos(value.toFloat())
fun getCordSystemFromVec3(vec3: SusVec3) : ArrayList<SusVec3> {

    val perpendikular = SusVec3(vec3.z, 0.0, vec3.x);
    return arrayListOf(vec3, vec3.cross(perpendikular), perpendikular)
}