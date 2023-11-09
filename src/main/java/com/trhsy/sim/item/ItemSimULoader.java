package com.trhsy.sim.item;

import com.trhsy.sim.loader.CreativeTabsLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.item
 * @ClassName: ItemSimULoader
 * @Description: 模拟城市启动卷轴
 * @date 2023/11/08 下午 1:51
 */
public class ItemSimULoader extends ItemBase {
    public ItemSimULoader() {
        super("simReel");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
        //if (!worldIn.isRemote) {
        //    NetWorkLoader.net.sendTo(new PacketOpenSetupGui(), (EntityPlayerMP)playerIn);
        //    //创造
        //    if (!playerIn.isCreative()) {
        //        playerIn.getHeldItem(hand).splitStack(1);
        //    }
        //}
        return super.onItemRightClick(worldIn, playerIn, hand);
    }
}