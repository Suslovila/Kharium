package com.suslovila.mixin;

import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import thaumcraft.client.fx.particles.FXSmokeSpiral;

import static com.suslovila.utils.SusUtilsKt.cos;
import static com.suslovila.utils.SusUtilsKt.sin;

@Mixin(value = FXSmokeSpiral.class)
public class MixinFXSmokeSpiral extends EntityFX {
    public MixinFXSmokeSpiral(World p_i1218_1_, double p_i1218_2_, double p_i1218_4_, double p_i1218_6_) {
        super(p_i1218_1_, p_i1218_2_, p_i1218_4_, p_i1218_6_);
    }
    @Shadow(remap = false)
    private int start;

    @Shadow(remap = false)
    private int miny;


    @Shadow(remap = false)
    private float radius;
    @Override
    public void renderParticle(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.66F * this.particleAlpha);
        int particle = (int)(1.0F + this.particleAge / this.particleMaxAge * 4.0F);
        float r1 = this.start + 720.0F * ((this.particleAge + f) / this.particleMaxAge);
        float r2 = 90.0F - 180.0F * ((this.particleAge + f) / this.particleMaxAge);
        float mX = -sin(r1 / 180.0F * Math.PI) * cos(r2 / 180D * Math.PI);
        float mZ = cos(r1 / 180.0F * Math.PI) * cos(r2 / 180D * Math.PI);
        double mY = -sin(r2 / 180.0F * Math.PI);
        mX = mX * this.radius;
        mY = mY * this.radius;
        mZ = mZ * this.radius;
        float var8 = (particle % 16) / 16.0F;
        float var9 = var8 + 0.0624375F;
        float var10 = ((float) particle / 16) / 16.0F;
        float var11 = var10 + 0.0624375F;
        float var12 = 0.15F * this.particleScale;
        double var13 = (this.posX + mX - interpPosX);
        double var14 = (Math.max(this.posY + mY, (this.miny + 0.1F)) - interpPosY);
        double var15 = (this.posZ + mZ - interpPosZ);
        float var16 = 1.0F;
        tessellator.setBrightness(this.getBrightnessForRender(f));
        tessellator.setColorRGBA_F(this.particleRed * var16, this.particleGreen * var16, this.particleBlue * var16, 0.66F * this.particleAlpha);
        tessellator.addVertexWithUV((var13 - f1 * var12 - f4 * var12), (var14 - f2 * var12), (var15 - f3 * var12 - f5 * var12), var9, var11);
        tessellator.addVertexWithUV((var13 - f1 * var12 + f4 * var12), (var14 + f2 * var12), (var15 - f3 * var12 + f5 * var12), var9, var10);
        tessellator.addVertexWithUV((var13 + f1 * var12 + f4 * var12), (var14 + f2 * var12), (var15 + f3 * var12 + f5 * var12), var8, var10);
        tessellator.addVertexWithUV((var13 + f1 * var12 - f4 * var12), (var14 - f2 * var12), (var15 + f3 * var12 - f5 * var12), var8, var11);
    }
}
