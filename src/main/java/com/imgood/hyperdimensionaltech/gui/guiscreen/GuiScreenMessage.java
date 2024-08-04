package com.imgood.hyperdimensionaltech.gui.guiscreen;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class GuiScreenMessage extends GuiScreen {

    public enum MessageType {
        INFO, WARNING, CUSTOM
    }

    private final MessageType messageType;
    private final String message;
    private final GuiScreen previousScreen;

    public GuiScreenMessage(EntityPlayer player, World world, MessageType messageType, String message, GuiScreen previousScreen) {
        this.messageType = messageType;
        this.message = message;
        this.previousScreen = previousScreen;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.clear();

        int offsetX = (this.width / 2) - 100;
        int offsetY = (this.height / 2) + 20;

        switch (messageType) {
            case INFO -> {
                this.buttonList.add(new GuiButton(0, offsetX, offsetY, 200, 20, "OK"));
            }
            case WARNING -> {
                this.buttonList.add(new GuiButton(1, offsetX, offsetY, 200, 20, "Confirm"));
            }
            case CUSTOM -> {
                this.buttonList.add(new GuiButton(2, offsetX, offsetY, 200, 20, "Proceed"));
                this.buttonList.add(new GuiButton(3, offsetX, offsetY + 25, 200, 20, "Cancel"));
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case 0, 1, 2, 3 -> mc.displayGuiScreen(previousScreen);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);

        String title = switch (messageType) {
            case INFO -> "Information";
            case WARNING -> "Warning";
            case CUSTOM -> "Custom Message";
        };

        this.drawCenteredString(this.fontRendererObj, title, this.width / 2, (this.height / 2) - 40, 0xFFFFFF);
        this.drawCenteredString(this.fontRendererObj, message, this.width / 2, (this.height / 2) - 20, 0xFFFFFF);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
