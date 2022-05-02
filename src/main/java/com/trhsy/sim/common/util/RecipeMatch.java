package com.trhsy.sim.common.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.trhsy.sim.ModSim;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @ClassName RecipeMatch
 * @Description todo
 * @Author Tian
 * @Date 2022/4/3020:50
 **/
public abstract class RecipeMatch {
    public final int amountNeeded;
    public final int amountMatched;

    public RecipeMatch(int amountMatched, int amountNeeded) {
        this.amountMatched = amountMatched;
        this.amountNeeded = amountNeeded;
    }

    public abstract List<ItemStack> getInputs();

    public abstract RecipeMatch.Match matches(ItemStack[] var1);

    public static RecipeMatch of(String oredict) {
        return of((String)oredict, 1);
    }

    public static RecipeMatch of(String oredict, int matched) {
        return of((String)oredict, 1, matched);
    }

    public static RecipeMatch of(String oredict, int amount, int matched) {
        return new RecipeMatch.Oredict(oredict, amount, matched);
    }

    public static RecipeMatch of(List<ItemStack> oredict) {
        return of((List)oredict, 1);
    }

    public static RecipeMatch of(List<ItemStack> oredict, int matched) {
        return of((List)oredict, 1, matched);
    }

    public static RecipeMatch of(List<ItemStack> oredict, int amount, int matched) {
        return new RecipeMatch.Oredict(oredict, amount, matched);
    }

    public static RecipeMatch of(net.minecraft.item.Item item) {
        return of((net.minecraft.item.Item)item, 1);
    }

    public static RecipeMatch of(net.minecraft.item.Item item, int matched) {
        return of((net.minecraft.item.Item)item, 1, matched);
    }

    public static RecipeMatch of(net.minecraft.item.Item item, int amount, int matched) {
        return new RecipeMatch.Item(new ItemStack(item), amount, matched);
    }

    public static RecipeMatch of(Block block) {
        return of((Block)block, 1);
    }

    public static RecipeMatch of(Block block, int matched) {
        return of((Block)block, 1, matched);
    }

    public static RecipeMatch of(Block block, int amount, int matched) {
        return new RecipeMatch.Item(new ItemStack(block), amount, matched);
    }

    public static RecipeMatch ofNBT(ItemStack stack) {
        return ofNBT(stack, 1);
    }

    public static RecipeMatch ofNBT(ItemStack stack, int matched) {
        return new RecipeMatch.ItemCombination(matched, new ItemStack[]{stack});
    }

    public static void removeMatch(ItemStack[] stacks, RecipeMatch.Match match) {
        Iterator var2 = match.stacks.iterator();

        while(true) {
            while(var2.hasNext()) {
                ItemStack stack = (ItemStack)var2.next();

                for(int i = 0; i < stacks.length; ++i) {
                    if (ItemStack.areItemsEqual(stack, stacks[i]) && ItemStack.areItemStackTagsEqual(stack, stacks[i])) {
                        if (stacks[i].stackSize < stack.stackSize) {
                            ModSim.log.error("RecipeMatch has incorrect stacksize! {}", new Object[]{stacks[i].toString()});
                        } else {
                            stacks[i].stackSize -= stack.stackSize;
                            if (stacks[i].stackSize == 0) {
                                stacks[i] = null;
                            }
                        }
                        break;
                    }
                }
            }

            return;
        }
    }

    public static class Match {
        public List<ItemStack> stacks;
        public int amount;

        public Match(List<ItemStack> stacks, int amount) {
            this.stacks = stacks;
            this.amount = amount;
        }
    }

    public static class Oredict extends RecipeMatch {
        private final List<ItemStack> oredictEntry;

        public Oredict(List<ItemStack> oredictEntry, int amountNeeded) {
            this((List)oredictEntry, amountNeeded, 1);
        }

        public Oredict(List<ItemStack> oredictEntry, int amountNeeded, int amountMatched) {
            super(amountMatched, amountNeeded);
            this.oredictEntry = oredictEntry;
        }

