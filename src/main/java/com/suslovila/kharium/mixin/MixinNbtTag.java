package com.suslovila.kharium.mixin;

import com.suslovila.kharium.mixinUtils.IMixinNbtTagProvider;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.HashMap;
import java.util.Map;

@Mixin(value = NBTTagCompound.class, remap = false)
public class MixinNbtTag implements IMixinNbtTagProvider {
    @Shadow
    private Map tagMap;

    @Override
    public HashMap tagMap() {
        return (HashMap) tagMap;
    }
}
