package com.trhsy.sim.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * 奶酪块
 */
public class BlockCheese extends Block {
    // ------------------------------ 私有构造（禁止外部直接实例化） ------------------------------
    /**
     * 私有构造方法（强制通过工厂方法创建）
     */
    public BlockCheese() {

        super(Block.Properties.create(Material.CAKE).sound(SoundType.STONE).hardnessAndResistance(0.1F,0.3F));  // 直接使用原生 Material.CAKE
        /*this.setSoundType(SoundType.WOOD);  // 木质点击音
        this.setHardness(0.1F);             // 易破坏
        this.setResistance(0.3F);           // 低抗性
        this.setCreativeTab(CreativeTabsLoader.tabSimU);  // 自定义创意标签页
        this.setUnlocalizedName("sim_u.cheeseBlock");  // 未本地化名称（关联 lang 文件）*/
    }

    /**
     * 添加物品提示信息（多语言支持）
     */
    public void addInformation(@Nonnull ItemStack stack, @Nullable World player,
                               @Nonnull List<String> tooltip, @Nonnull ITooltipFlag advanced) {
        // 从lang/en_us.json中读取提示文本（示例："block.sim.cheeseBlock.tooltip"="奶酪方块：易融化，需冷藏保存"）
        String fs_1 = new TextComponentTranslation("block.sim.cheeseBlock.tooltip", new Object[0]).getUnformattedComponentText();
        tooltip.add(fs_1);

        // 可选：添加状态提示（如是否被融化）
       /* if (this.isMelted()) {  // 假设新增了融化状态属性
            tooltip.add(I18n.translateToLocal("block.sim.cheeseBlock.melted"));
        }*/
    }
}
