package com.imgood.hyperdimensionaltech.loader;

import net.minecraft.item.ItemStack;

import com.imgood.hyperdimensionaltech.HT_ItemList;
import com.imgood.hyperdimensionaltech.machines.HT_HyperdimensionalResonanceEvolver;
import com.imgood.hyperdimensionaltech.utils.HTTextLocalization;

public class MachineLoader {

    public static ItemStack HyperdimensionalResonanceEvolver;

    public static void loadMachines() {

        HyperdimensionalResonanceEvolver = new HT_HyperdimensionalResonanceEvolver(
            10000,
            "NameHyperdimensionalResonanceEvolver",
            HTTextLocalization.NameHyperdimensionalResonanceEvolver).getStackForm(1);
        HT_ItemList.HyperdimensionalResonanceEvolver.set(HyperdimensionalResonanceEvolver);

    }

    public static void loadMachinePostInit() {

    }
}
