package com.trhsy.sim.util;

import com.trhsy.sim.loader.ModSimLoader;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @ClassName Commodity
 * @Description todo
 * @Author TRHSY
 * @Date 2023/7/1223:18
 **/
public class Commodity {
    /**商品**/
    public ItemStack theItemStack;
    /**数量**/
    public int quantity = 0;
    /**单价**/
    public float priceEach = 0.0F;
    /**可用商品**/
    private static List<ItemStack> availableItems = new CopyOnWriteArrayList();

    public Commodity(ItemStack is, int qty, float price) {
        try {
            this.theItemStack = is;
            this.quantity = qty;
            this.priceEach = price;
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];
            ModSimLoader.log.error("Commodity出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }
    /**
     * @Author fan
     * @Description //TODO 刷新可用商品
     * @Date 23:19 2023/7/12
     * @Param []
     * @return void
     **/
    public static void refreshAvailableCommoditities() {
        try {
            if (availableItems.size() == 0) {
                setupAvailableItems();
            }

            Random rand = new Random();
            ModSimLoader.theCommodities.clear();
            int count = rand.nextInt(3) + 2;

            for(int it = 0; it < count; it++) {
                int index = rand.nextInt(availableItems.size() - 1);
                int qty = rand.nextInt(10) + 1;
                float price = 300.0F + (float) rand.nextInt(300) + rand.nextFloat() * 100.0F;
                boolean gotIt = false;

                for (int shit = 0; shit < ModSimLoader.theCommodities.size(); ++shit) {
                    Commodity cshit = ModSimLoader.theCommodities.get(shit);
                    if (cshit.theItemStack.getDisplayName().contentEquals(((ItemStack) availableItems.get(index)).getDisplayName())) {
                        gotIt = true;
                        break;
                    }
                }

                if (!gotIt) {
                    ModSimLoader.theCommodities.add(new Commodity((ItemStack) availableItems.get(index), qty, price));
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("refreshAvailableCommoditities出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    private static void setupAvailableItems() {
        try {
            availableItems.clear();
            //末影珍珠
            availableItems.add(new ItemStack(Items.ENDER_PEARL));
            //火焰棒
            availableItems.add(new ItemStack(Items.BLAZE_ROD));
            //骨
            availableItems.add(new ItemStack(Items.BONE));
            //火药
            availableItems.add(new ItemStack(Items.GUNPOWDER));
            //粘液球
            availableItems.add(new ItemStack(Items.SLIME_BALL));
            //细绳
            availableItems.add(new ItemStack(Items.STRING));
            //蜘蛛眼
            availableItems.add(new ItemStack(Items.SPIDER_EYE));
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("setupAvailableItems出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }
}
