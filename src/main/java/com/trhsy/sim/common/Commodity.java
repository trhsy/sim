package com.trhsy.sim.common;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Random;

/**
 * @ClassName Commodity
 * @Description todo
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
        ModSimukraft.theCommodities.clear();
        int count = rand.nextInt(3) + 2;

        for(int it = 0; it < count; ++it) {
            int index = rand.nextInt(availableItems.size() - 1);
            int qty = rand.nextInt(10) + 1;
            float price = 300.0F + (float)rand.nextInt(300) + rand.nextFloat() * 100.0F;
            boolean gotIt = false;

            for(int shit = 0; shit < ModSimukraft.theCommodities.size(); ++shit) {
                Commodity cshit = (Commodity)ModSimukraft.theCommodities.get(shit);
                if (cshit.theItemStack.func_82833_r().contentEquals(((ItemStack)availableItems.get(index)).func_82833_r())) {
                    gotIt = true;
                    break;
                }
            }

            if (!gotIt) {
                ModSimukraft.theCommodities.add(new Commodity((ItemStack)availableItems.get(index), qty, price));
            }
        }

    }

    private static void setupAvailableItems() {
        availableItems.clear();
        availableItems.add(new ItemStack(Items.field_151079_bi));
        availableItems.add(new ItemStack(Items.field_151072_bj));
        availableItems.add(new ItemStack(Items.field_151103_aS));
        availableItems.add(new ItemStack(Items.field_151016_H));
        availableItems.add(new ItemStack(Items.field_151123_aH));
        availableItems.add(new ItemStack(Items.field_151007_F));
        availableItems.add(new ItemStack(Items.field_151070_bp));
    }
}
