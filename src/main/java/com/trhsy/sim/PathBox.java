package com.trhsy.sim;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import com.trhsy.sim.common.entity.V3;

/**
 * ========================================
 *
 * @ClassName PathBox
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/26 0026下午 6:08
 * ========================================
 **/
public class PathBox {
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