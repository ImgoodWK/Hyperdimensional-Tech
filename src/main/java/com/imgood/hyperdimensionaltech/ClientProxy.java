package com.imgood.hyperdimensionaltech;

import com.imgood.hyperdimensionaltech.client.render.HT_TileEntityHolographicDisplay;
import com.imgood.hyperdimensionaltech.client.render.HT_ItemRenderer;
import com.imgood.hyperdimensionaltech.client.render.HT_TileEntityRenderer_HoloController;
import com.imgood.hyperdimensionaltech.client.render.HT_TileEntityRenderer_ParticleStream;
import com.imgood.hyperdimensionaltech.client.render.HT_TileEntityRenderer_Feild;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;

import static com.imgood.hyperdimensionaltech.block.BasicBlocks.Block_RenderHolographicDisplay;

public class ClientProxy extends CommonProxy {

    // Override CommonProxy methods here, if you want a different behaviour on the client (e.g. registering renders).
    // Don't forget to call the super methods as well.
    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        new HT_TileEntityRenderer_Feild();
        new HT_TileEntityRenderer_ParticleStream();
        new HT_TileEntityRenderer_HoloController();
        new HT_TileEntityHolographicDisplay();
        Block holoController = GameRegistry.findBlock("gregtech", "gt.blockmachines");
        Block holoGraphicDisplay = GameRegistry.findBlock("hyperdimensionaltech", "tile.HolographicDisplay");
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(holoGraphicDisplay), new HT_ItemRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(holoController), new HT_ItemRenderer());
    }


    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }
}
