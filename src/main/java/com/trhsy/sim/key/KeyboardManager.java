package com.trhsy.sim.key;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.proxy.ClientProxy;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.key
 * @ClassName: KeyboardManager
 * @Description: 按键按下后
 * @date 2023.12.6 15:19
 */
@Mod.EventBusSubscriber
public class KeyboardManager {
    /**
     * 初始化
     */
    public static void init() {
        for (KeyBinding key : ClientProxy.KEY_BINDINGS) {
            ClientRegistry.registerKeyBinding(key);
        }
        ModSimLoader.log.info("注册了 d% 个按键",ClientProxy.KEY_BINDINGS.size());
    }

    /**
     * 捕获当按键按下时
     * @param event
     */
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onKeyPressed(InputEvent.KeyInputEvent event) {
        //当按下EFFECTME键(V键)时
        if (ClientProxy.SUMMON.isPressed()) {
            ModSimLoader.loadAllBuildings();
            ModSimLoader.log.info("加载所有建筑");
        }
    }
}
