package com.imgood.hyperdimensionaltech.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntitySwordBeam extends Entity {
    private static final int MAX_DISTANCE = 16;
    private EntityLivingBase shooter;
    private int ticksAlive;
    private double startX, startY, startZ;

    public EntitySwordBeam(World world) {
        super(world);
        this.setSize(0.5F, 0.5F);
    }

    public EntitySwordBeam(World world, EntityLivingBase shooter) {
        this(world);
        this.shooter = shooter;
        this.setLocationAndAngles(shooter.posX, shooter.posY + shooter.getEyeHeight(), shooter.posZ, shooter.rotationYaw, shooter.rotationPitch);
        this.startX = this.posX;
        this.startY = this.posY;
        this.startZ = this.posZ;

        Vec3 look = shooter.getLookVec();
        double speed = 1.5; // 调整速度
        this.motionX = look.xCoord * speed;
        this.motionY = look.yCoord * speed;
        this.motionZ = look.zCoord * speed;
    }

    @Override
    protected void entityInit() {
        // 用于同步的数据观察器
        this.dataWatcher.addObject(16, (int) 0); // ticksAlive
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (!this.worldObj.isRemote) {
            ticksAlive++;
            this.dataWatcher.updateObject(16, ticksAlive);
        } else {
            ticksAlive = this.dataWatcher.getWatchableObjectInt(16);
        }

        // 移动逻辑
        this.lastTickPosX = this.posX;
        this.lastTickPosY = this.posY;
        this.lastTickPosZ = this.posZ;
        this.posX += this.motionX;
        this.posY += this.motionY;
        this.posZ += this.motionZ;
        this.setPosition(this.posX, this.posY, this.posZ);

        // 检查碰撞
        MovingObjectPosition movingobjectposition = this.worldObj.rayTraceBlocks(Vec3.createVectorHelper(this.lastTickPosX, this.lastTickPosY, this.lastTickPosZ), Vec3.createVectorHelper(this.posX, this.posY, this.posZ));

        if (movingobjectposition != null) {
            this.onImpact(movingobjectposition);
        }

        // 检查距离
        double distance = Math.sqrt(Math.pow(this.posX - this.startX, 2) + Math.pow(this.posY - this.startY, 2) + Math.pow(this.posZ - this.startZ, 2));
        if (distance > MAX_DISTANCE) {
            this.setDead();
        }

        // 添加粒子效果 (仅在客户端)
        if (this.worldObj.isRemote) {
            for (int i = 0; i < 4; ++i) {
                this.worldObj.spawnParticle("crit", this.posX + this.motionX * (double)i / 4.0D, this.posY + this.motionY * (double)i / 4.0D, this.posZ + this.motionZ * (double)i / 4.0D, -this.motionX, -this.motionY + 0.2D, -this.motionZ);
            }
        }
    }

    protected void onImpact(MovingObjectPosition mop) {
        if (!this.worldObj.isRemote) {
            if (mop.entityHit != null) {
                // 对实体造成伤害
                mop.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.shooter), 8.0F);
            }
            // 在碰撞点生成粒子效果
            this.worldObj.spawnParticle("largeexplode", this.posX, this.posY, this.posZ, 1.0D, 0.0D, 0.0D);
            this.setDead();
        }
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        ticksAlive = compound.getInteger("TicksAlive");
        startX = compound.getDouble("StartX");
        startY = compound.getDouble("StartY");
        startZ = compound.getDouble("StartZ");
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        compound.setInteger("TicksAlive", ticksAlive);
        compound.setDouble("StartX", startX);
        compound.setDouble("StartY", startY);
        compound.setDouble("StartZ", startZ);
    }
}
