package com.imgood.hyperdimensionaltech.machines.machineaAttributes;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.imgood.hyperdimensionaltech.HyperdimensionalTech;
import com.imgood.hyperdimensionaltech.machines.HT_SingularityUnravelingDevice;
import net.minecraft.block.Block;

import java.util.Arrays;
import java.util.Objects;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;

/**
 * @program: Hyperdimensional-Tech
 * @description: 快速生成StructureDefinition
 * @author: Imgood
 * @create: 2024-07-10 13:56
 **/
public class HT_StructureDefinitionBuilder<T> {
    char elementChar = 'A';
    String[][] shape;
    String structureName;
    IStructureDefinition<T> STRUCTURE_DEFINITION; /*= StructureDefinition.<T>builder().build();*/
    public HT_StructureDefinitionBuilder<T> addElement(String block, int meta) {
        HyperdimensionalTech.logger.info("httestmsg"+this);
        STRUCTURE_DEFINITION = StructureDefinition.<T>builder().addElement(elementChar, ofBlock(Objects.requireNonNull(Block.getBlockFromName("gregtech:gt.blockcasings")),13)).build();
        elementChar ++;
        return this;
    }
    public IStructureDefinition<T> build() {
        return this.STRUCTURE_DEFINITION;
    }
    public HT_StructureDefinitionBuilder<T> setStructureName(String structureName) {
        this.structureName = structureName;
        return this;
    }
    public HT_StructureDefinitionBuilder<T> addShape(String[][] shape) {
        this.shape = shape;
        return this;
    }
    @Override
    public String toString() {
        return "HT_StructureDefinitionBuilder{" +
            "elementChar=" + elementChar +
            ", shape=" + Arrays.toString(shape) +
            ", structureName='" + structureName + '\'' +
            ", STRUCTURE_DEFINITION=" + STRUCTURE_DEFINITION +
            '}';
    }
}
