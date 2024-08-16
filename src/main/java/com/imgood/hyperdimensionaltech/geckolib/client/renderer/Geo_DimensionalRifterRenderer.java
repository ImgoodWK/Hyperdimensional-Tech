package com.imgood.hyperdimensionaltech.geckolib.client.renderer;

import com.imgood.hyperdimensionaltech.geckolib.TextMatrixStack;
import com.imgood.hyperdimensionaltech.geckolib.client.model.Geo_DimensionalRifterModel;
import com.imgood.hyperdimensionaltech.item.DimensionalRifter;
import net.geckominecraft.client.renderer.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;
import software.bernie.geckolib3.geo.render.built.GeoCube;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;
import java.nio.FloatBuffer;
import java.util.Iterator;
import java.util.Objects;

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


public class Geo_DimensionalRifterRenderer extends GeoItemRenderer<DimensionalRifter> {
    public Geo_DimensionalRifterRenderer() {
        super(new Geo_DimensionalRifterModel());
    }

    TextMatrixStack textMatrix = new TextMatrixStack();
    private GeoBone getBoneRecursively(String name, GeoBone bone) {
        if (bone.name.equals(name)) {
            return bone;
        } else {
            Iterator var3 = bone.childBones.iterator();

            GeoBone optionalBone;
            do {
                if (!var3.hasNext()) {
                    return null;
                }

                GeoBone childBone = (GeoBone)var3.next();
                if (childBone.name.equals(name)) {
                    return childBone;
                }

                optionalBone = this.getBoneRecursively(name, childBone);
            } while(optionalBone == null);

            return optionalBone;
        }
    }

    @Override
    public void renderRecursively(Tessellator builder, DimensionalRifter animatable, GeoBone bone, float red, float green, float blue, float alpha) {
        MATRIX_STACK.push();
        MATRIX_STACK.translate(bone);
        MATRIX_STACK.moveToPivot(bone);
        MATRIX_STACK.rotate(bone);
        MATRIX_STACK.scale(bone);
        MATRIX_STACK.moveBackFromPivot(bone);

        // 在这里添加你的文字渲染逻辑
//        if (bone.getName().equals("all")) {
//            renderTextAtBone(bone, animatable.getEnergy()+"");
//        }

        if (this.isBoneRenderOverriden(animatable, bone)) {
            this.drawOverridenBone(animatable, bone);
            MATRIX_STACK.pop();
        } else {
            Iterator var8;
            if (!bone.isHidden()) {
                var8 = bone.childCubes.iterator();

                while(var8.hasNext()) {
                    GeoCube cube = (GeoCube)var8.next();
                    MATRIX_STACK.push();
                    GlStateManager.pushMatrix();
                    this.renderCube(builder, cube, red, green, blue, alpha);
                    GlStateManager.popMatrix();
                    MATRIX_STACK.pop();
                }
            }

            if (!bone.childBonesAreHiddenToo()) {
                var8 = bone.childBones.iterator();

                while(var8.hasNext()) {
                    GeoBone childBone = (GeoBone)var8.next();
                    this.renderRecursively(builder, animatable, childBone, red, green, blue, alpha);
                }
            }

            MATRIX_STACK.pop();
        }
    }


    @Override
    public void render(GeoModel model, DimensionalRifter animatable, float partialTicks, float red, float green, float blue, float alpha) {
        GlStateManager.disableCull();
        GlStateManager.enableRescaleNormal();
        GL11.glBlendFunc(770, 771);
        GlStateManager.enableBlend();
        GL11.glEnable(3553);

        this.renderEarly(model, animatable, partialTicks, red, green, blue, alpha);
        this.renderLate(model, animatable, partialTicks, red, green, blue, alpha);

        Tessellator tess = Tessellator.instance;
        tess.startDrawing(7);

        for (GeoBone group : model.topLevelBones) {
            if (!Objects.equals(group.getName(), "glow")) {
                this.renderRecursively(tess, animatable, group, red, green, blue, alpha);
            }
        }

        tess.draw();

        // 渲染发光部分
        GlStateManager.disableLighting();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);

        tess.startDrawing(7);

        boolean shouldGlow = false;
        GeoBone glowBone = null;
        for (GeoBone group : model.topLevelBones) {
            if (Objects.equals(group.getName(), "glow")) {
                shouldGlow = true;
                glowBone = group;
                this.renderRecursively(tess, animatable, group, red, green, blue, alpha);
            }
        }



        // 渲染文字
        if (glowBone != null) {
            // 获取当前的变换矩阵
            GL11.glPushMatrix();

            // 获取动态文本
            String text = animatable.getEnergy() + "";
            FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;

            // 渲染文本
            GL11.glTranslatef(0.7f, 1, 0);  // 可以调整这个来设置文字的相对位置
            GL11.glScalef(0.01F, 0.01F, 0.01F);  // 缩放文字
            GL11.glRotated(180, 1, 0, 0);  // 翻转文本
            GL11.glRotated(180, 0, 1, 0);  // 翻转文本
            fontRenderer.drawString(text, 0, 0, 0x00FFFF);

            // 恢复变换
            GL11.glPopMatrix();
        }
        tess.draw();
        GlStateManager.enableLighting();

        this.renderAfter(model, animatable, partialTicks, red, green, blue, alpha);

        GlStateManager.disableBlend();
        GlStateManager.enableCull();
    }

    @Override
    public void renderItem(IItemRenderer.ItemRenderType var1, ItemStack itemStack, Object... var3) {
        GL11.glPushMatrix();
        if (var1 == ItemRenderType.INVENTORY) {
            GL11.glTranslated(-0.6,-1.3,0);
            GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
            GL11.glScaled(0.7, 0.7, 0.7);
        }

        if (var1 == ItemRenderType.EQUIPPED_FIRST_PERSON) {
            GL11.glTranslated(-0, -1.2, 1);
            GL11.glRotated(60, 0.0, 1.0, 0.0);
            GL11.glScaled(2, 2, 2);
        }

        if (var1 == ItemRenderType.ENTITY) {
        }

        if (var1 == ItemRenderType.EQUIPPED) {
            GL11.glTranslated(2.2, 2, 3.3);
            GL11.glRotated(90, 1, 0, 0.0);
            GL11.glRotated(115, 0, 0, 1);
            GL11.glRotated(270, 0, 1, 0);
            GL11.glScaled(3, 3, 3);
        }

        this.render((DimensionalRifter) itemStack.getItem(), itemStack);
        GL11.glPopMatrix();
    }



}
