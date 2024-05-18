package com.suslovila.kharium.utils;

import net.minecraft.item.ItemStack;

public final class StackHelper {
    public static boolean areItemStacksEqual(ItemStack first, ItemStack second) {
        return ((first == null && second == null) || (first != null && second != null && first.getItem() == second.getItem() && (!first.getHasSubtypes() || first.getMetadata() == second.getMetadata())));
    }

    public static boolean canStacksMerge(ItemStack stack1, ItemStack stack2) {
        if (stack1 == null || stack2 == null) {
            return false;
        }
        if (!stack1.isItemEqual(stack2)) {
            return false;
        }
        if (!ItemStack.areItemStackTagsEqual(stack1, stack2)) {
            return false;
        }
        return true;
    }

    public static int mergeStacks(ItemStack mergeSource, ItemStack mergeTarget, boolean doMerge) {
        if (!canStacksMerge(mergeSource, mergeTarget)) {
            return 0;
        }
        int mergeCount = Math.min(mergeTarget.getMaxStackSize() - mergeTarget.stackSize, mergeSource.stackSize);
        if (mergeCount < 1) {
            return 0;
        }
        if (doMerge) {
            mergeTarget.stackSize += mergeCount;
        }
        return mergeCount;
    }
}

