package com.imgood.hyperdimensionaltech.machines;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.imgood.hyperdimensionaltech.client.render.HT_TileEntityRenderer_HoloController;
import com.imgood.hyperdimensionaltech.machines.MachineBase.HT_MultiMachineBuilder;
import com.imgood.hyperdimensionaltech.machines.machineaAttributes.HT_MachineConstrucs;
import com.imgood.hyperdimensionaltech.utils.Utils;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GT_HatchElement;
import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_HatchElementBuilder;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Objects;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;

public class HT_UniversalMineralProcessor extends HT_MultiMachineBuilder<HT_UniversalMineralProcessor> {
    private final int horizontalOffSet = 27;
    private final int verticalOffSet = 37;
    private final int depthOffSet = 10;
    @SideOnly(Side.CLIENT)
    private HT_TileEntityRenderer_HoloController renderer;

    public HT_UniversalMineralProcessor(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        this.setConstructorOffSet(27, 37, 10);
        if (FMLCommonHandler.instance().getSide().isClient()) {
            this.renderer = new HT_TileEntityRenderer_HoloController();
        }
    }

    public HT_UniversalMineralProcessor(String aName) {
        super(aName);
    }

    public IStructureDefinition<HT_UniversalMineralProcessor> STRUCTURE_DEFINITION = null;

    @Override
    public IStructureDefinition<HT_UniversalMineralProcessor> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition
                .<HT_UniversalMineralProcessor>builder()
                .addShape(this.mName, transpose(HT_MachineConstrucs.CONSTRUCTOR_HyperdimensionalResonanceEvolver))
                .addElement('A', GT_HatchElementBuilder.<HT_UniversalMineralProcessor>builder()
                    .atLeast(GT_HatchElement.InputBus, GT_HatchElement.OutputBus)
                    .adder(HT_UniversalMineralProcessor::addToMachineList)
                    .casingIndex(12)
                    .dot(2)
                    .buildAndChain(Objects.requireNonNull(Block.getBlockFromName("gregtech:gt.blockcasings")), 12))
                .addElement('B', ofBlock(Objects.requireNonNull(Block.getBlockFromName("gregtech:gt.blockcasings")), 13))
                .addElement('C', ofBlock(Objects.requireNonNull(Block.getBlockFromName("gregtech:gt.blockcasings")), 14))
                .addElement('D', ofBlock(Objects.requireNonNull(Block.getBlockFromName("gregtech:gt.blockcasings5")), 13))
                .addElement('E', ofBlock(Objects.requireNonNull(Block.getBlockFromName("tectech:gt.blockcasingsBA0")), 12))
                .addElement('F', ofBlock(Objects.requireNonNull(Block.getBlockFromName("tectech:gt.blockcasingsTT")), 11))
                .addElement('G', ofBlock(Objects.requireNonNull(Block.getBlockFromName("tectech:gt.spacetime_compression_field_generator")), 8))
                .addElement('H', ofBlock(Objects.requireNonNull(Block.getBlockFromName("tectech:gt.stabilisation_field_generator")), 8))
                .addElement('I', ofBlock(Objects.requireNonNull(Block.getBlockFromName("tectech:gt.time_acceleration_field_generator")), 8))
                .addElement('J', ofBlock(Objects.requireNonNull(Block.getBlockFromName("hyperdimensionaltech:antiBlockFrameless")), 6))
                .addElement('K', ofBlock(Objects.requireNonNull(Block.getBlockFromName("tectech:tile.quantumGlass")), 0))
                .addElement('L', ofBlock(Objects.requireNonNull(Block.getBlockFromName("gregtech:gt.blockmachines")), 10002))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing, int colorIndex, boolean active, boolean redstoneLevel) {
        return null;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        repairMachine();
        if (!checkPiece(mName, this.horizontalOffSet, this.verticalOffSet, this.depthOffSet)) {
            return false;
        }
        this.setCoefficientMultiplier(1 + getExtraCoefficientMultiplierByVoltageTier());
        ItemStack controllerSlot = getControllerSlot();
        this.setIsWirelessMode(controllerSlot != null && controllerSlot.stackSize > 0
            && Utils.metaItemEqual(controllerSlot, ItemList.EnergisedTesseract.get(1)));
        return true;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, this.horizontalOffSet, this.verticalOffSet, this.depthOffSet);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) {
            return -1;
        }
        int result = survivialBuildPiece(mName, stackSize, this.horizontalOffSet, this.verticalOffSet, this.depthOffSet, elementBudget, env, false, true);
        return result;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void render(double x, double y, double z, double feildSizeX, double feildSizeY, double feildSizeZ, TileEntity tile) {
        if (this.renderer == null) {
            this.renderer = new HT_TileEntityRenderer_HoloController();
        }
        String ownerName = this.getBaseMetaTileEntity().getOwnerName();
        int progress = this.getBaseMetaTileEntity().getProgress();
        String name = this.getBaseMetaTileEntity().getInventoryName();
        if (ownerName.contains("BlockRender")) {
            //通过主机所有者名判断是NEI就够预览渲染就渲染所有主机部件并且发光
            this.renderer.renderNoGlowExpect(feildSizeX, feildSizeY, feildSizeZ, x, y, z, tile, "cubeholoscreen", "cubefrontline");
            this.renderer.renderGlow(feildSizeX, feildSizeY, feildSizeZ, x, y, z, tile, "cubeholoscreen", "cubefrontline");
        } else {
            //所有者不是NEI预览渲染就按照主机渲染逻辑渲染
            if (progress != 0) {
                //判断主机meta和progress如果meta正确且在工作状态就渲染所有主机部件并且特定部位发光，同时显示主机文字
                this.renderer.renderNoGlowExpect(feildSizeX, feildSizeY, feildSizeZ, x, y, z, tile, "cubeholoscreen", "cubefrontline");
                this.renderer.renderGlow(feildSizeX, feildSizeY, feildSizeZ, x, y, z, tile, "cubeholoscreen", "cubefrontline");
                this.renderer.drawCenteredString(tile, "进度:" + progress, x, 1.80 + y, z, 0x00ffff);
                this.renderer.drawCenteredString(tile, "所有:" + ownerName, x, 2.00 + y, z, 0x00ffff);
                this.renderer.drawCenteredString(tile, "名称:" + name, x, 2.20 + y, z, 0x00ffff);
                this.renderer.drawCenteredString(tile, "Hyperdimensional Tech", x, 2.40 + y, z, 0x00ffff);
            } else {
                //判断主机meta和progress如果meta正确且未在工作状态就渲染所有主机部件并且特定部位不发光，全息屏及全息字不显示
                this.renderer.renderNoGlowExpect(feildSizeX, feildSizeY, feildSizeZ, x, y, z, tile, "cubeholoscreen", "cubefrontline");
                this.renderer.renderGlow(feildSizeX, feildSizeY, feildSizeZ, x, y, z, tile, "cubefrontline");
            }
        }
    }
}
