package com.imgood.hyperdimensionaltech.recipemap;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.imgood.hyperdimensionaltech.HT_ItemList;
import com.imgood.hyperdimensionaltech.recipemap.recipeMapFrontends.HT_GeneralFrontend;

import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMapBuilder;
import gregtech.api.util.GT_Recipe;

public class HT_RecipeMap extends GT_Recipe {

    public HT_RecipeMap(ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecialItems, int[] aChances,
                        FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue) {
        super(
            false,
            aInputs,
            aOutputs,
            aSpecialItems,
            aChances,
            aFluidInputs,
            aFluidOutputs,
            aDuration,
            aEUt,
            aSpecialValue);
    }

    public static final RecipeMap<RecipeMapBackend> HyperdimensionalResonanceEvolverRecipes = RecipeMapBuilder
        // At the same time , the localization key of the NEI Name
        // of this page.
        .of("ht.recipe.HyperdimensionalResonanceEvolverRecipes")
        .maxIO(28, 28, 4, 4)
        .progressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .neiHandlerInfo(builder -> builder.setDisplayStack(HT_ItemList.HyperdimensionalResonanceEvolver.get(1)))
        .disableOptimize()
        .frontend(HT_GeneralFrontend::new)
        .build();
    public static final RecipeMap<RecipeMapBackend> SingularityUnravelingDeviceRecipes = RecipeMapBuilder
        // At the same time , the localization key of the NEI Name
        // of this page.
        .of("ht.recipe.SingularityUnravelingDeviceRecipes")
        .maxIO(28, 28, 4, 4)
        .progressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .neiHandlerInfo(builder -> builder.setDisplayStack(HT_ItemList.SingularityUnravelingDevice.get(1)))
        .disableOptimize()
        .frontend(HT_GeneralFrontend::new)
        .build();
}
