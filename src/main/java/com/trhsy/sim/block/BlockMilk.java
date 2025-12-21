package com.trhsy.sim.block;

import com.trhsy.sim.loader.CreativeTabsLoader;
import com.trhsy.sim.loader.FluidLoader;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fml.common.FMLLog;
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
    // 定义常量，避免硬编码
    private static final String UNLOCALIZED_NAME = "fluidMilk";
    private static final float HARDNESS = 100.0F;
    private static final int LIGHT_OPACITY = 3;

    public BlockMilk() {
        super(FluidLoader.fluidMilk,Material.WATER);
        try {
            // 设置未本地化名称
            this.setUnlocalizedName(UNLOCALIZED_NAME);
            // 设置硬度
            this.setHardness(100.0F);
            // 设置光照透明度
            this.setLightOpacity(3);
            // 禁用统计信息
            this.disableStats();
            // 设置创造模式标签
//            this.setCreativeTab(CreativeTabsLoader.tabSimU);
        }catch (Exception e) {
            // 记录异常信息，方便调试
            FMLLog.log.error("Failed to initialize BlockMilk: {}", e.getMessage());
        }

    }

    /**
     * 为物品栈添加信息到工具提示中。
     * 目前该方法为空，如果需要添加工具提示信息，可以在这里实现。
     * @param stack 物品栈
     * @param player 玩家所在的世界
     * @param tooltip 工具提示列表
     * @param advanced 是否为高级工具提示
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        // 如果需要添加工具提示信息，可以在这里实现
        // item.blackMilk.tooltip 奶酪工厂专用牛奶
        String blackMilk = new TextComponentTranslation("item.blackMilk.tooltip",new Object[0]).getUnformattedText();
         tooltip.add(blackMilk);
    }
}
