package com.imgood.hyperdimensionaltech.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import com.imgood.hyperdimensionaltech.HyperdimensionalTech;
import com.imgood.hyperdimensionaltech.Tags;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class NEI_Config implements IConfigureNEI {

    public static boolean isAdded = true;

    public static void hide(Block aBlock) {
        API.hideItem(new ItemStack(aBlock, 1));
    }

    public static void hide(Item aItem) {
        API.hideItem(new ItemStack(aItem, 1));
    }

    @Override
    public void loadConfig() {
        isAdded = true;
    }

    @Override
    public String getName() {
        return HyperdimensionalTech.MOD_NAME + " NEI Plugin";
    }

    @Override
    public String getVersion() {
        return Tags.VERSION;
    }
}
