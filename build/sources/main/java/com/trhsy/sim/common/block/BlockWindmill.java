package com.trhsy.sim.common.block;

import com.trhsy.sim.common.core.entity.TileEntityWindmill;
import com.trhsy.sim.common.loader.CreativeTabsLoader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * @ClassName BlockWindmill
 * @Description todo 风车
 * @Author Tian
 * @Date 2022/5/822:49
 **/
public class BlockWindmill extends BlockContainer {
    public BlockWindmill() {
        super(Material.wood);
        //用于设定走在方块上的响声。
        this.setStepSound(Block.soundTypeCloth);
        //设定方块的硬度，如黑曜石是50，铁块5，金块3，圆石2，石头1.5，南瓜1，泥土0.5，甘蔗0，基岩-1。
        this.setHardness(0.1F);
        //设定方块的爆炸抗性，如木头的抗性为4，石头为10，黑曜石为2000，基岩为6000000。
        this.setResistance(0.5F);
        this.setUnlocalizedName("windmill");
        //this.setTextureName(ModSim.MODID + ":" + "block_windmill");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }

    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityWindmill();
    }
}
