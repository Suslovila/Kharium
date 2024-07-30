package com.suslovila.kharium.common.item;

import com.suslovila.kharium.common.item.implants.*;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraftforge.common.util.EnumHelper;

public class ModItems {

    public static Item diary = new ItemDiary();
    public static Item crystallizedKharu = new ItemCrystallizedAntiMatter();

    public static Item.ToolMaterial voidMetalMaterial = EnumHelper.addToolMaterial("VOID_METAL", 3, 300, 6.2F, 2.0F, 20);

    public static void register() {
        GameRegistry.registerItem(crystallizedKharu, "crystallized_anti_matter");
        GameRegistry.registerItem(diary, "diary");

        GameRegistry.registerItem(ItemSpaceDivider.INSTANCE, ItemSpaceDivider.name);
        GameRegistry.registerItem(ItemRune.INSTANCE, ItemRune.name);
        GameRegistry.registerItem(ItemKharuNetConfigurator.INSTANCE, ItemKharuNetConfigurator.name);
        GameRegistry.registerItem(ItemPortableAspectContainer.INSTANCE, ItemPortableAspectContainer.name);
        registerImplants();
    }

    public static void registerImplants() {
        GameRegistry.registerItem(ImplantPhoenixHeart.INSTANCE, ImplantPhoenixHeart.name);
        GameRegistry.registerItem(ImplantPsiBlade.INSTANCE, ImplantPsiBlade.name);
        GameRegistry.registerItem(ImplantTimeAnchor.INSTANCE, ImplantTimeAnchor.name);
        GameRegistry.registerItem(ImplantSpaceDiver.INSTANCE, ImplantSpaceDiver.name);
        GameRegistry.registerItem(ImplantOverthinker.INSTANCE, ImplantOverthinker.name);
        GameRegistry.registerItem(ImplantMindBreaker.INSTANCE, ImplantMindBreaker.name);

    }
}
