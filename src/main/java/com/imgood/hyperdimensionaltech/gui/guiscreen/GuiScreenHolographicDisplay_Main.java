package com.imgood.hyperdimensionaltech.gui.guiscreen;

import com.imgood.hyperdimensionaltech.tiles.rendertiles.TileHolographicDisplay;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class GuiScreenHolographicDisplay_Main extends GuiScreen {

    private final TileHolographicDisplay tileHolographicDisplay;
    @SuppressWarnings("unused")
    private EntityPlayer player;
    private World world;

    private int index = 0;
    private List<GuiTextField> textFieldsLeft = new ArrayList<>();
    private List<GuiTextField> textFieldsRight = new ArrayList<>();
    private List<NBTTagCompound> dataList = new ArrayList<>();
    private GuiTextField hoveredTextField;

    private int facing;
    private String currentFacing;

    private int offsetX = 100;
    private int offsetY = 100;
    //private int interval = 30;
    private int textColor = 0x00FFFF;
    private int displayDataSize;

    private boolean isInitialized = false;

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

        //region 方向选择按钮
        this.buttonList.add(new GuiButton(100,
            this.offsetX + 0,
            this.offsetY + 130,
            40,
            20,
            "North"));
        this.buttonList.add(new GuiButton(101,
            this.offsetX + 35,
            this.offsetY + 130,
            40,
            20,
            "East"));
        this.buttonList.add(new GuiButton(102,
            this.offsetX + 70,
            this.offsetY + 130,
            40,
            20,
            "West"));
        this.buttonList.add(new GuiButton(103,
            this.offsetX + 105,
            this.offsetY + 130,
            40,
            20,
            "South"));
        //endregion
        // Add "New" button
        this.buttonList.add(new GuiButton(105,
            this.offsetX + 0,
            this.offsetY + 110,
            75, 20, "Add"));

        this.buttonList.add(new GuiButton(104,
            this.offsetX + 70,
            this.offsetY + 110,
            40, 20, "Hide"));

        this.buttonList.add(new GuiButton(106,
            this.offsetX + 105,
            this.offsetY + 110,
            40, 20, "Hide"));

        // Add buttons for existing data
        for (int i = 0; i < this.displayDataSize; i++) {
            this.buttonList.add(new GuiButton(i, 20 * i,
                0, 20, 20, String.valueOf(i + 1)));
        }
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
                openSubMenu();
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
                openSubMenu(button.id);
            }
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

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.drawCenteredString(this.fontRendererObj, "Main Menu", this.width / 2, 6, 0xFFFFFF);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
