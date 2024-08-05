package com.imgood.hyperdimensionaltech.gui.guiscreen;

import com.imgood.hyperdimensionaltech.HyperdimensionalTech;
import com.imgood.hyperdimensionaltech.tiles.rendertiles.TileHolographicDisplay;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import org.lwjgl.opengl.GL11;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class GuiScreenHolographicDisplay_Main extends GuiScreen {

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
    private int displayDataSize;
    private boolean isInitialized = false;

    private static String IMAGE_URL = "";
    private static final int MAX_DISPLAY_SIZE = 100; // 最大显示尺寸

    private ResourceLocation textureResourceLocation;
    private boolean isLoading = false;
    private BufferedImage loadedImage;
    private boolean needsTextureUpdate = false;
    private int imageWidth, imageHeight;
    private int displayWidth, displayHeight;
    int buffButtonId = -1;
    private static final String CACHE_DIR = "cache/holographic_images/";
    private String currentImageHash;

    private void loadImageAsync() {
        if (IMAGE_URL == null || IMAGE_URL.isEmpty()) {
            return;
        }

        isLoading = true;
        new Thread(() -> {
            try {
                File cacheDir = new File(Minecraft.getMinecraft().mcDataDir, CACHE_DIR);

                if (!cacheDir.exists()) {
                    cacheDir.mkdirs();
                }

                String imageHash = calculateImageHash(IMAGE_URL);
                File cachedFile = new File(cacheDir, imageHash + ".png");
                HyperdimensionalTech.logger.warn("testmsg86 "+imageHash+" "+cacheDir.getPath());
                if (!cachedFile.exists()) {
                    // 下载并保存图片
                    downloadAndSaveImage(IMAGE_URL, cachedFile);
                }

                // 加载并显示图片
                loadAndDisplayImage(cachedFile);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                isLoading = false;
            }
        }).start();
    }

    private String calculateImageHash(String imageUrl) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = md.digest(imageUrl.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    private void downloadAndSaveImage(String imageUrl, File outputFile) throws IOException {
        URL url = new URL(imageUrl);
        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        FileOutputStream fos = new FileOutputStream(outputFile);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
        rbc.close();
    }

    private void loadAndDisplayImage(File imageFile) {
        try {
            BufferedImage image = ImageIO.read(imageFile);
            DynamicTexture texture = new DynamicTexture(image);
            textureResourceLocation = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("holographic_image", texture);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void calculateDisplaySize() {
        if (imageWidth <= MAX_DISPLAY_SIZE && imageHeight <= MAX_DISPLAY_SIZE) {
            displayWidth = imageWidth;
            displayHeight = imageHeight;
        } else {
            float aspectRatio = (float) imageWidth / imageHeight;
            if (imageWidth > imageHeight) {
                displayWidth = MAX_DISPLAY_SIZE;
                displayHeight = (int) (MAX_DISPLAY_SIZE / aspectRatio);
            } else {
                displayHeight = MAX_DISPLAY_SIZE;
                displayWidth = (int) (MAX_DISPLAY_SIZE * aspectRatio);
            }
        }
    }


    private int[] calculateDimensions(int imgWidth, int imgHeight, int maxWidth, int maxHeight) {
        float aspectRatio = (float) imgWidth / imgHeight;
        int drawWidth = imgWidth;
        int drawHeight = imgHeight;

        if (drawWidth > maxWidth) {
            drawWidth = maxWidth;
            drawHeight = (int) (drawWidth / aspectRatio);
        }

        if (drawHeight > maxHeight) {
            drawHeight = maxHeight;
            drawWidth = (int) (drawHeight * aspectRatio);
        }

        return new int[]{drawWidth, drawHeight};
    }



    public GuiScreenHolographicDisplay_Main(EntityPlayer player, World world, TileHolographicDisplay tileEntity) {
        this.player = player;
        this.world = world;
        this.tileHolographicDisplay = tileEntity;
        this.facing = tileEntity.facing;
        this.displayDataSize = tileHolographicDisplay.getDisplayDataSize();
        this.loadDataFromTileEntity();
        FMLCommonHandler.instance().bus().register(this);
        //loadImageAsync();
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && needsTextureUpdate && loadedImage != null) {
            if (textureResourceLocation != null) {
                Minecraft.getMinecraft().getTextureManager().deleteTexture(textureResourceLocation);
            }
            DynamicTexture dynamicTexture = new DynamicTexture(loadedImage);
            textureResourceLocation = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("dynamic_image", dynamicTexture);
            needsTextureUpdate = false;
            loadedImage = null; // 清理内存
        }
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

        //region 方向选择按钮
        this.buttonList.add(new GuiButton(100,
            this.offsetX + 0,
            this.offsetY + 130,
            40,
            20,
            "North"));
        this.buttonList.add(new GuiButton(101,
            this.offsetX + 40,
            this.offsetY + 130,
            40,
            20,
            "East"));
        this.buttonList.add(new GuiButton(102,
            this.offsetX + 80,
            this.offsetY + 130,
            40,
            20,
            "West"));
        this.buttonList.add(new GuiButton(103,
            this.offsetX + 120,
            this.offsetY + 130,
            40,
            20,
            "South"));
        //endregion
        // Add "New" button
        this.buttonList.add(new GuiButton(105,
            this.offsetX + 0,
            this.offsetY + 110,
            80, 20, "Add"));

        this.buttonList.add(new GuiButton(104,
            this.offsetX + 80,
            this.offsetY + 110,
            40, 20, "Hide"));

        this.buttonList.add(new GuiButton(106,
            this.offsetX + 120,
            this.offsetY + 110,
            40, 20, "Hide"));

        // Add buttons for existing data
        addButtonsForExistingData(this.displayDataSize,
            20, 20, 8,
            this.offsetX, this.offsetY);
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
                refreshButtons();
            }
            case 101 -> {
                facing = new NBTTagCompound();
                facing.setInteger("Rotation", 3);
                this.tileHolographicDisplay.setFacing(3);
                this.currentFacing = "East";
                refreshButtons();
            }
            case 102 -> {
                facing = new NBTTagCompound();
                facing.setInteger("Rotation", 1);
                this.tileHolographicDisplay.setFacing(1);
                this.currentFacing = "West";
                refreshButtons();
            }
            case 103 -> {
                this.tileHolographicDisplay.setFacing(0);
                this.currentFacing = "South";
                refreshButtons();
            }
            case 105 -> {
                if (this.displayDataSize <= 31) {
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
            default -> {
                if (button.id <= 32) {
                    openSubMenu(button.id);
                }
            }
        }
    }

    public void addButtonsForExistingData(int displayDataSize, int buttonWidth, int buttonHeight, int maxButtonsPerRow, int offsetX, int offsetY) {
        for (int i = 0; i < displayDataSize; i++) {
            int x = offsetX + (i % maxButtonsPerRow) * buttonWidth;
            int y = offsetY + (i / maxButtonsPerRow) * buttonHeight;
            this.buttonList.add(new GuiButton(i, x, y, buttonWidth, buttonHeight, String.valueOf(i + 1)));
        }
    }
    private void openSubMenu() {
        mc.displayGuiScreen(new GuiScreenHolographicDisplay(this.player, this.world, this.tileHolographicDisplay, this.displayDataSize));
    }

    private void openSubMenu(int index) {
        mc.displayGuiScreen(new GuiScreenHolographicDisplay(this.player, this.world, this.tileHolographicDisplay, index));
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

        // 绑定边框材质
        //this.mc.getTextureManager().bindTexture(new ResourceLocation("modid", "textures/gui/tooltipsRect.png"));

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

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRendererObj, "§lHolographic Display"+(this.index+1), this.offsetX+192, this.offsetY-20, this.textColor);

        int x = this.offsetX+200;
        int y = this.offsetY+80;

        for (Object buttonObj : this.buttonList) {
            GuiButton button = (GuiButton) buttonObj;
                if (textureResourceLocation != null) {
                    Minecraft.getMinecraft().getTextureManager().deleteTexture(textureResourceLocation);
                }
                if (button.id < this.displayDataSize && isMouseOverButton(button, mouseX, mouseY)) {
                    List<String> tooltipData = this.tileHolographicDisplay.getDisplayDataToShow(button.id);
                    drawColoredHoveringText(tooltipData, mouseX, mouseY, button.id);
                    this.IMAGE_URL = this.tileHolographicDisplay.getImgURL(button.id);
                    if (textureResourceLocation != null && !isLoading) {
                        Minecraft.getMinecraft().getTextureManager().bindTexture(textureResourceLocation);


                        GL11.glPushMatrix();
                        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                        GL11.glEnable(GL11.GL_BLEND);
                        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

                        Tessellator tessellator = Tessellator.instance;
                        tessellator.startDrawingQuads();
                        tessellator.addVertexWithUV(x, y + displayHeight, 0, 0, 1);
                        tessellator.addVertexWithUV(x + displayWidth, y + displayHeight, 0, 1, 1);
                        tessellator.addVertexWithUV(x + displayWidth, y, 0, 1, 0);
                        tessellator.addVertexWithUV(x, y, 0, 0, 0);
                        tessellator.draw();

                        GL11.glDisable(GL11.GL_BLEND);
                        GL11.glPopMatrix();
                    } else if (isLoading) {
                        drawCenteredString(Minecraft.getMinecraft().fontRenderer, "Loading...", x, y+20, 0xFFFFFF);
                    }

                break;
            }
        }

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
            tooltipTextWidth += 10;
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
                    this.fontRendererObj.drawStringWithShadow(buff[0] + ": ", tooltipX, tooltipY, Integer.parseInt("00ffff", 16));
                    if (!buff[1].isEmpty()) {
                        this.fontRendererObj.drawStringWithShadow(buff[1], tooltipX + this.fontRendererObj.getStringWidth(buff[0] + ": "), tooltipY, Integer.parseInt(this.tileHolographicDisplay.getRGBColor(buttonId), 16));
                    }
                } else {
                    this.fontRendererObj.drawStringWithShadow( buff[0] + ": ", tooltipX, tooltipY, Integer.parseInt("00ffff", 16));
                    if (!buff[1].isEmpty()) {
                        this.fontRendererObj.drawStringWithShadow(buff[1], tooltipX + this.fontRendererObj.getStringWidth(buff[0] + ": "), tooltipY, Integer.parseInt("00ffff", 16));
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

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        FMLCommonHandler.instance().bus().unregister(this);
        if (textureResourceLocation != null) {
            Minecraft.getMinecraft().getTextureManager().deleteTexture(textureResourceLocation);
        }
    }
}
