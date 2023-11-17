package com.trhsy.sim.block;

import com.trhsy.sim.loader.CreativeTabsLoader;
import com.trhsy.sim.loader.FluidLoader;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.block
 * @ClassName: BlockMilk
 * @Description: 静态牛奶块
 * @date 2023/10/31 下午 1:58
 */
public class BlockMilk extends BlockFluidClassic {

    public BlockMilk() {
        super(FluidLoader.fluidMilk,Material.WATER);
        this.setUnlocalizedName("fluidMilk");
        this.setHardness(100.0F);
        this.setLightOpacity(3);
        this.disableStats();
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
    }
}
