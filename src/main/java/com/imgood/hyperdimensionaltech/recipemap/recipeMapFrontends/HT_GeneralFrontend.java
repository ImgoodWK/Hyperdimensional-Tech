package com.imgood.hyperdimensionaltech.recipemap.recipeMapFrontends;

import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.common.gui.modularui.UIHelper;

import java.util.List;

public class HT_GeneralFrontend extends RecipeMapFrontend {

    private static final int xDirMaxCount = 4;
    private static final int yOrigin = 16;
    private final int itemRowCount;

    public HT_GeneralFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
                              NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder.logoPos(new Pos2d(79, 15)), neiPropertiesBuilder);
        this.itemRowCount = getItemRowCount();
        neiProperties.recipeBackgroundSize = new Size(170, 20 + (itemRowCount + getFluidRowCount()) * 18);
    }

    private int getItemRowCount() {
        return (Math.max(uiProperties.maxItemInputs, uiProperties.maxItemOutputs) - 1) / xDirMaxCount + 1;
    }

    private int getFluidRowCount() {
        return (Math.max(uiProperties.maxFluidInputs, uiProperties.maxFluidOutputs) - 1) / xDirMaxCount + 1;
    }

    @Override
    public List<Pos2d> getItemInputPositions(int itemInputCount) {
        return UIHelper.getGridPositions(itemInputCount, 6, yOrigin, xDirMaxCount);
    }

    @Override
    public List<Pos2d> getItemOutputPositions(int itemOutputCount) {
        return UIHelper.getGridPositions(itemOutputCount, 98, yOrigin, xDirMaxCount);
    }

    @Override
    public List<Pos2d> getFluidInputPositions(int fluidInputCount) {
        return UIHelper.getGridPositions(fluidInputCount, 6, yOrigin + itemRowCount * 18, xDirMaxCount);
    }

    @Override
    public List<Pos2d> getFluidOutputPositions(int fluidOutputCount) {
        return UIHelper.getGridPositions(fluidOutputCount, 98, yOrigin + itemRowCount * 18, xDirMaxCount);
    }

}
