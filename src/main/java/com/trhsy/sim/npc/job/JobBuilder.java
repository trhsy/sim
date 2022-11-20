package com.trhsy.sim.npc.job;

import com.google.common.collect.UnmodifiableIterator;
import com.trhsy.sim.ModSim;
import com.trhsy.sim.block.BlockConstructorBox;
import com.trhsy.sim.entity.EntityConBox;
import com.trhsy.sim.loader.*;
import com.trhsy.sim.network.client.PacketSendBuildingRequirements;
import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.V3;
import com.trhsy.sim.npc.build.Building;
import com.trhsy.sim.npc.build.BuildingBlueprint;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockFence;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBed;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @ClassName JobBuilder
 * @Description todo 建筑工作
 * @Author TRHSY
 * @Date 2022/10/2014:25
 **/
public class JobBuilder extends Job {
    /**
     * 建筑蓝图
     **/
    public BuildingBlueprint blueprint;
    /**
     * 悬浮的控制箱
     **/
    public EntityConBox conBox;
    /**
     * 放置的块
     **/
    List<V3> placedBlocks = new CopyOnWriteArrayList<>();
    /**
     * 缺失的方块
     **/
    Block missingBlock = null;
    /**
     * 开始位置
     **/
    BlockPos startPos;
    /**
     * 建造位置
     **/
    BlockPos constructorPos;
    /**
     * 控制箱位置
     **/
    BlockPos controllerPos;
    /**
     * 活动区域
     **/
    BlockPos livingPos;
    /**
     * 建筑箱
     **/
    public BlockConstructorBox constructorBlock = null;
    /**
     * 方向
     */
    public int direction;
    //默认方向
    public int defaultDirection;
    int x = 0;
    int y = 0;
    int z = 0;
    /**
     * 块的编号
     **/
    public int blockNumber = 0;
    /**
     * 自上一个块位置的时间
     **/
    private transient long timeSinceLastBlockPlace = 0L;
    /**
     * 上次摆动手臂的时间
     **/
    private transient long timeSwingArm = 0L;
    /**
     * 已重新指派员工
     **/
    boolean hasReassignedEmployee;
    /**
     * 缺失检查
     **/
    int missingCheck = 0;

    /**
     * @return npc 蓝图 块
     * @Author fan
     * @Description //TODO 初始化工作
     * @Date 22:12 2022/10/30
     * @Param [folk, bp, pos, direction, world]
     **/
    public JobBuilder(NpcData folk, BuildingBlueprint bp, BlockPos pos, int direction, World world) {
        super(folk, pos, world);
        folk.holding = new ItemStack(Blocks.COBBLESTONE);
        //建筑工
        this.jobName = I18n.format("container.sim.Vocation1");
        this.startPos = pos;
        this.constructorPos = pos;
        this.direction = direction;
        this.blueprint = bp;
        int i = 0;
        boolean hasControlBox = false;
        //建筑蓝图的所有方块
        IBlockState[] var8 = this.blueprint.structure;
        for (int j = 0; j < var8.length; j++) {
            IBlockState st = var8[j];
            if (st != null) {
                Block block = st.getBlock();
                if (block == BlockLoader.blockControlBox) {
                    hasControlBox = true;
                    break;
                }
                ++i;
            }
        }

        if (!hasControlBox) {
            //如果没有找到控制箱，则第一个方块就是建筑箱
            this.blueprint.structure[0] = BlockLoader.blockControlBox.getDefaultState();
        }
        if (folk.entity != null) {
            IBlockState s = folk.entity.worldObj.getBlockState(pos);
            if (s != null) {
                Block block = s.getBlock();
                if (block != null) {
                    //建筑箱
                    this.constructorBlock = (BlockConstructorBox) block;
                    this.constructorBlock.employee = folk;
                }
            }
        }

        //建筑方向
        if (direction == 0) {
            this.startPos = this.startPos.add(0, 0, -1);
        } else if (direction == 1) {
            this.startPos = this.startPos.add(1, 0, 0);
        } else if (direction == 2) {
            this.startPos = this.startPos.add(0, 0, 1);
        } else {
            this.startPos = this.startPos.add(-1, 0, 0);
        }

        this.createConBox();
    }

