package com.imgood.hyperdimensionaltech.gui;

import com.imgood.hyperdimensionaltech.item.HT_ItemSword;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import static com.imgood.hyperdimensionaltech.utils.Enums.Names.MOD_ID;

/**
 * @program: Hyperdimensional-Tech
 * @description: HT剑屏幕上的能量条
 * @author: Imgood
 * @create: 2024-08-12 10:09
 **/
public class HT_GuiHT_Sword extends Gui {
    private static final ResourceLocation texture = new ResourceLocation(MOD_ID, "textures/gui/ht_Sword_EnergyBar.png");
    private HT_ItemSword sword;

    public HT_GuiHT_Sword(HT_ItemSword sword) {
        this.sword = sword;
    }

    public void drawEnergyBar(Minecraft mc, ItemStack stack) {
        mc.getTextureManager().bindTexture(texture);
        int energy = sword.getEnergy(stack);
        int width = (int) (128 * (energy / 100f));
        drawTexturedModalRect(mc.displayWidth - 138, mc.displayHeight - 20, 0, 0, 128, 10); // 背景
        drawTexturedModalRect(mc.displayWidth - 138, mc.displayHeight - 20, 0, 10, width, 10); // 能量条
    }
}
