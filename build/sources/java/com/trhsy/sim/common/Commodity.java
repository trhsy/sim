package com.trhsy.sim.common;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Random;

/**
 * @ClassName Commodity
 * @Description todo 商品
 * @Author Tian
 * @Date 2022/1/2320:18
 **/
public class Commodity {

    public ItemStack theItemStack = null;
    public int quantity = 0;
    public float priceEach = 0.0F;
    private static ArrayList<ItemStack> availableItems = new ArrayList();

    public Commodity(ItemStack is, int qty, float price) {
        this.theItemStack = is;
        this.quantity = qty;
        this.priceEach = price;
    }

    public static void refreshAvailableCommoditities() {
        if (availableItems.size() == 0) {
            setupAvailableItems();
        }

        Random rand = new Random();
        ModSim.theCommodities.clear();
        int count = rand.nextInt(3) + 2;

        for(int it = 0; it < count; ++it) {
            int index = rand.nextInt(availableItems.size() - 1);
            int qty = rand.nextInt(10) + 1;
            float price = 300.0F + (float) rand.nextInt(300) + rand.nextFloat() * 100.0F;
            boolean gotIt = false;

            for (int shit = 0; shit < ModSim.theCommodities.size(); ++shit) {
                Commodity cshit = (Commodity) ModSim.theCommodities.get(shit);
                if (cshit.theItemStack.getDisplayName().contentEquals(((ItemStack) availableItems.get(index)).getDisplayName())) {
                    gotIt = true;
                    break;
                }
            }

            if (!gotIt) {
                ModSim.theCommodities.add(new Commodity((ItemStack) availableItems.get(index), qty, price));
            }
        }

    }

    private static void setupAvailableItems() {
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
    }
}
