package com.imgood.hyperdimensionaltech.block.textures;

import static com.imgood.hyperdimensionaltech.utils.Enums.MOD;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import com.imgood.hyperdimensionaltech.HyperdimensionalTech;
import com.imgood.hyperdimensionaltech.utils.AutoMap;

import gregtech.api.GregTech_API;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.objects.GT_RenderedTexture;

public class TexturesGtBlock {

    private static AutoMap<Runnable> mCustomiconMap = new AutoMap<>();

    static {}

    public static class CustomIcon implements IIconContainer, Runnable {

        protected IIcon mIcon;
        protected String mIconName;
        protected String mModID;

        public CustomIcon(final String aIconName) {
            this(MOD.ID, aIconName);
        }

        public CustomIcon(final String aModID, final String aIconName) {
            this.mIconName = aIconName;
            this.mModID = aModID;
            mCustomiconMap.put(this);
            HyperdimensionalTech.logger.warn("Constructing a Custom Texture. " + this.mIconName);
            GregTech_API.sGTBlockIconload.add(this);
        }

        @Override
        public IIcon getIcon() {
            return this.mIcon;
        }

        @Override
        public IIcon getOverlayIcon() {
            return null;
        }

        @Override
        public void run() {
            this.mIcon = GregTech_API.sBlockIcons.registerIcon(this.mModID + ":" + this.mIconName);
            HyperdimensionalTech.logger.warn(
                "FIND ME _ Processing texture: " + this.getTextureFile()
                    .getResourcePath());
        }

        @Override
        public ResourceLocation getTextureFile() {
            return TextureMap.locationBlocksTexture;
        }
    }

    public static GT_RenderedTexture getTextureFromIcon(CustomIcon aIcon, short[] aRGB) {
        return new GT_RenderedTexture(aIcon, aRGB);
    }

    // spotless:on
    public static final CustomIcon HyperDimensionalResonanceEvolverField = new CustomIcon(
        "rendering/HyperDimensionalResonanceEvolverField");

}
