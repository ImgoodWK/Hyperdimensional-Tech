package com.imgood.hyperdimensionaltech.tiles.rendertiles;

import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
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
    ExtendedFacing extendedFacing;
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
        nbt.setDouble("renderStatus", size);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        size = nbt.getDouble("size");
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        Rotation = (Rotation + 1.2) % 360d;
    }

    public void setRotation(ExtendedFacing extendedFacing) {
        this.extendedFacing = extendedFacing;
    }

    public ExtendedFacing getExtendedFacing() {
        return extendedFacing;
    }
}
