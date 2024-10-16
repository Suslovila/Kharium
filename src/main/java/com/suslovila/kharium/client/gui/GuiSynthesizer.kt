package com.suslovila.kharium.client.gui

import com.suslovila.kharium.common.multiStructure.synthesizer.simpleSynthesizer.TileSynthesizerCore
import com.suslovila.kharium.common.sync.KhariumPacketHandler
import com.suslovila.kharium.common.sync.PacketItemPortableContainer
import com.suslovila.kharium.utils.SusGraphicHelper
import com.suslovila.sus_multi_blocked.client.gui.GuiMultiBlockFormer
import com.suslovila.sus_multi_blocked.utils.SerialiseType
import com.suslovila.sus_multi_blocked.utils.getTile
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.GuiTextField
import org.lwjgl.opengl.GL11
import thaumcraft.api.aspects.Aspect
import thaumcraft.client.lib.UtilsFX

class GuiSynthesizer(val synthesizer: TileSynthesizerCore) : GuiScreen() {


    protected var xTextureSize = 511

    protected var yTextureSize = 367

    protected var guiLeft: Int = 0
    protected var guiTop: Int = 0

    // obviously, you should not fire this field until "Aspect" class is fully initialised
    companion object {
        val buttonAssociations by lazy { Aspect.aspects.values.toList() }
    }

    lateinit var requiredAmount: GuiTextField

    var currentAspect: Aspect? = synthesizer.currentProducingAspect

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
        buttonAssociations.forEachIndexed { index, aspect ->
            buttonList.add(
                GuiButtonAspect(
                    this,
                    index,
                    guiLeft + rowCounter * (buttonSize + offsetBetweenButtons),
                    guiTop + lineCounter * (buttonSize + offsetBetweenButtons),
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
        val clickedAspect = buttonAssociations[button.id]
        currentAspect = clickedAspect
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
        val castedAmount = SerialiseType.INTEGER.cast(requiredAmount.text) as? Int ?: 0
        currentAspect?.let {
            KhariumPacketHandler.INSTANCE.sendToServer(
                PacketItemPortableContainer(
                    it.tag,
                    castedAmount
                )
            )
        }
    }
}

class GuiButtonAspect(
    val gui: GuiSynthesizer,
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
        val alpha = if (aspect == gui.currentAspect) 1.0f else 0.35f
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

//        GL11.glPushMatrix()
//        val aspectAmount = ItemPortableAspectContainer.getStoredAspects(gui.itemIn).getAmount(aspect).toString()
//        GL11.glTranslated((-4.0 * aspectAmount.length) / 2, 0.0, 1.0)
//        GL11.glScaled(0.7, 0.7, 0.7)
//        Minecraft.getMinecraft().fontRendererObj.drawString(aspectAmount, 0, 0, Color.white.rgb)
//        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
//        GL11.glPopMatrix()


        GL11.glPopMatrix()

    }
}
