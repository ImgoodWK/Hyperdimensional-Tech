package com.imgood.hyperdimensionaltech;

import com.cricketcraft.chisel.api.ChiselTabs;
import com.imgood.hyperdimensionaltech.client.render.SubmapManagerAntiblockFrameless;
import com.imgood.hyperdimensionaltech.config.HTConfigurations;
import cpw.mods.fml.common.Loader;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemDye;
import net.minecraftforge.oredict.OreDictionary;
import team.chisel.Chisel;
import team.chisel.block.BlockCarvable;
import team.chisel.block.BlockCarvableGlowie;
import team.chisel.config.Configurations;

public enum HyperdimensionalTechFeatures {

    ANTIBLOCKFRAMELESS {
        @Override
        void addBlocks() {
            HyperdimensionalTech.logger.info("Adding blocks:AntiblockFrameless");
            BlockCarvable antiBlockFrameless = (BlockCarvable) new BlockCarvableGlowie(Material.rock)
                .setCreativeTab(ChiselTabs.tabOtherChiselBlocks);

            if (!Configurations.allowChiselCrossColors) {
                antiBlockFrameless.carverHelper.forbidChiseling = true;
            }

            for (int i = 0; i < 16; i++) {
                HyperdimensionalTech.logger.info("Adding blocks:AntiblockFrameless" + ItemDye.field_150921_b[i] + ".desc");
                antiBlockFrameless.carverHelper.addVariation(
                    "tile.antiBlockFrameLess." + ItemDye.field_150921_b[i] + ".desc",
                    i,
                    new SubmapManagerAntiblockFrameless(ItemDye.field_150921_b[i]));
            }
            HyperdimensionalTech.logger.info("register blocks:AntiblockFrameless");
            antiBlockFrameless.carverHelper.registerAll(antiBlockFrameless, "antiBlockFrameless");
            OreDictionary.registerOre("antiBlockFrameless", antiBlockFrameless);
        }
    };

    private HyperdimensionalTechFeatures parent;

    private String requiredMod;

    static void preInit() {
        HyperdimensionalTech.logger.info("Starting pre-init...");
        loadBlocks();
        loadItems();
        HyperdimensionalTech.logger.info("Pre-init finished.");
    }

    static void init() {
        HyperdimensionalTech.logger.info("Starting init...");
        loadRecipes();
        HyperdimensionalTech.logger.info("Init finished.");
    }

    void addBlocks() {
        ;
    }

    private static void loadBlocks() {
        HyperdimensionalTech.logger.info("Loading blocks...");
        int num = 0;
        for (HyperdimensionalTechFeatures f : values()) {
            HyperdimensionalTech.logger.info("Enabled check");
            HyperdimensionalTech.logger.info(f.enabled());
            HyperdimensionalTech.logger.info(f);
            f.addBlocks();
            ++num;
        }
    }

    private static void loadItems() {
        Chisel.logger.info("Loading items...");
        int num = 0;
        for (HyperdimensionalTechFeatures f : values()) {
            if (f.enabled()) {
                f.addItems();
                ++num;
            } else {
                logDisabled(f);
            }
        }
        Chisel.logger.info(num + " Feature's items loaded.");
    }

    private static void logDisabled(HyperdimensionalTechFeatures f) {
        if (!f.hasParentFeature() && f.parent != null) {
            Chisel.logger.info(
                "Skipping feature {} as its parent feature {} was disabled.",
                HTConfigurations.featureName(f),
                HTConfigurations.featureName(f.parent));
        } else if (!f.hasRequiredMod() && f.getRequiredMod() != null) {
            Chisel.logger.info(
                "Skipping feature {} as its required mod {} was missing.",
                HTConfigurations.featureName(f),
                f.getRequiredMod());
        } else {
            Chisel.logger
                .info("Skipping feature {} as it was disabled in the config.", HTConfigurations.featureName(f));
        }
    }

    void addItems() {
        ;
    }

    private final boolean hasParentFeature() {
        return parent == null || parent.enabled();
    }

    public boolean enabled() {
        return HTConfigurations.featureEnabled(this) && hasRequiredMod() && hasParentFeature();
    }

    private final boolean hasRequiredMod() {
        return getRequiredMod() == null || Loader.isModLoaded(getRequiredMod());
    }

    private String getRequiredMod() {
        return requiredMod;
    }

    private static int meta = 0;

    private static void loadRecipes() {
        Chisel.logger.info("Loading recipes...");
        int num = 0;
        for (HyperdimensionalTechFeatures f : values()) {
            if (f.enabled()) {
                if (f.needsMetaRecipes()) {
                    for (int i = 0; i < 16; i++) {
                        meta = i;
                        f.addRecipes();
                    }
                    meta = 0;
                } else {
                    f.addRecipes();
                }
                ++num;
            } else {
                logDisabled(f);
            }
        }
        Chisel.logger.info(num + " Feature's recipes loaded.");
    }

    void addRecipes() {
        ;
    }

    boolean needsMetaRecipes() {
        return false;
    }
}
