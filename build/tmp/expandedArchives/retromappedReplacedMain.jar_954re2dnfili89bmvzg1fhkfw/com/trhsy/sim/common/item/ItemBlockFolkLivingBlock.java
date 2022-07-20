package com.trhsy.sim.common.item;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * 地毯
 */
public class ItemBlockFolkLivingBlock extends ItemBlock {
    public ItemBlockFolkLivingBlock(Block block) {
        super(block);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void func_77624_a(ItemStack par1ItemStack,
                               EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        //建筑完成后，请放此地毯，用于NPC夜间活动区域
        par3List.add(I18n.func_135052_a("container.sim.Living1"));
        super.func_77624_a(par1ItemStack, par2EntityPlayer, par3List, par4);
    }
}
