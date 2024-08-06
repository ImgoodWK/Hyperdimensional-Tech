package com.imgood.hyperdimensionaltech.client.render;

import com.gtnewhorizons.modularui.api.GlStateManager;
import com.imgood.hyperdimensionaltech.tiles.rendertiles.TileHolographicDisplay;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

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
    private static Map<String, ResourceLocation> imageCache = new HashMap<>();
    private double feildSizeX = 4;
    private double feildSizeY = 4;
    private double feildSizeZ = 4;
    private int facing;
    private TileHolographicDisplay tileHolographicDisplay;

    public HT_TileEntityHolographicDisplay() {
        ClientRegistry.bindTileEntitySpecialRenderer(TileHolographicDisplay.class, this);
    }

    private int tickCount = 0;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            tickCount++;

            if (tickCount >=21) {
                tickCount = 0;
            }
        }
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float timeSinceLastTick) {
        World world = Minecraft.getMinecraft().theWorld;
        TileHolographicDisplay tileEntity = (TileHolographicDisplay) world.getTileEntity(tile.xCoord, tile.yCoord, tile.zCoord);
        double textYOffset = 1;
        boolean isIntest = false;
        if (tileEntity != null && !isIntest &&this.tickCount == 0) {
            if (tileEntity.isVisableBody()) {
                renderNoGlowExpect(this.feildSizeX, this.feildSizeY, this.feildSizeZ, x, y + 0.4, z, tile, "screen", "botmidlight");
                renderGlow(this.feildSizeX, this.feildSizeY, this.feildSizeZ, x, y + 0.4, z, tile, "botmidlight");
            }
            if (tileEntity.isVisableScreen()) {
                renderGlow(this.feildSizeX, this.feildSizeY, this.feildSizeZ, x, y + 0.4, z, tile, "screen");
            }
            for (int i = 0; i < tileEntity.getDisplayDataSize(); i++) {
                this.facing = tileEntity.facing;
                String textContents[] = tileEntity.getContents(i);
                drawCenteredString(tile, textContents[3], x, textYOffset + 0.80 + y, z, Integer.parseInt(tileEntity.getRGBColor(i), 16));
                drawCenteredString(tile, textContents[2], x, textYOffset + 1.00 + y, z, Integer.parseInt(tileEntity.getRGBColor(i), 16));
                drawCenteredString(tile, textContents[1], x, textYOffset + 1.20 + y, z, Integer.parseInt(tileEntity.getRGBColor(i), 16));
                drawCenteredString(tile, textContents[0], x, textYOffset + 1.40 + y, z, Integer.parseInt(tileEntity.getRGBColor(i), 16));
                if (!tileEntity.getImgURL(i).isEmpty()) {
                    /*renderImage(tile,
                        tileEntity.getImgURL(i),
                        x + translateOffset("x", tileEntity.getImgStartX(i)),
                        y + tileEntity.getImgStartY(i),
                        z + translateOffset("z", tileEntity.getImgStartX(i)),
                        tileEntity.getImgScaledX(i),
                        tileEntity.getImgScaledY(i));
                    renderImageBack(tile,
                        tileEntity.getImgURL(i),
                        x - translateOffset("x", tileEntity.getImgStartX(i)),
                        y + tileEntity.getImgStartY(i),
                        z - translateOffset("z", tileEntity.getImgStartX(i)),
                        tileEntity.getImgScaledX(i),
                        tileEntity.getImgScaledY(i));*/
                    renderImageLocal(tile,
                        tileEntity.getImgPath(i),
                        x - translateOffset("x", tileEntity.getImgStartX(i)),
                        y + tileEntity.getImgStartY(i),
                        z - translateOffset("z", tileEntity.getImgStartX(i)),
                        tileEntity.getImgScaledX(i),
                        tileEntity.getImgScaledY(i));
                    renderImageLocalBack(tile,
                        tileEntity.getImgPath(i),
                        x - translateOffset("x", tileEntity.getImgStartX(i)),
                        y + tileEntity.getImgStartY(i),
                        z - translateOffset("z", tileEntity.getImgStartX(i)),
                        tileEntity.getImgScaledX(i),
                        tileEntity.getImgScaledY(i));
                }
            }
        } else {
            if (tileEntity.isVisableBody()) {
                renderNoGlowExpect(this.feildSizeX, this.feildSizeY, this.feildSizeZ, x, y + 0.4, z, tile, "screen", "botmidlight");
                renderGlow(this.feildSizeX, this.feildSizeY, this.feildSizeZ, x, y + 0.4, z, tile, "botmidlight");
            }
            if (tileEntity.isVisableScreen()) {
                renderGlow(this.feildSizeX, this.feildSizeY, this.feildSizeZ, x, y + 0.4, z, tile, "screen");
            }
        }
        this.clearImageCache();
    }


    public void renderNoGlowExpect(double sizeX, double sizeY, double sizeZ, double x, double y, double z, TileEntity tile, String... elementsExcept) {
        GL11.glPushMatrix();
        GL11.glTranslated(0.5, -0.3, 0.5);
        this.getTileEntityFacing(tile, x, y, z, 0.2);
        GL11.glEnable(GL11.GL_LIGHTING);  // 启用光照
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        this.bindTexture(ParticleStreamTexture);
        GL11.glScaled(sizeX, sizeY, sizeZ);
        ParticleStream.renderAllExcept(elementsExcept);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    public void renderNoGlow(double sizeX, double sizeY, double sizeZ, double x, double y, double z, TileEntity tile, String... elementsExcept) {

        GL11.glPushMatrix();
        GL11.glTranslated(0.5, -0.3, 0.5);
        //this.getRotationMatrix(tile, x, y, z);
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
        GL11.glTranslated(0.5, -0.3, 0.5);
        this.getTileEntityFacing(tile, x, y, z, 0.2);
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

    public void renderInventory(double scaleX, double scaleY, double scaleZ, double x, double y, double z, double rotation) {
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        this.bindTexture(ParticleStreamTexture);
        GL11.glScaled(scaleX, scaleY, scaleZ);
        GL11.glTranslated(x, y, z);
        GL11.glRotated(rotation, 0.0, 1.0, 0.0);
        ParticleStream.renderAllExcept("cubeholoscreen");
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LIGHTING);
    }

    public void renderInventory() {
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        this.bindTexture(ParticleStreamTexture);
        GL11.glScaled(0.7, 0.7, 0.7);
        GL11.glTranslated(0, -0.5, 0);
        GL11.glRotated(-90.0, 0.0, 1.0, 0.0);
        ParticleStream.renderAllExcept("cubeholoscreen");
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LIGHTING);
    }

    private void getTileEntityFacing(TileEntity tile, double x, double y, double z) {
        int rotation = ((TileHolographicDisplay) tile).facing;
        switch (rotation) {
            case 1 -> {
                //HyperdimensionalTech.logger.warn("WEST_NORMAL_NONE");
                GL11.glTranslated(x, y, z);
                GL11.glRotated(90.0, 0.0, 1.0, 0.0);
            }
            case 2 -> {
                //HyperdimensionalTech.logger.warn("NORTH_NORMAL_NONE");
                GL11.glTranslated(x, y, z);
                GL11.glRotated(0.0, 0.0, 1.0, 0.0);
            }
            case 0 -> {
                //HyperdimensionalTech.logger.warn("SOUTH_NORMAL_NONE");
                GL11.glTranslated(x, y, z);
                GL11.glRotated(180.0, 0.0, 1.0, 0.0);
            }
            default -> {
                //HyperdimensionalTech.logger.warn("EAST_NORMAL_NONE");
                GL11.glTranslated(x, y, z);
                GL11.glRotated(-90.0, 0.0, 1.0, 0.0);
            }
        }
    }

    private void getTextFacing(TileEntity tile, double x, double y, double z) {
        int rotation = ((TileHolographicDisplay) tile).facing;
        switch (rotation) {
            case 1 -> {
                //HyperdimensionalTech.logger.warn("WEST_NORMAL_NONE");
                GL11.glTranslated(x, y, z + 1);
                GL11.glRotated(90.0, 0.0, 1.0, 0.0);
            }
            case 2 -> {
                //HyperdimensionalTech.logger.warn("NORTH_NORMAL_NONE");
                GL11.glTranslated(x, y, z);
                GL11.glRotated(0.0, 0.0, 1.0, 0.0);
            }
            case 0 -> {
                //HyperdimensionalTech.logger.warn("SOUTH_NORMAL_NONE");
                GL11.glTranslated(x + 1, y, z + 1);
                GL11.glRotated(180.0, 0.0, 1.0, 0.0);
            }
            default -> {
                //HyperdimensionalTech.logger.warn("EAST_NORMAL_NONE");
                GL11.glTranslated(x + 1, y, z);
                GL11.glRotated(-90.0, 0.0, 1.0, 0.0);
            }
        }
    }

    private void getTextFacingBack(TileEntity tile, double x, double y, double z) {
        int rotation = ((TileHolographicDisplay) tile).facing;
        switch (rotation) {
            case 1 -> {
                //HyperdimensionalTech.logger.warn("WEST_NORMAL_NONE");
                GL11.glTranslated(x + 1.2, y, z);
                GL11.glRotated(-90.0, 0.0, 1.0, 0.0);
            }
            case 2 -> {
                //HyperdimensionalTech.logger.warn("NORTH_NORMAL_NONE");
                GL11.glTranslated(x + 1, y, z + 1.2);
                GL11.glRotated(180.0, 0.0, 1.0, 0.0);
            }
            case 0 -> {
                //HyperdimensionalTech.logger.warn("SOUTH_NORMAL_NONE");

                GL11.glTranslated(x, y, z - 0.2);
                GL11.glRotated(0.0, 0.0, 1.0, 0.0);
            }
            default -> {
                //HyperdimensionalTech.logger.warn("EAST_NORMAL_NONE");

                GL11.glTranslated(x - 0.2, y, z + 1);
                GL11.glRotated(90.0, 0.0, 1.0, 0.0);
            }
        }
    }

    private void getTileEntityFacing(TileEntity tile, double x, double y, double z, double offset) {
        int rotation = ((TileHolographicDisplay) tile).facing;
        //0.5 0.2 0.8

        switch (rotation) {
            case 1 -> {
                GL11.glTranslated(x + offset, y, z);
                GL11.glRotated(90.0, 0.0, 1.0, 0.0);
            }
            case 2 -> {
                GL11.glTranslated(x, y, z + offset);
                GL11.glRotated(0.0, 0.0, 1.0, 0.0);
            }
            case 0 -> {
                GL11.glTranslated(x, y, z - offset);
                GL11.glRotated(180.0, 0.0, 1.0, 0.0);
            }
            default -> {
                GL11.glTranslated(x - offset, y, z);
                GL11.glRotated(-90.0, 0.0, 1.0, 0.0);
            }
        }
    }

    private ResourceLocation getImageFromURL(String url) {
        if (imageCache.containsKey(url)) {
            return imageCache.get(url);
        }

        try {
            BufferedImage image = ImageIO.read(new URL(url));
            String textureName = "url_texture_" + url.hashCode();
            ResourceLocation resourceLocation = new ResourceLocation(MOD.ID, textureName);

            Minecraft.getMinecraft().getTextureManager().loadTexture(resourceLocation, new DynamicTexture(image));

            imageCache.put(url, resourceLocation);
            return resourceLocation;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Map<String, ResourceLocation> localImageCache = new HashMap<>();

    private ResourceLocation getImageFromLocal(String filename) {
        if (localImageCache.containsKey(filename)) {
            return localImageCache.get(filename);
        }

        try {
            File file = new File(Minecraft.getMinecraft().mcDataDir, "cache/holographic_images/" + filename+".png");
            if (!file.exists()) {
                System.out.println("Local image file not found: " + file.getAbsolutePath());
                return null;
            }

            BufferedImage image = ImageIO.read(file);
            String textureName = "local_texture_" + filename.hashCode();
            ResourceLocation resourceLocation = new ResourceLocation(MOD.ID, textureName);

            Minecraft.getMinecraft().getTextureManager().loadTexture(resourceLocation, new DynamicTexture(image));

            localImageCache.put(filename, resourceLocation);
            return resourceLocation;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void clearImageCache() {
        for (ResourceLocation resource : localImageCache.values()) {
            Minecraft.getMinecraft().getTextureManager().deleteTexture(resource);
        }
        localImageCache.clear();
    }

    public void renderImageLocal(TileEntity tile, String filename, double x, double y, double z, double width, double height) {
        ResourceLocation texture = getImageFromLocal(filename);
        if (texture == null) return;

        GL11.glPushMatrix();
        this.getTextFacing(tile, x, y, z);
        GL11.glTranslated(0.5, 0.0, 0.5);
        GlStateManager.disableLighting();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(-width / 2, height / 2, 0.01, 1, 0);
        tessellator.addVertexWithUV(width / 2, height / 2, 0.01, 0, 0);
        tessellator.addVertexWithUV(width / 2, -height / 2, 0.01, 0, 1);
        tessellator.addVertexWithUV(-width / 2, -height / 2, 0.01, 1, 1);
        tessellator.draw();
        GL11.glPopMatrix();
        //this.clearImageCache();
    }

    public void renderImageLocalBack(TileEntity tile, String filename, double x, double y, double z, double width, double height) {
        ResourceLocation texture = getImageFromLocal(filename);
        if (texture == null) return;

        GL11.glPushMatrix();
        GlStateManager.disableLighting();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);

        Tessellator tessellator = Tessellator.instance;
        this.getTextFacingBack(tile, x, y, z);
        GL11.glTranslated(0.5, 0.0, 0.5);
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(-width / 2, height / 2, 0.01, 1, 0);
        tessellator.addVertexWithUV(width / 2, height / 2, 0.01, 0, 0);
        tessellator.addVertexWithUV(width / 2, -height / 2, 0.01, 0, 1);
        tessellator.addVertexWithUV(-width / 2, -height / 2, 0.01, 1, 1);
        tessellator.draw();
        GL11.glPopMatrix();
        //this.clearImageCache();
    }

    public void renderImage(TileEntity tile, String imageUrl, double x, double y, double z, double width, double height) {
        ResourceLocation texture = getImageFromURL(imageUrl);
        if (texture == null) return;

        GL11.glPushMatrix();
        this.getTextFacing(tile, x, y, z);
        GL11.glTranslated(0.5, 0.0, 0.5);
        GlStateManager.disableLighting();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(-width / 2, height / 2, 0.01, 1, 0);
        tessellator.addVertexWithUV(width / 2, height / 2, 0.01, 0, 0);
        tessellator.addVertexWithUV(width / 2, -height / 2, 0.01, 0, 1);
        tessellator.addVertexWithUV(-width / 2, -height / 2, 0.01, 1, 1);
        tessellator.draw();
        GL11.glPopMatrix();


    }

    public void renderImageBack(TileEntity tile, String imageUrl, double x, double y, double z, double width, double height) {
        ResourceLocation texture = getImageFromURL(imageUrl);
        if (texture == null) return;

        GL11.glPushMatrix();
        GlStateManager.disableLighting();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);

        Tessellator tessellator = Tessellator.instance;
        this.getTextFacingBack(tile, x, y, z);
        GL11.glTranslated(0.5, 0.0, 0.5);
        Minecraft.getMinecraft().renderEngine.bindTexture(texture);
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(-width / 2, height / 2, 0.01, 1, 0);
        tessellator.addVertexWithUV(width / 2, height / 2, 0.01, 0, 0);
        tessellator.addVertexWithUV(width / 2, -height / 2, 0.01, 0, 1);
        tessellator.addVertexWithUV(-width / 2, -height / 2, 0.01, 1, 1);
        tessellator.draw();
        GL11.glPopMatrix();

    }

    public double translateOffset(String direction, double offset) {
        if (direction.equals("x")) {
            switch (this.facing) {
                case 1, 3 -> {
                    return 0;
                }
                case 2 -> {
                    return -offset;
                }
                case 0 -> {
                    return offset;
                }
            }
        } else if (direction.equals("z")) {
            switch (this.facing) {
                case 1 -> {
                    return offset;
                }
                case 2, 0 -> {
                    return 0;
                }
                case 3 -> {
                    return -offset;
                }
            }
        }
        return 0;
    }


    public void drawCenteredString(TileEntity tile, String text, double x, double y, double z, int color) {
        Minecraft mc = Minecraft.getMinecraft();

        GL11.glPushMatrix();
        this.getTextFacing(tile, x, y, z);
        GL11.glTranslated(0.5, 0.0, 0.5);
        float scale = 0.016666668F * 1.3F;
        GlStateManager.disableLighting();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glScalef(-scale, -scale, scale);
        mc.fontRenderer.drawString(text, -(mc.fontRenderer.getStringWidth(text) / 2), 0, color);
        GL11.glDisable(GL11.GL_BLEND);  // 禁用混合模式
        GL11.glEnable(GL11.GL_LIGHTING); // 启用光照
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        this.getTextFacingBack(tile, x, y, z);
        GL11.glTranslated(0.5, 0.0, 0.5);
        GlStateManager.disableLighting();
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0f, 240.0f);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glScalef(-scale, -scale, scale);
        mc.fontRenderer.drawString(text, -(mc.fontRenderer.getStringWidth(text) / 2), 0, color);
        GL11.glDisable(GL11.GL_BLEND);  // 禁用混合模式
        GL11.glEnable(GL11.GL_LIGHTING); // 启用光照
        GL11.glPopMatrix();
    }

}
