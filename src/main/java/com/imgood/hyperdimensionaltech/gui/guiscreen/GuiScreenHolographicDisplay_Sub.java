package com.imgood.hyperdimensionaltech.gui.guiscreen;

import com.imgood.hyperdimensionaltech.gui.costom.HT_Button;
import com.imgood.hyperdimensionaltech.tiles.rendertiles.TileHolographicDisplay;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

import static com.imgood.hyperdimensionaltech.utils.Enums.Names.MOD_ID;
import static com.imgood.hyperdimensionaltech.utils.HT_ContentsHelper.isValidDouble;
import static com.imgood.hyperdimensionaltech.utils.HT_ContentsHelper.isValidHexColor;
import static com.imgood.hyperdimensionaltech.utils.HT_ContentsHelper.wrapText;
import static com.imgood.hyperdimensionaltech.utils.HT_ContentsHelper.isImageURL;

/**
 * @program: Hyperdimensional-Tech
 * @description: 全息告示牌屏幕类
 * @author: Imgood
 * @create: 2024-07-30 16:46
 **/
public class GuiScreenHolographicDisplay_Sub extends GuiScreen {

    private final TileHolographicDisplay tileHolographicDisplay;
    @SuppressWarnings("unused")
    private EntityPlayer player;
    private World world;

    private int index;
    private List<GuiTextField> textFieldsLeft = new ArrayList<>();
    private List<GuiTextField> textFieldsRight = new ArrayList<>();
    private GuiTextField hoveredTextField;
    private GuiTextField textFieldLine1;
    private GuiTextField textFieldLine2;
    private GuiTextField textFieldLine3;
    private GuiTextField textFieldLine4;
    private GuiTextField textFieldRGBColor;
    private GuiTextField textFieldLinesYOffset;


    private GuiTextField textFieldImgUrl;
    private GuiTextField textFieldImgScaledX;
    private GuiTextField textFieldImgScaledY;
    private GuiTextField textFieldImgStartX;
    private GuiTextField textFieldImgStartY;
    private GuiTextField textFieldLocation;
    private List<String> contents = new ArrayList<>();

    private static final ResourceLocation button_texture = new ResourceLocation(MOD_ID,"textures/gui/button_HolographicDisplay.png");
    private static final ResourceLocation button_hover_texture = new ResourceLocation(MOD_ID,"textures/gui/button_hover_HolographicDisplay.png");

    private int offsetX = 100;
    private int offsetY = 100;
    //private int interval = 30;
    private int textColor = 0x00FFFF;
    private int textHoverColor = 0x0055FF;

    private int buttonRowYOffset1 = 160;
    private int buttonRowYOffset2 = 130;
    private boolean buttonRow1RGB = false;
    private boolean buttonRow2RGB = false;
    private int buttonRow1Width = 50;
    private int buttonRow2Width = 50;

    private boolean isInitialized = false;

    public GuiScreenHolographicDisplay_Sub(EntityPlayer player, World world, TileHolographicDisplay tileEntity, int index) {
        this.player = player;
        this.world = world;
        this.tileHolographicDisplay = tileEntity;
        this.index = index;
    }

