package com.imgood.hyperdimensionaltech.client.render;


import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
import com.gtnewhorizon.structurelib.alignment.enumerable.Rotation;
import com.gtnewhorizons.modularui.api.GlStateManager;
import com.imgood.hyperdimensionaltech.HyperdimensionalTech;
import com.imgood.hyperdimensionaltech.tiles.rendertiles.TileHoloController;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

import java.util.Objects;

import static com.imgood.hyperdimensionaltech.utils.Enums.MOD;

@SideOnly(Side.CLIENT)
public class HT_TileEntityRenderer_HoloController extends TileEntitySpecialRenderer {

    //private static final ResourceLocation FEILDTEXTURE = TexturesGtBlock.HyperDimensionalResonanceEvolverField.getTextureFile();
    private static ResourceLocation ParticleStreamTexture =  new ResourceLocation(MOD.ID + ":textures/model/stiched_texture.png");
    private static IModelCustom ParticleStream = AdvancedModelLoader
        .loadModel(new ResourceLocation(MOD.ID + ":model/Hyperdimensional Controller.obj"));
    private double feildSizeX = 1;
    private double feildSizeY = 1;
    private double feildSizeZ = 1;
    private String textureFile;
    private String objFile;
    private String facing;
    public Double rotation = 0.0;

    public HT_TileEntityRenderer_HoloController() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileHoloController.class, this);
    }

    public HT_TileEntityRenderer_HoloController(
        String textureFile,
        String objFile) {
        this.textureFile = textureFile;
        this.objFile = objFile;
        this.ParticleStreamTexture = new ResourceLocation(MOD.ID + ":textures/model/"+this.textureFile+".png");
        this.ParticleStream = AdvancedModelLoader.loadModel(new ResourceLocation(MOD.ID + ":model/"+this.objFile+".obj"));
        ClientRegistry.bindTileEntitySpecialRenderer(TileHoloController.class, this);
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float timeSinceLastTick) {
        if (!(tile instanceof TileHoloController tileHoloController)) return;
        //final double size = TILEhrefEILD.size;
        //this.setFacing(((TileHoloController) tile).Facing);

        GL11.glPushMatrix();
        this.getTileEntityFacing(tileHoloController, x, y, z);
        GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
        renderFeild(feildSizeX, feildSizeY, feildSizeZ);
        GL11.glPopMatrix();
    }

    private void renderFeild(double sizeX, double sizeY, double sizeZ) {
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        this.bindTexture(ParticleStreamTexture);
        GL11.glScaled(sizeX, sizeY, sizeZ);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
        ParticleStream.renderAll();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_LIGHTING);
    }
    public HT_TileEntityRenderer_HoloController setRenderSize(double sizeX, double sizeY, double sizeZ) {
        this.feildSizeX = sizeX;
        this.feildSizeY = sizeY;
        this.feildSizeZ = sizeZ;
        return this;
    }

    public String getFacing() {
        return this.facing;
    }

    public void setFacing(String facing) {
        this.facing = facing;
    }

    private void getTileEntityFacing(TileHoloController tile, double x, double y, double z) {
        switch (tile.getExtendedFacing()) {
            case WEST_NORMAL_NONE:
                HyperdimensionalTech.logger.warn("WEST_NORMAL_NONE");
                //GL11.glTranslated(x + 0.2, y, z + 0.5);
                GL11.glRotated(90.0, 0.0, 1.0, 0.0);
                break;
            case EAST_NORMAL_NONE:
                HyperdimensionalTech.logger.warn("EAST_NORMAL_NONE");
                //GL11.glTranslated(x + 0.8, y, z + 0.5);
                GL11.glRotated(-90.0, 0.0, 1.0, 0.0);
                break;
            case NORTH_NORMAL_NONE:
                HyperdimensionalTech.logger.warn("NORTH_NORMAL_NONE");
                //GL11.glTranslated(x + 0.5, y, z + 0.2);
                GL11.glRotated(0.0, 0.0, 1.0, 0.0);
                break;
            case SOUTH_NORMAL_NONE:
                HyperdimensionalTech.logger.warn("SOUTH_NORMAL_NONE");
                //GL11.glTranslated(x + 0.5, y, z + 0.8);
                GL11.glRotated(180.0, 0.0, 1.0, 0.0);
        }

    }
}
