package com.imgood.hyperdimensionaltech.machines;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.imgood.hyperdimensionaltech.HyperdimensionalTech;
import com.imgood.hyperdimensionaltech.machines.MachineBase.HT_LiteMultiMachineBase;
import com.imgood.hyperdimensionaltech.machines.machineaAttributes.HT_MachineConstrucs;

import com.imgood.hyperdimensionaltech.machines.machineaAttributes.HT_StructureDefinitionBuilder;
import gregtech.api.enums.GT_HatchElement;

import gregtech.api.interfaces.IHatchElement;
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

import java.util.List;
import java.util.Objects;


import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.ExoticEnergy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_OFF;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_ON;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION1_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;

/**
 * @author Imgood
 */
public class HT_SingularityUnravelingDevice extends HT_LiteMultiMachineBase<HT_SingularityUnravelingDevice> {

    /**
     * 快速开发简单的机器的构造方法，所以参数直接给进去就ok了，在loadMachines直接加进去
     *
     * @param aID                      机器的meta值，从10000开始，依次累增，不能重复，开发的时候乱动前面写好的id会导致存档机器出事
     * @param aName                    “Name”+机器的名称
     * @param aNameRegional            HTTextLocalization.Name机器的名称.getStackForm(1)
     * @param aConstructor             机器结构构成的二维String数组，用结构扫描器扫描获得，写在HT_MachineConstrucs里
     * @param aRecipeMap               机器的NEI界面相关，如：nei界面的进度条图标类型，输入输出物品/流体的格子数量，需要给定这个机器对应的Recipemap对象
     * @param enableRender             是否开启特效渲染，需要从HTConfigurations获取
     * @param defaultMode
     * @param tooltipBuilder
     * @param renderBlockOffsetX
     * @param renderBlockOffsetY
     * @param renderBlockOffsetZ
     * @param renderBlock
     * @param isEnablePerfectOverclock
     * @param verticalOffSet
     * @param horizontalOffSet
     * @param depthOffSet
     */
    public HT_SingularityUnravelingDevice(int aID, String aName, String aNameRegional, String[][] aConstructor, RecipeMap aRecipeMap, boolean enableRender, byte defaultMode, GT_Multiblock_Tooltip_Builder tooltipBuilder, int renderBlockOffsetX, int renderBlockOffsetY, int renderBlockOffsetZ, Block renderBlock, boolean isEnablePerfectOverclock, int verticalOffSet, int horizontalOffSet, int depthOffSet) {
        super(aID, aName, aNameRegional, aConstructor, aRecipeMap, enableRender, defaultMode, tooltipBuilder, renderBlockOffsetX, renderBlockOffsetY, renderBlockOffsetZ, renderBlock, isEnablePerfectOverclock, verticalOffSet, horizontalOffSet, depthOffSet);
    }

