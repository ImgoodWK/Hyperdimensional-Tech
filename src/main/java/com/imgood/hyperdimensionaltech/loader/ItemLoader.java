package com.imgood.hyperdimensionaltech.loader;

import com.imgood.hyperdimensionaltech.item.DimensionalRifter;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

/**
 * @program: Hyperdimensional-Tech
 * @description:
 * @author: Imgood
 * @create: 2024-08-12 15:40
 **/
public class ItemLoader {
    public static Item dimensionalRifter;
    public static void loadItems()
    {
        dimensionalRifter = new DimensionalRifter();
        GameRegistry.registerItem(dimensionalRifter, "energyWeapon");
    }
}
