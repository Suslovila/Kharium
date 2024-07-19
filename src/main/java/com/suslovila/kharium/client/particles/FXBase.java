package com.suslovila.kharium.client.particles;

import com.suslovila.kharium.utils.SusGraphicHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.Frustrum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.util.ArrayDeque;

import static org.lwjgl.opengl.GL11.*;


//provides comfort and optimised creation and rendering particles.
//NOTE: can be used only by particles wich have the same texture

//do not forget to register your particle in ParticleRenderDispatcher!!!!
@SideOnly(Side.CLIENT)
public abstract class FXBase extends EntityFX {
    float maxParticleScale;
    float partialTick = 0;
    float x, y, z, u, v = 0;
    boolean depthTest;
    int renderTick;
    boolean doKillIfCantSee;
    private final ResourceLocation FXTexture;


    public FXBase(World world, double x, double y, double z, double mX, double mY, double mZ, int lifeTime, float particleSize, boolean depthTest, ResourceLocation texture, boolean doKillIfCantSee) {
        super(world, x, y, z, mX, mY, mZ);
        this.doKillIfCantSee = doKillIfCantSee;
        this.motionX = mX;
        this.motionY = mY;
        this.motionZ = mZ;
        this.particleScale = particleSize;
        this.maxParticleScale = particleSize;
        this.particleMaxAge = lifeTime;
        this.noClip = true;
        this.particleGravity = 0;

        //ATTENTION!!!! WE MUST DO THIS IN ORDER TO PREVENT GRAPHIC BUGS SUCH AS TELEPORTING PARTICLES!!!!!!
        this.onUpdate();

        this.depthTest = depthTest;
        FXTexture = texture;

    }


    public final void renderQueued(Tessellator tessellator) {
        glPushMatrix();
        renderTick++;
        injectRender(tessellator);
        glPopMatrix();
    }


    @Override
    public final void renderParticle(Tessellator tessellator, float partialTick, float x, float y, float z, float u, float v) {
        this.partialTick = partialTick;
        this.x = x;
        this.y = y;
        this.z = z;
        this.u = u;
        this.v = v;

        if (doKillIfCantSee) {
            ICamera camera = new Frustrum();
            EntityLivingBase entitylivingbase = Minecraft.getMinecraft().renderViewEntity;
            double d0 = entitylivingbase.lastTickPosX + (entitylivingbase.posX - entitylivingbase.lastTickPosX) * (double) partialTick;
            double d1 = entitylivingbase.lastTickPosY + (entitylivingbase.posY - entitylivingbase.lastTickPosY) * (double) partialTick;
            double d2 = entitylivingbase.lastTickPosZ + (entitylivingbase.posZ - entitylivingbase.lastTickPosZ) * (double) partialTick;
            camera.setPosition(d0, d1, d2);
            if (!camera.isBoundingBoxInFrustum(boundingBox)) {
                this.kill();
            }
        }

        if (depthTest) getQueuedRenderers().add(this);
        else getQueuedDepthIgnoringRenders().add(this);


    }

    public void onUpdate() {
        super.onUpdate();
    }

    protected void injectRender(Tessellator tessellator) {
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1);
        glAlphaFunc(GL_GREATER, 0.003921569F);

        float f10 = 0.1F * this.particleScale;
        float f11 = (float) (this.prevPosX + (this.posX - this.prevPosX) * (double) partialTick - interpPosX);
        float f12 = (float) (this.prevPosY + (this.posY - this.prevPosY) * (double) partialTick - interpPosY);
        float f13 = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * (double) partialTick - interpPosZ);

        SusGraphicHelper.INSTANCE.pushBrightness(tessellator);
        tessellator.setBrightness(getBrightnessForRender(partialTick));

        tessellator.addVertexWithUV(f11 - x * f10 - u * f10, f12 - y * f10, (f13 - z * f10 - v * f10), 0, 0);
        tessellator.addVertexWithUV(f11 - x * f10 + u * f10, f12 + y * f10, (f13 - z * f10 + v * f10), 0, 1);
        tessellator.addVertexWithUV(f11 + x * f10 + u * f10, f12 + y * f10, (f13 + z * f10 + v * f10), 1, 1);
        tessellator.addVertexWithUV(f11 + x * f10 - u * f10, f12 - y * f10, (f13 + z * f10 - v * f10), 1, 0);

        SusGraphicHelper.INSTANCE.popBrightness(tessellator);
        glDisable(GL_BLEND);
        glAlphaFunc(GL_GREATER, 0.1F);
    }

    public abstract ArrayDeque<FXBase> getQueuedDepthIgnoringRenders();

    public abstract ArrayDeque<FXBase> getQueuedRenderers();


}