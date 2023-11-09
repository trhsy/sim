package com.trhsy.sim.item;

import net.minecraft.item.Item;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.item
 * @ClassName: ItemBase
 * @Description: 物品通用类
 * @date 2023/10/31 上午 10:06
 */
public class ItemBase extends Item {
    /**名字**/
    private String name;
    public ItemBase(String name) {
        this.name = name;
        this.setUnlocalizedName(name);
    }
}
