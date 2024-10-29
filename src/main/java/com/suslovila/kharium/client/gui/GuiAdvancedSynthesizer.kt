package com.suslovila.kharium.client.gui

import com.suslovila.kharium.common.multiStructure.synthesizer.advancedSynthesizer.TileAdvancedSynthesizerCore
import com.suslovila.kharium.common.sync.KhariumPacketHandler
import com.suslovila.kharium.common.sync.PacketGuiAdvancedSynthesizerAddRequest
import com.suslovila.kharium.common.sync.PacketGuiAdvancedSynthesizerChangeAspectAmount
import com.suslovila.kharium.utils.SusGraphicHelper
import com.suslovila.kharium.utils.getPosition
import com.suslovila.sus_multi_blocked.client.gui.GuiMultiBlockFormer
import com.suslovila.sus_multi_blocked.utils.SerialiseType
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.FontRenderer
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.GuiTextField
import org.lwjgl.input.Keyboard
import org.lwjgl.opengl.GL11
import thaumcraft.api.aspects.Aspect
import thaumcraft.client.lib.UtilsFX
import java.awt.Color

class GuiAdvancedSynthesizer(val synthesizer: TileAdvancedSynthesizerCore) : GuiScreen() {


    protected var xTextureSize = 511

    protected var yTextureSize = 367

    protected var guiLeft: Int = 0
    protected var guiTop: Int = 0

    companion object {
        val buttonAssociations by lazy { Aspect.aspects.values.filter { !it.isPrimal }.toList() }
    }

    lateinit var requiredAmount: GuiTextField

    fun getFontRenderer(): FontRenderer = this.fontRendererObj
    override fun initGui() {
        super.initGui()
        xTextureSize = 256
        yTextureSize = 198
        guiLeft = (width - this.xTextureSize) / 2
        guiTop = (height - this.yTextureSize) / 2
        buttonList.clear()
        val standartAmountOfAspectsCanShow = 45
        val basicOffsetBetweenButtons = 4
        val basicButtonSize = 24
        val availableSpaceOnX = (basicOffsetBetweenButtons + basicButtonSize) * 9
        val availableSpaceOnY = (basicOffsetBetweenButtons + basicButtonSize) * 5

        val currentAmountOfAspectsInLine = 9 * standartAmountOfAspectsCanShow / buttonAssociations.size
        val currentButtonSize = basicButtonSize * standartAmountOfAspectsCanShow / buttonAssociations.size
        val currentButtonOffset = basicOffsetBetweenButtons * standartAmountOfAspectsCanShow / buttonAssociations.size
        var lineCounter = 1
        var rowCounter = 0
        val yOffset = 50
        buttonAssociations.forEachIndexed { index, aspect ->
            buttonList.add(
                GuiButtonAspectAdvanced(
                    index,
                    guiLeft + rowCounter * (currentButtonSize + currentButtonOffset),
                    guiTop + yOffset + lineCounter * (currentButtonSize + currentButtonOffset),
                    currentButtonSize,
                    currentButtonSize,
                    ""
                )
            )
            rowCounter += 1
            if (rowCounter % currentAmountOfAspectsInLine == 0 ||
                rowCounter * (currentButtonSize + currentButtonOffset) > guiLeft + width
            ) {
                rowCounter = 0
                lineCounter += 1
            }
        }
        addRequestButtons()
        requiredAmount = GuiTextField(fontRendererObj, guiLeft + 100, guiTop + 13, 50, 12)
        setDefaultTextSettings(requiredAmount)
    }

    private fun addRequestButtons() {
        val basicOffsetBetweenButtons = 4
        val basicButtonSize = 24

        for (i in 0..synthesizer.currentRequestCapacity) {
            buttonList.add(
                GuiButtonAspectRequest(
                    buttonAssociations.size + i,
                    i,
                    this,
                    guiLeft + i * (basicButtonSize + basicOffsetBetweenButtons),
                    guiTop + 20,
                    basicButtonSize,
                    basicButtonSize,
                    ""
                )
            )
        }
    }

