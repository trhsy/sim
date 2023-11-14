package com.trhsy.sim.gui;

import com.google.common.collect.Lists;
import com.trhsy.sim.loader.ConfigLoader;
import com.trhsy.sim.loader.ModSimLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.gui
 * @ClassName: ConfigGui
 * @Description:
 * @date 2023/10/19 下午 2:55
 */
public class ConfigGui extends GuiConfig {
    public ConfigGui(GuiScreen parentScreen) {
        super(parentScreen, getConfigElements(), "sim", false, false, I18n.format("configgui.title"));
    }
    private static List<IConfigElement> getConfigElements() {
        List<IConfigElement> list = Lists.newArrayList();
        try {
            list.add(new ConfigElement(ConfigLoader.Gameplay));
            list.add(new ConfigElement(ConfigLoader.Nameplay));
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];
            ModSimLoader.log.error("getConfigElements出错了：" + e.getMessage()+"行数："+element.getLineNumber());
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
        public boolean hasConfigGui() {
            return true;
        }

        @Override
        public GuiScreen createConfigGui(GuiScreen parentScreen) {
            return new ConfigGui(parentScreen);
        }

        @Override
        public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
            return null;
        }
    }
}
