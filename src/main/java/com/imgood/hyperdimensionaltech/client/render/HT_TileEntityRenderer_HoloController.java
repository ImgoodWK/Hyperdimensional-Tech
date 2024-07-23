package com.imgood.hyperdimensionaltech.client.render;


import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
import com.gtnewhorizon.structurelib.alignment.enumerable.Rotation;
import com.gtnewhorizons.modularui.api.GlStateManager;
import com.imgood.hyperdimensionaltech.HyperdimensionalTech;
import com.imgood.hyperdimensionaltech.machines.HT_UniversalMineralProcessor;
import com.imgood.hyperdimensionaltech.tiles.rendertiles.TileHoloController;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.Field;
import java.util.Objects;

import static com.imgood.hyperdimensionaltech.utils.Enums.MOD;

@SideOnly(Side.CLIENT)
public class HT_TileEntityRenderer_HoloController extends TileEntitySpecialRenderer {

    //private static final ResourceLocation FEILDTEXTURE = TexturesGtBlock.HyperDimensionalResonanceEvolverField.getTextureFile();
    private static ResourceLocation ParticleStreamTexture =  new ResourceLocation(MOD.ID + ":textures/model/stiched_texture.png");
    private static IModelCustom ParticleStream = AdvancedModelLoader
        .loadModel(new ResourceLocation(MOD.ID + ":model/Hyperdimensional Controller.obj"));
    private double feildSizeX = 1.3;
    private double feildSizeY = 1.3;
    private double feildSizeZ = 1.3;
    private String textureFile;
    private String objFile;
    public Double rotation = 0.0;

    public HT_TileEntityRenderer_HoloController() {
        ClientRegistry.bindTileEntitySpecialRenderer(BaseMetaTileEntity.class, this);
    }

    public HT_TileEntityRenderer_HoloController(
        String textureFile,
        String objFile) {
        this.textureFile = textureFile;
        this.objFile = objFile;
        this.ParticleStreamTexture = new ResourceLocation(MOD.ID + ":textures/model/"+this.textureFile+".png");
        this.ParticleStream = AdvancedModelLoader.loadModel(new ResourceLocation(MOD.ID + ":model/"+this.objFile+".obj"));
        ClientRegistry.bindTileEntitySpecialRenderer(BaseMetaTileEntity.class, this);
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float timeSinceLastTick) {
        if (tile instanceof BaseMetaTileEntity){
            if (((BaseMetaTileEntity)tile).getMetaTileID() == 10002 ){
                GL11.glPushMatrix();
                GL11.glTranslated(x + 0.5, y + -0.5, z + 0.5);
                    this.getTileEntityFacing(tile, x, y, z);
                renderNoGlow(feildSizeX, feildSizeY, feildSizeZ);
                GL11.glPopMatrix();

                GL11.glPushMatrix();
                GL11.glTranslated(x + 0.5, y + -0.5, z + 0.5);
                this.getTileEntityFacing(tile, x, y, z);
                renderGlow(feildSizeX, feildSizeY, feildSizeZ);
                GL11.glPopMatrix();

            }
        }return;
        /*if (!(tile instanceof TileHoloController tileHoloController)) return;
        //final double size = TILEhrefEILD.size;
        //this.setFacing(((TileHoloController) tile).Facing);
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
        this.getTileEntityFacing(tileHoloController,x,y,z);
        renderFeild(feildSizeX, feildSizeY, feildSizeZ);
        GL11.glPopMatrix();*/
    }

    private void renderNoGlow(double sizeX, double sizeY, double sizeZ) {

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        this.bindTexture(ParticleStreamTexture);
        GL11.glScaled(sizeX, sizeY, sizeZ);
        ParticleStream.renderAllExcept("cubeholoscreen", "cubefrontline");
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LIGHTING);
    }
    private void renderGlow(double sizeX, double sizeY, double sizeZ) {
        GlStateManager.disableLighting();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        this.bindTexture(ParticleStreamTexture);
        GL11.glScaled(sizeX, sizeY, sizeZ);
        ParticleStream.renderOnly("cubeholoscreen", "cubefrontline");
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LIGHTING);
    }
    public HT_TileEntityRenderer_HoloController setRenderSize(double sizeX, double sizeY, double sizeZ) {
        this.feildSizeX = sizeX;
        this.feildSizeY = sizeY;
        this.feildSizeZ = sizeZ;
        return this;
    }


    private void getTileEntityFacing(TileEntity tile,double x, double y, double z)  {
        NBTTagCompound nbt = new NBTTagCompound();
        tile.writeToNBT(nbt);
        switch (ForgeDirection.getOrientation(nbt.getShort("mFacing"))) {
            case WEST -> {
                HyperdimensionalTech.logger.warn("WEST_NORMAL_NONE");
                //GL11.glTranslated(x + 0.2, y, z + 0.5);
                GL11.glRotated(90.0, 0.0, 1.0, 0.0);
            }
            case NORTH -> {
                HyperdimensionalTech.logger.warn("NORTH_NORMAL_NONE");
                //GL11.glTranslated(x + 0.5, y, z + 0.2);
                GL11.glRotated(0.0, 0.0, 1.0, 0.0);
            }
            case SOUTH -> {
                HyperdimensionalTech.logger.warn("SOUTH_NORMAL_NONE");
                //GL11.glTranslated(x + 0.5, y, z + 0.8);
                GL11.glRotated(180.0, 0.0, 1.0, 0.0);
            }
            default -> {
                HyperdimensionalTech.logger.warn("EAST_NORMAL_NONE");
                //GL11.glTranslated(x + 0.8, y, z + 0.5);
                GL11.glRotated(-90.0, 0.0, 1.0, 0.0);
            }
        }
    }


}
