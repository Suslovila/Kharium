package com.suslovila.kharium.common.item;

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
    }
}
