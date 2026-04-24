package com.greninja.cobwebtrap.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;

public class CobwebPlacerUtil {
    
    public static boolean placeCobwebAt(ServerWorld world, BlockPos pos) {
        // Check if block is replaceable
        BlockState blockState = world.getBlockState(pos);
        
        if (!blockState.getMaterial().isReplaceable()) {
            return false;
        }
        
        // Place cobweb
        world.setBlockState(pos, Blocks.COBWEB.getDefaultState());
        return true;
    }
    
    public static void playPlacementEffects(ServerWorld world, BlockPos pos) {
        // Particle effect
        if (world != null) {
            world.spawnParticles(
                ParticleTypes.CLOUD,
                pos.getX() + 0.5,
                pos.getY() + 0.5,
                pos.getZ() + 0.5,
                10,
                0.5, 0.5, 0.5,
                0.1
            );
        }
        
        // Sound effect
        world.playSound(
            null,
            pos,
            SoundEvents.ENTITY_SPIDER_AMBIENT,
            SoundCategory.BLOCKS,
            1.0f,
            1.0f
        );
    }
}