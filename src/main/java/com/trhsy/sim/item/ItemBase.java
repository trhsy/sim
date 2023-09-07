package com.trhsy.sim.item;

import net.minecraft.item.Item;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.item.granules
 * @ClassName: ItemBase
 * @Description:
 * @date 2022/9/20 0020 下午 1:17
 */
public class ItemBase extends Item {
    /**名字**/
    private String name;
    public ItemBase(String name) {
        this.name = name;
        this.setUnlocalizedName(name);
    }
}
