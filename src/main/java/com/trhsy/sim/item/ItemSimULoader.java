package com.trhsy.sim.item;

import com.trhsy.sim.loader.CreativeTabsLoader;
import com.trhsy.sim.loader.NetWorkLoader;
import com.trhsy.sim.network.client.PacketOpenSetupGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

/**
 * @ClassName ItemSimULoader
 * @Description todo 模拟城市启动卷轴
 * @Author Tian
 * @Date 2022/9/2510:04
 **/
public class ItemSimULoader extends ItemBase {
    public ItemSimULoader() {
        super("simReel");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }

    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        if (!worldIn.isRemote) {
            NetWorkLoader.net.sendTo(new PacketOpenSetupGui(), (EntityPlayerMP)playerIn);
            /*if (!playerIn.func_184812_l_()) {
                playerIn.func_184586_b(hand).func_190920_e(0);
            }*/
        }
        return super.onItemRightClick(itemStackIn,worldIn, playerIn, hand);
    }
}
