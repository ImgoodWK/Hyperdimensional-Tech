package com.imgood.hyperdimensionaltech.gui.guiscreen;

import com.imgood.hyperdimensionaltech.tiles.rendertiles.TileHolographicDisplay;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: Hyperdimensional-Tech
 * @description: 全息告示牌屏幕类
 * @author: Imgood
 * @create: 2024-07-30 16:46
 **/
public class GuiScreenHolographicDisplay extends GuiScreen {

    private final TileHolographicDisplay tileHolographicDisplay;
    @SuppressWarnings("unused")
    private EntityPlayer player;
    private World world;

    private List<GuiTextField> textFieldsLeft = new ArrayList<>();
    private List<GuiTextField> textFieldsRight = new ArrayList<>();
    private GuiTextField hoveredTextField;
    private GuiTextField textFieldLine1;
    private GuiTextField textFieldLine2;
    private GuiTextField textFieldLine3;
    private GuiTextField textFieldLine4;
    private GuiTextField textFieldRGBColor;
    private GuiTextField textFieldImgUrl;
    private GuiTextField textFieldImgScaledX;
    private GuiTextField textFieldImgScaledY;
    private GuiTextField textFieldImgStartX;
    private GuiTextField textFieldImgStartY;
    private List<String> contents = new ArrayList<>();
    private int facing;
    private String currentFacing;

    private int offsetX = 100;
    private int offsetY = 100;
    //private int interval = 30;
    private int textColor = 0x00FFFF;

    private boolean isInitialized = false;

    public GuiScreenHolographicDisplay(EntityPlayer player, World world, TileHolographicDisplay tileEntity) {
        this.player = player;
        this.world = world;
        this.tileHolographicDisplay = tileEntity;
        this.facing = tileEntity.facing;
    }

    private void saveCurrentState() {
        this.contents.clear();
        this.contents.add(textFieldLine1.getText());
        this.contents.add(textFieldLine2.getText());
        this.contents.add(textFieldLine3.getText());
        this.contents.add(textFieldLine4.getText());
        this.contents.add(textFieldRGBColor.getText());
        this.contents.add(textFieldImgUrl.getText());
        this.contents.add(textFieldImgScaledX.getText());
        this.contents.add(textFieldImgScaledY.getText());
        this.contents.add(textFieldImgStartX.getText());
        this.contents.add(textFieldImgStartY.getText());
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);

        if (isInitialized) {
            saveCurrentState();
        } else {
            this.contents.clear();
            this.contents.add(this.tileHolographicDisplay.getText(0));
            this.contents.add(this.tileHolographicDisplay.getText(1));
            this.contents.add(this.tileHolographicDisplay.getText(2));
            this.contents.add(this.tileHolographicDisplay.getText(3));
            this.contents.add(this.tileHolographicDisplay.RGBColor);
            this.contents.add(this.tileHolographicDisplay.getImgURL());
            this.contents.add(this.tileHolographicDisplay.getImgScaledX() + "");
            this.contents.add(this.tileHolographicDisplay.getImgScaledY() + "");
            this.contents.add(this.tileHolographicDisplay.getImgStartX() + "");
            this.contents.add(this.tileHolographicDisplay.getImgStartY() + "");
            isInitialized = true;
        }

        this.currentFacing = switch (this.tileHolographicDisplay.facing) {
            case 0 -> "South";
            case 1 -> "West";
            case 2 -> "North";
            default -> "East";
        };

        this.offsetX = (this.width / 2) - 192;
        this.offsetY = (this.height / 2) - 105;
        this.updateScreen();
        this.buttonList.clear();

        this.textFieldsLeft.clear();
        textFieldsLeft.add(this.textFieldLine1);
        textFieldsLeft.add(this.textFieldLine2);
        textFieldsLeft.add(this.textFieldLine3);
        textFieldsLeft.add(this.textFieldLine4);
        textFieldsLeft.add(this.textFieldRGBColor);
        autoTextField("Left",this.textFieldsLeft, 0, 20,this.offsetX+50, this.offsetY, 87, 20);

        this.textFieldsRight.clear();
        textFieldsRight.add(this.textFieldImgUrl);
        textFieldsRight.add(this.textFieldImgScaledX);
        textFieldsRight.add(this.textFieldImgScaledY);
        textFieldsRight.add(this.textFieldImgStartX);
        textFieldsRight.add(this.textFieldImgStartY);
        autoTextField("Right",this.textFieldsRight, 0, 20,this.offsetX+190, this.offsetY, 87, 20);

        this.textFieldLine1.setMaxStringLength(100);
        this.textFieldLine1.setFocused(true);
        this.textFieldLine1.setText(this.contents.get(0));
        this.textFieldLine2.setMaxStringLength(100);
        this.textFieldLine2.setText(this.contents.get(1));
        this.textFieldLine3.setMaxStringLength(100);
        this.textFieldLine3.setText(this.contents.get(2));
        this.textFieldLine4.setMaxStringLength(100);
        this.textFieldLine4.setText(this.contents.get(3));
        this.textFieldRGBColor.setMaxStringLength(6);
        this.textFieldRGBColor.setText(this.contents.get(4));

