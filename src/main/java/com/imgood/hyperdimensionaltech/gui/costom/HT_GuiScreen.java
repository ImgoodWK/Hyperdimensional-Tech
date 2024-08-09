package com.imgood.hyperdimensionaltech.gui.costom;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class HT_GuiScreen extends GuiScreen {

    private ResourceLocation backgroundTexture;
    private int x = 0; // 默认X起点
    private int y = 0; // 默认Y起点
    public int bg_width = 0; // 默认宽度，0表示使用整个屏幕宽度
    public int bg_height = 0; // 默认高度，0表示使用整个屏幕高度
    private boolean stretch = true; // 默认拉伸背景

    public HT_GuiScreen() {
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    public void drawImage(ResourceLocation texture, int x, int y, int maxWidth, int maxHeight) {
        if (texture == null) return;

        // 获取纹理的尺寸
        int[] dimensions = getTextureDimensions(texture);
        int originalWidth = dimensions[0];
        int originalHeight = dimensions[1];

        // 如果无法获取尺寸，则使用最大尺寸
        if (originalWidth == 0 || originalHeight == 0) {
            originalWidth = maxWidth;
            originalHeight = maxHeight;
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(texture);

        // 计算缩放比例
        float scaleX = (float) maxWidth / originalWidth;
        float scaleY = (float) maxHeight / originalHeight;
        float scale = Math.min(scaleX, scaleY);

        // 计算实际绘制的宽度和高度
        int width = Math.round(originalWidth * scale);
        int height = Math.round(originalHeight * scale);

        // 计算居中位置
        int drawX = x + (maxWidth - width) / 2;
        int drawY = y + (maxHeight - height) / 2;

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(drawX, drawY + height, 0, 0, 1);
        tessellator.addVertexWithUV(drawX + width, drawY + height, 0, 1, 1);
        tessellator.addVertexWithUV(drawX + width, drawY, 0, 1, 0);
        tessellator.addVertexWithUV(drawX, drawY, 0, 0, 0);
        tessellator.draw();
    }

    // 获取纹理尺寸的辅助方法
    private int[] getTextureDimensions(ResourceLocation texture) {
        try {
            ITextureObject textureObject = mc.getTextureManager().getTexture(texture);
            if (textureObject instanceof AbstractTexture) {
                AbstractTexture abstractTexture = (AbstractTexture) textureObject;
                int glTextureId = abstractTexture.getGlTextureId();

                // 绑定纹理
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, glTextureId);

                // 获取纹理宽度和高度
                int width = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
                int height = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);

                return new int[]{width, height};
            }
        } catch (Exception e) {
            // 处理可能的异常
            e.printStackTrace();
        }
        // 如果无法获取尺寸，返回0
        return new int[]{0, 0};
    }

    private void drawBackground() {
        int screenWidth = this.bg_width;
        int screenHeight = this.bg_height;

        int drawWidth = this.bg_width == 0 ? screenWidth : this.bg_width;
        int drawHeight = this.bg_height == 0 ? screenHeight : this.bg_height;

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        if (this.backgroundTexture == null) {
            return;
        }
        this.mc.getTextureManager().bindTexture(this.backgroundTexture);

        if (this.stretch) {
            this.drawTexturedModalRect(this.x, this.y, 0, 0, drawWidth, drawHeight);
        } else {
            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV(this.x, this.y + drawHeight, 0, 0, 1);
            tessellator.addVertexWithUV(this.x + drawWidth, this.y + drawHeight, 0, 1, 1);
            tessellator.addVertexWithUV(this.x + drawWidth, this.y, 0, 1, 0);
            tessellator.addVertexWithUV(this.x, this.y, 0, 0, 0);
            tessellator.draw();
        }
    }

    // 链式调用方法
    public HT_GuiScreen setBackgroundTexture(ResourceLocation texture) {
        this.backgroundTexture = texture;
        return this;
    }

    public HT_GuiScreen setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public HT_GuiScreen setSize(int width, int height) {
        this.bg_width = width;
        this.bg_height = height;
        return this;
    }

    public HT_GuiScreen setStretch(boolean stretch) {
        this.stretch = stretch;
        return this;
    }
}
