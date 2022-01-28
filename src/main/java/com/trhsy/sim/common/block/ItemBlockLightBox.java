package com.trhsy.sim.common.block;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * ========================================
 *
 * @ClassName ItemBlockLightBox
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/26 0026下午 6:01
 * ========================================
 **/
public class ItemBlockLightBox extends ItemBlock {
    public ItemBlockLightBox(Block par1, Block block) {
        super(par1);
        this.setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack is) {
        if (is.getMetadata() == 0) {
            return "tile.blockSUKLight.white";
        } else if (is.getMetadata() == 1) {
            return "tile.blockSUKLight.red";
        } else if (is.getMetadata() == 2) {
            return "tile.blockSUKLight.orange";
        } else if (is.getMetadata() == 3) {
            return "tile.blockSUKLight.yellow";
        } else if (is.getMetadata() == 4) {
            return "tile.blockSUKLight.green";
        } else if (is.getMetadata() == 5) {
            return "tile.blockSUKLight.blue";
        } else if (is.getMetadata() == 6) {
            return "tile.blockSUKLight.purple";
        } else {
            return is.getMetadata() == 7 ? "tile.blockSUKLight.rainbow" : null;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        par3List.add("Light up your world!");
        super.addInformation(par1ItemStack, par2EntityPlayer, par3List, par4);
    }

    @Override
    public int getMetadata(int par1) {
        return par1;
    }
}