    private void saveCurrentState() {
        this.contents.clear();
        this.contents.add(textFieldLine1.getText());
        this.contents.add(textFieldLine2.getText());
        this.contents.add(textFieldLine3.getText());
        this.contents.add(textFieldLine4.getText());
        this.contents.add(textFieldRGBColor.getText());
        this.contents.add(textFieldLinesYOffset.getText());
        this.contents.add(textFieldImgUrl.getText());
        this.contents.add(textFieldImgScaledX.getText());
        this.contents.add(textFieldImgScaledY.getText());
        this.contents.add(textFieldImgStartX.getText());
        this.contents.add(textFieldImgStartY.getText());
        this.contents.add(textFieldLocation.getText());
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        //this.currentHashCode = this.tileHolographicDisplay.getImgPath(this.index);
        if (isInitialized) {
            saveCurrentState();
        } else {
            this.contents.clear();
            this.contents.add(this.tileHolographicDisplay.getText(this.index, 0));
            this.contents.add(this.tileHolographicDisplay.getText(this.index, 1));
            this.contents.add(this.tileHolographicDisplay.getText(this.index, 2));
            this.contents.add(this.tileHolographicDisplay.getText(this.index, 3));
            this.contents.add(this.tileHolographicDisplay.getRGBColor(this.index));
            this.contents.add(this.tileHolographicDisplay.getLinesYOffset(this.index) + "");

            this.contents.add(this.tileHolographicDisplay.getImgURL(this.index));
            this.contents.add(this.tileHolographicDisplay.getImgScaledX(this.index) + "");
            this.contents.add(this.tileHolographicDisplay.getImgScaledY(this.index) + "");
            this.contents.add(this.tileHolographicDisplay.getImgStartX(this.index) + "");
            this.contents.add(this.tileHolographicDisplay.getImgStartY(this.index) + "");
            this.contents.add(this.tileHolographicDisplay.getImgPath(this.index));
            isInitialized = true;
        }

        this.offsetX = (this.width / 2) - 192;
        this.offsetY = (this.height / 2) - 90;
        this.updateScreen();
        this.buttonList.clear();

        this.textFieldsLeft.clear();
        textFieldsLeft.add(this.textFieldLine1);
        textFieldsLeft.add(this.textFieldLine2);
        textFieldsLeft.add(this.textFieldLine3);
        textFieldsLeft.add(this.textFieldLine4);
        textFieldsLeft.add(this.textFieldRGBColor);
        textFieldsLeft.add(this.textFieldLinesYOffset);
        autoTextField("Left",this.textFieldsLeft, 0, 20,this.offsetX+50, this.offsetY, 87, 20);

        this.textFieldsRight.clear();
        textFieldsRight.add(this.textFieldImgUrl);
        textFieldsRight.add(this.textFieldImgScaledX);
        textFieldsRight.add(this.textFieldImgScaledY);
        textFieldsRight.add(this.textFieldImgStartX);
        textFieldsRight.add(this.textFieldImgStartY);
        textFieldsRight.add(this.textFieldLocation);
        autoTextField("Right",this.textFieldsRight, 0, 20,this.offsetX+205, this.offsetY, 87, 20);

        this.textFieldLine1.setMaxStringLength(100);
        this.textFieldLine1.setFocused(true);
        this.textFieldLine1.setText(this.tileHolographicDisplay.getText(this.index,0));
        this.textFieldLine2.setMaxStringLength(100);
        this.textFieldLine2.setText(this.tileHolographicDisplay.getText(this.index,1));
        this.textFieldLine3.setMaxStringLength(100);
        this.textFieldLine3.setText(this.tileHolographicDisplay.getText(this.index,2));
        this.textFieldLine4.setMaxStringLength(100);
        this.textFieldLine4.setText(this.tileHolographicDisplay.getText(this.index,3));
        this.textFieldRGBColor.setMaxStringLength(6);
        this.textFieldRGBColor.setText(this.tileHolographicDisplay.getRGBColor(this.index));
        this.textFieldLinesYOffset.setMaxStringLength(5);
        this.textFieldLinesYOffset.setText(this.tileHolographicDisplay.getLinesYOffset(this.index)+"");

        this.textFieldImgUrl.setMaxStringLength(100);
        this.textFieldImgUrl.setText(this.tileHolographicDisplay.getImgURL(this.index));
        this.textFieldImgScaledX.setMaxStringLength(5);
        this.textFieldImgScaledX.setText(this.tileHolographicDisplay.getImgScaledX(this.index)+"");
        this.textFieldImgScaledY.setMaxStringLength(5);
        this.textFieldImgScaledY.setText(this.tileHolographicDisplay.getImgScaledY(this.index)+"");
        this.textFieldImgStartX.setMaxStringLength(5);
        this.textFieldImgStartX.setText(this.tileHolographicDisplay.getImgStartX(this.index)+"");
        this.textFieldImgStartY.setMaxStringLength(5);
        this.textFieldImgStartY.setText(this.tileHolographicDisplay.getImgStartY(this.index)+"");
        this.textFieldLocation.setMaxStringLength(100);
        this.textFieldLocation.setText(this.tileHolographicDisplay.getImgPath(this.index));

        //region 保存/取消按钮
        this.buttonList.add(new HT_Button(0,
            this.offsetX + 0,
            this.offsetY + buttonRowYOffset1,
            buttonRow1Width,
            20,
            "Save")
            .setTexture(button_texture)
            .setHoverTexture(button_hover_texture)
            .setUseRGBEffect(buttonRow1RGB)
            .setUseHoverEffect(true)
            .setLeftDecoration(button_hover_texture)
            .setRightDecoration(button_hover_texture)
            .setDecorationWidth(20)
            .setTextColor(textColor)
            .setTextHoverColor(textHoverColor));
        this.buttonList.add(new HT_Button(1,
            this.offsetX + 70,
            this.offsetY + buttonRowYOffset1,
            buttonRow1Width,
            20,
            "Cancel")
            .setTexture(button_texture)
            .setHoverTexture(button_hover_texture)
            .setUseRGBEffect(buttonRow1RGB)
            .setUseHoverEffect(true)
            .setTextColor(textColor)
            .setTextHoverColor(textHoverColor));

        this.buttonList.add(new HT_Button(2,
            this.offsetX + 0,
            this.offsetY + buttonRowYOffset2,
            buttonRow2Width,
            20,
            "§lBold")
            .setTexture(button_texture)
            .setHoverTexture(button_hover_texture)
            .setUseRGBEffect(buttonRow2RGB)
            .setUseHoverEffect(true)
            .setTextColor(textColor)
            .setTextHoverColor(textHoverColor));
        this.buttonList.add(new HT_Button(3,
            this.offsetX + 70,
            this.offsetY + buttonRowYOffset2,
            buttonRow2Width,
            20,
            "§oItalic")
            .setTexture(button_texture)
            .setHoverTexture(button_hover_texture)
            .setUseRGBEffect(buttonRow2RGB)
            .setUseHoverEffect(true)
            .setTextColor(textColor)
            .setTextHoverColor(textHoverColor));
        this.buttonList.add(new HT_Button(4,
            this.offsetX + 140,
            this.offsetY + buttonRowYOffset2,
            buttonRow2Width,
            20,
            "§nUnderline")
            .setTexture(button_texture)
            .setHoverTexture(button_hover_texture)
            .setUseRGBEffect(buttonRow2RGB)
            .setUseHoverEffect(true)
            .setTextColor(textColor)
            .setTextHoverColor(textHoverColor));
        this.buttonList.add(new HT_Button(5,
            this.offsetX + 210,
            this.offsetY + buttonRowYOffset2,
            buttonRow2Width,
            20,
            "§mStrike")
            .setTexture(button_texture)
            .setHoverTexture(button_hover_texture)
            .setUseRGBEffect(buttonRow2RGB)
            .setUseHoverEffect(true)
            .setTextColor(textColor)
            .setTextHoverColor(textHoverColor));
        this.buttonList.add(new HT_Button(6,
            this.offsetX + 280,
            this.offsetY + buttonRowYOffset2,
            buttonRow2Width,
            20,
            "RGB")
            .setTexture(button_texture)
            .setHoverTexture(button_hover_texture)
            .setUseRGBEffect(buttonRow2RGB)
            .setUseHoverEffect(true)
            .setTextColor(textColor)
            .setTextHoverColor(textHoverColor));

        //endregion

        /*//custom展示下怎么用的
        HT_Button customButton = new HT_Button(0, this.width / 2 - 100, this.height / 2 - 20, 20, 20, "RGB");

        // 设置普通状态的材质
        customButton.setTexture(new ResourceLocation(MOD_ID, "textures/gui/button_HolographicDisplay.png"));

        // 设置鼠标悬停状态的材质（可选）
        customButton.setHoverTexture(new ResourceLocation(MOD_ID,"textures/gui/button_HolographicDisplay.png"));

        // 设置文字颜色
        customButton.setTextColor(0xFF0000); // 红色文字

        // 启用或禁用鼠标悬停效果
        customButton.setUseHoverEffect(true);

        this.buttonList.add(customButton);*/

    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        isInitialized = false;
    }

