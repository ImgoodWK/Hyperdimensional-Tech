package com.imgood.hyperdimensionaltech.entity;

import com.imgood.hyperdimensionaltech.HyperdimensionalTech;
import com.imgood.hyperdimensionaltech.network.EnergyBladeHitPacket;
import cpw.mods.fml.common.registry.IEntityAdditionalSpawnData;
import cpw.mods.fml.common.registry.IThrowableEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.List;

public class EntityEnergyBlade extends Entity implements IThrowableEntity {
    private static final int MAX_LIFETIME = 40; // 2秒
    private int lifetime = 0;
    private EntityLivingBase shootingEntity;

    public EntityEnergyBlade(World world) {
        super(world);
        this.setSize(4F, 1F);
    }

    public EntityEnergyBlade(World world, EntityLivingBase shooter, double accelX, double accelY, double accelZ) {
        super(world);
        this.setSize(4F, 1F);
        this.setLocationAndAngles(shooter.posX, shooter.posY + shooter.getEyeHeight(), shooter.posZ, shooter.rotationYaw, shooter.rotationPitch);
        this.motionX = accelX;
        this.motionY = accelY;
        this.motionZ = accelZ;
        this.shootingEntity = shooter;
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

        updateRotation();

        this.moveEntity(this.motionX, this.motionY, this.motionZ);

        if (this.worldObj.isRemote) {  // 在客户端处理碰撞检测并发送网络包
            AxisAlignedBB collisionBox = this.boundingBox.expand(0.5, 0.5, 0.5);

            List<Entity> list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, collisionBox);
            for (Entity entity : list) {
                if (entity instanceof EntityLivingBase && entity != this.shootingEntity) {
                    EntityLivingBase livingEntity = (EntityLivingBase) entity;
                    sendDamagePacket(livingEntity);
                }
            }
        }
    }

    private void sendDamagePacket(EntityLivingBase entity) {
        float damage = 20.0F;  // 调整这个值来设置你想要的伤害量
        EnergyBladeHitPacket packet = new EnergyBladeHitPacket(entity.getEntityId(), this.shootingEntity.getEntityId(), damage, true);
        HyperdimensionalTech.network.sendToServer(packet);  // 发送到服务器
    }

    private void damageEntity(EntityLivingBase entity) {
        if (this.worldObj.isRemote) {
            float damage = 20.0F;  // 调整这个值来设置你想要的伤害量
            EnergyBladeHitPacket packet = new EnergyBladeHitPacket(entity.getEntityId(), this.shootingEntity.getEntityId(), damage, true);
            HyperdimensionalTech.network.sendToAll(packet);
            System.out.println("EntityEnergyBlade damageEntity: " + entity.getEntityId());
        }
    }



    private void updateRotation() {
        float f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
        this.rotationPitch = (float)(Math.atan2(this.motionY, (double)f) * 180.0D / Math.PI);

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

    @Override
    public Entity getThrower() {
        return this.shootingEntity;
    }

    @Override
    public void setThrower(Entity entity) {
        if (entity instanceof EntityLivingBase) {
            this.shootingEntity = (EntityLivingBase)entity;
        }
    }
}
