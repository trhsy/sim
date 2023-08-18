package com.trhsy.sim.item.food;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.loader.CreativeTabsLoader;
import com.trhsy.sim.loader.ItemLoader;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.*;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.item.food
 * @ClassName: ItemDrink
 * @Description:
 * @date 2023/08/03 下午 3:55
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
     * Called when the player finishes using this Item (E.g. finishes eating.). Not called when the player stops using
     * the Item before the action is complete.
     * 当玩家使用完该物品时调用（例如，吃完饭。）。当玩家在动作完成前停止使用该物品时不调用。
     */
    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        //玩家
        EntityPlayer entityplayer = entityLiving instanceof EntityPlayer ? (EntityPlayer) entityLiving : null;
        //创造模式
        if (entityplayer == null || !entityplayer.capabilities.isCreativeMode) {
            --stack.stackSize;
        }
        //客户端
        if (!worldIn.isRemote) {
            //for (PotionEffect potioneffect : PotionUtils.getEffectsFromStack(stack)) {
                entityLiving.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 100, 2));
                //entityLiving.addPotionEffect(new PotionEffect(potioneffect));
            //}
        }
        //玩家不为空
        if (entityplayer != null) {
            //使用统计信息
            entityplayer.addStat(StatList.getObjectUseStats(this));
        }
        //用完返回玻璃瓶
        if (entityplayer == null || !entityplayer.capabilities.isCreativeMode) {
            if (stack.stackSize <= 0) {
                return new ItemStack(ItemLoader.itemDrinkEmpty);
            }

            if (entityplayer != null) {
                entityplayer.inventory.addItemStackToInventory(new ItemStack(ItemLoader.itemDrinkEmpty));
            }
        }

        return stack;
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
     * @param itemStackIn
     * @param worldIn
     * @param playerIn
     * @param hand
     * @return
     */
    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
        playerIn.setActiveHand(hand);
        return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
    }

    /**
     * 允许项目将自定义信息行添加到鼠标悬停描述中
     *
     * @param advanced If the client has advanced tooltips (debug) enabled
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        PotionUtils.addPotionTooltip(stack, tooltip, 1.0F);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean hasEffect(ItemStack stack) {
        return !PotionUtils.getEffectsFromStack(stack).isEmpty();
    }

    /**
     * returns a list of items with the same ID, but different meta (eg: dye returns 16 items)
     * 返回具有相同ID但元不同的项目列表（例如：dye返回16个项目）
     */
    //@Override
    //@SideOnly(Side.CLIENT)
    //public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
    //    subItems.add(PotionUtils.addPotionToItemStack(new ItemStack(itemIn), PotionType.getPotionTypeForID(2)));
    //    //entityLiving.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 100, 2));
    //    //for (PotionType potiontype : PotionType.REGISTRY) {
    //    //    subItems.add(PotionUtils.addPotionToItemStack(new ItemStack(itemIn), potiontype));
    //    //}
    //}


    @Override
    public int getMetadata(int par1) {
        return par1;
    }

}
