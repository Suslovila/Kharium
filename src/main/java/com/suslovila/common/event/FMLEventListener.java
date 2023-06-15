package com.suslovila.common.event;

import com.suslovila.utils.mixins.EssenceContainer;
import com.suslovila.common.network.ServerMessagePacket;
import com.suslovila.examplemod.ExampleMod;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.BlockEvent;
import org.lwjgl.input.Keyboard;

public class FMLEventListener {
    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        // Если игрока зовут "PlayerName", то отправляем ему сообщение с приветствием
        if (event.player.getCommandSenderName().equals("Player312")) {
            event.player.addChatMessage(new ChatComponentText(String.format("Привет, %s!", event.player.getCommandSenderName())));
        }
    }
    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (Keyboard.isKeyDown(Keyboard.KEY_B)) {
            ExampleMod.NETWORK.sendToServer(new ServerMessagePacket("Привет мир!", 1337));
        }
    }

    @SubscribeEvent
    public static void onEntityKill(LivingHurtEvent event) {
       if (event.source.getEntity() instanceof EntityPlayer) {
            EssenceContainer essencePlayer = (EssenceContainer) event.source.getEntity();
           essencePlayer.setEssence(essencePlayer.getEssence() + 1.0F);
        }
   }

   @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        int t = 0;
       if (event.getPlayer() != null) {
           EssenceContainer essencePlayer = (EssenceContainer) event.getPlayer();
            if (essencePlayer.getEssence() > 0) {
               essencePlayer.setEssence(essencePlayer.getEssence() - 1.0F);
            } else event.setCanceled(true);
       }
   }
}