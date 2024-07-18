package com.imgood.hyperdimensionaltech.utils.recipes;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.ItemStack;

/**
 * @program: Hyperdimensional-Tech
 * @description: 几个写配方时候提供便利的方法
 * @author: Imgood
 * @create: 2024-07-18 14:55
 **/
public class RecipeHelper {
    public static ItemStack getItemStack(String mod,String id,int meta,int amount) {
        return new ItemStack(GameRegistry.findItem(mod,id),amount,meta);
    }
    public static ItemStack[] getItemStack(String mod,String id,int metaBegin,int metaEnd,int amount) {
        ItemStack[] itemStacks = new ItemStack[metaEnd-metaBegin];
        for(int i = 0;i < itemStacks.length;i++) {
            itemStacks[i] = getItemStack(mod,id,metaBegin+i,amount);
        }
        return itemStacks;
    }
}
