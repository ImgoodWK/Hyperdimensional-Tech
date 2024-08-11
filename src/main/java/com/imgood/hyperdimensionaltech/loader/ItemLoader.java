package com.imgood.hyperdimensionaltech.loader;

import com.imgood.hyperdimensionaltech.item.EnergyWeapon;
import com.imgood.hyperdimensionaltech.item.HT_ItemSword;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

import static net.minecraft.item.Item.ToolMaterial.EMERALD;

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
        GameRegistry.registerItem(new HT_ItemSword(EMERALD), "ht.sword");
        energyWeapon = new EnergyWeapon();
        GameRegistry.registerItem(energyWeapon, "energyWeapon");
    }
}
