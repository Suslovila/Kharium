package com.suslovila.kharium.common.item

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.utils.ThaumcraftIntegrator
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.ChatComponentText
import net.minecraft.util.StatCollector
import net.minecraft.world.World
import thaumcraft.common.Thaumcraft
import thaumcraft.common.lib.research.ResearchManager

class ItemDiary : Item() {
    init {
        unlocalizedName = "diary"
        setTextureName(Kharium.MOD_ID + ":diary")
        setMaxStackSize(1)
        creativeTab = Kharium.tab

    }

    override fun onItemRightClick(stack: ItemStack, world: World, player: EntityPlayer): ItemStack {
        if (!world.isRemote && !ResearchManager.isResearchComplete(player.commandSenderName, "DIARY")) {
            if (Thaumcraft.proxy.playerKnowledge.getWarpTotal(player.commandSenderName) >= 100) {
                ThaumcraftIntegrator.completeNormalResearch("DIARY", player, world)
                --stack.stackSize
            } else player.addChatMessage(ChatComponentText("§5§o" + StatCollector.translateToLocal("diary.text")))

        }
        return stack
    }
}

