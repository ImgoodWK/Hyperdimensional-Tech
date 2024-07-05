package com.imgood.hyperdimensionaltech.loader;

import com.imgood.hyperdimensionaltech.HyperdimensionalTech;
import com.imgood.hyperdimensionaltech.machines.machineaAttributes.HT_MachineConstrucs;
import com.imgood.hyperdimensionaltech.machines.HT_SingularityUnravelingDevice;
import com.imgood.hyperdimensionaltech.machines.machineaAttributes.HT_MachineTextureBuilder;
import com.imgood.hyperdimensionaltech.machines.machineaAttributes.HT_MachineTooltips;
import com.imgood.hyperdimensionaltech.recipemap.HT_RecipeMap;
import net.minecraft.item.ItemStack;

import com.imgood.hyperdimensionaltech.HT_ItemList;
import com.imgood.hyperdimensionaltech.machines.HT_HyperdimensionalResonanceEvolver;
import com.imgood.hyperdimensionaltech.utils.HTTextLocalization;

public class MachineLoader {

    public static ItemStack HyperdimensionalResonanceEvolver;
    public static ItemStack SingularityUnravelingDevice;

    public static void loadMachines() {

        //region Machine Base
        HyperdimensionalTech.logger.info("Loading machine HyperdimensionalResonanceEvolver!");
        HyperdimensionalResonanceEvolver = new HT_HyperdimensionalResonanceEvolver(
            10000,
            "NameHyperdimensionalResonanceEvolver",
            HTTextLocalization.NameHyperdimensionalResonanceEvolver).getStackForm(1);
        HT_ItemList.HyperdimensionalResonanceEvolver.set(HyperdimensionalResonanceEvolver);
        //endregion
        HyperdimensionalTech.logger.info("testmsgonload"+new HT_MachineTextureBuilder().getMachineTextures("SingularityUnravelingDevice"));
        //region Lite Machine Base
        SingularityUnravelingDevice = new HT_SingularityUnravelingDevice(
            10001,
            "SingularityUnravelingDevice",
            HTTextLocalization.NameSingularityUnravelingDevice,
            HT_MachineConstrucs.CONSTRUCTOR_SingularrityUnravelingDevice,
            HT_RecipeMap.HyperdimensionalResonanceEvolverRecipes,
            true,
            (byte)0,
            new HT_MachineTooltips().getTooltip("SingularityUnravelingDevice")).getStackForm(1);
            HT_ItemList.SingularityUnravelingDevice.set(SingularityUnravelingDevice);
        //endregion
    }

    public static void loadMachinePostInit() {

    }
}
