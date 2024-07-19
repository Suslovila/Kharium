package com.suslovila.kharium.common.item

import com.suslovila.kharium.Kharium
import com.suslovila.kharium.api.rune.RuneType
import cpw.mods.fml.relauncher.Side
import cpw.mods.fml.relauncher.SideOnly
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.creativetab.CreativeTabs
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.util.IIcon


object ItemRune : Item() {
    const val name = "rune"
    lateinit var icons: Array<IIcon>

    init {
        unlocalizedName = name
        setTextureName(Kharium.MOD_ID + ":" + name)
        setMaxStackSize(64)
        setHasSubtypes(true)
        maxDurability = 0
        creativeTab = Kharium.tab
    }

    override fun registerIcons(register: IIconRegister) {
        icons = Array(RuneType.values().size) { index ->
            register.registerIcon("${Kharium.MOD_ID}:rune_${RuneType.values()[index]}")
        }
    }

    @SideOnly(Side.CLIENT)
    override fun getIconFromDamage(meta: Int): IIcon =
        if (meta < icons.size) icons[meta] else icons[0]


    override fun getUnlocalizedName(par1ItemStack: ItemStack): String {
        return unlocalizedName + "." + par1ItemStack.metadata
    }


    override fun getSubItems(item: Item?, tabs: CreativeTabs?, list: MutableList<Any?>) {
        RuneType.values().forEach { runeType ->
            list.add(ItemStack(this, 1, runeType.ordinal))
        }
    }
}
