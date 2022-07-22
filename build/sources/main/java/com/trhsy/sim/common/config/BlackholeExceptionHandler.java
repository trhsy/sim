package com.trhsy.sim.common.config;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @ClassName BlackholeExceptionHandler
 * @Description todo
 * @Author Tian
 * @Date 2022/5/1220:31
 **/
@ParametersAreNonnullByDefault
public class BlackholeExceptionHandler implements IExceptionHandler {
    public BlackholeExceptionHandler() {
    }

    @Override
    public void handle(Exception ex) {
        // TODO document why this method is empty
    }

    @Override
    public void flush() {
        // TODO document why this method is empty
    }
}