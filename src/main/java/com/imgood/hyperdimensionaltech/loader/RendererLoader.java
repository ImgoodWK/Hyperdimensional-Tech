package com.imgood.hyperdimensionaltech.loader;

import com.imgood.hyperdimensionaltech.client.render.HT_TileEntityHolographicDisplay;
import com.imgood.hyperdimensionaltech.client.render.HT_TileEntityRenderer_Feild;
import com.imgood.hyperdimensionaltech.client.render.HT_TileEntityRenderer_HoloController;
import com.imgood.hyperdimensionaltech.client.render.HT_TileEntityRenderer_ParticleStream;

public class RendererLoader {
    public static void loadRenderers() {
        new HT_TileEntityRenderer_Feild();
        new HT_TileEntityRenderer_ParticleStream();
        new HT_TileEntityRenderer_HoloController();
        new HT_TileEntityHolographicDisplay();
    }
}
