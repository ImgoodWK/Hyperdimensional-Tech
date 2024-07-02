package com.imgood.hyperdimensionaltech;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.imgood.hyperdimensionaltech.utils.Utils;

import gregtech.api.util.GT_Log;

public enum HT_ItemList {

    // Items 模组内的机器（物品对象）或物品在此定义
    HyperdimensionalResonanceEvolver,
    SingularityUnravelingDevice,
    TestItem0;
    // region Member Variables

    private boolean mHasNotBeenSet;
    private boolean mDeprecated;
    private boolean mWarned;

    private ItemStack mStack;

    // endregion

    /**
     * 构造方法，构造后默认为Not been set状态
     */
    HT_ItemList() {
        mHasNotBeenSet = true;
    }

    /**
     * 构造方法
     * @param aDeprecated true代表弃用，false代表启用
     */
    HT_ItemList(boolean aDeprecated) {
        if (aDeprecated) {
            mDeprecated = true;
            mHasNotBeenSet = true;
        }
    }

    /**
     * 获取Item对象
     * @return 对应的Item
     */
    public Item getItem() {
        sanityCheck();
        if (Utils.isStackInvalid(mStack)) {
            return null;// TODO replace a default issue item
        }
        return mStack.getItem();
    }

    /**
     * 获取Block对象
     * @return 对应的Block
     */
    public Block getBlock() {
        sanityCheck();
        return Block.getBlockFromItem(getItem());
    }

    /**
     * 返回物品堆ItemStack对象
     * @param aAmount 数量
     * @param aReplacements  这啥啊 我也不知道，以后补充
     * @return ItemStack
     */
    public ItemStack get(int aAmount, Object... aReplacements) {
        sanityCheck();
        // if invalid, return a replacements
        if (Utils.isStackInvalid(mStack)) {
            GT_Log.out.println("Object in the ItemList is null at:");
            new NullPointerException().printStackTrace(GT_Log.out);
            return Utils.copyAmount(aAmount, TestItem0.get(1));
        }
        return Utils.copyAmount(aAmount, mStack);
    }

    /**
     * 枚举对象的Item“set”
     * @param aItem 传入Item对象
     * @return
     */
    public HT_ItemList set(Item aItem) {
        mHasNotBeenSet = false;
        if (aItem == null) {
            return this;
        }
        ItemStack aStack = new ItemStack(aItem, 1, 0);
        mStack = Utils.copyAmount(1, aStack);
        return this;
    }

    /**
     * 枚举对象的ItemStack“set”
     * @param aStack ItemStack对象
     * @return
     */
    public HT_ItemList set(ItemStack aStack) {
        if (aStack != null) {
            mHasNotBeenSet = false;
            mStack = Utils.copyAmount(1, aStack);
        }
        return this;
    }

    /**
     * 判断枚举对象Set状态
     * @return true为has been set，false为not set
     */
    public boolean hasBeenSet() {
        return !mHasNotBeenSet;
    }

    /**
     * Returns the internal stack. This method is unsafe. It's here only for quick operations. DON'T CHANGE THE RETURNED
     * VALUE!
     */
    public ItemStack getInternalStack_unsafe() {
        return mStack;
    }

    private void sanityCheck() {
        if (mHasNotBeenSet) {
            throw new IllegalAccessError("The Enum '" + name() + "' has not been set to an Item at this time!");
        }
        if (mDeprecated && !mWarned) {
            new Exception(this + " is now deprecated").printStackTrace(GT_Log.err);
            // warn only once
            mWarned = true;
        }
    }
}
