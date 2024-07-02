package com.imgood.hyperdimensionaltech.machines;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import javax.annotation.Nonnull;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;


import com.imgood.hyperdimensionaltech.HyperdimensionalTech;
import com.imgood.hyperdimensionaltech.block.BasicBlocks;
import com.imgood.hyperdimensionaltech.config.HTConfigurations;
import com.imgood.hyperdimensionaltech.machines.processingLogics.HT_ProcessingLogic;
import com.imgood.hyperdimensionaltech.recipemap.HT_RecipeMap;
import com.imgood.hyperdimensionaltech.utils.HTTextHandler;
import com.imgood.hyperdimensionaltech.utils.HTTextLocalization;
import com.imgood.hyperdimensionaltech.utils.Utils;
import static com.imgood.hyperdimensionaltech.utils.HTTextLocalization.Tooltip_DoNotNeedMaintenance;

import gregtech.api.enums.GT_HatchElement;
import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import static gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_OFF;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DTPF_ON;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FUSION1_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

/**
 * @author Imgood
 */
public class HT_HyperdimensionalResonanceEvolver
    extends HT_MultiMachineBase<HT_HyperdimensionalResonanceEvolver> {

    private byte mode = ValueEnum.Mode_Default_HyperdimensionalResonanceEvolver;
    private int coefficientMultiplier = 1;
    private boolean isWirelessMode = true;
    private UUID ownerUUID;
    private long costingWirelessEUTemp = 0;

    private boolean isRendering = true;

    private boolean enableRender = HTConfigurations.EnableRenderDefaultHyperdimensionalResonanceEvolver;

    public HT_HyperdimensionalResonanceEvolver(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public HT_HyperdimensionalResonanceEvolver(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new HT_HyperdimensionalResonanceEvolver(this.mName);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        if (tag.getBoolean("isWirelessMode")) {
            currentTip.add(EnumChatFormatting.LIGHT_PURPLE + HTTextLocalization.Waila_WirelessMode);
            currentTip.add(
                EnumChatFormatting.AQUA + HTTextLocalization.Waila_CurrentEuCost
                    + EnumChatFormatting.RESET
                    + ": "
                    + EnumChatFormatting.GOLD
                    + GT_Utility.formatNumbers(tag.getLong("costingWirelessEUTemp"))
                    + EnumChatFormatting.RESET
                    + " EU");
        } else {
            currentTip.add(
                EnumChatFormatting.GOLD + StatCollector
                    .translateToLocalFormatted("ThermalEnergyDevourer.modeMsg." + tag.getByte("mode"), new Object[0]));
        }
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        final IGregTechTileEntity tileEntity = getBaseMetaTileEntity();
        if (tileEntity != null) {
            tag.setByte("mode", mode);
            tag.setBoolean("isWirelessMode", isWirelessMode);
            tag.setLong("costingWirelessEUTemp", costingWirelessEUTemp);
        }
    }

    @Override
    public String[] getInfoData() {
        String[] origin = super.getInfoData();
        String[] ret = new String[origin.length + 1];
        System.arraycopy(origin, 0, ret, 0, origin.length);
        ret[origin.length] = EnumChatFormatting.AQUA + HTTextHandler
            .texter("Coefficient Multiplier", "MachineInfoData.HyperdimensionalResonanceEvolver.coefficientMultiplier")
            + ": "
            + EnumChatFormatting.GOLD
            + this.coefficientMultiplier;
        return ret;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setByte("mode", mode);
        aNBT.setInteger("coefficientMultiplier", coefficientMultiplier);
        aNBT.setBoolean("isWirelessMode", isWirelessMode);
        aNBT.setLong("costingWirelessEUTemp", costingWirelessEUTemp);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        mode = aNBT.getByte("mode");
        coefficientMultiplier = aNBT.getInteger("coefficientMultiplier");
        isWirelessMode = aNBT.getBoolean("isWirelessMode");
        costingWirelessEUTemp = aNBT.getLong("costingWirelessEUTemp");
    }

    @Override
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (getBaseMetaTileEntity().isServerSide()) {
            this.mode = (byte) ((this.mode + 1) % 2);
            GT_Utility.sendChatToPlayer(
                aPlayer,
                StatCollector.translateToLocal("HyperdimensionalResonanceEvolver.modeMsg." + this.mode));
        }
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new HT_ProcessingLogic() {

            @NotNull
            @Override
            public CheckRecipeResult process() {
                if (!isWirelessMode) {
                    setEuModifier(getEuModifier());
                    setSpeedBonus(getSpeedBonus());
                    setOverclock(isEnablePerfectOverclock() ? 2 : 1, 2);
                }
                return super.process();
            }

            @Nonnull
            @Override
            protected GT_OverclockCalculator createOverclockCalculator(@Nonnull GT_Recipe recipe) {
                return isWirelessMode ? GT_OverclockCalculator.ofNoOverclock(recipe)
                    : super.createOverclockCalculator(recipe);
            }
        }.setMaxParallelSupplier(this::getLimitedMaxParallel);
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        if (isWirelessMode) {
            // wireless mode ignore voltage limit
            logic.setAvailableVoltage(0);
            logic.setAvailableAmperage(1);
            logic.setAmperageOC(false);
        } else {
            super.setProcessingLogicPower(logic);
        }
    }

    @Nonnull
    @Override
    public CheckRecipeResult checkProcessing() {
        boolean flag = false;

        if (!isWirelessMode) {
            if (this.getBaseMetaTileEntity().isActive() && this.maxProgresstime() != 0) {
                this.isRendering = true;
                if (this.enableRender && this.isRendering) {
                    HyperdimensionalTech.logger.info("testmsgend"+(this.enableRender && !this.isRendering)+this.enableRender+this.isRendering);
                    this.createRenderBlock();
                }
            }else {
                this.isRendering = false;
                this.destroyRenderBlock();
            }
            return super.checkProcessing();
        }
        if (!flag) {
            return CheckRecipeResultRegistry.NO_RECIPE;
        } else {
            setupProcessingLogic(processingLogic);

            CheckRecipeResult result = doCheckRecipe();
            result = postCheckRecipe(result, processingLogic);
            // inputs are consumed at this point
            updateSlots();
            if (!result.wasSuccessful()) {
                return result;
            }

            mEfficiency = 10000;
            mEfficiencyIncrease = 10000;

            if (processingLogic.getCalculatedEut() > Long.MAX_VALUE / processingLogic.getDuration()) {
                // total eu cost has overflowed
                costingWirelessEUTemp = 1145141919810L;
                BigInteger finalCostEU = BigInteger.valueOf(-1)
                    .multiply(BigInteger.valueOf(processingLogic.getCalculatedEut()))
                    .multiply(BigInteger.valueOf(processingLogic.getDuration()));
                if (!addEUToGlobalEnergyMap(ownerUUID, finalCostEU)) {
                    return CheckRecipeResultRegistry.insufficientPower(1145141919810L);
                }
            } else {
                // fine
                costingWirelessEUTemp = processingLogic.getCalculatedEut() * processingLogic.getDuration();
                if (!addEUToGlobalEnergyMap(ownerUUID, -costingWirelessEUTemp)) {
                    return CheckRecipeResultRegistry.insufficientPower(costingWirelessEUTemp);
                }
            }
            mMaxProgresstime = ValueEnum.TickPerProgressing_WirelessMode_HyperdimensionalResonanceEvolver;

            mOutputItems = processingLogic.getOutputItems();
            mOutputFluids = processingLogic.getOutputFluids();


            return result;
        }

        // wireless mode

    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);

        this.ownerUUID = aBaseMetaTileEntity.getOwnerUuid();
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return HT_RecipeMap.HyperdimensionalResonanceEvolverRecipes;
    }

    @Override
    protected boolean isEnablePerfectOverclock() {
        return false;
    }

    @Override
    protected float getEuModifier() {
        return mode == 0 ? 1 : 1F / coefficientMultiplier;
    }

    @Override
    protected float getSpeedBonus() {
        return mode == 1 ? 1 : 0.5F / coefficientMultiplier;
    }

    @Override
    protected int getMaxParallelRecipes() {
        if (isWirelessMode) {
            return Integer.MAX_VALUE;
        }
        return mode == 1 ? ValueEnum.Parallel_HighParallelMode_HyperdimensionalResonanceEvolver
            : ValueEnum.Parallel_HighSpeedMode_HyperdimensionalResonanceEvolver;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        repairMachine();
        if (!checkPiece(STRUCTURE_PIECE_MAIN, horizontalOffSet, verticalOffSet, depthOffSet)) {
            return false;
        }
        coefficientMultiplier = 1 + getExtraCoefficientMultiplierByVoltageTier();
        ItemStack controllerSlot = getControllerSlot();
        isWirelessMode = controllerSlot != null && controllerSlot.stackSize > 0
            && Utils.metaItemEqual(controllerSlot, ItemList.EnergisedTesseract.get(1));
        return true;
    }

    public int getExtraCoefficientMultiplierByVoltageTier() {
        return (int) Utils.calculatePowerTier(getMaxInputEu());
    }
    // endregion

    // region Structure
    // spotless:off
    private final int horizontalOffSet = 27;
    private final int verticalOffSet = 37;
    private final int depthOffSet = 10;

    public int tier() {
        return 10;
    }
    private static final String STRUCTURE_PIECE_MAIN = "mainHyperdimensionalResonanceEvolver";
    private IStructureDefinition<HT_HyperdimensionalResonanceEvolver> STRUCTURE_DEFINITION = null;

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, horizontalOffSet, verticalOffSet, depthOffSet);
    }

    @Override
    public boolean addToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        return super.addToMachineList(aTileEntity, aBaseCasingIndex) || this.addExoticEnergyInputToMachineList(aTileEntity, aBaseCasingIndex);
    }
    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) {
            return -1;
        }
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, horizontalOffSet, verticalOffSet, depthOffSet, elementBudget, env, false, true);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean renderInWorld() {
        return false;
    }
    @Override
    public IStructureDefinition<HT_HyperdimensionalResonanceEvolver> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition
                .<HT_HyperdimensionalResonanceEvolver>builder()
                .addShape("mainHyperdimensionalResonanceEvolver", transpose(HT_MachineConstrucs.CONSTRUCTOR_HyperdimensionalResonanceEvolver))
                    .addElement('A', GT_HatchElementBuilder.<HT_HyperdimensionalResonanceEvolver>builder()
                    .atLeast(GT_HatchElement.InputBus, GT_HatchElement.OutputBus)
                    .adder(HT_HyperdimensionalResonanceEvolver::addToMachineList)
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
                .addElement('L', ofBlock(Objects.requireNonNull(Block.getBlockFromName("gregtech:gt.blockmachines")),10000))
                .build();
        }

        return STRUCTURE_DEFINITION;
    }




    // spotless:on
    // endregion

    // region General

    @Override
    public boolean getDefaultInputSeparationMode() {
        return false;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(HTTextLocalization.Tooltip_HyperdimensionalResonanceEvolver_MachineType)
            .addInfo(HTTextLocalization.Tooltip_HyperdimensionalResonanceEvolver_Controller)
            .addInfo(HTTextLocalization.Tooltip_HyperdimensionalResonanceEvolver_01)
            .addInfo(HTTextLocalization.Tooltip_HyperdimensionalResonanceEvolver_02)
            .addInfo(HTTextLocalization.Tooltip_HyperdimensionalResonanceEvolver_03)
            .addInfo(HTTextLocalization.Tooltip_HyperdimensionalResonanceEvolver_04)
            .addInfo(HTTextLocalization.Tooltip_HyperdimensionalResonanceEvolver_05)
            .addInfo(HTTextLocalization.Tooltip_HyperdimensionalResonanceEvolver_06)
            .addInfo(HTTextLocalization.Tooltip_HyperdimensionalResonanceEvolver_07)
            .addInfo(HTTextLocalization.Tooltip_HyperdimensionalResonanceEvolver_08)
            .addInfo(HTTextLocalization.Tooltip_HyperdimensionalResonanceEvolver_09)
            .addInfo(HTTextLocalization.Tooltip_HyperdimensionalResonanceEvolver_10)
            .addInfo(HTTextLocalization.Tooltip_HyperdimensionalResonanceEvolver_11)
            .addInfo(HTTextLocalization.Tooltip_HyperdimensionalResonanceEvolver_12)
            .addInfo(HTTextLocalization.Tooltip_HyperdimensionalResonanceEvolver_13)
            .addInfo(HTTextLocalization.Tooltip_HyperdimensionalResonanceEvolver_14)
            .addInfo(HTTextLocalization.textScrewdriverChangeMode)
            .addSeparator()
            .addInfo(HTTextLocalization.StructureTooComplex)
            .addInfo(HTTextLocalization.BLUE_PRINT_INFO)
            .addStructureInfo(HTTextLocalization.Tooltip_HyperdimensionalResonanceEvolver_2_01)
            .addStructureInfo(Tooltip_DoNotNeedMaintenance)
            .beginStructureBlock(15, 37, 15, false)
            .addController(HTTextLocalization.textFrontBottom)
            .addInputHatch(HTTextLocalization.textUseBlueprint, 1)
            .addOutputHatch(HTTextLocalization.textUseBlueprint, 1)
            .addInputBus(HTTextLocalization.textUseBlueprint, 1)
            .addOutputBus(HTTextLocalization.textUseBlueprint, 1)
            .addEnergyHatch(HTTextLocalization.textUseBlueprint, 2)
            .toolTipFinisher(HTTextLocalization.ModName);
        return tt;
    }

    public void createRenderBlock() {
        int x = this.getBaseMetaTileEntity().getXCoord();
        int y = this.getBaseMetaTileEntity().getYCoord();
        int z = this.getBaseMetaTileEntity().getZCoord();
        double xOffset = (double)(10 * this.getExtendedFacing().getRelativeBackInWorld().offsetX + 33 * this.getExtendedFacing().getRelativeUpInWorld().offsetX);
        double zOffset = (double)(10 * this.getExtendedFacing().getRelativeBackInWorld().offsetZ + 33 * this.getExtendedFacing().getRelativeUpInWorld().offsetZ);
        double yOffset = (double)(10 * this.getExtendedFacing().getRelativeBackInWorld().offsetY + 33 * this.getExtendedFacing().getRelativeUpInWorld().offsetY);
        this.getBaseMetaTileEntity().getWorld().setBlock((int)((double)x + xOffset), (int)((double)y + yOffset), (int)((double)z + zOffset), Blocks.air);
        this.getBaseMetaTileEntity().getWorld().setBlock((int)((double)x + xOffset), (int)((double)y + yOffset), (int)((double)z + zOffset), BasicBlocks.Block_HRERender);
    }
    public void destroyRenderBlock() {
        int x = this.getBaseMetaTileEntity().getXCoord();
        int y = this.getBaseMetaTileEntity().getYCoord();
        int z = this.getBaseMetaTileEntity().getZCoord();
        double xOffset = (double)(10 * this.getExtendedFacing().getRelativeBackInWorld().offsetX + 33 * this.getExtendedFacing().getRelativeUpInWorld().offsetX);
        double zOffset = (double)(10 * this.getExtendedFacing().getRelativeBackInWorld().offsetZ + 33 * this.getExtendedFacing().getRelativeUpInWorld().offsetZ);
        double yOffset = (double)(10 * this.getExtendedFacing().getRelativeBackInWorld().offsetY + 33 * this.getExtendedFacing().getRelativeUpInWorld().offsetY);
        this.getBaseMetaTileEntity().getWorld().setBlock((int)((double)x + xOffset), (int)((double)y + yOffset), (int)((double)z + zOffset), Blocks.air);
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

    /*
    @SideOnly(Side.CLIENT)
    private void renderField(double x, double y, double z, int side, double minU, double maxU, double minV,
        double maxV) {
        // Field height
        double fieldHeight = 32.0;
        // Offset
        y += 20;
        switch (this.getExtendedFacing()) {
            case NORTH_NORMAL_NONE:
                z += 7;
                break;
            case SOUTH_NORMAL_NONE:
                z -= 7;
                break;
            case EAST_NORMAL_NONE:
                x -= 7;
                break;
            case WEST_NORMAL_NONE:
                x += 7;
                break;
        }
        Tessellator tes = Tessellator.instance;
        switch (side) {
            case 0:
                tes.addVertexWithUV(x + 3.0, y, z + 7.0, maxU, maxV);
                tes.addVertexWithUV(x + 3.0, y + fieldHeight, z + 7.0, maxU, minV);
                tes.addVertexWithUV(x - 3.0, y + fieldHeight, z + 7.0, minU, minV);
                tes.addVertexWithUV(x - 3.0, y, z + 7.0, minU, maxV);
                tes.addVertexWithUV(x - 3.0, y, z + 7.0, minU, maxV);
                tes.addVertexWithUV(x - 3.0, y + fieldHeight, z + 7.0, minU, minV);
                tes.addVertexWithUV(x + 3.0, y + fieldHeight, z + 7.0, maxU, minV);
                tes.addVertexWithUV(x + 3.0, y, z + 7.0, maxU, maxV);
                break;
            case 1:
                tes.addVertexWithUV(x + 7.0, y, z + 4.0, maxU, maxV);
                tes.addVertexWithUV(x + 7.0, y + fieldHeight, z + 4.0, maxU, minV);
                tes.addVertexWithUV(x + 7.0, y + fieldHeight, z - 4.0, minU, minV);
                tes.addVertexWithUV(x + 7.0, y, z - 4.0, minU, maxV);
                tes.addVertexWithUV(x + 7.0, y, z - 4.0, minU, maxV);
                tes.addVertexWithUV(x + 7.0, y + fieldHeight, z - 4.0, minU, minV);
                tes.addVertexWithUV(x + 7.0, y + fieldHeight, z + 4.0, maxU, minV);
                tes.addVertexWithUV(x + 7.0, y, z + 4.0, maxU, maxV);
                break;
            case 2:
                tes.addVertexWithUV(x + 3.0, y, z - 7.0, maxU, maxV);
                tes.addVertexWithUV(x + 3.0, y + fieldHeight, z - 7.0, maxU, minV);
                tes.addVertexWithUV(x - 3.0, y + fieldHeight, z - 7.0, minU, minV);
                tes.addVertexWithUV(x - 3.0, y, z - 7.0, minU, maxV);
                tes.addVertexWithUV(x - 3.0, y, z - 7.0, minU, maxV);
                tes.addVertexWithUV(x - 3.0, y + fieldHeight, z - 7.0, minU, minV);
                tes.addVertexWithUV(x + 3.0, y + fieldHeight, z - 7.0, maxU, minV);
                tes.addVertexWithUV(x + 3.0, y, z - 7.0, maxU, maxV);
                break;
            case 3:
                tes.addVertexWithUV(x - 7.0, y, z + 4.0, maxU, maxV);
                tes.addVertexWithUV(x - 7.0, y + fieldHeight, z + 4.0, maxU, minV);
                tes.addVertexWithUV(x - 7.0, y + fieldHeight, z - 4.0, minU, minV);
                tes.addVertexWithUV(x - 7.0, y, z - 4.0, minU, maxV);
                tes.addVertexWithUV(x - 7.0, y, z - 4.0, minU, maxV);
                tes.addVertexWithUV(x - 7.0, y + fieldHeight, z - 4.0, minU, minV);
                tes.addVertexWithUV(x - 7.0, y + fieldHeight, z + 4.0, maxU, minV);
                tes.addVertexWithUV(x - 7.0, y, z + 4.0, maxU, maxV);
                break;
            case 4:
                tes.addVertexWithUV(x - 3.0, y, z + 7.0, maxU, maxV);
                tes.addVertexWithUV(x - 3.0, y + fieldHeight, z + 7.0, maxU, minV);
                tes.addVertexWithUV(x - 7.0, y + fieldHeight, z + 4.0, minU, minV);
                tes.addVertexWithUV(x - 7.0, y, z + 4.0, minU, maxV);
                tes.addVertexWithUV(x - 7.0, y, z + 4.0, minU, maxV);
                tes.addVertexWithUV(x - 7.0, y + fieldHeight, z + 4.0, minU, minV);
                tes.addVertexWithUV(x - 3.0, y + fieldHeight, z + 7.0, maxU, minV);
                tes.addVertexWithUV(x - 3.0, y, z + 7.0, maxU, maxV);
                break;
            case 5:
                tes.addVertexWithUV(x - 3.0, y, z - 7.0, maxU, maxV);
                tes.addVertexWithUV(x - 3.0, y + fieldHeight, z - 7.0, maxU, minV);
                tes.addVertexWithUV(x - 7.0, y + fieldHeight, z - 4.0, minU, minV);
                tes.addVertexWithUV(x - 7.0, y, z - 4.0, minU, maxV);
                tes.addVertexWithUV(x - 7.0, y, z - 4.0, minU, maxV);
                tes.addVertexWithUV(x - 7.0, y + fieldHeight, z - 4.0, minU, minV);
                tes.addVertexWithUV(x - 3.0, y + fieldHeight, z - 7.0, maxU, minV);
                tes.addVertexWithUV(x - 3.0, y, z - 7.0, maxU, maxV);
                break;
            case 6:
                tes.addVertexWithUV(x + 3.0, y, z + 7.0, maxU, maxV);
                tes.addVertexWithUV(x + 3.0, y + fieldHeight, z + 7.0, maxU, minV);
                tes.addVertexWithUV(x + 7.0, y + fieldHeight, z + 4.0, minU, minV);
                tes.addVertexWithUV(x + 7.0, y, z + 4.0, minU, maxV);
                tes.addVertexWithUV(x + 7.0, y, z + 4.0, minU, maxV);
                tes.addVertexWithUV(x + 7.0, y + fieldHeight, z + 4.0, minU, minV);
                tes.addVertexWithUV(x + 3.0, y + fieldHeight, z + 7.0, maxU, minV);
                tes.addVertexWithUV(x + 3.0, y, z + 7.0, maxU, maxV);
                break;
            case 7:
                tes.addVertexWithUV(x + 3.0, y, z - 7.0, maxU, maxV);
                tes.addVertexWithUV(x + 3.0, y + fieldHeight, z - 7.0, maxU, minV);
                tes.addVertexWithUV(x + 7.0, y + fieldHeight, z - 4.0, minU, minV);
                tes.addVertexWithUV(x + 7.0, y, z - 4.0, minU, maxV);
                tes.addVertexWithUV(x + 7.0, y, z - 4.0, minU, maxV);
                tes.addVertexWithUV(x + 7.0, y + fieldHeight, z - 4.0, minU, minV);
                tes.addVertexWithUV(x + 3.0, y + fieldHeight, z - 7.0, maxU, minV);
                tes.addVertexWithUV(x + 3.0, y, z - 7.0, maxU, maxV);
        }

    }*/

    /*@Override
    @SideOnly(Side.CLIENT)
    public boolean renderInWorld(IBlockAccess aWorld, int x, int y, int z, Block block, RenderBlocks renderer) {
        Tessellator tes = Tessellator.instance;
        IIcon hre = HyperDimensionalResonanceEvolverField.getIcon();

        if (this.getBaseMetaTileEntity().isActive()) {
            double minU = (double) hre.getMinU();
            double maxU = (double) hre.getMaxU();
            double minV = (double) hre.getMinV();
            double maxV = (double) hre.getMaxV();
            double xBaseOffset = (double) (3 * this.getExtendedFacing()
                .getRelativeBackInWorld().offsetX);
            double zBaseOffset = (double) (3 * this.getExtendedFacing()
                .getRelativeBackInWorld().offsetZ);
            tes.setColorOpaque_F(1.0F, 1.0F, 1.0F);
            tes.setBrightness(15728880);

            this.renderField(
                (double) x + xBaseOffset + 0.5,
                (double) y,
                (double) z + zBaseOffset + 0.5,
                0,
                minU,
                maxU,
                minV,
                maxV);
            this.renderField(
                (double) x + xBaseOffset + 0.5,
                (double) y,
                (double) z + zBaseOffset + 0.5,
                1,
                minU,
                maxU,
                minV,
                maxV);
            this.renderField(
                (double) x + xBaseOffset + 0.5,
                (double) y,
                (double) z + zBaseOffset + 0.5,
                2,
                minU,
                maxU,
                minV,
                maxV);
            this.renderField(
                (double) x + xBaseOffset + 0.5,
                (double) y,
                (double) z + zBaseOffset + 0.5,
                3,
                minU,
                maxU,
                minV,
                maxV);
            this.renderField(
                (double) x + xBaseOffset + 0.5,
                (double) y,
                (double) z + zBaseOffset + 0.5,
                4,
                minU,
                maxU,
                minV,
                maxV);
            this.renderField(
                (double) x + xBaseOffset + 0.5,
                (double) y,
                (double) z + zBaseOffset + 0.5,
                5,
                minU,
                maxU,
                minV,
                maxV);
            this.renderField(
                (double) x + xBaseOffset + 0.5,
                (double) y,
                (double) z + zBaseOffset + 0.5,
                6,
                minU,
                maxU,
                minV,
                maxV);
            this.renderField(
                (double) x + xBaseOffset + 0.5,
                (double) y,
                (double) z + zBaseOffset + 0.5,
                7,
                minU,
                maxU,
                minV,
                maxV);
        }

        return false;
    }
*/
}
