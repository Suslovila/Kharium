package com.suslovila.kharium.utils

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import thaumcraft.api.ThaumcraftApiHelper
import thaumcraft.api.aspects.AspectList
import thaumcraft.common.Thaumcraft
import thaumcraft.common.lib.network.PacketHandler
import thaumcraft.common.lib.network.playerdata.PacketResearchComplete

object ThaumcraftIntegrator {

    fun completeNormalResearch(researchName: String, player: EntityPlayerMP, world: World) {

        PacketHandler.INSTANCE.sendTo(PacketResearchComplete(researchName), player)
        Thaumcraft.proxy.getResearchManager().completeResearch(player, researchName)
        world.playSoundAtEntity(player, "thaumcraft:learn", 0.75f, 1.0f)

    }

    fun completeResearchSilently(researchName: String?, player: EntityPlayer?) =
        Thaumcraft.proxy.getResearchManager().completeResearch(player, researchName)

    fun getAspectList(stack: ItemStack?) = ThaumcraftApiHelper.getObjectAspects(stack) ?: AspectList()

}