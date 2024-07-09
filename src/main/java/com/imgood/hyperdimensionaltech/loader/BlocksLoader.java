package com.imgood.hyperdimensionaltech.loader;

import com.imgood.hyperdimensionaltech.HyperdimensionalTech;
import com.imgood.hyperdimensionaltech.block.BasicBlocks;
import com.imgood.hyperdimensionaltech.block.BlockHRERender;
import com.imgood.hyperdimensionaltech.tiles.rendertiles.TileHREFeild;
import cpw.mods.fml.common.registry.GameRegistry;

public class BlocksLoader {
    public static void loadBlocks() {
        GameRegistry.registerTileEntity(TileHREFeild.class, "HREFeildRender");
        BasicBlocks.Block_HRERender = new BlockHRERender();
    }
}
