/*    */
package com.suslovila.kharium.common.block;
/*    */
/*    */

import com.suslovila.kharium.Kharium;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.world.IBlockAccess;


public class BlockVoidMetal
        extends Block {
    public BlockVoidMetal() {
        super(Material.iron);
        setHardness(5.0F);
        setCreativeTab(Kharium.tab);
        textureName = (Kharium.MOD_ID + ":blockVoidMetal");
        setUnlocalizedName("blockVoidMetal");
        setStepSound(soundTypeMetal);
    }

    public boolean isBeaconBase(IBlockAccess w, int x, int y, int z, int x2, int y2, int z2) {
        return true;
    }
}