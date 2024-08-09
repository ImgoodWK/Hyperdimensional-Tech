package com.imgood.hyperdimensionaltech.utils;

import com.imgood.hyperdimensionaltech.tiles.rendertiles.TileHolographicDisplay;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.imgood.hyperdimensionaltech.utils.Enums.Names.MOD_ID;

/**
 * @program: Hyperdimensional-Tech
 * @description: 纹理管理工具，缓存纹理对象、减少IO操作、异步加载
 * @author: Imgood
 * @create: 2024-08-07 09:18
 **/
public class HT_TextureManager {
    private static final Map<String, ResourceLocation> textureCache = new ConcurrentHashMap<>();
    private static final Set<String> downloadingTextures = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private static final String CACHE_DIR = "cache/holographic_images/";

    @SideOnly(Side.CLIENT)
    public static ResourceLocation getOrLoadTexture(String filename, TileEntity entity, int index) throws NoSuchAlgorithmException {
        if (!(entity instanceof TileHolographicDisplay)) {
            return null;
        }
        if (textureCache.containsKey(filename)) {
            return textureCache.get(filename);
        } else if (isTextureDownloading(filename)) {
            return null;
        } else {
            ResourceLocation texture = loadTexture((TileHolographicDisplay) entity, filename, ((TileHolographicDisplay) entity).getImgURL(index), index);
            if (texture != null) {
                textureCache.put(filename, texture);
            }
            return texture;
        }
    }

    @SideOnly(Side.CLIENT)
    private static ResourceLocation loadTexture(TileHolographicDisplay tileHolographicDisplay, final String filename, final String url, int index) {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
            return null;
        }
        try {
            File file = new File(Minecraft.getMinecraft().mcDataDir, CACHE_DIR + filename + ".png");
            if (!file.exists()) {
                loadImageAsync(tileHolographicDisplay, index, url, filename);
                return null;
            }

            final BufferedImage image = ImageIO.read(file);
            final String textureName = "local_texture_" + filename.hashCode();
            final ResourceLocation resourceLocation = new ResourceLocation(MOD_ID, textureName);

            Minecraft.getMinecraft().func_152344_a(() -> {
                DynamicTexture dynamicTexture = new DynamicTexture(image);
                Minecraft.getMinecraft().getTextureManager().loadTexture(resourceLocation, dynamicTexture);
            });

            return resourceLocation;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void loadImageAsync(TileEntity tile, int index, String imageUrl, String filename) {
        if (isTextureDownloading(filename)) {
            return; // 如果已经在下载，直接返回
        }
        downloadingTextures.add(filename);

        new Thread(() -> {
            try {
                File cacheDir = new File(Minecraft.getMinecraft().mcDataDir, CACHE_DIR);
                if (!cacheDir.exists()) {
                    cacheDir.mkdirs();
                }

                String imageHash = calculateImageHash(imageUrl);
                File cachedFile = new File(cacheDir, imageHash + ".png");
                if (tile instanceof TileHolographicDisplay) {
                    ((TileHolographicDisplay) tile).setImgPath(index, imageHash);
                }

                if (!cachedFile.exists()) {
                    downloadAndSaveImage(imageUrl, cachedFile);
                }

                Minecraft.getMinecraft().func_152344_a(() -> {
                    try {
                        ResourceLocation texture = loadTexture((TileHolographicDisplay) tile, filename, imageUrl, index);
                        if (texture != null) {
                            textureCache.put(filename, texture);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        downloadingTextures.remove(filename);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                downloadingTextures.remove(filename);
            }
        }).start();
    }

    public static boolean isTextureDownloading(String filename) {
        return downloadingTextures.contains(filename);
    }

    private static void downloadAndSaveImage(String imageUrl, File outputFile) {
        URL url = null;
        try {
            url = new URL(imageUrl);

        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        FileOutputStream fos = new FileOutputStream(outputFile);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
        rbc.close();
        } catch (IOException e) {
            //throw new RuntimeException(e);
        }
    }

    @SideOnly(Side.CLIENT)
    public static void clearCache() {
        for (ResourceLocation resource : textureCache.values()) {
            Minecraft.getMinecraft().getTextureManager().deleteTexture(resource);
        }
        textureCache.clear();
    }

    private static String calculateImageHash(String imageUrl) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = md.digest(imageUrl.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
