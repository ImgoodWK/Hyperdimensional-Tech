package com.imgood.hyperdimensionaltech.machines.machineaAttributes;

import com.imgood.hyperdimensionaltech.utils.HTTextLocalization;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import org.jetbrains.annotations.NotNull;

public class HT_MachineTooltips {
    final GT_Multiblock_Tooltip_Builder Error = new GT_Multiblock_Tooltip_Builder();
    final GT_Multiblock_Tooltip_Builder SingularityUnravelingDevice = new GT_Multiblock_Tooltip_Builder();
    public GT_Multiblock_Tooltip_Builder getTooltip(@NotNull String machineName) {
        Error
            .addMachineType("错误机器名")
            .addInfo("错误的Tooltip,检查MachineLoader内注册参数getTooltip(‘机器名称’)是否错误")
            .addSeparator()
            .addStructureInfo("错误结构信息")
            .beginStructureBlock(1,1,1,false)
            .addController("错误控制器信息")
            .addInputHatch("错误输入仓",1)
            .addOutputHatch("错误输出仓",1)
            .addInputBus("错误输入总线", 1)
            .addOutputBus("错误输出总线", 1)
            .addEnergyHatch("错误能源仓", 1)
            .toolTipFinisher(HTTextLocalization.ModName);
        SingularityUnravelingDevice
            .addMachineType("奇点解压器")
            .addInfo("测试Tooltip")
            .addSeparator()
            .addStructureInfo("测试结构信息")
            .beginStructureBlock(15,37,15,false)
            .addController("测试控制器信息")
            .addInputHatch("测试输入仓",1)
            .addOutputHatch("测试输出仓",1)
            .addInputBus("测试输入总线", 1)
            .addOutputBus("测试输出总线", 1)
            .addEnergyHatch("测试能源仓", 2)
            .toolTipFinisher(HTTextLocalization.ModName);
        switch (machineName) {
            case "SingularityUnravelingDevice": return SingularityUnravelingDevice;
            default: return Error;
        }
    }
}