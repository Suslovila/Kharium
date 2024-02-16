package com.suslovila.client.particles;

import com.suslovila.utils.SusGraphicHelper;
import com.suslovila.utils.SusUtils;
import com.suslovila.utils.SusVec3;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.ArrayDeque;
import java.util.ArrayList;

public class FXSmokeSpiral extends FXBase {
    private float radius;
    private final int start;
    private final ArrayList<SusVec3> system;

    public static final ArrayDeque<FXBase> queuedDepthIgnoringRenders = new ArrayDeque<>();
    public static final ArrayDeque<FXBase> queuedRenders = new ArrayDeque<>();

    public FXSmokeSpiral(World world, double d, double d1, double d2, float radius, int start, int miny, SusVec3 direction, ResourceLocation resourceLocation) {
        super(world, d, d1, d2, 0.0D, 0.0D, 0.0D, 20, 0.5f, true, resourceLocation, false);
        this.particleGravity = -0.01F;
        this.motionX = this.motionY = this.motionZ = 0.0D;
        this.particleMaxAge = 30;
        this.noClip = false;
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        this.radius = radius;
        this.start = start;
        system = SusUtils.getCordSystemFromVec3(direction);
        onUpdate();
    }


    @Override
    protected void injectRender(Tessellator tessellator) {

        float r1 = (float) this.start + 720.0F * (((float) this.particleAge + partialTick) / (float) this.particleMaxAge);
        double r2 = 90.0F - 180.0F * ((this.particleAge + partialTick) / this.particleMaxAge);

        double mX = -MathHelper.sin(r1 / 180.0f * 3.1415927f) * MathHelper.cos((float) (r2 / 180.0f * 3.1415927f));
        double mZ = MathHelper.cos(r1 / 180.0f * 3.1415927f) * MathHelper.cos((float) (r2 / 180.0f * 3.1415927f));
        double mY = -MathHelper.sin((float) (r2 / 180.0f * 3.1415927f));

        SusVec3 resultVec3 = system.get(0).scale(mX).add((system.get(1).scale(mY)).add((system.get(2).scale(mZ))));

        float f10 = 0.14F * this.particleScale;
        float f11 = (float) (this.prevPosX + (this.posX - this.prevPosX) * (double) partialTick - interpPosX) + (float) resultVec3.x * radius;
        float f12 = (float) (this.prevPosY + (this.posY - this.prevPosY) * (double) partialTick - interpPosY) + (float) resultVec3.y * radius;
        float f13 = (float) (this.prevPosZ + (this.posZ - this.prevPosZ) * (double) partialTick - interpPosZ) + (float) resultVec3.z * radius;

        float xRendered = f11 + (float) resultVec3.x * radius;
        float yRendered = f12 + (float) resultVec3.y * radius;
        float zRendered = f13 + (float) resultVec3.z * radius;

        tessellator.setBrightness(getBrightnessForRender(partialTick));
        SusGraphicHelper.INSTANCE.bindColor(tessellator, SusUtils.humilitasColor, 0.4f, 1f);
        tessellator.addVertexWithUV(xRendered - x * f10 - u * f10, yRendered - y * f10, (zRendered - z * f10 - v * f10), 0, 0);
        tessellator.addVertexWithUV(xRendered - x * f10 + u * f10, yRendered + y * f10, (zRendered - z * f10 + v * f10), 0, 1);
        tessellator.addVertexWithUV(xRendered + x * f10 + u * f10, yRendered + y * f10, (zRendered + z * f10 + v * f10), 1, 1);
        tessellator.addVertexWithUV(xRendered + x * f10 - u * f10, yRendered - y * f10, (zRendered + z * f10 - v * f10), 1, 0);

    }

    public void onUpdate() {
        if (this.particleAge++ >= this.particleMaxAge) {
            this.setDead();
        }

    }

    @Override
    public ArrayDeque<FXBase> getQueuedDepthIgnoringRenders() {
        return queuedDepthIgnoringRenders;
    }

    @Override
    public ArrayDeque<FXBase> getQueuedRenderers() {
        return queuedRenders;
    }

}
