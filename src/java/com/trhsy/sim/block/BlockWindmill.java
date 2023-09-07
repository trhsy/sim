package com.trhsy.sim.block;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.entity.ContainerWindmill;
import com.trhsy.sim.entity.TileEntityWindmill;
import com.trhsy.sim.loader.BlockLoader;
import com.trhsy.sim.loader.CreativeTabsLoader;
import com.trhsy.sim.loader.ModSimLoader;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatBasic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class BlockWindmill extends BlockContainer {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    private boolean isBurning;
    public static final StatBase WINDMILL_INTERACTION = (new StatBasic("stat.windmillInteraction", new TextComponentTranslation("stat.windmillInteraction", new Object[0]))).registerStat();
    private static boolean keepInventory;

    public BlockWindmill(Material material) {
        super(material);
        //用于设定走在方块上的响声。
        this.setSoundType(SoundType.WOOD);
        //设定方块的硬度，如黑曜石是50，铁块5，金块3，圆石2，石头1.5，南瓜1，泥土0.5，甘蔗0，基岩-1。
        this.setHardness(0.1F);
        //设定方块的爆炸抗性，如木头的抗性为4，石头为10，黑曜石为2000，基岩为6000000。
        this.setResistance(0.5F);
        this.setUnlocalizedName("windmill");
        //this.setTextureName(ModSim.MODID + ":" + "windmill");
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
        this.setCreativeTab(CreativeTabsLoader.tabSimU);

    }

    /**
     * 获取该区块在收割时应该掉落的物品。
     */
    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(BlockLoader.blockWindmill);
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        this.setDefaultFacing(worldIn, pos, state);
    }

    private void setDefaultFacing(World worldIn, BlockPos pos, IBlockState state) {
        if (!worldIn.isRemote) {
            IBlockState iblockstate = worldIn.getBlockState(pos.north());
            IBlockState iblockstate1 = worldIn.getBlockState(pos.south());
            IBlockState iblockstate2 = worldIn.getBlockState(pos.west());
            IBlockState iblockstate3 = worldIn.getBlockState(pos.east());
            EnumFacing enumfacing = (EnumFacing) state.getValue(FACING);

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
        }
    }

    /**
     * @param stateIn
     * @param worldIn
     * @param pos
     * @param rand
     */
    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("incomplete-switch")
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (this.isBurning) {
           /* EnumFacing enumfacing = (EnumFacing) stateIn.getValue(FACING);
            double d0 = (double) pos.getX() + 0.5D;
            double d1 = (double) pos.getY() + rand.nextDouble() * 6.0D / 16.0D;
            double d2 = (double) pos.getZ() + 0.5D;
            double d3 = 0.52D;
            double d4 = rand.nextDouble() * 0.6D - 0.3D;*/
            if (rand.nextDouble() < 0.1D) {
                SoundEvent soundEvent = new SoundEvent(new ResourceLocation(ModSim.MODID + ":windmill"));
                worldIn.playSound((double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, soundEvent, SoundCategory.BLOCKS, 1.0F, 1.0F, false);
            }
            //烟雾特效
            //switch (enumfacing) {
            //    case WEST:
            //        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 - d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
            //        //worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 - d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
            //        break;
            //    case EAST:
            //        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
            //        //worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D, new int[0]);
            //        break;
            //    case NORTH:
            //        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 - d3, 0.0D, 0.0D, 0.0D, new int[0]);
            //        //worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d4, d1, d2 - d3, 0.0D, 0.0D, 0.0D, new int[0]);
            //        break;
            //    case SOUTH:
            //        worldIn.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d0 + d4, d1, d2 + d3, 0.0D, 0.0D, 0.0D, new int[0]);
            //        //worldIn.spawnParticle(EnumParticleTypes.FLAME, d0 + d4, d1, d2 + d3, 0.0D, 0.0D, 0.0D, new int[0]);
            //}
        }
    }

    /**
     * @return boolean
     * @Author fan
     * @Description //TODO 右键激活
     * @Date 11:16 2023/8/20
     * @Param [worldIn, pos, state, playerIn, hand, heldItem, side, hitX, hitY, hitZ]
     **/
    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        try {
            this.isBurning = true;
            //在给定块位置的中心为播放器播放指定的声音 constructor activated 控制箱激活
            if (worldIn.isRemote) {
                return true;
            } else {
                //NetWorkLoader.net.sendTo(new PacketOpenWindmillGui(playerIn, new V3(pos, playerIn.dimension)), (EntityPlayerMP) playerIn);
                TileEntity tileentity = worldIn.getTileEntity(pos);
                if (tileentity instanceof TileEntityWindmill) {
                    TileEntityWindmill tileEntityWindmill=(TileEntityWindmill)tileentity;
                    ModSimLoader.OpenWindmill(playerIn.inventory, tileEntityWindmill);
                    EntityPlayerMP entityPlayerMP= (EntityPlayerMP) playerIn;
                    //entityPlayerMP.displayGUIChest((TileEntityWindmill) tileentity);
                    entityPlayerMP.getNextWindowId();
                    entityPlayerMP.connection.sendPacket(new SPacketOpenWindow(entityPlayerMP.currentWindowId,tileEntityWindmill.getGuiID(), tileEntityWindmill.getDisplayName(), tileEntityWindmill.getSizeInventory()));
                    entityPlayerMP.openContainer = tileEntityWindmill.createContainer(entityPlayerMP.inventory, entityPlayerMP);
                    entityPlayerMP.openContainer.windowId = entityPlayerMP.currentWindowId;
                    entityPlayerMP.openContainer.addListener(entityPlayerMP);

                    playerIn.addStat(WINDMILL_INTERACTION);
                }
                return true;
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("BlockWindmill-onBlockActivated出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
            return false;
        }
    }

    public static void setState(boolean active, World worldIn, BlockPos pos) {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        keepInventory = true;

        if (active) {
            worldIn.setBlockState(pos, BlockLoader.blockWindmill.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)), 3);
            worldIn.setBlockState(pos, BlockLoader.blockWindmill.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)), 3);
        } else {
            worldIn.setBlockState(pos, BlockLoader.blockWindmill.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)), 3);
            worldIn.setBlockState(pos, BlockLoader.blockWindmill.getDefaultState().withProperty(FACING, iblockstate.getValue(FACING)), 3);
        }

        keepInventory = false;

        if (tileentity != null) {
            //验证互动程序实体
            tileentity.validate();
            //重新设置实体
            worldIn.setTileEntity(pos, tileentity);
        }
    }

    /**
     * 返回块的平铺实体类的新实例。在放置块时调用。
     *
     * @param worldIn
     * @param meta
     * @return
     */
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityWindmill();
    }

    /**
     * 在实际设置块之前由ItemBlocks调用，以允许调整IBlockstate
     *
     * @param worldIn
     * @param pos
     * @param facing
     * @param hitX
     * @param hitY
     * @param hitZ
     * @param meta
     * @param placer
     * @return
     */
    @Override
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        //获取此实体的水平方向。并设置
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 放置
     * @Date 11:16 2023/8/20
     * @Param [world, pos, state, placer, stack]
     **/
    @Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        try {
            //放置的时候设置方块
            world.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);
            if (stack.hasDisplayName()) {
                //获取当前方块实体
                TileEntity tileentity = world.getTileEntity(pos);
                //是风车
                if (tileentity instanceof TileEntityWindmill) {
                    //设置自定义名称
                    ((TileEntityWindmill) tileentity).setCustomInventoryName(stack.getDisplayName());
                }
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("路径箱onBlockPlacedBy出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }

    /**
     * 被损坏
     *
     * @param worldIn
     * @param pos
     * @param state
     */
    @Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {
        if (!keepInventory) {
            TileEntity tileentity = worldIn.getTileEntity(pos);

            if (tileentity instanceof TileEntityFurnace) {
                InventoryHelper.dropInventoryItems(worldIn, pos, (TileEntityFurnace) tileentity);
                worldIn.updateComparatorOutputLevel(pos, this);
            }
        }

        super.breakBlock(worldIn, pos, state);
    }

    /**
     * 具有比较器输入覆盖
     *
     * @param state
     * @return
     */
    @Override
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    /**
     * 获取比较器输入覆盖
     * @param blockState
     * @param worldIn
     * @param pos
     * @return
     */
    @Override
    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos) {
        return Container.calcRedstone(worldIn.getTileEntity(pos));
    }

    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(BlockLoader.blockWindmill);
    }

    /**
     * 调用的渲染函数的类型。3用于标准块体模型，2用于TESR，1用于液体，-1不渲染
     *
     * @param state
     * @return
     */
    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return EnumBlockRenderType.MODEL;
    }

    /**
     * 将给定的元数据转换为此块的BlockState
     *
     * @param meta
     * @return
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

}
