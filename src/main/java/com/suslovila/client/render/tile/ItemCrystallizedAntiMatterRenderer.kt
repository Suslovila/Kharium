package com.suslovila.client.render.tile

import com.suslovila.ExampleMod
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.IItemRenderer
import net.minecraftforge.client.model.AdvancedModelLoader
import net.minecraftforge.client.model.IModelCustom
import org.lwjgl.opengl.GL11
import thaumcraft.client.lib.UtilsFX

object ItemCrystallizedAntiMatterRenderer : IItemRenderer {
    val model: IModelCustom

    init {
        model = AdvancedModelLoader.loadModel(ResourceLocation(ExampleMod.MOD_ID, "models/items/anti_matter.obj"));
    }

    override fun handleRenderType(item: ItemStack?, type: IItemRenderer.ItemRenderType?) = true

    override fun shouldUseRenderHelper(
        type: IItemRenderer.ItemRenderType?,
        item: ItemStack?,
        helper: IItemRenderer.ItemRendererHelper?
    ) = true

    override fun renderItem(type: IItemRenderer.ItemRenderType, item: ItemStack?, vararg data: Any?) {
        GL11.glPushMatrix()
        UtilsFX.bindTexture(ExampleMod.MOD_ID, "textures/items/anti_matter.png")
        if (type == IItemRenderer.ItemRenderType.EQUIPPED || type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON) {
            GL11.glTranslatef(0.5f, 0.5f, 0.5f)

        }
        if (type == IItemRenderer.ItemRenderType.EQUIPPED_FIRST_PERSON) GL11.glTranslatef(0f, 0.15f, 0f)
        GL11.glScalef(2f, 2f, 2f)
        model.renderPart("inner_inner")
        renderOuterCristal()
        GL11.glPopMatrix()
    }

    private fun renderOuterCristal() {
        GL11.glDepthMask(false)
        GL11.glDisable(GL11.GL_CULL_FACE)
        GL11.glDisable(GL11.GL_ALPHA_TEST)
        GL11.glEnable(GL11.GL_BLEND)
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
        GL11.glDisable(GL11.GL_LIGHTING)

        GL11.glColor4f(1f, 1f, 1f, 0.3f)
        model.renderOnly("outer_outer")
        GL11.glEnable(GL11.GL_CULL_FACE)
        GL11.glEnable(GL11.GL_ALPHA_TEST)
        GL11.glDisable(GL11.GL_BLEND)
        GL11.glEnable(GL11.GL_LIGHTING)
        GL11.glDepthMask(true)
    }
}