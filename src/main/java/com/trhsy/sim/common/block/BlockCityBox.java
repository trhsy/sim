package com.trhsy.sim.common.block;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.loader.CreativeTabsLoader;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @ClassName BlockCityBox
 * @Description todo
 * @Author Tian
 * @Date 2022/5/322:47
 **/
public class BlockCityBox extends Block {
    public BlockCityBox() {
        super(Material.wood);
        this.setStepSound(Block.soundTypeWood);
        this.setHardness(10.0F);
        this.setResistance(1.0F);
        this.setUnlocalizedName("city_box");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }
    @SideOnly(Side.CLIENT)
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9) {
        world.playSoundEffect((double)i, (double)j, (double)k, ModSim.MODID+":computer", 1.0F, 1.0F);
//        GuiCityBox ui = null;
//        Minecraft mc = Minecraft.getMinecraft();
//        mc.setIngameNotInFocus();
//        ui = new GuiCityBox(new V3((double)i, (double)j, (double)k, entityplayer.dimension), entityplayer);
//        mc.displayGuiScreen(ui);
        return true;
    }
}
