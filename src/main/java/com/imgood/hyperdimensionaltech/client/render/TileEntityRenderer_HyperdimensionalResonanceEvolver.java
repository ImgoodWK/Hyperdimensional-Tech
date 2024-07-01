package com.imgood.hyperdimensionaltech.client.render;


import com.imgood.hyperdimensionaltech.tiles.rendertiles.TileHREFeild;
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
public class TileEntityRenderer_HyperdimensionalResonanceEvolver extends TileEntitySpecialRenderer {

    //private static final ResourceLocation FEILDTEXTURE = TexturesGtBlock.HyperDimensionalResonanceEvolverField.getTextureFile();
    private static final ResourceLocation FEILDTEXTURE = new ResourceLocation(MOD.ID + ":textures/model/HyperDimensionalResonanceEvolverField.png");
    private static final IModelCustom FEILD = AdvancedModelLoader
        .loadModel(new ResourceLocation(MOD.ID + ":model/feild.obj"));
    private double feildSizeX = 0.1;
    private double feildSizeY = 0.9;
    private double feildSizeZ = 0.1;

    private double feildSizeMax = 0.8;
    private double feildSizeMin = 0.7;
    private boolean feildFlag= true;
    private double feildChangeSspeed = 0.0005;

    public TileEntityRenderer_HyperdimensionalResonanceEvolver() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileHREFeild.class, this);
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float timeSinceLastTick) {
        if (!(tile instanceof TileHREFeild tileHREFeild)) return;
        //final double size = TILEhrefEILD.size;

        if(feildSizeX >= feildSizeMax) {
            feildFlag = false;
        }else if(feildSizeX <= feildSizeMin) {
            feildFlag = true;
        }
        if(feildFlag){
            feildSizeX += feildChangeSspeed;
            feildSizeZ += feildChangeSspeed;
        }else{
            feildSizeX -= feildChangeSspeed;
            feildSizeZ -= feildChangeSspeed;
        }
        GL11.glPushMatrix();
        GL11.glTranslated(x + 0.5, y + 0.5, z + 0.5);
        //GL11.glRotated(TILEhrefEILD.Rotation, 1, 1, 1);
        GL11.glRotated(tileHREFeild.Rotation, 0,10,0);
        renderFeild(feildSizeX, feildSizeY, feildSizeZ);
        GL11.glPopMatrix();
    }

    private void renderFeild(double sizeX, double sizeY, double sizeZ) {
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        this.bindTexture(FEILDTEXTURE);
        GL11.glScaled(sizeX, sizeY, sizeZ);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
        FEILD.renderAll();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_LIGHTING);
    }
}
