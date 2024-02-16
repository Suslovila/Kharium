package com.suslovila.common.block.container;

import com.suslovila.common.block.tileEntity.TileEssentiaReservoirVoid;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import thaumcraft.common.Thaumcraft;
import thaumcraft.common.blocks.BlockEssentiaReservoir;
import thaumcraft.common.config.ConfigBlocks;
import thaumcraft.common.tiles.*;

import static com.suslovila.common.block.ModBlocks.EssentiaReservoirVoidRI;

public class BlockEssentiaReservoirVoid extends BlockEssentiaReservoir {
    public IIcon icon = null;

    public BlockEssentiaReservoirVoid() {
        //super(Material.iron);
        this.setHardness(2.0F);
        this.setResistance(17.0F);
        setBlockName("blockEssentiaReservoirVoid");
        this.setStepSound(Block.soundTypeMetal);
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);

    }

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister ir) {
        this.icon = ir.registerIcon("thaumcraft:essentiareservoir");
    }

    public IIcon getIcon(int i, int md) {
        return this.icon;
    }

    public IIcon getIcon(IBlockAccess iblockaccess, int i, int j, int k, int side) {
        return this.icon;
    }

    public int getRenderType() {
        return EssentiaReservoirVoidRI;
    }

    public boolean isOpaqueCube() {
        return false;
    }

    public boolean renderAsNormalBlock() {
        return false;
    }

    public int getRenderBlockPass() {
        return 1;
    }

    public int damageDropped(int metadata) {
        return metadata;
    }

    public TileEntity createTileEntity(World world, int metadata) {
        return (TileEntity)(metadata == 0?new TileEssentiaReservoirVoid():super.createTileEntity(world, metadata));
    }

    public TileEntity createNewTileEntity(World var1, int md) {
        return null;
    }

    public boolean hasComparatorInputOverride() {
        return true;
    }

    public int getComparatorInputOverride(World world, int x, int y, int z, int rs) {
        TileEntity te = world.getTileEntity(x, y, z);
        if(te != null && te instanceof TileEssentiaReservoirVoid) {
            float r = (float)((TileEssentiaReservoirVoid)te).essentia.visSize() / (float)((TileEssentiaReservoirVoid)te).maxAmount;
            return MathHelper.floor_float(r * 14.0F) + (((TileEssentiaReservoirVoid)te).essentia.visSize() > 0?1:0);
        } else {
            return 0;
        }
    }

    public void breakBlock(World world, int x, int y, int z, Block par5, int par6) {
        TileEntity te = world.getTileEntity(x, y, z);
        if(te != null && te instanceof TileEssentiaReservoirVoid) {
            int sz = ((TileEssentiaReservoirVoid)te).essentia.visSize() / 16;
            int q = 0;
            if(sz > 0) {
                world.createExplosion((Entity)null, (double)x + 0.5D, (double)y + 0.5D, (double)z + 0.5D, 1.0F, false);

                for(int a = 0; a < 50; ++a) {
                    int xx = x + world.rand.nextInt(5) - world.rand.nextInt(5);
                    int yy = y + world.rand.nextInt(5) - world.rand.nextInt(5);
                    int zz = z + world.rand.nextInt(5) - world.rand.nextInt(5);
                    if(world.isAirBlock(xx, yy, zz)) {
                        if(yy < y) {
                            world.setBlock(xx, yy, zz, ConfigBlocks.blockFluxGoo, 8, 3);
                        } else {
                            world.setBlock(xx, yy, zz, ConfigBlocks.blockFluxGas, 8, 3);
                        }

                        if(q++ >= sz) {
                            break;
                        }
                    }
                }
            }
        }

        super.breakBlock(world, x, y, z, par5, par6);
    }
}
