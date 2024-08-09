package com.imgood.hyperdimensionaltech.gui.guiscreen;

import com.imgood.hyperdimensionaltech.gui.costom.HT_GuiButton;
import com.imgood.hyperdimensionaltech.gui.costom.HT_GuiScreen;
import com.imgood.hyperdimensionaltech.tiles.rendertiles.TileHolographicDisplay;
import com.imgood.hyperdimensionaltech.utils.HT_TextureManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static com.imgood.hyperdimensionaltech.utils.Enums.Names.MOD_ID;
import static com.imgood.hyperdimensionaltech.utils.HT_ContentsHelper.wrapText;

public class GuiScreenHolographicDisplay_Main extends HT_GuiScreen {

    private static final String IMAGE_PATH_PREFIX = "cache/holographic_images/";
    private final TileHolographicDisplay tileHolographicDisplay;
    @SuppressWarnings("unused")
    private EntityPlayer player;
    private World world;
    private int index = 0;
    private List<GuiTextField> textFieldsLeft = new ArrayList<>();
    private List<GuiTextField> textFieldsRight = new ArrayList<>();
    private List<NBTTagCompound> dataList = new ArrayList<>();
    private List<String> tooltipLines = new ArrayList<>();
    private GuiTextField hoveredTextField;
    private int facing;
    private String currentFacing;
    private int offsetX = 100;
    private int offsetY = 100;
    private int textColor = 0x00FFFF;
    private int textHoverColor = 0x0055FF;
    private int displayDataSize;

    private static final ResourceLocation button_texture = new ResourceLocation(MOD_ID,"textures/gui/button_HolographicDisplay.png");
    private static final ResourceLocation button_hover_texture = new ResourceLocation(MOD_ID,"textures/gui/button_hover_HolographicDisplay.png");
    private static final ResourceLocation button_texture_2020 = new ResourceLocation(MOD_ID,"textures/gui/button_HolographicDisplay_2020.png");
    private static final ResourceLocation button_hover_texture_2020 = new ResourceLocation(MOD_ID,"textures/gui/button_hover_HolographicDisplay_2020.png");

    private int buttonRowYOffset1 = 100;
    private int buttonRowYOffset2 = 145;
    private boolean buttonRow1RGB = false;
    private boolean buttonRow2RGB = false;
    private int buttonRow1Width = 40;
    private int buttonRow2Width = 40;

