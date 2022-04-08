package com.trhsy.sim.common.infrastructure;

/**
 * @ClassName Infrastructure
 * @Description todo 基础设施
 * @Author Tian
 * @Date 2022/4/722:22
 **/
public class Infrastructure {
    String infrastructureName = "";

    public Infrastructure() {
    }

    public void setInfrastructureName(String name) {
        this.infrastructureName = name;
    }

    public static enum infrastructures {
        None,
        Water,
        Electricity;

        private infrastructures() {
        }
    }
}
