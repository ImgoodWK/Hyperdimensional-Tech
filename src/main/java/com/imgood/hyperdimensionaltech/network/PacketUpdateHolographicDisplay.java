package com.imgood.hyperdimensionaltech.network;

import com.imgood.hyperdimensionaltech.tiles.rendertiles.TileHolographicDisplay;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class PacketUpdateHolographicDisplay implements IMessage {

    private int x, y, z;
    private NBTTagCompound data;

    public PacketUpdateHolographicDisplay() {
    }

    public PacketUpdateHolographicDisplay(int x, int y, int z, NBTTagCompound data) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.data = data;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        this.data = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        if (data != null) {
            ByteBufUtils.writeTag(buf, data);
        } else {
            ByteBufUtils.writeTag(buf, new NBTTagCompound());
        }
    }

    public static class Handler implements IMessageHandler<PacketUpdateHolographicDisplay, IMessage> {
        @Override
        public IMessage onMessage(PacketUpdateHolographicDisplay message, MessageContext ctx) {
            World world = ctx.getServerHandler().playerEntity.worldObj;
            TileEntity tileEntity = world.getTileEntity(message.x, message.y, message.z);
            if (tileEntity instanceof TileHolographicDisplay) {
                TileHolographicDisplay tile = (TileHolographicDisplay) tileEntity;
                tile.readFromNBT(message.data);
                tile.markDirty();
            }
            return null;
        }

    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public NBTTagCompound getData() {
        return data;
    }

    public void setData(NBTTagCompound data) {
        this.data = data;
    }
}

