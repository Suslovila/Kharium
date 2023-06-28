package com.suslovila.research;

import com.suslovila.ExampleMod;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.aspects.Aspect;

import java.awt.*;

public class ACAspect
{
 public static Aspect NEGATIO;
  public static float NEGATIO_COLOR_HSV = 0.0F;


  public static void initAspects() {
        NEGATIO = new Aspect("Negatio", 16761294, null, new ResourceLocation(ExampleMod.MOD_ID, "textures/aspects/negatio.png"), 1){
        /*    */
        /*    */         public int getColor()
        /*    */         {
            /* 22 */           ACAspect.NEGATIO_COLOR_HSV = 0.6f;
            /* 23 */           return Color.HSBtoRGB(ACAspect.NEGATIO_COLOR_HSV * 3.0F % 360.0F / 360.0F, 1F, 1F);
            /*    */         }
                    public boolean isPrimal() {
                            return false;
        }
            /*    */       };

 }
 }