package com.trhsy.sim.common.block;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.loader.CreativeTabsLoader;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
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
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
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
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    //public static final PropertyBool BURNING = PropertyBool.create("burning");
    public static final PropertyEnum<EnumControlBoxMaterial> MATERIAL = PropertyEnum.create("material", EnumControlBoxMaterial.class);

    @SideOnly(Side.CLIENT)
    public BlockControlBox() {
        super(Material.wood);
        this.setStepSound(Block.soundTypeWood);
        this.setHardness(10.0F);
        this.setResistance(1.0F);
        this.setUnlocalizedName("controlBox");
        //this.setTextureName(ModSim.MODID + ":" + "control_box");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(MATERIAL, EnumControlBoxMaterial.ATM));
    }


    @Override
    protected BlockState createBlockState() {
//        return new BlockState(this, FACING, BURNING, MATERIAL);
        return new BlockState(this,FACING,MATERIAL);
    }


    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        list.add(new ItemStack(itemIn, 1, 0));
        list.add(new ItemStack(itemIn, 1, 1));
        list.add(new ItemStack(itemIn, 1, 2));
        list.add(new ItemStack(itemIn, 1, 3));
    }

    /**
     * @return boolean
     * @Author fan
     * @Description //TODO 当右键方块时
     * @Date 22:33 2022/4/27
     * @Param [world, i, j, k, entityplayer, par6, par7, par8, par9]
     **/
    @SideOnly(Side.CLIENT)
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer entityplayer, int par6, float par7, float par8, float par9) {
        world.playSoundEffect((double) i, (double) j, (double) k, ModSim.MODID + ":computer", 1.0F, 1.0F);
        /*GuiControlBox ui = null;
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
        }*/

        return true;
    }

    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ,
                                     int meta, EntityLivingBase placer) {
        IBlockState origin = super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
        return origin.withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }


    /**
     * @return net.minecraft.block.state.IBlockState
     * @Author fan
     * @Description //TODO 将给定元数据转换为此块的BlockState
     * @Date 22:21 2022/4/27
     * @Param [meta]
     **/
    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing facing = EnumFacing.getHorizontal(meta & 3);
        EnumControlBoxMaterial material = EnumControlBoxMaterial.values()[meta >> 3];
        return this.getDefaultState().withProperty(FACING, facing).withProperty(MATERIAL, material);
    }

    /**
     * @return int
     * @Author fan
     * @Description //TODO 将BlockState转换为正确的元数据值
     * @Date 22:27 2022/4/27
     * @Param [state]
     **/
    @Override
    public int getMetaFromState(IBlockState state) {
        int facing = state.getValue(FACING).getHorizontalIndex();
        int material = state.getValue(MATERIAL).ordinal() << 3;
        return facing | material;
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(MATERIAL).ordinal() << 3;
    }
}
