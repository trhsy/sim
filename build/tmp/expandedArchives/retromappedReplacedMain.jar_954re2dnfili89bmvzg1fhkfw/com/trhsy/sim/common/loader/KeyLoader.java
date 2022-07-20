package com.trhsy.sim.common.loader;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

/**
 * @ClassName KeyLoader
 * @Description todo 热键加载绑定
 * @Author Tian
 * @Date 2022/5/1323:44
 **/
public class KeyLoader {
    public static KeyBinding showTime;

    public KeyLoader() {
        KeyLoader.showTime = new KeyBinding("key.sim.showTime", Keyboard.KEY_H, "key.categories.sim");

        ClientRegistry.registerKeyBinding(KeyLoader.showTime);
    }
}
