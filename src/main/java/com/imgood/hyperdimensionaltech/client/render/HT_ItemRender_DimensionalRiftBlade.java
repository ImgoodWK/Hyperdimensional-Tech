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
public class HT_ItemRender_DimensionalRiftBlade extends Render {
    private static final ResourceLocation texture = new ResourceLocation(MOD_ID, "textures/entity/BladeEntity.png");

    public HT_ItemRender_DimensionalRiftBlade() {
        super();
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw, float partialTicks) {
        GL11.glPushMatrix();
        this.bindEntityTexture(entity);
        GL11.glTranslatef((float)x, (float)y, (float)z);
        GL11.glRotatef(yaw, 0.0F, 1.0F, 0.0F);
        GL11.glRotated(90, 0, 1, 0);
        GL11.glScaled(4F, 4F, 4F);
        GL11.glRotatef(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 0.0F, 0.0F, 1.0F);

        Tessellator tessellator = Tessellator.instance;
        float minU = 0.0F;
        float maxU = 1.0F;
        float minV = 0.0F;
        float maxV = 1.0F;

        float halfWidth = 2.0F;
        float halfHeight = 0.5F;

        GL11.glEnable(GL12.GL_RESCALE_NORMAL);

        // 禁用光照和深度测试
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        // 设置全亮颜色
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        // 渲染上表面
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        tessellator.addVertexWithUV(-halfWidth, 0.0D, -halfHeight, minU, maxV);
        tessellator.addVertexWithUV(halfWidth, 0.0D, -halfHeight, maxU, maxV);
        tessellator.addVertexWithUV(halfWidth, 0.0D, halfHeight, maxU, minV);
        tessellator.addVertexWithUV(-halfWidth, 0.0D, halfHeight, minU, minV);
        tessellator.draw();

        // 渲染下表面
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        tessellator.addVertexWithUV(-halfWidth, 0.0D, halfHeight, minU, minV);
        tessellator.addVertexWithUV(halfWidth, 0.0D, halfHeight, maxU, minV);
        tessellator.addVertexWithUV(halfWidth, 0.0D, -halfHeight, maxU, maxV);
        tessellator.addVertexWithUV(-halfWidth, 0.0D, -halfHeight, minU, maxV);
        tessellator.draw();

        // 恢复状态
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return texture;
    }
}
