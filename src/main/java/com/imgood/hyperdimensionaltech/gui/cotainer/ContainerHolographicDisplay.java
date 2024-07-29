package com.imgood.hyperdimensionaltech.gui.cotainer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

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
}
