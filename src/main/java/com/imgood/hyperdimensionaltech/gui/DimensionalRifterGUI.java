package com.imgood.hyperdimensionaltech.gui;

import com.imgood.hyperdimensionaltech.item.DimensionalRifter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.Tessellator;
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
public class DimensionalRifterGUI extends Gui {
    private static final ResourceLocation texture = new ResourceLocation(MOD_ID, "textures/gui/ht_Sword_EnergyBar.png");
    private static final ResourceLocation texture2 = new ResourceLocation(MOD_ID, "textures/gui/ht_Sword_EnergyBar_Curent.png");
    private static final int BAR_WIDTH = 60;
    private static final int BAR_HEIGHT = 10;
    private static final int TEXTURE_WIDTH = 100; // 假设材质宽度为100
    private static final int TEXTURE_HEIGHT = 20; // 假设材质高度为20
    private final Minecraft mc = Minecraft.getMinecraft();

    public void drawGui(int screenWidth, int screenHeight) {
        ItemStack heldItem = mc.thePlayer.getHeldItem();
        if (heldItem == null || heldItem.stackSize <= 0) return;
        if (!(heldItem.getItem() instanceof DimensionalRifter)) return;

        DimensionalRifter weapon = (DimensionalRifter) heldItem.getItem();
        int energy = weapon.getEnergy();
        float energyPercentage = (float)energy / DimensionalRifter.MAX_ENERGY;

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        mc.getTextureManager().bindTexture(texture);

        // 计算绘制位置（例如，在屏幕右下角）
        int x = screenWidth - BAR_WIDTH - 10;
        int y = screenHeight - BAR_HEIGHT - 10;

        // 绘制背景（完整的能量条）
        drawImage(texture, x, y, BAR_WIDTH, BAR_HEIGHT, 0, 0, TEXTURE_WIDTH, TEXTURE_HEIGHT);

        // 绘制能量条前景（根据当前能量值动态缩放）
        int energyWidth = (int)(BAR_WIDTH * energyPercentage);
        drawImage(texture2, x, y+1, energyWidth, BAR_HEIGHT-2, 0, TEXTURE_HEIGHT / 2, TEXTURE_WIDTH, TEXTURE_HEIGHT / 2);

        // 绘制文本
        FontRenderer fontRenderer = mc.fontRenderer;
        String energyText = energy + "/" + DimensionalRifter.MAX_ENERGY;
        int textWidth = fontRenderer.getStringWidth(energyText);
        int textX = x + (BAR_WIDTH - textWidth) / 2;
        int textY = y - 10; // 将文本放在能量条上方
        // 绘制文本阴影以提高可读性
        fontRenderer.drawStringWithShadow(energyText, textX, textY, 0x00FFFF);

    }

    // 绘制缩放的纹理，加入起始纹理坐标和尺寸参数
    public void drawImage(ResourceLocation texture, int x, int y, int width, int height, int textureX, int textureY, int textureWidth, int textureHeight) {
        if (texture == null) return;
        GL11.glPushMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(texture);

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x, y + height, 0, (float) textureX / TEXTURE_WIDTH, (float)(textureY + textureHeight) / TEXTURE_HEIGHT);
        tessellator.addVertexWithUV(x + width, y + height, 0, (float)(textureX + textureWidth * (float) width / BAR_WIDTH) / TEXTURE_WIDTH, (float)(textureY + textureHeight) / TEXTURE_HEIGHT);
        tessellator.addVertexWithUV(x + width, y, 0, (float)(textureX + textureWidth * (float) width / BAR_WIDTH) / TEXTURE_WIDTH, (float)textureY / TEXTURE_HEIGHT);
        tessellator.addVertexWithUV(x, y, 0, (float)textureX / TEXTURE_WIDTH, (float)textureY / TEXTURE_HEIGHT);
        tessellator.draw();
        GL11.glPopMatrix();
    }
}



