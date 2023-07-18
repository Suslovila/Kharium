package com.suslovila.research;

import com.suslovila.ExampleMod;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.aspects.Aspect;

import java.awt.*;

public class ACAspect
{
    public static Aspect HIMILITAS;



  public static void initAspects() {
      HIMILITAS = new Aspect("Himilitas", 20566100, new Aspect[]{ Aspect.TRAP, Aspect.VOID}, new ResourceLocation(ExampleMod.MOD_ID, "textures/aspects/himilitas.png"), 1){};

 }
 }