package com.imgood.hyperdimensionaltech.tiles.rendertiles;

import com.imgood.hyperdimensionaltech.HyperdimensionalTech;
import com.imgood.hyperdimensionaltech.network.PacketUpdateHolographicDisplay;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TileHolographicDisplay extends TileEntity {
    public int facing = 0;
    private int meta = 0;
    private Map<Integer, NBTTagCompound> displayDataMap;
    private boolean visableScreen = true;
    private boolean visableBody = true;
    public TileHolographicDisplay() {
        this.displayDataMap = new HashMap<>();
        this.setDisplayData(0,null);
    }

    public TileHolographicDisplay(int meta) {
        this();
        this.meta = meta;
    }

    public int getMeta() {
        return meta;
    }

    public int getDisplayDataSize() {
        return displayDataMap.size();
    }

    public boolean isVisableScreen() {
        return visableScreen;
    }

    public void setVisableScreen(boolean visableScreen) {
        this.visableScreen = visableScreen;
        markDirty();
        sendUpdatePacket();
    }

    public boolean isVisableBody() {
        return visableBody;
    }

    public void setVisableBody(boolean visableBody) {
        this.visableBody = visableBody;
        markDirty();
        sendUpdatePacket();
    }

    public List<String> getDisplayDataToShow(int index) {
        NBTTagCompound displayData = displayDataMap.get(index);
        List<String> dataList = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            dataList.add("Text" + i + ":" + displayData.getString("Text" + i));
        }
            dataList.add("Color:" + displayData.getString("RGBColor"));
            dataList.add("ImgURL:" + displayData.getString("ImgURL"));
            dataList.add("ImgScaledX:" + displayData.getDouble("Width"));
            dataList.add("ImgScaledY:" + displayData.getDouble("Height"));
            dataList.add("ImgStartX:" + displayData.getDouble("ImgStartX"));
            dataList.add("ImgStartY:" + displayData.getDouble("ImgStartY"));
        return dataList;
    }

    public void setDisplayData(int index, NBTTagCompound displayData) {
        // 创建一个包含默认值的NBTTagCompound
        NBTTagCompound defaultData = new NBTTagCompound();

        // 设置默认值
        defaultData.setString("RGBColor", "00FFFF");
        defaultData.setString("ImgURL", "");
        defaultData.setDouble("ImgScaledX", 1);
        defaultData.setDouble("ImgScaledY", 1);
        defaultData.setDouble("ImgStartX", 0);
        defaultData.setDouble("ImgStartY", 1);

        for (int i = 1; i <= 4; i++) {
            defaultData.setString("Text" + i, "");
        }

        // 如果传入的displayData为null,使用默认值
        if (displayData == null) {
            displayData = defaultData;
        } else {
            // 对于每个默认字段,如果传入的displayData中没有,就使用默认值
            for (String key : defaultData.func_150296_c()) {
                if (!displayData.hasKey(key)) {
                    if (defaultData.getTag(key) instanceof NBTTagCompound) {
                        displayData.setTag(key, defaultData.getCompoundTag(key));
                    } else {
                        displayData.setTag(key, defaultData.getTag(key));
                    }
                }
            }
        }

        // 保存更新后的displayData
        displayDataMap.put(index, displayData);
        markDirty();
        sendUpdatePacket();
    }

    public NBTTagCompound getDisplayData(int index) {
        return displayDataMap.getOrDefault(index, new NBTTagCompound());
    }

    public String getText(int index, int line) {
        line += 1;
        return getDisplayData(index).getString("Text"+line);
    }

    public String[] getContents(int index) {
        String[] contents = new String[4];
        NBTTagCompound displayData = getDisplayData(index);
        for (int i = 1; i <= 4; i++) {
            contents[i-1] = displayData.getString("Text" + i);
        }
        return contents;
    }

    public void setText(int index, String... text) {
        NBTTagCompound displayData = getDisplayData(index);
        NBTTagCompound textData = new NBTTagCompound();
        for (int i = 0; i < text.length && i < 4; i++) {
            textData.setString("Text" + (i + 1), text[i]);
        }
        displayData.setTag("TextContent", textData);
        setDisplayData(index, displayData);
    }

    public void setText(int index, int line, String text) {
        NBTTagCompound displayData = getDisplayData(index);
        NBTTagCompound textData = displayData.getCompoundTag("TextContent");
        textData.setString("Text" + line, text);
        displayData.setTag("TextContent", textData);
        setDisplayData(index, displayData);
    }

    public String getRGBColor(int index) {
        return getDisplayData(index).getString("RGBColor");
    }

    public void setRGBColor(int index, String color) {
        NBTTagCompound displayData = getDisplayData(index);
        displayData.setString("RGBColor", color);
        setDisplayData(index, displayData);
    }

    public int getFacing() {
        return facing;
    }

    public void setFacing(int facing) {
        this.facing = facing;
        markDirty();
        sendUpdatePacket();
    }

    public String getImgURL(int index) {
        return getDisplayData(index).getString("ImgURL");
    }

    public void setImgURL(int index, String imgURL) {
        NBTTagCompound displayData = getDisplayData(index);
        displayData.setString("ImgURL", imgURL);
        setDisplayData(index, displayData);
    }

    public double getImgScaledX(int index) {
        return getDisplayData(index).getDouble("ImgScaledX");
    }

    public void setImgScaledX(int index, double imgScaledX) {
        NBTTagCompound displayData = getDisplayData(index);
        displayData.setDouble("ImgScaledX", imgScaledX);
        setDisplayData(index, displayData);
    }

    public double getImgScaledY(int index) {
        return getDisplayData(index).getDouble("ImgScaledY");
    }

    public void setImgScaledY(int index, double imgScaledY) {
        NBTTagCompound displayData = getDisplayData(index);
        displayData.setDouble("ImgScaledY", imgScaledY);
        setDisplayData(index, displayData);
    }

    public double getImgStartX(int index) {
        return getDisplayData(index).getDouble("ImgStartX");
    }

    public void setImgStartX(int index, double imgStartX) {
        NBTTagCompound displayData = getDisplayData(index);
        displayData.setDouble("ImgStartX", imgStartX);
        setDisplayData(index, displayData);
    }

    public double getImgStartY(int index) {
        return getDisplayData(index).getDouble("ImgStartY");
    }

    public void setImgStartY(int index, double imgStartY) {
        NBTTagCompound displayData = getDisplayData(index);
        displayData.setDouble("ImgStartY", imgStartY);
        setDisplayData(index, displayData);
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
        NBTTagCompound displayDataNBT = new NBTTagCompound();
        for (Map.Entry<Integer, NBTTagCompound> entry : displayDataMap.entrySet()) {
            displayDataNBT.setTag("DisplayData" + entry.getKey(), entry.getValue());
        }
        nbt.setTag("DisplayDataMap", displayDataNBT);
        nbt.setInteger("Facing", facing);
        nbt.setBoolean("visableBody", visableBody);
        nbt.setBoolean("visableScreen", visableScreen);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        displayDataMap.clear();
        if (nbt.hasKey("DisplayDataMap")) {
            NBTTagCompound displayDataNBT = nbt.getCompoundTag("DisplayDataMap");
            for (Object key : displayDataNBT.func_150296_c()) {
                String keyStr = (String) key;
                if (keyStr.startsWith("DisplayData")) {
                    int index = Integer.parseInt(keyStr.substring(11));
                    displayDataMap.put(index, displayDataNBT.getCompoundTag(keyStr));
                }
            }
        }
        if (displayDataMap.isEmpty()) {
            setDisplayData(this.getDisplayDataSize(), null);
        }
        this.facing = nbt.getInteger("Facing");
        this.visableBody = nbt.getBoolean("visableBody");
        this.visableScreen = nbt.getBoolean("visableScreen");
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        readFromNBT(pkt.func_148857_g());
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
