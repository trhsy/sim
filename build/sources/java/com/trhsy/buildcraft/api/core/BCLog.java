package com.trhsy.buildcraft.api.core;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;

/**
 * ========================================
 *
 * @ClassName BCLog
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 1:51
 * ========================================
 **/
public final class BCLog {
    public static final Logger logger = LogManager.getLogger("BuildCraft");

    private BCLog() {
    }

    public static void initLog() {
        logger.info("开始建造工艺 " + getVersion());
        logger.info("Copyright (c) SpaceToad, 2011-2014");
        logger.info("http://www.mod-buildcraft.com");
    }

    public static void logErrorAPI(String mod, Throwable error, Class<?> classFile) {
        StringBuilder msg = new StringBuilder(mod);
        msg.append(" API error, please update your mods. Error: ").append(error);
        StackTraceElement[] stackTrace = error.getStackTrace();
        if (stackTrace.length > 0) {
            msg.append(", ").append(stackTrace[0]);
        }

        logger.log(Level.ERROR, msg.toString());
        if (classFile != null) {
            msg = new StringBuilder(mod);
            msg.append(" API error: ").append(classFile.getSimpleName()).append(" is loaded from ").append(classFile.getProtectionDomain().getCodeSource().getLocation());
            logger.log(Level.ERROR, msg.toString());
        }

    }

    public static String getVersion() {
        try {
            Class<?> clazz = Class.forName("buildcraft.core.Version");
            Method method = clazz.getDeclaredMethod("getVersion");
            return String.valueOf(method.invoke((Object)null));
        } catch (Exception var2) {
            return "UNKNOWN VERSION";
        }
    }
}
