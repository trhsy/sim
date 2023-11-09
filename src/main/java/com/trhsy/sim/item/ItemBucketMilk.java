package com.trhsy.sim.item;

import com.trhsy.sim.loader.BlockLoader;
import com.trhsy.sim.loader.CreativeTabsLoader;
import net.minecraft.block.Block;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.item
 * @ClassName: ItemBucketMilk
 * @Description: 桶装牛奶
 * @date 2023/10/31 上午 10:08
 */
public class ItemBucketMilk extends ItemBucket {
    public ItemBucketMilk() {
        super(BlockLoader.milk);
        this.setContainerItem(Items.BUCKET);
        this.setUnlocalizedName("bucketMilk");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }
}
