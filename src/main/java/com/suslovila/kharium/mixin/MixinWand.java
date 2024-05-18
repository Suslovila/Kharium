package com.suslovila.kharium.mixin;

import com.suslovila.kharium.Config;
import com.suslovila.kharium.common.worldSavedData.KharuInfluenceHandler;
import com.suslovila.kharium.utils.SusMathHelper;
import com.suslovila.kharium.utils.SusVec3;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.common.items.wands.ItemWandCasting;

import java.util.List;

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
