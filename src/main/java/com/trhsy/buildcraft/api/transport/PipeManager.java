package com.trhsy.buildcraft.api.transport;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.buildcraft.api.transport.pluggable.PipePluggable;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ========================================
 *
 * @ClassName PipeManager
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 3:02
 * ========================================
 **/
public abstract class PipeManager {
    public static List<IStripesHandler> stripesHandlers = new ArrayList();
    public static ArrayList<Class<? extends PipePluggable>> pipePluggables = new ArrayList();
    private static Map<String, Class<? extends PipePluggable>> pipePluggableNames = new HashMap();
    private static Map<Class<? extends PipePluggable>, String> pipePluggableByNames = new HashMap();

    public PipeManager() {
    }

    /** @deprecated */
    @Deprecated
    public static boolean canExtractItems(Object extractor, World world, int i, int j, int k) {
        return true;
    }

    /** @deprecated */
    @Deprecated
    public static boolean canExtractFluids(Object extractor, World world, int i, int j, int k) {
        return true;
    }

    public static void registerStripesHandler(IStripesHandler handler) {
        stripesHandlers.add(handler);
    }

    public static void registerPipePluggable(Class<? extends PipePluggable> pluggable, String name) {
        pipePluggables.add(pluggable);
        pipePluggableNames.put(name, pluggable);
        pipePluggableByNames.put(pluggable, name);
    }

    public static Class<?> getPluggableByName(String pluggableName) {
        return (Class)pipePluggableNames.get(pluggableName);
    }

    public static String getPluggableName(Class<? extends PipePluggable> aClass) {
        return (String)pipePluggableByNames.get(aClass);
    }
}
