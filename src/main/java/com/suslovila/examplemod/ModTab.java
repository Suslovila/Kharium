package com.suslovila.examplemod;

import com.suslovila.common.item.ModItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class ModTab extends CreativeTabs {

    public static final ModTab INSTANCE = new ModTab();

    private ModTab() {
        super(ExampleMod.MOD_ID);
        setBackgroundImageName("item_search.png");// Чтобы отображалось поле поиска
        setBackgroundImageName("mcmodding.png");


    }
    @Override
    public int getSearchbarWidth() {
        return 70;
    }
    @Override
    public boolean hasSearchBar() {
        return true;
    }
    @Override
    @SideOnly(Side.CLIENT)
    public Item getTabIconItem() {
        return Items.apple;
    }
}