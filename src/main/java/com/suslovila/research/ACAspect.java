package com.suslovila.research;

import com.suslovila.ExampleMod;
import com.suslovila.api.utils.SUSUtils;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.config.ConfigItems;




public class ACAspect
{
    public static Aspect HUMILITAS;



  public static void initAspects() {
      HUMILITAS = new Aspect("Humilitas", SUSUtils.humilitasColor, new Aspect[]{ Aspect.TRAP, Aspect.VOID}, new ResourceLocation(ExampleMod.MOD_ID, "textures/aspects/humilitas.png"), 1){};
  }
    public static void initItemsAspects() {
      //nether star
       addAspectsToItem(new ItemStack(Items.nether_star), new AspectList().add(HUMILITAS, 8));
       addAspectsToItem(new ItemStack(ConfigItems.itemEldritchObject, 1, 3), new AspectList().add(HUMILITAS, 16));
    }
    private static void addAspectsToItem(ItemStack itemStack, AspectList aspectsToAdd){
        AspectList list = SUSUtils.INSTANCE.getAspectList(itemStack);
        list.add(aspectsToAdd);
        ThaumcraftApi.registerObjectTag(itemStack, list);
    }
 }