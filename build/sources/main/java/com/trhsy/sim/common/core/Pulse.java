package com.trhsy.sim.common.core;

import java.lang.annotation.*;

/**
 * @ClassName Pulse
 * @Description todo
 * @Author Tian
 * @Date 2022/5/1220:39
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
public @interface Pulse {
    String id();

    String description() default "";

    String modsRequired() default "";

    String pulsesRequired() default "";

    boolean forced() default false;

    boolean defaultEnable() default true;
}
