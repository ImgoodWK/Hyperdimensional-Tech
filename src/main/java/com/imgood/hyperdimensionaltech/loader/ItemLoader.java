package com.imgood.hyperdimensionaltech.loader;

import com.imgood.hyperdimensionaltech.geckolib.geckoitem.HT_Sword;
import com.imgood.hyperdimensionaltech.item.EnergyWeapon;
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
    public static Item htSword;
    public static void loadItems()
    {
        energyWeapon = new EnergyWeapon();
        htSword = new HT_Sword();
        GameRegistry.registerItem(energyWeapon, "energyWeapon");
        GameRegistry.registerItem(htSword, "htSword");
    }
}
