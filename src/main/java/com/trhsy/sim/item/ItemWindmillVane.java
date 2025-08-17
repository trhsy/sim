package com.trhsy.sim.item;

import com.trhsy.sim.loader.CreativeTabsLoader;
import com.trhsy.sim.loader.ModSimLoader;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.item
 * @ClassName: ItemWindmillVane
 * @Description: 风车叶片
 * @date 2023/11/08 下午 2:15
 */
public class ItemWindmillVane extends ItemBase{
    public ItemWindmillVane() {
        super("item_windmill_vane");
        this.maxStackSize = 64;
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }


    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        try {
            // 只在当前标签页是自定义标签页时，才添加子物品
            if (tab == CreativeTabsLoader.tabSimU) {
                for (int x = 0; x < 16; ++x) {
                    items.add(new ItemStack(this, 1, x));
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];
            ModSimLoader.log.error("getSubItems出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }
    // 核心修复：限制物品只属于自定义标签页
    @Override
    public CreativeTabs[] getCreativeTabs() {
        // 只返回你的自定义标签页，避免出现在其他标签页
        return new CreativeTabs[]{CreativeTabsLoader.tabSimU};
    }
    public String getTexture(String name) {
        return "sim:" + name;
    }

    public String getTexture(String folder, String name) {
        return "sim:" + folder + "/" + name;
    }
    @Override
    public String getUnlocalizedName(ItemStack is) {
        return this.getUnlocalizedName()+ is.getMetadata();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        try {
            //制作4个这样的叶片,合成风车帆
            String windmill_sail = new TextComponentTranslation("container.sim.windmill_sail",new Object[0]).getUnformattedText();
            tooltip.add(windmill_sail);
            super.addInformation(stack, worldIn, tooltip, flagIn);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];
            ModSimLoader.log.error("addInformation出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }
    @Override
    public int getMetadata(int par1) {
        return par1;
    }
}