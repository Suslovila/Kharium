package com.suslovila.client.render.block;


import com.suslovila.common.block.ModBlocks;
import com.suslovila.common.block.tileEntity.SmelterTile;
import com.suslovila.examplemod.ExampleMod;
import com.suslovila.utils.Vector3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.util.Vector;

import static org.lwjgl.opengl.GL11.*;

@SideOnly(Side.CLIENT)
public class RenderPoppetChest extends TileEntitySpecialRenderer {

   final ModelPoppetChest model = new ModelPoppetChest();
   private RenderItem renderItems = new RenderItem() {
      public byte getMiniItemCountForItemStack(ItemStack stack) {
         return (byte)1;
      }
      public byte getMiniBlockCountForItemStack(ItemStack stack) {
         return (byte)1;
      }
      public boolean shouldBob() {
         return false;
      }
      public boolean shouldSpreadItems() {
         return false;
      }
   };
   private static final ResourceLocation TEXTURE_URL = new ResourceLocation(ExampleMod.MOD_ID, "textures/blocks/poppetShelf.png");


   public RenderPoppetChest() {
      this.renderItems.setRenderManager(RenderManager.instance);
   }

   public void renderTileEntityAt(TileEntity tileEntity, double d, double d1, double d2, float f) {
      GL11.glPushMatrix();
      GL11.glTranslatef((float)d, (float)d1, (float)d2);
      SmelterTile tileEntityYour = (SmelterTile) tileEntity;
      this.renderPoppetChest(tileEntityYour, tileEntity.getWorldObj(), tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord, ModBlocks.SMELTER_BLOCK);
      GL11.glPopMatrix();
   }

   public void renderPoppetChest(SmelterTile te, World world, int x, int y, int z, Block block) {
      GL11.glPushMatrix();
      GL11.glTranslatef(0.5F, 0.5F, 0.5F);
      this.bindTexture(TEXTURE_URL);
      GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
      GL11.glTranslatef(-0.5F, 0.0F, -0.5F);
      this.model.render((Entity)null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
      GL11.glPopMatrix();
      if(world != null) {
         ItemStack newStack = null;
         float rotational = (float) Minecraft.getSystemTime() / 10F;
         EntityItem ei = new EntityItem(world);
         ei.hoverStart = 0.0F;
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
         GL11.glPushMatrix();
         GL11.glEnable('\u803a');
         GL11.glTranslatef(0.0F, 0.0F, 0.0F);
         OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 170.0F, 170.0F);
         GL11.glTranslatef(0.0F, 0.6F, 0.0F);
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

         float zShift = 0.5F;
         float yShift = 0.0F;
         float xShift = 0.5F;

         // boolean fancy = Witchery.proxy.getGraphicsLevel();
        ItemStack itemStack = te.getStack();
        int t = 0;
if(te.hasStack()){
    //xShift += 0.5F;
    GL11.glScalef(1F, 1F, 1F);
         newStack = te.getStack().copy();
         newStack.stackSize = 1;
         ei.setEntityItemStack(newStack);
         GL11.glPushMatrix();
         GL11.glTranslatef(xShift, yShift, zShift);

             Vector3 vec3 = new Vector3(Math.sin(Math.PI*Minecraft.getSystemTime()/1000)*0.5f, 0, Math.cos(Math.PI*Minecraft.getSystemTime()/1000)*0.5f);
            Vector3 vec32 = new Vector3(vec3.getZ(), 0, -vec3.getX());
            Vector3 xCord = new Vector3(1,0,0);
            Vector3 zCord = new Vector3(0,0,1);
            glTranslatef((float)vec3.getX(), 0.3f+(float)Math.sin(Math.PI*Minecraft.getSystemTime()/800)*0.5f, (float)vec3.getZ());
            GL11.glRotatef((float)(Math.toDegrees(vec32.angle(xCord)))*(vec32.dot(zCord) < 0 ? 1:-1), 0.0F, 1.0F, 0.0F);


         GL11.glPushMatrix();
         this.renderItems.doRender(ei, 0.0D, 0.0D, 0.0D, 0.0F, 0.0F);

         GL11.glPopMatrix();
         GL11.glPopMatrix();

      }

         GL11.glDisable('\u803a');
         GL11.glPopMatrix();
         GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      }

   }

}
