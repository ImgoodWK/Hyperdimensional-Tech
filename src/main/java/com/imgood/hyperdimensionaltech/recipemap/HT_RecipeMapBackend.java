package com.imgood.hyperdimensionaltech.recipemap;

import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMapBackendPropertiesBuilder;

/**
 * If recipe input items include GT Material meta item and stack size is larger than 64, use this class to replace
 * RecipeMapBackend .
 */
public class HT_RecipeMapBackend extends RecipeMapBackend {

    public HT_RecipeMapBackend(RecipeMapBackendPropertiesBuilder propertiesBuilder) {
        super(propertiesBuilder);
    }

    /**
     * Do nothing.
     */
    @Override
    public void reInit() {
    }
}
