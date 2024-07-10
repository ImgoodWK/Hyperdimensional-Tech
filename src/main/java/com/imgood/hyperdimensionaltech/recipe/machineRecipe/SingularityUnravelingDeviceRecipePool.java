package com.imgood.hyperdimensionaltech.recipe.machineRecipe;

import static gregtech.api.enums.TierEU.RECIPE_MAX;

import com.imgood.hyperdimensionaltech.HyperdimensionalTech;
import net.minecraft.item.ItemStack;

import com.imgood.hyperdimensionaltech.recipe.IRecipePool;
import com.imgood.hyperdimensionaltech.recipemap.HT_RecipeMap;
import com.imgood.hyperdimensionaltech.utils.recipes.HT_RecipeBuilder;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GT_Utility;

public class SingularityUnravelingDeviceRecipePool implements IRecipePool {

    final RecipeMap<?> SUD = HT_RecipeMap.SingularityUnravelingDeviceRecipes;

    // spotless:off
    @Override
    public void loadRecipes() {

        HyperdimensionalTech.LOG.info("HyperdimensionalResonanceEvolverRecipePool loading recipes.");

        HT_RecipeBuilder
            .builder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1)
            )
            .fluidOutputs(

            )
            .itemOutputs(
                new ItemStack(GameRegistry.findItem("eternalsingularity","eternal_singularity"),1),
                new ItemStack(GameRegistry.findItem("eternalsingularity","combined_singularity"),1,0),
                new ItemStack(GameRegistry.findItem("eternalsingularity","combined_singularity"),1,1),
                new ItemStack(GameRegistry.findItem("eternalsingularity","combined_singularity"),1,2),
                new ItemStack(GameRegistry.findItem("eternalsingularity","combined_singularity"),1,3),
                new ItemStack(GameRegistry.findItem("eternalsingularity","combined_singularity"),1,4),
                new ItemStack(GameRegistry.findItem("eternalsingularity","combined_singularity"),1,5),
                new ItemStack(GameRegistry.findItem("eternalsingularity","combined_singularity"),1,6),
                new ItemStack(GameRegistry.findItem("eternalsingularity","combined_singularity"),1,7),
                new ItemStack(GameRegistry.findItem("eternalsingularity","combined_singularity"),1,8),
                new ItemStack(GameRegistry.findItem("eternalsingularity","combined_singularity"),1,9),
                new ItemStack(GameRegistry.findItem("eternalsingularity","combined_singularity"),1,10),
                new ItemStack(GameRegistry.findItem("eternalsingularity","combined_singularity"),1,11),
                new ItemStack(GameRegistry.findItem("eternalsingularity","combined_singularity"),1,12),
                new ItemStack(GameRegistry.findItem("eternalsingularity","combined_singularity"),1,13),
                new ItemStack(GameRegistry.findItem("eternalsingularity","combined_singularity"),1,14),
                new ItemStack(GameRegistry.findItem("eternalsingularity","combined_singularity"),1,15)
            )
            .specialValue(11700)
            .eut(RECIPE_MAX)
            .outputChances(500,100,2000,2000,2000,2000,500,5000,1000,2000,100,100,1000,2000,1000,500)
            .duration(20 * 600)
            .addTo(SUD);
/*
        HT_RecipeBuilder
            .builder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1)
            )
            .fluidOutputs(

            )
            .itemOutputs(
                new ItemStack(GameRegistry.findItem("eternalsingularity","eternal_singularity"),1)
            )
            .specialValue(11700)
            .eut(RECIPE_MAX)
            .duration(20 * 600)
            .addTo(HRE);*/

    }

    // spotless:on
}
