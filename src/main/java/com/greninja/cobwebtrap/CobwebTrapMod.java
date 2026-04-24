package com.greninja.cobwebtrap;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.util.ActionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.greninja.cobwebtrap.config.ConfigManager;
import com.greninja.cobwebtrap.event.SwordHitHandler;

public class CobwebTrapMod implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("cobwebtrap");
    public static final String MOD_ID = "cobwebtrap";

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Cobweb Trap Mod...");
        
        // Load configuration
        ConfigManager.loadConfig();
        
        // Register attack event
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (!world.isClient && ConfigManager.isModEnabled()) {
                SwordHitHandler.handleSwordHit(player, entity);
            }
            return ActionResult.PASS;
        });
        
        LOGGER.info("Cobweb Trap Mod initialized successfully!");
    }
}