package com.imgood.hyperdimensionaltech.client.render;

import com.imgood.hyperdimensionaltech.HyperdimensionalTech;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

public class HT_ItemRenderer implements IItemRenderer {

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        String itemName = item.getItem().getUnlocalizedName();
        int meta = item.getItemDamage();
        return "gt.blockmachines".equals(itemName) && meta == 10002;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        String itemName = item.getItem().getUnlocalizedName();
        int meta = item.getItemDamage();
        return "gt.blockmachines".equals(itemName) && meta == 10002;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        String itemName = item.getItem().getUnlocalizedName();
        int meta = item.getItemDamage();
        if ("gt.blockmachines".equals(itemName) && meta == 10002) {
            new HT_TileEntityRenderer_HoloController().renderInventory();
        }
    }



}
