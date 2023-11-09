package com.trhsy.sim.item.food;

import com.trhsy.sim.loader.CreativeTabsLoader;
import com.trhsy.sim.loader.ModSimLoader;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.item.food
 * @ClassName: ItemCheeseburger
 * @Description: 奶酪汉堡
 * @date 2023/11/09 下午 2:45
 */
public class ItemCheeseburger extends ItemFood {
    public ItemCheeseburger() {
        super(6, 0.6F, false);
        this.setUnlocalizedName("foodCheeseburger");
        this.setHasSubtypes(true);
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        try {
            String sim_folks = I18n.format("container.sim.sim_folks");
            tooltip.add(sim_folks);
            super.addInformation(stack, worldIn, tooltip, flagIn);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];
            ModSimLoader.log.error("addInformation出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

}
