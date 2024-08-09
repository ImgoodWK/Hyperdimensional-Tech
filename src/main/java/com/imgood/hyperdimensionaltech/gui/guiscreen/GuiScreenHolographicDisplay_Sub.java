package com.imgood.hyperdimensionaltech.gui.guiscreen;

import com.imgood.hyperdimensionaltech.gui.costom.HT_GuiButton;
import com.imgood.hyperdimensionaltech.gui.costom.HT_GuiScreen;
import com.imgood.hyperdimensionaltech.gui.costom.HT_GuiTextField;
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
@SuppressWarnings("ALL")
public class GuiScreenHolographicDisplay_Sub extends HT_GuiScreen {

    private final TileHolographicDisplay tileHolographicDisplay;
    @SuppressWarnings("unused")
    private EntityPlayer player;
    private World world;

    private int index;
    private List<HT_GuiTextField> textFieldsLeft = new ArrayList<>();
    private List<HT_GuiTextField> textFieldsRight = new ArrayList<>();
    private HT_GuiTextField hoveredTextField;
    private HT_GuiTextField textFieldLine1;
    private HT_GuiTextField textFieldLine2;
    private HT_GuiTextField textFieldLine3;
    private HT_GuiTextField textFieldLine4;
    private HT_GuiTextField textFieldRGBColor;
    private HT_GuiTextField textFieldLinesYOffset;
    private HT_GuiTextField textFieldTextScaled;


    private HT_GuiTextField textFieldImgUrl;
    private HT_GuiTextField textFieldImgScaledX;
    private HT_GuiTextField textFieldImgScaledY;
    private HT_GuiTextField textFieldImgStartX;
    private HT_GuiTextField textFieldImgStartY;
    private HT_GuiTextField textFieldLocation;
    private List<String> contents = new ArrayList<>();

    private static final ResourceLocation button_texture = new ResourceLocation(MOD_ID,"textures/gui/button_HolographicDisplay.png");
    private static final ResourceLocation button_hover_texture = new ResourceLocation(MOD_ID,"textures/gui/button_hover_HolographicDisplay.png");
    private static final ResourceLocation guiScreenHolographicDisplay_Main_Background = new ResourceLocation(MOD_ID, "textures/gui/background_HolographicDisplay_Main.png");
    private static final ResourceLocation textField_texture = new ResourceLocation(MOD_ID, "textures/gui/textfield_HolographicDisplay_8020.png");
    private static final ResourceLocation textField_hover_texture = new ResourceLocation(MOD_ID, "textures/gui/textfield_hover_HolographicDisplay_8020.png");
    private static final ResourceLocation textField_selected_texture = new ResourceLocation(MOD_ID, "textures/gui/textfield_selected_HolographicDisplay.png");
    private static final ResourceLocation textField_selected_texture_01 = new ResourceLocation(MOD_ID, "textures/gui/textfield_selected_HolographicDisplay_1.png");
    private static final ResourceLocation getGuiScreenHolographicDisplay_Sub_Background = new ResourceLocation(MOD_ID, "textures/gui/background_HolographicDisplay_Sub.png");

    private int offsetX = 100;
    private int offsetY = 100;
    //private int interval = 30;
    private int textColor = 0x00FFFF;
    private int textHoverColor = 0x0055FF;

    private int buttonRowYOffset1 = 210;
    private int buttonRowYOffset2 = 180;
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
        this.setBackgroundTexture(getGuiScreenHolographicDisplay_Sub_Background);

