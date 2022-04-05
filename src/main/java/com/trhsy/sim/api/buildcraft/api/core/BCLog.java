package com.trhsy.sim.api.buildcraft.api.core;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

    /** @deprecated */
    @Deprecated
    public static void logErrorAPI(String mod, Throwable error, Class<?> classFile) {
        logErrorAPI(error, classFile);
    }

    public static void logErrorAPI(Throwable error, Class<?> classFile) {
        StringBuilder msg = new StringBuilder("API error! Please update your mods. Error: ");
        msg.append(error);
        StackTraceElement[] stackTrace = error.getStackTrace();
        if (stackTrace.length > 0) {
            msg.append(", ").append(stackTrace[0]);
        }

        logger.log(Level.ERROR, msg.toString());
        if (classFile != null) {
            msg.append("API error: ").append(classFile.getSimpleName()).append(" is loaded from ").append(classFile.getProtectionDomain().getCodeSource().getLocation());
            logger.log(Level.ERROR, msg.toString());
        }

    }

    /** @deprecated */
    @Deprecated
    public static String getVersion() {
        return BuildCraftAPI.getVersion();
    }
}
