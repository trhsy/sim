package com.trhsy.sim.item;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.loader.CreativeTabsLoader;
import com.trhsy.sim.loader.ModSimLoader;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.item
 * @ClassName: ItemWindmillVane
 * @Description: 风车叶片
 * @date 2022/9/20 0020 下午 1:55
 */
public class ItemWindmillVane extends ItemBase{
    public ItemWindmillVane() {
        super("item_windmill_vane");
        this.maxStackSize = 64;
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }


    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
        try {
            for (int x = 0; x < 16; ++x) {
                subItems.add(new ItemStack(this, 1, x));
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];
            ModSimLoader.log.error("getSubItems出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
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
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        try {
            String windmill_sail = I18n.format("container.sim.windmill_sail");
            par3List.add(windmill_sail);
            super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
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
