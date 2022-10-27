package com.trhsy.sim.common.block;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.client.gui.blocks.GuiPathBox;
import com.trhsy.sim.common.core.entity.V3;
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
 * @Description todo 路径箱
 * @Author Tian
 * @Date 2022/5/322:47
 **/
public class BlockPathBox extends Block {

    public BlockPathBox() {
        super(Material.wood);
        this.setStepSound(Block.soundTypeWood);
        this.setHardness(10.0F);
        this.setResistance(1);
        this.setUnlocalizedName("path_box");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState iBlockState, EntityPlayer thePlayer, EnumFacing enumFacing, float par7, float par8, float par9) {
        try {
            //world.playSoundEffect(blockPos.getX(),blockPos.getY(),blockPos.getZ(), ModSim.MODID+":computer", 1, 1);
            GuiPathBox ui = null;
            Minecraft mc = Minecraft.getMinecraft();
            mc.setIngameNotInFocus();
            ui = new GuiPathBox(new V3(blockPos.getX(),blockPos.getY(),blockPos.getZ(), thePlayer.dimension), thePlayer);
            mc.displayGuiScreen(ui);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("路径箱onBlockActivated出错了：" + e.getMessage()+"行数："+element.getLineNumber());
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
    public int quantityDropped(Random random) {
        return 0;
    }
}
