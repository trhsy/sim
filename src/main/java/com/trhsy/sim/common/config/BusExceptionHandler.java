package com.trhsy.sim.common.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @ClassName BusExceptionHandler
 * @Description todo
 * @Author Tian
 * @Date 2022/5/1220:38
 **/
@ParametersAreNonnullByDefault
public class BusExceptionHandler implements IExceptionHandler {
    private final String id;
    private final Logger logger;

    public BusExceptionHandler(String id) {
        this.id = id;
        this.logger = LogManager.getLogger(id + "-Pulsar-Flightpath");
    }

    @Override
    public void handle(Exception ex) {
        this.logger.error("Exception caught from a pulse on flightpath for mod ID " + this.id + ": ", ex);
        throw new Error(ex);
    }

    @Override
    public void flush() {
    }
}