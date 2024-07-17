package com.imgood.hyperdimensionaltech.loader;

import com.imgood.hyperdimensionaltech.block.BasicBlocks;
import com.imgood.hyperdimensionaltech.machines.HT_UniversalMineralProcessor;
import com.imgood.hyperdimensionaltech.machines.machineaAttributes.HT_MachineConstrucs;
import com.imgood.hyperdimensionaltech.machines.HT_SingularityUnravelingDevice;
import com.imgood.hyperdimensionaltech.machines.machineaAttributes.HT_MachineTooltips;
import com.imgood.hyperdimensionaltech.recipemap.HT_RecipeMap;
import net.minecraft.item.ItemStack;

import com.imgood.hyperdimensionaltech.HT_ItemList;
import com.imgood.hyperdimensionaltech.machines.HT_HyperdimensionalResonanceEvolver;
import com.imgood.hyperdimensionaltech.utils.HTTextLocalization;

public class MachineLoader {

    public static ItemStack HyperdimensionalResonanceEvolver;
    public static ItemStack SingularityUnravelingDevice;
    public static ItemStack UniversalMineralProcessor;

    public static void loadMachines() {

        //region HyperdimensionalResonanceEvolver
        HyperdimensionalResonanceEvolver = new HT_HyperdimensionalResonanceEvolver(
            10000,
            "NameHyperdimensionalResonanceEvolver",
            HTTextLocalization.NameHyperdimensionalResonanceEvolver).getStackForm(1);
        HT_ItemList.HyperdimensionalResonanceEvolver.set(HyperdimensionalResonanceEvolver);
        //endregion
        //region SingularityUnravelingDevice
        SingularityUnravelingDevice = new HT_SingularityUnravelingDevice(
            10001,
            "SingularityUnravelingDevice",
            HTTextLocalization.NameSingularityUnravelingDevice,
            HT_MachineConstrucs.CONSTRUCTOR_SingularrityUnravelingDevice,
            HT_RecipeMap.SingularityUnravelingDeviceRecipes,
            false,
            (byte)0,
            new HT_MachineTooltips().getTooltip("SingularityUnravelingDevice"),
            5, 5, 5,
            BasicBlocks.Block_RenderField,
            false,
            31,16,3).getStackForm(1);
        HT_ItemList.SingularityUnravelingDevice.set(SingularityUnravelingDevice);
        //endregion
        //region UniversalMineralProcessor 测试完记得改
        UniversalMineralProcessor = new HT_UniversalMineralProcessor(10002,
            "UniversalMineralProcessor",
            HTTextLocalization.NameUniversalMineralProcessor)
            .setConstructor(HT_MachineConstrucs.CONSTRUCTOR_SingularrityUnravelingDevice)
            .setRecipeMap(HT_RecipeMap.HyperdimensionalResonanceEvolverRecipes)
            .setTooltipBuilder(new HT_MachineTooltips().getTooltip("SingularityUnravelingDevice"))
            .setConstructorOffSet(5,5,5)
            .getStackForm(1);
        HT_ItemList.SingularityUnravelingDevice.set(UniversalMineralProcessor);
        //endregion
        //region test


        //endregion
    }

    public static void loadMachinePostInit() {

    }
}
