package com.suslovila.kharium.client

import GuiImplants
import com.suslovila.kharium.Kharium
import com.suslovila.kharium.api.implants.ImplantType
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


object KeyHandler {


    private val nextImplantTrigger = KeyBinding("next implant", Keyboard.KEY_V, Kharium.MOD_ID + ".key.category")
    private val previousImplantTrigger =
        KeyBinding("previous implant", Keyboard.KEY_C, Kharium.MOD_ID + ".key.category")
    private val firstAbilityTrigger = KeyBinding("use first ability", Keyboard.KEY_F, Kharium.MOD_ID + ".key.category")
    private val secondAbilityTrigger =
        KeyBinding("use second ability", Keyboard.KEY_G, Kharium.MOD_ID + ".key.category")

    fun register() {
        ClientRegistry.registerKeyBinding(nextImplantTrigger)
        ClientRegistry.registerKeyBinding(previousImplantTrigger)
        ClientRegistry.registerKeyBinding(firstAbilityTrigger)
        ClientRegistry.registerKeyBinding(secondAbilityTrigger)

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
    }

    fun checkImplantSwitch() {
        if (nextImplantTrigger.isPressed) {
            Minecraft.getMinecraft()?.thePlayer?.let { player ->
                val data = KhariumPlayerExtendedData.get(player) ?: return@let
                val nextIndex = (GuiImplants.currentImplantSlotId + 1) % ImplantType.slotAmount
                val indexes = SusUtils.getIndicesCycledFrom(nextIndex, ImplantType.slotAmount)
                for (index in indexes) {
                    val implant = data.implantStorage.getStackInSlot(index)
                    if (implant != null) {
                        GuiImplants.currentImplantSlotId = index
                        return
                    }
                }
            }
        }

        if (previousImplantTrigger.isPressed) {

            Minecraft.getMinecraft()?.thePlayer?.let { player ->
                val data = KhariumPlayerExtendedData.get(player) ?: return@let
                val previousIndex = GuiImplants.currentImplantSlotId
                val nextIndex =
                    if (previousIndex == 0) (ImplantType.slotAmount - 1).coerceAtLeast(0)
                    else previousIndex - 1

                val indexes = SusUtils.getIndicesCycledFrom(nextIndex, ImplantType.slotAmount).reversed()
                for (index in indexes) {
                    val implant = data.implantStorage.getStackInSlot(index)
                    if (implant != null) {
                        GuiImplants.currentImplantSlotId = index
                        return
                    }
                }
            }
        }
    }
}