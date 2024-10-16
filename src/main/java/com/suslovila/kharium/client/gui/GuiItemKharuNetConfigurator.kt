package com.suslovila.kharium.client.gui

import com.suslovila.kharium.common.item.ItemConfigurator.CURRENT_PRIORITY_NBT
import com.suslovila.kharium.common.sync.KhariumPacketHandler
import com.suslovila.kharium.common.sync.PacketItemKharuNetConfigurator
import com.suslovila.kharium.utils.KhariumSusNBTHelper.getOrCreateInteger
import com.suslovila.kharium.utils.KhariumSusNBTHelper.getOrCreateTag
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.GuiScreen
import net.minecraft.item.ItemStack
import java.awt.Color

class GuiItemKharuNetConfigurator(val itemIn: ItemStack) : GuiScreen() {

    private var currentPriority: Int = itemIn.getOrCreateTag().getOrCreateInteger(CURRENT_PRIORITY_NBT, 0)

    /** The X size of the inventory window in pixels.  */
    protected var xTextureSize = 511

    /** The Y size of the inventory window in pixels.  */
    protected var yTextureSize = 367

    protected var guiLeft: Int = 0
    protected var guiTop: Int = 0


    override fun initGui() {
        super.initGui()
        xTextureSize = 256
        yTextureSize = 198
        guiLeft = (width - this.xTextureSize) / 2
        guiTop = (height - this.yTextureSize) / 2
        buttonList.clear()

        buttonList.add(
            GuiButton(
                1,
                guiLeft + 105,
                guiTop + 30,
                4,
                4,
                "+"
            )
        )
        buttonList.add(
            GuiButton(
                0,
                guiLeft + 100,
                guiTop + 30,
                4,
                4,
                "-"
            )
        )
    }

    override fun drawScreen(x: Int, y: Int, p_73863_3_: Float) {
        super.drawScreen(x, y, p_73863_3_)
        fontRendererObj.drawString(currentPriority.toString(), guiLeft + 103, guiTop + 20, Color.white.rgb)
    }

    override fun drawBackground(p_146278_1_: Int) {

    }

    override fun actionPerformed(button: GuiButton) {
        when (button.id) {
            0 -> {
                currentPriority -= 1
            }
            else -> {
                currentPriority += 1
            }
        }
    }

    override fun updateScreen() {
        super.updateScreen()
    }


    override fun onGuiClosed() {
        super.onGuiClosed()
        KhariumPacketHandler.INSTANCE.sendToServer(
            PacketItemKharuNetConfigurator(
                currentPriority
            )
        )
    }
}

