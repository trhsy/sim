package com.trhsy.sim.common.block;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.loader.CreativeTabsLoader;
import com.trhsy.sim.common.tileentity.TileEntityMetalControlBox;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * @ClassName BlockControlBox
 * @Description todo 控制箱
 * @Author Tian
 * @Date 2022/4/1921:48
 **/
public class BlockControlBox extends Block {
    public static final PropertyEnum<EnumControlBoxMaterial> TYPE = PropertyEnum.create("type", EnumControlBoxMaterial.class);
    @SideOnly(Side.CLIENT)
    public BlockControlBox() {
        super(Material.wood);
        this.setStepSound(Block.soundTypeWood);
        this.setHardness(10.0F);
        this.setResistance(1.0F);
        this.setUnlocalizedName("controlBox");
        //this.setTextureName(ModSim.MODID + ":" + "control_box");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
        //this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(MATERIAL, EnumControlBoxMaterial.ATM));
    }  
    
    /**
     * @Author fan
     * @Description //TODO 状态
     * @Date 15:12 2022/4/30
     * @Param []
     * @return net.minecraft.block.state.BlockState
     **/
    @Override
    protected BlockState createBlockState() {
        return new BlockState(this,new IProperty[]{TYPE});
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        EnumControlBoxMaterial[] boxMaterials=EnumControlBoxMaterial.values();
        for (int i = 0; i < boxMaterials.length; i++) {
            EnumControlBoxMaterial type = boxMaterials[1];
            list.add(new ItemStack(this, 1, type.meta));
        }
    }
    @Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumControlBoxMaterial)state.getValue(TYPE)).meta;
    }

    @Override
    public int damageDropped(IBlockState state) {
        return this.getMetaFromState(state);
    }

/**
     * @return boolean
     * @Author fan
     * @Description //TODO 当右键方块时
     * @Date 22:33 2022/4/27
     * @Param [world, i, j, k, entityplayer, par6, par7, par8, par9]
     **//*
    @SideOnly(Side.CLIENT)
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9) {
        world.playSoundEffect((double) i, (double) j, (double) k, ModSim.MODID + ":computer", 1.0F, 1.0F);
        GuiControlBox ui = null;
        GuiBankATM ui2 = null;
        Minecraft mc = Minecraft.getMinecraft();
        mc.setIngameNotInFocus();
        int ma=world.getBlockMetadata(i, j, k);
        if (ma != 0 && ma != 2) {
            if (GameMode.gameMode == GameMode.GAMEMODES.CREATIVE) {
                mc.displayGuiScreen((GuiScreen) null);
                //银行在创造模式下不活动（因为没有钱！）
                String control_box_Creative = I18n.format("container.sim.control_box_Creative");
                ModSimReloaded.sendChat(control_box_Creative);
            } else {
                ui2 = new GuiBankATM(new V3((double) i, (double) j, (double) k, entityplayer.dimension), entityplayer);
                mc.displayGuiScreen(ui2);
            }
        } else {
            ui = new GuiControlBox(new V3((double) i, (double) j, (double) k, entityplayer.dimension), entityplayer);
            mc.displayGuiScreen(ui);
        }

        return true;
    }*/

}
