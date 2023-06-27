package com.suslovila.client.render.tile;


import com.suslovila.client.particles.FXShitAntiNode;
import com.suslovila.common.block.tileEntity.TileAntiNode;
import com.suslovila.examplemod.ExampleMod;
import com.suslovila.utils.SUSVec3;
import com.suslovila.utils.Vector3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityBubbleFX;
import net.minecraft.client.particle.EntityCritFX;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.INode;
import thaumcraft.api.nodes.IRevealer;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.api.nodes.NodeType;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.lib.QuadHelper;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.client.renderers.tile.TileNodeRenderer;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.items.relics.ItemThaumometer;
import thaumcraft.common.tiles.TileJarNode;
import thaumcraft.common.tiles.TileNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

import static org.lwjgl.opengl.GL11.*;

public class TileAntiNodeRenderer extends TileNodeRenderer {
    Random random = new Random();
    //todo: fix the center; fix size


    public static final ResourceLocation nodetex = new ResourceLocation(ExampleMod.MOD_ID, "textures/antinode/antinodetexture.png");


    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks) {
        if(tile instanceof INode) {

            float size = 1.0F;
            INode node = (INode)tile;
            double viewDistance = 64.0D;
            EntityLivingBase viewer = Minecraft.getMinecraft().renderViewEntity;
            //spawnShadowParticles((TileAntiNode)tile, viewer, 3,3,3,1,2D, 0.9, 1.1,0,0.01,0,0.01,0,0.01,tile.xCoord,tile.yCoord,tile.zCoord);

            boolean condition = false;
            boolean depthIgnore = false;
            if(viewer instanceof EntityPlayer) {
                if(tile != null && tile instanceof TileJarNode) {
                    condition = true;
                    size = 0.7F;
                } else if(((EntityPlayer)viewer).inventory.armorItemInSlot(3) != null && ((EntityPlayer)viewer).inventory.armorItemInSlot(3).getItem() instanceof IRevealer && ((IRevealer)((EntityPlayer)viewer).inventory.armorItemInSlot(3).getItem()).showNodes(((EntityPlayer)viewer).inventory.armorItemInSlot(3), viewer)) {
                    condition = true;
                    depthIgnore = true;
                } else if(((EntityPlayer)viewer).inventory.getCurrentItem() != null && ((EntityPlayer)viewer).inventory.getCurrentItem().getItem() instanceof ItemThaumometer && UtilsFX.isVisibleTo(0.44F, viewer, (double)tile.xCoord, (double)tile.yCoord, (double)tile.zCoord)) {
                    condition = true;
                    depthIgnore = true;
                    viewDistance = 48.0D;
                }
            }
            renderNodeVersion2(viewer, viewDistance, condition, depthIgnore, size, tile.xCoord, tile.yCoord, tile.zCoord, partialTicks, ((INode)tile).getAspects(), ((INode)tile).getNodeType(), ((INode)tile).getNodeModifier());
            double distance = viewer.getDistance((double)tile.xCoord + 0.5D, (double)tile.yCoord + 0.5D, (double)tile.zCoord + 0.5D);
            if(distance <= 48) {
                spawnShadowParticlesOld((TileAntiNode)tile, viewer, 5,3,3,1.28D,1.33D, 0.9, 1.1,0,0.01,0,0.01,0,0.01,tile.xCoord + 0.5D,tile.yCoord + 0.5D,tile.zCoord + 0.5D, 20, 0.8f);
                //handling anti-node bolt
                if(!Minecraft.getMinecraft().isGamePaused()) {
                    if (random.nextInt(9) == 6) {
                        float lightningLength = 2;
                        ExampleMod.proxy.nodeAntiBolt(viewer.worldObj, tile.xCoord + 0.5f, tile.yCoord + 0.5f, tile.zCoord + 0.5f, (float) (tile.xCoord + nextDouble(-lightningLength, lightningLength)), (float) (tile.yCoord + nextDouble(-lightningLength, lightningLength)), (float) (tile.zCoord + nextDouble(-lightningLength, lightningLength)));
                    }
                }
            }


            //handling taking vis
            if(tile instanceof TileNode && ((TileNode)tile).drainEntity != null && ((TileNode)tile).drainCollision != null) {
                Entity drainEntity = ((TileNode)tile).drainEntity;
                if(drainEntity instanceof EntityPlayer && !((EntityPlayer)drainEntity).isUsingItem()) {
                    ((TileNode)tile).drainEntity = null;
                    ((TileNode)tile).drainCollision = null;
                    return;
                }

                MovingObjectPosition drainCollision = ((TileNode)tile).drainCollision;
                GL11.glPushMatrix();
                float f10 = 0.0F;
                int iiud = ((EntityPlayer)drainEntity).getItemInUseDuration();
                if(drainEntity instanceof EntityPlayer) {
                    f10 = MathHelper.sin((float)iiud / 10.0F) * 10.0F;
                }

                Vec3 vec3 = Vec3.createVectorHelper(-0.1D, -0.1D, 0.5D);
                vec3.rotateAroundX(-(drainEntity.prevRotationPitch + (drainEntity.rotationPitch - drainEntity.prevRotationPitch) * partialTicks) * 3.1415927F / 180.0F);
                vec3.rotateAroundY(-(drainEntity.prevRotationYaw + (drainEntity.rotationYaw - drainEntity.prevRotationYaw) * partialTicks) * 3.1415927F / 180.0F);
                vec3.rotateAroundY(-f10 * 0.01F);
                vec3.rotateAroundX(-f10 * 0.015F);
                double d3 = drainEntity.prevPosX + (drainEntity.posX - drainEntity.prevPosX) * (double)partialTicks + vec3.xCoord;
                double d4 = drainEntity.prevPosY + (drainEntity.posY - drainEntity.prevPosY) * (double)partialTicks + vec3.yCoord;
                double d5 = drainEntity.prevPosZ + (drainEntity.posZ - drainEntity.prevPosZ) * (double)partialTicks + vec3.zCoord;
                double d6 = drainEntity == Minecraft.getMinecraft().thePlayer?0.0D:(double)drainEntity.getEyeHeight();
                UtilsFX.drawFloatyLine(d3, d4 + d6, d5, (double)drainCollision.blockX + 0.5D, (double)drainCollision.blockY + 0.5D, (double)drainCollision.blockZ + 0.5D, partialTicks, ((TileNode)tile).color.getRGB(), "textures/misc/wispy.png", -0.02F, (float)Math.min(iiud, 10) / 10.0F);
                GL11.glPopMatrix();
            }

        }
    }


    public static void renderNodeVersion2(EntityLivingBase viewer, double viewDistance, boolean visible, boolean depthIgnore, float size, int x, int y, int z, float partialTicks, AspectList aspects, NodeType type, NodeModifier mod) {
        long nt = System.nanoTime();
        UtilsFX.bindTexture(nodetex);
        int frames = 1;
        if(aspects.size() > 0 && visible) {
            double distance = viewer.getDistance((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D);
            if(distance > viewDistance) {
                return;
            }
            float alpha = (float)((viewDistance - distance) / viewDistance);
            GL11.glPushMatrix();
            glAlphaFunc(516, 0.003921569F);
            glAlphaFunc(516, 0.003921569F);
            GL11.glDepthMask(false);
            if(depthIgnore) {
                GL11.glDisable(2929);
            }

            GL11.glDisable(2884);
            float bscale = 0.25F;
            GL11.glPushMatrix();
            GL11.glColor4f(1.0F, 0.0F, 0.0F, alpha);
            int i = (int)((nt / 40000000L + (long)x) % (long)frames);
            int count = 0;
            float scale = 0.0F;
            float angle = 0.0F;
            float average = 0.0F;

            for(int j = 0; j < 2; j ++){
                //first - "perditio" color(because it fits the theme of node), second - fictive
                if(j == 0) {
                    alpha = (float)((double)alpha * 1.5D);
                }

                average += (float)(j == 0 ? 25:50);
                GL11.glPushMatrix();
                glEnable(3042);
                glBlendFunc(770, (j == 0 ? 771:1));
                scale = MathHelper.sin((float)viewer.ticksExisted / (14.0F - (float)count)) * bscale + bscale * 2.0F;
                scale = 0.2F + scale * ((float)(j == 0 ? 25:50)/ 50.0F);
                scale = scale * size;
                //angle = (float)(time % (long)(5000 + 500 * count)) / (5000.0F + (float)(500 * count)) * rad;
                UtilsFX.renderFacingStrip((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, angle, scale*(j == 1 ? 0.65f : 0.7f), alpha / Math.max(1.0F, (float)(float)(2) / 2.0F), frames, 0, i, partialTicks, (j == 0 ? 4210752:5685248));
                GL11.glDisable(3042);
                GL11.glPopMatrix();
                ++count;
                if(j == 0) {
                    alpha = (float)((double)alpha / 1.5D);
                }
            }
            average = average / (float)aspects.size();
            GL11.glPushMatrix();
            glEnable(3042);
            i = (int)((nt / 40000000L + (long)x) % (long)frames);
            scale = 0.1F + average / 150.0F;
            scale = scale * size;
            int strip = 1;

            GL11.glColor4f(1.0F, 0.0F, 0.0F, alpha);
            UtilsFX.renderFacingStrip((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, angle, scale, alpha, frames, strip, i, partialTicks, 16777215);
            GL11.glDisable(3042);
            GL11.glPopMatrix();
            GL11.glPopMatrix();
            glEnable(2884);
            if(depthIgnore) {
                glEnable(2929);
            }

            GL11.glDepthMask(true);
            glAlphaFunc(516, 0.1F);
            GL11.glPopMatrix();
        } else {
            GL11.glPushMatrix();
            glAlphaFunc(516, 0.003921569F);
            glEnable(3042);
            glBlendFunc(770, 1);
            GL11.glDepthMask(false);
            int i = (int)((nt / 40000000L + (long)x) % (long)frames);
            GL11.glColor4f(1.0F, 0.0F, 0F, 0.1F);
            UtilsFX.renderFacingStrip((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, 0.0F, 0.5F, 0.1F, frames, 1, i, partialTicks, 16777215);
            GL11.glDepthMask(true);
            GL11.glDisable(3042);
            glAlphaFunc(516, 0.1F);
            GL11.glPopMatrix();
        }
    }




    protected void spawnShadowParticlesOld(TileAntiNode tile, EntityLivingBase viewer, int maxShadowsAmount, int speed, int wholeIterationAmount, double minRadius, double maxRadius, double minParticleSize, double maxParticleSize, double minXOffset, double maxXOffset, double minYOffset, double maxYOffset, double minZOffset, double maxZOffset, double x, double y, double z, int particleLifeTime, float particleSize) {

        for(int hl = 0; hl < wholeIterationAmount; hl++){
            if (tile.cordsForShadows.size() < maxShadowsAmount) {
                double radius = nextDouble(minRadius, maxRadius);
                //creating random vector
                SUSVec3 coreVector = new SUSVec3(nextDouble(-1,1), nextDouble(-1,1), nextDouble(-1, 1));
                coreVector.normalize();
                ArrayList<Object> arrayList = new ArrayList<Object>();
                arrayList.add(radius);
                arrayList.add(coreVector);
                //adding timer
                arrayList.add(0);
                //I created some offsets if needed but haven't used them though
                tile.cordsForShadows.put(new SUSVec3(x + nextDouble(-minXOffset, maxXOffset), y + nextDouble(-minYOffset, maxYOffset), z + nextDouble(-minZOffset, maxZOffset)), arrayList);
            }
        }
        Iterator<SUSVec3> iterator = tile.cordsForShadows.keySet().iterator();
        while (iterator.hasNext()) {
            //so, our arrayList is like: 1 - radius, 2 - coreVector to rotate around, 2 - timer for each black "line"
            SUSVec3 dotInSpace = iterator.next();
            ArrayList<Object> list = tile.cordsForShadows.get(dotInSpace);
            double radius = (double) list.get(0);
            SUSVec3 coreVector = ((SUSVec3)list.get(1));
            //iterating and making particles
            for (int h = 0; h < speed; h++) {
                int timer = (int) list.get(2);
                //some Math
                SUSVec3 m = new SUSVec3(coreVector.z, 0, -coreVector.x);
                m = m.normalize();
                SUSVec3 k = coreVector.cross(m);
                k = k.normalize().scale(radius);
                m = m.scale(radius);
                //handling circle around Node
                SUSVec3 a = m.scale(Math.sin(timer * Math.PI / 100)).add(k.scale(Math.cos(timer * Math.PI / 100)));
                list.remove(2);
                list.remove(1);
                list.add(coreVector);
                list.add(timer + 1);
                SUSVec3 endPosition = dotInSpace.add(a);
                //now whe have a pos to add particle!
                FXShitAntiNode particle = new FXShitAntiNode(viewer.worldObj, endPosition.x, endPosition.y, endPosition.z, 0,0,0, particleLifeTime, particleSize);
                ParticleEngine.instance.addEffect(viewer.worldObj, particle);

                if (random.nextInt(300) == 37) tile.cordsForShadows.remove(dotInSpace);
            }
        }
    }







    //alternative version (not planned to use)
    protected void spawnShadowParticlesVortexVersion(TileAntiNode tile, EntityLivingBase viewer, int maxShadowsAmount, int minSpeed,int maxSpeed, int wholeIterationAmount, double minRadius, double maxRadius, double minDelta, double maxDelta, double minParticleSize, double maxParticleSize, double minXOffset, double maxXOffset, double minYOffset, double maxYOffset, double minZOffset, double maxZOffset, double x, double y, double z, int particleLifeTime, float particleSize){

        for(int hl = 0; hl < wholeIterationAmount; hl++){
            if ((tile.cordsForShadows.size() < maxShadowsAmount && random.nextInt(50) == 37)) {
                double radius = nextDouble(minRadius, maxRadius);
                int timer = 0;
                SUSVec3 coreVector = new SUSVec3(nextDouble(-1,1), nextDouble(-1,1), nextDouble(-1, 1));
                coreVector.normalize();
                ArrayList<Object> arrayList = new ArrayList<Object>();
                arrayList.add(nextDouble(minDelta, maxDelta)); // 0
                arrayList.add(coreVector); // 1
                arrayList.add((int)nextDouble(minSpeed, maxSpeed)); // 2
                arrayList.add(radius); // 3
                arrayList.add(timer); // 4
                tile.cordsForShadows.put(new SUSVec3(x + nextDouble(-minXOffset, maxXOffset), y + nextDouble(-minYOffset, maxYOffset), z + nextDouble(-minZOffset, maxZOffset)), arrayList);
            }
        }
        Iterator<SUSVec3> iterator = tile.cordsForShadows.keySet().iterator();
        while (iterator.hasNext()) {
            SUSVec3 dotInSpace = iterator.next();
            ArrayList<Object> list = tile.cordsForShadows.get(dotInSpace);
            SUSVec3 coreVector = ((SUSVec3)list.get(1));
            double delta =(double) list.get(0);
            int speed = (int)list.get(2);
            for (int h = 0; h < speed && tile.cordsForShadows.containsKey(dotInSpace); h++) {
                double radius = (double) list.get(3);
                if(radius<= 0) tile.cordsForShadows.remove(dotInSpace);
                else{
                    int timer = (int) list.get(4);
                    SUSVec3 m = new SUSVec3(coreVector.z, 0, -coreVector.x);
                    m = m.normalize();
                    SUSVec3 k = coreVector.cross(m);
                    k = k.normalize().scale(radius);
                    m = m.scale(radius);
                    SUSVec3 a = m.scale(Math.sin(timer * Math.PI / 100)).add(k.scale(Math.cos(timer * Math.PI / 100)));
                    list.remove(4);
                    list.remove(3);
                    list.add(radius - delta); // 0
                    list.add(timer + 1); // 3
                    SUSVec3 endPosition = dotInSpace.add(a);
                    FXShitAntiNode particle = new FXShitAntiNode(viewer.worldObj, endPosition.x, endPosition.y, endPosition.z, 0, 0, 0, particleLifeTime, particleSize);
                    ParticleEngine.instance.addEffect(viewer.worldObj, particle);
                }
            }
        }
    }

    //some private simple function
    private static double nextDouble(double n1, double n2){
        return ThreadLocalRandom.current().nextDouble(n1, n2);
    }

}