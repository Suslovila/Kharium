package com.suslovila.mixin;

import com.suslovila.utils.mixins.Tesselator;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.EntityPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Tessellator.class)
public class MixinTesselator implements Tesselator {
    @Shadow(remap = false)
    private boolean isDrawing;
public boolean isDrawing(){
    return this.isDrawing;
}
}
