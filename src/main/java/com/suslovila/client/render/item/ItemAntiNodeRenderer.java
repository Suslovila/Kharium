package com.suslovila.client.render.item;

import com.suslovila.client.render.tile.tileAntiNodeController.TileAntiNodeRenderer;
import com.suslovila.common.block.ModBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.INode;
import thaumcraft.api.nodes.NodeType;
import thaumcraft.client.lib.UtilsFX;
import thaumcraft.client.renderers.tile.ItemNodeRenderer;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.tiles.TileNode;

public class ItemAntiNodeRenderer extends ItemNodeRenderer {
    AspectList aspects = (new AspectList()).add(Aspect.AIR, 40).add(Aspect.FIRE, 40).add(Aspect.EARTH, 40).add(Aspect.WATER, 40);

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return item != null && item.getItem() == Item.getItemFromBlock(ModBlocks.ANTI_NODE);
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return helper != ItemRendererHelper.EQUIPPED_BLOCK;
    }

    //TAKEN FROM THAUMCRAFT
    //TODO: REWRITE
    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        if (type == ItemRenderType.ENTITY) {
            GL11.glTranslatef(-0.5F, -0.25F, -0.5F);
        } else if (type == ItemRenderType.EQUIPPED && data[1] instanceof EntityPlayer) {
            GL11.glTranslatef(0.0F, 0.0F, -0.5F);
        }

        TileNode tjf = new TileNode();
        tjf.setAspects(this.aspects);
        tjf.setNodeType(NodeType.NORMAL);
        tjf.blockType = ConfigBlocks.blockAiry;
        tjf.blockMetadata = 0;
        GL11.glPushMatrix();
        GL11.glTranslated(0.5D, 0.5D, 0.5D);
        GL11.glScaled(2.0D, 2.0D, 2.0D);
        renderItemNode(tjf);
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        renderItemNode(tjf);
        GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
        renderItemNode(tjf);
        GL11.glPopMatrix();
        //GL11.glEnable('耺');
    }

    public static void renderItemNode(INode node) {
        if (node.getAspects().size() > 0) {
            EntityLivingBase viewer = Minecraft.getMinecraft().renderViewEntity;
            float alpha = 0.5F;
            if (node.getNodeModifier() != null) {
                switch (node.getNodeModifier()) {
                    case BRIGHT:
                        alpha *= 1.5F;
                        break;
                    case PALE:
                        alpha *= 0.66F;
                        break;
                    case FADING:
                        alpha *= MathHelper.sin((float) viewer.ticksExisted / 3.0F) * 0.25F + 0.33F;
                }
            }

            GL11.glPushMatrix();
            GL11.glAlphaFunc(516, 0.003921569F);
            GL11.glDepthMask(false);
            GL11.glDisable(2884);
            long nt = System.nanoTime();
            long time = nt / 5000000L;
            float bscale = 0.25F;
            GL11.glPushMatrix();
            float rad = 6.2831855F;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, alpha);
            UtilsFX.bindTexture(TileAntiNodeRenderer.INSTANCE.getAntiNodeTexture());
            int frames = 32;
            int i = (int) ((nt / 40000000L + 1L) % (long) frames);
            int count = 0;
            float scale = 0.0F;
            float average = 0.0F;

            for (Aspect aspect : node.getAspects().getAspects()) {
                if (aspect.getBlend() == 771) {
                    alpha = (float) ((double) alpha * 1.5D);
                }

                average += (float) node.getAspects().getAmount(aspect);
                GL11.glPushMatrix();
                GL11.glEnable(3042);
                GL11.glBlendFunc(770, aspect.getBlend());
                scale = MathHelper.sin((float) viewer.ticksExisted / (14.0F - (float) count)) * bscale + bscale * 2.0F;
                scale = 0.2F + scale * ((float) node.getAspects().getAmount(aspect) / 50.0F);
                UtilsFX.renderAnimatedQuadStrip(scale, alpha / (float) node.getAspects().size(), frames, 0, i, 0.0F, aspect.getColor());
                GL11.glDisable(3042);
                GL11.glPopMatrix();
                ++count;
                if (aspect.getBlend() == 771) {
                    alpha = (float) ((double) alpha / 1.5D);
                }
            }

            average = average / (float) node.getAspects().size();
            GL11.glPushMatrix();
            GL11.glEnable(3042);
            i = (int) ((nt / 40000000L + 1L) % (long) frames);
            scale = 0.1F + average / 150.0F;
            int strip = 1;
            switch (node.getNodeType()) {
                case NORMAL:
                    GL11.glBlendFunc(770, 1);
                    break;
                case UNSTABLE:
                    GL11.glBlendFunc(770, 1);
                    strip = 6;
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
            UtilsFX.renderAnimatedQuadStrip(scale, alpha, frames, strip, i, 0.0F, 16777215);
            GL11.glDisable(3042);
            GL11.glPopMatrix();
            GL11.glPopMatrix();
            GL11.glEnable(2884);
            GL11.glDepthMask(true);
            GL11.glAlphaFunc(516, 0.1F);
            GL11.glPopMatrix();
        }

    }
}