    public GuiScreenHolographicDisplay_Main(EntityPlayer player, World world, TileHolographicDisplay tileEntity) {
        this.player = player;
        this.world = world;
        this.tileHolographicDisplay = tileEntity;
        this.facing = tileEntity.facing;
        this.displayDataSize = tileHolographicDisplay.getDisplayDataSize();
        this.loadDataFromTileEntity();
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.clear();

        this.currentFacing = switch (this.tileHolographicDisplay.facing) {
            case 0 -> "South";
            case 1 -> "West";
            case 2 -> "North";
            default -> "East";
        };

        this.offsetX = (this.width / 2) - 192;
        this.offsetY = (this.height / 2) - 90;
        this.updateScreen();
        this.buttonList.clear();
        this.setPosition(this.offsetX-20, this.offsetY-50);
        this.setSize(420, 260);
        this.setStretch(false);
        //region 方向选择按钮
        this.buttonList.add(new HT_GuiButton(100,
            this.offsetX + 0,
            this.offsetY+buttonRowYOffset1,
            buttonRow2Width,
            20,
            "North")
            .setTexture(button_texture)
            .setHoverTexture(button_hover_texture)
            .setUseRGBEffect(buttonRow1RGB)
            .setUseHoverEffect(true)
            .setLeftDecoration(button_hover_texture)
            .setRightDecoration(button_hover_texture)
            .setDecorationWidth(20)
            .setTextColor(textColor)
            .setTextHoverColor(textHoverColor));
        this.buttonList.add(new HT_GuiButton(101,
            this.offsetX + 60,
            this.offsetY+buttonRowYOffset1,
            buttonRow2Width,
            20,
            "East")
            .setTexture(button_texture)
            .setHoverTexture(button_hover_texture)
            .setUseRGBEffect(buttonRow1RGB)
            .setUseHoverEffect(true)
            .setLeftDecoration(button_hover_texture)
            .setRightDecoration(button_hover_texture)
            .setDecorationWidth(20)
            .setTextColor(textColor)
            .setTextHoverColor(textHoverColor));
        this.buttonList.add(new HT_GuiButton(102,
            this.offsetX + 120,
            this.offsetY+buttonRowYOffset1,
            buttonRow2Width,
            20,
            "West")
            .setTexture(button_texture)
            .setHoverTexture(button_hover_texture)
            .setUseRGBEffect(buttonRow1RGB)
            .setUseHoverEffect(true)
            .setLeftDecoration(button_hover_texture)
            .setRightDecoration(button_hover_texture)
            .setDecorationWidth(20)
            .setTextColor(textColor)
            .setTextHoverColor(textHoverColor));
        this.buttonList.add(new HT_GuiButton(103,
            this.offsetX + 180,
            this.offsetY+buttonRowYOffset1,
            buttonRow2Width,
            20,
            "South")
            .setTexture(button_texture)
            .setHoverTexture(button_hover_texture)
            .setUseRGBEffect(buttonRow1RGB)
            .setUseHoverEffect(true)
            .setLeftDecoration(button_hover_texture)
            .setRightDecoration(button_hover_texture)
            .setDecorationWidth(20)
            .setTextColor(textColor)
            .setTextHoverColor(textHoverColor));
        //endregion
        // Add "New" button
        this.buttonList.add(new HT_GuiButton(105,
            this.offsetX + 0,
            this.offsetY+buttonRowYOffset2,
            buttonRow1Width, 20, "Add")
            .setTexture(button_texture)
            .setHoverTexture(button_hover_texture)
            .setUseRGBEffect(buttonRow1RGB)
            .setUseHoverEffect(true)
            .setLeftDecoration(button_hover_texture)
            .setRightDecoration(button_hover_texture)
            .setDecorationWidth(20)
            .setTextColor(textColor)
            .setTextHoverColor(textHoverColor));

        this.buttonList.add(new HT_GuiButton(104,
            this.offsetX + 60,
            this.offsetY+buttonRowYOffset2,
            buttonRow1Width, 20, "Hide")
            .setTexture(button_texture)
            .setHoverTexture(button_hover_texture)
            .setUseRGBEffect(buttonRow1RGB)
            .setUseHoverEffect(true)
            .setLeftDecoration(button_hover_texture)
            .setRightDecoration(button_hover_texture)
            .setDecorationWidth(20)
            .setTextColor(textColor)
            .setTextHoverColor(textHoverColor));

        this.buttonList.add(new HT_GuiButton(106,
            this.offsetX+120,
            this.offsetY+buttonRowYOffset2,
            buttonRow1Width,  20, "Hide")
            .setTexture(button_texture)
            .setHoverTexture(button_hover_texture)
            .setUseRGBEffect(buttonRow1RGB)
            .setUseHoverEffect(true)
            .setLeftDecoration(button_hover_texture)
            .setRightDecoration(button_hover_texture)
            .setDecorationWidth(20)
            .setTextColor(textColor)
            .setTextHoverColor(textHoverColor));

        this.buttonList.add(new HT_GuiButton(107,
            this.offsetX+180,
            this.offsetY+buttonRowYOffset2,
            buttonRow1Width,  20, "Single")
            .setTexture(button_texture)
            .setHoverTexture(button_hover_texture)
            .setUseRGBEffect(buttonRow1RGB)
            .setUseHoverEffect(true)
            .setLeftDecoration(button_hover_texture)
            .setRightDecoration(button_hover_texture)
            .setDecorationWidth(20)
            .setTextColor(textColor)
            .setTextHoverColor(textHoverColor));

        // Add buttons for existing data
        addButtonsForExistingData(this.displayDataSize,
            20, 20, 12,
            this.offsetX+5, this.offsetY-10,
            10, 10);
        refreshButtons();
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        NBTTagCompound disPlayData;
        NBTTagCompound facing;
        switch (button.id) {
            case 100 -> {
                facing = new NBTTagCompound();
                facing.setInteger("Rotation", 2);
                this.tileHolographicDisplay.setFacing(2);
                this.currentFacing = "North";
                initGui();
                refreshButtons();
            }
            case 101 -> {
                facing = new NBTTagCompound();
                facing.setInteger("Rotation", 3);
                this.tileHolographicDisplay.setFacing(3);
                this.currentFacing = "East";
                initGui();
                refreshButtons();
            }
            case 102 -> {
                facing = new NBTTagCompound();
                facing.setInteger("Rotation", 1);
                this.tileHolographicDisplay.setFacing(1);
                this.currentFacing = "West";
                initGui();
                refreshButtons();
            }
            case 103 -> {
                this.tileHolographicDisplay.setFacing(0);
                this.currentFacing = "South";
                initGui();
                refreshButtons();
            }
            case 105 -> {
                if (this.displayDataSize <= 35) {
                    openSubMenu();
                } else {
                    mc.displayGuiScreen(new GuiScreenMessage(this.player, this.world, GuiScreenMessage.MessageType.WARNING, "Maximum number of data reached.", this));
                }
            }
            case 104 -> {
                boolean visableBody = button.displayString.equals("Show");
                this.tileHolographicDisplay.setVisableBody(visableBody);
                refreshButtons();
            }
            case 106 -> {
                boolean visableScreen = button.displayString.equals("Show");
                this.tileHolographicDisplay.setVisableScreen(visableScreen);
                refreshButtons();
            }
            case 107 -> {
                this.tileHolographicDisplay.setVisableBack(!this.tileHolographicDisplay.isVisableBack());
                button.displayString = this.tileHolographicDisplay.isVisableBack() ? "Single" : "Both";
                refreshButtons();
            }
            default -> {
                if (button.id <= 36) {
                    openSubMenu(button.id);
                }
            }
        }
    }

