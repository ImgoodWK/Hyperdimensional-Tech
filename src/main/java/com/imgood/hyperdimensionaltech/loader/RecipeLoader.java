package com.imgood.hyperdimensionaltech.loader;

import com.imgood.hyperdimensionaltech.HyperdimensionalTech;
import com.imgood.hyperdimensionaltech.recipe.IRecipePool;
import com.imgood.hyperdimensionaltech.recipe.machineRecipe.HyperdimensionalResonanceEvolverRecipePool;
import com.imgood.hyperdimensionaltech.recipe.machineRecipe.SingularityUnravelingDeviceRecipePool;

public class RecipeLoader {

    public static void loadRecipes() {
        IRecipePool[] recipePools = new IRecipePool[]{new HyperdimensionalResonanceEvolverRecipePool(),
            new SingularityUnravelingDeviceRecipePool()};

        for (IRecipePool recipePool : recipePools) {
            HyperdimensionalTech.logger.info("Loading recipes:" + recipePool);
            recipePool.loadRecipes();
        }

    }

}
