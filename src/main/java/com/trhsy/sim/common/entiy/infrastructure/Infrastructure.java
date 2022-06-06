package com.trhsy.sim.common.entiy.infrastructure;

/**
 * 基础设施
 */
public class Infrastructure {
    //基础设施名称
    String infrastructureName = "";

    public Infrastructure() {
    }

    public void setInfrastructureName(String name) {
        this.infrastructureName = name;
    }

    public static enum infrastructures {
        //没有
        None,
        //水
        Water,
        //电
        Electricity;

        private infrastructures() {
        }
    }
}
