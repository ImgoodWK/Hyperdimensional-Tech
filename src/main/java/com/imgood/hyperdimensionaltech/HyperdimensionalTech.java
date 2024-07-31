package com.imgood.hyperdimensionaltech;


import com.imgood.hyperdimensionaltech.loader.BlocksLoader;
import com.imgood.hyperdimensionaltech.loader.GuiLoader;
import com.imgood.hyperdimensionaltech.network.PacketUpdateHandlerHolographicDisplay;
import com.imgood.hyperdimensionaltech.network.PacketUpdateHolographicDisplay;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.imgood.hyperdimensionaltech.block.textures.TexturesGtBlock;
import com.imgood.hyperdimensionaltech.loader.MachineLoader;
import com.imgood.hyperdimensionaltech.loader.RecipeLoader;
import com.imgood.hyperdimensionaltech.nei.NEIHandler;
import com.imgood.hyperdimensionaltech.utils.HTTextHandler;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(
    modid = HyperdimensionalTech.MODID,
    version = Tags.VERSION,
    name = "HyperdimensionalTech",
    acceptedMinecraftVersions = "[1.7.10]",
    dependencies = "before:eternalsingularity; before:structurelib")

public class HyperdimensionalTech {

    public static final String MOD_NAME = "HyperdimensionalTech";
    public static final String MODID = "hyperdimensionaltech";
    public static final Logger LOG = LogManager.getLogger(MODID);
    public static final Logger logger = LogManager.getLogger(MOD_NAME);
    public static String DevResource = "";
    public static final boolean isInDevMode = false;
    @SidedProxy(clientSide = "com.imgood.hyperdimensionaltech.ClientProxy", serverSide = "com.imgood.hyperdimensionaltech.CommonProxy")
    public static CommonProxy proxy;
    @Mod.Instance(HyperdimensionalTech.MODID)
    public static HyperdimensionalTech instance;
    public static SimpleNetworkWrapper network;

    @Mod.EventHandler
    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {
        logger.info("Hyperdimensional Tech preinitialization");
        HyperdimensionalTechFeatures.preInit();
        HTTextHandler.initLangMap(isInDevMode);
        BlocksLoader.loadBlocks();
        proxy.preInit(event);
    }

    @Mod.EventHandler
    // load "Do your mod setup. Build whatever data structures you care about. Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {
        logger.info("Hyperdimensional Tech initialization");
        MachineLoader.loadMachines();
        // RecipeLoader.loadRecipes();
        NEIHandler.IMCSender();
        network = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);
        network.registerMessage(PacketUpdateHandlerHolographicDisplay.class, PacketUpdateHolographicDisplay.class, 0, Side.SERVER);
        proxy.init(event);
        logger.info(
            "Loading Textrues" + TexturesGtBlock.HyperDimensionalResonanceEvolverField.getTextureFile()
                .getResourcePath());

    }

    @Mod.EventHandler
    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {

        MachineLoader.loadMachinePostInit();
        HTTextHandler.serializeLangMap(isInDevMode);
        HyperdimensionalTechFeatures.init();
        GuiLoader.loadGui(this);
        proxy.postInit(event);
    }

    // register server commands in this event handler (Remove if not needed)
    public void serverStarting(FMLServerStartingEvent event) {
        proxy.serverStarting(event);
    }

    @Mod.EventHandler
    public void onLoadCompleteEvent(FMLLoadCompleteEvent event) {
        // Your post-load initialization code here
        RecipeLoader.loadRecipes();

    }


}
