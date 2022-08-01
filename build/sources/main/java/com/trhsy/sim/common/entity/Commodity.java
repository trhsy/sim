package com.trhsy.sim.common.entity;

import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 商品
 */
public class Commodity {
    /**商品**/
    public ItemStack theItemStack = null;
    /**数量**/
    public int quantity = 0;
    /**单价**/
    public float priceEach = 0.0F;
    /**可用商品**/
    private static CopyOnWriteArrayList<ItemStack> availableItems = new CopyOnWriteArrayList();

    public Commodity(ItemStack is, int qty, float price) {
        try {
            this.theItemStack = is;
            this.quantity = qty;
            this.priceEach = price;
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("Commodity出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    public static void refreshAvailableCommoditities() {
        try {
            if (availableItems.size() == 0) {
                setupAvailableItems();
            }

            Random rand = new Random();
            ModSimReloaded.theCommodities.clear();
            int count = rand.nextInt(3) + 2;

            for(int it = 0; it < count; it++) {
                int index = rand.nextInt(availableItems.size() - 1);
                int qty = rand.nextInt(10) + 1;
                float price = 300.0F + (float) rand.nextInt(300) + rand.nextFloat() * 100.0F;
                boolean gotIt = false;

                for (int shit = 0; shit < ModSimReloaded.theCommodities.size(); ++shit) {
                    Commodity cshit = (Commodity) ModSimReloaded.theCommodities.get(shit);
                    if (cshit.theItemStack.getDisplayName().contentEquals(((ItemStack) availableItems.get(index)).getDisplayName())) {
                        gotIt = true;
                        break;
                    }
                }

                if (!gotIt) {
                    ModSimReloaded.theCommodities.add(new Commodity((ItemStack) availableItems.get(index), qty, price));
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("refreshAvailableCommoditities出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    private static void setupAvailableItems() {
        try {
            availableItems.clear();
            //末影珍珠
            availableItems.add(new ItemStack(Items.ender_pearl));
            //火焰棒
            availableItems.add(new ItemStack(Items.blaze_rod));
            //骨
            availableItems.add(new ItemStack(Items.bone));
            //火药
            availableItems.add(new ItemStack(Items.gunpowder));
            //粘液球
            availableItems.add(new ItemStack(Items.slime_ball));
            //细绳
            availableItems.add(new ItemStack(Items.string));
            //蜘蛛眼
            availableItems.add(new ItemStack(Items.spider_eye));
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("setupAvailableItems出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }
}
