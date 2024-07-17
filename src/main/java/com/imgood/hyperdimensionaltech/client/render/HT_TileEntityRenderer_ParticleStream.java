package com.imgood.hyperdimensionaltech.client.render;


import com.imgood.hyperdimensionaltech.tiles.rendertiles.TileParticleStream;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

import static com.imgood.hyperdimensionaltech.utils.Enums.MOD;

@SideOnly(Side.CLIENT)
public class HT_TileEntityRenderer_ParticleStream extends TileEntitySpecialRenderer {

    //private static final ResourceLocation FEILDTEXTURE = TexturesGtBlock.HyperDimensionalResonanceEvolverField.getTextureFile();
    private static ResourceLocation ParticleStreamTexture =  new ResourceLocation(MOD.ID + ":textures/model/SingularityUnravelingDeviceParticleStream.png");
    private static IModelCustom ParticleStream = AdvancedModelLoader
        .loadModel(new ResourceLocation(MOD.ID + ":model/feild.obj"));
    private double feildSizeX = 3;
    private double feildSizeY = 0.1;
    private double feildSizeZ = 3;

    private double feildSizeMax = 2.9;
    private double feildSizeMin = 2.6;
    private boolean feildFlag= true;
    private double feildChangeSpeed = 0.0001;
    private String blockRenderId;
    private String textureFile;
    private String objFile;

    public HT_TileEntityRenderer_ParticleStream() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileParticleStream.class, this);
    }

    public HT_TileEntityRenderer_ParticleStream(
        String textureFile,
        String objFile) {
        this.textureFile = textureFile;
        this.objFile = objFile;
        this.ParticleStreamTexture = new ResourceLocation(MOD.ID + ":textures/model/"+this.textureFile+".png");
        this.ParticleStream = AdvancedModelLoader.loadModel(new ResourceLocation(MOD.ID + ":model/"+this.objFile+".obj"));
        ClientRegistry.bindTileEntitySpecialRenderer(TileParticleStream.class, this);
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float timeSinceLastTick) {
        if (!(tile instanceof TileParticleStream tileParticleStream)) return;
        //final double size = TILEhrefEILD.size;

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
        GL11.glRotated(tileParticleStream.Rotation, 0,-100,0);
        GL11.glRotated(tileParticleStream.Rotation, 0,-100,0);
        GL11.glRotated(tileParticleStream.Rotation, 0,-100,0);
        GL11.glRotated(tileParticleStream.Rotation, 0,-100,0);
        GL11.glRotated(tileParticleStream.Rotation, 0,-100,0);
        GL11.glRotated(tileParticleStream.Rotation, 0,-100,0);
        GL11.glRotated(tileParticleStream.Rotation, 0,-100,0);
        GL11.glRotated(tileParticleStream.Rotation, 0,-100,0);
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
    public HT_TileEntityRenderer_ParticleStream setRenderSize(double sizeX, double sizeY, double sizeZ) {
        this.feildSizeX = sizeX;
        this.feildSizeY = sizeY;
        this.feildSizeZ = sizeZ;
        return this;
    }
    public HT_TileEntityRenderer_ParticleStream setChangeSpeed(double changeSpeed) {
        this.feildChangeSpeed = changeSpeed;
        return this;
    }
    public HT_TileEntityRenderer_ParticleStream setMaxSize(double maxSize) {
        this.feildSizeMax = maxSize;
        return this;
    }
    public HT_TileEntityRenderer_ParticleStream setMinSize(double minSize) {
        this.feildSizeMin = minSize;
        return this;
    }
}
