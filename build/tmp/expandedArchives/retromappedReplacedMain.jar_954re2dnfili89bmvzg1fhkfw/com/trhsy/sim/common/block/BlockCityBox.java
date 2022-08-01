package com.trhsy.sim.common.block;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.entity.V3;
import com.trhsy.sim.common.gui.blocks.GuiCityBox;
import com.trhsy.sim.common.loader.CreativeTabsLoader;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

/**
 * @ClassName BlockCityBox
 * @Description todo 城市路径块
 * @Author Tian
 * @Date 2022/5/322:47
 **/
public class BlockCityBox extends Block {
    public BlockCityBox() {
        super(Material.field_151575_d);
        this.func_149672_a(Block.field_149766_f);
        this.func_149711_c(10.0F);
        this.func_149752_b(1.0F);
        this.func_149663_c("city_box");
        //this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean func_180639_a(World world, BlockPos blockPos, IBlockState iBlockState, EntityPlayer thePlayer, EnumFacing enumFacing, float par7, float par8, float par9) {
        try {
            world.func_72908_a(blockPos.func_177958_n(),blockPos.func_177956_o(),blockPos.func_177952_p(), ModSim.MODID+":computer", 1.0F, 1.0F);
            GuiCityBox ui = null;
            Minecraft mc = Minecraft.func_71410_x();
            mc.func_71364_i();
            ui = new GuiCityBox(new V3(blockPos.func_177958_n(),blockPos.func_177956_o(),blockPos.func_177952_p(), thePlayer.field_71093_bK), thePlayer);
            mc.func_147108_a(ui);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("城市方块onBlockActivated出错了：" + e.getMessage()+"行数："+element.getLineNumber());
            return false;
        }
        return true;
    }

    /**
     * 掉落数量为0，方块敲了就消失
     * @param random
     * @return
     */
    @Override
    public int func_149745_a(Random random) {
        return 0;
    }
}
