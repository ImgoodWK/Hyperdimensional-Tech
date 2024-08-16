package com.imgood.hyperdimensionaltech.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

public class HitPacket implements IMessage {
    private int targetEntityId;
    private int shooterEntityId;
    private float damage;
    private boolean IsInstantKill;

    // 无参构造函数是必须的
    public HitPacket() {}

    public HitPacket(int targetEntityId, int shooterEntityId, float damage, boolean IsInstantKill) {
        this.targetEntityId = targetEntityId;
        this.shooterEntityId = shooterEntityId;
        this.damage = damage;
        this.IsInstantKill = IsInstantKill;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.targetEntityId = buf.readInt();
        this.shooterEntityId = buf.readInt();
        this.damage = buf.readFloat();
        this.IsInstantKill = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(targetEntityId);
        buf.writeInt(shooterEntityId);
        buf.writeFloat(damage);
        buf.writeBoolean(IsInstantKill);
    }

    public static class Handler implements IMessageHandler<HitPacket, IMessage> {
        @Override
        public IMessage onMessage(HitPacket message, MessageContext ctx) {
            World world = ctx.getServerHandler().playerEntity.worldObj;
            Entity target = world.getEntityByID(message.targetEntityId);
            Entity shooter = world.getEntityByID(message.shooterEntityId);

            if (target instanceof EntityLivingBase && shooter != null) {
                DamageSource damageSource = shooter instanceof EntityPlayer
                    ? DamageSource.causePlayerDamage((EntityPlayer) shooter)
                    : DamageSource.causeMobDamage((EntityLivingBase) shooter);
                if (message.IsInstantKill) {
                    System.out.println("IsInstantKill");
                    target.attackEntityFrom(damageSource, Float.MAX_VALUE);
                    ((EntityLivingBase) target).setHealth(0);
                    for (int i = 0; i < 10; i++) {
                        ((EntityLivingBase) target).onDeath(damageSource);
                    }
                } else {
                    System.out.println("IsNotInstantKill");
                    target.attackEntityFrom(damageSource, message.damage);
                }

            }

            return null;
        }
    }
}
