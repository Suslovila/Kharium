package com.suslovila.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.lib.network.PacketHandler;
import thaumcraft.common.lib.network.playerdata.PacketResearchComplete;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static org.lwjgl.opengl.GL11.*;

public class SUSUtils {
    //some static fields and functions to make life easier
   public static Random random = new Random();
    public static final int halfConvertionTime = 120;
    public static final int himilitasColor = 16727457;


    public static void glTranslateRandomEqualD(double factor){
        glTranslated(nextDouble(factor), nextDouble(factor), nextDouble(factor));
    }
    public static double nextDouble(double n1, double n2){
        return ThreadLocalRandom.current().nextDouble(n1, n2);
    }
    public static double nextDouble(double n1){
        return (n1 == 0) ? 0 : ThreadLocalRandom.current().nextDouble(-n1, n1);
    }
    public static float wrapDegrees(float p_14178_) {
        float f = p_14178_ % 360.0F;
        if (f >= 180.0F) {
            f -= 360.0F;
        }

        if (f < -180.0F) {
            f += 360.0F;
        }

        return f;
    }

    public static AspectList getAspectList(ItemStack stack) {
        AspectList list = ThaumcraftApiHelper.getObjectAspects(stack);
        return list != null ? list : new AspectList();
    }

    public static int randomSign(){
        return random.nextBoolean() ? 1 : -1;
    }

    public static void completeNormalResearch(String researchName, EntityPlayer player, World world){
        PacketHandler.INSTANCE.sendTo(new PacketResearchComplete(researchName), (EntityPlayerMP)player);
        Thaumcraft.proxy.getResearchManager().completeResearch(player, researchName);
        world.playSoundAtEntity(player, "thaumcraft:learn", 0.75f, 1.0f);
    }
}
