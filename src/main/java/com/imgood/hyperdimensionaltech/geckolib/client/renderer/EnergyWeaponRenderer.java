package com.imgood.hyperdimensionaltech.geckolib.client.renderer;

import com.imgood.hyperdimensionaltech.geckolib.client.model.EnergyWeaponModel;
import com.imgood.hyperdimensionaltech.item.EnergyWeapon;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

/**
 * @program: Hyperdimensional-Tech
 * @description:
 * @author: Imgood
 * @create: 2024-08-16 11:01
 **/
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//


public class EnergyWeaponRenderer extends GeoItemRenderer<EnergyWeapon> {
    public EnergyWeaponRenderer() {
        super(new EnergyWeaponModel());
    }

    /*@Override
    public void render(GeoModel model, EnergyWeapon animatable, float partialTicks, float red, float green, float blue, float alpha) {

        GL11.glPushMatrix();
        GL11.glTranslated(0, -0.2, 0);
        GL11.glRotated(30, 0.0, 1.0, 0.0);
        GL11.glScaled(1.1, 1.1, 1.1);
        super.render(model, animatable, partialTicks, red, green, blue, alpha);
        GL11.glPopMatrix();
    }*/
   /* @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        switch (type) {
            case EQUIPPED_FIRST_PERSON:
                GL11.glPushMatrix();
                GL11.glTranslated(1, 0, 1);
                GL11.glRotated(65, 0.0, 1.0, 0.0);
                GL11.glScaled(2, 2, 2);
                super.renderItem(type, item, data);
                GL11.glPopMatrix();
                break;
        }
    }*/
    @Override
    public void renderItem(IItemRenderer.ItemRenderType var1, ItemStack itemStack, Object... var3) {
        GL11.glPushMatrix();
        if (var1 == ItemRenderType.INVENTORY) {
            GL11.glTranslated(-1.0, -   1, 0.0);
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
            GL11.glTranslated(0.5, 0.5, 0.5);
        }

        if (var1 == ItemRenderType.EQUIPPED_FIRST_PERSON) {
            GL11.glTranslated(-0, -1.2, 1);
            GL11.glRotated(60, 0.0, 1.0, 0.0);
            GL11.glScaled(2, 2, 2);
        }

        if (var1 == ItemRenderType.ENTITY) {
        }

        this.render((EnergyWeapon) itemStack.getItem(), itemStack);
        GL11.glPopMatrix();
    }
}
