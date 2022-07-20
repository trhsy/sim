package com.trhsy.sim.common.entity;

import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Random;

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
        ModSimReloaded.theCommodities.clear();
        int count = rand.nextInt(3) + 2;

        for(int it = 0; it < count; ++it) {
            int index = rand.nextInt(availableItems.size() - 1);
            int qty = rand.nextInt(10) + 1;
            float price = 300.0F + (float) rand.nextInt(300) + rand.nextFloat() * 100.0F;
            boolean gotIt = false;

            for (int shit = 0; shit < ModSimReloaded.theCommodities.size(); ++shit) {
                Commodity cshit = (Commodity) ModSimReloaded.theCommodities.get(shit);
                if (cshit.theItemStack.func_82833_r().contentEquals(((ItemStack) availableItems.get(index)).func_82833_r())) {
                    gotIt = true;
                    break;
                }
            }

            if (!gotIt) {
                ModSimReloaded.theCommodities.add(new Commodity((ItemStack) availableItems.get(index), qty, price));
            }
        }

    }

    private static void setupAvailableItems() {
        availableItems.clear();
        //末影珍珠
        availableItems.add(new ItemStack(Items.field_151079_bi));
        //火焰棒
        availableItems.add(new ItemStack(Items.field_151072_bj));
        //骨
        availableItems.add(new ItemStack(Items.field_151103_aS));
        //火药
        availableItems.add(new ItemStack(Items.field_151016_H));
        //粘液球
        availableItems.add(new ItemStack(Items.field_151123_aH));
        //细绳
        availableItems.add(new ItemStack(Items.field_151007_F));
        //蜘蛛眼
        availableItems.add(new ItemStack(Items.field_151070_bp));
    }
}
