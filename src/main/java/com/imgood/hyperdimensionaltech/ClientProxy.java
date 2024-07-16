package com.imgood.hyperdimensionaltech;

import com.imgood.hyperdimensionaltech.client.render.HT_TileEntityRenderer_ParticleStream;
import com.imgood.hyperdimensionaltech.client.render.HT_TileEntityRenderer_Feild;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;

public class ClientProxy extends CommonProxy {

    // Override CommonProxy methods here, if you want a different behaviour on the client (e.g. registering renders).
    // Don't forget to call the super methods as well.
    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        new HT_TileEntityRenderer_Feild();
        new HT_TileEntityRenderer_ParticleStream();
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }
}
