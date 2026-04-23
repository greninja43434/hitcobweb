package com.cobwebtrap;

import net.fabricmc.api.ClientModInitializer;

public class CobwebTrapClient implements ClientModInitializer {
    public static boolean enabled = false;

    @Override
    public void onInitializeClient() {
        CobwebTrapKeyBinding.register();
    }
}
