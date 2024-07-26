package com.imgood.hyperdimensionaltech.machines.machineaAttributes;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.imgood.hyperdimensionaltech.HyperdimensionalTech;
import com.imgood.hyperdimensionaltech.machines.HT_SingularityUnravelingDevice;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;

/**为什么这个类死掉了呢，因为这样写结构预览总有问题
 * @program: Hyperdimensional-Tech
 * @description: 快速生成StructureDefinition
 * @author: Imgood
 * @create: 2024-07-10 13:56
 **/
@Deprecated
public class HT_StructureDefinitionBuilder<T> {
    class element {
        String casingName;
        int meta;

        public element(String casingName, int meta) {
            this.casingName = casingName;
            this.meta = meta;
        }

        @Override
        public String toString() {
            return "element{" +
                "casingName='" + casingName + '\'' +
                ", meta=" + meta +
                '}';
        }
    }
    char elementChar = 'A';
    String[][] shape;
    String structureName;
    IStructureDefinition<T> STRUCTURE_DEFINITION = null; /*= StructureDefinition.<T>builder().build();*/
    List<element> elements = new ArrayList<>();

    public IStructureDefinition<T> build() {
        StructureDefinition.Builder<T> builder= StructureDefinition.<T>builder();
        builder.addShape(this.structureName, transpose(this.shape));
        for (element element : this.elements) {
            builder.addElement(elementChar++, ofBlock(Objects.requireNonNull(Block.getBlockFromName(element.casingName)), element.meta));
        }
        this.STRUCTURE_DEFINITION = builder.build();
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
    public HT_StructureDefinitionBuilder<T> addElement(String casingName, int meta) {
        this.elements.add(new element(casingName, meta));
        return this;
    }

    @Override
    public String toString() {
        String test = null;
        for(element element : this.elements) {
            test += element.toString();
        }
        return "HT_StructureDefinitionBuilder{" +
            "elementChar=" + elementChar +
            ", shape=" + shape +
            ", structureName='" + structureName + '\'' +
            ", STRUCTURE_DEFINITION=" + STRUCTURE_DEFINITION +
            ", elements=" + test +
            '}';
    }
}
