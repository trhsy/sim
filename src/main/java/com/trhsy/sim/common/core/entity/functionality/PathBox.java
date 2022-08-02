package com.trhsy.sim.common.core.entity.functionality;

import com.trhsy.sim.common.core.entity.V3;

/**
 * @ClassName PathBox
 * @Description todo
 * @Author Tian
 * @Date 2022/4/414:45
 **/
public class PathBox  {
    private static final long serialVersionUID = 2951402725466206500L;
    public V3 location;
    public V3 marker1XYZ;
    public String pathType = "";

    public PathBox() {
    }

    public PathBox(V3 location) {
        this.location = location;
    }
}