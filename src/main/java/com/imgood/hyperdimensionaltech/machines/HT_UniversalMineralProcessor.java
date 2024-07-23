package com.imgood.hyperdimensionaltech.machines;

import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.imgood.hyperdimensionaltech.HyperdimensionalTech;
import com.imgood.hyperdimensionaltech.block.BasicBlocks;
import com.imgood.hyperdimensionaltech.machines.MachineBase.HT_MultiMachineBuilder;
import com.imgood.hyperdimensionaltech.machines.machineaAttributes.HT_MachineConstrucs;
import com.imgood.hyperdimensionaltech.machines.machineaAttributes.HT_MachineTooltips;
import com.imgood.hyperdimensionaltech.tiles.rendertiles.TileHoloController;
import com.imgood.hyperdimensionaltech.utils.Utils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GT_HatchElement;
import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Objects;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;

public class HT_UniversalMineralProcessor extends HT_MultiMachineBuilder<HT_UniversalMineralProcessor> {
    private final int horizontalOffSet = 27;
    private final int verticalOffSet = 37;
    private final int depthOffSet = 10;

    public HT_UniversalMineralProcessor(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        this.setConstructorOffSet(27, 37, 10);
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
                .addElement('L', ofBlock(Objects.requireNonNull(Block.getBlockFromName("gregtech:gt.blockmachines")),10002))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }
    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        HyperdimensionalTech.logger.warn("testmsgonFirstTick"+this.getExtendedFacing());
        super.onFirstTick(aBaseMetaTileEntity);
        this.setOwnerUUID(aBaseMetaTileEntity.getOwnerUuid());
        int x = this.getBaseMetaTileEntity().getXCoord();
        int y = this.getBaseMetaTileEntity().getYCoord();
        int z = this.getBaseMetaTileEntity().getZCoord();
        this.getBaseMetaTileEntity().getWorld().setTileEntity(x,y - 1,z,new TileHoloController(set(getExtendedFacing())));
        /*switch (getExtendedFacing()){
          case WEST_NORMAL_NONE->  this.getBaseMetaTileEntity().getWorld().setBlock(x, y - 1, z, BasicBlocks.Block_RenderHoloController, 0, 3);
            case NORTH_NORMAL_NONE-> this.getBaseMetaTileEntity().getWorld().setBlock(x, y - 1, z, BasicBlocks.Block_RenderHoloController, 2, 3);
          case SOUTH_NORMAL_NONE-> this.getBaseMetaTileEntity().getWorld().setBlock(x, y - 1, z, BasicBlocks.Block_RenderHoloController, 3, 3);
            default ->  this.getBaseMetaTileEntity().getWorld().setBlock(x, y - 1, z, BasicBlocks.Block_RenderHoloController, 1, 3);
        }*/

    }

    private static int set(ExtendedFacing extendedFacing){
        switch (extendedFacing){
            case WEST_NORMAL_NONE-> {
                return  0;
            }
            case NORTH_NORMAL_NONE-> {return  2;}
            case SOUTH_NORMAL_NONE-> {return  3;}
            default ->  {return  1;}
        }
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing, int colorIndex, boolean active, boolean redstoneLevel) {
        return null;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        HyperdimensionalTech.logger.warn("testmsgonFirstTickin725"+this.horizontalOffSet);
        repairMachine();
        if (!checkPiece(mName, this.horizontalOffSet,this.verticalOffSet,this.depthOffSet)) {
            HyperdimensionalTech.logger.info("httestmsgcheckmachine725" + this.horizontalOffSet);
            return false;
        }
        this.setCoefficientMultiplier(1 + getExtraCoefficientMultiplierByVoltageTier());
        ItemStack controllerSlot = getControllerSlot();
        this.setIsWirelessMode(controllerSlot != null && controllerSlot.stackSize > 0
            && Utils.metaItemEqual(controllerSlot, ItemList.EnergisedTesseract.get(1)));
        HyperdimensionalTech.logger.info("httestmsgcheckmachine" + true);
        return true;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        HyperdimensionalTech.logger.warn("testmsgconstructin725"+this.horizontalOffSet);
        buildPiece(mName, stackSize, hintsOnly, this.horizontalOffSet,this.verticalOffSet,this.depthOffSet);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        HyperdimensionalTech.logger.warn("testmsgsurvivalConstructin725"+this.mName+this.horizontalOffSet);
        if (mMachine) {
            return -1;
        }
        int result = survivialBuildPiece(mName, stackSize,this.horizontalOffSet,this.verticalOffSet,this.depthOffSet, elementBudget, env, false, true);
        return result;
    }
}
