package com.suslovila.utils;

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
}