        this.textFieldImgUrl.setMaxStringLength(100);
        this.textFieldImgUrl.setText(this.contents.get(5));
        this.textFieldImgScaledX.setMaxStringLength(5);
        this.textFieldImgScaledX.setText(this.contents.get(6));
        this.textFieldImgScaledY.setMaxStringLength(5);
        this.textFieldImgScaledY.setText(this.contents.get(7));
        this.textFieldImgStartX.setMaxStringLength(5);
        this.textFieldImgStartX.setText(this.contents.get(8));
        this.textFieldImgStartY.setMaxStringLength(5);
        this.textFieldImgStartY.setText(this.contents.get(9));

        //region 保存/取消按钮
        this.buttonList.add(new GuiButton(0,
            this.offsetX + 0,
            this.offsetY + 110,
            70,
            20,
            "Save"));
        this.buttonList.add(new GuiButton(1,
            this.offsetX + 69,
            this.offsetY + 110,
            70,
            20,
            "Cancel"));
        //endregion

        //region 方向选择按钮
        this.buttonList.add(new GuiButton(2,
            this.offsetX + 0,
            this.offsetY + 130,
            35,
            20,
            "North"));
        this.buttonList.add(new GuiButton(3,
            this.offsetX + 35,
            this.offsetY + 130,
            35,
            20,
            "East"));
        this.buttonList.add(new GuiButton(4,
            this.offsetX + 70,
            this.offsetY + 130,
            35,
            20,
            "West"));
        this.buttonList.add(new GuiButton(5,
            this.offsetX + 105,
            this.offsetY + 130,
            35,
            20,
            "South"));
        //endregion
        this.refreshButtons();
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
            textFields.set(i, textField); // 更新列表中的元素
            intervalXCurrent += intervalX;
            intervalYCurrent += intervalY;

