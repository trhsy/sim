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
    public static final PropertyEnum<EnumControlBoxMaterial> TYPE = PropertyEnum.func_177709_a("type", EnumControlBoxMaterial.class);

    public BlockControlBox() {
        super(Material.field_151575_d, TYPE, EnumControlBoxMaterial.class);
        this.func_149672_a(Block.field_149766_f);
        this.func_149711_c(10.0F);
        this.func_149752_b(1.0F);
        this.func_149663_c("controlBox");
        this.func_180632_j(this.field_176227_L.func_177621_b().func_177226_a(TYPE, EnumControlBoxMaterial.TOP));
        //this.setTextureName(ModSim.MODID + ":" + "control_box");
        this.func_149647_a(CreativeTabsLoader.tabSimU);
        //this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(MATERIAL, EnumControlBoxMaterial.ATM));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void func_149666_a(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        try {
            EnumControlBoxMaterial[] boxMaterials = EnumControlBoxMaterial.values();
            for (int i = 0; i < boxMaterials.length; i++) {
                EnumControlBoxMaterial type = boxMaterials[i];
                list.add(new ItemStack(this, 1, type.meta));
            }
        } catch (Exception e) {
            ModSimReloaded.log.error("控制箱getSubBlocks出错了：" + e.getMessage());
        }

    }

    @Override
    public int func_176201_c(IBlockState state) {
        return ((EnumControlBoxMaterial) state.func_177229_b(TYPE)).meta;
    }

    @Override
    public int func_180651_a(IBlockState state) {
        return this.func_176201_c(state);
    }

    @Override
    protected BlockState func_180661_e() {
        return new BlockState(this, new IProperty[]{TYPE});
    }

    @Override
    public IBlockState func_176203_a(int meta) {
        return this.func_176223_P().func_177226_a(TYPE, EnumControlBoxMaterial.fromMeta(meta));
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
    public boolean func_180639_a(World world, BlockPos blockPos, IBlockState iBlockState, EntityPlayer thePlayer, EnumFacing enumFacing, float par7, float par8, float par9) {
        try {
            world.func_72908_a(blockPos.func_177958_n(),blockPos.func_177956_o(),blockPos.func_177952_p(), ModSim.MODID + ":computer", 1.0F, 1.0F);
            GuiControlBox ui = null;
            GuiBankATM ui2 = null;
            Minecraft mc = Minecraft.func_71410_x();
            mc.func_71364_i();
            IBlockState iBlockState1=world.func_180495_p(blockPos);
            int ma=iBlockState1.func_177230_c().func_176201_c(iBlockState1);
            if (ma != 0 && ma != 2) {
                if (GameMode.gameMode == GameMode.GAMEMODES.CREATIVE) {
                    mc.func_147108_a((GuiScreen) null);
                    //银行在创造模式下不活动（因为没有钱！）
                    String control_box_Creative = I18n.func_135052_a("container.sim.control_box_Creative");
                    ModSimReloaded.sendChat(control_box_Creative);
                } else {
                    ui2 = new GuiBankATM(new V3(blockPos.func_177958_n(),blockPos.func_177956_o(),blockPos.func_177952_p(), thePlayer.field_71093_bK), thePlayer);
                    mc.func_147108_a(ui2);
                }
            } else {
                ui = new GuiControlBox(new V3(blockPos.func_177958_n(),blockPos.func_177956_o(),blockPos.func_177952_p(), thePlayer.field_71093_bK), thePlayer);
                mc.func_147108_a(ui);
            }

        } catch (Exception e) {
            ModSimReloaded.log.error("控制箱onBlockActivated出错了：" + e.getMessage());
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
    public int func_149745_a(Random random) {
        return 0;
    }

}
