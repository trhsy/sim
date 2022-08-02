package com.trhsy.sim.common.core.entity.functionality;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */


import com.trhsy.sim.common.core.entity.EntityAlignBeam;
import com.trhsy.sim.common.core.entity.V3;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ========================================
 *
 * @ClassName Marker
 * @Description todo 标记
 * @Author Administrator
 * @Date 2022/1/26 0026下午 6:06
 * ========================================
 **/
public class Marker {
    public int x;
    public int y;
    public int z;
    int dimension;
    public String caption = "";
    public List<EntityAlignBeam> beams = new CopyOnWriteArrayList();

    public Marker(int i, int j, int k, int dime) {
        this.x = i;
        this.y = j;
        this.z = k;
        this.dimension = dime;
    }

    @Override
    public String toString() {
        return this.x + "," + this.y + "," + this.z;
    }

    public V3 toV3() {
        return new V3((double)this.x, (double)this.y, (double)this.z, this.dimension);
    }
}