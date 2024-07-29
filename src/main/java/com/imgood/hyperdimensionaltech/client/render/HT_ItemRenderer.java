package com.imgood.hyperdimensionaltech.client.render;

import com.imgood.hyperdimensionaltech.HyperdimensionalTech;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import java.lang.reflect.Field;

public class HT_ItemRenderer implements IItemRenderer {

    private Field mMetaTileEntityField;
    public HT_ItemRenderer() {
        try {
            mMetaTileEntityField = BaseMetaTileEntity.class.getDeclaredField("mMetaTileEntity");
            mMetaTileEntityField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }
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
        if (type == ItemRenderType.INVENTORY) {
            if ("gt.blockmachines".equals(itemName) && meta == 10002) {
                new HT_TileEntityRenderer_HoloController().renderInventory();
            }
        } else {
            if ("gt.blockmachines".equals(itemName) && meta == 10002) {
                new HT_TileEntityRenderer_HoloController().renderInventory(1,1,1,2,0.5,0,90);
            }
        }


    }



}
