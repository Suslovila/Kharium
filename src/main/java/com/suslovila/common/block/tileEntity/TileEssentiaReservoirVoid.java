package com.suslovila.common.block.tileEntity;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.ThaumcraftApiHelper;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.IEssentiaTransport;
import thaumcraft.common.tiles.TileEssentiaReservoir;

import java.awt.*;

public class TileEssentiaReservoirVoid extends TileEssentiaReservoir {
    //copying stuff from TileEssentiaReservoir class
    int count = 0;
    float tr = 1.0F;
    float tri = 0.0F;
    float tg = 1.0F;
    float tgi = 0.0F;
    float tb = 1.0F;
    float tbi = 0.0F;


    public void updateEntity() {
        super.updateEntity();
        ++count;
        if (!this.worldObj.isRemote && this.count % 5 == 0) {
            fillReservoir();
        }

        if (this.worldObj.isRemote) {
            int vs = this.essentia.visSize();
            if (vs > 0) {
                if (this.worldObj.rand.nextInt(500 - vs) == 0) {
                    this.worldObj.playSound((double) this.xCoord + 0.5D, (double) this.yCoord + 0.5D, (double) this.zCoord + 0.5D, "thaumcraft:creak", 1.0F, 1.4F + this.worldObj.rand.nextFloat() * 0.2F, false);
                }

                if (this.count % 20 == 0 && this.essentia.size() > 0) {
                    this.displayAspect = this.essentia.getAspects()[this.count / 20 % this.essentia.size()];
                    Color c = new Color(this.displayAspect.getColor());
                    this.tr = (float) c.getRed() / 255.0F;
                    this.tg = (float) c.getGreen() / 255.0F;
                    this.tb = (float) c.getBlue() / 255.0F;
                    this.tri = (this.cr - this.tr) / 20.0F;
                    this.tgi = (this.cg - this.tg) / 20.0F;
                    this.tbi = (this.cb - this.tb) / 20.0F;
                }

                if (this.displayAspect == null) {
                    this.tr = this.tg = this.tb = 1.0F;
                    this.tri = this.tgi = this.tbi = 0.0F;
                } else {
                    this.cr -= this.tri;
                    this.cg -= this.tgi;
                    this.cb -= this.tbi;
                }
            }
        }
    }

    void fillReservoir() {
        TileEntity te = ThaumcraftApiHelper.getConnectableTile(this.worldObj, this.xCoord, this.yCoord, this.zCoord, this.facing);
        if (te != null) {
            IEssentiaTransport ic = (IEssentiaTransport) te;
            if (!ic.canOutputTo(this.facing.getOpposite())) {
                return;
            }

            Aspect ta = null;
            if (ic.getEssentiaAmount(this.facing.getOpposite()) > 0 && ic.getSuctionAmount(this.facing.getOpposite()) < this.getSuctionAmount(this.facing) && this.getSuctionAmount(this.facing) >= ic.getMinimumSuction()) {
                ta = ic.getEssentiaType(this.facing.getOpposite());
            }

            if (ta != null && ic.getSuctionAmount(this.facing.getOpposite()) < this.getSuctionAmount(this.facing)) {
                this.addToContainer(ta, ic.takeEssentia(ta, 1, this.facing.getOpposite()));
            }
        }
    }

    public int addToContainer(Aspect tt, int am) {
        if (am == 0) {
            return am;
        } else {
            int space = this.maxAmount - this.essentia.visSize();
            if (space >= am) {
                this.essentia.add(tt, am);
                am = 0;
            } else {
                this.essentia.add(tt, space);
                //burning all redundant essentia
                am = 0;
            }

            if (space > 0) {
                this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
                this.markDirty();
            }

            return am;
        }
    }

    public int getSuctionAmount(ForgeDirection loc) {
        return 24;
    }

}
