package com.imgood.hyperdimensionaltech.gui.costom;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class HT_GuiTextField extends GuiTextField {
    private ResourceLocation backgroundTexture;
    private ResourceLocation focusedBackgroundTexture;
    private String hintText = "";
    private int hintColor = 0x7F7F7F;
    private FontRenderer fontRendererObj = Minecraft.getMinecraft().fontRenderer;
    public int xPosition;
    public int yPosition;
    /** The width of this text field. */
    public int width;
    public int height;
    /** Has the current text being edited on the textbox. */
    private String text = "";
    private int cursorCounter;
    /** If this value is true along with isEnabled, keyTyped will process the keys. */
    private boolean isFocused;
    /** If this value is true along with isFocused, keyTyped will process the keys. */
    private boolean isEnabled = true;
    /** The current character index that should be used as start of the rendered text. */
    private int lineScrollOffset;
    private int cursorPosition;
    /** other selection position, maybe the same as the cursor */
    private int selectionEnd;
    private int enabledColor = 14737632;
    private int disabledColor = 7368816;

    public HT_GuiTextField(FontRenderer fontRendererObj, int x, int y, int width, int height) {
        super(fontRendererObj, x, y, width, height);
        this.setEnableBackgroundDrawing(false);
        this.yPosition = y-8;
        this.xPosition = x-21;
    }

    @Override
    public void drawTextBox() {
        if (this.getVisible()) {
                ResourceLocation textureToDraw = this.isFocused() && this.focusedBackgroundTexture != null ? this.focusedBackgroundTexture : this.backgroundTexture;
                if (textureToDraw != null) {
                    drawTexturedRect(this.xPosition - 1, this.yPosition - 1, this.width + 2, this.height + 2, textureToDraw);
                } else {
                    drawTexturedRect(this.xPosition - 1, this.yPosition - 1, this.width + 2, this.height + 2, textureToDraw);
                    super.drawTextBox();  // 使用原版逻辑绘制背景
                }

            if (this.getText().isEmpty() && !this.isFocused() && !hintText.isEmpty()) {
                this.fontRendererObj.drawStringWithShadow(this.hintText, this.xPosition + 4, this.yPosition + (this.height - 8) / 2, this.hintColor);
            } else {
                super.drawTextBox();  // 使用原版逻辑绘制文本
                //this.fontRendererObj.drawStringWithShadow(this.hintText, this.xPosition + 4, this.yPosition + (this.height - 8) / 2, this.hintColor);

            }
        }
    }


    private void drawTexturedRect(int x, int y, int width, int height, ResourceLocation texture) {
        // 重置颜色
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        Minecraft minecraft = Minecraft.getMinecraft();
        minecraft.getTextureManager().bindTexture(texture);

        // 开始绘制
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();

        float zLevel = 10000000000F;  // 一般将 zLevel 设置为 0.0F

        // 确保使用正确的纹理坐标和顶点顺序
        tessellator.addVertexWithUV(x, y + height, zLevel, 0, 1);         // 左下角
        tessellator.addVertexWithUV(x + width, y + height, zLevel, 1, 1);  // 右下角
        tessellator.addVertexWithUV(x + width, y, zLevel, 1, 0);          // 右上角
        tessellator.addVertexWithUV(x, y, zLevel, 0, 0);                   // 左上角

        // 完成绘制
        tessellator.draw();
    }

    public HT_GuiTextField setBackgroundTexture(ResourceLocation texture) {
        this.backgroundTexture = texture;
        return this;
    }

    public HT_GuiTextField setFocusedBackgroundTexture(ResourceLocation texture) {
        this.focusedBackgroundTexture = texture;
        return this;
    }

    public HT_GuiTextField setHintText(String hintText) {
        this.hintText = hintText;
        return this;
    }

    public HT_GuiTextField setHintColor(int color) {
        this.hintColor = color;
        return this;
    }

    public ResourceLocation getTextFieldTexture() {
        return this.backgroundTexture;
    }

    public ResourceLocation getFocusedTextFieldTexture() {
        return this.focusedBackgroundTexture;
    }
}
