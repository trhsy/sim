package com.trhsy.sim.client.loader;

import com.trhsy.sim.common.loader.BlockLoader;
import com.trhsy.sim.common.loader.FluidLoader;
import com.trhsy.sim.common.loader.ItemLoader;

/**
 * @ClassName ItemRenderLoader
 * @Description todo 注册这个方块对应物品的渲染
 * @Author Tian
 * @Date 2022/4/1520:33
 **/
public class ItemRenderLoader {
    public ItemRenderLoader(){

        /**物品材质加载**/
        ItemLoader.registerRenders();
        /**方块材质加载**/
        BlockLoader.registerRenders();
        /**流体材质加载**/
        FluidLoader.registerRenders();
    }
}
