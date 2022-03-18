package com.trhsy.sim.api.buildcraft.api.core;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

/**
 * ========================================
 *
 * @ClassName BuildCraftAPI
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 1:53
 * ========================================
 **/

import net.minecraft.block.Block;
import net.minecraft.world.World;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public final class BuildCraftAPI {
    public static ICoreProxy proxy;
    public static final Set<Block> softBlocks = new HashSet();
    public static final HashMap<String, IWorldProperty> worldProperties = new HashMap();

    private BuildCraftAPI() {
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

    public static IWorldProperty getWorldProperty(String name) {
        return (IWorldProperty)worldProperties.get(name);
    }

    public static void registerWorldProperty(String name, IWorldProperty property) {
        if (worldProperties.containsKey(name)) {
            BCLog.logger.warn("The WorldProperty key '" + name + "' is being overidden with " + property.getClass().getSimpleName() + "!");
        }

        worldProperties.put(name, property);
    }

    public static boolean isSoftBlock(World world, int x, int y, int z) {
        return ((IWorldProperty)worldProperties.get("soft")).get(world, x, y, z);
    }
}
