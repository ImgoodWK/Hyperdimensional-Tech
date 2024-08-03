package com.imgood.hyperdimensionaltech.loader;

import com.imgood.hyperdimensionaltech.tiles.rendertiles.TileFeild;
import com.imgood.hyperdimensionaltech.tiles.rendertiles.TileHoloController;
import com.imgood.hyperdimensionaltech.tiles.rendertiles.TileHolographicDisplay;
import com.imgood.hyperdimensionaltech.tiles.rendertiles.TileParticleStream;
import cpw.mods.fml.common.registry.GameRegistry;

public class TileEntityLoader {
    public static void loadTileEntities()
    {
        GameRegistry.registerTileEntity(TileFeild.class, "FeildRender");
        GameRegistry.registerTileEntity(TileParticleStream.class, "ParticleStreamRender");
        GameRegistry.registerTileEntity(TileHoloController.class, "HoloControllerRender");
        GameRegistry.registerTileEntity(TileHolographicDisplay.class, "HolotypeDisplay");
    }
}
