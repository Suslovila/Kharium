package com.suslovila.research;

import com.suslovila.ExampleMod;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.config.ConfigItems;

import java.awt.*;

import static com.suslovila.utils.SUSUtils.getAspectList;


public class ACAspect
{
    public static Aspect HIMILITAS;



  public static void initAspects() {
      HIMILITAS = new Aspect("Himilitas", 16727457, new Aspect[]{ Aspect.TRAP, Aspect.VOID}, new ResourceLocation(ExampleMod.MOD_ID, "textures/aspects/himilitas.png"), 1){};
  }
    public static void initItemsAspects() {
      //nether star
       addAspectsToItem(new ItemStack(Items.nether_star), new AspectList().add(HIMILITAS, 8));
       addAspectsToItem(new ItemStack(ConfigItems.itemEldritchObject, 1, 3), new AspectList().add(HIMILITAS, 16));
    }
    private static void addAspectsToItem(ItemStack itemStack, AspectList aspectsToAdd){
        AspectList list = getAspectList(itemStack);
        list.add(aspectsToAdd);
        ThaumcraftApi.registerObjectTag(itemStack, list);
    }
 }