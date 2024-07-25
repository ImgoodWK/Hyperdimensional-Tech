package com.imgood.hyperdimensionaltech.client.render;


import com.gtnewhorizons.modularui.api.GlStateManager;
import com.imgood.hyperdimensionaltech.HyperdimensionalTech;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.metatileentity.BaseMetaTileEntity;
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

import static com.imgood.hyperdimensionaltech.utils.Enums.MOD;

@SideOnly(Side.CLIENT)
public class HT_TileEntityRenderer_HoloController extends TileEntitySpecialRenderer {

    //private static final ResourceLocation FEILDTEXTURE = TexturesGtBlock.HyperDimensionalResonanceEvolverField.getTextureFile();
    private static ResourceLocation ParticleStreamTexture = new ResourceLocation(MOD.ID + ":textures/model/stiched_texture.png");
    private static IModelCustom ParticleStream = AdvancedModelLoader
        .loadModel(new ResourceLocation(MOD.ID + ":model/Hyperdimensional Controller.obj"));
    public Double rotation = 0.0;
    private double feildSizeX = 1.3;
    private double feildSizeY = 1.3;
    private double feildSizeZ = 1.3;
    private String textureFile;
    private String objFile;

    public HT_TileEntityRenderer_HoloController() {
        ClientRegistry.bindTileEntitySpecialRenderer(BaseMetaTileEntity.class, this);
    }

    public HT_TileEntityRenderer_HoloController(
        String textureFile,
        String objFile) {
        this.textureFile = textureFile;
        this.objFile = objFile;
        this.ParticleStreamTexture = new ResourceLocation(MOD.ID + ":textures/model/" + this.textureFile + ".png");
        this.ParticleStream = AdvancedModelLoader.loadModel(new ResourceLocation(MOD.ID + ":model/" + this.objFile + ".obj"));
        ClientRegistry.bindTileEntitySpecialRenderer(BaseMetaTileEntity.class, this);
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float timeSinceLastTick) {

        // endregion
        if (tile instanceof BaseMetaTileEntity) {
            if (((BaseMetaTileEntity) tile).getOwnerName().contains("BlockRender")) {
                if (((BaseMetaTileEntity) tile).getMetaTileID() == 10002) {
                    renderNoGlow(feildSizeX, feildSizeY, feildSizeZ, x, y, z, tile, "cubeholoscreen", "cubefrontline");
                    renderGlow(feildSizeX, feildSizeY, feildSizeZ, x, y, z, tile, "cubeholoscreen", "cubefrontline");
                }
            } else {
                if (((BaseMetaTileEntity) tile).getMetaTileID() == 10002 && ((BaseMetaTileEntity) tile).getProgress() != 0) {
                    renderNoGlow(feildSizeX, feildSizeY, feildSizeZ,x,y,z,tile, "cubeholoscreen", "cubefrontline");
                    renderGlow(feildSizeX, feildSizeY, feildSizeZ,x,y,z,tile,"cubeholoscreen", "cubefrontline");
                } else {
                    renderNoGlow(feildSizeX, feildSizeY, feildSizeZ,x,y,z,tile, "cubeholoscreen", "cubefrontline");
                    renderGlow(feildSizeX, feildSizeY, feildSizeZ,x,y,z,tile,"cubefrontline");
                    drawCenteredString("Hyperdimensional", 0, 0, z , 0x99FFFF);
                }
            }
        }
    }

    public void renderNoGlow(double sizeX, double sizeY, double sizeZ, double x, double y, double z, TileEntity tile,String ... elementsExcept) {

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

    public void renderGlow(double sizeX, double sizeY, double sizeZ, double x,double y, double z, TileEntity tile, String... elements) {
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

    public void renderInventory() {
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        this.bindTexture(ParticleStreamTexture);
        GL11.glScaled(0.7, 0.7, 0.7);
        GL11.glRotated(180.0, 0.0, 1.0, 0.0);
        ParticleStream.renderAllExcept("cubeholoscreen");
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LIGHTING);
    }

    public HT_TileEntityRenderer_HoloController setRenderSize(double sizeX, double sizeY, double sizeZ) {
        this.feildSizeX = sizeX;
        this.feildSizeY = sizeY;
        this.feildSizeZ = sizeZ;
        return this;
    }


    private void getTileEntityFacing(TileEntity tile, double x, double y, double z) {
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
    private void drawCenteredString(String text, int x, int y, double z,int color) {
        Minecraft mc = Minecraft.getMinecraft();

        // 准备渲染
        GL11.glPushMatrix();

        // 平移到正确的位置
        GL11.glTranslated(x + 0.5, y + 1.5, z + 0.5);

        // 设置旋转角度，使文本朝向玩家
        //GL11.glRotatef(-mc.thePlayer.rotationYaw, 0.0F, 1.0F, 0.0F);
        //GL11.glRotatef(mc.thePlayer.rotationPitch, 1.0F, 0.0F, 0.0F);

        // 缩放文本，使其大小适合
        float scale = 0.016666668F * 1.6F;
        GL11.glScalef(-scale, -scale, scale);
        mc.fontRenderer.drawString(text, x - mc.fontRenderer.getStringWidth(text) / 2, y, color);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }

}
