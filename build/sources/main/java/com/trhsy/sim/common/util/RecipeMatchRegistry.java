package com.trhsy.sim.common.util;

import com.google.common.collect.Lists;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import java.util.*;

/**
 * @ClassName RecipeMatchRegistry
 * @Description todo
 * @Author Tian
 * @Date 2022/4/3020:49
 **/
public class RecipeMatchRegistry {
    protected final PriorityQueue<RecipeMatch> items;

    public RecipeMatchRegistry() {
        this.items = new PriorityQueue(1, RecipeMatchRegistry.RecipeComparator.INSTANCE);
    }


    public void addItem(String oredictItem, int amountNeeded, int amountMatched) {
        this.items.add(new RecipeMatch.Oredict(oredictItem, amountNeeded, amountMatched));
    }

    public void addItem(String oredictItem) {
        this.addItem((String)oredictItem, 1, 1);
    }

    public void addItem(Block block, int amountMatched) {
        this.items.add(new RecipeMatch.Item(new ItemStack(block), 1, amountMatched));
    }

    public void addItem(net.minecraft.item.Item item, int amountNeeded, int amountMatched) {
        this.items.add(new RecipeMatch.Item(new ItemStack(item), amountNeeded, amountMatched));
    }

    public void addItem(ItemStack item, int amountNeeded, int amountMatched) {
        this.items.add(new RecipeMatch.Item(item, amountNeeded, amountMatched));
    }

    public void addItem(net.minecraft.item.Item item) {
        this.addItem((net.minecraft.item.Item)item, 1, 1);
    }

    public void addRecipeMatch(RecipeMatch match) {
        this.items.add(match);
    }

    public static ItemStack[] copyItemStackArray(ItemStack[] in) {
        ItemStack[] stacksCopy = new ItemStack[in.length];
        try {
            for(int i = 0; i < in.length; i++) {
                if (in[i] != null) {
                    stacksCopy[i] = in[i].copy();
                }
            }

        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("copyItemStackArray出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

        return stacksCopy;
    }

    private static class RecipeComparator implements Comparator<RecipeMatch> {
        public static RecipeMatchRegistry.RecipeComparator INSTANCE = new RecipeMatchRegistry.RecipeComparator();

        private RecipeComparator() {
        }

        @Override
        public int compare(RecipeMatch o1, RecipeMatch o2) {
            return o2.amountMatched - o1.amountMatched;
        }
    }
}
