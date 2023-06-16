package com.suslovila.client.render.tile;

import com.suslovila.client.particles.FXAntiNode;
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
import java.util.concurrent.ThreadLocalRandom;

import static org.lwjgl.opengl.GL11.glAlphaFunc;

public class TileAntiNodeRenderer extends TileNodeRenderer {
    Random random = new Random();
    //todo: fix the center; fix size


    public static final ResourceLocation nodetex = new ResourceLocation(ExampleMod.MOD_ID, "textures/antinode/antinodetexture.png");

    public static void renderAntiNode(TileAntiNode tileAntiNode, EntityLivingBase viewer, double viewDistance, boolean visible, boolean depthIgnore, float size, int px, int py, int pz, float partialTicks, AspectList aspects, NodeType type, NodeModifier mod) {
        long nt = System.nanoTime();
        long time = nt / 50000000L;
        float scaley = 1.5F;
        int e = (int)(50.0F);

        float scale = (float)e / 50.0F * 1.3F;
        py = py + 1;
//        float m = (1.0F - portal.getHealth() / portal.getMaxHealth()) / 3.0F;
//        float bob = MathHelper.sin((float)portal.ticksExisted / (5.0F - 12.0F * m)) * m + m;
//        float bob2 = MathHelper.sin((float)portal.ticksExisted / (6.0F - 15.0F * m)) * m + m;
        float alpha = 1.0F;
//        scaley = scaley - bob / 4.0F;
//        scale = scale - bob2 / 3.0F;
        UtilsFX.bindTexture(nodetex);
        GL11.glPushMatrix();
//        GL11.glEnable(3042);
//        GL11.glBlendFunc(770, 771);
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, alpha);
        if(Minecraft.getMinecraft().renderViewEntity instanceof EntityPlayer) {
            Tessellator tessellator = Tessellator.instance;
            float arX = ActiveRenderInfo.rotationX;
            float arZ = ActiveRenderInfo.rotationZ;
            float arYZ = ActiveRenderInfo.rotationYZ;
            float arXY = ActiveRenderInfo.rotationXY;
            float arXZ = ActiveRenderInfo.rotationXZ;
            EntityPlayer player = (EntityPlayer)Minecraft.getMinecraft().renderViewEntity;
            double iPX = player.prevPosX + (player.posX - player.prevPosX) * (double)partialTicks;
            double iPY = player.prevPosY + (player.posY - player.prevPosY) * (double)partialTicks;
            double iPZ = player.prevPosZ + (player.posZ - player.prevPosZ) * (double)partialTicks;
            GL11.glTranslated(-iPX, -iPY, -iPZ);
            tessellator.startDrawingQuads();
            tessellator.setBrightness(220);
            //tessellator.setColorRGBA_I(color, (int)(alpha * 255.0F));
            Vec3 v1 = Vec3.createVectorHelper((double)(-arX * scale - arYZ * scale), (double)(-arXZ * scale), (double)(-arZ * scale - arXY * scale));
            Vec3 v2 = Vec3.createVectorHelper((double)(-arX * scale + arYZ * scale), (double)(arXZ * scale), (double)(-arZ * scale + arXY * scale));
            Vec3 v3 = Vec3.createVectorHelper((double)(arX * scale + arYZ * scale), (double)(arXZ * scale), (double)(arZ * scale + arXY * scale));
            Vec3 v4 = Vec3.createVectorHelper((double)(arX * scale - arYZ * scale), (double)(-arXZ * scale), (double)(arZ * scale - arXY * scale));
//            if(angle != 0.0F) {
//                Vec3 pvec = Vec3.createVectorHelper(iPX, iPY, iPZ);
//                Vec3 tvec = Vec3.createVectorHelper(px, py, pz);
//                Vec3 qvec = pvec.subtract(tvec).normalize();
//                QuadHelper.setAxis(qvec, (double)angle).rotate(v1);
//                QuadHelper.setAxis(qvec, (double)angle).rotate(v2);
//                QuadHelper.setAxis(qvec, (double)angle).rotate(v3);
//                QuadHelper.setAxis(qvec, (double)angle).rotate(v4);
//            }

//            float f2 = (float)frame / (float)frames;
//            float f3 = (float)(frame + 1) / (float)frames;
//            float f4 = (float)strip / (float)frames;
//            float f5 = ((float)strip + 1.0F) / (float)frames;
            tessellator.setNormal(0.0F, 0.0F, -1.0F);
            tessellator.addVertexWithUV(px + v1.xCoord, py + v1.yCoord, pz + v1.zCoord, (double)1, (double)1);
            tessellator.addVertexWithUV(px + v2.xCoord, py + v2.yCoord, pz + v2.zCoord, (double)1, (double)0);
            tessellator.addVertexWithUV(px + v3.xCoord, py + v3.yCoord, pz + v3.zCoord, (double)0, (double)0);
            tessellator.addVertexWithUV(px + v4.xCoord, py + v4.yCoord, pz + v4.zCoord, (double)0, (double)1);
            tessellator.draw();
        }



