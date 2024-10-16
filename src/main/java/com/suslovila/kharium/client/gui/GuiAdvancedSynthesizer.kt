package com.suslovila.kharium.client.gui

import com.suslovila.kharium.common.multiStructure.synthesizer.TileAdvancedSynthesizerCore
import com.suslovila.kharium.common.multiStructure.synthesizer.simpleSynthesizer.TileSynthesizerCore
import com.suslovila.kharium.common.sync.KhariumPacketHandler
import com.suslovila.kharium.common.sync.PacketGuiAdvancedSynthesizer
import com.suslovila.kharium.common.sync.PacketItemPortableContainer
import com.suslovila.kharium.utils.SusGraphicHelper
import com.suslovila.kharium.utils.getPosition
import com.suslovila.sus_multi_blocked.client.gui.GuiMultiBlockFormer
import com.suslovila.sus_multi_blocked.utils.SerialiseType
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.GuiTextField
import org.lwjgl.opengl.GL11
import thaumcraft.api.aspects.Aspect
import thaumcraft.client.lib.UtilsFX

class GuiAdvancedSynthesizer(val synthesizer: TileAdvancedSynthesizerCore) : GuiScreen() {


    protected var xTextureSize = 511

    protected var yTextureSize = 367

    protected var guiLeft: Int = 0
    protected var guiTop: Int = 0

    companion object {
        val buttonAssociations by lazy { Aspect.aspects.values.filter { !it.isPrimal }.toList() }
    }

    lateinit var requiredAmount: GuiTextField


    override fun initGui() {
        super.initGui()
        xTextureSize = 256
        yTextureSize = 198
        guiLeft = (width - this.xTextureSize) / 2
        guiTop = (height - this.yTextureSize) / 2
        buttonList.clear()

        val buttonSize = 24
        val offsetBetweenButtons = 4
        var lineCounter = 1
        var rowCounter = 0
        val maxAspectAmountInLine = 9
        val yOffset = 50
        buttonAssociations.forEachIndexed { index, aspect ->
            buttonList.add(
                GuiButtonAspectAdvanced(
                    index,
                    guiLeft + rowCounter * (buttonSize + offsetBetweenButtons),
                    guiTop + yOffset + lineCounter * (buttonSize + offsetBetweenButtons),
                    buttonSize,
                    buttonSize,
                    ""
                )
            )
            rowCounter += 1
            if (rowCounter % maxAspectAmountInLine == 0 ||
                rowCounter * (buttonSize + offsetBetweenButtons) > guiLeft + width
            ) {
                rowCounter = 0
                lineCounter += 1
            }
        }
        requiredAmount = GuiTextField(fontRendererObj, guiLeft + 100, guiTop + 13, 50, 12)
        setDefaultTextSettings(requiredAmount)
//        requiredAmount.text = ItemPortableAspectContainer.getRequiredAmount(itemIn).toString()

    }

    override fun drawScreen(x: Int, y: Int, p_73863_3_: Float) {
        super.drawScreen(x, y, p_73863_3_)
        requiredAmount.drawTextBox()

    }

    override fun drawBackground(p_146278_1_: Int) {

    }

    override fun actionPerformed(button: GuiButton) {
        val castedAmount = SerialiseType.INTEGER.cast(requiredAmount.text) as? Int ?: return
        val clickedAspect = buttonAssociations[button.id]
        KhariumPacketHandler.INSTANCE.sendToServer(PacketGuiAdvancedSynthesizer(clickedAspect, castedAmount, synthesizer.getPosition()))
    }

    override fun updateScreen() {
        super.updateScreen()
    }

    private fun setDefaultTextSettings(textField: GuiTextField) {
        textField.setTextColor(-1)
        textField.setDisabledTextColour(-1)
        textField.enableBackgroundDrawing = true
        textField.maxStringLength = 40
    }


    override fun keyTyped(par1: Char, keyId: Int) {
        if (requiredAmount.textboxKeyTyped(par1, keyId)) return
        if (keyId == GuiMultiBlockFormer.enterButtonKeyboardId) {
            if (requiredAmount.isFocused) {
                requiredAmount.isFocused = false
                return
            }
        }
        super.keyTyped(par1, keyId)
    }


    override fun mouseClicked(x: Int, y: Int, par3: Int) {
        super.mouseClicked(x, y, par3)
        if (isMouseOnField(requiredAmount, x - guiLeft, y - guiTop)) {
            requiredAmount.isFocused = true
            return
        }
    }


    fun isMouseOnField(field: GuiTextField, xClick: Int, yClick: Int): Boolean =
        ((xClick + guiLeft) < (field.xPosition + field.width) && (xClick + guiLeft) > field.xPosition)
                && ((yClick + guiTop) < (field.yPosition + field.height) && (yClick + guiTop) > field.yPosition)


    override fun onGuiClosed() {
        super.onGuiClosed()
    }
}

class GuiButtonAspectAdvanced(
    id: Int,
    x: Int,
    y: Int,
    xSize: Int,
    ySize: Int,
    text: String
) : GuiButton(
    id,
    x,
    y,
    xSize,
    ySize,
    text
) {
    override fun drawButton(mc: Minecraft?, mouseX: Int, mouseY: Int) {
        GL11.glPushMatrix()
        val aspect = GuiItemPortableContainer.buttonAssociations[this.id]!!
        val alpha = 1.0f
        UtilsFX.bindTexture(aspect.image)
        SusGraphicHelper.bindColor(aspect.color, alpha, 1.0f)
        GL11.glTranslated(
            this.xPosition.toDouble() + this.width.toDouble() / 2,
            this.yPosition.toDouble() + this.height.toDouble() / 2,
            0.0
        )
        GL11.glRotated(-90.0, 0.0, 0.0, 1.0)
        SusGraphicHelper.drawFromCenter(12.0)
        GL11.glRotated(90.0, 0.0, 0.0, 1.0)

        GL11.glPopMatrix()

    }
}
