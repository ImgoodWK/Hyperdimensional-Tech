package com.imgood.hyperdimensionaltech.geckolib.GeoRender;

import net.geckominecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import javax.swing.text.LayeredHighlighter;

public class EnergyWeaponGeockRender extends GeoLayerRenderer{
    public EnergyWeaponGeockRender(IGeoRenderer rendererIn) {
        super(rendererIn);
    }

    @Override
    public void render(EntityLivingBase entityLivingBase, float v, float v1, float v2, float v3, float v4, float v5, Color color) {

    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }
}
