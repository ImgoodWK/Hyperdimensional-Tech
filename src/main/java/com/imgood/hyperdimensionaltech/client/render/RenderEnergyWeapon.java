package com.imgood.hyperdimensionaltech.client.render;

import com.gtnewhorizons.modularui.api.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

import static com.imgood.hyperdimensionaltech.utils.Enums.MOD;

/**
 * @program: Hyperdimensional-Tech
 * @description:
 * @author: Imgood
 * @create: 2024-08-12 16:19
 **/
public class RenderEnergyWeapon implements IItemRenderer {
    private static ResourceLocation htSwordTexture = new ResourceLocation(MOD.ID + ":textures/item/HT_Sword.png");
    private static IModelCustom htSwordModel = AdvancedModelLoader
        .loadModel(new ResourceLocation(MOD.ID + ":model/Hyperdimensional BladeNew.obj"));

    public RenderEnergyWeapon() {
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        GL11.glPushMatrix();

        switch (type) {
            case EQUIPPED:
            case EQUIPPED_FIRST_PERSON:
                GL11.glTranslatef(0.3F, 0.5F, 0.5F);
                GL11.glRotated(140, 0, 1, 0);

                // 渲染不发光的部分
                renderNonGlowParts();

                // 渲染发光的部分
                renderGlowParts();
                break;
            case INVENTORY:
                GL11.glTranslatef(0F, -0.1F, 0F);
                GL11.glRotatef(180F, 0F, 1F, 0F);
                GL11.glScalef(0.8F, 0.8F, 0.8F);
                renderNonGlowParts();
                renderGlowParts();
                break;
            case ENTITY:
                GL11.glTranslatef(0F, 0F, 0F);
                GL11.glRotatef(180F, 0F, 1F, 0F);
                renderNonGlowParts();
                renderGlowParts();
                break;
            default:
                break;
        }

        GL11.glPopMatrix();
    }

    private void renderNonGlowParts() {
        GL11.glPushMatrix();
        Minecraft.getMinecraft().getTextureManager().bindTexture(htSwordTexture);
        htSwordModel.renderAllExcept("glow");
        GL11.glPopMatrix();
    }

    private void renderGlowParts() {
        GL11.glPushMatrix();

        // 保存当前的光照状态
        float lastBrightnessX = OpenGlHelper.lastBrightnessX;
        float lastBrightnessY = OpenGlHelper.lastBrightnessY;

        // 禁用光照和深度测试
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);

        // 启用混合
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);

        // 设置最大亮度
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);

        // 绑定纹理并设置颜色（这里设置为白色，你可以调整颜色以改变发光效果）
        Minecraft.getMinecraft().getTextureManager().bindTexture(htSwordTexture);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

        // 渲染发光部分
        htSwordModel.renderOnly("glow");

        // 恢复之前的渲染状态
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);

        // 恢复之前的亮度设置
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastBrightnessX, lastBrightnessY);

        GL11.glPopMatrix();
    }
}
