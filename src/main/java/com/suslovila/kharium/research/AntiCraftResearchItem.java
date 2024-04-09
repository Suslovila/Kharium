package com.suslovila.kharium.research;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;

public class AntiCraftResearchItem extends ResearchItem {
    public AntiCraftResearchItem(String key, String category) {
        super(key, category);
    }


    public AntiCraftResearchItem(String key, String category, AspectList tags, int col, int row, int complex, ItemStack icon) {
        super(key, category, tags, col, row, complex, icon);
    }


    public AntiCraftResearchItem(String key, String category, AspectList tags, int col, int row, int complex, ResourceLocation icon) {
        super(key, category, tags, col, row, complex, icon);
    }


    @SideOnly(Side.CLIENT)
    public String getName() {
        return StatCollector.translateToLocal("ac.name." + this.key);
    }


    @SideOnly(Side.CLIENT)
    public String getText() {
        return StatCollector.translateToLocal("ac.tag." + this.key);
    }


    public ResearchItem setPages(ResearchPage... par) {
        for (ResearchPage page : par) {

            if (page.type == ResearchPage.PageType.TEXT) {
                page.text = "ac.text." + this.key + "." + page.text;
            }
            if (page.type == ResearchPage.PageType.INFUSION_CRAFTING)
                if (this.parentsHidden == null || this.parentsHidden.length == 0) {

                    this.parentsHidden = new String[]{"INFUSION"};
                } else {

                    String[] newParents = new String[this.parentsHidden.length + 1];
                    newParents[0] = "INFUSION";
                    for (int i = 0; i < this.parentsHidden.length; i++)
                        newParents[i + 1] = this.parentsHidden[i];
                    this.parentsHidden = newParents;
                }
        }
        return super.setPages(par);
    }
}
