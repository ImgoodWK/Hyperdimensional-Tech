package com.imgood.hyperdimensionaltech.handler;

import com.imgood.hyperdimensionaltech.gui.HT_GuiHT_Sword;
import com.imgood.hyperdimensionaltech.item.HT_ItemSword;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

/**
 * @program: Hyperdimensional-Tech
 * @description: 屏幕渲染添加能量条
 * @author: Imgood
 * @create: 2024-08-12 10:26
 **/
public class onRenderGameOverlayEventHandler {
    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent.Post event) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer player = mc.thePlayer;
        if (player != null && player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof HT_ItemSword) {
            HT_ItemSword sword = (HT_ItemSword) player.getCurrentEquippedItem().getItem();
            HT_GuiHT_Sword gui = new HT_GuiHT_Sword(sword);
            gui.drawEnergyBar(mc, player.getCurrentEquippedItem());
        }
    }
}
