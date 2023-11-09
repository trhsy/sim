package com.trhsy.sim.loader.render;

import com.trhsy.sim.loader.BlockLoader;
import com.trhsy.sim.loader.FluidLoader;
import com.trhsy.sim.loader.ItemLoader;
import com.trhsy.sim.loader.ModSimLoader;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.loader.render
 * @ClassName: ItemRenderLoader
 * @Description:
 * @date 2023/10/31 下午 2:42
 */
public class ItemRenderLoader {

    public ItemRenderLoader(){
        try {

            /**物品材质加载**/
            //ItemLoader.registerRenders();
            /**方块材质加载**/
            //BlockLoader.registerRenders();
            /**流体材质加载**/
            FluidLoader.registerRenders();
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];
            ModSimLoader.log.error("ItemRenderLoader出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

}