    /**
     * @return
     * @Author fan
     * @Description //TODO 工作的npc 位置，方向，世界
     * @Date 10:08 2022/11/4
     * @Param [folk, pos, direction, world]
     **/
    public JobBuilder(NpcData folk, BlockPos pos, int direction, World world) {
        super(folk, pos, world);
        folk.holding = new ItemStack(Blocks.COBBLESTONE);
        this.jobName = I18n.format("container.sim.Vocation1");
        this.constructorPos = pos;
        this.startPos = pos;
        this.direction = direction;
        if (direction == 0) {
            this.startPos = this.startPos.add(0, 0, -1);
        } else if (direction == 1) {
            this.startPos = this.startPos.add(1, 0, 0);
        } else if (direction == 2) {
            this.startPos = this.startPos.add(0, 0, 1);
        } else {
            this.startPos = this.startPos.add(-1, 0, 0);
        }

//        this.createConBox();
    }

    public JobBuilder(NpcData folk, V3 pos, int direction, World world) {
        super(folk, pos, world);
        folk.holding = new ItemStack(Blocks.COBBLESTONE);
        this.jobName = I18n.format("container.sim.Vocation1");
        this.constructorPos = pos.toBlockPos();
        this.startPos = pos.toBlockPos();
        this.direction = direction;
        BlockConstructorBox cons = (BlockConstructorBox) folk.entity.worldObj.getBlockState(pos.toBlockPos()).getBlock();
        cons.employee = folk;
        if (direction == 0) {
            this.startPos = this.startPos.add(0, 0, -1);
        } else if (direction == 1) {
            this.startPos = this.startPos.add(1, 0, 0);
        } else if (direction == 2) {
            this.startPos = this.startPos.add(0, 0, 1);
        } else {
            this.startPos = this.startPos.add(-1, 0, 0);
        }

//        this.createConBox();
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 更新
     * @Date 10:13 2022/11/4
     * @Param []
     **/
    public void onUpdate() {
        super.onUpdate();
        if (this.folk != null) {
            if (this.folk.entity != null) {
                if (this.folk.entity.worldObj != null) {
                    if (!this.folk.entity.worldObj.isRemote) {
                        //NPC数据为空，并且没有指派元
                        if (this.folk.entity != null && !this.hasReassignedEmployee) {
                            //建造位置为空
                            if (this.folk.entity.worldObj.getBlockState(this.constructorPos) == null) {
                                return;
                            }
                            //获取建筑箱的
                            BlockConstructorBox cons = (BlockConstructorBox) this.folk.entity.worldObj.getBlockState(this.constructorPos).getBlock();
                            //当前建筑箱的工作人员是
                            cons.employee = this.folk;
                            //已经
                            this.hasReassignedEmployee = true;
                            //如果允许 NPC 说话
                            if (ConfigLoader.configFolkTalking) {
                                World world = FMLClientHandler.instance().getServer().getEntityWorld();
                                //播放 天亮了鸡叫
                                SoundEvent soundEvent = null;
                                //判断性别，发出不一样的声音
                                if (this.folk.gender == 0) {
                                    soundEvent = new SoundEvent(new ResourceLocation(ModSim.MODID + ":im_read_m"));
                                } else {
                                    soundEvent = new SoundEvent(new ResourceLocation(ModSim.MODID + ":im_read_y"));
                                }
                                for (int i = 0; i < world.playerEntities.size(); i++) {
                                    EntityPlayer entityPlayer = world.playerEntities.get(i);
                                    BlockPos pos = new BlockPos(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ);
                                    world.playSound(null, pos, soundEvent, SoundCategory.PLAYERS, 1, 1);
                                }
                            }
                        }
                        //建筑蓝图为空
                        if (this.blueprint == null) {
                            //等待蓝图
                            this.folk.setStatus(I18n.format("container.sim.job.builder_Awaiting_blueprint"));
                        } else {
                            //随机
//                            new Random();
                            //当前时间
                            Long now = System.currentTimeMillis();
                            //游戏模式是正常模式
                            if (ModSimLoader.states.gameModeNumber == 0) {
                                if ((float) (now - this.timeSinceLastBlockPlace) > 1000.0F - 100.0F * this.folk.skillBuilding) {
                                    ////上次时间为当前时间
                                    this.timeSinceLastBlockPlace = now;
                                    //放置方块
                                    this.placeBlock();
                                    //发送建筑蓝图
                                    NetWorkLoader.net.sendToAll(new PacketSendBuildingRequirements(this.blueprint, this));

                                }
                            } else {
                                //上次时间为当前时间
                                this.timeSinceLastBlockPlace = now;
                                //不是客户端
                                if (!this.folk.entity.worldObj.isRemote) {
                                    //直接放置方块
                                    this.placeBlock();
                                }
                            }

                        }
                    }
                }
            }
        }
    }

    public void onMinute() {
        if (this.missingCheck < 3) {
            ++this.missingCheck;
        } else {
            //谁在建造
            String s1 = I18n.format("container.sim.job.builder_constructor_started_who's");
            if (this.missingBlock != null && this.missingBlock != Blocks.AIR) {

                String s2 = I18n.format("container.sim.job.builder_constructor_started_more");
                //谁在建“”需要更多的“”
                ModSimLoader.sendChat(this.folk.getName() + s1 + "(" + this.blueprint.name + ") " + s2 + this.missingBlock.getLocalizedName());
            }

            if (ModSimLoader.states.credits < 0.02F) {
                //没有足够的资金支付给
                ModSimLoader.sendChat(I18n.format("container.sim.JobBuilder1") + this.folk.getName() + s1 + "( " + this.blueprint.name + ")!");
            }

            this.missingCheck = 0;
        }
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 放置方块
     * @Date 22:07 2022/10/30
     * @Param []
     **/
    public void placeBlock() {
        try {
            //正常的块
            boolean normalBlock = true;
            //已放置
            boolean hasPlaced = false;
            //方块编号大于
            if (this.blockNumber >= this.blueprint.structure.length) {
                this.folk.fire();
                if (this.constructorBlock != null) {
                    this.constructorBlock.employee = null;
                }
                this.createBuilding();
                ModSimLoader.log.info("从第一次功能检查激发");
                return;
            }

            BlockPos newBP = null;
            IBlockState st = null;
            //方向
            if (this.direction == 0) {
                newBP = new BlockPos(this.startPos.getX() + this.x, this.startPos.getY() + this.y, this.startPos.getZ() - this.z);
                st = this.blueprint.structure[this.blockNumber];
            } else if (this.direction == 1) {
                newBP = new BlockPos(this.startPos.getX() + this.z, this.startPos.getY() + this.y, this.startPos.getZ() + this.x);
                st = this.blueprint.structure[this.blockNumber];
            } else if (this.direction == 2) {
                newBP = new BlockPos(this.startPos.getX() - this.x, this.startPos.getY() + this.y, this.startPos.getZ() + this.z);
                st = this.blueprint.structure[this.blockNumber];
            } else {
                newBP = new BlockPos(this.startPos.getX() - this.z, this.startPos.getY() + this.y, this.startPos.getZ() - this.x);
                st = this.blueprint.structure[this.blockNumber];
            }
            Block fs_block = this.folk.entity.worldObj.getBlockState(newBP).getBlock();
            Block fs_st_block = st.getBlock();
            //控制箱
            if (fs_st_block == BlockLoader.blockControlBox) {
                this.controllerPos = newBP;
                //生活方块地毯
            } else if (fs_st_block == BlockLoader.blockLiving) {
                this.livingPos = newBP;
//                st = Blocks.AIR.getDefaultState();
            }
            if (fs_block != fs_st_block) {

                if (ModSimLoader.states.credits < 0.02F) {
                    //没有钱付给我！
                    this.folk.setStatus(I18n.format("container.sim.JobBuilder2"));
                    return;
                }
                Block newBlock = this.folk.entity.worldObj.getBlockState(newBP.down()).getBlock();
                //门
                if (fs_st_block instanceof BlockDoor && newBlock != fs_st_block) {
                    normalBlock = false;
                    //放置门
                    ItemDoor.placeDoor(this.folk.entity.worldObj, newBP, EnumFacing.NORTH, fs_st_block, false);
                    ModSimLoader.addMoney(-0.02F);
                }
                //床
                if (fs_st_block == Blocks.BED) {
                    String name = fs_block.getUnlocalizedName();
                    System.out.println(name);
                    if (name.contains("bed")) {
                        //                    normalBlock = false;//床不是普通的块
                    }
                }
                //控制箱
                if (fs_st_block == BlockLoader.blockControlBox) {
                    this.controllerPos = newBP;
                    //生活方块地毯
                } else if (fs_st_block == BlockLoader.blockLiving) {
                    this.livingPos = newBP;
//                st = Blocks.AIR.getDefaultState();
                }
                //是否是普通的块
                if (normalBlock) {
                    //模组模式是正常模式
                    if (ModSimLoader.states.gameModeNumber == 0) {
                        //是不是蓝图的块
                        boolean hasBlock = false;
                        //是不是必须的方块
                        if (!this.blueprint.isRequiredBlock(fs_st_block)) {
                            //放置方块
                            this.folk.setStatus(I18n.format("container.sim.JobBuilder3"));
                            //已放置
                            hasBlock = true;
                        } else {
                            Block b = fs_st_block;
                            newBlock = this.folk.entity.worldObj.getBlockState(newBP).getBlock();
                            //是否是同一个块，是否已放置
                            if (this.isSameBlock(newBlock, fs_st_block)) {
                                hasBlock = true;
                                hasPlaced = true;
                            } else {
                                //获取周围箱子
                                List<IInventory> inventoriesFindClosest = this.inventoriesFindClosest(this.workPlace, 5);
                                //循环箱子
                                for (IInventory inv : inventoriesFindClosest) {
                                    //循环箱子库存
                                    for (int i = 0; i < inv.getSizeInventory(); i++) {
                                        ItemStack itemStack = inv.getStackInSlot(i);
                                        //拿走当前需要的块
                                        if (itemStack != null && itemStack.getItem() == Item.getItemFromBlock(b)) {
                                            hasBlock = true;
                                            inv.decrStackSize(i, 1);
                                            break;
                                        }
                                    }
                                    if (hasBlock) {
                                        break;
                                    }
                                }

                            }
                        }

                        if (!hasBlock) {
                            //等待材料
                            this.folk.setStatus(I18n.format("container.sim.JobBuilder4"));
                            this.missingBlock = fs_st_block;
                            return;
                        }
                        //重置缺少的块
                        this.missingBlock = null;
                        Long now = System.currentTimeMillis();
                        if (now - this.timeSwingArm > 2000) {
                            this.timeSwingArm = now;
                            this.folk.entity.swingArm(EnumHand.MAIN_HAND);
                        }
                        //放置方块
                        this.folk.setStatus(I18n.format("container.sim.JobBuilder3"));
                    }
                    if (!hasPlaced) {
                        if (st.getProperties().size() == 0) {
                            //放置方块
                            this.folk.setStatus(I18n.format("container.sim.JobBuilder3"));

                            this.folk.entity.worldObj.setBlockState(newBP, st);
                            ModSimLoader.addMoney(-0.02F);
                            //栅栏
                        } else if (fs_st_block instanceof BlockFence) {
                            //放置方块
                            this.folk.setStatus(I18n.format("container.sim.JobBuilder3"));
                            this.folk.entity.worldObj.setBlockState(newBP, st);
                            ModSimLoader.addMoney(-0.02F);
                        } else {
                            EnumFacing facing = null;
                            IProperty prop = null;
                            Collection validFacings = null;
                            do {
                                for (IProperty p : st.getProperties().keySet()) {
                                    prop = p;
                                    if (prop.getName().equals("facing")) {
                                        //放置方块
                                        this.folk.setStatus(I18n.format("container.sim.JobBuilder3"));
                                        ModSimLoader.log.info("================================================================");
                                        ModSimLoader.log.info(this.direction + " : " + this.blueprint.direction);
                                        ModSimLoader.log.info("得到方块" + fs_st_block.getUnlocalizedName() + "面向");
                                        facing = (EnumFacing) st.getValue(prop);
                                        validFacings = prop.getAllowedValues();
                                        ModSimLoader.log.info("块当前面向" + facing.toString());
                                        break;
                                    }
                                    //放置方块
                                    this.folk.setStatus(I18n.format("container.sim.JobBuilder3"));
                                    this.folk.entity.worldObj.setBlockState(newBP, st);
                                    ModSimLoader.addMoney(-0.02F);
                                }
                            } while (validFacings != null && validFacings.size() < 4);
                            //不朝上 不朝下
                            if (facing != null && facing != EnumFacing.UP && facing != EnumFacing.DOWN) {
                                IBlockState newState = st.withProperty(prop, facing.rotateY());
                                if (this.direction == 0 && this.blueprint.direction == 3 || this.direction == 1 && this.blueprint.direction == 0 || this.direction == 2 && this.blueprint.direction == 1 || this.direction == 3 && this.blueprint.direction == 2) {
                                    newState = st.withProperty(prop, facing.rotateY());
                                    st = st.withRotation(Rotation.CLOCKWISE_90);
                                    if (!this.isSameBlock(this.folk.entity.worldObj.getBlockState(newBP).getBlock(), fs_st_block)) {
                                        this.folk.entity.worldObj.setBlockState(newBP, st);
                                        ModSimLoader.addMoney(-0.02F);
                                    }
                                    ModSimLoader.log.info("方块应该面向 " + facing.rotateY().toString() + " (顺时针方向的)");
                                    ModSimLoader.log.info("方块实际面向 " + ((EnumFacing) newState.getValue(prop)).toString());
                                } else if ((this.direction != 0 || this.blueprint.direction != 2) && (this.direction != 1 || this.blueprint.direction != 3) && (this.direction != 2 || this.blueprint.direction != 0) && (this.direction != 3 || this.blueprint.direction != 1)) {
                                    if (this.direction == 0 && this.blueprint.direction == 1 || this.direction == 1 && this.blueprint.direction == 2 || this.direction == 2 && this.blueprint.direction == 3 || this.direction == 3 && this.blueprint.direction == 0) {
                                        newState = st.withProperty(prop, facing.rotateYCCW());
                                        st = st.withRotation(Rotation.COUNTERCLOCKWISE_90);
                                        if (!this.isSameBlock(this.folk.entity.worldObj.getBlockState(newBP).getBlock(), fs_st_block)) {
                                            this.folk.entity.worldObj.setBlockState(newBP, st);
                                            ModSimLoader.addMoney(-0.02F);
                                        }
                                        ModSimLoader.log.info("方块应该面向 " + facing.rotateYCCW().toString() + " (逆时针方向)");
                                        ModSimLoader.log.info("方块实际面向 " + ((EnumFacing) newState.getValue(prop)).toString());
                                    } else if (this.direction == this.blueprint.direction) {
                                        if (!this.isSameBlock(this.folk.entity.worldObj.getBlockState(newBP).getBlock(), fs_st_block)) {
                                            this.folk.entity.worldObj.setBlockState(newBP, st);
                                            ModSimLoader.addMoney(-0.02F);
                                        }
                                        ModSimLoader.log.info("方块应该面向 " + facing.toString() + " (无旋转)");
                                    } else {
                                        ModSimLoader.log.info("出了问题(无旋转)");
                                        ModSimLoader.log.error(this.direction + ":" + this.blueprint.direction);
                                    }
                                } else {
                                    st = st.withRotation(Rotation.CLOCKWISE_180);
                                    if (!this.isSameBlock(this.folk.entity.worldObj.getBlockState(newBP).getBlock(), fs_st_block)) {
                                        this.folk.entity.worldObj.setBlockState(newBP, st);
                                        ModSimLoader.addMoney(-0.02F);
                                    }
                                    ModSimLoader.log.info("方块应该面向 " + facing.rotateY().rotateY().toString() + " (快速翻转)");
                                    ModSimLoader.log.info("方块实际面向 " + ((EnumFacing) st.getValue(prop)).toString());
                                }
                            } else {
                                this.folk.entity.worldObj.setBlockState(newBP, st);
                            }

                        }
                    }
                }
            } else {
                Long now = System.currentTimeMillis();
                if (now - this.timeSwingArm > 3000) {
                    this.timeSwingArm = now;
                    this.folk.entity.swingArm(EnumHand.MAIN_HAND);
                        //建造的音效
                        SoundEvent soundEvent = new SoundEvent(new ResourceLocation(ModSim.MODID + ":construction"));
                        for (int i = 0; i < this.jobWorld.playerEntities.size(); i++) {
                            EntityPlayer entityPlayer = this.jobWorld.playerEntities.get(i);
                            BlockPos pos = new BlockPos(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ);
                        this.jobWorld.playSound(null, pos, soundEvent, SoundCategory.PLAYERS, 1, 1);
                        }
                }
                //放置方块
                this.folk.setStatus(I18n.format("container.sim.JobBuilder3"));
            }
            //在客户端生成粒子
            if (this.jobWorld.isRemote) {
                Random rand = new Random();
                for (int i = 0; i < 20; ++i) {
                    double d0 = rand.nextGaussian() * 0.02D;
                    double d1 = rand.nextGaussian() * 0.02D;
                    double d2 = rand.nextGaussian() * 0.02D;
                    double d3 = 10.0D;
                    this.jobWorld.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, newBP.getX() + (double) (rand.nextFloat() * 1 * 2.0F) - (double) 1 - d0 * d3, newBP.getY() + (double) (rand.nextFloat() * 1) - d1 * d3, newBP.getZ() + (double) (rand.nextFloat() * 1 * 2.0F) - (double) 1 - d2 * d3, d0, d1, d2, new int[0]);
                }
            }
            this.placedBlocks.add(V3.fromBlockPos(newBP));
            int b4 = (int) Math.floor(this.folk.skillBuilding);
            //建筑等级
            if (this.folk.skillBuilding < 10.0F) {
                NpcData var10000 = this.folk;
                var10000.skillBuilding = (float) (var10000.skillBuilding + 0.001D / b4);
            }

            int aft = (int) Math.floor(this.folk.skillBuilding);
            if (b4 != aft) {
                ModSimLoader.sendChat(this.folk.getName() + I18n.format("container.sim.job.builder_constructor_levelled") + aft);
            }

            ++this.x;
            if (this.x > this.blueprint.length - 1) {
                this.x = 0;
                ++this.z;
            }

            if (this.z > this.blueprint.width - 1) {
                this.z = 0;
                ++this.y;
            }

            ++this.blockNumber;
            if (this.blockNumber >= this.blueprint.structure.length) {
                this.folk.fire();
                if (this.constructorBlock != null) {
                    this.constructorBlock.employee = null;
                }
                ModSimLoader.log.info("从中间功能检查中激发");
                this.createBuilding();
                return;
            }

            try {
                /*BlockPos tempBP;
                Block newNumberBP = this.blueprint.structure[this.blockNumber].getBlock();
                if (newNumberBP.getUnlocalizedName().contentEquals("tile.air")) {
                    ++this.blockNumber;
                    if (this.direction == 0) {
                        tempBP = new BlockPos(this.startPos.getX() + this.x, this.startPos.getY() + this.y, this.startPos.getZ() - this.z);
                    } else if (this.direction == 1) {
                        tempBP = new BlockPos(this.startPos.getX() + this.z, this.startPos.getY() + this.y, this.startPos.getZ() + this.x);
                    } else if (this.direction == 2) {
                        tempBP = new BlockPos(this.startPos.getX() - this.x, this.startPos.getY() + this.y, this.startPos.getZ() + this.z);
                    } else {
                        tempBP = new BlockPos(this.startPos.getX() - this.z, this.startPos.getY() + this.y, this.startPos.getZ() - this.x);
                    }

                    if (this.jobWorld.getBlockState(tempBP).getBlock() != Blocks.AIR) {
                        this.jobWorld.setBlockToAir(tempBP);
                        return;
                    }

                    ++this.x;
                    if (this.x > this.blueprint.length - 1) {
                        this.x = 0;
                        ++this.z;
                    }

                    if (this.z > this.blueprint.width - 1) {
                        this.z = 0;
                        ++this.y;
                    }
                }
                if (this.direction == 0) {
                    tempBP = new BlockPos(this.startPos.getX() + this.x, this.startPos.getY() + this.y, this.startPos.getZ() - this.z);
                    newNumberBP=this.blueprint.structure[this.blockNumber].getBlock();
                } else if (this.direction == 1) {
                    tempBP = new BlockPos(this.startPos.getX() + this.z, this.startPos.getY() + this.y, this.startPos.getZ() + this.x);
                    newNumberBP=this.blueprint.structure[this.blockNumber].getBlock();
                } else if (this.direction == 2) {
                    tempBP = new BlockPos(this.startPos.getX() - this.x, this.startPos.getY() + this.y, this.startPos.getZ() + this.z);
                    newNumberBP=this.blueprint.structure[this.blockNumber].getBlock();
                } else {
                    tempBP = new BlockPos(this.startPos.getX() - this.z, this.startPos.getY() + this.y, this.startPos.getZ() - this.x);
                    newNumberBP=this.blueprint.structure[this.blockNumber].getBlock();
                }
                Block newTempBP = this.folk.entity.worldObj.getBlockState(tempBP).getBlock();//已放置的
                String s = newNumberBP.getUnlocalizedName();
                boolean flag1 = newTempBP.getUnlocalizedName().contentEquals(s);
                boolean flag2 = s != "tile.air";
                boolean flag3 = !s.contentEquals("tile.air");
                while (flag1&&flag2&&flag3) {
                    ++this.x;
                    if (this.x > this.blueprint.length - 1) {
                        this.x = 0;
                        ++this.z;
                    }

                    if (this.z > this.blueprint.width - 1) {
                        this.z = 0;
                        ++this.y;
                    }

                    ++this.blockNumber;
                    this.placedBlocks.add(V3.fromBlockPos(tempBP));
                    if (this.blockNumber >= this.blueprint.structure.length) {
                        this.folk.fire();
                        this.constructorBlock.employee = null;
                        ModSimLoader.log.info("从中间功能检查激发");
                        this.createBuilding();
                        return;
                    }
                    if (this.direction == 0) {
                        tempBP = new BlockPos(this.startPos.getX() + this.x, this.startPos.getY() + this.y, this.startPos.getZ() - this.z);
                        newTempBP = this.folk.entity.worldObj.getBlockState(tempBP).getBlock();
                        newNumberBP = this.blueprint.structure[this.blockNumber].getBlock();
                    } else if (this.direction == 1) {
                        tempBP = new BlockPos(this.startPos.getX() + this.z, this.startPos.getY() + this.y, this.startPos.getZ() + this.x);
                        newTempBP = this.folk.entity.worldObj.getBlockState(tempBP).getBlock();
                        newNumberBP = this.blueprint.structure[this.blockNumber].getBlock();
                    } else if (this.direction == 2) {
                        tempBP = new BlockPos(this.startPos.getX() - this.x, this.startPos.getY() + this.y, this.startPos.getZ() + this.z);
                        newTempBP = this.folk.entity.worldObj.getBlockState(tempBP).getBlock();
                        newNumberBP = this.blueprint.structure[this.blockNumber].getBlock();
                    } else {
                        tempBP = new BlockPos(this.startPos.getX() - this.z, this.startPos.getY() + this.y, this.startPos.getZ() - this.x);
                        newTempBP = this.folk.entity.worldObj.getBlockState(tempBP).getBlock();
                        newNumberBP = this.blueprint.structure[this.blockNumber].getBlock();
                    }


                    if (newTempBP == BlockLoader.blockControlBox) {
                        this.controllerPos = tempBP;
                        //生活方块地毯
                    } else if (newTempBP == BlockLoader.blockLiving) {
                        this.livingPos = tempBP;
                    }
                    s = newNumberBP.getUnlocalizedName();
                    flag1 = newTempBP.getUnlocalizedName().contentEquals(s);
                    flag2 = s != "tile.air";
                    flag3 = !s.contentEquals("tile.air");
                }*/
            } catch (Exception var11) {
                ModSimLoader.log.info("建造建筑出错误了" + var11.getMessage());
                this.folk.fire();
                return;
            }
        } catch (Exception var12) {
            var12.printStackTrace();
            StackTraceElement element = var12.getStackTrace()[0];
            ModSimLoader.log.error("因错误而解雇的员工：" + var12.getMessage() + "行数：" + element.getLineNumber());
            this.folk.fire();
            if (this.constructorBlock != null) {
                this.constructorBlock.employee = null;
            }
        }

    }

    /**
     * @return boolean
     * @Author fan
     * @Description //TODO 是否是同一个块
     * @Date 16:37 2022/11/1
     * @Param [b1, b2]
     **/
    boolean isSameBlock(Block b1, Block b2) {
        if (b1 == b2) {
            return true;
        } else {
            //泥土 玻璃
            return b1 == Blocks.DIRT && b2 == Blocks.GRASS || b1 == Blocks.GRASS && b2 == Blocks.DIRT;
        }
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 当建筑完成时创建建筑物
     * @Date 16:31 2022/11/1
     * @Param []
     **/
    void createBuilding() {
        if (this.livingPos == null) {
            this.livingPos = this.controllerPos;
        }
        V3 v3 = V3.fromBlockPos(this.controllerPos);
        for (Building building : ModSimLoader.buildings) {
            if (building.controlXYZ.equals(v3)) {
                building.removeBuilding(building.ID);
            }
        }
        //创建建筑物
        Building build = new Building(this.blueprint.name, 10.0F, V3.fromBlockPos(this.controllerPos), V3.fromBlockPos(this.livingPos));
        //建筑物类型
        build.buildingType = this.blueprint.buildingType;
        //建筑物的结构
        build.structure = new CopyOnWriteArrayList<>(this.placedBlocks);
        //工作类型
        build.jobType = this.blueprint.jobType;
        //建筑作者
        build.author = this.blueprint.author;
        //租金
        build.rent = (float) this.blueprint.structure.length * 0.01F;
        ModSimLoader.buildings.add(build);
        //保存建筑
        build.saveBuilding();
        //已完成建设 【】
        String text = this.folk.getName() + I18n.format("container.sim.job.builder_constructor_completed") + this.blueprint.name;
        ModSimLoader.sendChat(text);
        //播放声音
        SoundEvent soundEvent = new SoundEvent(new ResourceLocation(ModSim.MODID + ":cashshort"));
        for (int i = 0; i < this.jobWorld.playerEntities.size(); i++) {
            EntityPlayer entityPlayer = this.jobWorld.playerEntities.get(i);
            BlockPos pos = new BlockPos(entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ);
            this.jobWorld.playSound(null, pos, soundEvent, SoundCategory.PLAYERS, 1, 1);
        }
    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 创建悬浮的控制箱
     * @Date 16:25 2022/11/1
     * @Param []
     **/
    void createConBox() {
        this.conBox = new EntityConBox(this.jobWorld, this);
        this.conBox.folk = this.folk;
        this.conBox.job = this;
        this.conBox.setLocationAndAngles(this.workPlace.x + 2.0D, this.workPlace.y, this.workPlace.z, 0.0F, 0.0F);
        if (!this.jobWorld.isRemote) {
            this.jobWorld.spawnEntityInWorld(this.conBox);
        }

    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 抵达
     * @Date 16:31 2022/11/1
     * @Param []
     **/
    public void onArrive() {
    }

    public String toString() {
        return I18n.format("container.sim.Vocation1");
    }
}
