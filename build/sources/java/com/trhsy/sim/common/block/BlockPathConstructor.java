package com.trhsy.sim.common.block;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import com.trhsy.sim.common.ModSim;
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

    public BlockPathConstructor() {
        super(Material.wood);
        //用于设定走在方块上的响声。
        this.setStepSound(Block.soundTypeCloth);
        //设定方块的硬度，如黑曜石是50，铁块5，金块3，圆石2，石头1.5，南瓜1，泥土0.5，甘蔗0，基岩-1。
        this.setHardness(0.1F);
        //设定方块的爆炸抗性，如木头的抗性为4，石头为10，黑曜石为2000，基岩为6000000。
        this.setResistance(0.5F);
        this.setUnlocalizedName("pathConstructor");
        this.setTextureName(ModSim.MODID + ":" + "path_constructor");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }
}
