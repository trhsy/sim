package com.trhsy.sim.common.config;

import com.trhsy.sim.common.loader.ModSimReloaded;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName AnnotationLocator
 * @Description todo
 * @Author Tian
 * @Date 2022/5/1220:31
 **/
@ParametersAreNonnullByDefault
public class AnnotationLocator implements ISubscriberLocator {
    private final Class<? extends Annotation> annotation;

    public AnnotationLocator(Class<? extends Annotation> annotation) {
        this.annotation = annotation;
    }

    @Override
    @Nonnull
    public Map<Class, Set<Method>> findSubscribers(Object obj) {
        Map<Class, Set<Method>> methods = new HashMap();
        try {
            Method[] var3 = obj.getClass().getMethods();
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                Method m = var3[var5];
                if (m.isAnnotationPresent(this.annotation) && m.getParameterTypes().length == 1) {
                    Class param = m.getParameterTypes()[0];
                    if (!methods.containsKey(param)) {
                        methods.put(param, new HashSet());
                    }

                    ((Set)methods.get(param)).add(m);
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("findSubscribers出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return methods;
    }
}