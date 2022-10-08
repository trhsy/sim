package com.trhsy.sim.item.tool;

import com.google.common.collect.Sets;
import com.trhsy.sim.loader.CreativeTabsLoader;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraftforge.common.util.EnumHelper;

import java.util.Set;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.item.tool
 * @ClassName: ItemTinAxe
 * @Description: 锡斧子
 * @date 2022/9/21 0021 上午 9:00
 */
public class ItemTinAxe extends ItemTool {
    /**
     * @Author fan
     * @Description //TODO 枚举 WOOD，STONE，IRON，EMERALD，GOLD
     * 木头、石头、铁、钻石、金
     * WOOD(0, 59, 2.0F, 0.0F, 15),
     * STONE(1, 131, 4.0F, 1, 5),
     * IRON(2, 250, 6.0F, 2.0F, 14),
     * EMERALD(3, 1561, 8.0F, 3.0F, 10),
     * GOLD(0, 32, 12.0F, 0.0F, 22);
     * harvestLevel参数表示制作出的工具等级。这一点在镐中尤其明显，如木头为0，只能挖掘对应等级为0的方块才能掉落物品，如石头等，而钻石为3，就可以挖掘出对应等级为3的，其他镐挖不出物品的方块，如黑曜石。这里使用了最高等级3
     * maxUses参数表示制作出的工具对应耐久。如钻石工具就是1561，耐久最高，而木工具为59，耐久最低。这里刻意250 与铁相同
     * efficiency参数表示制作出的工具使用效率。使用效率和该参数的值成正比。这里刻意提高了该数值，为16.0F
     * damageVsEntity参数表示攻击伤害力度。同样该力度和该参数的值成正相关。这里为4.0F，表示攻击力提高一点
     * enchantability参数与附魔等级相关。Minecraft中关于附魔等级的系统十分复杂。但是有一点，就是该值越高，对应的附魔就越容易得到高等级。这也是为何金更容易得到高等级附魔，而石头得到的附魔就相当低。这里为22，和金相同
     * @Date 14:48 2022/5/14
     * @Param
     * @return
     **/
    public static final Item.ToolMaterial REDSTONE = EnumHelper.addToolMaterial("TIN", 3, 500, 16.0F, 4.0F, 22);
    private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(new Block[] {Blocks.PLANKS, Blocks.BOOKSHELF, Blocks.LOG, Blocks.LOG2, Blocks.CHEST, Blocks.PUMPKIN, Blocks.LIT_PUMPKIN, Blocks.MELON_BLOCK, Blocks.LADDER, Blocks.WOODEN_BUTTON, Blocks.WOODEN_PRESSURE_PLATE});

    public ItemTinAxe() {
        super(REDSTONE,EFFECTIVE_ON);
        this.setUnlocalizedName("tinAxe");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);
        toolClass="axe";
    }

    @Override
    public float getStrVsBlock(ItemStack stack, IBlockState state) {
        Material material = state.getMaterial();
        return material != Material.WOOD && material != Material.PLANTS && material != Material.VINE ? super.getStrVsBlock(stack, state) : this.efficiencyOnProperMaterial;
    }
    /**
     * Return whether this item is repairable in an anvil.
     */
    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        ItemStack mat = REDSTONE.getRepairItemStack();
        if (mat != null && net.minecraftforge.oredict.OreDictionary.itemMatches(mat, repair, false)) {
            return true;
        }
        return super.getIsRepairable(toRepair, repair);
    }
    /*===================================== FORGE START =================================*/
    private String toolClass;
    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass)
    {
        int level = super.getHarvestLevel(stack, toolClass);
        if (level == -1 && toolClass != null && toolClass.equals(this.toolClass))
        {
            return this.toolMaterial.getHarvestLevel();
        }
        else
        {
            return level;
        }
    }

    @Override
    public Set<String> getToolClasses(ItemStack stack)
    {
        return toolClass != null ? com.google.common.collect.ImmutableSet.of(toolClass) : super.getToolClasses(stack);
    }
    /*===================================== FORGE END =================================*/
}