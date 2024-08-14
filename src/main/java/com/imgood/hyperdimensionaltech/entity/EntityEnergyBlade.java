package com.imgood.hyperdimensionaltech.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.List;

public class EntityEnergyBlade extends Entity {
    private static final int MAX_LIFETIME = 40; // 2秒
    private int lifetime = 0;
    private Entity shootingEntity;
    public EntityEnergyBlade(World world) {
        super(world);
        this.setSize(0.5F, 0.5F);

    }

    public EntityEnergyBlade(World world, EntityLivingBase shooter, double accelX, double accelY, double accelZ) {
        super(world);
        this.setSize(4F, 1F);
        this.setLocationAndAngles(shooter.posX, shooter.posY + shooter.getEyeHeight(), shooter.posZ, shooter.rotationYaw, shooter.rotationPitch);
        this.motionX = accelX;
        this.motionY = accelY;
        this.motionZ = accelZ;
        this.shootingEntity = shooter;
        // 计算并设置实体的旋转，使其与运动方向一致
        updateRotation();
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

        // 更新实体旋转
        updateRotation();

        // 保存旧位置
        double oldX = this.posX;
        double oldY = this.posY;
        double oldZ = this.posZ;

        this.moveEntity(this.motionX, this.motionY, this.motionZ);

        // 计算移动向量
        double deltaX = this.posX - oldX;
        double deltaY = this.posY - oldY;
        double deltaZ = this.posZ - oldZ;

        // 创建一个沿移动方向的长方体碰撞箱
        AxisAlignedBB collisionBox = this.boundingBox.copy();
        collisionBox = collisionBox.expand(Math.abs(deltaX) + 1, Math.abs(deltaY) + 1, Math.abs(deltaZ) + 1);
        float damage = 120f;

        // 检测实体碰撞
        List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, collisionBox);
        for (Entity entity : list) {
            if (entity instanceof EntityLivingBase livingEntity && entity != this.shootingEntity) {
                float currentHealth = livingEntity.getHealth();
                if ((currentHealth - damage) <= 0) {
                    livingEntity.setHealth(0);
                } else {
                    livingEntity.setHealth(currentHealth - damage);
                }
                livingEntity.setHealth(currentHealth - damage);
                System.out.println("Entity health before: " + currentHealth + ", after: " + livingEntity.getHealth());
            }
        }
    }

    private void updateRotation() {
        // 计算yaw（水平旋转）
        float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

        // 计算pitch（垂直旋转）
        this.rotationPitch = (float)(Math.atan2(this.motionY, (double)f) * 180.0D / Math.PI);

        // 确保旋转角度在正确范围内
        while (this.rotationPitch - this.prevRotationPitch < -180.0F) {
            this.prevRotationPitch -= 360.0F;
        }

        while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
            this.prevRotationPitch += 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
            this.prevRotationYaw -= 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
            this.prevRotationYaw += 360.0F;
        }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound tagCompund) {}

    @Override
    protected void writeEntityToNBT(NBTTagCompound tagCompound) {}
}
