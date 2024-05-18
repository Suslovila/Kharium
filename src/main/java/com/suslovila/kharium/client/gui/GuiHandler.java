package com.suslovila.kharium.client.gui;

import com.suslovila.common.inventory.container.ContainerKharuSnare;
import com.suslovila.kharium.common.multiStructure.kharuSnare.TileKharuSnare;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler {
    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if (!world.blockExists(x, y, z)) {
            return null;
        }

        TileEntity tile = world.getTileEntity(x, y, z);

        switch (id) {
            case GuiIds.KHARU_SNARE: {
                if (!(tile instanceof TileKharuSnare)) {
                    return null;
                }
                return new ContainerKharuSnare(player.inventory, (TileKharuSnare) tile);

            }
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        if (!world.blockExists(x, y, z)) {
            return null;
        }

        TileEntity tile = world.getTileEntity(x, y, z);

        switch (id) {
            case GuiIds.KHARU_SNARE: {
                if (!(tile instanceof TileKharuSnare)) {
                    return null;
                }
                return new GuiKharuSnare(player.inventory, (TileKharuSnare) tile);

            }
        }
        return null;
    }
}
