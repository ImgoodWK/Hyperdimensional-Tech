package com.imgood.hyperdimensionaltech.client.render;

import com.gtnewhorizons.modularui.api.GlStateManager;
import com.imgood.hyperdimensionaltech.tiles.rendertiles.TileHolographicDisplay;
import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.Field;

import static com.imgood.hyperdimensionaltech.utils.Enums.MOD;

/**
 * @program: Hyperdimensional-Tech
 * @description: 全息浮空字
 * @author: Imgood
 * @create: 2024-07-30 13:24
 **/
public class HT_TileEntityHolographicDisplay extends TileEntitySpecialRenderer {
    private static ResourceLocation ParticleStreamTexture = new ResourceLocation(MOD.ID + ":textures/model/HolographicDisplay.png");
    private static IModelCustom ParticleStream = AdvancedModelLoader
        .loadModel(new ResourceLocation(MOD.ID + ":model/HolographicDisplay.obj"));
    private double feildSizeX = 4;
    private double feildSizeY = 4;
    private double feildSizeZ = 4;
    private String textureFile;
    private String objFile;
    private Field mMetaTileEntityField;


    public HT_TileEntityHolographicDisplay() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileHolographicDisplay.class, this);
    }

    public HT_TileEntityHolographicDisplay(
        String textureFile,
        String objFile) {
        this.textureFile = textureFile;
        this.objFile = objFile;
        this.ParticleStreamTexture = new ResourceLocation(MOD.ID + ":textures/model/" + this.textureFile + ".png");
        this.ParticleStream = AdvancedModelLoader.loadModel(new ResourceLocation(MOD.ID + ":model/" + this.objFile + ".obj"));
        ClientRegistry.bindTileEntitySpecialRenderer(TileHolographicDisplay.class, this);
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float timeSinceLastTick) {
        double textYOffset = 0.5;
        String text = ((TileHolographicDisplay)tile).getText();

        renderNoGlowExpect(this.feildSizeX, this.feildSizeY, this.feildSizeZ, x, y+0.5, z, tile, "screen", "botmidlight");
        renderGlow(this.feildSizeX, this.feildSizeY, this.feildSizeZ, x, y+0.5, z, tile, "screen", "botmidlight");
        drawCenteredString(tile, "1234", x, textYOffset+0.80 + y, z, 0x00ffff);
        drawCenteredString(tile, "GTNH" , x, textYOffset+1.00 + y, z, 0x00ffff);
        drawCenteredString(tile, text , x, textYOffset+1.20 + y, z, 0x00ffff);
        drawCenteredString(tile, "⚙Hyperdimensional Tech", x, textYOffset+1.40 + y, z, 0x00ffff);

    }


    public void renderNoGlowExpect(double sizeX, double sizeY, double sizeZ, double x, double y, double z, TileEntity tile, String... elementsExcept) {

        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y + -0.3, z + 0.5);
        this.getTileEntityFacing(tile, x, y, z);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        this.bindTexture(ParticleStreamTexture);
        GL11.glScaled(sizeX, sizeY, sizeZ);
        ParticleStream.renderAllExcept(elementsExcept);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }

    public void renderNoGlow(double sizeX, double sizeY, double sizeZ, double x, double y, double z, TileEntity tile, String... elementsExcept) {

        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y + -0.3, z + 0.5);
        this.getTileEntityFacing(tile, x, y, z);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        this.bindTexture(ParticleStreamTexture);
        GL11.glScaled(sizeX, sizeY, sizeZ);
        ParticleStream.renderOnly(elementsExcept);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }

    public void renderGlow(double sizeX, double sizeY, double sizeZ, double x, double y, double z, TileEntity tile, String... elements) {
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y + -0.3, z + 0.5);
        this.getTileEntityFacing(tile, x, y, z);
        GlStateManager.disableLighting();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        this.bindTexture(ParticleStreamTexture);
        GL11.glScaled(sizeX, sizeY, sizeZ);
        ParticleStream.renderOnly(elements);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }

    public void renderInventory(double scaleX, double scaleY, double scaleZ, double x, double y, double z, double rotation){
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        this.bindTexture(ParticleStreamTexture);
        GL11.glScaled(scaleX, scaleY, scaleZ);
        GL11.glTranslated(x,y,z);
        GL11.glRotated(rotation, 0.0, 1.0, 0.0);
        ParticleStream.renderAllExcept("cubeholoscreen");
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LIGHTING);
    }
    public void renderInventory(){
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        this.bindTexture(ParticleStreamTexture);
        GL11.glScaled(0.7, 0.7, 0.7);
        GL11.glTranslated(0,-0.5,0);
        GL11.glRotated(-90.0, 0.0, 1.0, 0.0);
        ParticleStream.renderAllExcept("cubeholoscreen");
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LIGHTING);
    }

    private void getTileEntityFacing(TileEntity tile, double x, double y, double z) {
        NBTTagCompound nbt = new NBTTagCompound();
        tile.writeToNBT(nbt);
        switch (ForgeDirection.getOrientation(nbt.getShort("mFacing"))) {
            case WEST -> {
                //HyperdimensionalTech.logger.warn("WEST_NORMAL_NONE");
                //GL11.glTranslated(x + 0.2, y, z + 0.5);
                GL11.glRotated(90.0, 0.0, 1.0, 0.0);
            }
            case NORTH -> {
                //HyperdimensionalTech.logger.warn("NORTH_NORMAL_NONE");
                //GL11.glTranslated(x + 0.5, y, z + 0.2);
                GL11.glRotated(0.0, 0.0, 1.0, 0.0);
            }
            case SOUTH -> {
                //HyperdimensionalTech.logger.warn("SOUTH_NORMAL_NONE");
                //GL11.glTranslated(x + 0.5, y, z + 0.8);
                GL11.glRotated(180.0, 0.0, 1.0, 0.0);
            }
            default -> {
                //HyperdimensionalTech.logger.warn("EAST_NORMAL_NONE");
                //GL11.glTranslated(x + 0.8, y, z + 0.5);
                GL11.glRotated(-90.0, 0.0, 1.0, 0.0);
            }
        }
    }

    private void getTileEntityFacing(TileEntity tile, double x, double y, double z, double ox, double oy, double oz) {
        NBTTagCompound nbt = new NBTTagCompound();
        tile.writeToNBT(nbt);
        switch (ForgeDirection.getOrientation(nbt.getShort("mFacing"))) {
            case WEST -> {
                //HyperdimensionalTech.logger.warn("WEST_NORMAL_NONE");
                GL11.glTranslated(x + ox, y, z + oy - 0.4);
                GL11.glRotated(90.0, 0.0, 1.0, 0.0);
            }
            case NORTH -> {
                //HyperdimensionalTech.logger.warn("NORTH_NORMAL_NONE");
                GL11.glTranslated(x + oy + 1.4, y, z + ox);
                GL11.glRotated(0.0, 0.0, 1.0, 0.0);
            }
            case SOUTH -> {
                //HyperdimensionalTech.logger.warn("SOUTH_NORMAL_NONE");
                GL11.glTranslated(x + oy - 0.4, y, z + oz);
                GL11.glRotated(180.0, 0.0, 1.0, 0.0);
            }
            default -> {
                //HyperdimensionalTech.logger.warn("EAST_NORMAL_NONE");
                GL11.glTranslated(x + oz, y, z + oy + 1.4);
                GL11.glRotated(-90.0, 0.0, 1.0, 0.0);
            }
        }
    }

    public void drawCenteredString(TileEntity tile, String text, double x, double y, double z, int color) {
        Minecraft mc = Minecraft.getMinecraft();

        // 准备渲染
        GL11.glPushMatrix();
        this.getTileEntityFacing(tile, x, y, z, 0.2, 0, 0.8);

        // 缩放文本，使其大小适合
        float scale = 0.016666668F * 1.3F;
        GL11.glScalef(-scale, -scale, scale);
        mc.fontRenderer.drawString(text, 0, 0, color);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }

}
