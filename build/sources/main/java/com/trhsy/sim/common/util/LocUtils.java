package com.trhsy.sim.common.util;

import com.trhsy.sim.common.loader.ModSimReloaded;
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
        String makeLocString=null;
        try {
            makeLocString=unclean.toLowerCase(Locale.US).replaceAll(" ", "");
        } catch (Exception e) {
            ModSimReloaded.log.error("makeLocString出错了：" + e.getMessage());
        }
        return makeLocString;
    }

    public static String translateRecursive(String key, Object... params) {
        String makeLocString=null;
        try {
            makeLocString=StatCollector.translateToLocal(StatCollector.translateToLocalFormatted(key, params));
        } catch (Exception e) {
            ModSimReloaded.log.error("translateRecursive出错了：" + e.getMessage());
        }
        return makeLocString;
    }
}
