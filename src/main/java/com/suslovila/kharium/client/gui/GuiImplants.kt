package com.suslovila.kharium.client.gui

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.api.implants.ItemImplant
import com.suslovila.kharium.extendedData.KhariumPlayerExtendedData
import com.suslovila.kharium.utils.SusGraphicHelper
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.entity.RenderItem
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.RenderGameOverlayEvent
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL12
import thaumcraft.client.lib.UtilsFX
import java.awt.Color
import java.util.*

@SideOnly(Side.CLIENT)
object GuiImplants {
    //    var itemRender = RenderItem()
    val itemRender: RenderItem = RenderItem()

    val messages: ArrayList<GuiMessage> = ArrayList()
    val notEnoughFuelMessages: ArrayList<GuiMessageNotEnoughFuel> = ArrayList()

    var currentImplantSlotId = 0

    val slotActive = ResourceLocation(Kharium.MOD_ID, "textures/gui/implants/abilitySlotActive.png")
    val slotDeactivated = ResourceLocation(Kharium.MOD_ID, "textures/gui/implants/abilitySlotDeactivated.png")

    @SubscribeEvent
    fun renderGui(event: RenderGameOverlayEvent.Post) {
        if (event.type == RenderGameOverlayEvent.ElementType.ALL) {
            loadOverlayGLSettings()
            KhariumPlayerExtendedData.get(Minecraft.getMinecraft().thePlayer)?.implantStorage?.getStackInSlot(
                currentImplantSlotId
            )?.let {
                drawImplantWithScale(Minecraft.getMinecraft(), it, 70, 140, 1.5f)
            }
            val iterator = messages.iterator()
            while (iterator.hasNext()) {
                val message = iterator.next()
                message.tick()
                if (message.isExpired()) {
                    iterator.remove()
                } else {
                    message.draw(event)
                }
            }
            val startYPos = 200

            val notEnoughFuelIterator = notEnoughFuelMessages.iterator()
            var counter = 0
            val width = 5
            glPushMatrix()
            loadOverlayGLSettings()
            while (notEnoughFuelIterator.hasNext()) {
                val message = notEnoughFuelIterator.next()
                message.tick()
                if (message.isExpired()) {
                    notEnoughFuelIterator.remove()
                } else {
                    message.draw(event, startYPos - counter * width)
                    counter += 1
                }
            }
            glPopMatrix()
        }
    }

    fun drawImplantWithScale(mc: Minecraft, implant: ItemStack, x: Int, y: Int, scale: Float) {

        glPushMatrix()
        glTranslatef((x + 8 * scale), (y + 8 * scale), 0f)
        glScalef(scale, scale, scale)
        glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
        val implantClass = (implant.item as ItemImplant)

        val radius = 18.0
        val distanceBetweenAbilities = 3.0

        glDisable(GL_LIGHTING)

        glPushMatrix()
        glRotated(
            (System.nanoTime() / 30_000_000).toDouble(),
            0.0,
            0.0,
            1.0
        )
        UtilsFX.bindTexture(Kharium.MOD_ID, "textures/misc/radial4.png")
        SusGraphicHelper.drawFromCenter(radius)
        glPopMatrix()

        glPushMatrix()
        val startOffset = (implantClass.abilities.size - 1).coerceAtLeast(0) * (distanceBetweenAbilities + radius) / 2
        glTranslated(-startOffset, 30.0, 0.0)
        implantClass.abilities.forEachIndexed { index, ability ->
            glPushMatrix()


            val slotTexture = if(ability.isOnCooldown(implant) || !ability.isActive(implant)) slotDeactivated else slotActive
            UtilsFX.bindTexture(slotTexture)
            SusGraphicHelper.drawFromCenter(radius * 0.7)

            UtilsFX.bindTexture(ability.texture)
            SusGraphicHelper.drawFromCenter(radius * 0.5)

            glPushMatrix()
            val cooldown = ability.getCooldown(implant).toFloat() / 20
            val cooldownString = if(cooldown == 0.0f) "" else String.format("%.1f", cooldown)
            glTranslated(-2.0 * cooldownString.length / 2, 0.0, 0.0)
            glScaled(0.5, 0.5, 0.5)
            Minecraft.getMinecraft().fontRendererObj.drawString(cooldownString, 0, 0, Color.white.rgb)
            glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
            glPopMatrix()

            glPopMatrix()
            glTranslated(distanceBetweenAbilities + radius, 0.0, 0.0)

        }
        glPopMatrix()
        glPopMatrix()

        // drawing implant by itself
        glPushMatrix()
        glScalef(scale, scale, scale)
        glTranslatef(x / scale, y / scale, 0f)
        drawStack(Minecraft.getMinecraft(), implant, (1 / scale).toInt(), (1 / scale).toInt(), 0f)
        glPopMatrix()
    }

