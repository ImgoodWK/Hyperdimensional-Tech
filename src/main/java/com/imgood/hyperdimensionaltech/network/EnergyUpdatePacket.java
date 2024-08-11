package com.imgood.hyperdimensionaltech.network;

import com.imgood.hyperdimensionaltech.item.EnergyWeapon;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * @program: Hyperdimensional-Tech
 * @description:
 * @author: Imgood
 * @create: 2024-08-12 16:32
 **/
public class EnergyUpdatePacket implements IMessage {
    private int energy;

    public EnergyUpdatePacket() {}

    public EnergyUpdatePacket(int energy) {
        this.energy = energy;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        energy = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(energy);
    }

    public static class Handler implements IMessageHandler<EnergyUpdatePacket, IMessage> {
        @Override
        public IMessage onMessage(EnergyUpdatePacket message, MessageContext ctx) {
            EntityPlayer player = ctx.getServerHandler().playerEntity;
            ItemStack heldItem = player.getHeldItem();
            if (heldItem != null && heldItem.getItem() instanceof EnergyWeapon) {
                EnergyWeapon weapon = (EnergyWeapon) heldItem.getItem();
                weapon.setEnergy(message.energy);
                System.out.println("testmsgmessage"+message.energy);
            }
            return null;
        }
    }
}
