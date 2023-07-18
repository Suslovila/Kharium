package com.suslovila.mixin;

import com.suslovila.client.render.item.ItemCrystallizedAntiMatter;
import com.suslovila.common.block.ModBlocks;
import com.suslovila.mixinUtils.MixinTileNodeProvider;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemCarrotOnAStick;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.api.nodes.NodeType;
import thaumcraft.api.research.ScanResult;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.config.Config;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.lib.research.ScanManager;
import thaumcraft.common.tiles.TileNode;

import java.util.Iterator;
import java.util.List;

import static com.suslovila.mixinUtils.MixinStaticMethods.startNodeTransformation;
import static com.suslovila.utils.SUSUtils.*;

@Mixin(value = TileNode.class, remap = false)
public abstract class MixinTileNode extends TileThaumcraft implements MixinTileNodeProvider {

    @Shadow(remap = false)
    public abstract AspectList getAspects();
    @Shadow public abstract boolean takeFromContainer(Aspect aspect, int amount);
    private int transformationAspectSize;
    private int transformationTimer = -1;
    public int getTransformationTimer(){return transformationTimer;}
    public void addTime(int n){transformationTimer += n;}

    public int getTransformationAspectSize(){return transformationAspectSize;}
    public void setTransformationAspectSize(int n){transformationAspectSize = n;}


    @Inject(remap = false, method = "handleHungryNodeFirst", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;attackEntityFrom(Lnet/minecraft/util/DamageSource;F)Z",  shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    public void hungryNodeTransformationInvoker(boolean change, CallbackInfoReturnable<Boolean> cir, List ents, Iterator i$, Object ent, Entity eo, double d) {

     if(!eo.isEntityAlive() && eo instanceof EntityItem && ((EntityItem)eo).getEntityItem().getItem() instanceof ItemCrystallizedAntiMatter) startNodeTransformation((TileNode)(Object)this);
    }
    @Inject(remap = false, method = "handleHungryNodeFirst", at = @At("HEAD"), cancellable = true)
    public void hungryNodeAnimationPreventer(boolean change, CallbackInfoReturnable<Boolean> cir) {
        if(getTransformationTimer() > 120) cir.setReturnValue(false);

    }
    @Inject(remap = false, method = "handleHungryNodeSecond", at = @At("HEAD"), cancellable = true)
    public void hungryNodeEatingPreventer(boolean change, CallbackInfoReturnable<Boolean> cir) {
        if(getTransformationTimer() > 120) cir.setReturnValue(false);

    }
    @Inject(remap = false, method = "updateEntity", at = @At(value = "TAIL"))
    public void addTimer(CallbackInfo ci) {
        transformationTimer += (transformationTimer != -1 ? 1 : 0);
        transformationTimer = transformationTimer % (halfConvertionTime * 2);
        if (transformationTimer < halfConvertionTime && transformationTimer != -1) {
            int iterationAmount = (random.nextBoolean() ? 1 : 0) + getTransformationAspectSize() / (halfConvertionTime);
            for (int i = 0; i < iterationAmount; i++) {
                Aspect[] aspects = getAspects().getAspects();
                if (aspects.length != 0) {
                    Aspect aspect = aspects[random.nextInt(aspects.length)];
                    if (this.getAspects().getAmount(aspect) != 0)
                        this.takeFromContainer(aspects[random.nextInt(aspects.length)], 1);
                }
            }
        }
        if(transformationTimer == halfConvertionTime){
            Aspect[] aspects = this.getAspects().getAspects();
            for(Aspect aspect : aspects) this.getAspects().remove(aspect);
            this.worldObj.setBlock(this.xCoord, this.yCoord, this.zCoord, ModBlocks.ANTI_NODE);
        }
    }



    @Inject(remap = false, method = "writeCustomNBT", at = @At(value = "TAIL"))
    public void writeToNBT(NBTTagCompound nbttagcompound, CallbackInfo ci) {
        nbttagcompound.setInteger("transformationTimer", getTransformationTimer());
    }
    @Inject(remap = false, method = "readCustomNBT", at = @At(value = "TAIL"))
    public void readFromNBT(NBTTagCompound nbttagcompound, CallbackInfo ci) {
        transformationTimer = nbttagcompound.getInteger("transformationTimer");
    }
}
