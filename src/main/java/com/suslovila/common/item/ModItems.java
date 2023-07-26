package com.suslovila.common.item;

import com.suslovila.client.render.item.ItemCrystallizedAntiMatter;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModItems {


    public static void register(){
       GameRegistry.registerItem(new ItemCrystallizedAntiMatter(), "crystallized_anti_matter");
    }
}
