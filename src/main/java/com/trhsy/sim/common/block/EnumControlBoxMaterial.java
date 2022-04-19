package com.trhsy.sim.common.block;

import net.minecraft.util.IStringSerializable;
/**
 * @Author fan
 * @Description //TODO 控制箱枚举，用于多方块
 * @Date 21:52 2022/4/19
 * @Param 
 * @return 
 **/
public enum EnumControlBoxMaterial implements IStringSerializable {
    ATM("ATM"), side("side"),other("other");
    private String name;

    private EnumControlBoxMaterial(String material) {
        this.name = material;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
