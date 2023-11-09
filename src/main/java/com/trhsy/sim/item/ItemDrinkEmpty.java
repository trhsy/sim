package com.trhsy.sim.item;

import com.trhsy.sim.loader.CreativeTabsLoader;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.item
 * @ClassName: ItemDrinkEmpty
 * @Description: 空酒瓶
 * @date 2023/11/08 下午 1:49
 */
public class ItemDrinkEmpty extends ItemBase{
    public ItemDrinkEmpty() {
        super("drinkBeerEmpty");
        this.setMaxStackSize(64);
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
        this.setHasSubtypes(true);
    }
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
        ItemStack itemStackIn = playerIn.getHeldItem(hand);
        if (!playerIn.capabilities.isCreativeMode) {
            itemStackIn.shrink(1);
        }

        worldIn.playSound((EntityPlayer) null, playerIn.posX, playerIn.posY, playerIn.posZ, SoundEvents.ENTITY_SNOWBALL_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));

        //if (!worldIn.isRemote) {
        //    EntityDinkEmpty entitysnowball = new EntityDinkEmpty(worldIn, playerIn);
        //    entitysnowball.setHeadingFromThrower(playerIn, playerIn.rotationPitch, playerIn.rotationYaw, 0.0F, 1.5F, 1.0F);
        //    worldIn.spawnEntity(entitysnowball);
        //}

        playerIn.addStat(StatList.getObjectUseStats(this));
        return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
    }
}
