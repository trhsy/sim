package com.trhsy.sim.item.food;

import com.trhsy.sim.loader.CreativeTabsLoader;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.item.food
 * @ClassName: ItemDrink
 * @Description: 啤酒
 * @date 2023/11/09 下午 2:45
 */
public class ItemDrink  extends Item {
    public ItemDrink() {
        super(); //6个半红心，当吃了0.6f时默认为ItemFood
        this.setMaxStackSize(64);
        this.setUnlocalizedName("drinkBeer");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
        this.setHasSubtypes(true);
    }

    /**
     * 使用或消费物品需要多长时间
     */
    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 32;
    }

    /**
     * 返回指定使用项目时要播放的动画的操作
     */
    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.DRINK;
    }

    /**
     * 右键
     *
     * @param worldIn
     * @param playerIn
     * @param hand
     * @return
     */
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
        ItemStack itemStackIn = playerIn.getHeldItem(hand);
        playerIn.setActiveHand(hand);
        return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
    }

    /**
     * 允许项目将自定义信息行添加到鼠标悬停描述中
     *
     * @param advanced If the client has advanced tooltips (debug) enabled
     */
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        PotionUtils.addPotionTooltip(stack, tooltip, 1.0F);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return !PotionUtils.getEffectsFromStack(stack).isEmpty();
    }
    @Override
    public int getMetadata(int par1) {
        return par1;
    }

}
