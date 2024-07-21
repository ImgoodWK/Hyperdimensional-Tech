package com.imgood.hyperdimensionaltech.client.render;


import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
import com.gtnewhorizon.structurelib.alignment.enumerable.Rotation;
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

import static com.imgood.hyperdimensionaltech.utils.Enums.MOD;

@SideOnly(Side.CLIENT)
public class HT_TileEntityRenderer_HoloController extends TileEntitySpecialRenderer {

    //private static final ResourceLocation FEILDTEXTURE = TexturesGtBlock.HyperDimensionalResonanceEvolverField.getTextureFile();
    private static ResourceLocation ParticleStreamTexture =  new ResourceLocation(MOD.ID + ":textures/model/stiched_texture.png");
    private static IModelCustom ParticleStream = AdvancedModelLoader
        .loadModel(new ResourceLocation(MOD.ID + ":model/Hyperdimensional Controller.obj"));
    private double feildSizeX = 1.5;
    private double feildSizeY = 1.5;
    private double feildSizeZ = 1.5;

    private double feildSizeMax = 2.9;
    private double feildSizeMin = 2.6;
    private boolean feildFlag= true;
    private double feildChangeSpeed = 0.0000;
    private String blockRenderId;
    private String textureFile;
    private String objFile;
    private ExtendedFacing facing;
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
        this.rotation = ((TileHoloController) tile).getRotation();
        if (!(tile instanceof TileHoloController tileHoloController)) return;
        //final double size = TILEhrefEILD.size;
        this.setFacing(((TileHoloController) tile).getExtendedFacing());
        if(feildSizeX >= feildSizeMax) {
            feildFlag = false;
        }else if(feildSizeX <= feildSizeMin) {
            feildFlag = true;
        }
        if(feildFlag){
            feildSizeX += feildChangeSpeed;
            feildSizeZ += feildChangeSpeed;
        }else{
            feildSizeX -= feildChangeSpeed;
            feildSizeZ -= feildChangeSpeed;
        }
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
        //GL11.glRotated(TILEhrefEILD.Rotation, 1, 1, 1);
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
    public HT_TileEntityRenderer_HoloController setChangeSpeed(double changeSpeed) {
        this.feildChangeSpeed = changeSpeed;
        return this;
    }
    public HT_TileEntityRenderer_HoloController setMaxSize(double maxSize) {
        this.feildSizeMax = maxSize;
        return this;
    }
    public HT_TileEntityRenderer_HoloController setMinSize(double minSize) {
        this.feildSizeMin = minSize;
        return this;
    }

    public ExtendedFacing getFacing() {
        return facing;
    }

    public void setFacing(ExtendedFacing facing) {
        this.facing = facing;
    }
}
