package com.imgood.hyperdimensionaltech.loader;

import com.imgood.hyperdimensionaltech.client.render.HT_ItemRenderer;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;

public class ItemRendererLoader {
    public static void loadItemRenderers() {
        Block holoController = GameRegistry.findBlock("gregtech", "gt.blockmachines");
        Block holoGraphicDisplay = GameRegistry.findBlock("hyperdimensionaltech", "tile.HolographicDisplay");
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(holoGraphicDisplay), new HT_ItemRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(holoController), new HT_ItemRenderer());
    }
}
