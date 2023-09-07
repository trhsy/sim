package com.trhsy.sim.item.tool;

import com.google.common.collect.Sets;
import com.trhsy.sim.loader.CreativeTabsLoader;
import com.trhsy.sim.loader.ModSimLoader;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.*;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Set;

/**
 * @ClassName ItemTinTool
 * @Description todo
 * @Author TRHSY
 * @Date 2022/11/121:44
 **/
public class ItemTinTool {
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
    public static Item.ToolMaterial TIN = EnumHelper.addToolMaterial("TIN", 3, 1700, 18.0F, 6.0F, 44);

    public static class Axe extends ItemTool {
        private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(new Block[]{Blocks.PLANKS, Blocks.BOOKSHELF, Blocks.LOG, Blocks.LOG2, Blocks.CHEST, Blocks.PUMPKIN, Blocks.LIT_PUMPKIN, Blocks.MELON_BLOCK, Blocks.LADDER, Blocks.WOODEN_BUTTON, Blocks.WOODEN_PRESSURE_PLATE});

        public Axe() {
            super(8F, 0, TIN, EFFECTIVE_ON);
            this.setUnlocalizedName("tinAxe");
            this.setCreativeTab(CreativeTabsLoader.tabSimU);
        }

        @SideOnly(Side.CLIENT)
        @Override
        public boolean isFull3D() {
            return true;
        }

        @SideOnly(Side.CLIENT)
        @Override
        public boolean shouldRotateAroundWhenRendering() {
            return true;
        }

        @Override
        public float getStrVsBlock(ItemStack stack, IBlockState state) {
            Material material = state.getMaterial();
            return material != Material.WOOD && material != Material.PLANTS && material != Material.VINE ? super.getStrVsBlock(stack, state) : this.efficiencyOnProperMaterial;
        }
    }

    public static class Hoe extends ItemHoe {

        public Hoe() {
            super(TIN);
            this.setUnlocalizedName("tinHoe");
            this.setCreativeTab(CreativeTabsLoader.tabSimU);
        }

        @SideOnly(Side.CLIENT)
        @Override
        public boolean isFull3D() {
            return true;
        }

        @SideOnly(Side.CLIENT)
        @Override
        public boolean shouldRotateAroundWhenRendering() {
            return true;
        }
    }

    public static class Pickaxe extends ItemTool {
        private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(new Block[]{Blocks.ACTIVATOR_RAIL, Blocks.COAL_ORE, Blocks.COBBLESTONE, Blocks.DETECTOR_RAIL, Blocks.DIAMOND_BLOCK, Blocks.DIAMOND_ORE, Blocks.DOUBLE_STONE_SLAB, Blocks.GOLDEN_RAIL, Blocks.GOLD_BLOCK, Blocks.GOLD_ORE, Blocks.ICE, Blocks.IRON_BLOCK, Blocks.IRON_ORE, Blocks.LAPIS_BLOCK, Blocks.LAPIS_ORE, Blocks.LIT_REDSTONE_ORE, Blocks.MOSSY_COBBLESTONE, Blocks.NETHERRACK, Blocks.PACKED_ICE, Blocks.RAIL, Blocks.REDSTONE_ORE, Blocks.SANDSTONE, Blocks.RED_SANDSTONE, Blocks.STONE, Blocks.STONE_SLAB, Blocks.STONE_BUTTON, Blocks.STONE_PRESSURE_PLATE});

        public Pickaxe() {
            super(8F, 0, TIN, EFFECTIVE_ON);
            this.setUnlocalizedName("tinPickaxe");
            this.setCreativeTab(CreativeTabsLoader.tabSimU);
        }

        @SideOnly(Side.CLIENT)
        @Override
        public boolean isFull3D() {
            return true;
        }

        @SideOnly(Side.CLIENT)
        @Override
        public boolean shouldRotateAroundWhenRendering() {
            return true;
        }

