package com.imgood.hyperdimensionaltech.item;

import com.imgood.hyperdimensionaltech.HyperdimensionalTech;
import com.imgood.hyperdimensionaltech.entity.EntityEnergyBlade;
import com.imgood.hyperdimensionaltech.network.EnergyUpdatePacket;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;


/**
 * @author Imgood
 */
public class EnergyWeapon extends Item implements IAnimatable {
    public static final int MAX_ENERGY = 100;
    private int energy = 25;
    private long lastRightClickTime = 0;

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

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public void useEnergy(int amount) {
        this.energy -= amount;
        System.out.println("Energy: " + amount);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
        // 获取当前系统时间
        long currentTime = System.currentTimeMillis();

        // 判断是否冷却时间已过（0.1秒）
        if (currentTime - lastRightClickTime >= 1000) {
            // 更新上次右键时间
            lastRightClickTime = currentTime;
            Vec3 look = playerIn.getLookVec();
            // 确保只在服务器端进行处理
            if (energy <= MAX_ENERGY && energy >= 25) {
               // if (worldIn.isRemote) {
                    playerIn.addVelocity(look.xCoord * 5.5, look.yCoord * 5.5, look.zCoord * 5.5);
                    useEnergy(25);
              //  }
                EntityEnergyBlade energyBlade = new EntityEnergyBlade(worldIn, playerIn, look.xCoord * 5.5, look.yCoord * 5.5, look.zCoord * 5.5);

                worldIn.spawnEntityInWorld(energyBlade);
            }
        }

        return itemStackIn;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityPlayer playerIn, int timeLeft) {
        // 可以根据需要在这里添加逻辑
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (!world.isRemote && isSelected && entity instanceof EntityPlayer) {
            addEnergy(1);
            if (world.getTotalWorldTime() % 20 == 0) {
                HyperdimensionalTech.network.sendTo(new EnergyUpdatePacket(energy), (EntityPlayerMP) entity);
            }
        }
    }

    @Override
    public void registerControllers(AnimationData animationData) {

    }

    @Override
    public AnimationFactory getFactory() {
        return null;
    }
}
