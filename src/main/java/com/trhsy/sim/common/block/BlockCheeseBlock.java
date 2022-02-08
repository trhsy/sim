package com.trhsy.sim.common.block;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import com.trhsy.sim.common.ModSimukraft;
import com.trhsy.sim.common.creativetab.CreativeTabsLoader;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.IIcon;

/**
 * ========================================
 *
 * @ClassName BlockCheeseBlock
 * @Description todo 奶酪块
 * @Author Administrator
 * @Date 2022/1/26 0026下午 5:06
 * ========================================
 **/
public class BlockCheeseBlock extends Block {
    @SideOnly(Side.CLIENT)
    private IIcon[] icons;

    public BlockCheeseBlock() {
        super(Material.ground);
        this.setUnlocalizedName("block.cheeseBlock.name");
        this.setTextureName(ModSimukraft.MODID + ":" + "cheeseBlock");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.icons = new IIcon[1];
        this.icons[0] = iconRegister.registerIcon(ModSimukraft.MODID + ":cheeseblock");
    }
    private static void register(Block block, String name) {
        //注册方块
        GameRegistry.registerBlock(block,name);
    }
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return this.icons[0];
    }
}
