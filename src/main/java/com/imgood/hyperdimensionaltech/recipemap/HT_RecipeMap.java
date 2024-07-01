package com.imgood.hyperdimensionaltech.recipemap;

import com.imgood.hyperdimensionaltech.HT_ItemList;
import com.imgood.hyperdimensionaltech.recipemap.recipeMapFrontends.HT_GeneralFrontend;

import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMapBuilder;

public class HT_RecipeMap {

    public static final RecipeMap<HT_RecipeMapBackend> hyperdimensionalResonanceEvolverRecipes = RecipeMapBuilder
        // At the same time , the localization key of the NEI Name
        // of this page.
        .of("ht.recipe.hyperdimensionalResonanceEvolverRecipes", HT_RecipeMapBackend::new)
        .maxIO(4, 16, 4, 8)
        .progressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .frontend(HT_GeneralFrontend::new)
        .neiHandlerInfo(
            builder -> builder.setDisplayStack(HT_ItemList.HyperdimensionalResonanceEvolver.get(1))
                .setMaxRecipesPerPage(1))
        .disableOptimize()
        .build();

}