    public void addButtonsForExistingData(int displayDataSize, int buttonWidth, int buttonHeight, int maxButtonsPerRow, int offsetX, int offsetY, int xSpacing, int ySpacing) {
        for (int i = 0; i < displayDataSize; i++) {
            int x = offsetX + (i % maxButtonsPerRow) * (buttonWidth + xSpacing);
            int y = offsetY + (i / maxButtonsPerRow) * (buttonHeight + ySpacing);
            this.buttonList.add(new HT_GuiButton(i, x, y, buttonWidth, buttonHeight, String.valueOf(i + 1))
                .setTexture(button_texture_2020)
                .setHoverTexture(button_hover_texture_2020)
                .setUseRGBEffect(buttonRow1RGB)
                .setUseHoverEffect(true)
                .setLeftDecoration(button_hover_texture)
                .setRightDecoration(button_hover_texture)
                .setDecorationWidth(20)
                .setTextColor(textColor)
                .setTextHoverColor(textHoverColor));
        }
    }

    private void openSubMenu() {
        mc.displayGuiScreen(new GuiScreenHolographicDisplay_Sub(this.player, this.world, this.tileHolographicDisplay, this.displayDataSize));
    }

    private void openSubMenu(int index) {
        mc.displayGuiScreen(new GuiScreenHolographicDisplay_Sub(this.player, this.world, this.tileHolographicDisplay, index));
    }

    private void openSubMenu(NBTTagCompound data) {
        // Open your sub-menu GUI for editing existing data
        // Example: mc.displayGuiScreen(new GuiSubMenu(this, tileEntity, data));
    }

    public void onNewDataSaved(NBTTagCompound newData) {
        this.dataList.add(newData);
        saveDataToTileEntity();
        initGui(); // Refresh the GUI to show the new button
    }

    public void onDataEdited(int index, NBTTagCompound editedData) {
        dataList.set(index, editedData);
        saveDataToTileEntity();
    }

    public void refreshButtons() {
        for (GuiButton guiButton : this.buttonList) {
            if (guiButton.displayString.equals(currentFacing)) {
                guiButton.enabled = false;
            } else {
                guiButton.enabled = true;
            }
        }
        for (GuiButton guiButton : this.buttonList) {
            switch (guiButton.id) {
                case 104 -> {
                    guiButton.displayString = this.tileHolographicDisplay.isVisableBody() ? "Hide" : "Show";
                }
                case 106 -> {
                    guiButton.displayString = this.tileHolographicDisplay.isVisableScreen() ? "Hide" : "Show";
                }
                case 107 -> {
                    guiButton.displayString = this.tileHolographicDisplay.isVisableBack() ? "Single" : "Both";
                }
            }
        }
    }

    private void loadDataFromTileEntity() {
        NBTTagCompound nbt = new NBTTagCompound();
        this.tileHolographicDisplay.writeToNBT(nbt);
        int count = nbt.getInteger("DataCount");
        for (int i = 0; i < count; i++) {
            dataList.add(nbt.getCompoundTag("Data" + i));
        }
    }

