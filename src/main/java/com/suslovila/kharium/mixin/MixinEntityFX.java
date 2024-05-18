package com.suslovila.kharium.mixin;

import com.suslovila.kharium.mixinUtils.IFxScaleProvider;
import net.minecraft.client.particle.EntityFX;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = EntityFX.class)
public class MixinEntityFX implements IFxScaleProvider {
    @Shadow
    protected float particleScale;


    @Override
    public void setScale(float scale) {
        particleScale = scale;
    }

    @Override
    public void setMaxParticleAge(int age) {
    }
}