        this.setSize(420, 280);
        this.setStretch(false);
    }

    private void saveCurrentState() {
        this.contents.clear();
        this.contents.add(textFieldLine1.getText());
        this.contents.add(textFieldLine2.getText());
        this.contents.add(textFieldLine3.getText());
        this.contents.add(textFieldLine4.getText());
        this.contents.add(textFieldRGBColor.getText());
        this.contents.add(textFieldLinesYOffset.getText());
        this.contents.add(textFieldTextScaled.getText());
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
            this.contents.add(this.tileHolographicDisplay.getTextScaled(this.index) + "");

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
        this.setPosition(this.offsetX-20, this.offsetY-35);
        this.textFieldsLeft.clear();
        textFieldsLeft.add(this.textFieldLine1);
        textFieldsLeft.add(this.textFieldLine2);
        textFieldsLeft.add(this.textFieldLine3);
        textFieldsLeft.add(this.textFieldLine4);
        textFieldsLeft.add(this.textFieldRGBColor);
        textFieldsLeft.add(this.textFieldLinesYOffset);
        textFieldsLeft.add(this.textFieldTextScaled);
        try {
            autoTextField("Left",this.textFieldsLeft, 0, 25,this.offsetX+50, this.offsetY, 80, 20);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        this.textFieldsRight.clear();
        textFieldsRight.add(this.textFieldImgUrl);
        textFieldsRight.add(this.textFieldImgScaledX);
        textFieldsRight.add(this.textFieldImgScaledY);
        textFieldsRight.add(this.textFieldImgStartX);
        textFieldsRight.add(this.textFieldImgStartY);
        textFieldsRight.add(this.textFieldLocation);
        try {
            autoTextField("Right",this.textFieldsRight, 0, 25,this.offsetX+205, this.offsetY, 80, 20);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

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
        this.textFieldTextScaled.setMaxStringLength(6);
        this.textFieldTextScaled.setText(this.tileHolographicDisplay.getTextScaled(this.index)+"");

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
        this.buttonList.add(new HT_GuiButton(0,
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
        this.buttonList.add(new HT_GuiButton(1,
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

        this.buttonList.add(new HT_GuiButton(2,
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
        this.buttonList.add(new HT_GuiButton(3,
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
        this.buttonList.add(new HT_GuiButton(4,
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
        this.buttonList.add(new HT_GuiButton(5,
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
        this.buttonList.add(new HT_GuiButton(6,
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
        getButtonByid(2).displayString = (getLinesContains("§l") ? "Bold" : "§lBold");
        getButtonByid(3).displayString = (getLinesContains("§o") ? "Italic" : "§oItalic");
        getButtonByid(4).displayString = (getLinesContains("§n") ? "Underline" : "§nUnderline");
        getButtonByid(5).displayString = (getLinesContains("§m") ? "Strike" : "§mStrike");
        if (this.tileHolographicDisplay.isRGB(this.index)) {
            ((HT_GuiButton)getButtonByid(6)).setUseRGBEffect(false);
        } else {
            ((HT_GuiButton)getButtonByid(6)).setUseRGBEffect(true);
        }


    }

    private boolean textContains(GuiButton guiButton,String contains) {
        return guiButton.displayString.contains(contains);
    }

    private void setButtonDisplayString (int id, String displayString) {
        for (GuiButton button : this.buttonList) {
            if (button.id == id) {
                button.displayString = displayString;
                //这里不return是考虑到button id不是唯一的
            }
        }
    }

    private GuiButton getButtonByid (int id) {
        for (GuiButton button : this.buttonList) {
            if (button.id == id) {
                return button;
                //button id不是唯一的，但会返回第一个检查到的
            }
        }
        return null;
    }

    private boolean getLinesContains(String contains) {
        String[] contents = this.tileHolographicDisplay.getContents(this.index);
        for (String content : contents) {
            if (content.contains(contains)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        isInitialized = false;
    }

    public void autoTextField(String row,List<HT_GuiTextField> textFields, int intervalX, int intervalY, int startX, int startY, int width, int height) throws NoSuchFieldException, IllegalAccessException {
        int intervalXCurrent = 0;
        int intervalYCurrent = 0;
        for (int i = 0; i < textFields.size(); i++) {
            HT_GuiTextField textField = new HT_GuiTextField(this.fontRendererObj,
                startX + intervalXCurrent,
                startY + intervalYCurrent,
                width,
                height)
                .setBackgroundTexture(textField_texture)
                .setFocusedBackgroundTexture(textField_hover_texture);
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
                    case 6: this.textFieldTextScaled = textField; break;
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

    public void drawTextFieldBackground(HT_GuiTextField textField, int x, int y, int width, int height) {
        this.drawImage(textField.getTextFieldTexture(), x, y, width, height);
    }

    public void drawTextFieldFocusBackground(HT_GuiTextField textField, int x, int y, int width, int height) {
        this.drawImage(textField.getFocusedTextFieldTexture(), x, y, width, height);
    }

    public void drawTextFieldBackground(List<HT_GuiTextField> textField) {
        int xCoord;
        int yCoord;
        int textWidth;
        int textHeight;
        for (HT_GuiTextField textFieldCurrunt : textField)
        {
            xCoord = textFieldCurrunt.xPosition;
            yCoord = textFieldCurrunt.yPosition+2;
            textWidth = textFieldCurrunt.width+20;
            textHeight = textFieldCurrunt.height;
            if (textFieldCurrunt.isFocused()) {
                drawTextFieldFocusBackground(textFieldCurrunt,xCoord, yCoord, 100, 20);
            } else {
                drawTextFieldBackground(textFieldCurrunt, xCoord, yCoord, 100, 20);
            }
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

    private boolean isMouseOver(HT_GuiTextField textField, int mouseX, int mouseY) {
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
                if (isValidDouble(this.textFieldTextScaled.getText())) {
                    nbt.setDouble("TextScaled", Double.parseDouble(this.textFieldTextScaled.getText()));
                    //this.tileHolographicDisplay.setImgScaledY(this.index, Double.parseDouble(this.textFieldImgScaledY.getText()));
                } else {
                    this.textFieldImgScaledX.setText(String.valueOf(this.tileHolographicDisplay.getRGBColor(this.index)));
                }
//                this.tileHolographicDisplay.setText(0, this.textFieldLine1.getText());
//                this.tileHolographicDisplay.setText(1, this.textFieldLine2.getText());
//                this.tileHolographicDisplay.setText(2, this.textFieldLine3.getText());
//                this.tileHolographicDisplay.setText(3, this.textFieldLine4.getText());
//                this.tileHolographicDisplay.setImgURL(this.index, this.textFieldImgUrl.getText());
                //this.tileHolographicDisplay.loadImageAsync(this.index, this.textFieldImgUrl.getText());
                nbt.setBoolean("isRGB", this.tileHolographicDisplay.isRGB(this.index));
                this.tileHolographicDisplay.setDisplayData(this.index,nbt);
                this.tileHolographicDisplay.writeToNBT(nbt);
                isInitialized = false;
                //this.mc.displayGuiScreen(null);
                this.mc.displayGuiScreen(new GuiScreenHolographicDisplay_Main(this.player, this.world, this.tileHolographicDisplay)
                    .setPosition(0, 0)
                    .setSize(200, 200)
                    .setStretch(true)
                    .setBackgroundTexture(guiScreenHolographicDisplay_Main_Background));
            }
            case 1 -> {
                //this.mc.displayGuiScreen(null);
                this.mc.displayGuiScreen(new GuiScreenHolographicDisplay_Main(this.player, this.world, this.tileHolographicDisplay)
                    .setPosition(0, 0)
                    .setSize(200, 200)
                    .setStretch(true)
                    .setBackgroundTexture(guiScreenHolographicDisplay_Main_Background));
            }
            case 6 -> {
                ((HT_GuiButton)button).setUseRGBEffect(!((HT_GuiButton) button).getUseRGBEffect());
                this.tileHolographicDisplay.isRGB(this.index, !this.tileHolographicDisplay.isRGB(this.index));
             }

             case 2 -> {
                for(GuiButton button2 : this.buttonList) {
                    if(button2.id == 2 && button2.displayString.contains("§l")) {
                        this.textFieldLine1.setText("§l"+this.textFieldLine1.getText());
                        this.textFieldLine2.setText("§l"+this.textFieldLine2.getText());
                        this.textFieldLine3.setText("§l"+this.textFieldLine3.getText());
                        this.textFieldLine4.setText("§l"+this.textFieldLine4.getText());
                        button2.displayString = button2.displayString.replace("§l", "");
                    } else if (button2.id == 2 && !button2.displayString.contains("§l")){
                        this.textFieldLine1.setText(this.textFieldLine1.getText().replace("§l", ""));
                        this.textFieldLine2.setText(this.textFieldLine2.getText().replace("§l", ""));
                        this.textFieldLine3.setText(this.textFieldLine3.getText().replace("§l", ""));
                        this.textFieldLine4.setText(this.textFieldLine4.getText().replace("§l", ""));
                        button2.displayString = "§l" + button2.displayString;
                    }
                }
            }
            case 3 -> {
                for(GuiButton button3 : this.buttonList) {
                    if(button3.id == 3 && button3.displayString.contains("§o")) {
                        this.textFieldLine1.setText("§o"+this.textFieldLine1.getText());
                        this.textFieldLine2.setText("§o"+this.textFieldLine2.getText());
                        this.textFieldLine3.setText("§o"+this.textFieldLine3.getText());
                        this.textFieldLine4.setText("§o"+this.textFieldLine4.getText());
                        button3.displayString = button3.displayString.replace("§o", "");
                    } else if (button3.id == 3 && !button3.displayString.contains("§o")){
                        this.textFieldLine1.setText(this.textFieldLine1.getText().replace("§o", ""));
                        this.textFieldLine2.setText(this.textFieldLine2.getText().replace("§o", ""));
                        this.textFieldLine3.setText(this.textFieldLine3.getText().replace("§o", ""));
                        this.textFieldLine4.setText(this.textFieldLine4.getText().replace("§o", ""));
                        button3.displayString = "§o" + button3.displayString;
                    }
                }
            }
            case 4 -> {
                for(GuiButton button4 : this.buttonList) {
                    if(button4.id == 4 && button4.displayString.contains("§n")) {
                        this.textFieldLine1.setText("§n"+this.textFieldLine1.getText());
                        this.textFieldLine2.setText("§n"+this.textFieldLine2.getText());
                        this.textFieldLine3.setText("§n"+this.textFieldLine3.getText());
                        this.textFieldLine4.setText("§n"+this.textFieldLine4.getText());
                        button4.displayString = button4.displayString.replace("§n", "");
                    } else if (button4.id == 4 && !button4.displayString.contains("§n")){
                        this.textFieldLine1.setText(this.textFieldLine1.getText().replace("§n", ""));
                        this.textFieldLine2.setText(this.textFieldLine2.getText().replace("§n", ""));
                        this.textFieldLine3.setText(this.textFieldLine3.getText().replace("§n", ""));
                        this.textFieldLine4.setText(this.textFieldLine4.getText().replace("§n", ""));
                        button4.displayString = "§n" + button4.displayString;
                    }
                }
            }
            case 5 -> {
                for(GuiButton button5 : this.buttonList) {
                    if(button5.id == 5 && button5.displayString.contains("§m")) {
                        this.textFieldLine1.setText("§m"+this.textFieldLine1.getText());
                        this.textFieldLine2.setText("§m"+this.textFieldLine2.getText());
                        this.textFieldLine3.setText("§m"+this.textFieldLine3.getText());
                        this.textFieldLine4.setText("§m"+this.textFieldLine4.getText());
                        button5.displayString = button5.displayString.replace("§m", "");
                    } else if (button5.id == 5 && !button5.displayString.contains("§m")){
                        this.textFieldLine1.setText(this.textFieldLine1.getText().replace("§m", ""));
                        this.textFieldLine2.setText(this.textFieldLine2.getText().replace("§m", ""));
                        this.textFieldLine3.setText(this.textFieldLine3.getText().replace("§m", ""));
                        this.textFieldLine4.setText(this.textFieldLine4.getText().replace("§m", ""));
                        button5.displayString = "§m" + button5.displayString;
                    }
                }
            }
            default -> {
                // 处理其他按钮的行为
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        super.keyTyped(typedChar, keyCode);
        for (HT_GuiTextField textField : textFieldsLeft) {
            textField.textboxKeyTyped(typedChar, keyCode);
        }
        for (HT_GuiTextField textField : textFieldsRight) {
            textField.textboxKeyTyped(typedChar, keyCode);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        for (HT_GuiTextField textField : textFieldsLeft) {
            textField.mouseClicked(mouseX, mouseY, mouseButton);
        }
        for (HT_GuiTextField textField : textFieldsRight) {
            textField.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        for (HT_GuiTextField textField : textFieldsLeft) {
            textField.updateCursorCounter();
        }
        for (HT_GuiTextField textField : textFieldsRight) {
            textField.updateCursorCounter();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();

        String[] text = {"§lLine1", "§lLine2", "§lLine3", "§lLine4", "§lColor","§lYOffset","§lScaled"};
        autoText(text, 0, 25, this.offsetX-4, this.offsetY, this.textColor);
        String[] text2 = {"§lImgUrl", "§lWidth", "§lHeight", "§lStartX", "§lStartY", "§lLocation"};
        autoText(text2, 0, 25, this.offsetX+150-4, this.offsetY, this.textColor);
        this.drawCenteredString(this.fontRendererObj, "§lData Config Of §n"+(this.index+1), this.offsetX+212, this.offsetY-35, this.textColor);
        drawTextFieldBackground(textFieldsLeft);
        drawTextFieldBackground(textFieldsRight);
        if (isValidHexColor(this.textFieldRGBColor.getText())) {
            this.drawCenteredString(this.fontRendererObj, "§l■", this.offsetX+90, this.offsetY+99, Integer.parseInt(this.textFieldRGBColor.getText(), 16));
        }

        //this.drawCenteredString(this.fontRendererObj, this.currentHashCode, this.offsetX+192, this.offsetY+100, this.textColor);

        hoveredTextField = null;
        for (HT_GuiTextField textField : textFieldsLeft) {
            textField.drawTextBox();
            if (isMouseOver(textField, mouseX, mouseY)) {
                hoveredTextField = textField;
            }
        }
        for (HT_GuiTextField textField : textFieldsRight) {
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
