package com.imgood.hyperdimensionaltech.loader;

import com.imgood.hyperdimensionaltech.HyperdimensionalTech;
import com.imgood.hyperdimensionaltech.entity.HT_EntityDimensionalRiftBlade;
import cpw.mods.fml.common.registry.EntityRegistry;

/**
 * @program: Hyperdimensional-Tech
 * @description:
 * @author: Imgood
 * @create: 2024-08-12 14:43
 **/
public class EntityLoader {
    public static void loadEntities()
    {
        int entityID = EntityRegistry.findGlobalUniqueEntityId();
 EntityRegistry.registerModEntity(HT_EntityDimensionalRiftBlade.class, "EnergyBlade", 1, HyperdimensionalTech.instance, 64, 10, true);
    }
}
