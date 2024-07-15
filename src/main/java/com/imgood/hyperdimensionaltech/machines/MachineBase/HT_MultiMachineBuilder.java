package com.imgood.hyperdimensionaltech.machines.MachineBase;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nonnull;


import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.imgood.hyperdimensionaltech.HyperdimensionalTech;

import com.imgood.hyperdimensionaltech.block.BasicBlocks;
import com.imgood.hyperdimensionaltech.machines.HT_HyperdimensionalResonanceEvolver;
import com.imgood.hyperdimensionaltech.machines.HT_SingularityUnravelingDevice;
import com.imgood.hyperdimensionaltech.machines.machineaAttributes.HT_MachineConstrucs;
import com.imgood.hyperdimensionaltech.machines.machineaAttributes.ValueEnum;
import com.imgood.hyperdimensionaltech.utils.HTTextLocalization;

import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.ITexture;
import gregtech.api.recipe.RecipeMap;

import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
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
import org.jetbrains.annotations.ApiStatus.OverrideOnly;
import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.imgood.hyperdimensionaltech.machines.processingLogics.HT_ProcessingLogic;
import com.imgood.hyperdimensionaltech.utils.HTTextHandler;
import com.imgood.hyperdimensionaltech.utils.Utils;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Dynamo;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Muffler;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.GT_Utility.ItemId;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_Hatch_InputBus_ME;
import gregtech.common.tileentities.machines.IDualInputHatch;
import gregtech.common.tileentities.machines.IDualInputInventory;


import static gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap;

/**
 * 为了更快速便捷开发机器诞生的快速Base类，只需要传入一堆属性或者别的对象就能正常运作，如有特殊机器请用另一个机器的Base类
 */
