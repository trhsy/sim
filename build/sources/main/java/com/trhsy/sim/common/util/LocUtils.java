package com.trhsy.sim.common.util;

import net.minecraft.util.StatCollector;

import java.util.Locale;

/**
 * @ClassName LocUtils
 * @Description todo
 * @Author Tian
 * @Date 2022/4/3016:55
 **/
public class LocUtils {
    private LocUtils() {
    }

    public static String makeLocString(String unclean) {
        return unclean.toLowerCase(Locale.US).replaceAll(" ", "");
    }

    public static String translateRecursive(String key, Object... params) {
        return StatCollector.translateToLocal(StatCollector.translateToLocalFormatted(key, params));
    }
}
