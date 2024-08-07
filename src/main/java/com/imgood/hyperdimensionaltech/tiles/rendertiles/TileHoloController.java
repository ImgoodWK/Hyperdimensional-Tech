package com.imgood.hyperdimensionaltech.tiles.rendertiles;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

/**
 * @program: Hyperdimensional-Tech
 * @description: 全息主机的特效
 * @author: Imgood
 * @create: 2024-07-17 13:12
 **/
public class TileHoloController extends TileEntity {
    public double Rotation = 0;
    public double size = 1;
    private int meta = 0;

    public TileHoloController() {
    }

    public TileHoloController(int meta) {
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
        nbt.setDouble("renderStatus", this.size);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        size = nbt.getDouble("size");
    }

    /*@Override
    public void updateEntity() {
        HyperdimensionalTech.logger.warn("testmsgupDateEntity"+this.getExtendedFacing());
        super.updateEntity();
        if (this.Facing != null) {
            switch(this.Facing) {
                case "WEST_NORMAL_NONE":
                    this.Rotation = 90;
                    break;
                case "SOUTH_NORMAL_NONE":
                    this.Rotation = 180;
                    break;
                case "EAST_NORMAL_NONE":
                    this.Rotation = 270;
                    break;
                case "NORTH_NORMAL_NONE":
                    this.Rotation = 0;
                    break;
                default:
                    break;
            }

        Rotation = (Rotation + 1.2) % 360d;
        }*/


    public double getRotation() {
        return Rotation;
    }

    public void setRotation(double rotation) {
        Rotation = rotation;
    }

}
