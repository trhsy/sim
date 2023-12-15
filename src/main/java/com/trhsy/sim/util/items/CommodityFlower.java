package com.trhsy.sim.util.items;

import com.trhsy.sim.loader.ModSimLoader;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @ClassName Commodity
 * @Description todo 花店物品
 * @Author TRHSY
 * @Date 2023/7/1223:18
 **/
public class CommodityFlower {
    /**商品**/
    public ItemStack theItemStack;
    /**数量**/
    public int quantity = 0;
    /**单价**/
    public float priceEach = 0.0F;
    /**可用商品**/
    private static List<ItemStack> availableItems = new CopyOnWriteArrayList();
    /*
        杂货商目前正在销售的商品列表，每天早上都会更新新商品
         */
    public static List<CommodityFlower> theCommodities = new CopyOnWriteArrayList();
    public CommodityFlower(ItemStack is, int qty, float price) {
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
            theCommodities.clear();
            int count = rand.nextInt(10) + 2;

            for(int it = 0; it < count; it++) {
                int index = rand.nextInt(availableItems.size() - 1);
                int qty = rand.nextInt(10) + 1;
                float price = 10.0F + (float) rand.nextInt(10) + rand.nextFloat() * 10.0F;
                boolean gotIt = false;

                for (int shit = 0; shit < theCommodities.size(); ++shit) {
                    CommodityFlower cshit = theCommodities.get(shit);
                    if (cshit.theItemStack.getDisplayName().contentEquals(((ItemStack) availableItems.get(index)).getDisplayName())) {
                        gotIt = true;
                        break;
                    }
                }

                if (!gotIt) {
                    theCommodities.add(new CommodityFlower((ItemStack) availableItems.get(index), qty, price));
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("refreshAvailableCommoditities出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    private static void setupAvailableItems() {
        try {
            availableItems.clear();
            //蒲公英
            availableItems.add(new ItemStack(Blocks.YELLOW_FLOWER));
            //虞美人
            availableItems.add(new ItemStack(Blocks.RED_FLOWER));
            //兰花
            availableItems.add(new ItemStack(Blocks.RED_FLOWER,1,1));
            //绒球葱
            availableItems.add(new ItemStack(Blocks.RED_FLOWER,1,2));
            //蓝花美耳草
            availableItems.add(new ItemStack(Blocks.RED_FLOWER,1,3));
            //红色郁金香
            availableItems.add(new ItemStack(Blocks.RED_FLOWER,1,4));
            //橙色郁金香
            availableItems.add(new ItemStack(Blocks.RED_FLOWER,1,5));
            //白色郁金香
            availableItems.add(new ItemStack(Blocks.RED_FLOWER,1,6));
            //粉红色郁金香
            availableItems.add(new ItemStack(Blocks.RED_FLOWER,1,7));
            //滨菊
            availableItems.add(new ItemStack(Blocks.RED_FLOWER,1,8));
            //向日葵
            availableItems.add(new ItemStack(Blocks.DOUBLE_PLANT));
            //丁香
            availableItems.add(new ItemStack(Blocks.DOUBLE_PLANT,1,1));
            //玫瑰丛
            availableItems.add(new ItemStack(Blocks.DOUBLE_PLANT,1,4));
            //牡丹
            availableItems.add(new ItemStack(Blocks.DOUBLE_PLANT,1,5));
            //花盆
            availableItems.add(new ItemStack(Blocks.FLOWER_POT));
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("setupAvailableItems出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }
}
