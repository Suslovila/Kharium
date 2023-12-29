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

import java.util.ArrayDeque;
import java.util.Queue;

@SideOnly(Side.CLIENT)
public class FXAntiNode extends FXSusBase
{
    public static final ArrayDeque<FXSusBase> queuedDepthIgnoringRenders = new ArrayDeque<>();


    public static final ArrayDeque<FXSusBase> queuedRenders = new ArrayDeque<>();


    public static final ResourceLocation FXTexture = new ResourceLocation(ExampleMod.MOD_ID, "textures/particles/antinodefx.png");



    public FXAntiNode(World world, double x, double y, double z, double mX, double mY, double mZ, int lifeTime, float particleSize, boolean depthTest)
    {
        super(world, x, y, z, mX, mY, mZ, lifeTime, particleSize, depthTest,FXTexture, true);
    }
    public void onUpdate() {
        super.onUpdate();
        //it seems it does not work because particles are black. I need to "play" with blend and alpha to find out the solution
        fadeOut();
    }

    @Override
    public ArrayDeque<FXSusBase> getQueuedDepthIgnoringRenders() {
        return queuedDepthIgnoringRenders;
    }

    @Override
    public ArrayDeque<FXSusBase> getQueuedRenderers() {
        return queuedRenders;
    }


    private void fadeOut(){
        particleScale = maxParticleScale * (particleMaxAge - particleAge)/particleMaxAge;
        particleAlpha = (float) (particleMaxAge - particleAge) / particleMaxAge;
    }
}