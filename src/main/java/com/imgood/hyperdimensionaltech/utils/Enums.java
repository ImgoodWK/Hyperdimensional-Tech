package com.imgood.hyperdimensionaltech.utils;

import java.util.Locale;

import net.minecraft.util.ResourceLocation;

import cpw.mods.fml.common.Loader;

public enum Enums {

    Test(Names.TEST),
    MOD(Names.MOD_ID),

    ;

    public static class Names {

        public static final String TEST = "test";
        public static final String MOD_ID = "hyperdimensionaltech";

    }

    public final String ID;
    public final String resourceDomain;
    private Boolean modLoaded;

    Enums(String ID) {
        this.ID = ID;
        this.resourceDomain = ID.toLowerCase(Locale.ENGLISH);
    }

    public boolean isModLoaded() {
        if (this.modLoaded == null) {
            this.modLoaded = Loader.isModLoaded(ID);
        }
        return this.modLoaded;
    }

    public String getResourcePath(String... path) {
        return this.getResourceLocation(path)
            .toString();
    }

    public ResourceLocation getResourceLocation(String... path) {
        return new ResourceLocation(this.resourceDomain, String.join("/", path));
    }
}
