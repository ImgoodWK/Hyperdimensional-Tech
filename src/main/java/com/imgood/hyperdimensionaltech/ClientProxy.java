package com.imgood.hyperdimensionaltech;

import com.imgood.hyperdimensionaltech.client.render.HT_ItemRenderer;
import com.imgood.hyperdimensionaltech.client.render.HT_TileEntityRenderer_HoloController;
import com.imgood.hyperdimensionaltech.client.render.HT_TileEntityRenderer_ParticleStream;
import com.imgood.hyperdimensionaltech.client.render.HT_TileEntityRenderer_Feild;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;

public class ClientProxy extends CommonProxy {

    // Override CommonProxy methods here, if you want a different behaviour on the client (e.g. registering renders).
    // Don't forget to call the super methods as well.
    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        new HT_TileEntityRenderer_Feild();
        new HT_TileEntityRenderer_ParticleStream();
        new HT_TileEntityRenderer_HoloController();
        Block block = GameRegistry.findBlock("gregtech", "gt.blockmachines");
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(block), new HT_ItemRenderer());
    }


    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }
}
