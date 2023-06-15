package com.suslovila.common.event;

import com.suslovila.utils.mixins.EssenceContainer;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.BlockEvent;

public class SweetMixinListener {
    @SubscribeEvent
    public void onEntityKill(LivingHurtEvent event) {
//        if (event.source.getEntity() instanceof EntityPlayer && !event.source.getEntity().worldObj.isRemote) {
//            EssenceContainer essencePlayer = (EssenceContainer) event.source.getEntity();
//            essencePlayer.setEssence(essencePlayer.getEssence() + 1.0F);
//            if (!event.source.getEntity().worldObj.isRemote)
//                ((EntityPlayer) event.source.getEntity()).addChatMessage(new ChatComponentText("Ваш уровень эссенции: " + essencePlayer.getEssence()));
//
//        }
    }

    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
//        int t = 0;
//        if (event.getPlayer() != null) {
//            EssenceContainer essencePlayer = (EssenceContainer) event.getPlayer();
//            if (essencePlayer.getEssence() > 0) {
//                essencePlayer.setEssence(essencePlayer.getEssence() - 1.0F);
//            } else event.setCanceled(true);
//        }
   }
}
