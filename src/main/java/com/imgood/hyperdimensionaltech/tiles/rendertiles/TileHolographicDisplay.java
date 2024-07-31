package com.imgood.hyperdimensionaltech.tiles.rendertiles;

import com.imgood.hyperdimensionaltech.HyperdimensionalTech;
import com.imgood.hyperdimensionaltech.network.PacketUpdateHolographicDisplay;
import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.Arrays;

/**
 * @program: Hyperdimensional-Tech
 * @description: 全息显示的tile
 * @author: Imgood
 * @create: 2024-07-30 14:00
 **/
public class TileHolographicDisplay extends TileEntity {
    public int rotation = 0;
    private int meta = 0;
    public String[] textContents = {"","","",""};
    public String RGBColor = "00FFFF";
    public String getText(int line) {
        if (textContents[line] != null) {
            return this.textContents[line];
        }
        return "";
    }

    public void setText(String... text) {
        this.textContents = text;
        markDirty();
        sendUpdatePacket();
    }
    public void setText(int line, String text) {
        this.textContents[line] = text;
        markDirty();
        sendUpdatePacket();
    }
    public void setRGBColor(String color) {
        this.RGBColor = color;
        markDirty();
        sendUpdatePacket();
    }

    public TileHolographicDisplay(){
    }

    public TileHolographicDisplay(int meta){
        this.meta = meta;
    }

    public int getMeta() {
        return meta;
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return INFINITE_EXTENT_AABB;
    }

    @Override
    public double getMaxRenderDistanceSquared() {
        return 65536;
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setString("Text1",this.textContents[0]);
        nbt.setString("Text2",this.textContents[1]);
        nbt.setString("Text3",this.textContents[2]);
        nbt.setString("Text4",this.textContents[3]);
        nbt.setInteger("Rotation", this.rotation);
        nbt.setString("RGBColor",this.RGBColor);
        //HyperdimensionalTech.network.sendToServer(new PacketUpdateHolographicDisplay(xCoord, yCoord, zCoord, nbt));
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        this.textContents[0] = nbt.getString("Text1");
        this.textContents[1] = nbt.getString("Text2");
        this.textContents[2] = nbt.getString("Text3");
        this.textContents[3] = nbt.getString("Text4");
        this.rotation = nbt.getInteger("Rotation");
        this.RGBColor = nbt.getString("RGBColor");
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        NBTTagCompound nbt = pkt.func_148857_g();
        readFromNBT(nbt);
        worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
    }
    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbtTag = new NBTTagCompound();
        writeToNBT(nbtTag);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, nbtTag);
    }
    private void sendUpdatePacket() {
        NBTTagCompound nbt = new NBTTagCompound();
        writeToNBT(nbt);
        HyperdimensionalTech.network.sendToServer(new PacketUpdateHolographicDisplay(xCoord, yCoord, zCoord, nbt));
    }
}
