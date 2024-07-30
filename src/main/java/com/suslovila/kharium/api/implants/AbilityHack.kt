package com.suslovila.kharium.api.implants

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.utils.SusGraphicHelper
import com.suslovila.kharium.utils.SusGraphicHelper.pushBrightness
import com.suslovila.kharium.utils.SusNBTHelper.getOrCreateInteger
import com.suslovila.kharium.utils.SusNBTHelper.getOrCreateTag
import com.suslovila.kharium.utils.SusNBTHelper.getUUIDOrNull
import com.suslovila.kharium.utils.SusNBTHelper.setUUID
import com.suslovila.kharium.utils.SusWorldHelper
import net.minecraft.client.Minecraft
import net.minecraft.client.particle.EntityFX
import net.minecraft.client.renderer.Tessellator
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraft.util.MovingObjectPosition
import net.minecraftforge.client.event.RenderWorldLastEvent
import net.minecraftforge.event.entity.living.LivingEvent
import org.lwjgl.opengl.GL11.*

abstract class AbilityHack(name: String) : Ability(name) {
    companion object {
        val HACK_TIME_LEFT_NBT = Kharium.prefixAppender.doAndGet("hack")
        val HACK_VICTIM_UUID_NBT = Kharium.prefixAppender.doAndGet("victim_uuid")
        val HACK_VICTIM_ID_NBT = Kharium.prefixAppender.doAndGet("victim_id")
    }

    override fun isActive(implant: ItemStack): Boolean =
        isHacking(implant)

    abstract fun getHackTime(): Int
    abstract fun hackEntity(hacker: EntityPlayer, victim: Entity, slotIndex: Int, implant: ItemStack)
    override fun onEnableButtonClicked(player: EntityPlayer, index: Int, implant: ItemStack) {
        // only server here
        val tag = implant.getOrCreateTag()
        if (!isOnCooldown(implant) && !player.worldObj.isRemote) {
            val hitResult = SusWorldHelper.raytraceEntities(player.worldObj, player, Double.MAX_VALUE)
            val hitEntity = hitResult?.entityHit
            if (hitResult?.typeOfHit != MovingObjectPosition.MovingObjectType.ENTITY ||
                hitEntity == null
            ) {
                return
            }
            tag.setInteger(HACK_TIME_LEFT_NBT, getHackTime())
            tag.setUUID(HACK_VICTIM_UUID_NBT, hitEntity.persistentID)
            // sync memes
            tag.setInteger(HACK_VICTIM_ID_NBT, hitEntity.entityId)

            notifyClient(player, index, implant)
        }
    }

    override fun onPlayerUpdateEvent(event: LivingEvent.LivingUpdateEvent, index: Int, implant: ItemStack) {
        val world = event.entityLiving.worldObj
        val tag = implant.getOrCreateTag()
        val hackTime = tag.getOrCreateInteger(HACK_TIME_LEFT_NBT, 0)
        // for optimisation purposes we decrement timer on both sides
        if (world.isRemote) return

        if (isHacking(implant)) {
            val player = (event.entityLiving as? EntityPlayer) ?: return
            val previousFocusedEntityUUID = tag.getUUIDOrNull(HACK_VICTIM_UUID_NBT)
            if (previousFocusedEntityUUID == null) {
                sendToCooldown(implant)
                notifyClient(player, index, implant)
                return
            }
            val hitResult = SusWorldHelper.raytraceEntities(player.worldObj, player, Double.MAX_VALUE)
            val hitEntity = hitResult?.entityHit
            if (hitEntity == null) {
                sendToCooldown(implant)
                notifyClient(player, index, implant)
                return
            }
            val isHitEntityTheSame = hitEntity.persistentID == previousFocusedEntityUUID
            val isAlive = hitEntity.isEntityAlive
            if (!isHitEntityTheSame || !isAlive) {
                sendToCooldown(implant)
                notifyClient(player, index, implant)
                return
            }
            tag.setInteger(HACK_TIME_LEFT_NBT, hackTime - 1)

            // if after decreasing we are done :
            if (!isHacking(implant)) {
                hackEntity(player, hitEntity, index, implant)
                sendToCooldown(implant)
                notifyClient(player, index, implant)
                return
            }
        } else {
            super.onPlayerUpdateEvent(event, index, implant)
        }
    }

    override fun onRenderWorldLastEvent(event: RenderWorldLastEvent, index: Int, implant: ItemStack) {
        val player = Minecraft.getMinecraft()?.thePlayer ?: return
        val world = player.worldObj ?: return
        val tag = implant.getOrCreateTag()
//        if (isHacking(implant)) {
//            val victim = world.getEntityByID(tag.getInteger(HACK_VICTIM_ID_NBT)) ?: return
//            val tessellator = Tessellator.instance
//            val scale: Float = 0.1f
//            SusGraphicHelper.translateFromPlayerTo()
//            val destX = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.partialTicks
//            val destY = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.partialTicks
//            val destZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.partialTickspartialTicks
//            pushBrightness(tessellator)
//            tessellator.setBrightness(getBrightnessForRender(partialTick))
//
//            val u = 1.0
//            val` v = 1.0
//            tessellator.addVertexWithUV(
//                (destX - x * scale - u * scale).toDouble(),
//                (destY - y * scale).toDouble(),
//                (destZ - z * scale - v * scale).toDouble(),
//                0.0,
//                0.0
//            )
//            tessellator.addVertexWithUV(
//                (destX - x * scale + u * scale).toDouble(),
//                (destY + y * scale).toDouble(),
//                (destZ - z * scale + v * scale).toDouble(),
//                0.0,
//                1.0
//            )
//            tessellator.addVertexWithUV(
//                (destX + x * scale + u * scale).toDouble(),
//                (destY + y * scale).toDouble(),
//                (destZ + z * scale + v * scale).toDouble(),
//                1.0,
//                1.0
//            )
//            tessellator.addVertexWithUV(
//                (destX + x * scale - u * scale).toDouble(),
//                (destY - y * scale).toDouble(),
//                (destZ + z * scale - v * scale).toDouble(),
//                1.0,
//                0.0
//            )
//        }
    }

    override fun sendToCooldown(implant: ItemStack) {
        super.sendToCooldown(implant)
        val tag = implant.getOrCreateTag()
        tag.setInteger(HACK_TIME_LEFT_NBT, 0)
        tag.removeTag(HACK_VICTIM_UUID_NBT)
    }

    fun isHacking(implant: ItemStack): Boolean =
        implant.getOrCreateTag().getOrCreateInteger(HACK_TIME_LEFT_NBT, 0) != 0

    override fun isOnCooldown(implant: ItemStack): Boolean = implant.getOrCreateTag().let { tag ->
        return (tag.getOrCreateInteger(HACK_TIME_LEFT_NBT, 0) == 0 &&
                tag.getOrCreateInteger(COOLDOWN_NBT, 0) == 0)

    }
}