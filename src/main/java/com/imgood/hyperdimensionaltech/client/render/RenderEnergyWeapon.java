package com.imgood.hyperdimensionaltech.client.render;

import com.imgood.hyperdimensionaltech.item.model.ModelEnergyWeapon;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

/**
 * @program: Hyperdimensional-Tech
 * @description:
 * @author: Imgood
 * @create: 2024-08-12 16:19
 **/
public class RenderEnergyWeapon implements IItemRenderer {
    private ModelEnergyWeapon model;

    public RenderEnergyWeapon() {
        model = new ModelEnergyWeapon();
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return type == ItemRenderType.ENTITY;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        GL11.glPushMatrix();
        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("yourmodid", "textures/items/energyweapon.png"));

        switch(type) {
            case EQUIPPED:
                GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(45F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45F, 0.0F, 0.0F, 1.0F);
                GL11.glTranslatef(0.2F, -0.3F, 0.5F);
                GL11.glScalef(0.5F, 0.5F, 0.5F);
                break;
            case EQUIPPED_FIRST_PERSON:
                GL11.glRotatef(90F, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef(45F, 1.0F, 0.0F, 0.0F);
                GL11.glRotatef(45F, 0.0F, 0.0F, 1.0F);
                GL11.glTranslatef(0.2F, -0.3F, 0.5F);
                GL11.glScalef(0.5F, 0.5F, 0.5F);
                break;
            case INVENTORY:
                GL11.glTranslatef(0F, -0.1F, 0F);
                GL11.glRotatef(180F, 0F, 1F, 0F);
                GL11.glScalef(0.7F, 0.7F, 0.7F);
                break;
            default:
                break;
        }

        model.render(null, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        GL11.glPopMatrix();
    }
}
