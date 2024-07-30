package com.suslovila.kharium.client.gui


import com.suslovila.kharium.Kharium
import com.suslovila.kharium.api.implants.RuneUsingItem
import com.suslovila.kharium.api.rune.RuneType
import com.suslovila.kharium.common.container.ContainerKharuContainer
import com.suslovila.kharium.common.item.ItemRune
import com.suslovila.kharium.common.container.ContainerRuneInstaller
import com.suslovila.kharium.common.multiStructure.kharuContainer.TileKharuContainer
import com.suslovila.kharium.common.multiStructure.runeInstaller.TileRuneInstaller
import com.suslovila.kharium.common.sync.KhariumPacketHandler
import com.suslovila.kharium.common.sync.PacketRuneInstallerButtonClicked
import com.suslovila.kharium.utils.SusGraphicHelper
import com.suslovila.kharium.utils.getPosition
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11

class GuiKharuContainer(
    val tile: TileKharuContainer,
    val player: EntityPlayer
) :
    GuiContainer(ContainerKharuContainer(tile, player)) {
    init {
        xSize = 481
        ySize = 256
    }


    override fun initGui() {
        super.initGui()
    }


    override fun drawGuiContainerBackgroundLayer(f: Float, x: Int, y: Int) {
        GL11.glColor4f(1f, 1f, 1f, 1f)
        Minecraft.getMinecraft().textureManager.bindTexture(backTexture)
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, 225, ySize)

    }


    companion object {

        private val backTexture = ResourceLocation(Kharium.MOD_ID, "textures/gui/runeInstaller.png")
    }
}