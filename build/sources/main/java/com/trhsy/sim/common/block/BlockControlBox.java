package com.trhsy.sim.common.block;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.entity.GameMode;
import com.trhsy.sim.common.entity.V3;
import com.trhsy.sim.common.gui.blocks.GuiBankATM;
import com.trhsy.sim.common.gui.blocks.GuiControlBox;
import com.trhsy.sim.common.loader.CreativeTabsLoader;
import com.trhsy.sim.common.loader.ModSimReloaded;
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
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Random;

/**
 * @ClassName BlockControlBox
 * @Description todo 控制箱
 * @Author Tian
 * @Date 2022/4/1921:48
 **/
public class BlockControlBox extends EnumBlock<EnumControlBoxMaterial> {
    public static final PropertyEnum<EnumControlBoxMaterial> TYPE = PropertyEnum.create("type", EnumControlBoxMaterial.class);

    public BlockControlBox() {
        super(Material.wood, TYPE, EnumControlBoxMaterial.class);
        this.setStepSound(Block.soundTypeWood);
        this.setHardness(10.0F);
        this.setResistance(1.0F);
        this.setUnlocalizedName("controlBox");
        this.setDefaultState(this.blockState.getBaseState().withProperty(TYPE, EnumControlBoxMaterial.TOP));
        //this.setTextureName(ModSim.MODID + ":" + "control_box");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
        //this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(MATERIAL, EnumControlBoxMaterial.ATM));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        try {
            EnumControlBoxMaterial[] boxMaterials = EnumControlBoxMaterial.values();
            for (int i = 0; i < boxMaterials.length; i++) {
                EnumControlBoxMaterial type = boxMaterials[i];
                list.add(new ItemStack(this, 1, type.meta));
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("控制箱getSubBlocks出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumControlBoxMaterial) state.getValue(TYPE)).meta;
    }

    @Override
    public int damageDropped(IBlockState state) {
        return this.getMetaFromState(state);
    }

    @Override
    protected BlockState createBlockState() {
        return new BlockState(this, new IProperty[]{TYPE});
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(TYPE, EnumControlBoxMaterial.fromMeta(meta));
    }

    /**
     * 当右键方块时
     * @param world
     * @param blockPos
     * @param iBlockState
     * @param thePlayer
     * @param enumFacing
     * @param par7
     * @param par8
     * @param par9
     * @return
     */
    @Override
    @SideOnly(Side.CLIENT)
    public boolean onBlockActivated(World world, BlockPos blockPos, IBlockState iBlockState, EntityPlayer thePlayer, EnumFacing enumFacing, float par7, float par8, float par9) {
        try {
            world.playSoundEffect(blockPos.getX(),blockPos.getY(),blockPos.getZ(), ModSim.MODID + ":computer", 1.0F, 1.0F);
            GuiControlBox ui = null;
            GuiBankATM ui2 = null;
            Minecraft mc = Minecraft.getMinecraft();
            mc.setIngameNotInFocus();
            IBlockState iBlockState1=world.getBlockState(blockPos);
            int ma=iBlockState1.getBlock().getMetaFromState(iBlockState1);
            if (ma != 0 && ma != 2) {
                if (GameMode.gameMode == GameMode.GAMEMODES.CREATIVE) {
                    mc.displayGuiScreen((GuiScreen) null);
                    //银行在创造模式下不活动（因为没有钱！）
                    String control_box_Creative = I18n.format("container.sim.control_box_Creative");
                    ModSimReloaded.sendChat(control_box_Creative);
                } else {
                    ui2 = new GuiBankATM(new V3(blockPos.getX(),blockPos.getY(),blockPos.getZ(), thePlayer.dimension), thePlayer);
                    mc.displayGuiScreen(ui2);
                }
            } else {
                ui = new GuiControlBox(new V3(blockPos.getX(),blockPos.getY(),blockPos.getZ(), thePlayer.dimension), thePlayer);
                mc.displayGuiScreen(ui);
            }

        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("控制箱onBlockActivated出错了：" + e.getMessage()+"行数："+element.getLineNumber());
            return false;
        }

        return true;
    }

    /**
     * 销毁时要丢弃的项目数量
     * @param random
     * @return
     */
    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

}
