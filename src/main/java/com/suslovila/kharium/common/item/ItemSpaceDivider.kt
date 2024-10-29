package com.suslovila.kharium.common.item

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.utils.SusVec3
import com.suslovila.kharium.utils.SusWorldHelper
import com.suslovila.kharium.utils.SusWorldHelper.boundingBoxFromTwoVec
import com.suslovila.kharium.utils.getPosition
import com.suslovila.sus_multi_blocked.utils.WorldHelper.boundingBoxFromTwoPos
import net.minecraft.entity.EntityLiving
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.DamageSource
import net.minecraft.world.World
import kotlin.math.abs


object ItemSpaceDivider : Item() {
    const val name = "space_divider"

    init {
        unlocalizedName = name
        setTextureName(Kharium.MOD_ID + ":" + name)
        setMaxStackSize(1)
        creativeTab = Kharium.tab
        setFull3D()

    }

    override fun getMaxItemUseDuration(p_77626_1_: ItemStack?): Int {
        return 72000
    }

    override fun onItemRightClick(itemStackIn: ItemStack?, worldIn: World?, player: EntityPlayer): ItemStack? {
        player.setItemInUse(itemStackIn, getMaxItemUseDuration(itemStackIn))

        return itemStackIn
    }

    fun getMaxPreparationTime(stack: ItemStack?): Int {
//        stack?.getOrCreateTag()?.getInteger("")

        return 40
    }


    override fun onPlayerStoppedUsing(itemstack: ItemStack?, world: World, player: EntityPlayer, countdown: Int) {
        if (world.isRemote) {
            return
        }
        val playerPosBefore = player.getPosition()
        val facingVec = SusVec3(player.lookVec).normalize()
        val elapsedTicks = getMaxItemUseDuration(itemstack) - countdown
        player.fallDistance = 0.0f
        val maxTeleportDistance = 10 * elapsedTicks / 20
        val hitMOP = SusWorldHelper.doCustomRayTrace(world, player, true, maxTeleportDistance.toDouble())
        if (hitMOP != null) {
            SusWorldHelper.teleportEntity(player, hitMOP)
        } else {
            val position = player.getPosition()
                .add(facingVec.scale(maxTeleportDistance.toDouble()))
                .add(SusVec3(0.0, player.eyeHeight, 0.0))
            SusWorldHelper.teleportEntity(player, position)
        }
        val playerPosAfter = player.getPosition()

        val zone = boundingBoxFromTwoVec(playerPosBefore, playerPosAfter).expand(2.0, 2.0, 2.0)
        world.getEntitiesWithinAABBExcludingEntity(player, zone).forEach { entity ->
            if (entity !is EntityLivingBase) return@forEach
            val distance = abs(entity.getPosition().subtract(playerPosBefore).cross(facingVec).length())
            if(distance < 5) {
                entity.attackEntityFrom(DamageSource.outOfWorld, 10f)
            }
        }
    }
}
