package com.trhsy.sim.item;

import com.trhsy.sim.loader.CreativeTabsLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.loader.NetWorkLoader;
import com.trhsy.sim.network.client.PacketOpenSetupGui;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

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
        if (!worldIn.isRemote) {
            NetWorkLoader.net.sendTo(new PacketOpenSetupGui(), (EntityPlayerMP)playerIn);
            //创造
            if (!playerIn.isCreative()) {
                playerIn.getHeldItem(hand).splitStack(1);
            }
        }
        return super.onItemRightClick(worldIn, playerIn, hand);
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        try {
            //用于启动或更改模拟城市的运行模式
            String windmill_base = new TextComponentTranslation("container.sim.item_sim_u_loader",new Object[0]).getUnformattedText();
            tooltip.add(windmill_base);
            super.addInformation(stack, worldIn, tooltip, flagIn);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];
            ModSimLoader.log.error("addInformation出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }
}