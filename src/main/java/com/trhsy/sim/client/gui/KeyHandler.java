package com.trhsy.sim.client.gui;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

public class KeyHandler {
    public static final int FOLKCLOSE_KEY = 0;
    private static final String[] keyDesc = new String[]{"key.sim.desc"};
    private static final int[] keyValues = new int[]{1};
    private final KeyBinding[] keys;

    public KeyHandler() {
        this.keys = new KeyBinding[keyValues.length];

        for(int i = 0; i < keyValues.length; ++i) {
            this.keys[i] = new KeyBinding(keyDesc[i], keyValues[i], "key.sim.category");
            ClientRegistry.registerKeyBinding(this.keys[i]);
        }

    }

    @SubscribeEvent
    public void onKeyInput(KeyInputEvent event) {
        if (!FMLClientHandler.instance().isGUIOpen(GuiChat.class)) {
            int key = Keyboard.getEventKey();
            boolean isDown = Keyboard.getEventKeyState();
            if (isDown && key == keyValues[0]) {
            }
        }

    }
}
