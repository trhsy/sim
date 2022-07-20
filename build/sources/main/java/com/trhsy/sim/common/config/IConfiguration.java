package com.trhsy.sim.common.config;

import com.trhsy.sim.common.core.PulseMeta;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @ClassName IConfiguration
 * @Description todo
 * @Author Tian
 * @Date 2022/5/1220:35
 **/
@ParametersAreNonnullByDefault
public interface IConfiguration {
    void load();

    boolean isModuleEnabled(PulseMeta pulseMeta);

    void flush();
}
