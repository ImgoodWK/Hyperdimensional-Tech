package com.imgood.hyperdimensionaltech;

import com.imgood.hyperdimensionaltech.loader.ItemRendererLoader;
import com.imgood.hyperdimensionaltech.loader.RendererLoader;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;

public class ClientProxy extends CommonProxy {

    // Override CommonProxy methods here, if you want a different behaviour on the client (e.g. registering renders).
    // Don't forget to call the super methods as well.
    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        RendererLoader.loadRenderers();
        ItemRendererLoader.loadItemRenderers();
    }


    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }
}
