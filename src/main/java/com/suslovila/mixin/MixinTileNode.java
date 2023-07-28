package com.suslovila.mixin;

import com.suslovila.common.item.ItemCrystallizedAntiMatter;
import com.suslovila.common.block.ModBlocks;
import com.suslovila.common.block.tileEntity.TileAntiNode;
import com.suslovila.mixinUtils.MixinTileNodeProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.nbt.NBTTagCompound;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import thaumcraft.api.TileThaumcraft;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.tiles.TileNode;

import java.util.Iterator;
import java.util.List;

import static com.suslovila.mixinUtils.MixinStaticMethods.startNodeTransformation;
import static com.suslovila.utils.SUSUtils.*;

@Mixin(value = TileNode.class)
public abstract class MixinTileNode extends TileThaumcraft implements MixinTileNodeProvider {

    @Shadow(remap = false)
    public abstract AspectList getAspects();
    @Shadow(remap = false)
    public abstract boolean takeFromContainer(Aspect aspect, int amount);
    private int transformationAspectSize;
    private int transformationTimer = -1;
    public int getTransformationTimer(){return transformationTimer;}
    public void addTime(int n){
        transformationTimer += n;
        markDirty();
    }

    public int getTransformationAspectSize(){return transformationAspectSize;}
    public void setTransformationAspectSize(int n){transformationAspectSize = n;}

//USE OBFUSCATION FUNCTION NAME OR THINGS ARE NOT GOING TO WORK!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    @Inject(remap = false, method = "handleHungryNodeFirst", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;func_70097_a(Lnet/minecraft/util/DamageSource;F)Z", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    public void hungryNodeTransformationInvoker(boolean change, CallbackInfoReturnable<Boolean> cir,List list, Iterator iterator, Object entity) {
        Entity ent = (Entity) entity;
        if(!ent.isEntityAlive() && ent instanceof EntityItem && ((EntityItem)ent).getEntityItem().getItem() instanceof ItemCrystallizedAntiMatter && !worldObj.isRemote) startNodeTransformation((TileNode)(Object)this);
    }

    @Inject(remap = true, method = "updateEntity", at = @At(value = "TAIL"))
    public void addTimer(CallbackInfo ci) {
        //ticking transformation
        if(!worldObj.isRemote) {
            if(isNodeBeingTransformed()) addTime(1);

            //removing aspects from node
            if (isNodeBeingTransformed() && transformationTimer < halfConvertionTime) {
                int iterationAmount = 1 + getTransformationAspectSize() / (halfConvertionTime);
                for (int i = 0; i < iterationAmount; i++) {
                    Aspect[] aspects = getAspects().getAspects();
                    if (aspects.length != 0) {
                        Aspect aspect = aspects[random.nextInt(aspects.length)];
                        if (this.getAspects().getAmount(aspect) != 0)
                            this.takeFromContainer(aspects[random.nextInt(aspects.length)], 1);
                    }
                }
            }
            if (transformationTimer == halfConvertionTime) {
//            //removing redundant aspects
//            for(Aspect aspect : this.getAspects().getAspects()) this.getAspects().remove(aspect);


                //handling anti-node initialization
                this.worldObj.setBlock(this.xCoord, this.yCoord, this.zCoord, ModBlocks.ANTI_NODE);
                TileAntiNode antiNode = (TileAntiNode) worldObj.getTileEntity(xCoord, yCoord, zCoord);
                antiNode.energy = transformationAspectSize;

            }
        }
    }


    @Inject(remap = false, method = "writeCustomNBT", at = @At(value = "TAIL"))
    public void writeToNBT(NBTTagCompound nbttagcompound, CallbackInfo ci) {
        nbttagcompound.setInteger("transformationTimer", getTransformationTimer());
        nbttagcompound.setInteger("transformationAspectSize", getTransformationAspectSize());
    }
    @Inject(remap = false, method = "readCustomNBT", at = @At(value = "TAIL"))
    public void readFromNBT(NBTTagCompound nbttagcompound, CallbackInfo ci) {
        transformationTimer = nbttagcompound.getInteger("transformationTimer");
        transformationAspectSize = nbttagcompound.getInteger("transformationAspectSize");
    }
    public boolean isNodeBeingTransformed(){return transformationTimer != -1;}

    public void markDirty() {
        super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
        super.markDirty();
    }
}