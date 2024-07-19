package com.suslovila.kharium.client.gui


import com.suslovila.kharium.Kharium
import com.suslovila.kharium.api.implants.ImplantType
import com.suslovila.kharium.api.implants.ImplantType.Companion.slotAmount
import com.suslovila.kharium.common.block.container.DefaultContainer
import com.suslovila.kharium.common.container.ContainerImplantHolder
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.renderer.OpenGlHelper
import net.minecraft.client.renderer.RenderHelper
import net.minecraft.client.renderer.entity.RenderManager
import net.minecraft.client.renderer.texture.TextureMap
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.inventory.Container
import net.minecraft.inventory.Slot
import net.minecraft.util.EnumChatFormatting
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11
import thaumcraft.client.lib.UtilsFX

class GuiImplantInstaller(
    val player: EntityPlayer
) :
    GuiContainer(ContainerImplantHolder(player)) {

    var mouseX = 0f
    var mouseY = 0f

    init {
        xSize = 481
        ySize = 256
    }

    override fun initGui() {
        super.initGui()
    }

    override fun actionPerformed(button: GuiButton) {

    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        super.drawScreen(mouseX, mouseY, partialTicks)
        this.mouseX = mouseX.toFloat()
        this.mouseY = mouseY.toFloat()
    }

    override fun drawGuiContainerBackgroundLayer(f: Float, x: Int, y: Int) {
        GL11.glColor4f(1f, 1f, 1f, 1f)
        Minecraft.getMinecraft().textureManager.bindTexture(textureRight)
        drawTexturedModalRect(guiLeft + 256, guiTop, 0, 0, 225, ySize)
        Minecraft.getMinecraft().textureManager.bindTexture(textureLeft)
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, 256, ySize)
        renderPlayerModel(
            guiLeft + xSize / 2,
            guiTop + ySize / 2,
            50,
            0f,
            0f,
            player
        )
//        renderImplants()
    }

    override fun drawSlot(slotIn: Slot) {
        val i = slotIn.xDisplayPosition
        val j = slotIn.yDisplayPosition
        var stackInSlot = slotIn.stack
        var flag = false
        val itemstack1 = mc.thePlayer.inventory.itemStack
        var s: String? = null

        if (slotIn === clickedSlot && draggedStack != null && isRightMouseClick && stackInSlot != null) {
            stackInSlot = stackInSlot.copy()
            stackInSlot.stackSize /= 2
        } else if (dragSplitting && dragSplittingSlots.contains(slotIn) && itemstack1 != null) {
            if (dragSplittingSlots.size == 1) {
                return
            }
            if (Container.canAddItemToSlot(slotIn, itemstack1, true) && inventorySlots.canDragIntoSlot(slotIn)) {
                stackInSlot = itemstack1.copy()
                flag = true
                Container.computeStackSize(
                    dragSplittingSlots,
                    dragSplittingLimit,
                    stackInSlot,
                    if (slotIn.stack == null) 0 else slotIn.stack.stackSize
                )
                if (stackInSlot.stackSize > stackInSlot.maxStackSize) {
                    s = EnumChatFormatting.YELLOW.toString() + "" + stackInSlot.maxStackSize
                    stackInSlot.stackSize = stackInSlot.maxStackSize
                }
                if (stackInSlot.stackSize > slotIn.slotStackLimit) {
                    s = EnumChatFormatting.YELLOW.toString() + "" + slotIn.slotStackLimit
                    stackInSlot.stackSize = slotIn.slotStackLimit
                }
            } else {
                dragSplittingSlots.remove(slotIn)
                updateDragSplitting()
            }
        }

        zLevel = 100.0f
        itemRender.zLevel = 100.0f

        val iicon = slotIn.backgroundIconIndex
        if (iicon != null) {
            GL11.glDisable(GL11.GL_LIGHTING)
            GL11.glEnable(GL11.GL_BLEND) // Forge: Blending needs to be enabled for this.
            mc.textureManager.bindTexture(TextureMap.locationItemsTexture)
            drawTexturedModelRectFromIcon(i, j, iicon, 16, 16)
            GL11.glDisable(GL11.GL_BLEND) // Forge: And clean that up
            GL11.glEnable(GL11.GL_LIGHTING)

        }

        if (flag) {
            drawRect(i, j, i + 16, j + 16, -2130706433)
        }
        GL11.glEnable(GL11.GL_DEPTH_TEST)
        itemRender.renderItemAndEffectIntoGUI(fontRendererObj, mc.textureManager, stackInSlot, i, j)
        itemRender.renderItemOverlayIntoGUI(fontRendererObj, mc.textureManager, stackInSlot, i, j, s)

        itemRender.zLevel = 0.0f
        zLevel = 0.0f
    }

    fun renderImplants() {
        val implantTypes = ImplantType.values()
        val halfIndex = implantTypes.size / 2
        var index = 0

        var yOffset = 10
        for (implantTypeIndex in implantTypes.indices) {
            val type = implantTypes[implantTypeIndex]
            val isLeftSide = (implantTypeIndex % 2 == 0)
            UtilsFX.bindTexture(slotTextures[type.ordinal])
            val offsetForTypeSlots = DefaultContainer.oneSlotStep * type.slotAmount * (if (isLeftSide) -1 else 0)
            for (typeIndex in 0 until type.slotAmount) {
                drawTexturedModalRect(
                    120 + (if (!isLeftSide) 200 else 0) + typeIndex * DefaultContainer.oneSlotStep + offsetForTypeSlots,
                    yOffset,
                    0, 0, 16, 16
                )
            }
            if (implantTypeIndex % 2 != 0) yOffset += 18
        }
    }

    fun renderPlayerModel(x: Int, y: Int, scale: Int, yaw: Float, pitch: Float, playerdrawn: EntityLivingBase) {
        GL11.glEnable(2903)
        GL11.glPushMatrix()
        GL11.glTranslatef(x.toFloat(), y.toFloat(), 50.0f)
        GL11.glScalef((-scale).toFloat(), scale.toFloat(), scale.toFloat())
        GL11.glRotatef(180.0f, 0.0f, 0.0f, 1.0f)
        val f2 = playerdrawn.renderYawOffset
        val f3 = playerdrawn.rotationYaw
        val f4 = playerdrawn.rotationPitch
        val f5 = playerdrawn.prevRotationYawHead
        val f6 = playerdrawn.rotationYawHead
        GL11.glRotatef(135.0f, 0.0f, 1.0f, 0.0f)
        RenderHelper.enableStandardItemLighting()
        GL11.glRotatef(-135.0f, 0.0f, 1.0f, 0.0f)
        GL11.glRotatef(-Math.atan((pitch / 40.0f).toDouble()).toFloat() * 20.0f, 1.0f, 0.0f, 0.0f)
        playerdrawn.renderYawOffset = Math.atan((yaw / 40.0f).toDouble()).toFloat() * 20.0f
        playerdrawn.rotationYaw = Math.atan((yaw / 40.0f).toDouble()).toFloat() * 40.0f
        playerdrawn.rotationPitch = -Math.atan((pitch / 40.0f).toDouble()).toFloat() * 20.0f
        playerdrawn.rotationYawHead = playerdrawn.rotationYaw
        playerdrawn.prevRotationYawHead = playerdrawn.rotationYaw
        GL11.glTranslatef(0.0f, playerdrawn.yOffset, 0.0f)
        RenderManager.instance.playerViewY = 180.0f
        RenderManager.instance.renderEntityWithPosYaw(playerdrawn, 0.0, 0.0, 0.0, 0.0f, 1.0f)
        playerdrawn.renderYawOffset = f2
        playerdrawn.rotationYaw = f3
        playerdrawn.rotationPitch = f4
        playerdrawn.prevRotationYawHead = f5
        playerdrawn.rotationYawHead = f6
        GL11.glPopMatrix()
        RenderHelper.disableStandardItemLighting()
        GL11.glDisable(32826)
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit)
        GL11.glDisable(3553)
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit)
    }

    companion object {
        val slotTextures = Array(ImplantType.typeAmount) { i ->
            ResourceLocation(
                Kharium.MOD_ID,
                "textures/gui/implantSlots/${ImplantType.values()[i]}.png"
            )
        }

        private val IMAGE_URL = ResourceLocation(Kharium.MOD_ID, "textures/gui/assembly_table.png")
        private val textureLeft = ResourceLocation(Kharium.MOD_ID, "textures/gui/DraconicChestLeft.png")
        private val textureRight = ResourceLocation(Kharium.MOD_ID, "textures/gui/DraconicChestRight.png")
    }
}