package tech.funkyra.mixins.client;

import com.suslovila.common.block.tileEntity.tileAntiNodeController.TileAntiNodeControllerBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.MinecraftForgeClient;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(RenderGlobal.class)
public class MixinEntityRenderer{

    @Shadow(remap = false)
    public int renderEntitiesStartupCounter;
    @Shadow(remap = false)
    public WorldClient theWorld;
    @Shadow(remap = false)
    public int countEntitiesTotal;
    @Shadow(remap = false)
    public int countEntitiesRendered;
    @Shadow(remap = false)
    public int countEntitiesHidden;
    @Shadow(remap = false)
    public boolean displayListEntitiesDirty;
    @Shadow(remap = false)
    public Minecraft mc;
    @Shadow(remap = false)
    public int displayListEntities;
    @Shadow(remap = false)
    public List tileEntities;
    /**
     * @author
     * @reason
     */
    @Overwrite
    public void renderEntities(EntityLivingBase p_147589_1_, ICamera p_147589_2_, float p_147589_3_)
    {
        int pass = MinecraftForgeClient.getRenderPass();
        if (this.renderEntitiesStartupCounter > 0)
        {
            if (pass > 0) return;
            --this.renderEntitiesStartupCounter;
        }
        else
        {
            double d0 = p_147589_1_.prevPosX + (p_147589_1_.posX - p_147589_1_.prevPosX) * (double)p_147589_3_;
            double d1 = p_147589_1_.prevPosY + (p_147589_1_.posY - p_147589_1_.prevPosY) * (double)p_147589_3_;
            double d2 = p_147589_1_.prevPosZ + (p_147589_1_.posZ - p_147589_1_.prevPosZ) * (double)p_147589_3_;
            this.theWorld.theProfiler.startSection("prepare");
            TileEntityRendererDispatcher.instance.cacheActiveRenderInfo(this.theWorld, this.mc.getTextureManager(), this.mc.fontRenderer, this.mc.renderViewEntity, p_147589_3_);
            RenderManager.instance.cacheActiveRenderInfo(this.theWorld, this.mc.getTextureManager(), this.mc.fontRenderer, this.mc.renderViewEntity, this.mc.pointedEntity, this.mc.gameSettings, p_147589_3_);
            if (pass == 0) // no indentation to shrink patch
            {
                this.countEntitiesTotal = 0;
                this.countEntitiesRendered = 0;
                this.countEntitiesHidden = 0;
            }
            EntityLivingBase entitylivingbase1 = this.mc.renderViewEntity;
            double d3 = entitylivingbase1.lastTickPosX + (entitylivingbase1.posX - entitylivingbase1.lastTickPosX) * (double)p_147589_3_;
            double d4 = entitylivingbase1.lastTickPosY + (entitylivingbase1.posY - entitylivingbase1.lastTickPosY) * (double)p_147589_3_;
            double d5 = entitylivingbase1.lastTickPosZ + (entitylivingbase1.posZ - entitylivingbase1.lastTickPosZ) * (double)p_147589_3_;
            TileEntityRendererDispatcher.staticPlayerX = d3;
            TileEntityRendererDispatcher.staticPlayerY = d4;
            TileEntityRendererDispatcher.staticPlayerZ = d5;
            this.theWorld.theProfiler.endStartSection("staticentities");

            if (this.displayListEntitiesDirty)
            {
                RenderManager.renderPosX = 0.0D;
                RenderManager.renderPosY = 0.0D;
                RenderManager.renderPosZ = 0.0D;
                this.rebuildDisplayListEntities();
            }

            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glPushMatrix();
            GL11.glTranslated(-d3, -d4, -d5);
            GL11.glCallList(this.displayListEntities);
            GL11.glPopMatrix();
            RenderManager.renderPosX = d3;
            RenderManager.renderPosY = d4;
            RenderManager.renderPosZ = d5;
            this.mc.entityRenderer.enableLightmap((double)p_147589_3_);
            this.theWorld.theProfiler.endStartSection("global");
            List list = this.theWorld.getLoadedEntityList();
            if (pass == 0) // no indentation for smaller patch size
            {
                this.countEntitiesTotal = list.size();
            }
            int i;
            Entity entity;

            for (i = 0; i < this.theWorld.weatherEffects.size(); ++i)
            {
                entity = (Entity)this.theWorld.weatherEffects.get(i);
                if (!entity.shouldRenderInPass(pass)) continue;
                ++this.countEntitiesRendered;

                if (entity.isInRangeToRender3d(d0, d1, d2))
                {
                    RenderManager.instance.renderEntitySimple(entity, p_147589_3_);
                }
            }

            this.theWorld.theProfiler.endStartSection("entities");

            for (i = 0; i < list.size(); ++i)
            {
                entity = (Entity)list.get(i);
                if (!entity.shouldRenderInPass(pass)) continue;
                boolean flag = entity.isInRangeToRender3d(d0, d1, d2) && (entity.ignoreFrustumCheck || p_147589_2_.isBoundingBoxInFrustum(entity.boundingBox) || entity.riddenByEntity == this.mc.thePlayer);

                if (!flag && entity instanceof EntityLiving)
                {
                    EntityLiving entityliving = (EntityLiving)entity;

                    if (entityliving.getLeashed() && entityliving.getLeashedToEntity() != null)
                    {
                        Entity entity1 = entityliving.getLeashedToEntity();
                        flag = p_147589_2_.isBoundingBoxInFrustum(entity1.boundingBox);
                    }
                }

                if (flag && (entity != this.mc.renderViewEntity || this.mc.gameSettings.thirdPersonView != 0 || this.mc.renderViewEntity.isPlayerSleeping()) && this.theWorld.blockExists(MathHelper.floor_double(entity.posX), 0, MathHelper.floor_double(entity.posZ)))
                {
                    ++this.countEntitiesRendered;
                    RenderManager.instance.renderEntitySimple(entity, p_147589_3_);
                }
            }

            this.theWorld.theProfiler.endStartSection("blockentities");
            RenderHelper.enableStandardItemLighting();

            for (i = 0; i < this.tileEntities.size(); ++i)
            {
                TileEntity tile = (TileEntity)this.tileEntities.get(i);
                if(!(tile instanceof TileAntiNodeControllerBase)) {
                    if (tile.shouldRenderInPass(pass) && p_147589_2_.isBoundingBoxInFrustum(tile.getRenderBoundingBox())) {
                        TileEntityRendererDispatcher.instance.renderTileEntity(tile, p_147589_3_);
                    }
                }
            }
            for (i = 0; i < this.tileEntities.size(); ++i)
            {
                TileEntity tile = (TileEntity)this.tileEntities.get(i);
                if((tile instanceof TileAntiNodeControllerBase)) {
                    if (tile.shouldRenderInPass(pass) && p_147589_2_.isBoundingBoxInFrustum(tile.getRenderBoundingBox())) {
                        TileEntityRendererDispatcher.instance.renderTileEntity(tile, p_147589_3_);
                    }
                }
            }
            this.mc.entityRenderer.disableLightmap((double)p_147589_3_);
            this.theWorld.theProfiler.endSection();
        }
    }
    public void rebuildDisplayListEntities()
    {
        this.theWorld.theProfiler.startSection("staticentityrebuild");
        GL11.glPushMatrix();
        GL11.glNewList(this.displayListEntities, GL11.GL_COMPILE);
        List list = this.theWorld.getLoadedEntityList();
        this.displayListEntitiesDirty = false;

        for (int i = 0; i < list.size(); ++i)
        {
            Entity entity = (Entity)list.get(i);

            if (RenderManager.instance.getEntityRenderObject(entity).isStaticEntity())
            {
                this.displayListEntitiesDirty = this.displayListEntitiesDirty || !RenderManager.instance.renderEntityStatic(entity, 0.0F, true);
            }
        }

        GL11.glEndList();
        GL11.glPopMatrix();
        this.theWorld.theProfiler.endSection();
    }
}
