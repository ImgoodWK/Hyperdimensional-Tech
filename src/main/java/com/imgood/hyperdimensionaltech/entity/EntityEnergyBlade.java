package com.imgood.hyperdimensionaltech.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import java.util.List;

/**
 * @program: Hyperdimensional-Tech
 * @description:
 * @author: Imgood
 * @create: 2024-08-12 16:11
 **/
public class EntityEnergyBlade extends Entity {
    private static final int MAX_LIFETIME = 40; // 2秒
    private int lifetime = 0;

    public EntityEnergyBlade(World world) {
        super(world);
        this.setSize(0.5F, 0.5F);
    }

    public EntityEnergyBlade(World world, EntityLivingBase shooter, double accelX, double accelY, double accelZ) {
        super(world);
        this.setSize(0.5F, 0.5F);
        this.setLocationAndAngles(shooter.posX, shooter.posY + shooter.getEyeHeight(), shooter.posZ, shooter.rotationYaw, shooter.rotationPitch);
        this.motionX = accelX;
        this.motionY = accelY;
        this.motionZ = accelZ;
    }

    @Override
    protected void entityInit() {}

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (lifetime++ >= MAX_LIFETIME || this.isCollided) {
            this.setDead();
            return;
        }
        this.moveEntity(this.motionX, this.motionY, this.motionZ);

        // 检测实体碰撞
        List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
        for (Entity entity : list) {
            if (entity instanceof EntityLivingBase) {
                entity.attackEntityFrom(DamageSource.magic, 5.0F);
            }
        }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound tagCompund) {}

    @Override
    protected void writeEntityToNBT(NBTTagCompound tagCompound) {}
}
