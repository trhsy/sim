package com.trhsy.sim.item.food;

import com.trhsy.sim.loader.CreativeTabsLoader;
import com.trhsy.sim.loader.ItemLoader;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
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
public class ItemDrink extends Item {
    public ItemDrink() {
        super(); //6个半红心，当吃了0.6f时默认为ItemFood
        this.setMaxStackSize(64);
        this.setUnlocalizedName("drinkBeer");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
        this.setHasSubtypes(true);
    }
    /**
     * 饮用完成后触发（核心逻辑：消耗物品、添加效果）
     */
    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        EntityPlayer player = (EntityPlayer) entityLiving;

        // 仅在服务器端执行逻辑（避免客户端同步问题）
        if (!worldIn.isRemote) {
            int meta = stack.getMetadata(); // 获取子类型
            // 根据子类型添加不同效果
//            if (meta == TYPE_LIGHT) {
                // 淡啤酒：轻微速度提升 + 少量饥饿恢复
                player.addPotionEffect(new PotionEffect(net.minecraft.potion.Potion.getPotionById(1), 200, 0)); // 速度I，10秒
                player.getFoodStats().addStats(2, 0.2F); // 恢复2点饥饿值，0.2饱和度
            /*} else if (meta == TYPE_DARK) {
                // 黑啤酒：轻微力量提升 + 微量中毒（模拟醉酒）
                player.addPotionEffect(new PotionEffect(net.minecraft.potion.Potion.getPotionById(5), 180, 0)); // 力量I，9秒
                player.addPotionEffect(new PotionEffect(net.minecraft.potion.Potion.getPotionById(19), 100, 0)); // 中毒I，5秒
                player.getFoodStats().addStats(3, 0.3F); // 恢复3点饥饿值，0.3饱和度
            }*/
        }

        // 消耗物品（最后一个不消耗，留给玩家空瓶逻辑，可选）
        if (!player.capabilities.isCreativeMode) {
            stack.shrink(1);
        }

        return stack.isEmpty() ? new ItemStack(ItemLoader.itemDrinkEmpty) : stack; // 替换为空瓶
    }
    @Override
    @SideOnly(Side.CLIENT)
    public ItemStack getDefaultInstance() {
        return PotionUtils.addPotionToItemStack(super.getDefaultInstance(), PotionTypes.WATER);
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
        // 只有当玩家饥饿值未满时才能饮用（可选逻辑）
        if (playerIn.getFoodStats().needFood()) {
            playerIn.setActiveHand(hand);
            return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
        } else {
            return new ActionResult<>(EnumActionResult.FAIL, itemStackIn);
        }
//        playerIn.setActiveHand(hand);
//        return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
    }

    /**
     * 允许项目将自定义信息行添加到鼠标悬停描述中
     *
     * @param advanced If the client has advanced tooltips (debug) enabled
     */
    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        String sim_folks = new TextComponentTranslation("container.sim.items_drink",new Object[0]).getUnformattedText();
        tooltip.add(sim_folks);
        PotionUtils.addPotionTooltip(stack, tooltip, 1.0F);

    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return super.hasEffect(stack) || !PotionUtils.getEffectsFromStack(stack).isEmpty();
    }

    @Override
    public int getMetadata(int par1) {
        return par1;
    }

}
