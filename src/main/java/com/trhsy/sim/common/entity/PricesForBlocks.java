package com.trhsy.sim.common.entity;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;

/**
 * ========================================
 *
 * @ClassName PricesForBlocks
 * @Description todo 方块的价格
 * @Author Administrator
 * @Date 2022/1/26 0026下午 6:08
 * ========================================
 **/
public class PricesForBlocks implements Serializable {
    private static final long serialVersionUID = -2617939458756927761L;
    private static Float basePricePlanks = 0.0131F;
    private static Float basePriceLogs = 0.0524F;
    private static Float basePriceCobblestone = 0.0032F;
    private static Float basePriceStone = 0.0141F;
    private static Float basePriceGlass = 0.0121F;
    private static Float basePriceWool = 0.0115F;
    private static Float basePriceBrick = 0.0251F;
    private static Float basePriceStonebrick = 0.0261F;
    private static Float basePriceFence = 0.0113F;
    public static Float bankPriceDiamond = 10.23F;
    public static Float bankPriceEmerald = 9.34F;
    public static Float bankPriceRedstone = 3.75F;
    public static Float bankPriceGlowstone = 2.48F;
    public static Float bankPriceGold = 5.12F;

    public PricesForBlocks() {
    }

    public static Float getPrice(Block block, boolean isBuying) {
        float base = 0.0F;
        if (block == Blocks.planks) {
            base = basePricePlanks;
        } else if (block == Blocks.log) {
            base = basePriceLogs;
        } else if (block == Blocks.cobblestone) {
            base = basePriceCobblestone;
        } else if (block == Blocks.stone) {
            base = basePriceStone;
        } else if (block == Blocks.glass) {
            base = basePriceGlass;
        } else if (block == Blocks.wool) {
            base = basePriceWool;
        } else if (block == Blocks.brick_block) {
            base = basePriceBrick;
        } else if (block == Blocks.stonebrick) {
            base = basePriceStonebrick;
        } else if (block == Blocks.oak_fence||block == Blocks.spruce_fence||block == Blocks.birch_fence||block == Blocks.jungle_fence||block == Blocks.dark_oak_fence||block == Blocks.acacia_fence) {
            base = basePriceFence;
        }

        base *= 64.0F;
        if (isBuying) {
            base = (float)((double)base + (double)base * 1.12D);
        }

        return base;
    }

    public static void setPrice(Block block, float newPrice) {
        if (block == Blocks.planks) {
            basePricePlanks = newPrice;
        } else if (block == Blocks.log) {
            basePriceLogs = newPrice;
        } else if (block == Blocks.cobblestone) {
            basePriceCobblestone = newPrice;
        } else if (block == Blocks.stone) {
            basePriceStone = newPrice;
        } else if (block == Blocks.glass) {
            basePriceGlass = newPrice;
        } else if (block == Blocks.wool) {
            basePriceWool = newPrice;
        } else if (block == Blocks.brick_block) {
            basePriceBrick = newPrice;
        } else if (block == Blocks.stonebrick) {
            basePriceStonebrick = newPrice;
        } else if (block == Blocks.oak_fence||block == Blocks.spruce_fence||block == Blocks.birch_fence||block == Blocks.jungle_fence||block == Blocks.dark_oak_fence||block == Blocks.acacia_fence) {
            basePriceFence = newPrice;
        }

    }

    public static void adjustPrice(Block block, boolean afterBuying) {
        Random r = new Random();
        float cprice;
        if (afterBuying) {
            cprice = getPrice(block, false) / 64.0F;
            cprice += r.nextFloat() / 100.0F;
            if ((double)cprice > 0.99D) {
                cprice = 0.99F;
            }

            setPrice(block, cprice);
        } else {
            cprice = getPrice(block, false) / 64.0F;
            cprice -= r.nextFloat() / 100.0F;
            if ((double)cprice < 0.012D) {
                cprice = 0.012F;
            }

            setPrice(block, cprice);
        }

        if (block == Blocks.planks) {
            setPrice(Blocks.log, cprice * 4.0F);
        }

        if (block == Blocks.log) {
            setPrice(Blocks.planks, cprice / 4.0F);
        }

    }

    public static String formatPrice(float price) {
        NumberFormat formatter = new DecimalFormat("#0.00");
        return formatter.format((double)price);
    }
}
