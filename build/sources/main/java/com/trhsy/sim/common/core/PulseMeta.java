package com.trhsy.sim.common.core;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @ClassName PulseMeta
 * @Description todo
 * @Author Tian
 * @Date 2022/5/1122:57
 **/
@ParametersAreNonnullByDefault
public class PulseMeta {
    /****/
    private String id;
    /**
     * 描述
     **/
    private String description;
    /**
     * 强迫
     **/
    private boolean forced;
    /**
     * 启用
     **/
    private boolean enabled;
    /**
     * 默认启用
     **/
    private boolean defaultEnabled;
    /**
     * 丢失
     **/
    private boolean missingDeps = false;

    public PulseMeta(String id, @Nullable String description, boolean forced, boolean enabled, boolean defaultEnabled) {
        this.id = id;
        this.description = description;
        this.forced = forced;
        this.enabled = enabled;
        this.defaultEnabled = defaultEnabled;
    }

    public String getId() {
        return this.id;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean isForced() {
        return !this.missingDeps && this.forced;
    }

    public boolean isEnabled() {
        return !this.missingDeps && this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void setMissingDeps(boolean missing) {
        this.missingDeps = missing;
    }

    public boolean isDefaultEnabled() {
        return this.defaultEnabled;
    }
}
