package com.suslovila.mixin;

import com.suslovila.utils.SusMathHelper;
import com.suslovila.mixinUtils.IMixinTileNodeProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
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
        if (viewer instanceof EntityPlayer) {
            if (tile instanceof TileJarNode) {
                condition = true;
                size = 0.7F;
            } else if (((EntityPlayer) viewer).inventory.armorItemInSlot(3) != null && ((EntityPlayer) viewer).inventory.armorItemInSlot(3).getItem() instanceof IRevealer && ((IRevealer) ((EntityPlayer) viewer).inventory.armorItemInSlot(3).getItem()).showNodes(((EntityPlayer) viewer).inventory.armorItemInSlot(3), viewer)) {
                condition = true;
                depthIgnore = true;
            } else if (((EntityPlayer) viewer).inventory.getCurrentItem() != null && ((EntityPlayer) viewer).inventory.getCurrentItem().getItem() instanceof ItemThaumometer && UtilsFX.isVisibleTo(0.44F, viewer, (double) tile.xCoord, (double) tile.yCoord, (double) tile.zCoord)) {
                condition = true;
                depthIgnore = true;
                viewDistance = 48.0D;
            }
        }

        if (tile != null && !(tile instanceof TileJarNode) && ((IMixinTileNodeProvider) tile).isNodeBeingTransformed()) {
            int transformationTimer = ((IMixinTileNodeProvider) tile).getTransformationTimer();
            int requiredTime = ((IMixinTileNodeProvider) tile).getRequiredTimeForTransformation();
            float globalScaleTransformationFactor = (float) (requiredTime - transformationTimer) / requiredTime;
            SusMathHelper.INSTANCE.glTranslateRandomWithEqualD(0.08 * globalScaleTransformationFactor);
            renderNode(viewer, viewDistance, condition, depthIgnore, size * globalScaleTransformationFactor, tile.xCoord, tile.yCoord, tile.zCoord, partialTicks, ((INode) tile).getAspects(), ((INode) tile).getNodeType(), ((INode) tile).getNodeModifier());
            //renderHungryNodeTransformation(transformationTimer, requiredTime, tile, viewer, viewDistance, condition, depthIgnore, size, tile.xCoord, tile.yCoord, tile.zCoord, partialTicks, ((INode) tile).getAspects(), ((INode) tile).getNodeType(), ((INode) tile).getNodeModifier());
        } else
            renderNode(viewer, viewDistance, condition, depthIgnore, size, tile.xCoord, tile.yCoord, tile.zCoord, partialTicks, ((INode) tile).getAspects(), ((INode) tile).getNodeType(), ((INode) tile).getNodeModifier());

        GL11.glPopMatrix();

    }


    @Redirect(remap = true, method = "renderTileEntityAt", at = @At(value = "INVOKE", target = "Lthaumcraft/client/renderers/tile/TileNodeRenderer;renderNode(Lnet/minecraft/entity/EntityLivingBase;DZZFIIIFLthaumcraft/api/aspects/AspectList;Lthaumcraft/api/nodes/NodeType;Lthaumcraft/api/nodes/NodeModifier;)V"))
    public void removeClassicRenderMethod(EntityLivingBase viewer, double viewDistance, boolean visible, boolean depthIgnore, float size, int x, int y, int z, float partialTicks, AspectList aspects, NodeType type, NodeModifier mod) {
    }
}