    override fun drawScreen(x: Int, y: Int, p_73863_3_: Float) {
        super.drawScreen(x, y, p_73863_3_)
        requiredAmount.drawTextBox()

    }

    override fun drawBackground(p_146278_1_: Int) {

    }

    override fun actionPerformed(button: GuiButton) {
        if (button is GuiButtonAspectAdvanced) {
            val castedAmount = SerialiseType.INTEGER.cast(requiredAmount.text) as? Int ?: return
            val clickedAspect = buttonAssociations[button.id]
            KhariumPacketHandler.INSTANCE.sendToServer(
                PacketGuiAdvancedSynthesizerAddRequest(
                    clickedAspect,
                    castedAmount,
                    synthesizer.getPosition()
                )
            )
        }
        if (button is GuiButtonAspectRequest) {
        }
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


    override fun mouseClicked(x: Int, y: Int, mouseButton: Int) {
        super.mouseClicked(x, y, mouseButton)
        if (isMouseOnField(requiredAmount, x - guiLeft, y - guiTop)) {
            requiredAmount.isFocused = true
            return
        }


        buttonList.forEach { button ->
            if (button !is GuiButton) return@forEach
            if (isMouseOnButton(button, x, y) && button is GuiButtonAspectRequest) {
                if (mouseButton == 0 || mouseButton == 1) { // ЛКМ
                    val increaseOrDecrease = if (mouseButton == 0) 1 else -1
                    val areControlsPressed =
                        (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) || Keyboard.isKeyDown(Keyboard.KEY_RCONTROL))
                    val areShiftsPressed =
                        (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))

                    val addedAmount = (if (areShiftsPressed) {
                        16
                    } else if (areControlsPressed) {
                        32
                    } else {
                        1
                    }) * increaseOrDecrease

                    KhariumPacketHandler.INSTANCE.sendToServer(PacketGuiAdvancedSynthesizerChangeAspectAmount(button.requestId, addedAmount, synthesizer.getPosition()))

                }
            }
        }
    }


    fun isMouseOnField(field: GuiTextField, xClick: Int, yClick: Int): Boolean =
        ((xClick + guiLeft) < (field.xPosition + field.width) && (xClick + guiLeft) > field.xPosition)
                && ((yClick + guiTop) < (field.yPosition + field.height) && (yClick + guiTop) > field.yPosition)


    fun isMouseOnButton(button: GuiButton, xClick: Int, yClick: Int): Boolean =
        ((xClick + guiLeft) < (button.xPosition + button.width) && (xClick + guiLeft) > button.xPosition)
                && ((yClick + guiTop) < (button.yPosition + button.height) && (yClick + guiTop) > button.yPosition)


    override fun onGuiClosed() {
        super.onGuiClosed()
    }

    override fun doesGuiPauseGame(): Boolean = false
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

class GuiButtonAspectRequest(
    id: Int,
    val requestId: Int,
    val gui: GuiAdvancedSynthesizer,
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
        val request = try {
            gui.synthesizer.aspectRequestQueue[this.requestId]
        }
        catch (exception: IndexOutOfBoundsException) {
            return
        }

        GL11.glPushMatrix()


        val alpha = 1.0f
        UtilsFX.bindTexture(request.aspect.image)
        SusGraphicHelper.bindColor(request.aspect.color, alpha, 1.0f)
        GL11.glTranslated(
            this.xPosition.toDouble() + this.width.toDouble() / 2,
            this.yPosition.toDouble() + this.height.toDouble() / 2,
            0.0
        )
        GL11.glRotated(-90.0, 0.0, 0.0, 1.0)
        SusGraphicHelper.drawFromCenter(12.0)
        GL11.glPopMatrix()

        this.drawString(this.gui.getFontRenderer(),request.amount.toString(), this.xPosition, this.yPosition, Color.white.rgb)
    }
}
