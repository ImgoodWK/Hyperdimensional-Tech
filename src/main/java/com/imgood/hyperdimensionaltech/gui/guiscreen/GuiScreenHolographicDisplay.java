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
    private GuiTextField textFieldLine1;
    private GuiTextField textFieldLine2;
    private GuiTextField textFieldLine3;
    private GuiTextField textFieldLine4;
    private GuiTextField textFieldColorR;
    private GuiTextField textFieldColorG;
    private GuiTextField textFieldColorB;
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

        int k = 0;
        int l = 0;

        this.textFieldLine1 = new GuiTextField(this.fontRendererObj,
            k + 10,
            l + 60,
            150,
            20);
        this.textFieldLine2 = new GuiTextField(this.fontRendererObj,
            k + 10,
            l + 40,
            150,
            20);
        this.textFieldLine3 = new GuiTextField(this.fontRendererObj,
            k + 10,
            l + 20,
            150,
            20);
        this.textFieldLine4 = new GuiTextField(this.fontRendererObj,
            k + 10,
            l + 0,
            150,
            20);
        this.textFieldLine1.setMaxStringLength(100);
        this.textFieldLine1.setFocused(true);
        this.textFieldLine1.setText(this.tileHolographicDisplay.getText(0));
        this.textFieldLine2.setMaxStringLength(100);
        this.textFieldLine2.setFocused(true);
        this.textFieldLine2.setText(this.tileHolographicDisplay.getText(1));
        this.textFieldLine3.setMaxStringLength(100);
        this.textFieldLine3.setFocused(true);
        this.textFieldLine3.setText(this.tileHolographicDisplay.getText(2));
        this.textFieldLine4.setMaxStringLength(100);
        this.textFieldLine4.setFocused(true);
        this.textFieldLine4.setText(this.tileHolographicDisplay.getText(3));

        this.buttonList.add(new GuiButton(0, k + 10, l + 100, 70, 20, "Save"));
        this.buttonList.add(new GuiButton(1, k + 90, l + 100, 70, 20, "Cancel"));
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button.id == 0) {
            this.tileHolographicDisplay.setText(0, this.textFieldLine1.getText());
            this.tileHolographicDisplay.setText(1, this.textFieldLine2.getText());
            this.tileHolographicDisplay.setText(2, this.textFieldLine3.getText());
            this.tileHolographicDisplay.setText(3, this.textFieldLine4.getText());
            this.world.markBlockForUpdate(this.tileHolographicDisplay.xCoord, this.tileHolographicDisplay.yCoord, this.tileHolographicDisplay.zCoord);
            this.mc.displayGuiScreen(null);
        } else if (button.id == 1) {
            this.mc.displayGuiScreen(null);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (!this.textFieldLine1.textboxKeyTyped(typedChar, keyCode) &&
            !this.textFieldLine2.textboxKeyTyped(typedChar, keyCode) &&
            !this.textFieldLine3.textboxKeyTyped(typedChar, keyCode) &&
            !this.textFieldLine4.textboxKeyTyped(typedChar, keyCode)) {
        } else {
            super.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.textFieldLine1.mouseClicked(mouseX, mouseY, mouseButton);
        this.textFieldLine2.mouseClicked(mouseX, mouseY, mouseButton);
        this.textFieldLine3.mouseClicked(mouseX, mouseY, mouseButton);
        this.textFieldLine4.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void updateScreen() {
        this.textFieldLine1.updateCursorCounter();
        this.textFieldLine2.updateCursorCounter();
        this.textFieldLine3.updateCursorCounter();
        this.textFieldLine4.updateCursorCounter();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, "Floating Text Editor", this.width / 2, 10, 16777215);
        this.textFieldLine1.drawTextBox();
        this.textFieldLine2.drawTextBox();
        this.textFieldLine3.drawTextBox();
        this.textFieldLine4.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
