package com.trhsy.sim.common.block;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import com.trhsy.sim.client.gui.GuiBankATM;
import com.trhsy.sim.client.gui.GuiControlBox;
import com.trhsy.sim.common.GameMode;
import com.trhsy.sim.common.ModSim;
import com.trhsy.sim.common.creativetab.CreativeTabsLoader;
import com.trhsy.sim.common.entity.V3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import java.util.Random;

/**
 * ========================================
 *
 * @ClassName BlockControlBox
 * @Description todo 控制盒
 * @Author Administrator
 * @Date 2022/1/26 0026下午 5:10
 * ========================================
 **/
public class BlockControlBox extends Block {
    @SideOnly(Side.CLIENT)
    private IIcon[] icons;

    @SideOnly(Side.CLIENT)
    public BlockControlBox() {
        super(Material.wood);
        this.setStepSound(Block.soundTypeWood);
        this.setHardness(10.0F);
        this.setResistance(1.0F);
        this.setUnlocalizedName("controlBox");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister) {
        this.icons = new IIcon[4];
        this.icons[0] = par1IconRegister.registerIcon(ModSim.MODID + ":blockControlTop");
        this.icons[1] = par1IconRegister.registerIcon(ModSim.MODID + ":blockControlSide");
        this.icons[2] = par1IconRegister.registerIcon(ModSim.MODID + ":blockATM");
        this.icons[3] = par1IconRegister.registerIcon(ModSim.MODID + ":blockControlTopOther");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int par1, int par2) {
        switch(par2) {
            case 0:
                return this.icons[0];
            case 1:
                switch (par1) {
                    case 0:
                        return this.icons[1];
                    case 1:
                        return this.icons[2];
                    default:
                        return this.icons[3];
                }
            default:

                ModSim.log.info("元数据无效 " + this.getUnlocalizedName());
                return this.icons[0];
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9) {
        world.playSoundEffect((double) i, (double) j, (double) k, ModSim.MODID + ":computer", 1.0F, 1.0F);
        GuiControlBox ui = null;
        GuiBankATM ui2 = null;
        Minecraft mc = Minecraft.getMinecraft();
        mc.setIngameNotInFocus();
        if (world.getBlockMetadata(i, j, k) != 0 && world.getBlockMetadata(i, j, k) != 2) {
            if (ModSim.gameMode == GameMode.CREATIVE) {
                mc.displayGuiScreen((GuiScreen) null);
                String control_box_Creative = I18n.format("container.sim.control_box_Creative");
                ModSim.sendChat(control_box_Creative);
            } else {
                ui2 = new GuiBankATM(new V3((double) i, (double) j, (double) k, entityplayer.dimension), entityplayer);
                mc.displayGuiScreen(ui2);
            }
        } else {
            ui = new GuiControlBox(new V3((double) i, (double) j, (double) k, entityplayer.dimension), entityplayer);
            mc.displayGuiScreen(ui);
        }

        return true;
    }

    /**
     * @return int
     * @Author fan
     * @Description //TODO 返回块销毁时要丢弃的项目数量。
     * @Date 16:50 2022/2/12
     * @Param [random]
     **/
    @Override
    public int quantityDropped(Random random) {
        return 0;
    }
}