package com.trhsy.sim.common.block;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import com.trhsy.sim.common.creativetab.CreativeTabsLoader;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * ========================================
 *
 * @ClassName BlockWindmill
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/26 0026下午 5:44
 * ========================================
 **/
public class BlockWindmill extends BlockContainer {
    protected BlockWindmill(Material p_i45386_1_) {
        super(p_i45386_1_);
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }


    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return null;
    }
}
