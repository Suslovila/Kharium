package com.suslovila.common.event;
import com.suslovila.ExampleMod;
import com.suslovila.common.item.ModItems;
import com.suslovila.research.ACAspect;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.world.BlockEvent;
import org.lwjgl.input.Keyboard;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.nodes.NodeModifier;
import thaumcraft.api.nodes.NodeType;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.blocks.ItemJarNode;
import thaumcraft.common.config.ConfigItems;
import thaumcraft.common.entities.monster.boss.EntityEldritchWarden;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketSyncAspects;
import thaumcraft.common.lib.research.ResearchManager;
import thaumcraft.common.tiles.TileJarNode;

public class FMLEventListener {
//event class


    //event for comfort testing :D
    @SubscribeEvent
    public void giveAllAspects(PlayerEvent.PlayerLoggedInEvent event) {
        if (!event.player.worldObj.isRemote) {
            EntityPlayer player = event.player;
            for (Aspect aspect : Aspect.aspects.values()) {
                Thaumcraft.proxy.playerKnowledge.addAspectPool(player.getCommandSenderName(), aspect, (short) 10);
            }
            ResearchManager.scheduleSave(player);
            PacketHandler.INSTANCE.sendTo(new PacketSyncAspects(player), (EntityPlayerMP) player);
            ItemStack drop = new ItemStack(ConfigItems.itemJarNode);
            ((ItemJarNode) drop.getItem()).setAspects(drop, new AspectList().add(Aspect.HUNGER, 100).add(Aspect.WATER, 100).add(Aspect.ELDRITCH, 100).add(ACAspect.HIMILITAS, 100));
            ((ItemJarNode) drop.getItem()).setNodeAttributes(drop, NodeType.HUNGRY, NodeModifier.BRIGHT, "");
            event.player.worldObj.spawnEntityInWorld(new EntityItem(event.player.worldObj, player.posX, player.posY, player.posZ, drop));
        }
    }
    @SubscribeEvent
    public void addDiaryIntoDrops(LivingDropsEvent event) {
        if (event.entity instanceof EntityEldritchWarden) event.entity.entityDropItem(new ItemStack(ModItems.diary), 1.5F);
    }
}