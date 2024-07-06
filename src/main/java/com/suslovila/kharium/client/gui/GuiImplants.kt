import com.suslovila.kharium.Kharium
import com.suslovila.kharium.api.implants.ItemImplant
import com.suslovila.kharium.extendedData.KhariumPlayerExtendedData
import com.suslovila.kharium.research.KhariumAspect
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.entity.RenderItem
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.event.RenderGameOverlayEvent
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL12
import thaumcraft.client.lib.UtilsFX
import java.util.*

@SideOnly(Side.CLIENT)
object GuiImplants : GuiScreen() {
    //    var itemRender = RenderItem()
    val itemRender: RenderItem = RenderItem()

    var currentImplantSlotId = 0
    private val decayBarTexture = ResourceLocation("thebetweenlands:textures/gui/decayBar.png")
    private val mc = Minecraft.getMinecraft()
    private val random = Random()

    @SubscribeEvent
    fun renderGui(event: RenderGameOverlayEvent.Post) {
        if (event.type == RenderGameOverlayEvent.ElementType.ALL) {
            val mc = Minecraft.getMinecraft()
            val width = event.resolution.scaledWidth
            val height = event.resolution.scaledHeight
//            mc.fontRendererObj.drawString(currentImplantSlotId.toString(),80,10, 0xFFFFFF)
            val player = mc.thePlayer ?: return
            val implant = KhariumPlayerExtendedData.get(player)?.implantStorage?.getStackInSlot(
                currentImplantSlotId
            ) ?: return
//            drawStack(Minecraft.getMinecraft(), implant, 70, 70, 150f)
            drawImplantWithScale(Minecraft.getMinecraft(), implant, 50, 180, 2f)

        }
    }

    fun drawImplantWithScale(mc: Minecraft, implant: ItemStack, x: Int, y: Int, scale: Float) {


        glPushMatrix()
        glTranslatef((x + 8 * scale), (y + 8 * scale), 200f)
        glScalef(scale, scale, scale)
        val tessellator = Tessellator.instance

        val implantClass = (implant.item as ItemImplant)

        val startRadius = 18.0

        glEnable(GL_BLEND)
        glDisable(GL_LIGHTING)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
        glDisable(GL_ALPHA_TEST)

        implantClass.abilities.forEachIndexed { index, ability ->
            glPushMatrix()
            val radius = startRadius * (index + 1)
            val v1 = 0.0
            val v2 = 1.0
            val u1 = 0.0
            val u2 = 1.0
            glRotated(
                (System.nanoTime() / 30_000_000).toDouble() * (if (index % 2 == 0) 1 else -1) % 360,
                0.0,
                0.0,
                1.0
            )
            tessellator.setColorRGBA_F(1.0f, 1.0f, 1.0f, 1.0f)

            UtilsFX.bindTexture(Kharium.MOD_ID, "textures/misc/circleImplant.png")

            tessellator.startDrawingQuads()
            tessellator.addVertexWithUV(-radius, radius, -300.0, u2, v2)
            tessellator.addVertexWithUV(radius, radius, -300.0, u2, v1)
            tessellator.addVertexWithUV(radius, -radius, -300.0, u1, v1)
            tessellator.addVertexWithUV(-radius, -radius, -300.0, u1, v2)
            tessellator.draw()

            val scale = (1 + index).toFloat()
            renderAspect(
                radius / 1.4,
                scale,
                tessellator,
                ResourceLocation(Kharium.MOD_ID, "textures/misc/antinode.png")
            )
            ability.getFuelConsumeOnActivation(implant).aspects.aspects.keys.forEachIndexed { index, aspect ->
                val angle = 360 / (index + 2).toDouble()
                glRotated(angle, 0.0, 0.0, 1.0)
//                renderAspect()
            }
            glPopMatrix()

        }

        glPopMatrix()

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
}