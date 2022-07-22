package com.trhsy.sim.common.gui;

import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

public class KeyHandler {
    public static final int FOLKCLOSE_KEY = 0;
    private static final String[] keyDesc = new String[]{"key.sim.desc"};
    private static final int[] keyValues = {Keyboard.KEY_ESCAPE};
    private final KeyBinding[] keys;

    public KeyHandler() {
        this.keys = new KeyBinding[keyValues.length];

        try {
            for(int i = 0; i < keyValues.length; i++) {
                this.keys[i] = new KeyBinding(keyDesc[i], keyValues[i], "key.sim.category");
                ClientRegistry.registerKeyBinding(this.keys[i]);
            }
        } catch (Exception e) {
            ModSimReloaded.log.error("出错了：" + e.getMessage());
        }
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        try {
            if (!FMLClientHandler.instance().isGUIOpen(GuiChat.class)) {
                int key = Keyboard.getEventKey();
                boolean isDown = Keyboard.getEventKeyState();
                if (isDown && key == keyValues[0]) {
                }
            }
        } catch (Exception e) {
            ModSimReloaded.log.error("onKeyInput出错了：" + e.getMessage());
        }


    }
}
