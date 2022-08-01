package com.trhsy.sim.common.entity;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import com.trhsy.sim.common.loader.ModSimReloaded;
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
        try {
            if (block == Blocks.field_150344_f) {
                base = basePricePlanks;
            } else if (block == Blocks.field_150364_r) {
                base = basePriceLogs;
            } else if (block == Blocks.field_150347_e) {
                base = basePriceCobblestone;
            } else if (block == Blocks.field_150348_b) {
                base = basePriceStone;
            } else if (block == Blocks.field_150359_w) {
                base = basePriceGlass;
            } else if (block == Blocks.field_150325_L) {
                base = basePriceWool;
            } else if (block == Blocks.field_150336_V) {
                base = basePriceBrick;
            } else if (block == Blocks.field_150417_aV) {
                base = basePriceStonebrick;
            } else if (block == Blocks.field_180407_aO||block == Blocks.field_180408_aP||block == Blocks.field_180404_aQ||block == Blocks.field_180403_aR||block == Blocks.field_180406_aS||block == Blocks.field_180405_aT) {
                base = basePriceFence;
            }

            base *= 64.0F;
            if (isBuying) {
                base = (float)((double)base + (double)base * 1.12D);
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("getPrice出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }


        return base;
    }

    public static void setPrice(Block block, float newPrice) {
        try {
            if (block == Blocks.field_150344_f) {
                basePricePlanks = newPrice;
            } else if (block == Blocks.field_150364_r) {
                basePriceLogs = newPrice;
            } else if (block == Blocks.field_150347_e) {
                basePriceCobblestone = newPrice;
            } else if (block == Blocks.field_150348_b) {
                basePriceStone = newPrice;
            } else if (block == Blocks.field_150359_w) {
                basePriceGlass = newPrice;
            } else if (block == Blocks.field_150325_L) {
                basePriceWool = newPrice;
            } else if (block == Blocks.field_150336_V) {
                basePriceBrick = newPrice;
            } else if (block == Blocks.field_150417_aV) {
                basePriceStonebrick = newPrice;
            } else if (block == Blocks.field_180407_aO||block == Blocks.field_180408_aP||block == Blocks.field_180404_aQ||block == Blocks.field_180403_aR||block == Blocks.field_180406_aS||block == Blocks.field_180405_aT) {
                basePriceFence = newPrice;
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("setPrice出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }


    }

    public static void adjustPrice(Block block, boolean afterBuying) {
        try {
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

            if (block == Blocks.field_150344_f) {
                setPrice(Blocks.field_150364_r, cprice * 4.0F);
            }

            if (block == Blocks.field_150364_r) {
                setPrice(Blocks.field_150344_f, cprice / 4.0F);
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("adjustPrice出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }


    }

    public static String formatPrice(float price) {
        String formatPrice="";
        try {
            NumberFormat formatter = new DecimalFormat("#0.00");
            formatPrice=formatter.format((double)price);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("formatPrice出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return formatPrice;
    }
}
