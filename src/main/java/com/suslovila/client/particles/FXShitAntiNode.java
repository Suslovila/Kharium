package com.suslovila.client.particles;

import com.suslovila.ExampleMod;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.particle.EntityFX;

import static org.lwjgl.opengl.GL11.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.Frustrum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;

import java.util.ArrayDeque;
import java.util.Queue;

@SideOnly(Side.CLIENT)
public class FXShitAntiNode extends EntityFX
{
    float maxParticleScale = 1.1f;
    float partialTick = 0;
    float x,  y,  z,  u,  v = 0;
    boolean depthTest;

    private static final ResourceLocation FXTexture = new ResourceLocation(ExampleMod.MOD_ID, "textures/particles/antinodefx.png");
    public static Queue<FXShitAntiNode> queuedRenders = new ArrayDeque();
    public static Queue<FXShitAntiNode> queuedDepthIgnoringRenders = new ArrayDeque();


    public FXShitAntiNode(World world, double x, double y, double z, double mX, double mY, double mZ, int lifeTime, float particleSize, boolean depthTest)
    {
        super(world, x, y, z, mX, mY, mZ);
        this.motionX = mX;
        this.motionY = mY;
        this.motionZ = mZ;
        this.particleScale= particleSize;
        this.maxParticleScale = particleSize;
        //System.out.println(particleScale);
        //this.particleAlpha = 0.5F;
        this.particleMaxAge = lifeTime;
        this.noClip = true;
        this.particleGravity = 0;
        //ATTENTION!!!! WE MUST DO THIS IN ORDER TO PREVENT GRAPHIC BUGS SUCH AS TELEPORTING PARTICLES!!!!!!
        this.onUpdate();
        this.depthTest = depthTest;

    }


    public static void dispatchQueuedRenders(Tessellator tessellator) {
        ParticleRenderDispatcher.wispFxCount = 0;
        ParticleRenderDispatcher.depthIgnoringWispFxCount = 0;

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1F);
        Minecraft.getMinecraft().renderEngine.bindTexture(FXTexture);

        if(!queuedRenders.isEmpty()) {
            tessellator.startDrawingQuads();
            for(FXShitAntiNode wisp : queuedRenders)
                wisp.renderQueued(tessellator);
            tessellator.draw();
        }

        if(!queuedDepthIgnoringRenders.isEmpty()) {
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            tessellator.startDrawingQuads();
            for(FXShitAntiNode wisp : queuedDepthIgnoringRenders)
                wisp.renderQueued(tessellator);
            tessellator.draw();
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        }

        queuedRenders.clear();
        queuedDepthIgnoringRenders.clear();
    }
    private void renderQueued(Tessellator tessellator) {
        glPushMatrix();
        //glDepthMask(false);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glAlphaFunc(GL_GREATER, 0.003921569F);
        glAlphaFunc(GL_GREATER, 0.6F - ((float)this.particleAge + partialTick - 1.0F) * 0.25F * 0.5F);

        float f10 = 0.1F * this.particleScale;
        float f11 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)partialTick - interpPosX);
        float f12 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)partialTick - interpPosY);
        float f13 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)partialTick - interpPosZ);

        tessellator.setBrightness(getBrightnessForRender(partialTick));

        tessellator.addVertexWithUV((double)(f11 - x * f10 - u * f10), (double)(f12 - y * f10), (double)(f13 - z * f10 - v * f10), 0, 0);
        tessellator.addVertexWithUV((double)(f11 - x * f10 + u * f10), (double)(f12 + y * f10), (double)(f13 - z * f10 + v * f10), 0, 1);
        tessellator.addVertexWithUV((double)(f11 + x * f10 + u * f10), (double)(f12 + y * f10), (double)(f13 + z * f10 + v * f10), 1, 1);
        tessellator.addVertexWithUV((double)(f11 + x * f10 - u * f10), (double)(f12 - y * f10), (double)(f13 + z * f10 - v * f10), 1, 0);

        glDisable(GL_BLEND);
        //glDepthMask(true);
        glAlphaFunc(GL_GREATER, 0.1F);
        glPopMatrix();
    }


@Override
    public void renderParticle(Tessellator tessellator, float partialTick, float x, float y, float z, float u, float v) {
        this.partialTick = partialTick;
        this.x = x;
        this.y = y;
        this.z = z;
        this.u = u;
        this.v = v;
    ICamera camera = new Frustrum();
    EntityLivingBase entitylivingbase = Minecraft.getMinecraft().renderViewEntity;
    double d0 = entitylivingbase.lastTickPosX + (entitylivingbase.posX - entitylivingbase.lastTickPosX) * (double)partialTick;
    double d1 = entitylivingbase.lastTickPosY + (entitylivingbase.posY - entitylivingbase.lastTickPosY) * (double)partialTick;
    double d2 = entitylivingbase.lastTickPosZ + (entitylivingbase.posZ - entitylivingbase.lastTickPosZ) * (double)partialTick;
    camera.setPosition(d0, d1, d2);
    if(!camera.isBoundingBoxInFrustum(boundingBox)) this.kill();

    if(depthTest)
        queuedRenders.add(this);
    else queuedDepthIgnoringRenders.add(this);

    }






    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        //trying not to render particles when player does not see them
//        ICamera camera = new Frustrum();
//        EntityLivingBase entitylivingbase = Minecraft.getMinecraft().renderViewEntity;
//        double d0 = entitylivingbase.lastTickPosX + (entitylivingbase.posX - entitylivingbase.lastTickPosX) * (double)partialTick;
//        double d1 = entitylivingbase.lastTickPosY + (entitylivingbase.posY - entitylivingbase.lastTickPosY) * (double)partialTick;
//        double d2 = entitylivingbase.lastTickPosZ + (entitylivingbase.posZ - entitylivingbase.lastTickPosZ) * (double)partialTick;
//        camera.setPosition(d0, d1, d2);
//        if(!camera.isBoundingBoxInFrustum(boundingBox)) this.setDead();

         if (this.particleAge++ >= this.particleMaxAge) this.setDead();

        //it seems it does not work because particles are black. I need to "play" with blend and alpha to find out the solution
        fadeOut();
    }
    private void fadeOut(){
        particleScale = maxParticleScale * (particleMaxAge - particleAge)/particleMaxAge;
        particleAlpha = (float) (particleMaxAge - particleAge) / particleMaxAge;
    }
}