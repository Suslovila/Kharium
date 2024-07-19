package com.suslovila.kharium.client.gui


import com.suslovila.kharium.Kharium
import com.suslovila.kharium.api.implants.RuneUsingItem
import com.suslovila.kharium.api.rune.RuneType
import com.suslovila.kharium.common.item.ItemRune
import com.suslovila.kharium.common.container.ContainerRuneInstaller
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

class GuiRuneInstaller(
    val tile: TileRuneInstaller,
    val player: EntityPlayer
) :
    GuiContainer(ContainerRuneInstaller(tile, player)) {
    init {
        xSize = 481
        ySize = 256
    }

    // do not even ask me wtf is going on here

    override fun initGui() {
        super.initGui()

        val center = xSize / 2
        val offset = 34
        val runeAmount = RuneType.values().size
        val startXPosition = (center - (runeAmount.toFloat() / 2) * offset).toInt() + 12
        var xOffset = -offset - 14
        val startYPosition = guiTop + 120

        val buttonIndexes = Array(RuneType.values().size) { index -> Pair(index * 2, index * 2 + 1) }
        for (buttonIndexesPair in buttonIndexes) {
            xOffset += offset + 6
            val removeButton = GuiButton(
                buttonIndexesPair.first, startXPosition + xOffset, startYPosition, 4, 4, "-"
            )
            val addButton = GuiButton(
                buttonIndexesPair.second, startXPosition + xOffset + 6, startYPosition, 4, 4, "+"
            )
            buttonList.add(addButton)
            buttonList.add(removeButton)

        }
    }

    override fun actionPerformed(button: GuiButton) {
        KhariumPacketHandler.INSTANCE.sendToServer(
            PacketRuneInstallerButtonClicked(
                tile.getPosition(),
                RuneType.values()[button.id / 2],
                button.id % 2 != 0
            )
        )
    }


    override fun drawGuiContainerBackgroundLayer(f: Float, x: Int, y: Int) {
        GL11.glColor4f(1f, 1f, 1f, 1f)
        Minecraft.getMinecraft().textureManager.bindTexture(backTexture)
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, 225, ySize)

        renderRunes()
    }


    fun renderRunes() {
        val stack = tile.inventory.getStackInSlot(0)
        val center = xSize / 2
        val runeAmount = RuneType.values().size
        val offset = 40
        val startXPosition = (center - (runeAmount.toFloat() / 2) * offset).toInt() + 12
        for (i in 0 until runeAmount) {
            SusGraphicHelper.drawStack(
                itemRender,
                ItemStack(ItemRune, 1, i),
                startXPosition + i * offset,
                100,
                200f
            )
            val runeType = RuneType.values()[i]
            if (stack != null) {
                val currentAmount = RuneUsingItem.getRuneAmountOfType(stack, runeType)
                fontRendererObj.drawString(currentAmount.toString(), startXPosition + i * offset + 5, 84, 7877878)
            }
        }

    }

    companion object {

        private val backTexture = ResourceLocation(Kharium.MOD_ID, "textures/gui/runeInstaller.png")
    }
}