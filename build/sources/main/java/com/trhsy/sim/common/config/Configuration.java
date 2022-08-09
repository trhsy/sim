package com.trhsy.sim.common.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.trhsy.sim.common.core.PulseMeta;
import net.minecraftforge.fml.common.Loader;
import org.apache.logging.log4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @ClassName Configuration
 * @Description todo
 * @Author Tian
 * @Date 2022/5/1220:36
 **/
@ParametersAreNonnullByDefault
public class Configuration implements IConfiguration {
    private static final int CONFIG_LEVEL = 1;
    private static final Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
    private final String confPath;
    private final Logger logger;
    private Map<String, ConfigEntry> modules;

    public Configuration(String confName, Logger logger) {
        this.confPath = Loader.instance().getConfigDir().toString() + File.separator + confName + ".json";
        this.logger = logger;
    }

    @Override
    public void load() {
        this.getModulesFromJson();
    }

    @Override
    public boolean isModuleEnabled(PulseMeta meta) {
        Configuration.ConfigEntry entry = (Configuration.ConfigEntry)this.modules.get(meta.getId());
        if (entry == null) {
            this.modules.put(meta.getId(), new Configuration.ConfigEntry(meta.isDefaultEnabled(), meta.getDescription()));
            return meta.isEnabled();
        } else {
            return entry.getEnabled();
        }
    }

    @Override
    public void flush() {
        this.writeModulesToJson();
    }

    private void getModulesFromJson() {
        File f = new File(this.confPath);
        if (!f.exists()) {
            this.logger.info("Couldn't find config file; will generate a new one later.");
            this.modules = new HashMap();
        } else if (f.canRead() && f.canWrite()) {
            try {
                try {
                    this.modules = this.parseV1Config(f);
                } catch (Exception e) {
                    this.logger.warn("Failed to parse " + f.getName() + " using the v1 parser; trying the v0 parser.");
                    Map<String, Configuration.ConfigEntry> conf = this.parseV0Config(f);
                    this.logger.info("Found valid v0 configuration. Upgrading it.");
                    this.modules = conf;
                    this.writeModulesToJson();
                    this.logger.info("Upgrade complete! Config is now in v1 format.");
                }
            } catch (Exception e) {
                this.logger.warn("Invalid config file. Discarding.");
                //var5.printStackTrace();
                this.modules = new HashMap();
            }

        } else {
            throw new Configuration.FileNotReadWritableException("Could not read/write Pulsar config: " + this.confPath);
        }
    }

    private Map<String, Configuration.ConfigEntry> parseV0Config(File f) throws Exception {
        try {
            JsonReader reader = new JsonReader(new InputStreamReader(new FileInputStream(f)));
            Map<String, Boolean> m = (Map)gson.fromJson(reader, (new TypeToken<HashMap<String, Boolean>>() {
            }).getType());
            if (m == null) {
                throw new NullPointerException("Gson returned null.");
            } else {
                Map<String, Configuration.ConfigEntry> out = new HashMap();
            for (Map.Entry<String, Boolean> e: m.entrySet()){
                    out.put(e.getKey(), new Configuration.ConfigEntry((Boolean)e.getValue()));
                }

                return out;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("This shouldn't be possible... " + e);
        }
    }

    private Map<String, Configuration.ConfigEntry> parseV1Config(File f) throws Exception {
        try {
            JsonReader reader = new JsonReader(new InputStreamReader(new FileInputStream(f)));
            Configuration.GsonConfig c = (Configuration.GsonConfig)gson.fromJson(reader, Configuration.GsonConfig.class);
            if (c.getConfigVersion() > 1) {
                throw new RuntimeException("Pulsar config is from a newer version! Remove it! " + f.getAbsolutePath());
            } else if (c.getModules() == null) {
                throw new IllegalArgumentException("Not a valid GsonConfig. Try v0 parsing.");
            } else {
                return c.getModules();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("This shouldn't be possible... " + e);
        }
    }

    private void writeModulesToJson() {
        try {
            JsonWriter writer = new JsonWriter(new OutputStreamWriter(new FileOutputStream(new File(this.confPath))));
            writer.setIndent("  ");
            Configuration.GsonConfig out = new Configuration.GsonConfig(1, this.modules);
            gson.toJson(out, Configuration.GsonConfig.class, writer);
            writer.close();
        } catch (Exception e) {
            this.logger.warn("Could not write config? " + this.confPath);
        }

    }

    private static class GsonConfig {
        private int CONFIG_VERSION = 0;
        private Map<String, Configuration.ConfigEntry> modules;

        public GsonConfig(int version, Map<String, Configuration.ConfigEntry> modules) {
            this.CONFIG_VERSION = version;
            this.modules = modules;
        }

        public int getConfigVersion() {
            return this.CONFIG_VERSION;
        }

        public Map<String, Configuration.ConfigEntry> getModules() {
            return this.modules;
        }
    }

    private static class ConfigEntry {
        private Boolean enabled;
        private String description;

        public ConfigEntry(Boolean enabled) {
            this.description = null;
            this.enabled = enabled;
        }

        public ConfigEntry(Boolean enabled, String description) {
            this(enabled);
            this.description = description;
        }

        public Boolean getEnabled() {
            return this.enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }

        public String getDescription() {
            return this.description;
        }
    }

    private static class FileNotReadWritableException extends RuntimeException {
        public FileNotReadWritableException(String message) {
            super(message);
        }
    }
}
