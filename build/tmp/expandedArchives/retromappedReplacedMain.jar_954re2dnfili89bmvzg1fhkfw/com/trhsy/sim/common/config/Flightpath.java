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
        synchronized(this.lock) {
            this.exceptionHandler = handler;
        }
    }

    public void register(Object obj) {
        synchronized(this.lock) {
            if (!this.subscribers.containsKey(obj)) {
                this.subscribers.put(obj, this.locator.findSubscribers(obj));
            }
        }
    }

    public void post(Object evt) {
        synchronized(this.lock) {
            Iterator var3 = this.subscribers.entrySet().iterator();

            label46:
            while(var3.hasNext()) {
                Map.Entry<Object, Map<Class, Set<Method>>> ent = (Map.Entry)var3.next();
                Iterator var5 = ((Map)ent.getValue()).entrySet().iterator();

                while(true) {
                    Map.Entry objEnt;
                    do {
                        if (!var5.hasNext()) {
                            continue label46;
                        }

                        objEnt = (Map.Entry)var5.next();
                    } while(!((Class)objEnt.getKey()).isAssignableFrom(evt.getClass()));

                    Set<Method> ms = (Set)objEnt.getValue();
                    Iterator var8 = ms.iterator();

                    while(var8.hasNext()) {
                        Method m = (Method)var8.next();

                        try {
                            boolean access = m.isAccessible();
                            m.setAccessible(true);
                            m.invoke(ent.getKey(), evt);
                            m.setAccessible(access);
                        } catch (Exception var12) {
                            this.exceptionHandler.handle(var12);
                        }
                    }
                }
            }

            this.exceptionHandler.flush();
        }
    }
}