        public Oredict(String oredictEntry, int amountNeeded) {
            this((String)oredictEntry, amountNeeded, 1);
        }

        public Oredict(String oredictEntry, int amountNeeded, int amountMatched) {
            super(amountMatched, amountNeeded);
            this.oredictEntry = OreDictionary.getOres(oredictEntry);
        }

        @Override
        public List<ItemStack> getInputs() {
            return this.oredictEntry;
        }

        @Override
        public RecipeMatch.Match matches(ItemStack[] stacks) {
            List<ItemStack> found = Lists.newLinkedList();
            int stillNeeded = this.amountNeeded;
            Iterator var4 = this.oredictEntry.iterator();

            while(var4.hasNext()) {
                ItemStack ore = (ItemStack)var4.next();
                ItemStack[] var6 = stacks;
                int var7 = stacks.length;

                for(int var8 = 0; var8 < var7; ++var8) {
                    ItemStack stack = var6[var8];
                    if (OreDictionary.itemMatches(ore, stack, false)) {
                        ItemStack copy = stack.copy();
                        copy.stackSize = Math.min(copy.stackSize, stillNeeded);
                        found.add(copy);
                        stillNeeded -= copy.stackSize;
                        if (stillNeeded <= 0) {
                            return new RecipeMatch.Match(found, this.amountMatched);
                        }
                    }
                }
            }

            return null;
        }
    }

    public static class ItemCombination extends RecipeMatch {
        protected final ItemStack[] itemStacks;

        public ItemCombination(int amountMatched, ItemStack... stacks) {
            super(amountMatched, 0);
            this.itemStacks = stacks;
        }

        @Override
        public List<ItemStack> getInputs() {
            //return this.itemStacks.length != 0 && this.itemStacks.length <= 1 ? ImmutableList.of(this.itemStacks[0]) : ImmutableList.of();
            return ImmutableList.of(this.itemStacks[0]);
        }

        @Override
        public RecipeMatch.Match matches(ItemStack[] stacks) {
            List<ItemStack> found = Lists.newLinkedList();
            Set<Integer> needed = Sets.newHashSet();

            for(int i = 0; i < this.itemStacks.length; ++i) {
                if (this.itemStacks[i] != null) {
                    needed.add(i);
                }
            }

            ItemStack[] var12 = stacks;
            int var5 = stacks.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                ItemStack stack = var12[var6];
                Iterator iter = needed.iterator();

                while(iter.hasNext()) {
                    int index = (Integer)iter.next();
                    ItemStack template = this.itemStacks[index];
                    if (ItemStack.areItemsEqual(template, stack) && ItemStack.areItemStackTagsEqual(template, stack)) {
                        ItemStack copy = stack.copy();
                        copy.stackSize = 1;
                        found.add(copy);
                        iter.remove();
                        break;
                    }
                }
            }

            if (needed.isEmpty()) {
                return new RecipeMatch.Match(found, this.amountMatched);
            } else {
                return null;
            }
        }
    }

    public static class Item extends RecipeMatch {
        private final ItemStack template;

        public Item(ItemStack template, int amountNeeded) {
            this(template, amountNeeded, 1);
        }

        public Item(ItemStack template, int amountNeeded, int amountMatched) {
            super(amountMatched, amountNeeded);
            this.template = template;
        }

        @Override
        public List<ItemStack> getInputs() {
            return ImmutableList.of(this.template);
        }

        @Override
        public RecipeMatch.Match matches(ItemStack[] stacks) {
            List<ItemStack> found = Lists.newLinkedList();
            int stillNeeded = this.amountNeeded;
            ItemStack[] var4 = stacks;
            int var5 = stacks.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                ItemStack stack = var4[var6];
                if (OreDictionary.itemMatches(this.template, stack, false)) {
                    ItemStack copy = stack.copy();
                    copy.stackSize = Math.min(copy.stackSize, stillNeeded);
                    found.add(copy);
                    stillNeeded -= copy.stackSize;
                    if (stillNeeded <= 0) {
                        return new RecipeMatch.Match(found, this.amountMatched);
                    }
                }
            }

            return null;
        }
    }
}