        //GL11.glDisable(3042);
        GL11.glPopMatrix();

    }
        public static void renderNode(EntityLivingBase viewer, double viewDistance, boolean visible, boolean depthIgnore, float size, int x, int y, int z, float partialTicks, AspectList aspects, NodeType type, NodeModifier mod) {
        long nt = System.nanoTime();

//        if(nt % 10000 == 0) {

//        }
        //handling circles


        //glAlphaFunc(GL_GREATER, 1/255);


        UtilsFX.bindTexture(nodetex);
        int frames = 1;
        if(aspects.size() > 0 && visible) {
            double distance = viewer.getDistance((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D);
            if(distance > viewDistance) {
                return;
            }

            float alpha = (float)((viewDistance - distance) / viewDistance);
            if(mod != null) {
                switch(mod) {
                    case BRIGHT:
                        alpha *= 1.5F;
                        break;
                    case PALE:
                        alpha *= 0.66F;
                        break;
                    case FADING:
                        alpha *= MathHelper.sin((float)viewer.ticksExisted / 3.0F) * 0.25F + 0.33F;
                }
            }

            GL11.glPushMatrix();
            glAlphaFunc(516, 0.003921569F);
            glAlphaFunc(516, 0.003921569F);
            GL11.glDepthMask(false);
            if(depthIgnore) {
                GL11.glDisable(2929);
            }

            GL11.glDisable(2884);
            long time = nt / 5000000L;
            float bscale = 0.25F;
            GL11.glPushMatrix();
            float rad = 6.2831855F;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, alpha);
            int i = (int)((nt / 40000000L + (long)x) % (long)frames);
            int count = 0;
            float scale = 0.0F;
            float angle = 0.0F;
            float average = 0.0F;

            for(Aspect aspect : aspects.getAspects()) {
                if(aspect.getBlend() == 771) {
                    alpha = (float)((double)alpha * 1.5D);
                }

                average += (float)aspects.getAmount(aspect);
                GL11.glPushMatrix();
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, aspect.getBlend());
                scale = MathHelper.sin((float)viewer.ticksExisted / (14.0F - (float)count)) * bscale + bscale * 2.0F;
                scale = 0.2F + scale * ((float)aspects.getAmount(aspect) / 50.0F);
                scale = scale * size;
                //angle = (float)(time % (long)(5000 + 500 * count)) / (5000.0F + (float)(500 * count)) * rad;
                UtilsFX.renderFacingStrip((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, angle, scale, alpha / Math.max(1.0F, (float)aspects.size() / 2.0F), frames, 0, i, partialTicks, aspect.getColor());
                GL11.glDisable(3042);
                GL11.glPopMatrix();
                ++count;
                if(aspect.getBlend() == 771) {
                    alpha = (float)((double)alpha / 1.5D);
                }
            }

            average = average / (float)aspects.size();
            GL11.glPushMatrix();
            GL11.glEnable(3042);
            i = (int)((nt / 40000000L + (long)x) % (long)frames);
            scale = 0.1F + average / 150.0F;
            scale = scale * size;
            int strip = 1;
            switch(type) {
                case NORMAL:
                    GL11.glBlendFunc(770, 1);
                    break;
                case UNSTABLE:
                    GL11.glBlendFunc(770, 1);
                    strip = 6;
                    angle = 0.0F;
                    break;
                case DARK:
                    GL11.glBlendFunc(770, 771);
                    strip = 2;
                    break;
                case TAINTED:
                    GL11.glBlendFunc(770, 771);
                    strip = 5;
                    break;
                case PURE:
                    GL11.glBlendFunc(770, 1);
                    strip = 4;
                    break;
                case HUNGRY:
                    scale *= 0.75F;
                    GL11.glBlendFunc(770, 1);
                    strip = 3;
            }

            GL11.glColor4f(1.0F, 0.0F, 1.0F, alpha);
            UtilsFX.renderFacingStrip((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, angle, scale, alpha, frames, strip, i, partialTicks, 16777215);
            GL11.glDisable(3042);
            GL11.glPopMatrix();
            GL11.glPopMatrix();
            GL11.glEnable(2884);
            if(depthIgnore) {
                GL11.glEnable(2929);
            }

            GL11.glDepthMask(true);
            glAlphaFunc(516, 0.1F);
            GL11.glPopMatrix();
        } else {
            GL11.glPushMatrix();
            glAlphaFunc(516, 0.003921569F);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 1);
            GL11.glDepthMask(false);
            int i = (int)((nt / 40000000L + (long)x) % (long)frames);
            GL11.glColor4f(1.0F, 0.0F, 1.0F, 0.1F);
            UtilsFX.renderFacingStrip((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, 0.0F, 0.5F, 0.1F, frames, 1, i, partialTicks, 16777215);
            GL11.glDepthMask(true);
            GL11.glDisable(3042);
            glAlphaFunc(516, 0.1F);
            GL11.glPopMatrix();
        }

    }

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
            renderNode(viewer, viewDistance, condition, depthIgnore, size, tile.xCoord, tile.yCoord, tile.zCoord, partialTicks, ((INode)tile).getAspects(), ((INode)tile).getNodeType(), ((INode)tile).getNodeModifier());

            spawnShadowParticles((TileAntiNode)tile, viewer, 5,3,3,1.28D,1.33D, 0.9, 1.1,0,0.01,0,0.01,0,0.01,tile.xCoord + 0.5D,tile.yCoord + 0.5D,tile.zCoord + 0.5D);

            //handling anti-node bolt
            if(!Minecraft.getMinecraft().isGamePaused()) {
                if (random.nextInt(9) == 6)
                    ExampleMod.proxy.nodeAntiBolt(viewer.worldObj, tile.xCoord + 0.5f, tile.yCoord + 0.5f, tile.zCoord + 0.5f, (float) (tile.xCoord  + nextDouble(-3, 3)), (float) (tile.yCoord  + nextDouble(-3, 3)), (float) (tile.zCoord + nextDouble(-3, 3)));
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
    //some private simple function
    private static double nextDouble(double n1, double n2){
        return ThreadLocalRandom.current().nextDouble(n1, n2);
    }

    private void spawnCircleMovingParticle(TileAntiNode tileAntiNode, EntityLivingBase viewer){
        tileAntiNode.timer += 1;
        double posX = Math.sin(tileAntiNode.timer * Math.PI / 100);
        double posZ = Math.cos(tileAntiNode.timer * Math.PI / 100);
        FXShitAntiNode particle = new FXShitAntiNode(viewer.worldObj, tileAntiNode.xCoord + posX, tileAntiNode.yCoord, tileAntiNode.zCoord + posZ, 0,0,0);
        ParticleEngine.instance.addEffect(viewer.worldObj, particle);
    }



    protected void spawnShadowParticles(TileAntiNode tile, EntityLivingBase viewer, int maxShadowsAmount, int speed, int wholeIterationAmount, double minRadius, double maxRadius, double minParticleSize, double maxParticleSize, double minXOffset, double maxXOffset, double minYOffset, double maxYOffset, double minZOffset, double maxZOffset, double x, double y, double z){

        for(int hl = 0; hl < wholeIterationAmount; hl++){
            if (tile.cordsForShadows.size() < maxShadowsAmount) {
                double radius = nextDouble(minRadius, maxRadius);
                int timer = 0;
                SUSVec3 coreVector = new SUSVec3(nextDouble(-1,1), nextDouble(-1,1), nextDouble(-1, 1));
                coreVector.normalize();


//                SUSVec3 m = new SUSVec3(coreVector.z, 0, -coreVector.x);
//                m = m.normalize();
//                SUSVec3 k = coreVector.cross(m);
//                k = k.normalize();


                ArrayList<Object> arrayList = new ArrayList<Object>();
                arrayList.add(radius);
                arrayList.add(coreVector);
                //arrayList.add(k);
                //arrayList.add(nextDouble(minParticleSize, maxParticleSize));
                arrayList.add(timer);
                tile.cordsForShadows.put(new SUSVec3(x + nextDouble(-minXOffset, maxXOffset), y + nextDouble(-minYOffset, maxYOffset), z + nextDouble(-minZOffset, maxZOffset)), arrayList);
            }
        }
        HashMap<SUSVec3, ArrayList> map = (HashMap) tile.cordsForShadows.clone();
        Iterator<SUSVec3> iterator = map.keySet().iterator();
        while (iterator.hasNext()) {
            SUSVec3 dotInSpace = iterator.next();
            ArrayList<Object> list = map.get(dotInSpace);
            double radius = (double) list.get(0);
            SUSVec3 coreVector = ((SUSVec3)list.get(1));
//            SUSVec3 k = ((SUSVec3) list.get(2)).scale(radius);
            //double particleSize = (double)list.get(3);
            for (int h = 0; h < speed; h++) {
                int timer = (int) list.get(2);
                //randomising the rotation
                //coreVector = coreVector.add(nextDouble(-0.01, 0.01), nextDouble(-0.01, 0.01), nextDouble(-0.01, 0.01));
//                coreVector = coreVector.xRot((float)(Math.PI* Math.cos(timer/1000)));
//                coreVector = coreVector.yRot((float)(Math.PI* Math.sin(timer/1000)));
//                coreVector = coreVector.zRot((float)(Math.PI* Math.cos(timer/1000)));
                //System.out.println(timer);
                SUSVec3 m = new SUSVec3(coreVector.z, 0, -coreVector.x);
                m = m.normalize();
                SUSVec3 k = coreVector.cross(m);
                k = k.normalize().scale(radius);
                m = m.scale(radius);

//                if (timer % 100 == 0 && timer != 0 && random.nextBoolean()) {
//                    tile.cordsForShadows.remove(dotInSpace);
//                    double newRadius = nextDouble(minRadius, maxRadius);
//                    double chis = k.getY() * Math.cos(timer * Math.PI / 100);
//                    Vector3 newDotInSpace = new Vector3(dotInSpace.getX(), dotInSpace.getY() + chis/Math.abs(chis)*(newRadius + radius), dotInSpace.getZ());
//                    Vector3 newK = new Vector3(0, -k.getY(), 0);
//                    ArrayList<Object> newList = new ArrayList<>();
//                    newList.add(newRadius);
//                    newList.add(m.normalize());
//                    newList.add(newK.normalize());
//                    newList.add(particleSize);
//                    newList.add(timer+1);
//                    tile.cordsForShadows.put(newDotInSpace, newList);
//                }
                SUSVec3 a = m.scale(Math.sin(timer * Math.PI / 100)).add(k.scale(Math.cos(timer * Math.PI / 100)));
                list.remove(2);
                list.remove(1);
                list.add(coreVector);
                list.add(timer + 1);
                SUSVec3 endPosition = dotInSpace.add(a);
                FXShitAntiNode particle = new FXShitAntiNode(viewer.worldObj, endPosition.x, endPosition.y, endPosition.z, 0,0,0 );
                ParticleEngine.instance.addEffect(viewer.worldObj, particle);

               if (random.nextInt(300) == 37) {
                   tile.cordsForShadows.remove(dotInSpace);
               }
            }
        }
    }
}
