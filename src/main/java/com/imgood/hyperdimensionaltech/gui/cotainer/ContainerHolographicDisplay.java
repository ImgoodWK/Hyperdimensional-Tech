package com.imgood.hyperdimensionaltech.gui.cotainer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;

/**
 * @program: Hyperdimensional-Tech
 * @description: 全息告示牌的容器
 * @author: Imgood
 * @create: 2024-07-30 16:17
 **/
public class ContainerHolographicDisplay extends Container {
    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }
    private final World world;
    private final int x, y, z;

    public ContainerHolographicDisplay(InventoryPlayer playerInventory, World world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
