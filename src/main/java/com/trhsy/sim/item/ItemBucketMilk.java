package com.trhsy.sim.item;

import com.trhsy.sim.loader.BlockLoader;
import com.trhsy.sim.loader.CreativeTabsLoader;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBucket;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.item
 * @ClassName: ItemBucketMilk
 * @Description: 牛奶桶
 * @date 2022/9/22 0022 上午 11:47
 */
public class ItemBucketMilk extends ItemBucket {
    public ItemBucketMilk(){
        super(BlockLoader.milk);
        this.setContainerItem(Items.BUCKET);
        this.setUnlocalizedName("bucketMilk");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }
}
