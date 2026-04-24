package com.greninja.cobwebtrap.client;

import net.fabricmc.api.ClientModInitializer;
import com.greninja.cobwebtrap.keybind.KeyBindManager;

public class ClientInitializer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        KeyBindManager.registerKeybinds();
    }
}