package com.trhsy.sim.common.block.gases;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.creativetab.CreativeTabsLoader;
import com.trhsy.sim.common.loader.BlockLoader;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

/**
 * @ClassName BlockGasDispenser
 * @Description todo 加气机
 * @Author Tian
 * @Date 2022/4/1016:04
 **/
public class BlockGasDispenser extends BlockContainer implements ITileEntityProvider {
    String name;
    boolean isEnabled = true;
    @SideOnly(Side.CLIENT)
    private IIcon[] icons;
    public BlockGasDispenser(String name) {
        super(Material.rock);
        //this.setUnlocalizedName("blockGasDispenser."+this.name);
        this.setStepSound(Block.soundTypeStone);
        this.setHardness(1.0F);
        this.setResistance(1.8E7F);
       //this.setTextureName(ModSim.MODID +":blockGasDispenserSide");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
        this.setTickRandomly(true);
        this.name=name;
    }

    @Override
    public String getUnlocalizedName(){
        return "tile.blockGasDispenser."+this.name;
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        for(int i = 0; i < 1; ++i) {
            list.add(new ItemStack(item, 1, i));
        }

    }
    @Override
    public void updateTick(World world, int i, int j, int k, Random random) {
        if (world.getBlockMetadata(i, j, k) == 0 && this.isEnabled) {
            world.setBlock(i, j + 1, k, BlockLoader.blockCarbonDioxide);

        } else if (world.getBlockMetadata(i, j, k) == 1 && this.isEnabled) {
            world.setBlock(i, j + 1, k, BlockLoader.blockSulphurDioxide);

        } else if (world.getBlockMetadata(i, j, k) == 2 && this.isEnabled) {
            world.setBlock(i, j + 1, k, BlockLoader.blockRadiationGas);

        }

    }
    @Override
    public void onBlockAdded(World world, int i, int j, int k) {
        world.scheduleBlockUpdate(i, j, k, this, 50);
    }
    @Override
    public TileEntity createNewTileEntity(World world, int p_149915_2_) {
        return new TileEntityBlockGasDispenser();
    }
    @Override
    public int damageDropped(int meta) {
        return meta;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister) {
        this.icons = new IIcon[5];
        this.icons[0] = par1IconRegister.registerIcon(ModSim.MODID +":blockGasDispenserTop");
        this.icons[1] = par1IconRegister.registerIcon(ModSim.MODID +":blockGasDispenserSide");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return side == 1 ? this.icons[0] : this.icons[1];
    }
}
