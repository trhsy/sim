package com.trhsy.sim.block;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.entity.TileEntityWindmill;
import com.trhsy.sim.loader.BlockLoader;
import com.trhsy.sim.loader.ModSimClientLoader;
import com.trhsy.sim.loader.ModSimLoader;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatBasic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.block
 * @ClassName: BlockWindmill
 * @Description: 风车方块类，处理风车方块的各种行为和属性
 * 改进点：状态管理、声音事件规范、多语言支持、代码健壮性
 * @date 2023/11/08 上午 11:17
 */
public class BlockWindmill extends BlockContainer{

    // 方块属性：朝向（水平方向）
    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    // 方块属性：燃烧状态（是否运行）
    public static final PropertyBool BURNING = PropertyBool.create("burning");
    // 交互统计项
    public static final StatBase INTERACTION_STAT = (new StatBasic(
            "stat.sim.windmill.interact",
            new TextComponentTranslation("tooltip.sim.windmill.interact")
    )).registerStat();



    private boolean isBurning;
    public static final StatBase WINDMILL_INTERACTION = (new StatBasic("stat.windmillInteraction", new TextComponentTranslation("stat.windmillInteraction", new Object[0]))).registerStat();
    private static boolean keepInventory;
    // 定义声音事件的资源位置常量
    // 声音事件（需在ModSim初始化时注册）
//    private static final ResourceLocation WINDMILL_SOUND = new ResourceLocation(ModSim.MODID + ":windmill");
    public static final SoundEvent SOUND_WINDMILL = new SoundEvent(new ResourceLocation(ModSim.MODID, "windmill"));

    public BlockWindmill(boolean isBurning) {
        super(Material.WOOD);
        //用于设定走在方块上的响声。
        this.setSoundType(SoundType.WOOD);
        //设定方块的硬度，如黑曜石是50，铁块5，金块3，圆石2，石头1.5，南瓜1，泥土0.5，甘蔗0，基岩-1。
        this.setHardness(0.1F);
        //设定方块的爆炸抗性，如木头的抗性为4，石头为10，黑曜石为2000，基岩为6000000。
        this.setResistance(0.5F);
//        this.setUnlocalizedName("windmill");
        //this.setTextureName(ModSim.MODID + ":" + "windmill");
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
//        this.setCreativeTab(CreativeTabsLoader.tabSimU);
        this.isBurning = isBurning;

    }
    /**
     * 获取该区块在收割时应该掉落的物品。
     * 获取方块被破坏时掉落的物品（自身）
     */
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(this);
    }

    /**
     * 方块放置后调整默认朝向（避免卡在墙内）
     * @param worldIn
     * @param pos
     * @param state
     */
    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote) {
            this.setDefaultFacing(worldIn, pos, state);
        }
