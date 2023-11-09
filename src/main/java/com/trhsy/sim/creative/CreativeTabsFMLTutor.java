package com.trhsy.sim.creative;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.loader.BlockLoader;
import com.trhsy.sim.loader.ModSimLoader;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.creative
 * @ClassName: CreativeTabsFMLTutor
 * @Description:
 * @date 2023/10/31 上午 10:10
 */
public class CreativeTabsFMLTutor extends CreativeTabs{
    public CreativeTabsFMLTutor() {
        //返回modid
        super(ModSim.MODID);
    }

    @Override
    public ItemStack getTabIconItem() {
        ItemStack itemStack=null;
        try {
            itemStack=new ItemStack(BlockLoader.blockConstructorBox);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];
            ModSimLoader.log.error("创造模式物品栏上显示的物品出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return itemStack;
    }
}
