package com.imgood.hyperdimensionaltech.tiles.rendertiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

/**
 * @program: Hyperdimensional-Tech
 * @description: 全息显示的tile
 * @author: Imgood
 * @create: 2024-07-30 14:00
 **/
public class TileHolographicDisplay extends TileEntity {
    public double Rotation = 0;
    public double size = 1;
    private int meta = 0;
    private String text = "";

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        markDirty();
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
        nbt.setDouble("renderStatus",this.size);
        nbt.setString("Text1",getText());
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        size = nbt.getDouble("size");
        this.text = nbt.getString("Text1");
    }

    public double getRotation() {
        return Rotation;
    }

    public void setRotation(double rotation) {
        Rotation = rotation;
    }
}
