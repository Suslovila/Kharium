package com.suslovila.kharium.common.event

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.api.implants.ImplantType
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.util.IIcon
import net.minecraftforge.client.event.TextureStitchEvent

object Icons {
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    fun onItemIconRegister(event: TextureStitchEvent) {
        val iconRegister = event.map
        if (iconRegister.textureType == 1) {
            for (type in ImplantType.values()) {
                ImplantType.iconLocations.add(
                    type.ordinal, iconRegister.registerIcon(Kharium.MOD_ID + ":$type")
                )
            }
        }
    }
}