    public void autoTextField(String row,List<GuiTextField> textFields, int intervalX, int intervalY, int startX, int startY, int width, int height) {
        int intervalXCurrent = 0;
        int intervalYCurrent = 0;
        for (int i = 0; i < textFields.size(); i++) {
            GuiTextField textField = new GuiTextField(this.fontRendererObj, startX + intervalXCurrent, startY + intervalYCurrent, width, height);
            textFields.set(i, textField);
            intervalXCurrent += intervalX;
            intervalYCurrent += intervalY;

            if (row.equals("Left")) {
                switch (i) {
                    case 0: this.textFieldLine1 = textField; break;
                    case 1: this.textFieldLine2 = textField; break;
                    case 2: this.textFieldLine3 = textField; break;
                    case 3: this.textFieldLine4 = textField; break;
                    case 4: this.textFieldRGBColor = textField; break;
                    case 5: this.textFieldLinesYOffset = textField; break;
                }
            } else if (row.equals("Right")) {
                switch (i) {
                    case 0: this.textFieldImgUrl = textField; break;
                    case 1: this.textFieldImgScaledX = textField; break;
                    case 2: this.textFieldImgScaledY = textField; break;
                    case 3: this.textFieldImgStartX = textField; break;
                    case 4: this.textFieldImgStartY = textField; break;
                    case 5: this.textFieldLocation = textField; break;
                }
            }
            // 根据 i 的值更新对应的成员变量

        }
    }

