package com.imgood.hyperdimensionaltech.gui;

import com.imgood.hyperdimensionaltech.item.EnergyWeapon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import static com.imgood.hyperdimensionaltech.utils.Enums.Names.MOD_ID;

/**
 * @program: Hyperdimensional-Tech
 * @description:
 * @author: Imgood
 * @create: 2024-08-12 16:27
 **/
public class EnergyWeaponGUI extends Gui {
    private static final ResourceLocation texture = new ResourceLocation(MOD_ID, "textures/gui/energy_bar.png");
    private static final int BAR_WIDTH = 82;
    private static final int BAR_HEIGHT = 8;

    public void drawEnergyBar(int screenWidth, int screenHeight) {
        Minecraft mc = Minecraft.getMinecraft();
        ItemStack heldItem = mc.thePlayer.getHeldItem();

        if (heldItem != null && heldItem.getItem() instanceof EnergyWeapon) {
            EnergyWeapon weapon = (EnergyWeapon) heldItem.getItem();
            int energy = weapon.getEnergy();

            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            mc.getTextureManager().bindTexture(texture);

            // Draw background
            this.drawTexturedModalRect(screenWidth - 90, screenHeight - 20, 0, 0, BAR_WIDTH, BAR_HEIGHT);

            // Draw energy bar
            int energyWidth = (int)((float)energy / EnergyWeapon.MAX_ENERGY * BAR_WIDTH);
            this.drawTexturedModalRect(screenWidth - 90, screenHeight - 20, 0, BAR_HEIGHT, energyWidth, BAR_HEIGHT);
        }
    }
}
