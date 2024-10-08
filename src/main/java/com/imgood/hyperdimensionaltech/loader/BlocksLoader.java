package com.imgood.hyperdimensionaltech.loader;

import com.imgood.hyperdimensionaltech.block.BasicBlocks;
import com.imgood.hyperdimensionaltech.block.BlockHolographicDisplay;
import com.imgood.hyperdimensionaltech.block.BlockRenderFeild;
import com.imgood.hyperdimensionaltech.block.BlockRenderHoloController;
import com.imgood.hyperdimensionaltech.block.BlockRenderParticleStream;

public class BlocksLoader {
    public static void loadBlocks() {

        BasicBlocks.Block_RenderParticleStream = new BlockRenderParticleStream("ht.sud_render");
        BasicBlocks.Block_RenderField = new BlockRenderFeild();
        BasicBlocks.Block_RenderHoloController = new BlockRenderHoloController("ht.holocontroller_render");
        BasicBlocks.Block_RenderHolographicDisplay = new BlockHolographicDisplay("ht.holographicdisplay");
    }
}