public class HT_MultiMachineBuilder<T extends HT_MultiMachineBuilder<T>>
    extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<T> implements IConstructable, ISurvivalConstructable {

    /**
     * 机器内部属性，用于模式相关功能
     */
    private byte mode;
    /**
     * 默认模式，如若机器需要有模式切换功能，这里需要给定com.imgood.hyperdimensionaltech.machines.ValueEnum内的Mode_Default_机器名
     */
    private byte defaultMode = 0;
    /**
     * 机器内部属性，通过主机的标签来获取机器是否为无线模式
     */
    private boolean isWirelessMode;
    /**
     * 机器内部属性，存储主机放置的玩家UUID
     */
    private UUID ownerUUID;
    /**
     * 机器内部属性，临时存储无限模式耗电
     */
    private long costingWirelessEUTemp = 0;
    /**
     * 大概是超频增幅倍率（我也不知道干嘛的，有时候写机器就是这样，和猜谜似的，以后知道再补充吧
     */
    private int coefficientMultiplier = 1;
    /**
     * 是否开启特效渲染
     */
    private boolean enableRender;
    /**
     * 机器的结构二维数组
     */
    private String[][] constructor;
    /**
     * 机器的recipe map（配方NEI界面
     */
    private RecipeMap recipeMap;
    /**
     * 内部属性，是否渲染
     */
    // 控制渲染状态的变量
    private boolean isRendering = true;

    // 渲染块的偏移量，用于调整渲染位置
    private int renderBlockOffsetX = 0;
    private int renderBlockOffsetY = 0;
    private int renderBlockOffsetZ = 0;

    // 渲染器块
    private Block renderBlock;

    // 水平、垂直和深度偏移量，用于调整结构位置（如果扫描结构设置了偏移量就填在这
    public int horizontalOffSet;
    public int verticalOffSet;
    public int depthOffSet;

    // 结构件主名称，用于标识和区分不同的结构件
    private String STRUCTURE_PIECE_MAIN;


    private boolean enablePerfectOverclock;
    private GT_Multiblock_Tooltip_Builder tooltipBuilder;
    HT_MachineConstrucs machineConstrucs = new HT_MachineConstrucs();
    IStructureDefinition<T> structureDefinition;

    public HT_MultiMachineBuilder(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public HT_MultiMachineBuilder(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<T> getStructureDefinition() {
        return this.structureDefinition;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        return this.getTooltipBuilder();
    }

    /**
     * 快速开发简单的机器的构造方法，所以参数直接给进去就ok了，在loadMachines直接加进去
     * @param aID 机器的meta值，从10000开始，依次累增，不能重复，开发的时候乱动前面写好的id会导致存档机器出事
     * @param aName “Name”+机器的名称
     * @param aNameRegional HTTextLocalization.Name机器的名称.getStackForm(1)
     * @param aConstructor 机器结构构成的二维String数组，用结构扫描器扫描获得，写在HT_MachineConstrucs里
     * @param aRecipeMap 机器的NEI界面相关，如：nei界面的进度条图标类型，输入输出物品/流体的格子数量，需要给定这个机器对应的Recipemap对象
     * @param enableRender 是否开启特效渲染，需要从HTConfigurations获取
     */
    public HT_MultiMachineBuilder(int aID,
                                   String aName,
                                   String aNameRegional,
                                   String[][] aConstructor,
                                   RecipeMap aRecipeMap,
                                   boolean enableRender,
                                   byte defaultMode,
                                   GT_Multiblock_Tooltip_Builder tooltipBuilder,
                                   int renderBlockOffsetX,
                                   int renderBlockOffsetY,
                                   int renderBlockOffsetZ,
                                   Block renderBlock,
                                   boolean isEnablePerfectOverclock,
                                   int horizontalOffSet,
                                   int verticalOffSet,
                                   int depthOffSet
    ) {
        super(aID, aName, aNameRegional);
        this.constructor = aConstructor;
        this.recipeMap = aRecipeMap;
        this.enableRender = enableRender;
        this.defaultMode = defaultMode;
        this.enableRender = enableRender;
        this.tooltipBuilder = tooltipBuilder;
        this.renderBlockOffsetX = renderBlockOffsetX;
        this.renderBlockOffsetY = renderBlockOffsetY;
        this.renderBlockOffsetZ = renderBlockOffsetZ;
        this.renderBlock = renderBlock;
        this.enablePerfectOverclock = isEnablePerfectOverclock;
        this.STRUCTURE_PIECE_MAIN = aName;
        this.horizontalOffSet = machineConstrucs.getOffset(aName)[0];
        this.verticalOffSet = machineConstrucs.getOffset(aName)[1];
        this.depthOffSet = machineConstrucs.getOffset(aName)[2];

    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        //HyperdimensionalTech.logger.warn("testmsggetWailaBody");
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
                    .translateToLocalFormatted(this.mName + ".modeMsg." + tag.getByte("mode"), new Object[0]));
        }
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y, int z) {
        //HyperdimensionalTech.logger.warn("testmsggetWailaBody");
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        final IGregTechTileEntity tileEntity = getBaseMetaTileEntity();
        if (tileEntity != null) {
            tag.setByte("mode", mode);
            tag.setBoolean("isWirelessMode", isWirelessMode);
            tag.setLong("costingWirelessEUTemp", costingWirelessEUTemp);
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        HyperdimensionalTech.logger.warn("testmsgloadNBTData");
        super.loadNBTData(aNBT);
        mode = aNBT.getByte("mode");
        coefficientMultiplier = aNBT.getInteger("coefficientMultiplier");
        isWirelessMode = aNBT.getBoolean("isWirelessMode");
        costingWirelessEUTemp = aNBT.getLong("costingWirelessEUTemp");
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        HyperdimensionalTech.logger.warn("testmsgaveNBTData");
        super.saveNBTData(aNBT);
        mode = aNBT.getByte("mode");
        coefficientMultiplier = aNBT.getInteger("coefficientMultiplier");
        isWirelessMode = aNBT.getBoolean("isWirelessMode");
        costingWirelessEUTemp = aNBT.getLong("costingWirelessEUTemp");
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        HyperdimensionalTech.logger.warn("testmsggetRecipeMap");
        return this.recipeMap;
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        HyperdimensionalTech.logger.warn("testmsgonScrewdriverRightClick");
        if (getBaseMetaTileEntity().isServerSide()) {
            this.mode = (byte) ((this.mode + 1) % 2);
            GT_Utility.sendChatToPlayer(
                aPlayer,
                StatCollector.translateToLocal(this.mName +".modeMsg." + this.mode));
        }
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        HyperdimensionalTech.logger.warn("testmsgsetProcessingLogicPower");
        if (isWirelessMode) {
            // wireless mode ignore voltage limit
            logic.setAvailableVoltage(0);
            logic.setAvailableAmperage(1);
            logic.setAmperageOC(false);
        } else {
            super.setProcessingLogicPower(logic);
        }
    }

    /**
     * 机器的渲染器生成，需要先写好渲染器后给此方法，偏移量的使用自己摸索，偏移量指的是相对于主机
     * @param offsetX X轴偏移量，也就是主机面向你时的左右偏移
     * @param offsetY Y轴偏移量，也就是主机面向你时的上下偏移
     * @param offsetZ Z轴偏移量，也就是主机面向你时的左右偏移
     * @param isRendering True放置渲染器，False清除渲染器
     * @param renderBlock 渲染器直接放这里就行，支持现有的渲染器，如果你想用TST或者GTNH里其他的渲染器你可以找到代码把渲染器传进去
     */
    public void RenderBlock(int offsetX, int offsetY, int offsetZ, boolean isRendering, Block renderBlock) {
        HyperdimensionalTech.logger.warn("testmsgRenderBlock");
        int x = this.getBaseMetaTileEntity().getXCoord();
        int y = this.getBaseMetaTileEntity().getYCoord();
        int z = this.getBaseMetaTileEntity().getZCoord();
        double xOffset = (double)(offsetZ * this.getExtendedFacing().getRelativeBackInWorld().offsetX + offsetY * this.getExtendedFacing().getRelativeUpInWorld().offsetX + offsetX * this.getExtendedFacing().getRelativeLeftInWorld().offsetX);
        double zOffset = (double)(offsetZ * this.getExtendedFacing().getRelativeBackInWorld().offsetZ + offsetY * this.getExtendedFacing().getRelativeUpInWorld().offsetZ + offsetX * this.getExtendedFacing().getRelativeLeftInWorld().offsetZ);
        double yOffset = (double)(offsetZ * this.getExtendedFacing().getRelativeBackInWorld().offsetY + offsetY * this.getExtendedFacing().getRelativeUpInWorld().offsetY + offsetX * this.getExtendedFacing().getRelativeLeftInWorld().offsetY);
        if (isRendering) {
            this.getBaseMetaTileEntity().getWorld().setBlock((int)((double)x + xOffset), (int)((double)y + yOffset), (int)((double)z + zOffset), Blocks.air);
            this.getBaseMetaTileEntity().getWorld().setBlock((int)((double)x + xOffset), (int)((double)y + yOffset), (int)((double)z + zOffset), this.renderBlock);
        }else {
            this.getBaseMetaTileEntity().getWorld().setBlock((int)((double)x + xOffset), (int)((double)y + yOffset), (int)((double)z + zOffset), Blocks.air);
        }
    }
    @Override
    public boolean addToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        HyperdimensionalTech.logger.warn("testmsgaddToMachineList");
        return super.addToMachineList(aTileEntity, aBaseCasingIndex) || this.addExoticEnergyInputToMachineList(aTileEntity, aBaseCasingIndex);
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        HyperdimensionalTech.logger.warn("testmsgonFirstTick");
        super.onFirstTick(aBaseMetaTileEntity);
        this.ownerUUID = aBaseMetaTileEntity.getOwnerUuid();
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing, int colorIndex, boolean active, boolean redstoneLevel) {
        return new ITexture[0];
    }
//施工到这里

    /**
     * 这段直接复制到机器类里
     * @param aBaseMetaTileEntity
     * @param aStack
     * @return
     */
    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        HyperdimensionalTech.logger.warn("testmsgonFirstTickin");
        repairMachine();
        if (!checkPiece(mName, this.horizontalOffSet,this.verticalOffSet,this.depthOffSet)) {
            HyperdimensionalTech.logger.info("httestmsgcheckmachine" + false);
            return false;
        }
        this.setCoefficientMultiplier(1 + getExtraCoefficientMultiplierByVoltageTier());
        ItemStack controllerSlot = getControllerSlot();
        isWirelessMode = controllerSlot != null && controllerSlot.stackSize > 0
            && Utils.metaItemEqual(controllerSlot, ItemList.EnergisedTesseract.get(1));
        HyperdimensionalTech.logger.info("httestmsgcheckmachine" + true);
        return true;
    }
    public int getExtraCoefficientMultiplierByVoltageTier() {
        HyperdimensionalTech.logger.warn("testmsgetExtraCoefficientMultiplierByVoltageTier");
        return (int) Utils.calculatePowerTier(getMaxInputEu());
    }

    /**
     * 这段直接复制到机器类里
     * @param stackSize
     * @param hintsOnly
     */
    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        HyperdimensionalTech.logger.warn("testmsgconstructin");
        buildPiece(mName, stackSize, hintsOnly, this.horizontalOffSet,this.verticalOffSet,this.depthOffSet);
    }

    /**
     *
     * @param stackSize
     * @param elementBudget The server configured element budget. The implementor can choose to tune this up a bit if
     *                      the structure is too big, but generally should not be a 4 digits number to not overwhelm the
     *                      server
     * @param env
     * @return
     */
    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        HyperdimensionalTech.logger.warn("testmsgsurvivalConstructin"+this.mName+this.horizontalOffSet);
        if (mMachine) {
            return -1;
        }
        int result = survivialBuildPiece(mName, stackSize,this.horizontalOffSet,this.verticalOffSet,this.depthOffSet, elementBudget, env, false, true);
        return result;
    }

    public void repairMachine() {
        HyperdimensionalTech.logger.warn("testmsgrepairMachine");
        this.mHardHammer = true;
        this.mSoftHammer = true;
        this.mScrewdriver = true;
        this.mCrowbar = true;
        this.mSolderingTool = true;
        this.mWrench = true;
    }

    /**
     * 还需要施工，这里的内容是实现后自带的
     * @return
     */
    @OverrideOnly
    @Override
    protected ProcessingLogic createProcessingLogic() {
        HyperdimensionalTech.logger.warn("testmsgcreateProcessingLogic");
        return (ProcessingLogic) (new HT_ProcessingLogic() {

            @Override
            public @NotNull CheckRecipeResult process() {
                this.setEuModifier(HT_MultiMachineBuilder.this.getEuModifier());
                this.setSpeedBonus(HT_MultiMachineBuilder.this.getSpeedBonus());
                this.setOverclock(HT_MultiMachineBuilder.this.enablePerfectOverclock ? 2 : 1, 2);
                return super.process();
            }
        }).setMaxParallelSupplier(this::getLimitedMaxParallel);
    }

    /**
     * 通过反射创建实例，每次写机器类的时候就不用override这方法了，自动创建MetaTileEntity
     */
    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        try {
            // 通过反射创建实例
            return this.getClass().getConstructor(String.class).newInstance(this.mName);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create new meta tile entity of " + this.getClass().getSimpleName(), e);
        }
    }

    protected boolean isEnablePerfectOverclock(boolean isEnablePerfectOverclock){
        HyperdimensionalTech.logger.warn("testmsgisEnablePerfectOverclock");
        return isEnablePerfectOverclock;
    }

    protected float getEuModifier() {
        HyperdimensionalTech.logger.warn("testmsggetEuModifier");
        return mode == 0 ? 1 : 1F / coefficientMultiplier;
    }

    protected float getSpeedBonus() {

        HyperdimensionalTech.logger.warn("testmsggetSpeedBonus");
        return mode == 1 ? 1 : 0.5F / coefficientMultiplier;
    }

    protected  int getMaxParallelRecipes() {
        HyperdimensionalTech.logger.warn("testmsggetMaxParallelRecipes1");
        if (isWirelessMode) {
            HyperdimensionalTech.logger.warn("testmsggetMaxParallelRecipes2");
            return Integer.MAX_VALUE;
        }
        int test = mode == 1 ? ValueEnum.Parallel_HighParallelMode_HyperdimensionalResonanceEvolver
            : ValueEnum.Parallel_HighSpeedMode_HyperdimensionalResonanceEvolver;
        HyperdimensionalTech.logger.warn("testmsggetMaxParallelRecipes3"+test);
        return mode == 1 ? ValueEnum.Parallel_HighParallelMode_HyperdimensionalResonanceEvolver
            : ValueEnum.Parallel_HighSpeedMode_HyperdimensionalResonanceEvolver;
    }

    protected int getLimitedMaxParallel() {
        HyperdimensionalTech.logger.warn("testmsggetLimitedMaxParallel");
        return this.getMaxParallelRecipes();
    }

    @Override
    @Nonnull
    public CheckRecipeResult checkProcessing() {
        HyperdimensionalTech.logger.warn("testmsgcheckProcessing");
        boolean flag = false;

        if (!isWirelessMode) {
            if (this.getBaseMetaTileEntity().isActive()) {
                this.isRendering = true;
                if (this.enableRender && this.isRendering) {
                    HyperdimensionalTech.logger.info("testmsgend"+(this.enableRender && !this.isRendering)+this.enableRender+this.isRendering);
                    RenderBlock(this.renderBlockOffsetX, this.renderBlockOffsetY, this.renderBlockOffsetZ, true, BasicBlocks.Block_HRERender);
                }
            }else {
                this.isRendering = false;
                RenderBlock(this.renderBlockOffsetX, this.renderBlockOffsetY, this.renderBlockOffsetZ, false, BasicBlocks.Block_HRERender);;
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

    public ArrayList<ItemStack> getStoredInputsWithoutDualInputHatch() {
        HyperdimensionalTech.logger.warn("testmsggetStoredInputsWithoutDualInputHatch");
        ArrayList<ItemStack> rList = new ArrayList();
        Iterator var2 = ((ArrayList) Utils.filterValidMTEs(this.mInputBusses)).iterator();

        while (var2.hasNext()) {
            GT_MetaTileEntity_Hatch_InputBus tHatch = (GT_MetaTileEntity_Hatch_InputBus) var2.next();
            tHatch.mRecipeMap = this.getRecipeMap();
            IGregTechTileEntity tileEntity = tHatch.getBaseMetaTileEntity();

            for (int i = tileEntity.getSizeInventory() - 1; i >= 0; --i) {
                ItemStack itemStack = tileEntity.getStackInSlot(i);
                if (itemStack != null) {
                    rList.add(itemStack);
                }
            }
        }

        if (this.getStackInSlot(1) != null && this.getStackInSlot(1)
            .getUnlocalizedName()
            .startsWith("gt.integrated_circuit")) {
            rList.add(this.getStackInSlot(1));
        }

        return rList;
    }

    public ArrayList<ItemStack> getStoredInputsNoSeparation() {
        HyperdimensionalTech.logger.warn("testmsggetStoredInputsNoSeparation");
        ArrayList<ItemStack> rList = new ArrayList();
        if (this.supportsCraftingMEBuffer()) {
            Iterator var2 = this.mDualInputHatches.iterator();

            label75: while (var2.hasNext()) {
                IDualInputHatch dualInputHatch = (IDualInputHatch) var2.next();
                Iterator<? extends IDualInputInventory> inventoryIterator = dualInputHatch.inventories();

                while (true) {
                    ItemStack[] items;
                    do {
                        do {
                            if (!inventoryIterator.hasNext()) {
                                continue label75;
                            }

                            items = ((IDualInputInventory) inventoryIterator.next()).getItemInputs();
                        } while (items == null);
                    } while (items.length == 0);

                    for (int i = 0; i < items.length; ++i) {
                        if (items[i] != null) {
                            rList.add(items[i]);
                        }
                    }
                }
            }
        }

        Map<GT_Utility.ItemId, ItemStack> inputsFromME = new HashMap();
        Iterator var10 = ((ArrayList) GT_Utility.filterValidMTEs(this.mInputBusses)).iterator();

        while (var10.hasNext()) {
            GT_MetaTileEntity_Hatch_InputBus tHatch = (GT_MetaTileEntity_Hatch_InputBus) var10.next();
            tHatch.mRecipeMap = this.getRecipeMap();
            IGregTechTileEntity tileEntity = tHatch.getBaseMetaTileEntity();
            boolean isMEBus = tHatch instanceof GT_MetaTileEntity_Hatch_InputBus_ME;

            for (int i = tileEntity.getSizeInventory() - 1; i >= 0; --i) {
                ItemStack itemStack = tileEntity.getStackInSlot(i);
                if (itemStack != null) {
                    if (isMEBus) {
                        inputsFromME.put(ItemId.createNoCopy(itemStack), itemStack);
                    } else {
                        rList.add(itemStack);
                    }
                }
            }
        }

        if (this.getStackInSlot(1) != null && this.getStackInSlot(1)
            .getUnlocalizedName()
            .startsWith("gt.integrated_circuit")) {
            rList.add(this.getStackInSlot(1));
        }

        if (!inputsFromME.isEmpty()) {
            rList.addAll(inputsFromME.values());
        }

        return rList;
    }
    @Override
    public String[] getInfoData() {
        HyperdimensionalTech.logger.warn("testmsggetInfoData");
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


    public boolean addEnergyHatchOrExoticEnergyHatchToMachineList(IGregTechTileEntity aTileEntity,
                                                                  int aBaseCasingIndex) {
        HyperdimensionalTech.logger.warn("testmsgaddEnergyHatchOrExoticEnergyHatchToMachineList");
        return this.addEnergyInputToMachineList(aTileEntity, aBaseCasingIndex)
            || this.addExoticEnergyInputToMachineList(aTileEntity, aBaseCasingIndex);
    }

    public boolean addInputBusOrOutputBusToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        return this.addInputBusToMachineList(aTileEntity, aBaseCasingIndex)
            || this.addOutputBusToMachineList(aTileEntity, aBaseCasingIndex);
    }

    public boolean addInputHatchOrOutputHatchToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        return this.addInputHatchToMachineList(aTileEntity, aBaseCasingIndex)
            || this.addOutputHatchToMachineList(aTileEntity, aBaseCasingIndex);
    }

    public boolean addFluidInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        } else {
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity == null) {
                return false;
            } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
                ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
                ((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity).mRecipeMap = this.getRecipeMap();
                return this.mInputHatches.add((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity);
            } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Muffler) {
                ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
                return this.mMufflerHatches.add((GT_MetaTileEntity_Hatch_Muffler) aMetaTileEntity);
            } else {
                return false;
            }
        }
    }

    @Override
    public boolean addEnergyOutput(long aEU) {
        HyperdimensionalTech.logger.warn("testmsgaddEnergyOutput");
        if (aEU <= 0L) {
            return true;
        } else {
            return !this.mDynamoHatches.isEmpty() ? this.addEnergyOutputMultipleDynamos(aEU, true) : false;
        }
    }

    @Override
    public boolean addEnergyOutputMultipleDynamos(long aEU, boolean aAllowMixedVoltageDynamos) {
        HyperdimensionalTech.logger.warn("testmsgaddEnergyOutputMultipleDynamos");
        int injected = 0;
        long totalOutput = 0L;
        long aFirstVoltageFound = -1L;
        boolean aFoundMixedDynamos = false;

        long leftToInject;
        long aVoltage;
        for (Iterator var10 = ((ArrayList) Utils.filterValidMTEs(this.mDynamoHatches)).iterator(); var10
            .hasNext(); totalOutput += aVoltage) {
            GT_MetaTileEntity_Hatch_Dynamo aDynamo = (GT_MetaTileEntity_Hatch_Dynamo) var10.next();
            leftToInject = aDynamo.maxEUOutput();
            aVoltage = aDynamo.maxAmperesOut() * leftToInject;
            if (aFirstVoltageFound == -1L) {
                aFirstVoltageFound = leftToInject;
            } else if (aFirstVoltageFound != leftToInject) {
                aFoundMixedDynamos = true;
            }
        }

        long actualOutputEU;
        if (totalOutput < aEU) {
            actualOutputEU = totalOutput;
        } else {
            actualOutputEU = aEU;
        }

        Iterator var19 = ((ArrayList) Utils.filterValidMTEs(this.mDynamoHatches)).iterator();

        while (var19.hasNext()) {
            GT_MetaTileEntity_Hatch_Dynamo aDynamo = (GT_MetaTileEntity_Hatch_Dynamo) var19.next();
            leftToInject = actualOutputEU - (long) injected;
            aVoltage = aDynamo.maxEUOutput();
            int aAmpsToInject = (int) (leftToInject / aVoltage);
            int aRemainder = (int) (leftToInject - (long) aAmpsToInject * aVoltage);
            int ampsOnCurrentHatch = (int) Math.min(aDynamo.maxAmperesOut(), (long) aAmpsToInject);

            for (int i = 0; i < ampsOnCurrentHatch; ++i) {
                aDynamo.getBaseMetaTileEntity()
                    .increaseStoredEnergyUnits(aVoltage, false);
            }

            injected = (int) ((long) injected + aVoltage * (long) ampsOnCurrentHatch);
            if (aRemainder > 0 && (long) ampsOnCurrentHatch < aDynamo.maxAmperesOut()) {
                aDynamo.getBaseMetaTileEntity()
                    .increaseStoredEnergyUnits((long) aRemainder, false);
                injected += aRemainder;
            }
        }

        return injected > 0;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        HyperdimensionalTech.logger.warn("testmsgisCorrectMachinePart");
        return true;
    }

    @Override
    public boolean doRandomMaintenanceDamage() {
        HyperdimensionalTech.logger.warn("testmsgdoRandomMaintenanceDamage");
        return true;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        HyperdimensionalTech.logger.warn("testmsggetMaxEfficiency");
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        HyperdimensionalTech.logger.warn("testmsggetDamageToComponent");
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        HyperdimensionalTech.logger.warn("testmsgexplodesOnComponentBreak");
        return false;
    }

    @Override
    public boolean supportsVoidProtection() {
        //HyperdimensionalTech.logger.warn("testmsgsupportsVoidProtection");
        return true;
    }

    @Override
    public boolean supportsInputSeparation() {
        //HyperdimensionalTech.logger.warn("testmsgsupportsInputSeparation");
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        //HyperdimensionalTech.logger.warn("testmsgsupportsBatchMode");
        return true;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        //HyperdimensionalTech.logger.warn("testmsgsupportsSingleRecipeLocking");
        return true;
    }

    public String getStructureName() {
        HyperdimensionalTech.logger.info("httestmsggetStructureName");
        return STRUCTURE_PIECE_MAIN;
    }

    public GT_Multiblock_Tooltip_Builder getTooltipBuilder() {
        HyperdimensionalTech.logger.info("httestmsggetTooltipBuilder");
        return tooltipBuilder;
    }

    public int getHorizontalOffSet() {
        HyperdimensionalTech.logger.info("httestmsgHorizontal"+this.horizontalOffSet);
        return this.horizontalOffSet;
    }

    public int getVerticalOffSet() {
        HyperdimensionalTech.logger.info("httestmsgverticalOffSet"+this.verticalOffSet);
        return this.verticalOffSet;
    }

    public int getDepthOffSet() {
        HyperdimensionalTech.logger.info("httestmsgdepthOffSet"+this.depthOffSet);
        return this.depthOffSet;
    }

    public void setMode(byte mode) {
        this.mode = mode;
    }

    public void setHorizontalOffSet(int horizontalOffSet) {
        this.horizontalOffSet = horizontalOffSet;
    }

    public void setVerticalOffSet(int verticalOffSet) {
        this.verticalOffSet = verticalOffSet;
    }

    public void setDepthOffSet(int depthOffSet) {
        this.depthOffSet = depthOffSet;
    }


    @Override
    public String toString() {
        return "HT_LiteMultiMachineBase{" +
            "mode=" + mode +
            ", defaultMode=" + defaultMode +
            ", isWirelessMode=" + isWirelessMode +
            ", ownerUUID=" + ownerUUID +
            ", costingWirelessEUTemp=" + costingWirelessEUTemp +
            ", coefficientMultiplier=" + coefficientMultiplier +
            ", enableRender=" + enableRender +
            ", constructor=" + Arrays.toString(constructor) +
            ", recipeMap=" + recipeMap +
            ", isRendering=" + isRendering +
            ", renderBlockOffsetX=" + renderBlockOffsetX +
            ", renderBlockOffsetY=" + renderBlockOffsetY +
            ", renderBlockOffsetZ=" + renderBlockOffsetZ +
            ", renderBlock=" + renderBlock +
            ", horizontalOffSet=" + horizontalOffSet +
            ", verticalOffSet=" + verticalOffSet +
            ", depthOffSet=" + depthOffSet +
            ", STRUCTURE_PIECE_MAIN='" + STRUCTURE_PIECE_MAIN + '\'' +
            ", enablePerfectOverclock=" + enablePerfectOverclock +
            ", tooltipBuilder=" + tooltipBuilder +
            '}';
    }

    public int getCoefficientMultiplier() {
        return coefficientMultiplier;
    }

    public void setCoefficientMultiplier(int coefficientMultiplier) {
        HyperdimensionalTech.logger.warn("httestmsgsetCoefficientMultiplier"+this.coefficientMultiplier);
        this.coefficientMultiplier = coefficientMultiplier;
    }

    public boolean getIsWirelessMode() {
        return isWirelessMode;
    }

    public void setIsWirelessMode(boolean wirelessMode) {
        HyperdimensionalTech.logger.warn("httestmsgsetIsWirelessMode"+this.isWirelessMode);
        isWirelessMode = wirelessMode;
    }


    //Builder用
    public HT_MultiMachineBuilder<T> setConstructor(String[][] constructor) {
        this.constructor = constructor;
        return this;
    }

    public HT_MultiMachineBuilder<T> setRecipeMap(RecipeMap<?> recipeMap) {
        this.recipeMap = recipeMap;
        return this;
    }
    public HT_MultiMachineBuilder<T> setRenderBlock(Block renderBlock) {
        this.renderBlock = renderBlock;
        return this;
    }
    public HT_MultiMachineBuilder<T> setRenderBlockOffset(int x, int y, int z) {
        this.renderBlockOffsetX = x;
        this.renderBlockOffsetY = y;
        this.renderBlockOffsetZ = z;
        return this;
    }
    public HT_MultiMachineBuilder<T> setEnablePerfectOverclock(boolean enablePerfectOverclock) {
        this.enablePerfectOverclock = enablePerfectOverclock;
        return this;
    }
    public HT_MultiMachineBuilder<T> setConstructorOffSet(int horizontalOffSet, int verticalOffSet, int depthOffSet) {
        this.horizontalOffSet = horizontalOffSet;
        this.verticalOffSet = verticalOffSet;
        this.depthOffSet = depthOffSet;
        return this;
    }
    public HT_MultiMachineBuilder<T> setTooltipBuilder(GT_Multiblock_Tooltip_Builder tooltipBuilder) {
        this.tooltipBuilder = tooltipBuilder;
        return this;
    }

    public HT_MultiMachineBuilder<T> setEnableRender(boolean enableRender) {
        this.enableRender = enableRender;
        return this;
    }

    public HT_MultiMachineBuilder<T> setDefaultMode(byte defaultMode) {
        this.defaultMode = defaultMode;
        return this;
    }

    public HT_MultiMachineBuilder<T> setStructureDefinition(IStructureDefinition<T> structureDefinition) {
        this.structureDefinition = structureDefinition;
        return this;
    }
}