    public IStructureDefinition<HT_SingularityUnravelingDevice> STRUCTURE_DEFINITION = null;
    public HT_SingularityUnravelingDevice(@SuppressWarnings("AlibabaLowerCamelCaseVariableNaming") int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public HT_SingularityUnravelingDevice(String aName) {
        super(aName);
    }

    public IStructureDefinition<HT_SingularityUnravelingDevice> getStructureDefinition() {
        HyperdimensionalTech.logger.info( new HT_StructureDefinitionBuilder<HT_SingularityUnravelingDevice>()
            .setStructureName(this.getStructureName())
            .addElement("gregtech:gt.blockcasings" , 12)
            .addElement("gregtech:gt.blockcasings" , 12)
            .addElement("gregtech:gt.blockcasings" , 12)
            .addElement("gregtech:gt.blockcasings" , 12)
            .addElement("gregtech:gt.blockcasings" , 12)
            .addElement("gregtech:gt.blockcasings" , 12)
            .addElement("gregtech:gt.blockcasings" , 12)
            .addElement("gregtech:gt.blockcasings" , 12)
            .addElement("gregtech:gt.blockcasings" , 12)
            .addElement("gregtech:gt.blockcasings" , 12)
            .addElement("gregtech:gt.blockcasings" , 12)
            .addElement("gregtech:gt.blockmachines" , 10001)
            .build());
        return new HT_StructureDefinitionBuilder<HT_SingularityUnravelingDevice>()
            .setStructureName(this.getStructureName())
            .addElement("gregtech:gt.blockcasings" , 12)
            .addElement("gregtech:gt.blockcasings" , 12)
            .addElement("gregtech:gt.blockcasings" , 12)
            .addElement("gregtech:gt.blockcasings" , 12)
            .addElement("gregtech:gt.blockcasings" , 12)
            .addElement("gregtech:gt.blockcasings" , 12)
            .addElement("gregtech:gt.blockcasings" , 12)
            .addElement("gregtech:gt.blockcasings" , 12)
            .addElement("gregtech:gt.blockcasings" , 12)
            .addElement("gregtech:gt.blockcasings" , 12)
            .addElement("gregtech:gt.blockcasings" , 12)
            .addElement("gregtech:gt.blockmachines" , 10001)
            .build();
    }
    /*@Override
    public IStructureDefinition<HT_SingularityUnravelingDevice> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition
                .<HT_SingularityUnravelingDevice>builder()
                .addShape(this.getStructUre_Pice_Main(), transpose(HT_MachineConstrucs.CONSTRUCTOR_HyperdimensionalResonanceEvolver))
                .addElement('A', GT_HatchElementBuilder.<HT_SingularityUnravelingDevice>builder()
                    .atLeast(GT_HatchElement.InputBus, GT_HatchElement.OutputBus)
                    .adder(HT_SingularityUnravelingDevice::addToMachineList)
                    .casingIndex(12)
                    .dot(2)
                    .buildAndChain(Objects.requireNonNull(Block.getBlockFromName("gregtech:gt.blockcasings")), 12))
                .addElement('B', ofBlock(Objects.requireNonNull(Block.getBlockFromName("gregtech:gt.blockcasings")),13))
                .addElement('C', ofBlock(Objects.requireNonNull(Block.getBlockFromName("gregtech:gt.blockcasings")),14))
                .addElement('D', ofBlock(Objects.requireNonNull(Block.getBlockFromName("gregtech:gt.blockcasings5")),13))
                .addElement('E', ofBlock(Objects.requireNonNull(Block.getBlockFromName("tectech:gt.blockcasingsBA0")),12))
                .addElement('F', ofBlock(Objects.requireNonNull(Block.getBlockFromName("tectech:gt.blockcasingsTT")),11))
                .addElement('G', ofBlock(Objects.requireNonNull(Block.getBlockFromName("tectech:gt.spacetime_compression_field_generator")),8))
                .addElement('H', ofBlock(Objects.requireNonNull(Block.getBlockFromName("tectech:gt.stabilisation_field_generator")),8))
                .addElement('I', ofBlock(Objects.requireNonNull(Block.getBlockFromName("tectech:gt.time_acceleration_field_generator")),8))
                .addElement('J', ofBlock(Objects.requireNonNull(Block.getBlockFromName("hyperdimensionaltech:antiBlockFrameless")),6))
                .addElement('K', ofBlock(Objects.requireNonNull(Block.getBlockFromName("tectech:tile.quantumGlass")),0))
                .addElement('L', ofBlock(Objects.requireNonNull(Block.getBlockFromName("gregtech:gt.blockmachines")),10001))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }*/
    private List<IHatchElement<? super HT_SingularityUnravelingDevice>> getAllowedHatches() {
        return ImmutableList.of(InputHatch, OutputHatch, InputBus, OutputBus, Maintenance, Energy, ExoticEnergy);
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
        return this.getTooltipBuilder();
    }


    /**
     * 必须要重写这个方法，返回的内容:该机器类(this.mName)/构造函数
     *
     * @param aTileEntity
     */
    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new HT_SingularityUnravelingDevice(this.mName);
    }

    @Override
    public boolean addToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        return super.addToMachineList(aTileEntity, aBaseCasingIndex);
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
