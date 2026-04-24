package com.greninja.cobwebtrap.config;

public class ModConfig {
    // Basic settings
    public boolean modEnabled = true;
    public String toggleKeybind = "K";
    
    // Cobweb placement
    public int placementRadius = 3;
    public int maxCobwebsPerHit = 8;
    public int cobwebDurationTicks = 200; // 10 seconds default
    
    // Target filtering
    public boolean playersOnly = false;
    public boolean mobsOnly = false;
    public boolean includeArmored = true;
    
    // Visual and audio
    public boolean enableParticles = true;
    public boolean enableSound = true;
    
    // Advanced settings
    public double trapChance = 1.0; // 0.0 to 1.0 (100% chance by default)
    public int spreadPattern = 0; // 0=circle, 1=square, 2=random
    
    // Cooldown settings
    public boolean cooldownEnabled = true;
    public int cooldownTicks = 20; // 1 second default
    
    // Minimum sword damage requirement
    public float minimumDamage = 2.0f;
}