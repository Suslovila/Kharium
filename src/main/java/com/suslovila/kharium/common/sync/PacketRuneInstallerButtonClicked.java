package com.suslovila.kharium.common.sync;

import com.suslovila.kharium.api.implants.RuneUsingItem;
import com.suslovila.kharium.api.rune.RuneType;
import com.suslovila.kharium.common.event.KharuTickHandler;
import com.suslovila.kharium.common.item.ItemRune;
import com.suslovila.kharium.common.multiStructure.runeInstaller.TileRuneInstaller;
import com.suslovila.kharium.common.worldSavedData.KharuHotbed;
import com.suslovila.kharium.utils.SusWorldHelper;
import com.suslovila.sus_multi_blocked.utils.Position;
import com.suslovila.sus_multi_blocked.utils.SusUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import thaumcraft.common.lib.utils.InventoryUtils;

import java.util.ArrayList;

public class PacketRuneInstallerButtonClicked implements IMessage {
    private Position installerPosition;
    private RuneType runeType;
    private boolean isAddRuneButton;

    public PacketRuneInstallerButtonClicked() {
    }

    public PacketRuneInstallerButtonClicked(Position position, RuneType runeType, boolean actionType) {
        this.installerPosition = position;
        this.runeType = runeType;
        this.isAddRuneButton = actionType;
    }

    public void toBytes(ByteBuf buffer) {
        installerPosition.writeTo(buffer);
        buffer.writeInt(runeType.ordinal());
        buffer.writeBoolean(isAddRuneButton);
    }

    public void fromBytes(ByteBuf buffer) {
        installerPosition = Position.Companion.readFrom(buffer);
        runeType = RuneType.values()[buffer.readInt()];
        isAddRuneButton = buffer.readBoolean();
    }

    public static class Handler implements IMessageHandler<PacketRuneInstallerButtonClicked, IMessage> {
        @Override
        public IMessage onMessage(PacketRuneInstallerButtonClicked packet, MessageContext ctx) {
            World world = ctx.getServerHandler().playerEntity.worldObj;
            //chunk protection
            int x = packet.installerPosition.getX();
            int y = packet.installerPosition.getY();
            int z = packet.installerPosition.getZ();
            if (world.blockExists(x, y, z)) {
                TileEntity tile = world.getTileEntity(x, y, z);
                if (tile instanceof TileRuneInstaller) {
                    TileRuneInstaller installer = (TileRuneInstaller) tile;

                    ItemStack stack = installer.getInventory().getStackInSlot(0);
                    if (stack != null) {
                        Item item = stack.getItem();
                        if (item instanceof RuneUsingItem) {
                            RuneUsingItem runeUsingItem = (RuneUsingItem) item;
                            int maxAmount = runeUsingItem.getMaxRuneAmount();
                            int runeTypeCurrentAmount = RuneUsingItem.Companion.getRuneAmountOfType(stack, packet.runeType);
                            int wholeRuneAmount = 0;
                            for(RuneType runeType : RuneType.values()) {
                                wholeRuneAmount += RuneUsingItem.Companion.getRuneAmountOfType(stack, runeType);
                            }

                            if (packet.isAddRuneButton) {
                                if (wholeRuneAmount < maxAmount) {
                                    ItemStack stackLeft =
                                            InventoryUtils.extractStack(
                                                    ctx.getServerHandler().playerEntity.inventory,
                                                    new ItemStack(ItemRune.INSTANCE, 1, packet.runeType.ordinal()),
                                                    0,
                                                    false,
                                                    false,
                                                    false,
                                                    true
                                            );
                                    if (stackLeft != null) {
                                        RuneUsingItem.Companion.setRuneAmountOfType(stack, packet.runeType, runeTypeCurrentAmount + 1);
                                    }
                                }
                            }
                            else {
                                if (runeTypeCurrentAmount > 0) {
                                    ItemStack stackLeft =
                                            InventoryUtils.placeItemStackIntoInventory(
                                                    new ItemStack(ItemRune.INSTANCE, 1, packet.runeType.ordinal()),
                                                    ctx.getServerHandler().playerEntity.inventory,
                                                    0,
                                                    true
                                            );
                                    if (stackLeft == null || stackLeft.stackSize == 0) {
                                        RuneUsingItem.Companion.setRuneAmountOfType(stack, packet.runeType, runeTypeCurrentAmount - 1);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return null;
        }
    }


}
