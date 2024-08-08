package com.imgood.hyperdimensionaltech.gui.costom;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class HT_Button extends GuiButton {
    private static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation("textures/gui/widgets.png");

    private ResourceLocation texture;
    private ResourceLocation hoverTexture;
    private ResourceLocation leftDecoration;
    private ResourceLocation rightDecoration;

    private int textColor;
    private int textColorHover;
    private int disabledTextColor;
    private int decorationWidth = 16; //

    private boolean useHoverEffect;
    private boolean useRGBEffect;

    private long startTime;

    public HT_Button(int id, int x, int y, int width, int height, String text) {
        super(id, x, y, width, height, text);
        this.textColor = 0xFFFFFF; // 默认白色
        this.textColorHover = 0xFFFFFF;
        disabledTextColor = 0xA0A0A0; // 不可用状态
        this.useHoverEffect = false;
        this.useRGBEffect = false;
        this.startTime = System.currentTimeMillis();
        this.texture = DEFAULT_TEXTURE; // 设置默认纹理
    }

    public HT_Button setTextHoverColor(int textColor) {
        this.textColorHover = textColor;
        return this;
    }

    public HT_Button setLeftDecoration(ResourceLocation leftDecoration) {
        this.leftDecoration = leftDecoration;
        return this;
    }

    public HT_Button setRightDecoration(ResourceLocation rightDecoration) {
        this.rightDecoration = rightDecoration;
        return this;
    }

    public HT_Button setDecorationWidth(int width) {
        this.decorationWidth = width;
        return this;
    }

    public HT_Button setTexture(ResourceLocation texture) {
        this.texture = texture != null ? texture : DEFAULT_TEXTURE;
        return this;
    }

    public HT_Button setHoverTexture(ResourceLocation hoverTexture) {
        this.hoverTexture = hoverTexture;
        this.useHoverEffect = (hoverTexture != null);
        return this;
    }

    public HT_Button setTextColor(int color) {
        this.textColor = color;
        return this;
    }

    public HT_Button setUseHoverEffect(boolean useHoverEffect) {
        this.useHoverEffect = useHoverEffect;
        return this;
    }

    public HT_Button setUseRGBEffect(boolean useRGBEffect) {
        this.useRGBEffect = useRGBEffect;
        return this;
    }

    public HT_Button setDisabledTextColor(int disabledTextColor) {
        this.disabledTextColor = disabledTextColor;
        return this;
    }

    public boolean getUseRGBEffect() {
        return this.useRGBEffect;
    }
    private int getRGBColor() {
        long elapsed = System.currentTimeMillis() - startTime;
        float hue = (elapsed % 3000) / 3000f;
        return java.awt.Color.HSBtoRGB(hue, 1.0f, 1.0f) & 0xFFFFFF;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        if (this.visible) {
            // 判断鼠标是否在按钮上
            boolean isHovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;

            // 绑定当前适用的纹理
            ResourceLocation currentTexture = isHovered && this.useHoverEffect && this.hoverTexture != null ? this.hoverTexture : this.texture;
            mc.getTextureManager().bindTexture(currentTexture);

            // 设置颜色和混合模式
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA); // 设置alpha混合模式

            // 禁用深度测试，确保透明部分正确渲染
            GL11.glDisable(GL11.GL_DEPTH_TEST);

            // 根据按钮尺寸缩放纹理坐标
            float u = 0.0F, v = 0.0F, u1 = 1.0F, v1 = 1.0F;

            // 绘制拉伸后的材质
            this.zLevel = 0.0F;
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glTexCoord2f(u, v); GL11.glVertex3f(this.xPosition, this.yPosition, this.zLevel);
            GL11.glTexCoord2f(u, v1); GL11.glVertex3f(this.xPosition, this.yPosition + this.height, this.zLevel);
            GL11.glTexCoord2f(u1, v1); GL11.glVertex3f(this.xPosition + this.width, this.yPosition + this.height, this.zLevel);
            GL11.glTexCoord2f(u1, v); GL11.glVertex3f(this.xPosition + this.width, this.yPosition, this.zLevel);
            GL11.glEnd();

            // 恢复深度测试
            GL11.glEnable(GL11.GL_DEPTH_TEST);

            // 绘制左侧装饰
            if (this.leftDecoration != null) {
                mc.getTextureManager().bindTexture(this.leftDecoration);
                this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 10, decorationWidth, this.height);
            }

            // 绘制右侧装饰
            if (this.rightDecoration != null) {
                mc.getTextureManager().bindTexture(this.rightDecoration);
                this.drawTexturedModalRect(this.xPosition + this.width - decorationWidth, this.yPosition, 0, 10, decorationWidth, this.height);
            }

            // 绘制按钮文本
            int textColor = this.textColor;
            if (!this.enabled) {
                this.setUseHoverEffect(false);
                textColor = disabledTextColor; // 不可用状态
            } else if (isHovered) {
                textColor = textColorHover; // 鼠标悬停状态
            } else if (this.useRGBEffect) {
                textColor = getRGBColor(); // RGB 效果
            }

            this.drawCenteredString(mc.fontRenderer, this.displayString,
                this.xPosition + this.width / 2,
                this.yPosition + (this.height - 8) / 2,
                textColor);

            // 禁用混合模式以避免影响其他绘制
            GL11.glDisable(GL11.GL_BLEND);
        }
    }
    }
