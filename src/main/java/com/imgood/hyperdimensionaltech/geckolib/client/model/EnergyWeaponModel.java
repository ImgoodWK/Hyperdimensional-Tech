package com.imgood.hyperdimensionaltech.geckolib.client.model;

import com.imgood.hyperdimensionaltech.item.EnergyWeapon;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

/**
 * @program: Hyperdimensional-Tech
 * @description:
 * @author: Imgood
 * @create: 2024-08-16 10:13
 **/
public class EnergyWeaponModel extends AnimatedGeoModel<EnergyWeapon> {

    public EnergyWeaponModel() {
        super();
    }
    @Override
    public ResourceLocation getModelLocation(EnergyWeapon energyWeapon) {
        return new ResourceLocation("geckolib3", "geo/Hyperdimensional Blade.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EnergyWeapon energyWeapon) {
        return new ResourceLocation("geckolib3", "textures/items/HT_Sword.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EnergyWeapon energyWeapon) {
        return new ResourceLocation("geckolib3", "animations/HTSword.animation.json");
    }
}
