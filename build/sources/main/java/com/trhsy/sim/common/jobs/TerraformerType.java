package com.trhsy.sim.common.jobs;

/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */
public enum TerraformerType {
    //填海
    WATERTODIRT,
    //植树
    NATURE,
    //割草
    LAWNMOWER,
    //铺平
    FLATTENIZER,
    //单层泥土
    VALUEPACK,
    //方冰
    GLACIAL,
    //放水
    MOISTURIZER,
    //放岩浆
    THERMALIZER,
    //除冰
    DEICER;

    private TerraformerType() {
    }
}
