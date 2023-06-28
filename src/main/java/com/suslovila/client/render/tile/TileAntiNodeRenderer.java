package com.suslovila.client.render.tile;


import com.suslovila.client.particles.FXShitAntiNode;
import com.suslovila.common.block.tileEntity.TileAntiNode;
import com.suslovila.ExampleMod;
import com.suslovila.utils.SUSVec3;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.fx.ParticleEngine;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.client.renderers.tile.TileNodeRenderer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static org.lwjgl.opengl.GL11.*;

public class TileAntiNodeRenderer extends TileNodeRenderer {
    Random random = new Random();



    public static final ResourceLocation nodetex = new ResourceLocation(ExampleMod.MOD_ID, "textures/antinode/antinodetexture.png");


    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks) {
            double viewDistance = 48;
            EntityLivingBase viewer = Minecraft.getMinecraft().renderViewEntity;
            double distance = viewer.getDistance((double)tile.xCoord + 0.5D, (double)tile.yCoord + 0.5D, (double)tile.zCoord + 0.5D);
            if(distance <= viewDistance) {
                renderNodeVersion2(viewer, viewDistance, true, 1.0F, tile.xCoord, tile.yCoord, tile.zCoord, partialTicks, 0.5f);
                spawnShadowParticlesOld((TileAntiNode)tile, viewer, 5,3,3,1.28D,1.33D, 0.9, 1.1,0,0.01,0,0.01,0,0.01,tile.xCoord + 0.5D,tile.yCoord + 0.5D,tile.zCoord + 0.5D, 20, 0.8f);
                //handling anti-node bolt
                if(!Minecraft.getMinecraft().isGamePaused()) {
                    if (random.nextInt(9) == 6) {
                        float lightningLength = 2;
                        ExampleMod.proxy.nodeAntiBolt(viewer.worldObj, tile.xCoord + 0.5f, tile.yCoord + 0.5f, tile.zCoord + 0.5f, (float) (tile.xCoord + nextDouble(-lightningLength, lightningLength)), (float) (tile.yCoord + nextDouble(-lightningLength, lightningLength)), (float) (tile.zCoord + nextDouble(-lightningLength, lightningLength)));
                    }
                }
            }
    }


    public static void renderNodeVersion2(EntityLivingBase viewer, double viewDistance, boolean depthIgnore, float size, int x, int y, int z, float partialTicks, float sizeOfNode) {
        UtilsFX.bindTexture(nodetex);
        int frames = 1;
            double distance = viewer.getDistance((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D);
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
            int count = 0;
            float scale;
            float average = 0.0F;

            for(int j = 0; j < 2; j ++){
                //first - "perditio" color(because it fits the theme of node), second - fictive(terra color, does not matter, bacause we only draw red color)
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
                UtilsFX.renderFacingStrip((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, 0, scale*(j == 1 ? 0.65f : 0.7f), alpha, frames, 0, 0, partialTicks, (j == 0 ? 4210752:5685248));
                GL11.glDisable(3042);
                GL11.glPopMatrix();
                ++count;
                if(j == 0) {
                    alpha = (float)((double)alpha / 1.5D);
                }
            }
            average = average * sizeOfNode;
            GL11.glPushMatrix();
            glEnable(3042);
            scale = 0.1F + average / 150.0F;
            scale = scale * size;
            int strip = 1;

            GL11.glColor4f(1.0F, 0.0F, 0.0F, alpha);
            UtilsFX.renderFacingStrip((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, 0, scale, alpha, frames, strip, 0, partialTicks, 16777215);
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
    }




    protected void spawnShadowParticlesOld(TileAntiNode tile, EntityLivingBase viewer, int maxShadowsAmount, int speed, int wholeIterationAmount, double minRadius, double maxRadius, double minParticleSize, double maxParticleSize, double minXOffset, double maxXOffset, double minYOffset, double maxYOffset, double minZOffset, double maxZOffset, double x, double y, double z, int particleLifeTime, float particleSize) {

        for(int hl = 0; hl < wholeIterationAmount; hl++){
            if (tile.cordsForShadows.size() < maxShadowsAmount) {
                double radius = nextDouble(minRadius, maxRadius);
                //creating random vector
                SUSVec3 coreVector = new SUSVec3(nextDouble(-1,1), nextDouble(-1,1), nextDouble(-1, 1));
                coreVector.normalize();
                ArrayList<Object> arrayList = new ArrayList<>();
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