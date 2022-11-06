package com.trhsy.sim.npc.build;

/**
 * @ClassName BuildingSymbol
 * @Description todo 建筑符号
 * @Author TRHSY
 * @Date 2022/10/1917:50
 **/
public class BuildingSymbol {
    public String symbol;
    public String blockName;
    public int meta;

    public BuildingSymbol(String symbol, String blockName, int meta) {
        this.symbol = symbol;
        this.blockName = blockName;
        this.meta = meta;
    }
}
