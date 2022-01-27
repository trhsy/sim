package com.trhsy.buildcraft.api.core;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import java.util.Arrays;

/**
 * ========================================
 *
 * @ClassName JavaTools
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:05
 * ========================================
 **/
public final class JavaTools {
    private JavaTools() {
    }

    public static <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public static int[] concat(int[] first, int[] second) {
        int[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public static float[] concat(float[] first, float[] second) {
        float[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public static String surroundWithQuotes(String stringToSurroundWithQuotes) {
        return String.format("\"%s\"", stringToSurroundWithQuotes);
    }

    public static String stripSurroundingQuotes(String stringToStripQuotes) {
        return stringToStripQuotes.replaceAll("^\"|\"$", "");
    }
}
