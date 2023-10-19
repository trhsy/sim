package com.trhsy.sim.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
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
    public ConfigGui(GuiScreen parentScreen, String modid, String title) {
        super(parentScreen, modid, title);
    }

    public ConfigGui(GuiScreen parentScreen, String modID, boolean allRequireWorldRestart, boolean allRequireMcRestart, String title, Class<?>... configClasses) {
        super(parentScreen, modID, allRequireWorldRestart, allRequireMcRestart, title, configClasses);
    }

    public ConfigGui(GuiScreen parentScreen, List<IConfigElement> configElements, String modID, String configID, boolean allRequireWorldRestart, boolean allRequireMcRestart, String title) {
        super(parentScreen, configElements, modID, configID, allRequireWorldRestart, allRequireMcRestart, title);
    }

    public ConfigGui(GuiScreen parentScreen, List<IConfigElement> configElements, String modID, boolean allRequireWorldRestart, boolean allRequireMcRestart, String title) {
        super(parentScreen, configElements, modID, allRequireWorldRestart, allRequireMcRestart, title);
    }

    public ConfigGui(GuiScreen parentScreen, List<IConfigElement> configElements, String modID, boolean allRequireWorldRestart, boolean allRequireMcRestart, String title, String titleLine2) {
        super(parentScreen, configElements, modID, allRequireWorldRestart, allRequireMcRestart, title, titleLine2);
    }

    public ConfigGui(GuiScreen parentScreen, List<IConfigElement> configElements, String modID, @Nullable String configID, boolean allRequireWorldRestart, boolean allRequireMcRestart, String title, @Nullable String titleLine2) {
        super(parentScreen, configElements, modID, configID, allRequireWorldRestart, allRequireMcRestart, title, titleLine2);
    }
    public static class ConfigGuiFactory implements IModGuiFactory {
        public ConfigGuiFactory() {
        }

        @Override
        public void initialize(Minecraft minecraftInstance) {

        }

        @Override
        public boolean hasConfigGui() {
            return false;
        }

        @Override
        public GuiScreen createConfigGui(GuiScreen parentScreen) {
            return null;
        }

        @Override
        public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
            return null;
        }
    }
}
