package com.imgood.hyperdimensionaltech.client.render;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import static com.imgood.hyperdimensionaltech.utils.Enums.Names.MOD_ID;

/**
 * @program: Hyperdimensional-Tech
 * @description:
 * @author: Imgood
 * @create: 2024-08-12 16:35
 **/
public class RenderEnergyBlade extends Render {
    private static final ResourceLocation texture = new ResourceLocation(MOD_ID, "textures/gui/ht_Sword_EnergyBar.png");

    public RenderEnergyBlade() {
        super();
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTicks) {
        GL11.glPushMatrix();
        this.bindEntityTexture(entity);
        GL11.glTranslatef((float)x, (float)y, (float)z);
        GL11.glRotatef(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - 90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 0.0F, 0.0F, 1.0F);

        Tessellator tessellator = Tessellator.instance;
        float f9 = 0.0F;
        float f10 = 0.5F;
        float f11 = (float)(0 + 0 * 10) / 32.0F;
        float f12 = (float)(5 + 0 * 10) / 32.0F;
        float f13 = 0.0F;
        float f14 = 0.15625F;
        float f15 = (float)(5 + 0 * 10) / 32.0F;
        float f16 = (float)(10 + 0 * 10) / 32.0F;
        float f17 = 0.05625F;
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);

        GL11.glRotatef(45.0F, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(f17, f17, f17);
        GL11.glTranslatef(-4.0F, 0.0F, 0.0F);

        for (int i = 0; i < 4; ++i) {
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            GL11.glNormal3f(0.0F, 0.0F, f17);
            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV(-8.0D, -2.0D, 0.0D, (double)f9, (double)f11);
            tessellator.addVertexWithUV(8.0D, -2.0D, 0.0D, (double)f10, (double)f11);
            tessellator.addVertexWithUV(8.0D, 2.0D, 0.0D, (double)f10, (double)f12);
            tessellator.addVertexWithUV(-8.0D, 2.0D, 0.0D, (double)f9, (double)f12);
            tessellator.draw();
        }

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return texture;
    }
}
