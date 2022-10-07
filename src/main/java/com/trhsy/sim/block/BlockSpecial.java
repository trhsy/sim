package com.trhsy.sim.block;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.loader.CreativeTabsLoader;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
/**
 * @Author fan
 * @Description //TODO 空气
 * @Date 18:46 2022/10/7
 * @Param 
 * @return 
 **/
public class BlockSpecial extends BlockAir{

    public static final ResourceLocation still = new ResourceLocation(ModSim.MODID + ":" + "block_special");

    public BlockSpecial() {
//        super(Material.air);
        //this.setRiseRate(5);
        this.setTickRandomly(true);
        this.disableStats();
        this.setHardness(0.0F);
        this.setUnlocalizedName("blockSpecial");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }
    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }
    @Override
    public boolean isFullCube(IBlockState state)
    {
        return false;
    }
}
