package com.suslovila.kharium.mixin;

import com.suslovila.kharium.utils.config.Config;
import com.suslovila.kharium.common.worldSavedData.KharuInfluenceHandler;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.common.items.wands.ItemWandCasting;

@Mixin(value = ItemWandCasting.class)
public abstract class MixinWand {

    @Shadow(remap = false) public abstract int getVis(ItemStack is, Aspect aspect);

    @ModifyVariable(method = "addVis", at = @At(value = "HEAD"), remap = false, argsOnly = true)
    public int changeAmount(int amount, ItemStack stack) {
        System.out.println("decreasing");
        int radiance = KharuInfluenceHandler.INSTANCE.getKharuAmount(stack);
        double decr = Math.max(0, 1 - ((double) radiance) / Config.kharuItemDestructionBorder);
        System.out.println(decr);
        return (int) (amount * decr);
    }
//    @Inject(method = "addRealVis", at = @At(value = "TAIL"), remap = false)
//    public void addRealVisScal(ItemStack stack, Aspect aspect, int amount, boolean doit, CallbackInfoReturnable<Integer> cir) {
//        System.out.println("made: " + amount);
//    }
//    @Inject(method = "addRealVis", at = @At(value = "TAIL"), remap = false)
//    public void addRealVisScal(ItemStack stack, Aspect aspect, int amount, boolean doit, CallbackInfoReturnable<Integer> cir) {
//        System.out.println("made: " + amount);
//    }

//    @ModifyVariable(method = "addVis", at = @At(value = "HEAD"), argsOnly = true)
//    public int addVisScale(ItemStack stack, Aspect aspect, int amount, boolean doit) {
//        return amount * KharuInfluenceHandler.INSTANCE.getKharuAmount(stack) / Config.kharuItemDestructionBorder;
//    }
//    @ModifyArg(method = "addVis", at = @At(value = "TAIL"), remap = false)
//    public void addVisScal(ItemStack stack, Aspect aspect, int amount, boolean doit, CallbackInfoReturnable<Integer> cir) {
//        if (!aspect.isPrimal()) {
//            return 0;
//        } else {
//            int storeAmount = this.getVis(is, aspect) + amount * 100;
//            int leftover = Math.max(storeAmount - this.getMaxVis(is), 0);
//            if (doit) {
//                this.storeVis(is, aspect, Math.min(storeAmount, this.getMaxVis(is)));
//            }
//
//            return leftover / 100;
//        }
//    }

}