//        this.setDefaultFacing(worldIn, pos, state);
    }
    /**
     * 设置方块的默认朝向
     *调整朝向逻辑：选择最近的空位方向
     * @param worldIn 世界对象
     * @param pos     方块位置
     * @param state   方块状态
     */
    private void setDefaultFacing(World worldIn, BlockPos pos, IBlockState state) {
        EnumFacing currentFacing = state.getValue(FACING);
        // 检查四个水平方向，选择第一个空位方向
        for (EnumFacing facing : EnumFacing.HORIZONTALS) {
            BlockPos checkPos = pos.offset(facing);
            if (!worldIn.getBlockState(checkPos).isFullBlock()) {
                worldIn.setBlockState(pos, state.withProperty(FACING, facing), 2);
                return;
            }
        }
        // 若四周都被阻挡，保持原朝向
        worldIn.setBlockState(pos, state, 2);

        /*if (!worldIn.isRemote) {
            // 获取东南西北四个方向的方块状态
            IBlockState iblockstate = worldIn.getBlockState(pos.north());
            IBlockState iblockstate1 = worldIn.getBlockState(pos.south());
            IBlockState iblockstate2 = worldIn.getBlockState(pos.west());
            IBlockState iblockstate3 = worldIn.getBlockState(pos.east());
            EnumFacing enumfacing = (EnumFacing) state.getValue(FACING);
            // 根据周围方块状态调整朝向
            if (enumfacing == EnumFacing.NORTH && iblockstate.isFullBlock() && !iblockstate1.isFullBlock()) {
                enumfacing = EnumFacing.SOUTH;
            } else if (enumfacing == EnumFacing.SOUTH && iblockstate1.isFullBlock() && !iblockstate.isFullBlock()) {
                enumfacing = EnumFacing.NORTH;
            } else if (enumfacing == EnumFacing.WEST && iblockstate2.isFullBlock() && !iblockstate3.isFullBlock()) {
                enumfacing = EnumFacing.EAST;
            } else if (enumfacing == EnumFacing.EAST && iblockstate3.isFullBlock() && !iblockstate2.isFullBlock()) {
                enumfacing = EnumFacing.WEST;
            }
            worldIn.setBlockState(pos, state.withProperty(FACING, enumfacing), 2);
        }*/
    }

    /**
     * 客户端随机显示粒子和声音效果
     * @param stateIn 方块状态
     * @param worldIn 世界对象
     * @param pos     方块位置
     * @param rand    随机数生成器
     */
    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("incomplete-switch")
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (this.isBurning) {
            // 10%概率播放风车运转声
            if (rand.nextDouble() < 0.1D) {
                worldIn.playSound(
                        (double) pos.getX() + 0.5D,
                        (double) pos.getY(),
                        (double) pos.getZ() + 0.5D,
                        SOUND_WINDMILL,
                        SoundCategory.BLOCKS,
                        0.5F,  // 音量
                        1.0F + (rand.nextFloat() - 0.5F) * 0.2F,  // 音高随机变化
                        false
                );
            }
        }
        /*if (this.isBurning) {
            if (rand.nextDouble() < 0.1D) {
                SoundEvent soundEvent = new SoundEvent(WINDMILL_SOUND);
                worldIn.playSound((double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, soundEvent, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
//                worldIn.playSound((double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, soundEvent, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            }
        }*/
    }
    /**
     * @return boolean
     * @Author fan
     * @Description //TODO 右键激活 右键激活方块
     * @Date 11:16 2023/8/20
     * @param worldIn  世界对象
     * @param pos      方块位置
     * @param state    方块状态
     * @param playerIn 玩家对象
     * @param hand     玩家手持物品的手
     * @param side     点击的方块面
     * @param hitX     点击的X坐标
     * @param hitY     点击的Y坐标
     * @param hitZ     点击的Z坐标
     * @return 是否激活成功
     **/
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        try {

            //在给定块位置的中心为播放器播放指定的声音 constructor activated 控制箱激活
            if (worldIn.isRemote) {
                return true;
            } else {
//                NetWorkLoader.net.sendTo(new PacketOpenWindmillGui(playerIn, new V3(pos, playerIn.dimension)), (EntityPlayerMP) playerIn);
                TileEntity tileentity = worldIn.getTileEntity(pos);
                if (tileentity instanceof TileEntityWindmill) {

                    TileEntityWindmill tileEntityWindmill=(TileEntityWindmill)tileentity;
                    ModSimClientLoader.openWindmill(playerIn.inventory, tileEntityWindmill);
//                    playerIn.openContainer((IInteractionObject) tileentity); // 打开GUI
                    //playerIn.displayGUIChest((TileEntityWindmill) tileentity);
                    /*EntityPlayerMP entityPlayerMP= (EntityPlayerMP) playerIn;

                    entityPlayerMP.getNextWindowId();
                    entityPlayerMP.connection.sendPacket(new SPacketOpenWindow(entityPlayerMP.currentWindowId,((IInteractionObject)tileEntityWindmill).getGuiID(), tileEntityWindmill.getDisplayName(), tileEntityWindmill.getSizeInventory()));
                    entityPlayerMP.openContainer = ((IInteractionObject)tileEntityWindmill).createContainer(entityPlayerMP.inventory, entityPlayerMP);
                    entityPlayerMP.openContainer.windowId = entityPlayerMP.currentWindowId;
                    entityPlayerMP.openContainer.addListener(entityPlayerMP);
                    net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.player.PlayerContainerEvent.Open(entityPlayerMP, entityPlayerMP.openContainer));*/
                    playerIn.addStat(WINDMILL_INTERACTION);// 统计交互次数
                }
                return true;
            }
        } catch (Exception e) {
            logError("BlockWindmill-onBlockActivated出错了", e);
            return false;
        }
    }
    /**
     * 切换燃烧状态（由TileEntity触发）
     */
    public static void setBurningState(World worldIn, BlockPos pos, boolean burning) {
        IBlockState state = worldIn.getBlockState(pos);
        if (state.getValue(BURNING) != burning) {
            worldIn.setBlockState(pos, state.withProperty(BURNING, burning), 3); // 通知客户端更新
        }
    }
    /**
     * 设置方块的激活状态
     *
     * @param active   是否激活
     * @param worldIn  世界对象
     * @param pos      方块位置
     */
    public static void setState(boolean active, World worldIn, BlockPos pos) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        keepInventory = true;

        IBlockState newState = active ? BlockLoader.litBlockWindmill.getDefaultState() : BlockLoader.blockWindmill.getDefaultState();
        newState = newState.withProperty(FACING, iblockstate.getValue(FACING));
        worldIn.setBlockState(pos, newState, 3);

        keepInventory = false;

        if (tileentity != null) {
            tileentity.validate();
            worldIn.setTileEntity(pos, tileentity);
        }
    }


    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityWindmill();
    }

    /**
     * 放置方块时的处理逻辑
     *
     * @param world  世界对象
     * @param pos    方块位置
     * @param state  方块状态
     * @param placer 放置者
     * @param stack  物品栈
     */
    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
            try {
                world.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
                if (stack.hasDisplayName()) {
                    TileEntity tileentity = world.getTileEntity(pos);
                    if (tileentity instanceof TileEntityWindmill) {
                        ((TileEntityWindmill) tileentity).setCustomInventoryName(stack.getDisplayName());
                    }
                }
            } catch (Exception e) {
                logError("路径箱onBlockPlacedBy出错了", e);
            }
    }
        /**
         * 方块被破坏时的处理逻辑
         *
         * @param worldIn 世界对象
         * @param pos     方块位置
         * @param state   方块状态
         */
    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (!keepInventory) {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntityWindmill) {
                InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityWindmill) tileentity);
                worldIn.updateComparatorOutputLevel(pos, this);
            }
        }

        super.breakBlock(worldIn, pos, state);
    }
    /**
     * 判断方块是否具有比较器输入覆盖
     *
     * @param state 方块状态
     * @return 是否具有比较器输入覆盖
     */
    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    /**
     * 获取比较器输入覆盖的值
     *
     * @param blockState 方块状态
     * @param worldIn    世界对象
     * @param pos        方块位置
     * @return 比较器输入覆盖的值
     */
    @Override
    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
        return Container.calcRedstone(worldIn.getTileEntity(pos));
    }
    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(this);
    }
    /**
     * 获取方块的渲染类型
     *
     * @param state 方块状态
     * @return 方块的渲染类型
     */
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    /**
     * 将给定的元数据转换为此块的BlockState
     *
     * @param meta 元数据
     * @return 方块状态
     */
    @Override
    public IBlockState getStateFromMeta(int meta) {
        EnumFacing enumfacing = EnumFacing.getFront(meta);

        if (enumfacing.getAxis() == EnumFacing.Axis.Y) {
            enumfacing = EnumFacing.NORTH;
        }

        return this.getDefaultState().withProperty(FACING, enumfacing);
    }

    /**
     * 将BlockState转换为正确的元数据值
     *
     * @param state
     * @return
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumFacing) state.getValue(FACING)).getIndex();
    }
    /**
     * 从传递的块状态返回具有给定旋转的块状态。如果不适用，则返回传递的块状态。
     *
     * @param state
     * @param rot
     * @return
     */
    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate((EnumFacing) state.getValue(FACING)));
    }
    /**
     * 返回具有所传递块状态的给定镜像的块状态。如果不适用，则返回传递的块状态。
     *
     * @param state
     * @param mirrorIn
     * @return
     */
    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation((EnumFacing) state.getValue(FACING)));
    }
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{FACING});
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
        // 添加多语言提示（显示在物品栏）
        tooltip.add(I18n.format("tooltip.sim.windmill.description"));
    }
    /**
     * 统一日志记录方法
     *
     * @param message 错误信息
     * @param e       异常对象
     */
    private void logError(String message, Exception e) {
        StackTraceElement element = e.getStackTrace()[0];
        ModSimLoader.log.error("{}：{} 行数：{}", message, e.getMessage(), element.getLineNumber());
    }
}
