package com.imgood.hyperdimensionaltech.machines;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.imgood.hyperdimensionaltech.recipemap.HT_RecipeMap;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * @author Imgood
 */
public class HT_SingularityUnravelingDevice extends HT_LiteMultiMachineBase<HT_SingularityUnravelingDevice>{
    public HT_SingularityUnravelingDevice(@SuppressWarnings("AlibabaLowerCamelCaseVariableNaming") int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public HT_SingularityUnravelingDevice(String aName) {
        super(aName);
    }

    public HT_SingularityUnravelingDevice(int aID, String aName, String aNameRegional, String[][] aConstructor, RecipeMap aRecipeMap) {
        super(aID, aName, aNameRegional, aConstructor, aRecipeMap);
    }
    @Override
    protected boolean isEnablePerfectOverclock() {
        return false;
    }

    @Override
    protected float getSpeedBonus() {
        return 0;
    }

    @Override
    protected int getMaxParallelRecipes() {
        return 0;
    }

    @Override
    public boolean renderInWorld() {
        return false;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {

    }

    /**
     * Due to limitation of Java type system, you might need to do an unchecked cast. HOWEVER, the returned
     * IStructureDefinition is expected to be evaluated against current instance only, and should not be used against
     * other instances, even for those of the same class.
     */
    @Override
    public IStructureDefinition<HT_SingularityUnravelingDevice> getStructureDefinition() {
        return null;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        return null;
    }

    /**
     * Checks the Machine. You have to assign the MetaTileEntities for the Hatches here.
     *
     * @param aBaseMetaTileEntity
     * @param aStack
     */
    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return false;
    }

    /**
     * @param aTileEntity is just because the internal Variable "mBaseMetaTileEntity" is set after this Call.
     * @return a newly created and ready MetaTileEntity
     */
    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return null;
    }

    /**
     * Icon of the Texture. If this returns null then it falls back to getTextureIndex.
     *
     * @param baseMetaTileEntity
     * @param side               is the Side of the Block
     * @param facing             is the direction the Block is facing
     * @param colorIndex         The Minecraft Color the Block is having
     * @param active             if the Machine is currently active (use this instead of calling)
     *                           {@code mBaseMetaTileEntity.mActive)}. Note: In case of Pipes this means if this Side is
     *                           connected to something or not.
     * @param redstoneLevel      if the Machine is currently outputting a RedstoneSignal (use this instead of calling
     *                           {@code mBaseMetaTileEntity.mRedstone} !!!)
     */
    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing, int colorIndex, boolean active, boolean redstoneLevel) {
        return new ITexture[0];
    }
}
