package com.imgood.hyperdimensionaltech.utils.recipes;

import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GT_Recipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public class HT_RecipeBuilder {

    public static HT_RecipeBuilder builder() {
        return new HT_RecipeBuilder();
    }

    private ItemStack[] inputItems = new ItemStack[0];
    private ItemStack[] outputItems = new ItemStack[0];
    private FluidStack[] inputFluids = new FluidStack[0];
    private FluidStack[] outputFluids = new FluidStack[0];
    private int[] outputChance;
    private int eut = 0;
    private int duration = 0;
    private int specialValue = 0;

    public HT_RecipeBuilder() {
    }

    public HT_RecipeBuilder itemInputs(ItemStack... inputItems) {
        this.inputItems = inputItems;
        return this;
    }

    public HT_RecipeBuilder itemOutputs(ItemStack... outputItems) {
        this.outputItems = outputItems;
        return this;
    }

    public HT_RecipeBuilder itemOutputs(ItemStack[]... outputItems) {
        int buff = 0;
        List<ItemStack> itemStacks = new ArrayList<>();
        for (int i = 0; i < outputItems.length; i++) {
            for (int j = 0; j < outputItems[i].length; j++) {
                itemStacks.add(outputItems[i][j]);
                buff++;
            }
        }
        this.outputItems = itemStacks.toArray(new ItemStack[buff]);
        return this;
    }

    public HT_RecipeBuilder fluidInputs(FluidStack... inputFluids) {
        this.inputFluids = inputFluids;
        return this;
    }

    public HT_RecipeBuilder fluidOutputs(FluidStack... outputFluids) {
        this.outputFluids = outputFluids;
        return this;
    }

    public HT_RecipeBuilder outputChances(int... outputChance) {
        this.outputChance = outputChance;
        return this;
    }

    public HT_RecipeBuilder eut(int eut) {
        this.eut = eut;
        return this;
    }

    public HT_RecipeBuilder eut(long eut) {
        this.eut = (int) eut;
        return this;
    }

    public HT_RecipeBuilder duration(int duration) {
        this.duration = duration;
        return this;
    }

    public HT_RecipeBuilder specialValue(int specialValue) {
        this.specialValue = specialValue;
        return this;
    }

    public HT_RecipeBuilder noOptimize() {
        return this;
    }

    public HT_RecipeBuilder addTo(RecipeMap<?> recipeMap) {
        GT_Recipe tempRecipe = new GT_Recipe(
            false,
            inputItems,
            outputItems,
            null,
            outputChance,
            inputFluids,
            outputFluids,
            duration,
            eut,
            specialValue);

        tempRecipe.mInputs = inputItems.clone();
        tempRecipe.mOutputs = outputItems.clone();

        recipeMap.add(tempRecipe);
        return this;
    }
}