            if (row.equals("Left")) {
                switch (i) {
                    case 0: this.textFieldLine1 = textField; break;
                    case 1: this.textFieldLine2 = textField; break;
                    case 2: this.textFieldLine3 = textField; break;
                    case 3: this.textFieldLine4 = textField; break;
                    case 4: this.textFieldRGBColor = textField; break;
                }
            } else if (row.equals("Right")) {
                switch (i) {
                    case 0: this.textFieldImgUrl = textField; break;
                    case 1: this.textFieldImgScaledX = textField; break;
                    case 2: this.textFieldImgScaledY = textField; break;
                    case 3: this.textFieldImgStartX = textField; break;
                    case 4: this.textFieldImgStartY = textField; break;
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

    public void refreshButtons() {
        for (GuiButton guiButton : this.buttonList ) {
            if (guiButton.displayString.equals(currentFacing)) {
                guiButton.enabled = false;
            } else {
                guiButton.enabled = true;
            }
        }
    }

    /**
     * 检查字符串是否是有效的 6 位十六进制颜色代码
     * @param color 字符串表示的颜色代码
     * @return 如果字符串是有效的十六进制颜色代码，则返回 true，否则返回 false
     */
    private boolean isValidHexColor(String color) {
        return color != null && color.matches("^[0-9A-Fa-f]{6}$");
    }

    public static boolean isValidInteger(String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isValidDouble(String text) {
        try {
            Double.parseDouble(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isMouseOver(GuiTextField textField, int mouseX, int mouseY) {
        return mouseX >= textField.xPosition && mouseX < textField.xPosition + textField.width &&
            mouseY >= textField.yPosition && mouseY < textField.yPosition + textField.height;
    }

    private List<String> wrapText(String text, int lineLength) {
        List<String> lines = new ArrayList<>();
        int length = text.length();
        int startIndex = 0;

        while (startIndex < length) {
            int endIndex = Math.min(startIndex + lineLength, length);

            // 如果不是最后一行，且下一个字符不是空格，则向前查找最后一个空格
            if (endIndex < length && text.charAt(endIndex) != ' ') {
                while (endIndex > startIndex && text.charAt(endIndex - 1) != ' ') {
                    endIndex--;
                }
                // 如果没有找到空格，则强制在lineLength处截断
                if (endIndex == startIndex) {
                    endIndex = Math.min(startIndex + lineLength, length);
                }
            }

            // 添加当前行到结果列表
            lines.add(text.substring(startIndex, endIndex).trim());
            startIndex = endIndex;
        }

        return lines;
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
                nbt.setInteger("Rotation", this.tileHolographicDisplay.facing);
                nbt.setString("ImgURL", this.textFieldImgUrl.getText());
//                nbt.setDouble("ImgScaledX", Double.parseDouble(this.textFieldImgScaledX.getText()));
//                nbt.setDouble("ImgScaledY", Double.parseDouble(this.textFieldImgScaledY.getText()));
//                nbt.setDouble("ImgStartX", Double.parseDouble(this.textFieldImgStartX.getText()));
//                nbt.setDouble("ImgStartY", Double.parseDouble(this.textFieldImgStartY.getText()));
                if (!isValidHexColor(this.textFieldRGBColor.getText())) {
                    // 如果 RGB 颜色不是有效的十六进制字符串，则恢复之前的状态
                    this.textFieldRGBColor.setText(this.tileHolographicDisplay.RGBColor);
                } else {
                    nbt.setString("RGBColor", this.textFieldRGBColor.getText());
                    this.tileHolographicDisplay.setRGBColor(this.textFieldRGBColor.getText());
                }
                if (!isValidDouble(this.textFieldImgStartX.getText())) {
                    this.textFieldImgStartX.setText(String.valueOf(this.tileHolographicDisplay.imgStartX));
                } else {
                    nbt.setDouble("ImgStartX", Double.parseDouble(this.textFieldImgStartX.getText()));
                    this.tileHolographicDisplay.setImgStartX(Double.parseDouble(this.textFieldImgStartX.getText()));
                }
                if (!isValidDouble(this.textFieldImgStartY.getText())) {
                    this.textFieldImgStartY.setText(String.valueOf(this.tileHolographicDisplay.imgStartY));
                } else {
                    nbt.setDouble("ImgStartY", Double.parseDouble(this.textFieldImgStartY.getText()));
                    this.tileHolographicDisplay.setImgStartY(Double.parseDouble(this.textFieldImgStartY.getText()));
                }
                if (isValidDouble(this.textFieldImgScaledX.getText())) {
                    nbt.setDouble("ImgScaledX", Double.parseDouble(this.textFieldImgScaledX.getText()));
                    this.tileHolographicDisplay.setImgScaledX(Double.parseDouble(this.textFieldImgScaledX.getText()));
                } else {
                    this.textFieldImgScaledX.setText(String.valueOf(this.tileHolographicDisplay.imgScaledX));
                }
                if (isValidDouble(this.textFieldImgScaledY.getText())) {
                    nbt.setDouble("ImgScaledY", Double.parseDouble(this.textFieldImgScaledY.getText()));
                    this.tileHolographicDisplay.setImgScaledY(Double.parseDouble(this.textFieldImgScaledY.getText()));
                } else {
                    this.textFieldImgScaledY.setText(String.valueOf(this.tileHolographicDisplay.imgScaledY));
                }
                this.tileHolographicDisplay.setText(0, this.textFieldLine1.getText());
                this.tileHolographicDisplay.setText(1, this.textFieldLine2.getText());
                this.tileHolographicDisplay.setText(2, this.textFieldLine3.getText());
                this.tileHolographicDisplay.setText(3, this.textFieldLine4.getText());
                this.tileHolographicDisplay.setImgURL(this.textFieldImgUrl.getText());
                this.tileHolographicDisplay.writeToNBT(nbt);
                isInitialized = false; // 重置初始化状态
                this.mc.displayGuiScreen(null);
            }
            case 1 -> {
                this.mc.displayGuiScreen(null);
            }

            case 2 -> {
                nbt = new NBTTagCompound();
                nbt.setInteger("Rotation", 2);
                this.tileHolographicDisplay.setFacing(2);
                this.currentFacing = "North";
                refreshButtons();
            }
            case 3 -> {
                nbt = new NBTTagCompound();
                nbt.setInteger("Rotation", 3);
                this.tileHolographicDisplay.setFacing(3);
                this.currentFacing = "East";
                refreshButtons();
            }
            case 4 -> {
                nbt = new NBTTagCompound();
                nbt.setInteger("Rotation", 1);
                this.tileHolographicDisplay.setFacing(1);
                this.currentFacing = "West";
                refreshButtons();
            }
            case 5 -> {
                nbt = new NBTTagCompound();
                nbt.setInteger("Rotation", 0);
                this.tileHolographicDisplay.setFacing(0);
                this.currentFacing = "South";
                refreshButtons();
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

        String[] text = {"Line1", "Line2", "Line3", "Line4", "RGB Color"};
        autoText(text, 0, 22, this.offsetX, this.offsetY, this.textColor);
        String[] text2 = {"ImgUrl", "ScaledX", "ScaledY", "StartX", "StartY"};
        autoText(text2, 0, 22, this.offsetX+150, this.offsetY, this.textColor);

        this.drawCenteredString(this.fontRendererObj, "Holographic Display", this.width / 2, (this.height/2)-125, this.textColor);

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
            List<String> wrappedText = wrapText(hoveredTextField.getText(), 10);
            drawHoveringText(wrappedText, mouseX, mouseY, this.fontRendererObj);
        }
    }
}
