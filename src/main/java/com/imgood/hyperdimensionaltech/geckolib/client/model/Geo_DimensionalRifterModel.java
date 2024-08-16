package com.imgood.hyperdimensionaltech.geckolib.client.model;

import com.imgood.hyperdimensionaltech.item.DimensionalRifter;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

/**
 * @program: Hyperdimensional-Tech
 * @description:
 * @author: Imgood
 * @create: 2024-08-16 10:13
 **/
public class Geo_DimensionalRifterModel extends AnimatedGeoModel<DimensionalRifter> {

    public Geo_DimensionalRifterModel() {
        super();
    }
    @Override
    public ResourceLocation getModelLocation(DimensionalRifter dimensionalRifter) {
        return new ResourceLocation("geckolib3", "geo/Hyperdimensional Blade.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(DimensionalRifter dimensionalRifter) {
        return new ResourceLocation("geckolib3", "textures/items/HT_Sword.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(DimensionalRifter dimensionalRifter) {
        return new ResourceLocation("geckolib3", "animations/HTSword.animation.json");
    }
}
