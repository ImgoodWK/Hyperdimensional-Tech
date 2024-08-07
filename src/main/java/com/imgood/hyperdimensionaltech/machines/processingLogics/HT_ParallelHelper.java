package com.imgood.hyperdimensionaltech.machines.processingLogics;

import com.imgood.hyperdimensionaltech.utils.HT_ItemID;
import com.imgood.hyperdimensionaltech.utils.Utils;
import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.tileentity.IRecipeLockable;
import gregtech.api.interfaces.tileentity.IVoidable;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.objects.XSTR;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SingleRecipeCheck;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_ParallelHelper;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.VoidProtectionHelper;
import ic2.core.Ic2Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class HT_ParallelHelper extends GT_ParallelHelper {

    private static final double MAX_BATCH_MODE_TICK_TIME = 128.0;
    private IVoidable machine;
    private IRecipeLockable singleRecipeMachine;
    private boolean isRecipeLocked;
    private GT_Recipe recipe;
    private long availableEUt;
    private int currentParallel = 0;
    private int maxParallel = 1;
    private int batchModifier = 1;
    private ItemStack[] itemInputs;
    private ItemStack[] itemOutputs;
    private FluidStack[] fluidInputs;
    private FluidStack[] fluidOutputs;
    private boolean protectExcessItem;
    private boolean protectExcessFluid;
    private boolean consume;
    private boolean batchMode;
    private boolean calculateOutputs;
    private boolean built;
    private double durationMultiplier;
    private float eutModifier = 1.0F;
    private GT_ParallelHelper.MaxParallelCalculator maxParallelCalculator = HT_ParallelHelper::maxParallelCalculatedByInputs;
    private GT_ParallelHelper.InputConsumer inputConsumer = HT_ParallelHelper::consumeInput;
    private GT_OverclockCalculator calculator;
    private CheckRecipeResult result;
    private Function<Integer, ItemStack[]> customItemOutputCalculation;
    private Function<Integer, FluidStack[]> customFluidOutputCalculation;

    public HT_ParallelHelper() {
        this.result = CheckRecipeResultRegistry.NONE;
    }

    /**
     * @deprecated
     */
    @Deprecated
    public GT_ParallelHelper setController(GT_MetaTileEntity_MultiBlockBase machineMeta) {
        return this.setMachine(machineMeta, machineMeta.protectsExcessItem(), machineMeta.protectsExcessFluid());
    }

    /**
     * @deprecated
     */
    @Deprecated
    public GT_ParallelHelper setController(GT_MetaTileEntity_MultiBlockBase machineMeta, boolean protectExcessItem,
                                           boolean protectExcessFluid) {
        return this.setMachine(machineMeta, protectExcessItem, protectExcessFluid);
    }

    public GT_ParallelHelper setMachine(IVoidable machine) {
        return this.setMachine(machine, machine.protectsExcessItem(), machine.protectsExcessFluid());
    }

    public GT_ParallelHelper setMachine(IVoidable machine, boolean protectExcessItem, boolean protectExcessFluid) {
        this.protectExcessItem = protectExcessItem;
        this.protectExcessFluid = protectExcessFluid;
        this.machine = machine;
        return this;
    }

    public GT_ParallelHelper setRecipe(@Nonnull GT_Recipe aRecipe) {
        this.recipe = (GT_Recipe) Objects.requireNonNull(aRecipe);
        return this;
    }

    public GT_ParallelHelper setRecipeLocked(IRecipeLockable singleRecipeMachine, boolean isRecipeLocked) {
        this.singleRecipeMachine = singleRecipeMachine;
        this.isRecipeLocked = isRecipeLocked;
        return this;
    }

    public GT_ParallelHelper setItemInputs(ItemStack... aItemInputs) {
        this.itemInputs = aItemInputs;
        return this;
    }

    public GT_ParallelHelper setFluidInputs(FluidStack... aFluidInputs) {
        this.fluidInputs = aFluidInputs;
        return this;
    }

    public GT_ParallelHelper setAvailableEUt(long aAvailableEUt) {
        this.availableEUt = aAvailableEUt;
        return this;
    }

    public GT_ParallelHelper setEUtModifier(float aEUtModifier) {
        this.eutModifier = aEUtModifier;
        return this;
    }

    public GT_ParallelHelper setCalculator(GT_OverclockCalculator calculator) {
        this.calculator = calculator;
        return this;
    }

    /**
     * @deprecated
     */
    @Deprecated
    public GT_ParallelHelper enableConsumption() {
        return this.setConsumption(true);
    }

    public GT_ParallelHelper setConsumption(boolean consume) {
        this.consume = consume;
        return this;
    }

    public GT_ParallelHelper setMaxParallel(int maxParallel) {
        this.maxParallel = maxParallel;
        return this;
    }

    public GT_ParallelHelper enableBatchMode(int batchModifier) {
        this.batchMode = batchModifier > 1;
        this.batchModifier = batchModifier;
        return this;
    }

    /**
     * @deprecated
     */
    @Deprecated
    public GT_ParallelHelper enableOutputCalculation() {
        return this.setOutputCalculation(true);
    }

    public GT_ParallelHelper setOutputCalculation(boolean calculateOutputs) {
        this.calculateOutputs = calculateOutputs;
        return this;
    }

    public GT_ParallelHelper setCustomItemOutputCalculation(Function<Integer, ItemStack[]> custom) {
        this.customItemOutputCalculation = custom;
        return this;
    }

    public GT_ParallelHelper setCustomFluidOutputCalculation(Function<Integer, FluidStack[]> custom) {
        this.customFluidOutputCalculation = custom;
        return this;
    }

    public GT_ParallelHelper build() {
        if (this.built) {
            throw new IllegalStateException("Tried to build twice");
        } else if (this.recipe == null) {
            throw new IllegalStateException("Recipe is not set");
        } else {
            this.built = true;
            this.determineParallel();
            return this;
        }
    }

    public int getCurrentParallel() {
        if (!this.built) {
            throw new IllegalStateException("Tried to get parallels before building");
        } else {
            return this.currentParallel;
        }
    }

    public double getDurationMultiplierDouble() {
        if (!this.built) {
            throw new IllegalStateException("Tried to get duration multiplier before building");
        } else {
            return this.batchMode && this.durationMultiplier > 0.0 ? this.durationMultiplier : 1.0;
        }
    }

    /**
     * @deprecated
     */
    @Deprecated
    public float getDurationMultiplier() {
        return (float) this.getDurationMultiplierDouble();
    }

    @Nonnull
    public ItemStack[] getItemOutputs() {
        if (this.built && this.calculateOutputs) {
            return this.itemOutputs;
        } else {
            throw new IllegalStateException(
                "Tried to get item outputs before building or without enabling calculation of outputs");
        }
    }

    @Nonnull
    public FluidStack[] getFluidOutputs() {
        if (this.built && this.calculateOutputs) {
            return this.fluidOutputs;
        } else {
            throw new IllegalStateException(
                "Tried to get fluid outputs before building or without enabling calculation of outputs");
        }
    }

    @Nonnull
    public CheckRecipeResult getResult() {
        if (!this.built) {
            throw new IllegalStateException("Tried to get recipe result before building");
        } else {
            return this.result;
        }
    }

    /**
     * @deprecated
     */
    @Deprecated
    protected boolean tryConsumeRecipeInputs(GT_Recipe recipe, FluidStack[] fluids, ItemStack[] items) {
        return this.tryConsumeRecipeInputs(recipe, fluids, items, 1);
    }

    protected boolean tryConsumeRecipeInputs(GT_Recipe recipe, FluidStack[] fluids, ItemStack[] items,
                                             int minParallel) {
        return recipe.isRecipeInputEqual(true, false, minParallel, fluids, items);
    }

    protected void determineParallel() {
        if (this.itemInputs == null) {
            this.itemInputs = new ItemStack[0];
        }

        if (this.fluidInputs == null) {
            this.fluidInputs = new FluidStack[0];
        }

        if (!this.consume) {
            this.copyInputs();
        }

        if (this.calculator == null) {
            this.calculator = (new GT_OverclockCalculator()).setEUt(this.availableEUt)
                .setRecipeEUt((long) this.recipe.mEUt)
                .setDuration(this.recipe.mDuration)
                .setEUtDiscount(this.eutModifier);
        }

        int tRecipeEUt = (int) Math.ceil((double) ((float) this.recipe.mEUt * this.eutModifier));
        if (this.availableEUt < (long) tRecipeEUt) {
            this.result = CheckRecipeResultRegistry.insufficientPower((long) tRecipeEUt);
        } else {
            int originalMaxParallel = this.maxParallel;
            double tickTimeAfterOC = this.calculator.setParallel(originalMaxParallel)
                .calculateDurationUnderOneTick();
            if (tickTimeAfterOC < 1.0) {
                this.maxParallel = Utils.safeInt((long) ((double) this.maxParallel / tickTimeAfterOC), 1);
            }

            int maxParallelBeforeBatchMode = this.maxParallel;
            if (this.batchMode) {
                this.maxParallel = Utils.safeInt((long) this.maxParallel * (long) this.batchModifier, 1);
            }

            ItemStack[] truncatedItemOutputs = this.recipe.mOutputs != null ? (ItemStack[]) Arrays.copyOfRange(
                this.recipe.mOutputs,
                0,
                Math.min(this.machine.getItemOutputLimit(), this.recipe.mOutputs.length)) : new ItemStack[0];
            FluidStack[] truncatedFluidOutputs = this.recipe.mFluidOutputs != null ? (FluidStack[]) Arrays.copyOfRange(
                this.recipe.mFluidOutputs,
                0,
                Math.min(this.machine.getFluidOutputLimit(), this.recipe.mFluidOutputs.length)) : new FluidStack[0];
            SingleRecipeCheck recipeCheck = null;
            SingleRecipeCheck.Builder tSingleRecipeCheckBuilder = null;
            if (this.isRecipeLocked && this.singleRecipeMachine != null) {
                recipeCheck = this.singleRecipeMachine.getSingleRecipeCheck();
                if (recipeCheck == null) {
                    RecipeMap<?> recipeMap = this.singleRecipeMachine.getRecipeMap();
                    if (recipeMap != null) {
                        tSingleRecipeCheckBuilder = SingleRecipeCheck.builder(recipeMap)
                            .setBefore(this.itemInputs, this.fluidInputs);
                    }
                }
            }

            if (this.protectExcessItem || this.protectExcessFluid) {
                if (this.machine == null) {
                    throw new IllegalStateException("Tried to calculate void protection, but machine is not set");
                }

                VoidProtectionHelper voidProtectionHelper = new VoidProtectionHelper();
                voidProtectionHelper.setMachine(this.machine)
                    .setItemOutputs(truncatedItemOutputs)
                    .setFluidOutputs(truncatedFluidOutputs)
                    .setMaxParallel(this.maxParallel)
                    .build();
                this.maxParallel = Math.min(voidProtectionHelper.getMaxParallel(), this.maxParallel);
                if (this.maxParallel <= 0) {
                    this.result = CheckRecipeResultRegistry.ITEM_OUTPUT_FULL;
                    return;
                }
            }

            maxParallelBeforeBatchMode = Math.min(this.maxParallel, maxParallelBeforeBatchMode);
            int actualMaxParallel = tRecipeEUt > 0
                ? (int) Math.min((long) maxParallelBeforeBatchMode, this.availableEUt / (long) tRecipeEUt)
                : maxParallelBeforeBatchMode;
            if (recipeCheck != null) {
                this.currentParallel = recipeCheck
                    .checkRecipeInputs(true, actualMaxParallel, this.itemInputs, this.fluidInputs);
            } else {
                this.currentParallel = (int) this.maxParallelCalculator
                    .calculate(this.recipe, actualMaxParallel, this.fluidInputs, this.itemInputs);
                if (this.currentParallel > 0) {
                    if (tSingleRecipeCheckBuilder != null) {
                        this.inputConsumer.consume(this.recipe, 1, this.fluidInputs, this.itemInputs);
                        SingleRecipeCheck builtCheck = tSingleRecipeCheckBuilder
                            .setAfter(this.itemInputs, this.fluidInputs)
                            .setRecipe(this.recipe)
                            .build();
                        this.singleRecipeMachine.setSingleRecipeCheck(builtCheck);
                        this.inputConsumer
                            .consume(this.recipe, this.currentParallel - 1, this.fluidInputs, this.itemInputs);
                    } else {
                        this.inputConsumer
                            .consume(this.recipe, this.currentParallel, this.fluidInputs, this.itemInputs);
                    }
                }
            }

            if (this.currentParallel <= 0) {
                this.result = CheckRecipeResultRegistry.INTERNAL_ERROR;
            } else {
                long eutUseAfterOC = this.calculator
                    .calculateEUtConsumptionUnderOneTick(originalMaxParallel, this.currentParallel);
                this.calculator.setParallel(Math.min(this.currentParallel, originalMaxParallel))
                    .calculate();
                if (this.currentParallel > originalMaxParallel) {
                    this.calculator.setRecipeEUt(eutUseAfterOC);
                }

                if (this.batchMode && this.currentParallel > 0 && (double) this.calculator.getDuration() < 128.0) {
                    double batchMultiplierMax = 128.0 / (double) this.calculator.getDuration();
                    int maxExtraParallels = (int) Math.floor(
                        Math.min(
                            (double) this.currentParallel
                                * Math.min(batchMultiplierMax - 1.0, (double) (this.batchModifier - 1)),
                            (double) (this.maxParallel - this.currentParallel)));
                    int tExtraParallels;
                    if (recipeCheck != null) {
                        tExtraParallels = recipeCheck
                            .checkRecipeInputs(true, maxExtraParallels, this.itemInputs, this.fluidInputs);
                    } else {
                        tExtraParallels = (int) this.maxParallelCalculator
                            .calculate(this.recipe, maxExtraParallels, this.fluidInputs, this.itemInputs);
                        this.inputConsumer.consume(this.recipe, tExtraParallels, this.fluidInputs, this.itemInputs);
                    }

                    this.durationMultiplier = (double) (1.0F + (float) tExtraParallels / (float) this.currentParallel);
                    this.currentParallel += tExtraParallels;
                }

                if (this.calculateOutputs && this.currentParallel > 0) {
                    if (this.recipe.mOutputs != null) {
                        this.calculateItemOutputs();
                    }

                    if (this.recipe.mFluidOutputs != null) {
                        this.calculateFluidOutputs();
                    }
                }

                this.result = CheckRecipeResultRegistry.SUCCESSFUL;
            }
        }
    }

    protected void copyInputs() {
        ItemStack[] itemInputsToUse = new ItemStack[this.itemInputs.length];

        int i;
        for (i = 0; i < this.itemInputs.length; ++i) {
            itemInputsToUse[i] = this.itemInputs[i].copy();
        }

        FluidStack[] fluidInputsToUse = new FluidStack[this.fluidInputs.length];

        for (i = 0; i < this.fluidInputs.length; ++i) {
            fluidInputsToUse[i] = this.fluidInputs[i].copy();
        }

        this.itemInputs = itemInputsToUse;
        this.fluidInputs = fluidInputsToUse;
    }

    protected void calculateItemOutputs() {
        if (this.customItemOutputCalculation != null) {
            this.itemOutputs = (ItemStack[]) this.customItemOutputCalculation.apply(this.currentParallel);
        } else {
            ArrayList<ItemStack> tempItemStack = new ArrayList();

            for (int i = 0; i < this.recipe.mOutputs.length; ++i) {
                long items = 0L;
                long remain = 0L;
                int itemStackSize = this.recipe.getOutput(i).stackSize;
                items = (long) this.currentParallel * (long) itemStackSize
                    * (long) this.recipe.getOutputChance(i)
                    / 10000L;
                remain = (long) this.currentParallel * (long) itemStackSize
                    * (long) this.recipe.getOutputChance(i)
                    % 10000L;
                if (remain > (long) XSTR.XSTR_INSTANCE.nextInt(10000)) {
                    items += (long) itemStackSize;
                }

                ItemStack origin;
                ItemStack item;
                for (origin = this.recipe.getOutput(i)
                    .copy(); items >= 2147483647L; items -= 2147483647L) {
                    item = origin.copy();
                    item.stackSize = Integer.MAX_VALUE;
                    tempItemStack.add(item);
                }

                item = origin.copy();
                item.stackSize = (int) items;
                tempItemStack.add(item);
            }

            this.itemOutputs = (ItemStack[]) tempItemStack.toArray(new ItemStack[0]);
        }
    }

    protected void calculateFluidOutputs() {
        if (this.customFluidOutputCalculation != null) {
            this.fluidOutputs = (FluidStack[]) this.customFluidOutputCalculation.apply(this.currentParallel);
        } else {
            ArrayList<FluidStack> tempFluidStack = new ArrayList();
            this.fluidOutputs = new FluidStack[this.recipe.mFluidOutputs.length];

            for (int i = 0; i < this.recipe.mFluidOutputs.length; ++i) {
                if (this.recipe.getFluidOutput(i) != null) {
                    FluidStack tFluid = this.recipe.getFluidOutput(i)
                        .copy();

                    long amount;
                    FluidStack fluid;
                    for (amount = (long) tFluid.amount * (long) this.currentParallel; amount
                        >= 2147483647L; amount -= 2147483647L) {
                        fluid = tFluid.copy();
                        fluid.amount = Integer.MAX_VALUE;
                        tempFluidStack.add(fluid);
                    }

                    fluid = tFluid.copy();
                    fluid.amount = (int) amount;
                    tempFluidStack.add(fluid);
                }
            }

            this.fluidOutputs = (FluidStack[]) tempFluidStack.toArray(new FluidStack[0]);
        }
    }

    public static void consumeInput(GT_Recipe recipe, int amountMultiplier, FluidStack[] aFluidInputs,
                                    ItemStack... aInputs) {
        if (amountMultiplier > 0) {
            long remainingCost;
            int var7;
            int var8;
            int var12;
            if (aFluidInputs != null) {
                FluidStack[] var6 = recipe.mFluidInputs;
                var7 = var6.length;

                for (var8 = 0; var8 < var7; ++var8) {
                    FluidStack recipeFluidCost = var6[var8];
                    if (recipeFluidCost != null) {
                        remainingCost = (long) recipeFluidCost.amount * (long) amountMultiplier;
                        FluidStack[] var10 = aFluidInputs;
                        int var11 = aFluidInputs.length;

                        for (var12 = 0; var12 < var11; ++var12) {
                            FluidStack providedFluid = var10[var12];
                            if (providedFluid != null && providedFluid.isFluidEqual(recipeFluidCost)) {
                                if ((long) providedFluid.amount >= remainingCost) {
                                    providedFluid.amount -= (int) remainingCost;
                                    break;
                                }

                                remainingCost -= (long) providedFluid.amount;
                                providedFluid.amount = 0;
                            }
                        }
                    }
                }
            }

            if (aInputs != null) {
                ItemStack[] var15 = recipe.mInputs;
                var7 = var15.length;

                for (var8 = 0; var8 < var7; ++var8) {
                    ItemStack recipeItemCost = var15[var8];
                    if (recipeItemCost.stackSize != 0) {
                        ItemStack unifiedItemCost = GT_OreDictUnificator.get_nocopy(recipeItemCost);
                        if (unifiedItemCost != null) {
                            remainingCost = (long) recipeItemCost.stackSize * (long) amountMultiplier;
                            ItemStack[] var18 = aInputs;
                            var12 = aInputs.length;

                            for (int var19 = 0; var19 < var12; ++var19) {
                                ItemStack providedItem = var18[var19];
                                if ((!recipe.isNBTSensitive
                                    || GT_Utility.areStacksEqual(providedItem, unifiedItemCost, false))
                                    && (recipe.isNBTSensitive
                                    || GT_OreDictUnificator.isInputStackEqual(providedItem, unifiedItemCost))
                                    && (!GT_Recipe.GTppRecipeHelper
                                    || !GT_Utility.areStacksEqual(providedItem, Ic2Items.FluidCell.copy(), true)
                                    && !GT_Utility.areStacksEqual(
                                    providedItem,
                                    ItemList.Tool_DataStick.get(1L, new Object[0]),
                                    true)
                                    && !GT_Utility.areStacksEqual(
                                    providedItem,
                                    ItemList.Tool_DataOrb.get(1L, new Object[0]),
                                    true)
                                    || GT_Utility.areStacksEqual(providedItem, recipeItemCost, false))) {
                                    if ((long) providedItem.stackSize >= remainingCost) {
                                        providedItem.stackSize -= (int) remainingCost;
                                        break;
                                    }

                                    remainingCost -= (long) providedItem.stackSize;
                                    providedItem.stackSize = 0;
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    public static double maxParallelCalculatedByInputs(GT_Recipe recipe, int maxParallel, FluidStack[] aFluidInputs,
                                                       ItemStack... aInputs) {
        if (recipe.mInputs.length > 0 && aInputs == null) {
            return 0.0;
        } else if (recipe.mFluidInputs.length > 0 && aFluidInputs == null) {
            return 0.0;
        } else {
            double currentParallel = (double) maxParallel;
            int providedAmount;
            if (recipe.mFluidInputs.length > 0) {
                Map<Fluid, Long> fluidMap = new HashMap();
                Map<Fluid, Long> fluidCost = new HashMap();
                FluidStack[] var8 = aFluidInputs;
                providedAmount = aFluidInputs.length;

                int var10;
                FluidStack fluidStack;
                for (var10 = 0; var10 < providedAmount; ++var10) {
                    fluidStack = var8[var10];
                    if (fluidStack != null) {
                        fluidMap.merge(fluidStack.getFluid(), (long) fluidStack.amount, Long::sum);
                    }
                }

                var8 = recipe.mFluidInputs;
                providedAmount = var8.length;

                for (var10 = 0; var10 < providedAmount; ++var10) {
                    fluidStack = var8[var10];
                    if (fluidStack != null) {
                        fluidCost.merge(fluidStack.getFluid(), (long) fluidStack.amount, Long::sum);
                    }
                }

                Iterator var20 = fluidCost.entrySet()
                    .iterator();

                while (var20.hasNext()) {
                    Map.Entry<Fluid, Long> costEntry = (Map.Entry) var20.next();
                    if ((Long) costEntry.getValue() > 0L) {
                        currentParallel = Math.min(
                            currentParallel,
                            (double) (Long) fluidMap.getOrDefault(costEntry.getKey(), 0L)
                                / (double) (Long) costEntry.getValue());
                    }

                    if (currentParallel <= 0.0) {
                        return 0.0;
                    }
                }
            }

            boolean isNBTSensitive = recipe.isNBTSensitive;
            if (recipe.mInputs.length > 0) {
                Map<HT_ItemID, Long> itemCost = new HashMap();
                int var12;
                int var13;
                ItemStack recipeItemCost;
                ItemStack[] var23;
                if (isNBTSensitive) {
                    var23 = recipe.mInputs;
                    var12 = var23.length;

                    for (var13 = 0; var13 < var12; ++var13) {
                        recipeItemCost = var23[var13];
                        if (recipeItemCost.stackSize != 0) {
                            itemCost
                                .merge(HT_ItemID.create(recipeItemCost), (long) recipeItemCost.stackSize, Long::sum);
                        }
                    }
                } else {
                    var23 = recipe.mInputs;
                    var12 = var23.length;

                    for (var13 = 0; var13 < var12; ++var13) {
                        recipeItemCost = var23[var13];
                        if (recipeItemCost.stackSize != 0) {
                            itemCost.merge(
                                HT_ItemID.createNoNBT(recipeItemCost),
                                (long) recipeItemCost.stackSize,
                                Long::sum);
                        }
                    }
                }

                Iterator var24 = itemCost.entrySet()
                    .iterator();

                while (true) {
                    label100:
                    while (var24.hasNext()) {
                        Map.Entry<HT_ItemID, Long> itemID = (Map.Entry) var24.next();
                        ItemStack unifiedItemCost;
                        if (isNBTSensitive) {
                            unifiedItemCost = GT_OreDictUnificator
                                .get_nocopy(((HT_ItemID) itemID.getKey()).getItemStackWithNBT());
                        } else {
                            unifiedItemCost = GT_OreDictUnificator
                                .get_nocopy(((HT_ItemID) itemID.getKey()).getItemStack());
                        }

                        double remainingCost = (double) (Long) itemID.getValue() * currentParallel;
                        providedAmount = 0;
                        ItemStack[] var27 = aInputs;
                        int var15 = aInputs.length;

                        for (int var16 = 0; var16 < var15; ++var16) {
                            ItemStack providedItem = var27[var16];
                            if ((!isNBTSensitive || GT_Utility.areStacksEqual(providedItem, unifiedItemCost, false))
                                && (isNBTSensitive
                                || GT_OreDictUnificator.isInputStackEqual(providedItem, unifiedItemCost))) {
                                providedAmount += providedItem.stackSize;
                                if ((double) providedAmount >= remainingCost) {
                                    continue label100;
                                }
                            }
                        }

                        if (providedAmount == 0) {
                            return 0.0;
                        }

                        currentParallel = Math
                            .min(currentParallel, (double) providedAmount / (double) (Long) itemID.getValue());
                    }

                    return currentParallel;
                }
            } else {
                return currentParallel;
            }
        }
    }
}
