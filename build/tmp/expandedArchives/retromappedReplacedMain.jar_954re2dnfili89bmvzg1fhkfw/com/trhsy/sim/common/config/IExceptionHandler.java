package com.trhsy.sim.common.config;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @ClassName IExceptionHandler
 * @Description todo
 * @Author Tian
 * @Date 2022/5/1220:32
 **/
@ParametersAreNonnullByDefault
public interface IExceptionHandler {
    void handle(Exception var1);

    void flush();
}
