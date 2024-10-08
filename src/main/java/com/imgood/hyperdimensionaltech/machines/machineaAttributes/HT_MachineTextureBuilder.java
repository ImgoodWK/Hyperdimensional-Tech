package com.imgood.hyperdimensionaltech.machines.machineaAttributes;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_OFF;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_ON;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION1_GLOW;

public class HT_MachineTextureBuilder {
    //region 内部属性
    //机器开启时正面材质
    private IIconContainer machineON;
    //机器关闭时正面材质
    private IIconContainer machineOFF;
    //机器控制器材质
    private IIconContainer machineControll;

    private int machineCasingPage;
    private int machineCasingId;
    //机器名
    private String machineName;
    //endregion


    public HT_MachineTextureBuilder getMachineTextures(String machineName) {
        HT_MachineTextureBuilder StandardHyperdimensionalTech = new HT_MachineTextureBuilder();
        StandardHyperdimensionalTech.setMachineName("SingularityUnravelingDevice")
            .setMachineON(OVERLAY_DTPF_ON)
            .setMachineOFF(OVERLAY_DTPF_OFF)
            .setMachineControl(OVERLAY_FUSION1_GLOW)
            .setMachineCasing(0, 12);
        return switch (machineName) {
            case "SingularityUnravelingDevice", "singularityunravelingdevice", "UniversalMineralProcessor",
                 "universalmineralprocessor" -> StandardHyperdimensionalTech;
            default -> null;
        };
    }


    public HT_MachineTextureBuilder setMachineON(Textures.BlockIcons machineON) {
        this.machineON = machineON;
        return this;
    }


    public HT_MachineTextureBuilder setMachineOFF(Textures.BlockIcons machineOFF) {
        this.machineOFF = machineOFF;
        return this;
    }


    public HT_MachineTextureBuilder setMachineCasing(int casingPage, int casingId) {
        this.machineCasingPage = casingPage;
        this.machineCasingId = casingId;
        return this;
    }


    public HT_MachineTextureBuilder setMachineControl(Textures.BlockIcons machineControll) {
        this.machineControll = machineControll;
        return this;
    }


    public HT_MachineTextureBuilder setMachineName(String maachineName) {
        this.machineName = maachineName;
        return this;
    }

    public IIconContainer getMachineON() {
        return machineON;
    }

    public IIconContainer getMachineOFF() {
        return machineOFF;
    }

    public int getMachineCasingPage() {
        return machineCasingPage;
    }

    public int getMachineCasingId() {
        return machineCasingId;
    }

    public IIconContainer getMachineControl() {
        return machineControll;
    }

    public String getMachineName() {
        return machineName;
    }

}
