package com.trhsy.sim.common.block;

import com.trhsy.sim.client.gui.blocks.GuiCityBox;
import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.creativetab.CreativeTabsLoader;
import com.trhsy.sim.common.entity.V3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.Random;

/**
 * @ClassName BlockCityBox
 * @Description todo
 * @Author Tian
 * @Date 2022/4/414:07
 **/
public class BlockCityBox extends Block {
    @SideOnly(Side.CLIENT)
    private IIcon[] icons;

    public BlockCityBox() {
        super(Material.wood);
        this.setStepSound(Block.soundTypeWood);
        this.setHardness(10.0F);
        this.setResistance(1.0F);
        this.setUnlocalizedName("city_box");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        this.icons = new IIcon[1];
        this.icons[0] = iconRegister.registerIcon(ModSim.MODID + ":" +"path_constructor");
    }
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return this.icons[0];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9) {
        world.playSoundEffect((double)i, (double)j, (double)k, "ashjacksimukraftreloaded:computer", 1.0F, 1.0F);
        GuiCityBox ui = null;
        Minecraft mc = Minecraft.getMinecraft();
        mc.setIngameNotInFocus();
        ui = new GuiCityBox(new V3((double)i, (double)j, (double)k, entityplayer.dimension), entityplayer);
        mc.displayGuiScreen(ui);
        return true;
    }
    @Override
    public int quantityDropped(Random random) {
        return 0;
    }
}