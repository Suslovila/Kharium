package com.suslovila.kharium.client

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.api.implants.ImplantType
import com.suslovila.kharium.client.gui.GuiImplants
import com.suslovila.kharium.common.sync.KhariumPacketHandler
import com.suslovila.kharium.common.sync.implant.PacketEnableImplantSync
import com.suslovila.kharium.extendedData.KhariumPlayerExtendedData
import com.suslovila.kharium.utils.SusUtils
import cpw.mods.fml.client.registry.ClientRegistry
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent
import net.minecraft.client.Minecraft
import net.minecraft.client.settings.KeyBinding
import net.minecraftforge.common.MinecraftForge
import org.lwjgl.input.Keyboard
import java.util.*


object KeyHandler {


    private val nextImplantTrigger = KeyBinding("next implant", Keyboard.KEY_V, Kharium.MOD_ID + ".key.category")
    private val previousImplantTrigger =
        KeyBinding("previous implant", Keyboard.KEY_C, Kharium.MOD_ID + ".key.category")
    private val firstAbilityTrigger = KeyBinding("use first ability", Keyboard.KEY_F, Kharium.MOD_ID + ".key.category")
    private val secondAbilityTrigger =
        KeyBinding("use second ability", Keyboard.KEY_G, Kharium.MOD_ID + ".key.category")
    private val thirdAbilityTrigger =
        KeyBinding("use third ability", Keyboard.KEY_P, Kharium.MOD_ID + ".key.category")
    private val renderImplants =
        KeyBinding("disable implant render", Keyboard.KEY_I, Kharium.MOD_ID + ".key.category")

    fun register() {
        ClientRegistry.registerKeyBinding(nextImplantTrigger)
        ClientRegistry.registerKeyBinding(previousImplantTrigger)
        ClientRegistry.registerKeyBinding(firstAbilityTrigger)
        ClientRegistry.registerKeyBinding(secondAbilityTrigger)
        ClientRegistry.registerKeyBinding(thirdAbilityTrigger)
        ClientRegistry.registerKeyBinding(renderImplants)

        FMLCommonHandler.instance().bus().register(this)
        MinecraftForge.EVENT_BUS.register(this)
    }

    @SubscribeEvent
    fun onKeyInput(event: KeyInputEvent?) {
        checkImplantSwitch()
        if (firstAbilityTrigger.isPressed) {
            KhariumPacketHandler.INSTANCE.sendToServer(PacketEnableImplantSync(GuiImplants.currentImplantSlotId, 0))
        }
        if (secondAbilityTrigger.isPressed) {
            KhariumPacketHandler.INSTANCE.sendToServer(PacketEnableImplantSync(GuiImplants.currentImplantSlotId, 1))
        }
        if (renderImplants.isPressed) {
            GuiImplants.shouldRenderGui = !GuiImplants.shouldRenderGui
        }
    }

    fun checkImplantSwitch() {
        if (nextImplantTrigger.isPressed) {
            Minecraft.getMinecraft()?.thePlayer?.let { player ->
                val data = KhariumPlayerExtendedData.get(player) ?: return@let
                val nextIndex = (GuiImplants.currentImplantSlotId + 1) % ImplantType.slotAmount
                val indexes = SusUtils.getIndicesCycledFrom(nextIndex, ImplantType.slotAmount)
                setNextImplant(data, indexes)
            }
        }

        if (previousImplantTrigger.isPressed) {

            Minecraft.getMinecraft()?.thePlayer?.let { player ->
                val data = KhariumPlayerExtendedData.get(player) ?: return@let
                val previousIndex = GuiImplants.currentImplantSlotId
                val indexes = SusUtils.getIndicesCycledFrom(previousIndex, ImplantType.slotAmount).reversed()
                setNextImplant(data, indexes)
            }
        }
    }

    fun setNextImplant(data: KhariumPlayerExtendedData, indexes: List<Int>) {
        for (index in indexes) {
            val implant = data.implantStorage.getStackInSlot(index)
            if (implant != null) {
                GuiImplants.currentImplantSlotId = index
                return
            }
        }
    }
}