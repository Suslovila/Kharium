package com.suslovila.mixin;

import com.suslovila.mixinUtils.MixinTileNodeProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.INode;
import thaumcraft.api.nodes.IRevealer;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.api.nodes.NodeType;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.client.renderers.tile.TileNodeRenderer;
import thaumcraft.common.items.relics.ItemThaumometer;
import thaumcraft.common.tiles.TileJarNode;

import static com.suslovila.client.render.tile.TileAntiNodeRenderer.renderAntiNode;
import static com.suslovila.utils.SUSUtils.*;
import static thaumcraft.client.renderers.tile.TileNodeRenderer.renderNode;

@Mixin(value = TileNodeRenderer.class, remap = false)
public abstract class MixinTileNodeRenderer extends TileEntitySpecialRenderer {

    @Inject(remap = true, method = "renderTileEntityAt", at = @At(value = "INVOKE", target = "Lthaumcraft/client/renderers/tile/TileNodeRenderer;renderNode(Lnet/minecraft/entity/EntityLivingBase;DZZFIIIFLthaumcraft/api/aspects/AspectList;Lthaumcraft/api/nodes/NodeType;Lthaumcraft/api/nodes/NodeModifier;)V"))
    public void renderTileEntityAtHEAD(TileEntity tile, double x, double y, double z, float partialTicks, CallbackInfo ci) {

        GL11.glPushMatrix();
        float size = 1.0F;
        double viewDistance = 64.0D;
        EntityLivingBase viewer = Minecraft.getMinecraft().renderViewEntity;
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

       if (tile != null && !(tile instanceof TileJarNode) && ((MixinTileNodeProvider)tile).isNodeBeingTransformed()) {
           int transformationTimer = ((MixinTileNodeProvider)tile).getTransformationTimer();
           float globalScaleTransformationFactor = (float)(halfConvertionTime - transformationTimer) / halfConvertionTime;
               glTranslateRandomEqualD(0.08 * globalScaleTransformationFactor);
               renderHungryNodeTransformation(transformationTimer, tile, viewer, viewDistance, condition, depthIgnore, size, tile.xCoord, tile.yCoord, tile.zCoord, partialTicks, ((INode)tile).getAspects(), ((INode)tile).getNodeType(), ((INode)tile).getNodeModifier());
       }
       else renderNode(viewer, viewDistance, condition, depthIgnore, size, tile.xCoord, tile.yCoord, tile.zCoord, partialTicks, ((INode)tile).getAspects(), ((INode)tile).getNodeType(), ((INode)tile).getNodeModifier());

       GL11.glPopMatrix();

    }


    @Redirect(remap = true, method = "renderTileEntityAt", at = @At(value = "INVOKE", target = "Lthaumcraft/client/renderers/tile/TileNodeRenderer;renderNode(Lnet/minecraft/entity/EntityLivingBase;DZZFIIIFLthaumcraft/api/aspects/AspectList;Lthaumcraft/api/nodes/NodeType;Lthaumcraft/api/nodes/NodeModifier;)V"))
    public void removeClassicRenderMethod(EntityLivingBase viewer, double viewDistance, boolean visible, boolean depthIgnore, float size, int x, int y, int z, float partialTicks, AspectList aspects, NodeType type, NodeModifier mod) {
    }





    //NOTHING NEW, THE ONLY THING I NEEDED WAS TO CHANGE THE SIZE todo: remove with injection
    private static void renderHungryNodeTransformation(int transformationTimer, TileEntity node, EntityLivingBase viewer, double viewDistance, boolean visible, boolean depthIgnore, float size, int x, int y, int z, float partialTicks, AspectList aspects, NodeType type, NodeModifier mod) {
        long nt = System.nanoTime();
        UtilsFX.bindTexture(transformationTimer > 120 ? com.suslovila.client.render.tile.TileAntiNodeRenderer.nodetex : TileNodeRenderer.nodetex);
        float globalScaleTransformationFactor = (transformationTimer <= 120 ? (float)(120 - transformationTimer) / 120 : (float) transformationTimer / 120);
        int frames = 32;
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
            GL11.glAlphaFunc(516, 0.003921569F);
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
                angle = (float)(time % (long)(5000 + 500 * count)) / (5000.0F + (float)(500 * count)) * rad;
                UtilsFX.renderFacingStrip((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, angle, scale * globalScaleTransformationFactor, alpha / Math.max(1.0F, (float)aspects.size() / 2.0F), frames, 0, i, partialTicks, aspect.getColor());
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


               //HUNGRY NODE CALCULATIONS
                    scale *= 0.75F;
                    GL11.glBlendFunc(770, 1);
                    strip = 3;


            GL11.glColor4f(1.0F, 0.0F, 1.0F, alpha);
            UtilsFX.renderFacingStrip((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, angle, scale * globalScaleTransformationFactor, alpha, frames, strip, i, partialTicks, 16777215);
            GL11.glDisable(3042);
            GL11.glPopMatrix();
            GL11.glPopMatrix();
            GL11.glEnable(2884);
            if(depthIgnore) {
                GL11.glEnable(2929);
            }

            GL11.glDepthMask(true);
            GL11.glAlphaFunc(516, 0.1F);
            GL11.glPopMatrix();
        } else {
            GL11.glPushMatrix();
            GL11.glAlphaFunc(516, 0.003921569F);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 1);
            GL11.glDepthMask(false);
            int i = (int)((nt / 40000000L + (long)x) % (long)frames);
            GL11.glColor4f(1.0F, 0.0F, 1.0F, 0.1F);
            UtilsFX.renderFacingStrip((double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, 0.0F, 0.5F * globalScaleTransformationFactor, 0.1F, frames, 1, i, partialTicks, 16777215);
            GL11.glDepthMask(true);
            GL11.glDisable(3042);
            GL11.glAlphaFunc(516, 0.1F);
            GL11.glPopMatrix();
        }
    }
}
