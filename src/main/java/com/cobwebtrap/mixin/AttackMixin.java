package com.cobwebtrap.mixin;

import com.cobwebtrap.CobwebTrapClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.world.World;
import net.minecraft.block.Blocks;
import net.minecraft.util.hit.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerInteractionManager.class)
public class AttackMixin {

    @Inject(method = "attackEntity", at = @At("TAIL"))
    private void onAttackEntity(PlayerEntity player, Entity target, CallbackInfo ci) {
        if (!CobwebTrapClient.enabled) return;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null) return;

        // Only trigger when holding a sword
        ItemStack held = client.player.getMainHandStack();
        if (!held.getItem().toString().contains("sword")) return;

        // Only works on living entities
        if (!(target instanceof LivingEntity livingTarget)) return;

        // Schedule cobweb placement after a short delay on the main thread
        // We need to wait for knockback to be applied server-side then predict landing
        new Thread(() -> {
            try {
                // Wait for knockback to be applied (2 ticks = ~100ms)
                Thread.sleep(100);

                client.execute(() -> {
                    if (client.player == null || client.world == null) return;

                    // Get the attacker (us) and target positions
                    Vec3d attackerPos = client.player.getPos();
                    Vec3d targetPos = target.getPos();

                    // Calculate horizontal knockback direction (away from attacker)
                    double dx = targetPos.x - attackerPos.x;
                    double dz = targetPos.z - attackerPos.z;
                    double dist = Math.sqrt(dx * dx + dz * dz);

                    if (dist == 0) return;

                    // Normalize direction
                    dx /= dist;
                    dz /= dist;

                    // Standard Minecraft knockback values
                    // Base knockback strength (0.4 horizontal per hit)
                    // Knockback enchant adds 0.5 per level - we assume up to KB2
                    double knockbackStrength = 0.4;

                    // Check for knockback enchantment on the held sword
                    int kbLevel = getKnockbackLevel(held);
                    knockbackStrength += kbLevel * 0.5;

                    // Simulate projectile motion to find landing block
                    // Start from target's current feet position
                    double simX = targetPos.x;
                    double simY = targetPos.y;
                    double simZ = targetPos.z;

                    // Initial velocity
                    double velX = dx * knockbackStrength;
                    double velY = 0.4; // upward knockback
                    double velZ = dz * knockbackStrength;

                    // Simulate up to 40 ticks (2 seconds)
                    BlockPos landingPos = null;
                    for (int tick = 0; tick < 40; tick++) {
                        // Apply gravity (0.08 per tick) and drag (0.98 horizontal, 0.98 vertical)
                        velY -= 0.08;
                        velX *= 0.98;
                        velZ *= 0.98;
                        velY *= 0.98;

                        simX += velX;
                        simY += velY;
                        simZ += velZ;

                        // Check if the block below this position is solid
                        BlockPos checkPos = new BlockPos((int) Math.floor(simX), (int) Math.floor(simY), (int) Math.floor(simZ));
                        BlockPos belowPos = checkPos.down();

                        if (!client.world.getBlockState(belowPos).isAir() &&
                            client.world.getBlockState(checkPos).isAir()) {
                            landingPos = checkPos;
                            break;
                        }

                        // Also stop if they hit the ground mid-tick
                        if (simY <= targetPos.y - 0.5 && tick > 2) {
                            landingPos = new BlockPos((int) Math.floor(simX), (int) Math.floor(simY + 1), (int) Math.floor(simZ));
                            break;
                        }
                    }

                    if (landingPos == null) return;

                    // Find cobweb in hotbar
                    int cobwebSlot = -1;
                    for (int i = 0; i < 9; i++) {
                        if (client.player.getInventory().getStack(i).getItem() == Items.COBWEB) {
                            cobwebSlot = i;
                            break;
                        }
                    }

                    if (cobwebSlot == -1) return; // No cobweb in hotbar

                    // Switch to cobweb slot
                    int prevSlot = client.player.getInventory().selectedSlot;
                    client.player.getInventory().selectedSlot = cobwebSlot;

                    // Place cobweb at predicted landing position
                    // We place it on top of the block below landing pos
                    BlockPos placeOn = landingPos.down();
                    BlockHitResult hitResult = new BlockHitResult(
                            Vec3d.ofCenter(landingPos),
                            Direction.UP,
                            placeOn,
                            false
                    );

                    client.interactionManager.interactBlock(client.player, Hand.MAIN_HAND, hitResult);
                    client.player.swingHand(Hand.MAIN_HAND);

                    // Switch back to previous slot
                    client.player.getInventory().selectedSlot = prevSlot;
                });

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    private int getKnockbackLevel(ItemStack stack) {
        // Check for knockback enchantment
        var enchants = stack.getEnchantments();
        // Iterate NBT to find knockback level
        try {
            var list = enchants.getEnchantments();
            for (var entry : list) {
                if (entry.toString().contains("knockback")) {
                    return enchants.getLevel(entry.getKey().get());
                }
            }
        } catch (Exception ignored) {}
        return 0;
    }
}
