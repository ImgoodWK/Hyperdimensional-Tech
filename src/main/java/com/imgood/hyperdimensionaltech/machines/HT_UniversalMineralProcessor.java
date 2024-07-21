package com.imgood.hyperdimensionaltech.machines;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.imgood.hyperdimensionaltech.HyperdimensionalTech;
import com.imgood.hyperdimensionaltech.block.BasicBlocks;
import com.imgood.hyperdimensionaltech.machines.MachineBase.HT_MultiMachineBuilder;
import com.imgood.hyperdimensionaltech.machines.machineaAttributes.HT_MachineConstrucs;
import com.imgood.hyperdimensionaltech.machines.machineaAttributes.HT_MachineTooltips;
import com.imgood.hyperdimensionaltech.tiles.rendertiles.TileHoloController;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GT_HatchElement;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GT_HatchElementBuilder;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Objects;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;

public class HT_UniversalMineralProcessor extends HT_MultiMachineBuilder<HT_UniversalMineralProcessor> {

    public HT_UniversalMineralProcessor(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
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
                .addElement('L', ofBlock(Objects.requireNonNull(Block.getBlockFromName("gregtech:gt.blockmachines")),10000))
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
        double xOffset = (double)(0 * this.getExtendedFacing().getRelativeBackInWorld().offsetX + -1 * this.getExtendedFacing().getRelativeUpInWorld().offsetX + 0 * this.getExtendedFacing().getRelativeLeftInWorld().offsetX);
        double zOffset = (double)(0 * this.getExtendedFacing().getRelativeBackInWorld().offsetZ + -1 * this.getExtendedFacing().getRelativeUpInWorld().offsetZ + 0 * this.getExtendedFacing().getRelativeLeftInWorld().offsetZ);
        double yOffset = (double)(0 * this.getExtendedFacing().getRelativeBackInWorld().offsetY + -1 * this.getExtendedFacing().getRelativeUpInWorld().offsetY + 0 * this.getExtendedFacing().getRelativeLeftInWorld().offsetY);
        this.getBaseMetaTileEntity().getWorld().setBlock((int)((double)x + xOffset), (int)((double)y + yOffset), (int)((double)z + zOffset), Blocks.air);
        this.getBaseMetaTileEntity().getWorld().setBlock((int)((double)x + xOffset), (int)((double)y + yOffset), (int)((double)z + zOffset), BasicBlocks.Block_RenderHoloController);
        TileHoloController tile =
            (TileHoloController) getBaseMetaTileEntity().getWorld().getTileEntity((int)((double)x + xOffset), (int)((double)y + yOffset), (int)((double)z + zOffset));
        tile.setFacing(this.getExtendedFacing());
    }
}
