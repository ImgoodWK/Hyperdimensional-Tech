package com.imgood.hyperdimensionaltech.machines;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nonnull;

import com.imgood.hyperdimensionaltech.recipemap.HT_RecipeMap;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.recipe.RecipeMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

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

/**
 * 为了更快速便捷开发机器诞生的快速Base类，只需要传入一堆属性或者别的对象就能正常运作，如有特殊机器请用另一个机器的Base类
 */
public abstract class HT_LiteMultiMachineBase<T extends HT_LiteMultiMachineBase<T>>
    extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<T> implements IConstructable, ISurvivalConstructable {

    /**
     * 如若机器需要有模式切换功能，这里需要给定com.imgood.hyperdimensionaltech.machines.ValueEnum内的Mode_Default_机器名
     */
    private byte mode;
    /**
     *机器内部属性，通过主机的标签来获取机器是否为无线模式
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

    public HT_LiteMultiMachineBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public HT_LiteMultiMachineBase(String aName) {
        super(aName);
    }

    /**
     * 快速开发简单的机器的构造方法，所以参数直接给进去就ok了，在loadMachines直接加进去
     * @param aID 机器的meta值，从10000开始，依次累增，不能重复，开发的时候乱动前面写好的id会导致存档机器出事
     * @param aName “Name”+机器的名称
     * @param aNameRegional HTTextLocalization.Name机器的名称.getStackForm(1)
     * @param aConstructor 机器结构构成的二维String数组，用结构扫描器扫描获得
     * @param aRecipeMap 机器的NEI界面相关，如：nei界面的进度条图标类型，输入输出物品/流体的格子数量，需要给定这个机器对应的Recipemap对象
     */
    public HT_LiteMultiMachineBase(int aID, String aName, String aNameRegional, String[][] aConstructor, RecipeMap aRecipeMap) {
        super(aID, aName, aNameRegional);
    }

    public void repairMachine() {
        this.mHardHammer = true;
        this.mSoftHammer = true;
        this.mScrewdriver = true;
        this.mCrowbar = true;
        this.mSolderingTool = true;
        this.mWrench = true;
    }

    @OverrideOnly
    protected ProcessingLogic createProcessingLogic() {
        return (ProcessingLogic) (new HT_ProcessingLogic() {

            public @NotNull CheckRecipeResult process() {
                this.setEuModifier(HT_LiteMultiMachineBase.this.getEuModifier());
                this.setSpeedBonus(HT_LiteMultiMachineBase.this.getSpeedBonus());
                this.setOverclock(HT_LiteMultiMachineBase.this.isEnablePerfectOverclock() ? 2 : 1, 2);
                return super.process();
            }
        }).setMaxParallelSupplier(this::getLimitedMaxParallel);
    }

    protected abstract boolean isEnablePerfectOverclock();

    @OverrideOnly
    protected float getEuModifier() {
        return 1.0F;
    }

    @OverrideOnly
    protected abstract float getSpeedBonus();

    @OverrideOnly
    protected abstract int getMaxParallelRecipes();

    protected int getLimitedMaxParallel() {
        return this.getMaxParallelRecipes();
    }

    @Nonnull
    public CheckRecipeResult checkProcessing() {
        if (this.processingLogic == null) {
            return this.checkRecipe(this.mInventory[1]) ? CheckRecipeResultRegistry.SUCCESSFUL
                : CheckRecipeResultRegistry.NO_RECIPE;
        } else {
            this.setupProcessingLogic(this.processingLogic);
            CheckRecipeResult result = this.doCheckRecipe();
            result = this.postCheckRecipe(result, this.processingLogic);
            this.updateSlots();
            if (!result.wasSuccessful()) {
                return result;
            } else {
                this.mEfficiency = 10000 - (this.getIdealStatus() - this.getRepairStatus()) * 1000;
                this.mEfficiencyIncrease = 10000;
                this.mMaxProgresstime = this.processingLogic.getDuration();
                this.setEnergyUsage(this.processingLogic);
                this.mOutputItems = this.processingLogic.getOutputItems();
                this.mOutputFluids = this.processingLogic.getOutputFluids();
                return result;
            }
        }
    }

    public ArrayList<ItemStack> getStoredInputsWithoutDualInputHatch() {
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

    public String[] getInfoData() {
        String dSpeed = String.format("%.3f", this.getSpeedBonus() * 100.0F) + "%";
        String dEUMod = String.format("%.3f", this.getEuModifier() * 100.0F) + "%";
        String[] origin = super.getInfoData();
        String[] ret = new String[origin.length + 3];
        System.arraycopy(origin, 0, ret, 0, origin.length);
        ret[origin.length] = EnumChatFormatting.AQUA + HTTextHandler.texter("Parallels", "MachineInfoData.Parallels")
            + ": "
            + EnumChatFormatting.GOLD
            + this.getLimitedMaxParallel();
        ret[origin.length + 1] = EnumChatFormatting.AQUA + HTTextHandler
            .texter("Speed multiplier", "MachineInfoData.SpeedMultiplier") + ": " + EnumChatFormatting.GOLD + dSpeed;
        ret[origin.length + 2] = EnumChatFormatting.AQUA + HTTextHandler
            .texter("EU Modifier", "MachineInfoData.EuModifier") + ": " + EnumChatFormatting.GOLD + dEUMod;
        return ret;
    }

    @Override
    public boolean addToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        return super.addToMachineList(aTileEntity, aBaseCasingIndex)
            || this.addExoticEnergyInputToMachineList(aTileEntity, aBaseCasingIndex);
    }

    public boolean addEnergyHatchOrExoticEnergyHatchToMachineList(IGregTechTileEntity aTileEntity,
                                                                  int aBaseCasingIndex) {
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

    public boolean addEnergyOutput(long aEU) {
        if (aEU <= 0L) {
            return true;
        } else {
            return !this.mDynamoHatches.isEmpty() ? this.addEnergyOutputMultipleDynamos(aEU, true) : false;
        }
    }

    public boolean addEnergyOutputMultipleDynamos(long aEU, boolean aAllowMixedVoltageDynamos) {
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

    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    public boolean doRandomMaintenanceDamage() {
        return true;
    }

    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    public boolean supportsVoidProtection() {
        return true;
    }

    public boolean supportsInputSeparation() {
        return true;
    }

    public boolean supportsBatchMode() {
        return true;
    }

    public boolean supportsSingleRecipeLocking() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public abstract boolean renderInWorld();
}
