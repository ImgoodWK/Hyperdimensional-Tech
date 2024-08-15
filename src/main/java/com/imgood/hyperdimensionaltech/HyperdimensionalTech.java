package com.imgood.hyperdimensionaltech;


import com.imgood.hyperdimensionaltech.client.render.HT_TileEntityHolographicDisplay;
import com.imgood.hyperdimensionaltech.client.render.RenderEnergyBlade;
import com.imgood.hyperdimensionaltech.client.render.RenderEnergyWeapon;
import com.imgood.hyperdimensionaltech.entity.EntityEnergyBlade;
import com.imgood.hyperdimensionaltech.geckolib.ClientListener;
import com.imgood.hyperdimensionaltech.geckolib.CommonListener;
import com.imgood.hyperdimensionaltech.gui.EnergyWeaponGUI;
import com.imgood.hyperdimensionaltech.item.EnergyWeapon;
import com.imgood.hyperdimensionaltech.loader.BlocksLoader;
import com.imgood.hyperdimensionaltech.loader.GuiLoader;
import com.imgood.hyperdimensionaltech.loader.ItemLoader;
import com.imgood.hyperdimensionaltech.loader.MachineLoader;
import com.imgood.hyperdimensionaltech.loader.RecipeLoader;
import com.imgood.hyperdimensionaltech.loader.TileEntityLoader;
import com.imgood.hyperdimensionaltech.loader.EntityLoader;
import com.imgood.hyperdimensionaltech.nei.NEIHandler;
import com.imgood.hyperdimensionaltech.network.EnergyBladeHitPacket;
import com.imgood.hyperdimensionaltech.network.EnergyUpdatePacket;
import com.imgood.hyperdimensionaltech.network.PacketUpdateHandlerHolographicDisplay;
import com.imgood.hyperdimensionaltech.network.PacketUpdateHolographicDisplay;
import com.imgood.hyperdimensionaltech.utils.HTTextHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemLead;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.GeckoLib;
import software.bernie.geckolib3.handler.PlayerLoginHandler;
import software.bernie.geckolib3.particles.BedrockLibrary;
import software.bernie.geckolib3.resource.AnimationLibrary;
import software.bernie.geckolib3.resource.ModelLibrary;

import java.io.File;

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

    private static CreativeTabs geckolibItemGroup;
    public static BedrockLibrary particleLibraryInstance;
    public static ModelLibrary modelLibraryInstance;
    public static AnimationLibrary animationLibraryInstance;

    @Mod.EventHandler
    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {
        logger.info("Hyperdimensional Tech preinitialization");
        HyperdimensionalTechFeatures.preInit();
        HTTextHandler.initLangMap(isInDevMode);
        BlocksLoader.loadBlocks();
        TileEntityLoader.loadTileEntities();
        EntityLoader.loadEntities();
        ItemLoader.loadItems();

        //NetworkHandler.init();
            CommonListener.onRegisterBlocks();
            CommonListener.onRegisterItems();
            CommonListener.onRegisterEntities();
        particleLibraryInstance = new BedrockLibrary(new File("./particle"));
        particleLibraryInstance.reload();
        modelLibraryInstance = new ModelLibrary(new File("./models"));
        modelLibraryInstance.reload(false);
        animationLibraryInstance = new AnimationLibrary(new File("./animations"));
        animationLibraryInstance.reload(false);


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
        network.registerMessage(EnergyUpdatePacket.Handler.class, EnergyUpdatePacket.class, 2, Side.SERVER);
        network.registerMessage(EnergyBladeHitPacket.Handler.class, EnergyBladeHitPacket.class, 3, Side.SERVER);
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
            MinecraftForge.EVENT_BUS.register(new HT_TileEntityHolographicDisplay());
            //MinecraftForgeClient.registerItemRenderer(ItemLoader.energyWeapon, new RenderEnergyWeapon());
            MinecraftForge.EVENT_BUS.register(new EnergyWeaponGUIHandler());
            RenderingRegistry.registerEntityRenderingHandler(EntityEnergyBlade.class, new RenderEnergyBlade());
        }
        proxy.init(event);

    }

    @Mod.EventHandler
    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {

        MachineLoader.loadMachinePostInit();
        HTTextHandler.serializeLangMap(isInDevMode);
        HyperdimensionalTechFeatures.init();
        GuiLoader.loadGuis(this);
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

    public static class EnergyWeaponGUIHandler {
        private EnergyWeaponGUI gui = new EnergyWeaponGUI();

        @SubscribeEvent
        public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
            if (event.type == RenderGameOverlayEvent.ElementType.EXPERIENCE) {
                gui.drawGui(event.resolution.getScaledWidth(), event.resolution.getScaledHeight());
            }
        }
    }

    public static CreativeTabs getGeckolibItemGroup() {
            geckolibItemGroup = new CreativeTabs(CreativeTabs.getNextID(), "geckolib_examples") {
                @Override
                public Item getTabIconItem() {
                    return (ItemLoader.energyWeapon);
                }
            };

        return geckolibItemGroup;
    }

    public HyperdimensionalTech() {
        MinecraftForge.EVENT_BUS.register(new CommonListener());
        FMLCommonHandler.instance().bus().register(new PlayerLoginHandler());
        MinecraftForge.EVENT_BUS.register(new PlayerLoginHandler());
    }

    @SideOnly(Side.CLIENT)
    @Mod.EventHandler
    public void registerRenderers(FMLInitializationEvent event) {
            ClientListener.registerReplacedRenderers(event);
            ClientListener.registerRenderers(event);
        GeckoLib.initialize();
    }

}
