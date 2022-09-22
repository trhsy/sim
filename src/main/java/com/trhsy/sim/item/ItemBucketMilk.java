package com.trhsy.sim.item;

import com.trhsy.sim.loader.BlockLoader;
import com.trhsy.sim.loader.CreativeTabsLoader;
import com.trhsy.sim.loader.FluidLoader;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidContainerRegistry;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.item
 * @ClassName: ItemBucketMilk
 * @Description: 牛奶桶
 * @date 2022/9/22 0022 上午 11:47
 */
public class ItemBucketMilk extends ItemBucket {
    public ItemBucketMilk(){
        super(BlockLoader.fluidMilk);
        this.setContainerItem(Items.bucket);
        this.setUnlocalizedName("bucketMilk");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);

    }
}
