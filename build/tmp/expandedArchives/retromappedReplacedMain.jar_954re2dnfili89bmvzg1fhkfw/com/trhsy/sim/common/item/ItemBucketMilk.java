package com.trhsy.sim.common.item;

import com.trhsy.sim.common.loader.BlockLoader;
import com.trhsy.sim.common.loader.CreativeTabsLoader;
import com.trhsy.sim.common.loader.FluidLoader;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;

/**
 * @ClassName ItemBucketMercury
 * @Description todo 为流体牛奶加上桶
 * @Author Tian
 * @Date 2022/5/921:17
 **/
public class ItemBucketMilk  extends ItemBucket {
    public ItemBucketMilk(){
        super(BlockLoader.blockFluidMilk);
        this.func_77642_a(Items.field_151133_ar);
        this.func_77655_b("bucketMilk");
        this.func_77637_a(CreativeTabsLoader.tabSimU);
        FluidContainerRegistry.registerFluidContainer(FluidLoader.fluidMilk, new ItemStack(this),
                FluidContainerRegistry.EMPTY_BUCKET);
    }
}