        /**
         * 检查此项是否可以获取给定块
         */
        @Override
        public boolean canHarvestBlock(IBlockState blockIn) {
            Block block = blockIn.getBlock();

            if (block == Blocks.OBSIDIAN) {
                return this.toolMaterial.getHarvestLevel() == 3;
            } else if (block != Blocks.DIAMOND_BLOCK && block != Blocks.DIAMOND_ORE) {
                if (block != Blocks.EMERALD_ORE && block != Blocks.EMERALD_BLOCK) {
                    if (block != Blocks.GOLD_BLOCK && block != Blocks.GOLD_ORE) {
                        if (block != Blocks.IRON_BLOCK && block != Blocks.IRON_ORE) {
                            if (block != Blocks.LAPIS_BLOCK && block != Blocks.LAPIS_ORE) {
                                if (block != Blocks.REDSTONE_ORE && block != Blocks.LIT_REDSTONE_ORE) {
                                    Material material = blockIn.getMaterial();
                                    return material == Material.ROCK ? true : (material == Material.IRON ? true : material == Material.ANVIL);
                                } else {
                                    return this.toolMaterial.getHarvestLevel() >= 2;
                                }
                            } else {
                                return this.toolMaterial.getHarvestLevel() >= 1;
                            }
                        } else {
                            return this.toolMaterial.getHarvestLevel() >= 1;
                        }
                    } else {
                        return this.toolMaterial.getHarvestLevel() >= 2;
                    }
                } else {
                    return this.toolMaterial.getHarvestLevel() >= 2;
                }
            } else {
                return this.toolMaterial.getHarvestLevel() >= 2;
            }
        }

        @Override
        public float getStrVsBlock(ItemStack stack, IBlockState state) {
            Material material = state.getMaterial();
            return material != Material.IRON && material != Material.ANVIL && material != Material.ROCK ? super.getStrVsBlock(stack, state) : this.efficiencyOnProperMaterial;
        }
    }

    public static class Spade extends ItemTool {
        private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(new Block[]{Blocks.CLAY, Blocks.DIRT, Blocks.FARMLAND, Blocks.GRASS, Blocks.GRAVEL, Blocks.MYCELIUM, Blocks.SAND, Blocks.SNOW, Blocks.SNOW_LAYER, Blocks.SOUL_SAND, Blocks.GRASS_PATH});

        public Spade() {
            super(8F, 0, TIN, EFFECTIVE_ON);
            this.setUnlocalizedName("tinSpade");
            this.setCreativeTab(CreativeTabsLoader.tabSimU);
        }

        @Override
        @SideOnly(Side.CLIENT)
        public boolean isFull3D() {
            return true;
        }

        @Override
        @SideOnly(Side.CLIENT)
        public boolean shouldRotateAroundWhenRendering() {
            return true;
        }

        /**
         * Check whether this Item can harvest the given Block
         */
        @Override
        public boolean canHarvestBlock(IBlockState blockIn) {
            Block block = blockIn.getBlock();
            return block == Blocks.SNOW_LAYER ? true : block == Blocks.SNOW;
        }

        @Override
        public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
            if (!playerIn.canPlayerEdit(pos.offset(facing), facing, stack)) {
                return EnumActionResult.FAIL;
            } else {
                IBlockState iblockstate = worldIn.getBlockState(pos);
                Block block = iblockstate.getBlock();

                if (facing != EnumFacing.DOWN && worldIn.getBlockState(pos.up()).getMaterial() == Material.AIR && block == Blocks.GRASS) {
                    IBlockState iblockstate1 = Blocks.GRASS_PATH.getDefaultState();
                    Minecraft mc = Minecraft.getMinecraft();
                    for (EntityPlayer entityPlayer : mc.theWorld.playerEntities) {
                        mc.theWorld.playSound(entityPlayer,entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.AMBIENT, 1.0F, 1.0F);
                    }

                    if (!worldIn.isRemote) {
                        worldIn.setBlockState(pos, iblockstate1, 11);
                        stack.damageItem(1, playerIn);
                    }

                    return EnumActionResult.SUCCESS;
                } else {
                    return EnumActionResult.PASS;
                }
            }
        }
    }

    public static class Sword extends ItemSword {
        public static Item.ToolMaterial TIN = EnumHelper.addToolMaterial("TIN", 3, 1700, 18.0F, 16.0F, 44);

        public Sword() {
            super(TIN);
            this.setUnlocalizedName("tinSword");
            this.setCreativeTab(CreativeTabsLoader.tabSimU);
        }
        @Override
        @SideOnly(Side.CLIENT)
        public boolean isFull3D() {
            return true;
        }
        @Override
        @SideOnly(Side.CLIENT)
        public boolean shouldRotateAroundWhenRendering() {
            return true;
        }
    }
}
