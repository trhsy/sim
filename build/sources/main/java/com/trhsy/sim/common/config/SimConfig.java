package com.trhsy.sim.common.config;

import com.trhsy.sim.common.core.PulseMeta;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.Loader;

import java.io.File;
import java.util.Locale;

/**
 * @ClassName SimConfig
 * @Description todo
 * @Author Tian
 * @Date 2022/5/1122:54
 **/
public class SimConfig implements IConfiguration{
    private Configuration config;
    private final String confPath;
    private final String description;
    public SimConfig(String confName, String description){
        this.confPath = Loader.instance().getConfigDir().toString() + File.separator + confName + ".cfg";
        this.description = description.toLowerCase(Locale.US);
        this.config = new Configuration(new File(this.confPath), "1");
    }
    @Override
    public void load() {

        this.config.load();
    }

    @Override
    public boolean isModuleEnabled(PulseMeta pulseMeta) {
        Property prop = this.config.get(this.description, pulseMeta.getId(), pulseMeta.isEnabled(), pulseMeta.getDescription());
        prop.setRequiresMcRestart(true);
        return prop.getBoolean(pulseMeta.isEnabled());
    }

    @Override
    public void flush() {
        try {
            if (this.config.hasChanged()) {
                this.config.save();
            }
        } catch (Exception e) {
            ModSimReloaded.log.error("sim配置flush出错了：" + e.getMessage());
        }

    }
    public Configuration getConfig() {
        return this.config;
    }

    public ConfigCategory getCategory() {
        return this.config.getCategory(this.description);
    }
}
