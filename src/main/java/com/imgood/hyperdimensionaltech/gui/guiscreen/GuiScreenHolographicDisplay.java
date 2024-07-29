package com.imgood.hyperdimensionaltech.gui.guiscreen;

import com.imgood.hyperdimensionaltech.tiles.rendertiles.TileHolographicDisplay;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;

/**
 * @program: Hyperdimensional-Tech
 * @description: 全息告示牌屏幕类
 * @author: Imgood
 * @create: 2024-07-30 16:46
 **/
public class GuiScreenHolographicDisplay extends GuiScreen {
    private final TileHolographicDisplay tileHolographicDisplay;
    private GuiTextField textField;
    private EntityPlayer player;
    private World world;
    private int xSize;
    private int ySize;

    public GuiScreenHolographicDisplay(EntityPlayer player, World world, TileHolographicDisplay tileEntity) {
        this.player = player;
        this.world = world;
        this.tileHolographicDisplay = tileEntity;
    }

    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        this.buttonList.clear();

        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;

        this.textField = new GuiTextField(this.fontRendererObj, k + 10, l + 20, 150, 20);
        this.textField.setMaxStringLength(100);
        this.textField.setFocused(true);
        this.textField.setText(this.tileHolographicDisplay.getText());

        this.buttonList.add(new GuiButton(0, k + 10, l + 50, 70, 20, "Save"));
        this.buttonList.add(new GuiButton(1, k + 90, l + 50, 70, 20, "Cancel"));
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) { // Save button
            this.tileHolographicDisplay.setText(this.textField.getText());
            this.world.markBlockForUpdate(this.tileHolographicDisplay.xCoord, this.tileHolographicDisplay.yCoord, this.tileHolographicDisplay.zCoord);
            this.mc.displayGuiScreen(null);
        } else if (button.id == 1) { // Cancel button
            this.mc.displayGuiScreen(null);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (this.textField.textboxKeyTyped(typedChar, keyCode)) {
            // Handle text input
        } else {
            super.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.textField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void updateScreen() {
        this.textField.updateCursorCounter();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, "Floating Text Editor", this.width / 2, 10, 16777215);
        this.textField.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
