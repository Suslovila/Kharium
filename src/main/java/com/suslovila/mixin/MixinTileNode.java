package com.suslovila.mixin;

import com.suslovila.common.item.ItemCrystallizedAntiMatter;
import com.suslovila.common.block.ModBlocks;
import com.suslovila.common.block.tileEntity.TileAntiNode;
import com.suslovila.mixinUtils.IMixinTileNodeProvider;
import com.suslovila.api.utils.SUSUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
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
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.tiles.TileNode;

import java.util.Iterator;
import java.util.List;

import static com.suslovila.mixinUtils.MixinStaticMethods.startNodeTransformation;
import static com.suslovila.api.utils.SUSUtils.*;

@Mixin(value = TileNode.class)
public abstract class MixinTileNode extends TileThaumcraft implements IMixinTileNodeProvider {

    @Shadow(remap = false)
    public abstract AspectList getAspects();
    @Shadow(remap = false)
    public abstract boolean takeFromContainer(Aspect aspect, int amount);

    //maximum amount of aspects in node determines the max power of anti-Node
    //Synced client-server values
    private int transformationAspectSize;
    private int transformationTimer = -1;

    private String ownerName = "";

    public String getOwnerName(){return ownerName;}
    public void setOwnerName(String name){ownerName = name;}
    public int getTransformationTimer(){return transformationTimer;}
    public void addTime(int n){
        transformationTimer += n;
        markDirty();
    }
    public int getRequiredTimeForTransformation(){return isNodeBeingTransformed() ? transformationAspectSize / aspectReducePerTick : -1;}
public AspectList getAspectsAsList(){
        return getAspects();
}
    public int getTransformationAspectSize(){return transformationAspectSize;}
    public void setTransformationAspectSize(int n){transformationAspectSize = n;}

//USE OBFUSCATION FUNCTION NAME OR THINGS ARE NOT GOING TO WORK!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    @Inject(remap = false, method = "handleHungryNodeFirst", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;func_70097_a(Lnet/minecraft/util/DamageSource;F)Z", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    public void hungryNodeTransformationInvoker(boolean change, CallbackInfoReturnable<Boolean> cir,List list, Iterator iterator, Object entity) {
        Entity ent = (Entity) entity;
        if(!ent.isEntityAlive() && ent instanceof EntityItem && ((EntityItem)ent).getEntityItem().getItem() instanceof ItemCrystallizedAntiMatter && !worldObj.isRemote && !isNodeBeingTransformed()) startNodeTransformation((TileNode)(Object)this, ((EntityItem) ent).getEntityItem());
    }

    @Inject(remap = true, method = "updateEntity", at = @At(value = "TAIL"))
    public void addTimer(CallbackInfo ci) {
        //ticking transformation
        if(!worldObj.isRemote) {
            if(isNodeBeingTransformed()) addTime(1);

            //removing aspects from node
            if (isNodeBeingTransformed()) {
                for (int i = 0; i < aspectReducePerTick; i++) {
                    Aspect[] aspects = getAspectsAsList().getAspects();
                    if (aspects.length != 0) {
                        Aspect aspect = aspects[INSTANCE.getRandom().nextInt(aspects.length)];
                        if (this.getAspectsAsList().getAmount(aspect) != 0)
                            this.takeFromContainer(aspects[SUSUtils.INSTANCE.getRandom().nextInt(aspects.length)], 1);
                        else this.getAspectsAsList().remove(aspect);
                    }
                }

                if (transformationTimer >= getRequiredTimeForTransformation()) {
                    if(Thaumcraft.proxy.getCompletedResearch().get(getOwnerName()).contains("CRYSTALLIZED_KHARU")) {
                        EntityPlayer player = worldObj.getPlayerEntityByName(getOwnerName());
                        SUSUtils.INSTANCE.completeNormalResearch("ANTI_NODE", player, worldObj);
                    }
                    //handling anti-node initialization
                    this.worldObj.setBlock(this.xCoord, this.yCoord, this.zCoord, ModBlocks.ANTI_NODE);
                    TileAntiNode antiNode = (TileAntiNode) worldObj.getTileEntity(xCoord, yCoord, zCoord);
                    antiNode.setMaxEnergy(transformationAspectSize);


                }
            }
        }
    }


    @Inject(remap = false, method = "writeCustomNBT", at = @At(value = "TAIL"))
    public void writeToNBT(NBTTagCompound nbttagcompound, CallbackInfo ci) {
        nbttagcompound.setInteger("transformationTimer", getTransformationTimer());
        nbttagcompound.setInteger("transformationAspectSize", getTransformationAspectSize());
        nbttagcompound.setString(ItemCrystallizedAntiMatter.Companion.getGlobalOwnerName(), getOwnerName());

    }
    @Inject(remap = false, method = "readCustomNBT", at = @At(value = "TAIL"))
    public void readFromNBT(NBTTagCompound nbttagcompound, CallbackInfo ci) {
        transformationTimer = nbttagcompound.getInteger("transformationTimer");
        transformationAspectSize = nbttagcompound.getInteger("transformationAspectSize");
        ownerName = nbttagcompound.getString(ItemCrystallizedAntiMatter.Companion.getGlobalOwnerName());

    }
    public boolean isNodeBeingTransformed(){return transformationTimer != -1;}

    public void markDirty() {
        super.worldObj.markBlockForUpdate(super.xCoord, super.yCoord, super.zCoord);
        super.markDirty();
    }
}