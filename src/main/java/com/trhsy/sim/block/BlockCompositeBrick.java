package com.trhsy.sim.block;

import com.trhsy.sim.loader.CreativeTabsLoader;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.block
 * @ClassName: BlockCompositeBrick
 * @Description: 复合砖 复合砖方块（优化版）
 *  * 改进点：名称规范化、多语言提示、属性集中管理
 * @date 2023/11/02 下午 5:11
 */
public class BlockCompositeBrick extends Block {
    // 提示信息键（对应 lang 文件中的翻译键）
    private static final String TOOLTIP_KEY = "block.sim.composite_brick.tooltip";


    public BlockCompositeBrick() {
        super(Material.ROCK);
        this.setSoundType(SoundType.STONE);
        this.setHardness(8.0F);
        this.setResistance(7.0F);
        //设置挖掘工具、挖掘等级
        this.setHarvestLevel("pickaxe",0);
        this.setUnlocalizedName(":composite_brick");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(@Nonnull ItemStack stack, @Nullable World worldIn, @Nonnull List<String> tooltip,@Nonnull ITooltipFlag advanced) {
        // 从 lang 文件读取翻译（格式：block.modid.composite_brick.tooltip）
        tooltip.add(I18n.translateToLocal(TOOLTIP_KEY));
    }
}