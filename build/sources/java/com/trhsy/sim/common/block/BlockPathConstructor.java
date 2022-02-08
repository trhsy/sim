package com.trhsy.sim.common.block;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import com.trhsy.sim.common.creativetab.CreativeTabsLoader;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.IIcon;

/**
 * ========================================
 *
 * @ClassName BlockPathConstructor
 * @Description todo 建造路径块
 * @Author Administrator
 * @Date 2022/1/26 0026下午 5:43
 * ========================================
 **/
public class BlockPathConstructor extends Block {
    private IIcon[] icons;

    public BlockPathConstructor(int par1) {
        super(Material.wood);
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }
}
