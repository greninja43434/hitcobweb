package com.cobwebtrap;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class CobwebTrapKeyBinding {

    private static KeyBinding toggleKey;

    public static void register() {
        toggleKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.cobwebtrap.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_J,
                "category.cobwebtrap"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.wasPressed()) {
                CobwebTrapClient.enabled = !CobwebTrapClient.enabled;
                if (client.player != null) {
                    client.player.sendMessage(
                        net.minecraft.text.Text.literal(
                            "[CobwebTrap] " + (CobwebTrapClient.enabled ? "§aEnabled" : "§cDisabled")
                        ), true
                    );
                }
            }
        });
    }
}
