package com.imgood.hyperdimensionaltech.tiles.rendertiles;

import com.imgood.hyperdimensionaltech.HyperdimensionalTech;
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
    public int rotation = 0;
    public double size = 1;
    private int meta = 0;
    public String[] textContents = {"1","2","3","4"};

    public String getText(int line) {
        if (textContents[line] != null) {
            return this.textContents[line];
        }
        return "";
    }

    public void setText(String... text) {
        this.textContents = text;
        markDirty();
    }
    public void setText(int line, String text) {
        this.textContents[line] = text;
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
        nbt.setString("Text1",getText(0));
        nbt.setString("Text2",getText(1));
        nbt.setString("Text3",getText(2));
        nbt.setString("Text4",getText(3));
        nbt.setInteger("Rotation", this.rotation);
        HyperdimensionalTech.logger.warn("testmsg731write"+this.rotation);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        size = nbt.getDouble("size");
        this.textContents[0] = nbt.getString("Text1");
        this.textContents[1] = nbt.getString("Text2");
        this.textContents[2] = nbt.getString("Text3");
        this.textContents[3] = nbt.getString("Text4");
        this.rotation = nbt.getInteger("Rotation");
        HyperdimensionalTech.logger.warn("testmsg731read"+this.rotation);
    }

}
