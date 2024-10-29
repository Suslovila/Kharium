package com.suslovila.kharium.mixin;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = Item.class)
public abstract class MixinItem {
    @Inject(method = "addInformation", at = @At(value = "HEAD"))
    public void addingKharuToAll(ItemStack stack, EntityPlayer player, List list, boolean isShift, CallbackInfo ci) {
//            NBTTagCompound tag = SusNBTHelper.INSTANCE.getOrCreateTag(stack);
//            if(tag.hasKey(KharuInfluenceHandler.INSTANCE.getAMOUNT_NBT())) {
//                list.add(EnumChatFormatting.DARK_RED.toString() + "Destructed by: " + (int)(KharuInfluenceHandler.INSTANCE.getKharuLinearAmountPercent(stack) * 100) + "%");
//        }
    }
}
