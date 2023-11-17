package com.trhsy.sim.block;

import com.trhsy.sim.loader.CreativeTabsLoader;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.block
 * @ClassName: BlockFlowingMilk
 * @Description:
 * @date 2023/11/07 下午 5:11
 */
public class BlockFlowingMilk extends BlockDynamicLiquid {
    public BlockFlowingMilk() {
        super(Material.WATER);
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