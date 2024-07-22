package com.imgood.hyperdimensionaltech.tiles.rendertiles;

import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
import com.imgood.hyperdimensionaltech.HyperdimensionalTech;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import org.lwjgl.opengl.GL11;

/**
 * @program: Hyperdimensional-Tech
 * @description: 全息主机的特效
 * @author: Imgood
 * @create: 2024-07-17 13:12
 **/
public class TileHoloController extends TileEntity {
    public double Rotation = 0;
    public double size = 1;
    public ExtendedFacing extendedFacing = ExtendedFacing.EAST_NORMAL_NONE;
    public String Facing;
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
        this.Facing = nbt.getString("Facing");
        HyperdimensionalTech.logger.warn("testmsgFacingnbt"+this.Facing);
        nbt.setDouble("renderStatus", size);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        size = nbt.getDouble("size");
        this.Facing = nbt.getString("Facing");
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

    public void setFacing(ExtendedFacing extendedFacing) {
        HyperdimensionalTech.logger.warn("testmsgTileHolosetFacing"+extendedFacing);
        this.extendedFacing = extendedFacing;
    }

    public ExtendedFacing getExtendedFacing() {
        return this.extendedFacing;
    }

    public double getRotation() {
        return Rotation;
    }

    public void setRotation(double rotation) {
        Rotation = rotation;
    }

}
