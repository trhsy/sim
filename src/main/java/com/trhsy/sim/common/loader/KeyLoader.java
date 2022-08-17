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
        try {
            KeyLoader.showTime = new KeyBinding("key.sim.showTime", Keyboard.KEY_NONE, "key.categories.sim");
            ClientRegistry.registerKeyBinding(KeyLoader.showTime);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("CraftGuide-出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }
}