    public void autoText(String[] text, int intervalX, int intervalY, int startX, int startY, int color){
        int intervalXCurrent = 0;
        int intervalYCurrent = 0;
        for (String t : text){
            this.fontRendererObj.drawString(t, startX + intervalXCurrent, startY + intervalYCurrent, this.textColor);
            intervalXCurrent += intervalX;
            intervalYCurrent += intervalY;
        }
    }

    private boolean isMouseOver(GuiTextField textField, int mouseX, int mouseY) {
        return mouseX >= textField.xPosition && mouseX < textField.xPosition + textField.width &&
            mouseY >= textField.yPosition && mouseY < textField.yPosition + textField.height;
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        NBTTagCompound nbt;
        switch (button.id) {
            case 0 -> {
                nbt = new NBTTagCompound();
                nbt.setString("Text1", this.textFieldLine1.getText());
                nbt.setString("Text2", this.textFieldLine2.getText());
                nbt.setString("Text3", this.textFieldLine3.getText());
                nbt.setString("Text4", this.textFieldLine4.getText());
                //nbt.setString("ImgURL", this.textFieldImgUrl.getText());
                if (!isImageURL(this.textFieldImgUrl.getText())) {
                    // 如果 RGB 颜色不是有效的十六进制字符串，则恢复之前的状态
                    this.textFieldImgUrl.setText(this.tileHolographicDisplay.getImgURL(this.index));
                } else {
                    nbt.setString("ImgURL", this.textFieldImgUrl.getText());
                    //this.tileHolographicDisplay.setRGBColor(this.index, this.textFieldRGBColor.getText());
                }
                if (!isValidHexColor(this.textFieldRGBColor.getText())) {
                    // 如果 RGB 颜色不是有效的十六进制字符串，则恢复之前的状态
                    this.textFieldRGBColor.setText(this.tileHolographicDisplay.getRGBColor(this.index));
                } else {
                    nbt.setString("RGBColor", this.textFieldRGBColor.getText());
                    //this.tileHolographicDisplay.setRGBColor(this.index, this.textFieldRGBColor.getText());
                }
                if (!isValidDouble(this.textFieldImgStartX.getText())) {
                    this.textFieldImgStartX.setText(String.valueOf(this.tileHolographicDisplay.getRGBColor(this.index)));
                } else {
                    nbt.setDouble("ImgStartX", Double.parseDouble(this.textFieldImgStartX.getText()));
                    //this.tileHolographicDisplay.setImgStartX(this.index, Double.parseDouble(this.textFieldImgStartX.getText()));
                }
                if (!isValidDouble(this.textFieldImgStartY.getText())) {
                    this.textFieldImgStartY.setText(String.valueOf(this.tileHolographicDisplay.getRGBColor(this.index)));
                } else {
                    nbt.setDouble("ImgStartY", Double.parseDouble(this.textFieldImgStartY.getText()));
                    //this.tileHolographicDisplay.setImgStartY(this.index, Double.parseDouble(this.textFieldImgStartY.getText()));
                }
                if (isValidDouble(this.textFieldImgScaledX.getText())) {
                    nbt.setDouble("ImgScaledX", Double.parseDouble(this.textFieldImgScaledX.getText()));
                    //this.tileHolographicDisplay.setImgScaledX(this.index, Double.parseDouble(this.textFieldImgScaledX.getText()));
                } else {
                    this.textFieldImgScaledX.setText(String.valueOf(this.tileHolographicDisplay.getRGBColor(this.index)));
                }
                if (isValidDouble(this.textFieldImgScaledY.getText())) {
                    nbt.setDouble("ImgScaledY", Double.parseDouble(this.textFieldImgScaledY.getText()));
                    //this.tileHolographicDisplay.setImgScaledY(this.index, Double.parseDouble(this.textFieldImgScaledY.getText()));
                } else {
                    this.textFieldImgScaledY.setText(String.valueOf(this.tileHolographicDisplay.getRGBColor(this.index)));
                }
                if (isValidDouble(this.textFieldLinesYOffset.getText())) {
                    nbt.setDouble("LinesYOffset", Double.parseDouble(this.textFieldLinesYOffset.getText()));
                    //this.tileHolographicDisplay.setImgScaledY(this.index, Double.parseDouble(this.textFieldImgScaledY.getText()));
                } else {
                    this.textFieldLinesYOffset.setText(String.valueOf(this.tileHolographicDisplay.getLinesYOffset(this.index)));
                }
//                this.tileHolographicDisplay.setText(0, this.textFieldLine1.getText());
//                this.tileHolographicDisplay.setText(1, this.textFieldLine2.getText());
//                this.tileHolographicDisplay.setText(2, this.textFieldLine3.getText());
//                this.tileHolographicDisplay.setText(3, this.textFieldLine4.getText());
//                this.tileHolographicDisplay.setImgURL(this.index, this.textFieldImgUrl.getText());
                //this.tileHolographicDisplay.loadImageAsync(this.index, this.textFieldImgUrl.getText());

                this.tileHolographicDisplay.setDisplayData(this.index,nbt);
                this.tileHolographicDisplay.writeToNBT(nbt);
                isInitialized = false;
                //this.mc.displayGuiScreen(null);
                this.mc.displayGuiScreen(new GuiScreenHolographicDisplay_Main(this.player, this.world, this.tileHolographicDisplay));
            }
            case 1 -> {
                //this.mc.displayGuiScreen(null);
                this.mc.displayGuiScreen(new GuiScreenHolographicDisplay_Main(this.player, this.world, this.tileHolographicDisplay));
            }
            case 7 -> {
//                this.tileHolographicDisplay.loadImageAsync(this.index, this.textFieldImgUrl.getText());
//                this.currentHashCode = this.tileHolographicDisplay.getImgPath(this.index);
             }
            default -> {
                // 处理其他按钮的行为
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        super.keyTyped(typedChar, keyCode);
        for (GuiTextField textField : textFieldsLeft) {
            textField.textboxKeyTyped(typedChar, keyCode);
        }
        for (GuiTextField textField : textFieldsRight) {
            textField.textboxKeyTyped(typedChar, keyCode);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        for (GuiTextField textField : textFieldsLeft) {
            textField.mouseClicked(mouseX, mouseY, mouseButton);
        }
        for (GuiTextField textField : textFieldsRight) {
            textField.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        for (GuiTextField textField : textFieldsLeft) {
            textField.updateCursorCounter();
        }
        for (GuiTextField textField : textFieldsRight) {
            textField.updateCursorCounter();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        String[] text = {"§lLine1", "§lLine2", "§lLine3", "§lLine4", "§lColor"};
        autoText(text, 0, 22, this.offsetX, this.offsetY, this.textColor);
        String[] text2 = {"§lImgUrl", "§lWidth", "§lHeight", "§lStartX", "§lStartY", "§lLocation"};
        autoText(text2, 0, 22, this.offsetX+150, this.offsetY, this.textColor);

        this.drawCenteredString(this.fontRendererObj, "§lData Config Of §n"+(this.index+1), this.offsetX+192, this.offsetY-20, this.textColor);
        //this.drawCenteredString(this.fontRendererObj, this.currentHashCode, this.offsetX+192, this.offsetY+100, this.textColor);

        hoveredTextField = null;
        for (GuiTextField textField : textFieldsLeft) {
            textField.drawTextBox();
            if (isMouseOver(textField, mouseX, mouseY)) {
                hoveredTextField = textField;
            }
        }
        for (GuiTextField textField : textFieldsRight) {
            textField.drawTextBox();
            if (isMouseOver(textField, mouseX, mouseY)) {
                hoveredTextField = textField;
            }
        }

        super.drawScreen(mouseX, mouseY, partialTicks);

        // 绘制 tooltip
        if (hoveredTextField != null) {
            List<String> wrappedText = wrapText(hoveredTextField.getText(), 12);
            drawColoredHoveringText(wrappedText, mouseX, mouseY, this.index);
            //drawHoveringText(wrappedText, mouseX, mouseY, this.fontRendererObj);
        }
    }

    private void drawTooltipBackground(int x, int y, int width, int height) {
        //弃用了所以给了个0
        int borderSize = 0;

        // 绘制背景（使用 OpenGL 绘制一个纯色或半透明矩形）
        drawRect(x, y, x + width, y + height, 0xaf00aaaa);

        // 左上角
        drawTexturedModalRect(x - borderSize, y - borderSize, 0, 0, borderSize, borderSize);
        // 右上角
        drawTexturedModalRect(x + width, y - borderSize, 16 - borderSize, 0, borderSize, borderSize);
        // 左下角
        drawTexturedModalRect(x - borderSize, y + height, 0, 16 - borderSize, borderSize, borderSize);
        // 右下角
        drawTexturedModalRect(x + width, y + height, 16 - borderSize, 16 - borderSize, borderSize, borderSize);

        /*
        // 上边框
        drawTexturedModalRect(x, y - borderSize, borderSize, 0, width, borderSize);
        // 下边框
        drawTexturedModalRect(x, y + height, borderSize, 16 - borderSize, width, borderSize);
        // 左边框
        drawTexturedModalRect(x - borderSize, y, 0, borderSize, borderSize, height);
        // 右边框
        drawTexturedModalRect(x + width, y, 16 - borderSize, borderSize, borderSize, height);*/
    }

    private void drawColoredHoveringText(List<String> textLines, int x, int y, int buttonId) {
        if (!textLines.isEmpty()) {
            int tooltipTextWidth = 0;
            for (String line : textLines) {
                int lineWidth = this.fontRendererObj.getStringWidth(line);
                if (lineWidth > tooltipTextWidth) {
                    tooltipTextWidth = lineWidth;
                }
            }
            tooltipTextWidth += 1;
            int tooltipX = x + 12;
            int tooltipY = y - 12;
            int tooltipHeight = 8;

            if (textLines.size() > 1) {
                tooltipHeight += 2 + (textLines.size() - 1) * 10;
            }

            if (tooltipX + tooltipTextWidth > this.width) {
                tooltipX -= 28 + tooltipTextWidth;
            }

            if (tooltipY + tooltipHeight + 6 > this.height) {
                tooltipY = this.height - tooltipHeight - 6;
            }

            tooltipX += 10;
            tooltipY += 10;

            //region 绘制背景
            // 绘制背景和边框
            this.zLevel = 0F;
            drawTooltipBackground(tooltipX, tooltipY, tooltipTextWidth, tooltipHeight);
            //end region


            for (int lineNumber = 0; lineNumber < textLines.size(); ++lineNumber) {
                String line = textLines.get(lineNumber);
                int colonIndex = line.indexOf(':');
                String[] buff;
                if (colonIndex != -1) {
                    buff = new String[]{
                        line.substring(0, colonIndex),
                        colonIndex < line.length() - 1 ? line.substring(colonIndex + 1).trim() : ""
                    };
                } else {
                    buff = new String[]{line, ""};
                }
                if (line.contains("Color:") || line.contains("Text1:") || line.contains("Text2:") || line.contains("Text3:") || line.contains("Text4:")) {
                    this.fontRendererObj.drawStringWithShadow(buff[0], tooltipX, tooltipY, Integer.parseInt("00ffff", 16));
                    if (!buff[1].isEmpty()) {
                        this.fontRendererObj.drawStringWithShadow(buff[1], tooltipX + this.fontRendererObj.getStringWidth(buff[0]), tooltipY, Integer.parseInt(this.tileHolographicDisplay.getRGBColor(buttonId), 16));
                    }
                } else {
                    this.fontRendererObj.drawStringWithShadow( buff[0], tooltipX, tooltipY, Integer.parseInt("00ffff", 16));
                    if (!buff[1].isEmpty()) {
                        this.fontRendererObj.drawStringWithShadow(buff[1], tooltipX + this.fontRendererObj.getStringWidth(buff[0]), tooltipY, Integer.parseInt("00ffff", 16));
                    }
                }

                if (lineNumber == 0) {
                    tooltipY += 2;
                }

                tooltipY += 10;
            }

            this.zLevel = 0.0F;
        }
    }
}
