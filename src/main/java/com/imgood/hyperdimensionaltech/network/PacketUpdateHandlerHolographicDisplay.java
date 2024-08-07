package com.imgood.hyperdimensionaltech.network;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PacketUpdateHandlerHolographicDisplay implements IMessageHandler<PacketUpdateHolographicDisplay, IMessage> {


    @Override
    public IMessage onMessage(PacketUpdateHolographicDisplay message, MessageContext ctx) {
        World world = ctx.getServerHandler().playerEntity.worldObj;
        if (world != null) {
            TileEntity tileEntity = world.getTileEntity(message.getX(), message.getY(), message.getZ());
            if (tileEntity != null) {
                tileEntity.readFromNBT(message.getData());
                tileEntity.markDirty();
                world.markBlockForUpdate(message.getX(), message.getY(), message.getZ());
            }
        }
        return null;
    }
}
