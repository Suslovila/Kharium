package com.suslovila.kharium.common.item.implants

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.api.implants.*
import com.suslovila.kharium.client.render.item.ItemSpaceDividerRenderer
import com.suslovila.kharium.common.worldSavedData.KharuInfluenceHandler.addKharu
import com.suslovila.kharium.utils.SusGraphicHelper
import com.suslovila.kharium.utils.SusNBTHelper.getOrCreateTag
import com.suslovila.kharium.utils.getPosition
import net.minecraft.entity.EntityLiving
import net.minecraft.item.ItemStack
import net.minecraft.util.DamageSource
import net.minecraftforge.client.event.RenderHandEvent
import net.minecraftforge.client.event.RenderPlayerEvent
import net.minecraftforge.event.entity.player.AttackEntityEvent
import org.lwjgl.opengl.GL11.*
import kotlin.math.abs

class ImplantPsiBlade : ItemImplant(ImplantType.HAND) {
    init {
        unlocalizedName = "blade"
        setTextureName(Kharium.MOD_ID + ":diary")
        setMaxStackSize(1)
        creativeTab = Kharium.tab
    }
    override fun onPlayerAttackEntityEvent(event: AttackEntityEvent, stack: ItemStack) {
        val lastHurt = "lastHurt"
        val tag = stack.getOrCreateTag()
        val worldTime = event.entityPlayer.worldObj.totalWorldTime
        val previousHitTime = tag.getLong(lastHurt)
        val basicDamage = 3.0f
        val basicReloading = 60
        val halfInvulnarableDelay = 10
        val hasEnoughTimePassed =
            abs(previousHitTime - worldTime) > (halfInvulnarableDelay * (9 - RuneUsingItem.getRuneAmountOfType(stack, RuneType.OVERCLOCK)))
        if (hasEnoughTimePassed) {
            val damage = basicDamage * (1 + RuneUsingItem.getRuneAmountOfType(stack, RuneType.EXPANSION) / 2)

            (event.target as? EntityLiving)?.run {
                attackEntityFrom(
                    DamageSource.outOfWorld,
                    damage
                )
                tag.setLong(lastHurt, worldTime)
            }
            event.entityPlayer.addKharu(10_00)
        }
        tag.setLong(
            lastHurt,
            if (tag.hasKey(lastHurt))
                tag.getLong(lastHurt)
            else worldTime
        )
    }


    override fun onRenderHandEvent(event: RenderHandEvent, stack: ItemStack) {
        ItemSpaceDividerRenderer.runeModel.renderAll()
    }

    override fun onRenderPlayerEvent(event: RenderPlayerEvent, stack: ItemStack) {
        glPushMatrix()
        glEnable(GL_CULL_FACE)
//        SusGraphicHelper.translateFromPlayerTo(event.entityPlayer.getPosition(), event.partialRenderTick)
//        GL11.glScaled(10.0, 10.0, 10.0)
        glColor4f(1.0f, 1.0f, 1.0f, 1.0f)
        ItemSpaceDividerRenderer.runeModel.renderAll()
        glPopMatrix()
    }
}