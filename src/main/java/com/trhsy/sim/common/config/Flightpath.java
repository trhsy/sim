package com.trhsy.sim.common.config;

import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName Flightpath
 * @Description todo
 * @Author Tian
 * @Date 2022/5/1220:30
 **/
@ParametersAreNonnullByDefault
public class Flightpath {
    private final ISubscriberLocator locator;
    private final LinkedHashMap<Object, Map<Class, Set<Method>>> subscribers = new LinkedHashMap();
    private final Object lock = new Object();
    private IExceptionHandler exceptionHandler = new BlackholeExceptionHandler();

    public Flightpath() {
        this.locator = new AnnotationLocator(Airdrop.class);
    }

    public Flightpath(ISubscriberLocator locator) {
        this.locator = locator;
    }

    public void setExceptionHandler(IExceptionHandler handler) {
        synchronized (this.lock) {
            this.exceptionHandler = handler;
        }
    }

    public void register(Object obj) {
        synchronized (this.lock) {
            if (!this.subscribers.containsKey(obj)) {
                this.subscribers.put(obj, this.locator.findSubscribers(obj));
            }
        }
    }

    public void post(Object evt) {
        synchronized (this.lock) {

            label46:
            for (Map.Entry<Object, Map<Class, Set<Method>>> ent : this.subscribers.entrySet()) {

                for (Map.Entry objEnt : ent.getValue().entrySet()) {
                    if (!((Class) objEnt.getKey()).isAssignableFrom(evt.getClass())) {
                        continue label46;
                    }
                    Set<Method> ms = (Set) objEnt.getValue();
                    for (Method m : ms) {
                        try {
                            boolean access = m.isAccessible();
                            m.setAccessible(true);
                            m.invoke(ent.getKey(), evt);
                            m.setAccessible(access);
                        } catch (Exception e) {
                            this.exceptionHandler.handle(e);
                        }
                    }
                }
            }

            this.exceptionHandler.flush();
        }
    }
}
