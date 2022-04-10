package com.trhsy.sim.common.block;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.creativetab.CreativeTabsLoader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;
import java.util.Random;

/**
 * ========================================
 *
 * @ClassName BlockLightBox
 * @Description todo 灯箱
 * @Author Administrator
 * @Date 2022/1/26 0026下午 5:02
 * ========================================
 **/
public class BlockLightBox extends Block {
    private IIcon[] icons;
    //public static final String[] names = new String[]{"White", "Red", "Orange", "Yellow", "Green", "Blue", "Purple", "Rainbow"};
    public String name;
    public BlockLightBox(String name) {
        super(Material.wood);
        this.setLightLevel(1.0F);
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
        this.setStepSound(Block.soundTypeWood);
        this.setHardness(2.0F);
        this.setResistance(1.0F);
        this.setTickRandomly(true);
        this.name=name;
        //this.setTextureName(ModSim.MODID + ":" + "light_block_White");
        //this.setUnlocalizedName("lightBox");
    }
    @Override
    public int damageDropped(int metadata) {
        return metadata;
    }

    @Override
    public String getUnlocalizedName(){
        return "tile.lightBox."+this.name;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.icons = new IIcon[1];
        for (int i = 0; i < this.icons.length; i++) {
            this.icons[0] = iconRegister.registerIcon(ModSim.MODID + ":light_block_" + this.name);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        //return meta < 8 && meta >= 0 ? this.icons[meta] : this.icons[0];
        return this.icons[meta];
    }

    public BlockLightBox idDropped(int par1, Random par2Random, int par3) {
        return this;
    }


    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        for (int i = 0; i < this.icons.length; ++i) {
            //par3List.add(new ItemStack(itemIn, 1, meta));
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public int getBlockColor() {
        return 16777215;
    }

}
