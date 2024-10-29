package com.suslovila.kharium.mixin;

import com.suslovila.kharium.common.item.implants.ImplantOverthinker;
import com.suslovila.kharium.extendedData.KhariumPlayerExtendedData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thaumcraft.api.research.ResearchCategories;
import thaumcraft.common.lib.research.ResearchManager;

@Mixin(value = ResearchManager.class, remap = false)
public abstract class MixinResearch {


    @Inject(method = "completeResearch", at = @At(value = "INVOKE", target = "Lthaumcraft/common/lib/research/ResearchManager;scheduleSave(Lnet/minecraft/entity/player/EntityPlayer;)V"))
    public void tryOverThink(EntityPlayer player, String key, CallbackInfo ci) {
        KhariumPlayerExtendedData data = KhariumPlayerExtendedData.Companion.get(player);
        if (data != null && !player.worldObj.isRemote && player instanceof EntityPlayerMP) {
            data.getImplantStorage().forEachImplant((index, implant) ->
                    {
                        if (implant != null && implant.getItem() instanceof ImplantOverthinker) {
                            ImplantOverthinker.overThink((EntityPlayerMP) player, index, implant, ResearchCategories.getResearch(key));
                        }
                        return null;
                    }
            );
        }
    }
}
