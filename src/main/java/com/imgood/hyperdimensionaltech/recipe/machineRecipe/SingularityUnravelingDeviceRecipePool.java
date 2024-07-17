package com.imgood.hyperdimensionaltech.recipe.machineRecipe;

import static gregtech.api.enums.TierEU.RECIPE_MAX;

import com.imgood.hyperdimensionaltech.HyperdimensionalTech;
import net.minecraft.item.ItemStack;

import com.imgood.hyperdimensionaltech.recipe.IRecipePool;
import com.imgood.hyperdimensionaltech.recipemap.HT_RecipeMap;
import com.imgood.hyperdimensionaltech.utils.recipes.HT_RecipeBuilder;
import static com.imgood.hyperdimensionaltech.utils.recipes.RecipeHelper.getItemStack;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GT_Utility;

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
                new ItemStack(GameRegistry.findItem("appliedenergistics2","item.ItemMultiMaterial"),64,47)

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
