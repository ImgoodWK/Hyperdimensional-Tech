package com.imgood.hyperdimensionaltech.loader;

import com.imgood.hyperdimensionaltech.HyperdimensionalTech;
import com.imgood.hyperdimensionaltech.entity.EntityEnergyBlade;
import com.imgood.hyperdimensionaltech.entity.EntitySwordBeam;
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
        EntityRegistry.registerGlobalEntityID(EntitySwordBeam.class, "SwordBeam", entityID);
        EntityRegistry.registerModEntity(EntitySwordBeam.class, "SwordBeam", entityID, HyperdimensionalTech.instance, 64, 1, true);
        EntityRegistry.registerModEntity(EntityEnergyBlade.class, "EnergyBlade", 1, HyperdimensionalTech.instance, 64, 10, true);
    }
}
