package com.imgood.hyperdimensionaltech.gui.guihandler;

import com.imgood.hyperdimensionaltech.gui.cotainer.ContainerHolographicDisplay;
import com.imgood.hyperdimensionaltech.gui.guiscreen.GuiScreenHolographicDisplay;
import com.imgood.hyperdimensionaltech.tiles.rendertiles.TileHolographicDisplay;
import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GuiHandlerHolographicDisplay implements IGuiHandler {

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        /*TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof TileHolographicDisplay) {
            if (ID == 0) {
                return new ContainerHolographicDisplay(player.inventory, world, x, y, z);
            }
        }*/
        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof TileHolographicDisplay) {
            return new GuiScreenHolographicDisplay(player, world, (TileHolographicDisplay) tileEntity);
        }
        return null;
    }
}
