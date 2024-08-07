package com.imgood.hyperdimensionaltech.recipe.machineRecipe;

import com.imgood.hyperdimensionaltech.HyperdimensionalTech;
import com.imgood.hyperdimensionaltech.recipe.IRecipePool;
import com.imgood.hyperdimensionaltech.recipemap.HT_RecipeMap;
import com.imgood.hyperdimensionaltech.utils.recipes.HT_RecipeBuilder;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GT_Utility;

import static com.imgood.hyperdimensionaltech.utils.recipes.RecipeHelper.getItemStack;
import static gregtech.api.enums.TierEU.RECIPE_MAX;

public class SingularityUnravelingDeviceRecipePool implements IRecipePool {

    final RecipeMap<?> SUD = HT_RecipeMap.SingularityUnravelingDeviceRecipes;

    // spotless:off
    @Override
    public void loadRecipes() {

        HyperdimensionalTech.LOG.info("SingularityUnravelingDeviceRecipePool loading recipes.");

        HT_RecipeBuilder
            .builder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                getItemStack("eternalsingularity", "combined_singularity", 10, 1))
            .fluidOutputs(

            )
            .itemOutputs(
                getItemStack("gregtech", "gt.metaitem.03", 32160, 32177, 64),
                getItemStack("eternalsingularity", "combined_singularity", 10, 10, 1)
            )
            .specialValue(11700)
            .eut(RECIPE_MAX)
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
