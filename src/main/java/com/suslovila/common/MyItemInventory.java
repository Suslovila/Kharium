package com.suslovila.common;

import com.suslovila.common.item.MyItem;
import com.suslovila.examplemod.ExampleMod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

public class MyItemInventory implements IInventory
{
    private String name = "Inventory Item";

    /** Provides NBT Tag Compound to reference */
    private final ItemStack myItemInstance;

    /** Defining your inventory size this way is handy */
    public static final int INV_SIZE = 8;

    /** Inventory's size must be same as number of slots you add to the Container class */
    private ItemStack[] inventory = new ItemStack[INV_SIZE];

    /**
     * @param stack - the ItemStack to which this inventory belongs
     */
    public MyItemInventory(ItemStack stack)
    {
        myItemInstance = stack;

        // Create a new NBT Tag Compound if one doesn't already exist, or you will crash
//        if (!stack.hasTagCompound()) {
//            stack.setTagCompound(new NBTTagCompound());
//        }
//        // note that it's okay to use stack instead of invItem right there
//        // both reference the same memory location, so whatever you change using
//        // either reference will change in the other

        // Read the inventory contents from NBT
        readFromNBT(ExampleMod.getOrCreateTag(stack));
    }

    @Override
    public int getSizeInventory()
    {
        return inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int slot)
    {
        return inventory[slot];
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount)
    {
        ItemStack stack = getStackInSlot(slot);
        if(stack != null)
        {
            if(stack.stackSize > amount)
            {
                stack = stack.splitStack(amount);
                // Don't forget this line or your inventory will not be saved!
                markDirty();
            }
            else
            {
                // this method also calls onInventoryChanged, so we don't need to call it again
                setInventorySlotContents(slot, null);
            }
        }
        return stack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slot)
    {
      ItemStack stack = getStackInSlot(slot);
//        setInventorySlotContents(slot, null);
        //return stack;
       return null;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack)
    {
        inventory[slot] = stack;

        if (stack != null && stack.stackSize > getInventoryStackLimit())
        {
            stack.stackSize = getInventoryStackLimit();
        }

        // Don't forget this line or your inventory will not be saved!
        markDirty();
    }

    // 1.7.2+ renamed to getInventoryName
    @Override
    public String getInventoryName()
    {
        return name;
    }

    // 1.7.2+ renamed to hasCustomInventoryName
    @Override
    public boolean hasCustomInventoryName()
    {
        return name.length() > 0;
    }

    @Override
    public int getInventoryStackLimit()
    {
        return 64;
    }

    /**
     * This is the method that will handle saving the inventory contents, as it is called (or should be called!)
     * anytime the inventory changes. Perfect. Much better than using onUpdate in an Item, as this will also
     * let you change things in your inventory without ever opening a Gui, if you want.
     */
    // 1.7.2+ renamed to markDirty
    @Override
    public void markDirty()
    {
        writeToNBT(myItemInstance.getTagCompound());
    }


    @Override
    public boolean isUseableByPlayer(EntityPlayer entityplayer)
    {
        return true;
    }

    // 1.7.2+ renamed to openInventory(EntityPlayer player)
    @Override
    public void openInventory() {}

    // 1.7.2+ renamed to closeInventory(EntityPlayer player)
    @Override
    public void closeInventory() {}

    /**
     * This method doesn't seem to do what it claims to do, as
     * items can still be left-clicked and placed in the inventory
     * even when this returns false
     */
    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemstack)
    {
        // Don't want to be able to store the inventory item within itself
        // Bad things will happen, like losing your inventory
        // Actually, this needs a custom Slot to work
        return !(itemstack.getItem() instanceof MyItem);
    }

    /**
     * A custom method to read our inventory from an ItemStack's NBT compound
     */
    public void readFromNBT(NBTTagCompound compound)
    {
        // Gets the custom taglist we wrote to this compound, if any
        // 1.7.2+ change to compound.getTagList("ItemInventory", Constants.NBT.TAG_COMPOUND);
        NBTTagList items = compound.getTagList("ItemInventory", Constants.NBT.TAG_COMPOUND);

        for (int i = 0; i < items.tagCount(); ++i)
        {
            // 1.7.2+ change to items.getCompoundTagAt(i)
            NBTTagCompound item = (NBTTagCompound) items.getCompoundTagAt(i);
            int slot = item.getInteger("Slot");

            // Just double-checking that the saved slot index is within our inventory array bounds
            if (slot >= 0 && slot < getSizeInventory()) {
                inventory[slot] = ItemStack.loadItemStackFromNBT(item);
            }
        }
    }

    /**
     * A custom method to write our inventory to an ItemStack's NBT compound
     */
    public void writeToNBT(NBTTagCompound tagcompound)
    {
        // Create a new NBT Tag List to store itemstacks as NBT Tags
        NBTTagList items = new NBTTagList();

        for (int i = 0; i < getSizeInventory(); ++i)
        {
            // Only write stacks that contain items
            if (getStackInSlot(i) != null)
            {
                // Make a new NBT Tag Compound to write the itemstack and slot index to
                NBTTagCompound item = new NBTTagCompound();
                item.setInteger("Slot", i);
                // Writes the itemstack in slot(i) to the Tag Compound we just made
                getStackInSlot(i).writeToNBT(item);

                // add the tag compound to our tag list
                items.appendTag(item);
            }
        }
        // Add the TagList to the ItemStack's Tag Compound with the name "ItemInventory"
        tagcompound.setTag("ItemInventory", items);
    }
}
//    /*
//    If you want to be able to place your inventory-holding Item within another
//    instance of itself, you'll need to have a way to distinguish between each
//    instance of the item so you can check to make sure you're not placing the item
//    within itself. What you'll need to do is assign a UUID to a String variable
//    within your Inventory class for every ItemStack that holds an instance of your
//    Item, like so:
//     */
//// declaration of variable:
//    protected String uniqueID;
//
///** initialize variable within the constructor: */
//uniqueID = "";
//
//        if (!itemstack.hasTagCompound())
//        {
//        itemstack.setTagCompound(new NBTTagCompound());
//        // no tag compound means the itemstack does not yet have a UUID, so assign one:
//        uniqueID = UUID.randomUUID().toString();
//        }
//
///** When reading from NBT: */
//        if ("".equals(uniqueID))
//        {
//        // try to read unique ID from NBT
//        uniqueID = tagcompound.getString("uniqueID");
//        // if it's still "", assign a new one:
//        if ("".equals(uniqueID))
//        {
//        uniqueID = UUID.randomUUID().toString();
//        }
//        }
//
///** Writing to NBT: */
//// just add this line:
//        tagcompound.setString("uniqueID", this.uniqueID);
///*
//Finally, in your Container class, you will need to check if the currently opened
//inventory's uniqueID is equal to the itemstack's uniqueID in the method
//'transferStackInSlot' as well as check if the itemstack is the currently equipped
//item in the method 'slotClick'. In both cases, you'll need to prevent the itemstack
//from being moved or it will cause bad things to happen.
//*/
//
///**
// * Step 3: Create a custom Container for your Inventory
// */
///*
//There's a LOT of code in this one, but read through all of the comments carefully
//and it should become clear what everything does.
//As a bonus, one of my previous tutorials is included within!
//"How to Properly Override Shift-Clicking" is here and better than ever!
//At least in my opinion.
//If you're like me, and you find no end of frustration trying to figure out which
//f-ing index you should use for which slots in your container when overriding
//transferStackInSlot, or if your following the original tutorial, then read on.
// */