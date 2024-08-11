package com.imgood.hyperdimensionaltech.loader;

import com.imgood.hyperdimensionaltech.network.EnergyUpdatePacket;
import com.imgood.hyperdimensionaltech.network.NetworkHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

/**
 * @program: Hyperdimensional-Tech
 * @description:
 * @author: Imgood
 * @create: 2024-08-12 14:51
 **/
public class NetworkLoader {
    public static SimpleNetworkWrapper network;
    public static void loadNetwork()
    {
        NetworkHandler.init();
    }
}