    private void saveDataToTileEntity() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("DataCount", dataList.size());
        for (int i = 0; i < dataList.size(); i++) {
            nbt.setTag("Data" + i, dataList.get(i));
        }
        this.tileHolographicDisplay.readFromNBT(nbt);
    }

    private boolean isMouseOverButton(GuiButton button, int mouseX, int mouseY) {
        return mouseX >= button.xPosition && mouseX < button.xPosition + button.width &&
            mouseY >= button.yPosition && mouseY < button.yPosition + button.height;
    }

    private void drawTooltipBackground(int x, int y, int width, int height) {
        int borderSize = 0;

        // 绑定背景材质（可选）
        //this.mc.getTextureManager().bindTexture(new ResourceLocation("modid", "textures/gui/tooltipsBackground.png"));
        // 绘制背景（使用 OpenGL 绘制一个纯色或半透明矩形）
        drawRect(x, y, x + width, y + height, 0xaf00aaaa); // 半透明黑色背景


        // 左上角
        drawTexturedModalRect(x - borderSize, y - borderSize, 0, 0, borderSize, borderSize);
        // 右上角
        drawTexturedModalRect(x + width, y - borderSize, 16 - borderSize, 0, borderSize, borderSize);
        // 左下角
        drawTexturedModalRect(x - borderSize, y + height, 0, 16 - borderSize, borderSize, borderSize);
        // 右下角
        drawTexturedModalRect(x + width, y + height, 16 - borderSize, 16 - borderSize, borderSize, borderSize);

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRendererObj, "§lHolographic Display", this.offsetX + 192, this.offsetY - 40, this.textColor);
        this.drawCenteredString(this.fontRendererObj, "Facing", this.offsetX + 110, this.offsetY +85, this.textColor);
        this.drawCenteredString(this.fontRendererObj, "Data", this.offsetX + 20, this.offsetY +130, this.textColor);
        this.drawCenteredString(this.fontRendererObj, "Body", this.offsetX + 85, this.offsetY +130, this.textColor);
        this.drawCenteredString(this.fontRendererObj, "Screen", this.offsetX + 135, this.offsetY +130, this.textColor);
        this.drawCenteredString(this.fontRendererObj, "Back", this.offsetX + 200, this.offsetY +130, this.textColor);
        for (Object buttonObj : this.buttonList) {
            GuiButton button = (GuiButton) buttonObj;
            if (button.id < this.displayDataSize && isMouseOverButton(button, mouseX, mouseY)) {
                List<String> tooltipData = this.tileHolographicDisplay.getDisplayDataToShow(button.id);
                drawColoredHoveringText(tooltipData, mouseX, mouseY, button.id);
                try {
                    this.drawImage(HT_TextureManager.getOrLoadTexture(this.tileHolographicDisplay.getImgPath(button.id), this.tileHolographicDisplay , button.id),
                        this.offsetX + 250,
                        this.offsetY +95,
                        80,
                        80);
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
        }

        // 获取鼠标位置并显示在左下角
        String mousePos = String.format("Mouse Position: %d, %d", mouseX, mouseY);
        int x = 10; // X坐标，距离屏幕左边的距离
        int y = this.height - 20; // Y坐标，距离屏幕底部的距离
        this.fontRendererObj.drawString(mousePos, x, y,0x00ffff);
    }


    private void drawColoredHoveringText(List<String> textLines, int x, int y, int buttonId) {
        if (textLines == null || textLines.isEmpty()) return;

        int tooltipTextWidth = 0;
        int tooltipHeight = 8; // 初始高度
        int tooltipX = x + 12;
        int tooltipY = y - 12;
        int maxLineWidth = 200; // 设置最大行宽

        // 计算提示框的尺寸
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

            int prefixWidth = this.fontRendererObj.getStringWidth(buff[0] + ": ");

            if (line.contains("Color:") || line.contains("Text1:") || line.contains("Text2:") || line.contains("Text3:") || line.contains("Text4:")||line.contains("ImgURL:")) {
                if (!buff[1].isEmpty()) {
                    List<String> wrappedText = wrapText(buff[1], 12);
                    for (int i = 0; i < wrappedText.size(); i++) {
                        int wrappedLineWidth = prefixWidth + this.fontRendererObj.getStringWidth(wrappedText.get(i));
                        tooltipTextWidth = Math.max(tooltipTextWidth, Math.min(wrappedLineWidth, maxLineWidth));
                        if (i < wrappedText.size() - 1) {
                            tooltipHeight += 10;
                        }
                    }
                } else {
                    tooltipTextWidth = Math.max(tooltipTextWidth, Math.min(prefixWidth, maxLineWidth));
                }
            } else {
                int lineWidth = this.fontRendererObj.getStringWidth(line);
                tooltipTextWidth = Math.max(tooltipTextWidth, Math.min(lineWidth, maxLineWidth));
            }
            tooltipHeight += 10;
        }

        // 添加一些额外的填充
        tooltipTextWidth += 8;
        tooltipHeight += 2;

        if (tooltipX + tooltipTextWidth > this.width) {
            tooltipX -= 28 + tooltipTextWidth;
        }

        if (tooltipY + tooltipHeight + 6 > this.height) {
            tooltipY = this.height - tooltipHeight - 6;
        }

        tooltipX -= 10;
        tooltipY += 10;

        // 先绘制背景
        this.zLevel = 300.0F;
        drawTooltipBackground(tooltipX, tooltipY - 5, tooltipTextWidth, tooltipHeight);

        // 然后绘制文字
        this.zLevel = 301.0F;
        int currentY = tooltipY;
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
            for (String text : buff) {
                text.replace("§l", "");
                text.replace("§o", "");
                text.replace("§n", "");
                text.replace("§m", "");
            }
            if (line.contains("Color:") || line.contains("Text1:") || line.contains("Text2:") || line.contains("Text3:") || line.contains("Text4:")) {
                this.fontRendererObj.drawStringWithShadow(buff[0] + ": ", tooltipX, currentY, Integer.parseInt("00ffff", 16));
                if (!buff[1].isEmpty()) {
                    List<String> wrappedText = wrapText(buff[1], 12);
                    for (int i = 0; i < wrappedText.size(); i++) {
                        String text = wrappedText.get(i);
                        if (line.contains("Color:")) {
                            this.fontRendererObj.drawStringWithShadow("§n"+text, tooltipX + this.fontRendererObj.getStringWidth(buff[0] + ": "), currentY, Integer.parseInt(this.tileHolographicDisplay.getRGBColor(buttonId), 16));
                        } else {
                            this.fontRendererObj.drawStringWithShadow(text, tooltipX + this.fontRendererObj.getStringWidth(buff[0] + ": "), currentY, Integer.parseInt(this.tileHolographicDisplay.getRGBColor(buttonId), 16));
                        }
                        // 只有当不是最后一次迭代时才增加 currentY
                        if (i < wrappedText.size() - 1) {
                            currentY += 10;
                        }
                    }
                }
            } else if (line.contains("ImgURL:")) {
                this.fontRendererObj.drawStringWithShadow(buff[0] + ": ", tooltipX, currentY, Integer.parseInt("00ffff", 16));
                if (!buff[1].isEmpty()) {
                    List<String> wrappedText = wrapText(buff[1], 12);
                    for (int i = 0; i < wrappedText.size(); i++) {
                        String text = wrappedText.get(i);
                        this.fontRendererObj.drawStringWithShadow("§n"+text, tooltipX + this.fontRendererObj.getStringWidth(buff[0] + ": "), currentY, Integer.parseInt("00ffff", 16));

                        // 只有当不是最后一次迭代时才增加 currentY
                        if (i < wrappedText.size() - 1) {
                            currentY += 10;
                        }
                    }
                }
            } else if (line.contains("Style")) {
                this.fontRendererObj.drawStringWithShadow(buff[0], tooltipX+32, currentY, Integer.parseInt("00ffff", 16));
            } else {
                this.fontRendererObj.drawStringWithShadow(buff[0] + ": ", tooltipX, currentY, Integer.parseInt("00ffff", 16));
                if (!buff[1].isEmpty()) {
                    this.fontRendererObj.drawStringWithShadow("§n"+buff[1], tooltipX + this.fontRendererObj.getStringWidth(buff[0] + ": "), currentY, Integer.parseInt("00ffff", 16));
                }
            }

            if (lineNumber == 0) {
                currentY += 2;
            }

            currentY += 10;
        }

        // 恢复 zLevel
        this.zLevel = 0.0F;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

}
