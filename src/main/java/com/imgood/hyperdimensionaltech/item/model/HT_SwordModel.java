package com.imgood.hyperdimensionaltech.item.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * @program: Hyperdimensional-Tech
 * @description: HT剑的模型Base
 * @author: Imgood
 * @create: 2024-08-12 09:59
 **/
public class HT_SwordModel extends ModelBase {
    public ModelRenderer blade;
    public ModelRenderer handle;

    public HT_SwordModel() {
        textureWidth = 64;
        textureHeight = 32;

        blade = new ModelRenderer(this, 0, 0);
        blade.addBox(-1F, -14F, -2F, 2, 14, 4);
        blade.setRotationPoint(0F, 0F, 0F);

        handle = new ModelRenderer(this, 0, 18);
        handle.addBox(-1F, 0F, -1F, 2, 6, 2);
        handle.setRotationPoint(0F, 0F, 0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        super.render(entity, f, f1, f2, f3, f4, f5);
        blade.render(f5);
        handle.render(f5);
    }
}
