package com.imgood.hyperdimensionaltech.config;

import com.imgood.hyperdimensionaltech.HyperdimensionalTech;
import com.imgood.hyperdimensionaltech.HyperdimensionalTechFeatures;
import net.minecraftforge.common.config.Configuration;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class HTConfigurations {
    //类本身需要的属性（开发时不需要动它
    public static Configuration config;
    public static Map<String, Boolean> features = new HashMap<>();
    public static boolean activateCombatStats;
    //用于定义机器或模组其他功能的字段（开发机器或新功能时需要申明
    public static final String HyperdimensionalResonanceEvolver = "HyperdimensionalResonanceEvolver";
    public static double secondsOfHyperdimensionalResonanceProgressCycleTime = 64.0;
    public static boolean EnableRenderDefaultHyperdimensionalResonanceEvolver = true;
    public static byte Mode_Default_HyperdimensionalResonanceEvolver;
    public static int Parallel_HighSpeedMode_HyperdimensionalResonanceEvolver;
    public static int Parallel_HighParallelMode_HyperdimensionalResonanceEvolver;
    public static int TickPerProgressing_WirelessMode_HyperdimensionalResonanceEvolver;

    /**
     * 刷新Config（开发时不需要动它
     *
     * @return 布尔类型刷新结果
     */
    public static boolean refreshConfig() {

        /* features */
        HyperdimensionalTech.logger.info("Put block feature in config file...");
        for (HyperdimensionalTechFeatures feature : HyperdimensionalTechFeatures.values()) {
            HyperdimensionalTech.logger.info("Put" + feature + "in config file.");
            features.put(
                featureName(feature),
                config.get("features", featureName(feature), true)
                    .getBoolean(true));
        }

        if (config.hasChanged()) {
            HyperdimensionalTech.logger.info("Config file has changed,saving config...");
            config.save();
        }
        return true;
    }

    /**
     * 检查模组功能/内容是否启用（开发时不需要动它
     *
     * @param feature HyperdimensionalTechTeatures类型的本模组内容
     * @return 布尔类型启用结果
     */
    public static boolean featureEnabled(HyperdimensionalTechFeatures feature) {
        boolean enabled = features.getOrDefault(featureName(feature), false);
        HyperdimensionalTech.logger.info(feature + " is " + (enabled ? "enabled" : "disabled."));
        return enabled;
    }

    /**
     * Makes the old camelCase names from the new CONSTANT_CASE names
     * 将新的CONSTANT_CASE名称转换为旧的camelCase名称的对象（开发时不需要动它
     *
     * @param feature HyperdimensionalTechFeatures对象
     * @return 转换结果
     */
    public static String featureName(HyperdimensionalTechFeatures feature) {
        String[] words = feature.name()
            .toLowerCase(Locale.ENGLISH)
            .split("_");
        if (words.length == 1) {
            return words[0];
        }

        String ret = words[0];
        for (int i = 1; i < words.length; i++) {
            ret += StringUtils.capitalize(words[i]);
        }
        return ret;
    }

    @Deprecated
    public static boolean featureEnabled(String feature) {
        return false;
    }

    /**
     * 静态方法，模组加载时候调用，用于同步（开发时将需要同步的内容申明在这
     *
     * @param configFile 同步的文件对象
     */
    public static void synchronizeConfiguration(File configFile) {
        HyperdimensionalTech.logger.info("Synchronizing configuration...");
        Configuration configuration = new Configuration(configFile);
        secondsOfHyperdimensionalResonanceProgressCycleTime = Double.parseDouble(configuration.getString("secondsOfHyperdimensionalResonanceProgressCycleTime",
            HyperdimensionalResonanceEvolver, String.valueOf(secondsOfHyperdimensionalResonanceProgressCycleTime), "Seconds of HyperdimensionalResonance one progress time. Type: double, turn to tick time."));
        EnableRenderDefaultHyperdimensionalResonanceEvolver = configuration.getBoolean("EnableRenderDefaultHyperdimensionalResonanceEvolver", HyperdimensionalResonanceEvolver, EnableRenderDefaultHyperdimensionalResonanceEvolver, "Enable Render of Hyperdimensional Resonance Evolver when placing a new one.");

        activateCombatStats = configuration.getBoolean("activateCombatStats", "CombatStats", activateCombatStats, "decide whether to enable the combatstats system(WIP).DO NOT USE IT FOR NOW!");
        Mode_Default_HyperdimensionalResonanceEvolver = (byte) configuration.getInt("Mode_Default_HyperdimensionalResonanceEvolver", "HyperdimensionalResonanceEvolver", Mode_Default_HyperdimensionalResonanceEvolver, 0, 1, "The default mode when deploy HyperdimensionalResonanceEvolver. 0=HighSpeedMode, 1=HighParallelMode. Type: byte");
        if (Mode_Default_HyperdimensionalResonanceEvolver < 0 || Mode_Default_HyperdimensionalResonanceEvolver > 1) {
            Mode_Default_HyperdimensionalResonanceEvolver = 0;
        }
        Parallel_HighSpeedMode_HyperdimensionalResonanceEvolver = configuration.getInt("Parallel_HighSpeedMode_HyperdimensionalResonanceEvolver", "HyperdimensionalResonanceEvolver", Parallel_HighSpeedMode_HyperdimensionalResonanceEvolver, 1, Integer.MAX_VALUE, "Max Parallel of Hyperdimensional Resonance Evolver high speed mode. Type: int");
        TickPerProgressing_WirelessMode_HyperdimensionalResonanceEvolver = configuration.getInt("TickPerProgressing_WirelessMode_HyperdimensionalResonanceEvolver", "HyperdimensionalResonanceEvolver", TickPerProgressing_WirelessMode_HyperdimensionalResonanceEvolver, 1, 16384, "How many ticks per progressing cost in Wireless mode of Hyperdimensional Resonance Evolver. Type: int");
        HyperdimensionalTech.logger.info("Synchronize configuration finished.");
        if (configuration.hasChanged()) {
            configuration.save();
        }
    }

    static {
        activateCombatStats = false;
        Mode_Default_HyperdimensionalResonanceEvolver = 0;
        Parallel_HighSpeedMode_HyperdimensionalResonanceEvolver = 1024;
        Parallel_HighParallelMode_HyperdimensionalResonanceEvolver = Integer.MAX_VALUE;
        TickPerProgressing_WirelessMode_HyperdimensionalResonanceEvolver = 128;
    }
}
