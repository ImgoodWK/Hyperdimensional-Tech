package com.imgood.hyperdimensionaltech.loader;

import com.imgood.hyperdimensionaltech.block.BasicBlocks;
import com.imgood.hyperdimensionaltech.block.BlockRender;
import com.imgood.hyperdimensionaltech.tiles.rendertiles.TileFeild;
import cpw.mods.fml.common.registry.GameRegistry;

public class BlocksLoader {
    public static void loadBlocks() {
        GameRegistry.registerTileEntity(TileFeild.class, "HREFeildRender");
        BasicBlocks.Block_HRERender = new BlockRender();
    }
}
