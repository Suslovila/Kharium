package tech.funkyra.mixins.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import thaumcraft.common.tiles.TileInfusionMatrix;

@Mixin(value = TileInfusionMatrix.class, remap = false)
public class MixinTileInfusionMatrix {
    /**
     * @author
     * @reason
     */
    @Overwrite
    public void craftCycle() {
    }
}
