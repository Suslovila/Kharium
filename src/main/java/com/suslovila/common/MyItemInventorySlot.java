package com.suslovila.common;

import com.suslovila.common.item.MyItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class MyItemInventorySlot extends Slot
{
    public MyItemInventorySlot(IInventory inv, int index, int xPos, int yPos)
    {
        super(inv, index, xPos, yPos);
    }

    // This is the only method we need to override so that
    // we can't place our inventory-storing Item within
    // its own inventory (thus making it permanently inaccessible)
    // as well as preventing abuse of storing backpacks within backpacks
    /**
     * Check if the stack is a valid item for this slot.
     */
    @Override
    public boolean isItemValid(ItemStack itemstack)
    {
        // Everything returns true except an instance of our Item
        return !(itemstack.getItem() instanceof MyItem);
    }
}