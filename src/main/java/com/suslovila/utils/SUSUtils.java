package com.suslovila.utils;

import net.minecraft.item.ItemStack;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.AspectList;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static org.lwjgl.opengl.GL11.*;

public class SUSUtils {
   public static Random random = new Random();
    public static int halfConvertionTime = 120;
    public static void glTranslateRandomEqualD(double factor){
        glTranslated(nextDouble(factor), nextDouble(factor), nextDouble(factor));
    }
    public static double nextDouble(double n1, double n2){
        return ThreadLocalRandom.current().nextDouble(n1, n2);
    }
    public static double nextDouble(double n1){
        return ThreadLocalRandom.current().nextDouble(-n1, n1);
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

}
