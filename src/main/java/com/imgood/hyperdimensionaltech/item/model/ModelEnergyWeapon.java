package com.imgood.hyperdimensionaltech.item.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * @program: Hyperdimensional-Tech
 * @description:
 * @author: Imgood
 * @create: 2024-08-12 16:20
 **/
public class ModelEnergyWeapon extends ModelBase {
    private ModelRenderer handle;
    private ModelRenderer blade;

    public ModelEnergyWeapon() {
        textureWidth = 64;
        textureHeight = 32;

        handle = new ModelRenderer(this, 0, 0);
        handle.addBox(-1F, -3F, -1F, 2, 6, 2);
        handle.setRotationPoint(0F, 0F, 0F);

        blade = new ModelRenderer(this, 0, 8);
        blade.addBox(-0.5F, -13F, -0.5F, 1, 10, 1);
        blade.setRotationPoint(0F, 0F, 0F);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        handle.render(f5);
        blade.render(f5);
    }
}
