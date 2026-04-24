package com.greninja.cobwebtrap.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigManager {
    private static final Logger LOGGER = LoggerFactory.getLogger("cobwebtrap");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String CONFIG_PATH = "config/cobwebtrap.json";
    private static ModConfig currentConfig;

    public static void loadConfig() {
        File configFile = new File(CONFIG_PATH);
        
        if (!configFile.exists()) {
            LOGGER.info("Config file not found. Creating default config...");
            currentConfig = new ModConfig();
            saveConfig();
        } else {
            try (FileReader reader = new FileReader(configFile)) {
                currentConfig = GSON.fromJson(reader, ModConfig.class);
                LOGGER.info("Config loaded successfully!");
            } catch (IOException e) {
                LOGGER.error("Failed to load config. Using defaults.", e);
                currentConfig = new ModConfig();
            }
        }
    }

    public static void saveConfig() {
        File configFile = new File(CONFIG_PATH);
        configFile.getParentFile().mkdirs();
        
        try (FileWriter writer = new FileWriter(configFile)) {
            GSON.toJson(currentConfig, writer);
            LOGGER.info("Config saved successfully!");
        } catch (IOException e) {
            LOGGER.error("Failed to save config.", e);
        }
    }

    // Getters
    public static boolean isModEnabled() { return currentConfig.modEnabled; }
    public static void setModEnabled(boolean value) { currentConfig.modEnabled = value; saveConfig(); }
    
    public static int getPlacementRadius() { return currentConfig.placementRadius; }
    public static void setPlacementRadius(int value) { currentConfig.placementRadius = value; saveConfig(); }
    
    public static int getMaxCobwebs() { return currentConfig.maxCobwebsPerHit; }
    public static void setMaxCobwebs(int value) { currentConfig.maxCobwebsPerHit = value; saveConfig(); }
    
    public static boolean isPlayersOnly() { return currentConfig.playersOnly; }
    public static void setPlayersOnly(boolean value) { currentConfig.playersOnly = value; saveConfig(); }
    
    public static boolean isMobsOnly() { return currentConfig.mobsOnly; }
    public static void setMobsOnly(boolean value) { currentConfig.mobsOnly = value; saveConfig(); }
    
    public static int getCobwebDuration() { return currentConfig.cobwebDurationTicks; }
    public static void setCobwebDuration(int value) { currentConfig.cobwebDurationTicks = value; saveConfig(); }
    
    public static boolean isParticleEnabled() { return currentConfig.enableParticles; }
    public static void setParticleEnabled(boolean value) { currentConfig.enableParticles = value; saveConfig(); }
    
    public static boolean isSoundEnabled() { return currentConfig.enableSound; }
    public static void setSoundEnabled(boolean value) { currentConfig.enableSound = value; saveConfig(); }
    
    public static double getChance() { return currentConfig.trapChance; }
    public static void setChance(double value) { currentConfig.trapChance = value; saveConfig(); }
    
    public static int getSpreadPattern() { return currentConfig.spreadPattern; }
    public static void setSpreadPattern(int value) { currentConfig.spreadPattern = value; saveConfig(); }
    
    public static boolean isCooldownEnabled() { return currentConfig.cooldownEnabled; }
    public static void setCooldownEnabled(boolean value) { currentConfig.cooldownEnabled = value; saveConfig(); }
    
    public static int getCooldownTicks() { return currentConfig.cooldownTicks; }
    public static void setCooldownTicks(int value) { currentConfig.cooldownTicks = value; saveConfig(); }
}