package com.imgood.hyperdimensionaltech.loader;

import com.imgood.hyperdimensionaltech.item.EnergyWeapon;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

/**
 * @program: Hyperdimensional-Tech
 * @description:
 * @author: Imgood
 * @create: 2024-08-12 15:40
 **/
public class ItemLoader {
    public static Item energyWeapon;
    public static void loadItems()
    {
        energyWeapon = new EnergyWeapon();
        GameRegistry.registerItem(energyWeapon, "energyWeapon");
    }
}
