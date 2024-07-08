package com.imgood.hyperdimensionaltech.machines;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.imgood.hyperdimensionaltech.HyperdimensionalTech;
import com.imgood.hyperdimensionaltech.machines.MachineBase.HT_LiteMultiMachineBase;
import com.imgood.hyperdimensionaltech.machines.machineaAttributes.HT_MachineConstrucs;
import com.imgood.hyperdimensionaltech.machines.machineaAttributes.HT_MachineTextureBuilder;
import gregtech.api.enums.GT_HatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Objects;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_OFF;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_ON;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION1_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;

/**
 * @author Imgood
 */
public class HT_SingularityUnravelingDevice extends HT_LiteMultiMachineBase<HT_SingularityUnravelingDevice> {

    public IStructureDefinition<HT_SingularityUnravelingDevice> STRUCTURE_DEFINITION = null;
    public HT_SingularityUnravelingDevice(@SuppressWarnings("AlibabaLowerCamelCaseVariableNaming") int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public HT_SingularityUnravelingDevice(String aName) {
        super(aName);
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


    /**
     * 快速开发简单的机器的构造方法，所以参数直接给进去就ok了，在loadMachines直接加进去
     *
     * @param aID                机器的meta值，从10000开始，依次累增，不能重复，开发的时候乱动前面写好的id会导致存档机器出事
     * @param aName              “Name”+机器的名称
     * @param aNameRegional      HTTextLocalization.Name机器的名称.getStackForm(1)
     * @param aConstructor       机器结构构成的二维String数组，用结构扫描器扫描获得，写在HT_MachineConstrucs里
     * @param aRecipeMap         机器的NEI界面相关，如：nei界面的进度条图标类型，输入输出物品/流体的格子数量，需要给定这个机器对应的Recipemap对象
     * @param enableRender       是否开启特效渲染，需要从HTConfigurations获取
     * @param defaultMode
     * @param tooltipBuilder
     * @param renderBlockOffsetX
     * @param renderBlockOffsetY
     * @param renderBlockOffsetZ
     * @param renderBlock
     */
    public HT_SingularityUnravelingDevice(int aID, String aName, String aNameRegional, String[][] aConstructor, RecipeMap aRecipeMap, boolean enableRender, byte defaultMode, GT_Multiblock_Tooltip_Builder tooltipBuilder, int renderBlockOffsetX, int renderBlockOffsetY, int renderBlockOffsetZ, Block renderBlock) {
        super(aID, aName, aNameRegional, aConstructor, aRecipeMap, enableRender, defaultMode, tooltipBuilder, renderBlockOffsetX, renderBlockOffsetY, renderBlockOffsetZ, renderBlock);
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

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {

    }

    /**
     * Due to limitation of Java type system, you might need to do an unchecked cast. HOWEVER, the returned
     * IStructureDefinition is expected to be evaluated against current instance only, and should not be used against
     * other instances, even for those of the same class.
     */


    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        return this.tooltipBuilder;
    }


    /**
     * 必须要重写这个方法，返回的内容:该机器类(this.mName)/构造函数
     *
     * @param aTileEntity
     */
    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        HyperdimensionalTech.logger.info("HTtestmsg" + new HT_SingularityUnravelingDevice(this.mName));
        return new HT_SingularityUnravelingDevice(this.mName);
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
    public boolean addToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        return super.addToMachineList(aTileEntity, aBaseCasingIndex) || this.addExoticEnergyInputToMachineList(aTileEntity, aBaseCasingIndex);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
                                 int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] { casingTexturePages[0][12], TextureFactory.builder()
                    .addIcon(OVERLAY_DTPF_ON)
                    .extFacing()
                    .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FUSION1_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] { casingTexturePages[0][12], TextureFactory.builder()
                    .addIcon(OVERLAY_DTPF_OFF)
                    .extFacing()
                    .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_DTPF_OFF)
                        .extFacing()
                        .glow()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { casingTexturePages[0][12] };
        }
        return rTexture;
    }
}
