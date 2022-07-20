package com.trhsy.sim.common.config;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName ISubscriberLocator
 * @Description todo
 * @Author Tian
 * @Date 2022/5/1220:32
 **/
@ParametersAreNonnullByDefault
public interface ISubscriberLocator {
    @Nonnull
    Map<Class, Set<Method>> findSubscribers(Object var1);
}
