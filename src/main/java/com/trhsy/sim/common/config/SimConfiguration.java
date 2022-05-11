package com.trhsy.sim.common.config;

import com.trhsy.sim.common.core.PulseMeta;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @ClassName SimConfiguration
 * @Description todo
 * @Author Tian
 * @Date 2022/5/1122:55
 **/
@ParametersAreNonnullByDefault
public interface SimConfiguration {
    /**
     * @Author fan
     * @Description //TODO 加载
     * @Date 22:55 2022/5/11
     * @Param []
     * @return void
     **/
    void load();

    boolean isModuleEnabled(PulseMeta pulseMeta);
    /**
     * @Author fan
     * @Description //TODO 刷新
     * @Date 22:59 2022/5/11
     * @Param []
     * @return void
     **/
    void flush();
}