    private fun renderAspect(offset: Double, scale: Float, tessellator: Tessellator, texture: ResourceLocation) {
        val v1 = 0.0
        val v2 = 1.0
        val u1 = 0.0
        val u2 = 1.0

        glPushMatrix()
        glTranslated(offset / 1.4, 0.0, 0.0)
        UtilsFX.bindTexture(texture)
        tessellator.startDrawingQuads()
        val aspectRadius = 3.0 * scale
        tessellator.addVertexWithUV(-aspectRadius, aspectRadius, -299.0, u2, v2)
        tessellator.addVertexWithUV(aspectRadius, aspectRadius, -299.0, u2, v1)
        tessellator.addVertexWithUV(aspectRadius, -aspectRadius, -299.0, u1, v1)
        tessellator.addVertexWithUV(-aspectRadius, -aspectRadius, -299.0, u1, v2)
        tessellator.draw()

        tessellator.startDrawingQuads()

        val aspectHolderRadius = 3.0 * scale
        UtilsFX.bindTexture(Kharium.MOD_ID, "textures/misc/aspectSlot.png")
        tessellator.addVertexWithUV(-aspectHolderRadius, aspectHolderRadius, -299.0, u2, v2)
        tessellator.addVertexWithUV(aspectHolderRadius, aspectHolderRadius, -299.0, u2, v1)
        tessellator.addVertexWithUV(aspectHolderRadius, -aspectHolderRadius, -299.0, u1, v1)
        tessellator.addVertexWithUV(-aspectHolderRadius, -aspectHolderRadius, -299.0, u1, v2)
        tessellator.draw()
        glPopMatrix()
    }


    fun drawStack(mc: Minecraft, item: ItemStack?, x: Int, y: Int) {
        if (item != null) {
            glEnable(GL_LIGHTING)
            val prevZ = itemRender.zLevel
            itemRender.zLevel = 200f
            itemRender.renderItemAndEffectIntoGUI(mc.fontRendererObj, mc.renderEngine, item, x, y)
            itemRender.renderItemOverlayIntoGUI(mc.fontRendererObj, mc.renderEngine, item, x, y)
            itemRender.zLevel = prevZ
            glDisable(GL_LIGHTING)
        }
    }

    fun drawStack(mc: Minecraft, item: ItemStack?, x: Int, y: Int, zLevel: Float) {
        RenderHelper.enableGUIStandardItemLighting()
        glPushMatrix()
        glPushAttrib(GL_TRANSFORM_BIT)
        glEnable(GL12.GL_RESCALE_NORMAL)
        if (item != null) {
            glEnable(GL_LIGHTING)
            glEnable(GL_DEPTH_TEST)
            val prevZ: Float = itemRender.zLevel
            itemRender.zLevel = zLevel
            itemRender.renderWithColor = false
            itemRender.renderItemAndEffectIntoGUI(mc.fontRendererObj, mc.renderEngine, item, x, y)
            itemRender.renderItemOverlayIntoGUI(mc.fontRendererObj, mc.renderEngine, item, x, y)
            itemRender.zLevel = prevZ
            itemRender.renderWithColor = true
            glDisable(GL_DEPTH_TEST)
            glDisable(GL_LIGHTING)
        }
        glPopAttrib()
        glPopMatrix()
    }

    fun loadOverlayGLSettings() {
        val mc = Minecraft.getMinecraft()
        val scaledResolution = ScaledResolution(Minecraft.getMinecraft(), mc.displayWidth, mc.displayHeight)
        glClear(GL_DEPTH_BUFFER_BIT)
        SusGraphicHelper.drawGuideArrows()
        glMatrixMode(GL_PROJECTION)
        SusGraphicHelper.drawGuideArrows()
        glLoadIdentity()
        glOrtho(
            0.0,
            scaledResolution.scaledWidth_double,
            scaledResolution.scaledHeight_double,
            0.0,
            1000.0,
            3000.0
        )
        glMatrixMode(GL_MODELVIEW)
        glLoadIdentity()
        glTranslatef(0.0f, 0.0f, -2000.0f)
        glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
        SusGraphicHelper.drawGuideArrows()

    }


}