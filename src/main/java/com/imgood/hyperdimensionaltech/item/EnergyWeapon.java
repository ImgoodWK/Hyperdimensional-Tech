package com.imgood.hyperdimensionaltech.item;

import com.imgood.hyperdimensionaltech.HyperdimensionalTech;
import com.imgood.hyperdimensionaltech.entity.EntityEnergyBlade;
import com.imgood.hyperdimensionaltech.network.EnergyUpdatePacket;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.entity.Entity;

public class EnergyWeapon extends Item {
    public static final int MAX_ENERGY = 100;
    private int energy = 25;

    public EnergyWeapon() {
        super();
        this.setUnlocalizedName("energyWeapon");
        this.setMaxStackSize(1);
        this.setCreativeTab(CreativeTabs.tabCombat);
    }

    public void addEnergy(int amount) {
        this.energy = Math.min(MAX_ENERGY, this.energy + amount);
    }

    public int getEnergy() {
        return this.energy;
    }

    public void useEnergy(int amount) {
        this.energy = Math.max(0, this.energy - amount);
    }


    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
        if (energy <= MAX_ENERGY) {
            System.out.println("testmsguseitem");
            playerIn.setItemInUse(itemStackIn, this.getMaxItemUseDuration(itemStackIn));
        }
        if (energy <= MAX_ENERGY) {
            // 释放剑气
            System.out.println("testmsgstopuse");
            Vec3 look = playerIn.getLookVec();
            EntityEnergyBlade energyBlade = new EntityEnergyBlade(worldIn, playerIn, look.xCoord, look.yCoord, look.zCoord);
            worldIn.spawnEntityInWorld(energyBlade);

            // 向前冲锋
            playerIn.addVelocity(look.xCoord * 0.5, 0.2, look.zCoord * 0.5);

            useEnergy(MAX_ENERGY);
        }
        return itemStackIn;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityPlayer playerIn, int timeLeft) {
        System.out.println("testmsgstopuse");
        if (energy <= MAX_ENERGY) {
            // 释放剑气
            System.out.println("testmsgstopuse");
            Vec3 look = playerIn.getLookVec();
            EntityEnergyBlade energyBlade = new EntityEnergyBlade(worldIn, playerIn, look.xCoord, look.yCoord, look.zCoord);
            worldIn.spawnEntityInWorld(energyBlade);

            // 向前冲锋
            playerIn.addVelocity(look.xCoord * 0.5, 0.2, look.zCoord * 0.5);

            useEnergy(MAX_ENERGY);
        }
    }
    public void setEnergy(int energy) {
        this.energy = energy;
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (!world.isRemote && isSelected && entity instanceof EntityPlayer) {
            addEnergy(1); // 每tick增加1点能量
            if (world.getTotalWorldTime() % 20 == 0) { // 每秒同步一次
                HyperdimensionalTech.network.sendTo(new EnergyUpdatePacket(energy), (EntityPlayerMP)entity);
                System.out.println("testmsgsend"+energy);
            }
        }
    }
}
