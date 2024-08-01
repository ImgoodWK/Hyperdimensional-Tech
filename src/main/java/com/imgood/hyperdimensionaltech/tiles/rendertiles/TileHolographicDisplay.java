package com.imgood.hyperdimensionaltech.tiles.rendertiles;

import com.imgood.hyperdimensionaltech.HyperdimensionalTech;
import com.imgood.hyperdimensionaltech.network.PacketUpdateHolographicDisplay;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

/**
 * @program: Hyperdimensional-Tech
 * @description: 全息显示的tile
 * @author: Imgood
 * @create: 2024-07-30 14:00
 **/
public class TileHolographicDisplay extends TileEntity {
    public int facing = 0;
    private int meta = 0;
    public String[] textContents = {"","","",""};
    public String RGBColor = "00FFFF";
    public String imgURL = "https://pic.imgdb.cn/item/6571a358c458853aef8a6b19.png";
    public double imgScaledX = 1;
    public double imgScaledY = 1;
    public double imgStartX = 0;
    public double imgStartY = 1;

    public double getImgScaledX() {
        return imgScaledX;
    }

    public void setImgScaledX(double imgScaledX) {
        this.imgScaledX = imgScaledX;
        markDirty();
        sendUpdatePacket();
    }

    public double getImgScaledY() {
        return imgScaledY;
    }

    public void setImgScaledY(double imgScaledY) {
        this.imgScaledY = imgScaledY;
        markDirty();
        sendUpdatePacket();
    }

    public double getImgStartX() {
        return imgStartX;
    }

    public void setImgStartX(double imgStartX) {
        this.imgStartX = imgStartX;
        markDirty();
        sendUpdatePacket();
    }

    public double getImgStartY() {
        return imgStartY;
    }

    public void setImgStartY(double imgStartY) {
        this.imgStartY = imgStartY;
        markDirty();
        sendUpdatePacket();
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
        markDirty();
        sendUpdatePacket();
    }

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

    public void setFacing(int facing) {
        this.facing = facing;
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
        nbt.setInteger("Rotation", this.facing);
        nbt.setString("RGBColor",this.RGBColor);
        nbt.setString("imgURL", this.imgURL);
        nbt.setDouble("imgScaledX", this.imgScaledX);
        nbt.setDouble("imgScaledY", this.imgScaledY);
        nbt.setDouble("imgStartX", this.imgStartX);
        nbt.setDouble("imgStartY", this.imgStartY);
        //HyperdimensionalTech.network.sendToServer(new PacketUpdateHolographicDisplay(xCoord, yCoord, zCoord, nbt));
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        this.textContents[0] = nbt.getString("Text1");
        this.textContents[1] = nbt.getString("Text2");
        this.textContents[2] = nbt.getString("Text3");
        this.textContents[3] = nbt.getString("Text4");
        this.facing = nbt.getInteger("Rotation");
        this.RGBColor = nbt.getString("RGBColor");
        this.imgURL = nbt.getString("imgURL");
        this.imgScaledX = nbt.getDouble("imgScaledX");
        this.imgScaledY = nbt.getDouble("imgScaledY");
        this.imgStartX = nbt.getDouble("imgStartX");
        this.imgStartY = nbt.getDouble("imgStartY");
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
