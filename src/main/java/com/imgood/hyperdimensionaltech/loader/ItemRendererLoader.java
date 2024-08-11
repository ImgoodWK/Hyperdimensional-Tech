package com.imgood.hyperdimensionaltech.loader;

import com.imgood.hyperdimensionaltech.HyperdimensionalTech;
import com.imgood.hyperdimensionaltech.client.render.HT_ItemRenderer;
import com.imgood.hyperdimensionaltech.client.render.HT_Sword;
import com.imgood.hyperdimensionaltech.client.render.RenderEnergyWeapon;
import com.imgood.hyperdimensionaltech.handler.onRenderGameOverlayEventHandler;
import com.imgood.hyperdimensionaltech.item.EnergyWeapon;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

import static com.imgood.hyperdimensionaltech.utils.Enums.Names.MOD_ID;

public class ItemRendererLoader {
    public static void loadItemRenderers() {
        Block holoController = GameRegistry.findBlock("gregtech", "gt.blockmachines");
        Block holoGraphicDisplay = GameRegistry.findBlock("hyperdimensionaltech", "tile.HolographicDisplay");
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(holoGraphicDisplay), new HT_ItemRenderer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(holoController), new HT_ItemRenderer());
        MinecraftForgeClient.registerItemRenderer(GameRegistry.findItem(MOD_ID, "item.HT_Sword"), new HT_Sword());
        MinecraftForge.EVENT_BUS.register(new onRenderGameOverlayEventHandler());
        MinecraftForgeClient.registerItemRenderer(new EnergyWeapon(), new RenderEnergyWeapon());
    }
}
