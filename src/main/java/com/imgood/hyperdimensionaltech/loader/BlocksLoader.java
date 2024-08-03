package com.imgood.hyperdimensionaltech.loader;

import com.imgood.hyperdimensionaltech.block.BasicBlocks;
import com.imgood.hyperdimensionaltech.block.BlockHolographicDisplay;
import com.imgood.hyperdimensionaltech.block.BlockRenderFeild;
import com.imgood.hyperdimensionaltech.block.BlockRenderHoloController;
import com.imgood.hyperdimensionaltech.block.BlockRenderParticleStream;
import com.imgood.hyperdimensionaltech.tiles.rendertiles.TileFeild;
import com.imgood.hyperdimensionaltech.tiles.rendertiles.TileHoloController;
import com.imgood.hyperdimensionaltech.tiles.rendertiles.TileHolographicDisplay;
import com.imgood.hyperdimensionaltech.tiles.rendertiles.TileParticleStream;
import cpw.mods.fml.common.registry.GameRegistry;

public class BlocksLoader {
    public static void loadBlocks() {

        BasicBlocks.Block_RenderParticleStream = new BlockRenderParticleStream("ht.sud_render");
        BasicBlocks.Block_RenderField = new BlockRenderFeild();
        BasicBlocks.Block_RenderHoloController = new BlockRenderHoloController("ht.holocontroller_render");
        BasicBlocks.Block_RenderHolographicDisplay = new BlockHolographicDisplay("ht.holographicdisplay");
    }
}
