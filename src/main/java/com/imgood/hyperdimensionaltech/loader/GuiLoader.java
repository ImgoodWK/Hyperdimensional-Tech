package com.imgood.hyperdimensionaltech.loader;

import com.imgood.hyperdimensionaltech.HyperdimensionalTech;
import com.imgood.hyperdimensionaltech.gui.guihandler.GuiHandlerHolographicDisplay;
import cpw.mods.fml.common.network.NetworkRegistry;

/**
 * @program: Hyperdimensional-Tech
 * @description: 加载GUI，初始化调用
 * @author: Imgood
 * @create: 2024-07-30 16:14
 **/
public class GuiLoader {
    public static void loadGuis(HyperdimensionalTech hyperdimensionalTech)
    {
        NetworkRegistry.INSTANCE.registerGuiHandler(hyperdimensionalTech, new GuiHandlerHolographicDisplay());
    }
}
