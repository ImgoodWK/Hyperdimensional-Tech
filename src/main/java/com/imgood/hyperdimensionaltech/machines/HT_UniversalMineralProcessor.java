package com.imgood.hyperdimensionaltech.machines;

import com.imgood.hyperdimensionaltech.machines.MachineBase.HT_MultiMachineBuilder;
import com.imgood.hyperdimensionaltech.machines.machineaAttributes.HT_MachineTooltips;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import net.minecraft.block.Block;

public class HT_UniversalMineralProcessor extends HT_MultiMachineBuilder<HT_UniversalMineralProcessor> {

    public HT_UniversalMineralProcessor(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public HT_UniversalMineralProcessor(String aName) {
        super(aName);
    }

    public HT_UniversalMineralProcessor(int aID, String aName, String aNameRegional, String[][] aConstructor, RecipeMap aRecipeMap, boolean enableRender, byte defaultMode, GT_Multiblock_Tooltip_Builder tooltipBuilder, int renderBlockOffsetX, int renderBlockOffsetY, int renderBlockOffsetZ, Block renderBlock, boolean isEnablePerfectOverclock, int horizontalOffSet, int verticalOffSet, int depthOffSet) {
        super(aID, aName, aNameRegional, aConstructor, aRecipeMap, enableRender, defaultMode, tooltipBuilder, renderBlockOffsetX, renderBlockOffsetY, renderBlockOffsetZ, renderBlock, isEnablePerfectOverclock, horizontalOffSet, verticalOffSet, depthOffSet);
    }

}
