package com.suslovila.common.item;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

public class ModItems {

    public static Item diary = new ItemDiary();
    public static Item crystallizedKharu = new ItemCrystallizedAntiMatter();

    public static void register(){
       GameRegistry.registerItem(crystallizedKharu, "crystallized_anti_matter");
        GameRegistry.registerItem(diary, "diary");
    }
}
