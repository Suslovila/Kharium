package com.suslovila.client.particles;


import com.suslovila.examplemod.ExampleMod;
import com.suslovila.mixin.MixinTesselator;
import com.suslovila.utils.mixins.Tesselator;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.particle.EntityFX;

import static org.lwjgl.opengl.GL11.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import thaumcraft.client.lib.UtilsFX;

@SideOnly(Side.CLIENT)
public class FXShitAntiNode extends EntityFX
{
    float maxParticleScale = 0.8f;
    private static final ResourceLocation WellingFXTexture = new ResourceLocation(ExampleMod.MOD_ID, "textures/particles/antiNodeFX.png");

    public FXShitAntiNode(World world, double x, double y, double z, double mX, double mY, double mZ)
    {
        super(world, x, y, z, mX, mY, mZ);
        this.motionX = mX;
        this.motionY = mY;
        this.motionZ = mZ;
        this.particleScale= maxParticleScale;
        System.out.println(particleScale);
        this.particleAlpha = 0.5F;
        this.particleMaxAge = 20;
        this.noClip = true;
        this.particleGravity = 0;
        //ATTENTION!!!! WE MUST DO THIS IN ORDER TO PREVENT GRAPHIC BUGS EITH TELEPORTING PARTICLES!!!!!!
        this.onUpdate();
    }

    public void renderParticle(Tessellator tessellator, float partialTick, float x, float y, float z, float u, float v) {
        glPushMatrix();
        tessellator.draw();
        Minecraft.getMinecraft().renderEngine.bindTexture(WellingFXTexture);

        glDepthMask(false);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glAlphaFunc(GL_GREATER, 0.003921569F);
        glAlphaFunc(GL_GREATER, 0.6F - ((float)this.particleAge + partialTick - 1.0F) * 0.25F * 0.5F);

        float f10 = 0.1F * this.particleScale;
        float f11 = (float)(this.prevPosX + (this.posX - this.prevPosX) * (double)partialTick - interpPosX);
        float f12 = (float)(this.prevPosY + (this.posY - this.prevPosY) * (double)partialTick - interpPosY);
        float f13 = (float)(this.prevPosZ + (this.posZ - this.prevPosZ) * (double)partialTick - interpPosZ);

        tessellator.startDrawingQuads();
        tessellator.setBrightness(getBrightnessForRender(partialTick));

        tessellator.addVertexWithUV((double)(f11 - x * f10 - u * f10), (double)(f12 - y * f10), (double)(f13 - z * f10 - v * f10), 0, 0);
        tessellator.addVertexWithUV((double)(f11 - x * f10 + u * f10), (double)(f12 + y * f10), (double)(f13 - z * f10 + v * f10), 0, 1);
        tessellator.addVertexWithUV((double)(f11 + x * f10 + u * f10), (double)(f12 + y * f10), (double)(f13 + z * f10 + v * f10), 1, 1);
        tessellator.addVertexWithUV((double)(f11 + x * f10 - u * f10), (double)(f12 - y * f10), (double)(f13 + z * f10 - v * f10), 1, 0);
        tessellator.draw();

        glDisable(GL_BLEND);
        glDepthMask(true);
        glAlphaFunc(GL_GREATER, 0.1F);
        tessellator.startDrawingQuads();
        glPopMatrix();
        Minecraft.getMinecraft().renderEngine.bindTexture(UtilsFX.getParticleTexture());

    }



//
//    public int getBrightnessForRender(float brightnessFR)
//    {
//        return 15728880;
//    }
//
//    public float getBrightness(float brightness)
//    {
//        return 1.0F;
//    }
//
//    public boolean canBePushed()
//    {
//        return false;
//    }
//
//    public int getFXLayer()
//    {
//        return 0;
//    }

    //    public AxisAlignedBB getBoundingBox()
//    {
//        return null;
//    }
    public void onUpdate() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        if (this.particleAge++ >= this.particleMaxAge) this.setDead();

        fadeOut();
    }
    private void fadeOut(){
        particleScale = maxParticleScale * (particleMaxAge - particleAge)/particleMaxAge;
        particleAlpha = (float) (particleMaxAge - particleAge) / particleMaxAge;
    }
}