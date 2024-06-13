package com.suslovila.kharium.client.gui


import com.suslovila.common.inventory.container.ContainerKharuSnare
import com.suslovila.kharium.Kharium
import com.suslovila.kharium.common.multiStructure.kharuSnare.TileKharuSnare
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.renderer.entity.RenderItem
import net.minecraft.entity.player.InventoryPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11

class GuiKharuSnare(
    val inventoryPlayer: InventoryPlayer,
    val snare: TileKharuSnare
) :
    GuiContainer(ContainerKharuSnare(inventoryPlayer, snare)) {

    init {
        xSize = 176
        ySize = 207
    }

    override fun drawGuiContainerBackgroundLayer(partialTicks: Float, mouseX: Int, mouseY: Int) {
        mc.textureManager.bindTexture(IMAGE_URL)
        val k = (width - xSize) / 2
        val l = (height - ySize) / 2
        drawTexturedModalRect(k, l, 0, 0, xSize, ySize)
    }

    fun drawStack(mc: Minecraft, item: ItemStack?, x: Int, y: Int) {
        if (item != null) {
            GL11.glEnable(GL11.GL_LIGHTING)
            val prevZ = itemRenderer.zLevel
            itemRenderer.zLevel = 200f
            itemRenderer.renderItemAndEffectIntoGUI(fontRenderer, mc.renderEngine, item, x, y)
            itemRenderer.renderItemOverlayIntoGUI(fontRenderer, mc.renderEngine, item, x, y)
            itemRenderer.zLevel = prevZ
            GL11.glDisable(GL11.GL_LIGHTING)
        }
    }

    override fun initGui() {
        super.initGui()
        //важно: id кнопок напрямую связаны с логикой выбора шаблона
    }

    override fun actionPerformed(button: GuiButton) {

    }

    val itemRenderer: RenderItem
        get() = itemRender
    val fontRenderer: FontRenderer
        get() = fontRendererObj

    companion object {
        private val IMAGE_URL = ResourceLocation(Kharium.MOD_ID, "textures/gui/assembly_table.png")
        const val energyClientPixels = 70.0
    }
}