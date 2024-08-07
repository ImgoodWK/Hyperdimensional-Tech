package com.imgood.hyperdimensionaltech.machines.processingLogics;

import gregtech.api.logic.ProcessingLogic;
import gregtech.api.util.GT_ParallelHelper;
import gregtech.api.util.GT_Recipe;

import javax.annotation.Nonnull;

public class HT_ProcessingLogic extends ProcessingLogic {

    public HT_ProcessingLogic() {

    }

    @Nonnull
    protected GT_ParallelHelper createParallelHelper(@Nonnull GT_Recipe recipe) {
        return (new HT_ParallelHelper()).setRecipe(recipe)
            .setItemInputs(this.inputItems)
            .setFluidInputs(this.inputFluids)
            .setAvailableEUt(this.availableVoltage * this.availableAmperage)
            .setMachine(this.machine, this.protectItems, this.protectFluids)
            .setRecipeLocked(this.recipeLockableMachine, this.isRecipeLocked)
            .setMaxParallel(this.maxParallel)
            .setEUtModifier(this.euModifier)
            .enableBatchMode(this.batchSize)
            .setConsumption(true)
            .setOutputCalculation(true);
    }
}
