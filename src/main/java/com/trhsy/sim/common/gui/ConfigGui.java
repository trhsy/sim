package com.trhsy.sim.common.gui;

import com.google.common.collect.Lists;
import com.trhsy.sim.common.loader.ConfigLoader;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.List;
import java.util.Set;

/**
 * @ClassName ConfigGui
 * @Description todo
 * @Author Tian
 * @Date 2022/5/121:11
 **/
public class ConfigGui extends GuiConfig {
    public ConfigGui(GuiScreen parentScreen) {
        super(parentScreen, getConfigElements(), "sim", false, false, I18n.format("configgui.title"));
    }
    private static List<IConfigElement> getConfigElements() {
        List<IConfigElement> list = Lists.newArrayList();
        try {
            //        list.add(new ConfigElement(ConfigLoader.Modules));
            list.add(new ConfigElement(ConfigLoader.Gameplay));
            list.add(new ConfigElement(ConfigLoader.Nameplay));
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("getConfigElements出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

        return list;
    }

    public static class ConfigGuiFactory implements IModGuiFactory {
        public ConfigGuiFactory() {
        }

        @Override
        public void initialize(Minecraft minecraftInstance) {
        }
        @Override
        public Class<? extends GuiScreen> mainConfigGuiClass() {
            return ConfigGui.class;
        }
        @Override
        public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
            return null;
        }
        @Override
        public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
            return null;
        }
    }
}
