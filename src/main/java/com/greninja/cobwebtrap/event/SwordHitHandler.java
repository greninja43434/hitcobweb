package com.greninja.cobwebtrap.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.greninja.cobwebtrap.config.ConfigManager;
import com.greninja.cobwebtrap.util.CobwebPlacerUtil;

import java.util.HashMap;
import java.util.UUID;

public class SwordHitHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger("cobwebtrap");
    private static final HashMap<UUID, Long> lastHitTime = new HashMap<>();

    public static void handleSwordHit(PlayerEntity player, Entity target) {
        if (!(target instanceof LivingEntity livingEntity)) return;
        
        // Check if target should be affected
        if (!shouldAffectTarget(target)) return;
        
        // Check cooldown
        if (!isCooldownPassed(player.getUuid())) return;
        
        // Check trap chance
        if (Math.random() > ConfigManager.getChance()) return;
        
        // Place cobwebs at entity's position
        placeCobwebsAtTarget(player, livingEntity);
        
        // Update last hit time
        lastHitTime.put(player.getUuid(), System.currentTimeMillis());
    }

    private static boolean shouldAffectTarget(Entity entity) {
        if (entity instanceof PlayerEntity) {
            return !ConfigManager.isMobsOnly();
        }
        if (entity instanceof LivingEntity) {
            return !ConfigManager.isPlayersOnly();
        }
        return false;
    }

    private static boolean isCooldownPassed(UUID playerId) {
        if (!ConfigManager.isCooldownEnabled()) return true;
        
        long lastHit = lastHitTime.getOrDefault(playerId, 0L);
        long cooldownMs = ConfigManager.getCooldownTicks() * 50L; // 50ms per tick
        return System.currentTimeMillis() - lastHit >= cooldownMs;
    }

    private static void placeCobwebsAtTarget(PlayerEntity player, LivingEntity target) {
        if (!(player.getWorld() instanceof ServerWorld serverWorld)) return;
        
        Vec3d targetPos = target.getPos();
        BlockPos centerPos = target.getBlockPos();
        int radius = ConfigManager.getPlacementRadius();
        int maxCobwebs = ConfigManager.getMaxCobwebs();
        int placed = 0;
        
        int pattern = ConfigManager.getSpreadPattern();
        
        switch (pattern) {
            case 0: // Circle pattern
                placed = placeCirclePattern(serverWorld, centerPos, radius, maxCobwebs);
                break;
            case 1: // Square pattern
                placed = placeSquarePattern(serverWorld, centerPos, radius, maxCobwebs);
                break;
            case 2: // Random pattern
                placed = placeRandomPattern(serverWorld, centerPos, radius, maxCobwebs);
                break;
        }
        
        if (placed > 0) {
            CobwebPlacerUtil.playPlacementEffects(serverWorld, centerPos);
            LOGGER.info("Placed {} cobwebs around {}", placed, centerPos);
        }
    }

    private static int placeCirclePattern(ServerWorld world, BlockPos center, int radius, int maxCobwebs) {
        int placed = 0;
        double angleStep = (2 * Math.PI) / maxCobwebs;
        
        for (int i = 0; i < maxCobwebs; i++) {
            double angle = i * angleStep;
            int x = (int) (center.getX() + radius * Math.cos(angle));
            int z = (int) (center.getZ() + radius * Math.sin(angle));
            
            if (CobwebPlacerUtil.placeCobwebAt(world, new BlockPos(x, center.getY(), z))) {
                placed++;
            }
        }
        return placed;
    }

    private static int placeSquarePattern(ServerWorld world, BlockPos center, int radius, int maxCobwebs) {
        int placed = 0;
        int side = (int) Math.sqrt(maxCobwebs);
        
        for (int x = -radius; x <= radius && placed < maxCobwebs; x++) {
            for (int z = -radius; z <= radius && placed < maxCobwebs; z++) {
                BlockPos pos = center.add(x, 0, z);
                if (CobwebPlacerUtil.placeCobwebAt(world, pos)) {
                    placed++;
                }
            }
        }
        return placed;
    }

    private static int placeRandomPattern(ServerWorld world, BlockPos center, int radius, int maxCobwebs) {
        int placed = 0;
        Random random = world.getRandom();
        
        for (int i = 0; i < maxCobwebs; i++) {
            int x = center.getX() + random.nextInt(radius * 2 + 1) - radius;
            int y = center.getY() + random.nextInt(3) - 1;
            int z = center.getZ() + random.nextInt(radius * 2 + 1) - radius;
            
            if (CobwebPlacerUtil.placeCobwebAt(world, new BlockPos(x, y, z))) {
                placed++;
            }
        }
        return placed;
    }
}