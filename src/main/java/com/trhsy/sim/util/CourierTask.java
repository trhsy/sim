package com.trhsy.sim.util;

import com.trhsy.sim.npc.V3;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.util
 * @ClassName: CourierTask
 * @Description:
 * @date 2023/08/15 下午 4:17
 */
public class CourierTask {
    //任务名称
    public String name = "";
    //npc
    public String folkname = "";
    //提货地点
    public V3 pickup ;
    //卸货地点
    public V3 dropoff;
    //重复
    public boolean repeat = true;

    public CourierTask() {
    }
}
