package com.suslovila.client.render;

import com.suslovila.client.particles.ParticleRenderDispatcher;
import com.suslovila.common.item.MyItem;
import com.suslovila.examplemod.ExampleMod;
import com.suslovila.utils.Vector3;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import static com.suslovila.common.item.MyItem.raytraceBlocks;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;

@SideOnly(Side.CLIENT)
public class ClientEventHandler {
    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        Profiler profiler = Minecraft.getMinecraft().mcProfiler;

        profiler.startSection("botania-particles");
        //ParticleRenderDispatcher.dispatch();

    }




    public static final ResourceLocation zoneTextureLocation = new ResourceLocation(ExampleMod.MOD_ID,"textures/zone.png");

    @SubscribeEvent
    public void render(RenderWorldLastEvent event) {
        Minecraft minecraft = Minecraft.getMinecraft();
        EntityClientPlayerMP player = minecraft.thePlayer;
        World world = player.worldObj;
        if (player.inventory.getCurrentItem() != null && player.inventory.getCurrentItem().getItem() instanceof MyItem) {
            Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
            MovingObjectPosition pickedBlock = raytraceBlocks(world, player, true, 100000);
            Vec3 lookVec = player.getLookVec();
            if (pickedBlock != null) {
                int x = pickedBlock.blockX;
                int y = pickedBlock.blockY;
                int z = pickedBlock.blockZ;
              glPushMatrix();
                glEnable(GL_BLEND);
                glBlendFunc(GL_SRC_ALPHA, GL_DST_ALPHA);
                Minecraft.getMinecraft().renderEngine.bindTexture(zoneTextureLocation);
                glTranslated(x - player.posX, y - player.posY, z - player.posZ);
                drawFakeBlock(0,1,0);
              glPopMatrix();
                Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
            glDisable(GL_BLEND);



//                glPushMatrix();
//                    Minecraft.getMinecraft().renderEngine.bindTexture(zoneTextureLocation);
//                    drawFakeBlock(0,0,0);
//                glPopMatrix();
//                Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);

            }
        }
    }
  public static void draw(Vector3 vector3, Block block, int meta, double minX, double minY, double minZ, World world) {
 Tessellator tessellator = Tessellator.instance;
tessellator.startDrawingQuads();
tessellator.setColorRGBA(255, 255, 255, 200);

    }


        /*     */   public static void drawFakeBlock(double minX, double minY, double minZ) {

        /*  16 */     double maxX = minX + 1.0D;
        /*  17 */     double maxY = minY + 1.0D;
        /*  18 */     double maxZ = minZ + 1.0D;
        /*  19 */     Tessellator tessellator = Tessellator.instance;
                        //GL11.glGetInteger(GL_TEXTURE);
        /*     */
        /*  21 */     tessellator.startDrawingQuads();
        /*  22 */     tessellator.setColorRGBA(255, 255, 255, 200);
        /*     */
        /*     */
        /*     */
        /*  26 */     float texMinU = getMinU();
        /*  27 */     float texMaxU = getMaxU();
        /*  28 */     float texMinV = getMinV();
        /*  29 */     float texMaxV = getMaxV();
        /*  30 */     tessellator.addVertexWithUV(minX, minY, minZ, texMinU, texMinV);
        /*  31 */     tessellator.addVertexWithUV(maxX, minY, minZ, texMaxU, texMinV);
        /*  32 */     tessellator.addVertexWithUV(maxX, minY, maxZ, texMaxU, texMaxV);
        /*  33 */     tessellator.addVertexWithUV(minX, minY, maxZ, texMinU, texMaxV);
        /*     */
        /*  35 */
        /*  39 */     tessellator.addVertexWithUV(minX, maxY, maxZ, texMinU, texMaxV);
        /*  40 */     tessellator.addVertexWithUV(maxX, maxY, maxZ, texMaxU, texMaxV);
        /*  41 */     tessellator.addVertexWithUV(maxX, maxY, minZ, texMaxU, texMinV);
        /*  42 */     tessellator.addVertexWithUV(minX, maxY, minZ, texMinU, texMinV);
        /*     */
        /*  44 */
        /*  48 */     tessellator.addVertexWithUV(maxX, minY, minZ, texMinU, texMaxV);
        /*  49 */     tessellator.addVertexWithUV(minX, minY, minZ, texMaxU, texMaxV);
        /*  50 */     tessellator.addVertexWithUV(minX, maxY, minZ, texMaxU, texMinV);
        /*  51 */     tessellator.addVertexWithUV(maxX, maxY, minZ, texMinU, texMinV);
        /*     */
        /*  53 */
        /*  57 */     tessellator.addVertexWithUV(minX, minY, maxZ, texMinU, texMaxV);
        /*  58 */     tessellator.addVertexWithUV(maxX, minY, maxZ, texMaxU, texMaxV);
        /*  59 */     tessellator.addVertexWithUV(maxX, maxY, maxZ, texMaxU, texMinV);
        /*  60 */     tessellator.addVertexWithUV(minX, maxY, maxZ, texMinU, texMinV);
        /*     */
        /*  62 */
        /*  66 */     tessellator.addVertexWithUV(minX, minY, minZ, texMinU, texMaxV);
        /*  67 */     tessellator.addVertexWithUV(minX, minY, maxZ, texMaxU, texMaxV);
        /*  68 */     tessellator.addVertexWithUV(minX, maxY, maxZ, texMaxU, texMinV);
        /*  69 */     tessellator.addVertexWithUV(minX, maxY, minZ, texMinU, texMinV);
        /*     */
        /*  71 */
        /*  75 */     tessellator.addVertexWithUV(maxX, minY, maxZ, texMinU, texMaxV);
        /*  76 */     tessellator.addVertexWithUV(maxX, minY, minZ, texMaxU, texMaxV);
        /*  77 */     tessellator.addVertexWithUV(maxX, maxY, minZ, texMaxU, texMinV);
        /*  78 */     tessellator.addVertexWithUV(maxX, maxY, maxZ, texMinU, texMinV);
        /*     */
        /*  80 */     tessellator.draw();
        /*     */   }
    /*     */
    /*     */
    /*     */   private static float getMinU() {
        /*  85 */     return 0;
        /*     */   }
    /*     */
    /*     */
    /*     */   private static float getMaxU() {

        /*  92 */     return 64;
        /*     */   }
    /*     */
    /*     */
    /*     */   private static float getMinV() {
        /*  98 */     return 0;
        /*     */   }
    /*     */
    /*     */
    /*     */   private static float getMaxV() {
        /* 103 */     return 64;
        /*     */   }
    /*     */ }

