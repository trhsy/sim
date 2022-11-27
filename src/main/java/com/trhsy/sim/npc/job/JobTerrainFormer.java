package com.trhsy.sim.npc.job;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.block.BlockConstructorBox;
import com.trhsy.sim.entity.EntityConBox;
import com.trhsy.sim.loader.ConfigLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.loader.NetWorkLoader;
import com.trhsy.sim.network.client.PacketSendBuildingRequirements;
import com.trhsy.sim.network.client.PacketSendTerrainTypeRequitrements;
import com.trhsy.sim.network.server.PacketSendTerrainType;
import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.V3;
import com.trhsy.sim.npc.build.TerrainType;
import com.trhsy.sim.util.GameStates;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @ClassName JobTerrainFormer
 * @Description todo 地形构造师
 * @Author TRHSY
 * @Date 2022/11/1622:23
 **/
public class JobTerrainFormer extends Job {
    /**
     * @Author fan
     * @Description //TODO 规划类型
     * @Date 21:03 2022/11/23
     * @Param
     * @return
     **/
    private TerrainType terrainType;
    /**
     * 开始位置
     **/
    private BlockPos startPos;
    /**
     * 悬浮的控制箱
     **/
    public EntityConBox conBox;
    /**
     * 建筑箱
     **/
    private BlockConstructorBox constructorBlock = null;
    /**
     * 自上一个块位置的时间
     **/
    private long timeSinceLastBlockPlace = 0L;
    /**
     * 缺失的方块
     **/
    private Block missingBlock = null;
    /**
     * 建造位置
     **/
    private BlockPos constructorPos;
    private List<V3> closestBlocks = new CopyOnWriteArrayList();
    /**
     * 缺失检查
     **/
    private int missingCheck = 0;
    private int totalBlockCount = 0;
    /**
     * 已重新指派员工
     **/
    boolean hasReassignedEmployee;

    public JobTerrainFormer(NpcData folk, TerrainType terrainType, BlockPos pos, World world) {
        super(folk, pos, world);
        this.startPos = pos;
        this.constructorPos = pos;
        this.terrainType = terrainType;
        //建筑工
        this.jobName = I18n.format("container.sim.Vocation16");
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
        this.createConBox();
    }

    public JobTerrainFormer(NpcData folk, V3 pos, World world) {
        super(folk, pos, world);
        this.constructorPos = pos.toBlockPos();
        //建筑箱
        this.constructorBlock = (BlockConstructorBox) folk.entity.worldObj.getBlockState(pos.toBlockPos()).getBlock();
        //建筑工
        this.jobName = I18n.format("container.sim.Vocation16");
        this.constructorBlock.employee = folk;
        this.createConBox();
    }

    public void onUpdate() {
        super.onUpdate();
        if (this.folk != null) {
            if (this.folk.entity != null) {
                if (this.folk.entity.worldObj != null) {
                    if (!this.folk.entity.worldObj.isRemote) {
                        //NPC数据为空，并且没有指派员工
                        if (this.folk.entity != null && !this.hasReassignedEmployee) {
                            //建造位置为空
                            if (this.folk.entity.worldObj.getBlockState(this.constructorPos) == null) {
                                return;
                            }
                            //获取建筑箱的
                            BlockConstructorBox cons = (BlockConstructorBox) this.folk.entity.worldObj.getBlockState(this.constructorPos).getBlock();
                            //当前建筑箱的工作人员是
                            cons.employee = this.folk;
                            //已经指派
                            this.hasReassignedEmployee = true;
                            //如果允许 NPC 说话
                            if (ConfigLoader.configFolkTalking) {
                                World world = FMLClientHandler.instance().getServer().getEntityWorld();
                                //播放 我准备好了
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
                            //等待规划类型
                            if(this.terrainType==null){
                                //等待蓝图
                                this.folk.setStatus(I18n.format("container.sim.job.builder_Awaiting_terrainType"));
                            }else{
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
                                        NetWorkLoader.net.sendToAll(new PacketSendTerrainTypeRequitrements(this.terrainType, this));

                                    }
                                }else{
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


                        //当前时间
                        Long now = System.currentTimeMillis();
                        //游戏模式是正常模式
                        if (ModSimLoader.states.gameModeNumber == 0) {
                            if ((float) (now - this.timeSinceLastBlockPlace) > 1000.0F - 100.0F * this.folk.skillBuilding) {
                                //上次时间为当前时间
                                this.timeSinceLastBlockPlace = now;
                                placeBlock();
                            }

                        } else {
                            //上次时间为当前时间
                            this.timeSinceLastBlockPlace = now;
                        }
                    }
                }
            }
        }
    }

    public void placeBlock() {
        try {
            //重置缺少的块
            this.missingBlock = null;
            Boolean fsMissBlock=true;
            if (ModSimLoader.states.credits < 0.02F) {
                //没有钱付给我！
                this.folk.setStatus(I18n.format("container.sim.JobBuilder2"));
                return;
            }
            CopyOnWriteArrayList blockIDs;
            switch (this.terrainType.terrainType){
                case "1":
                    //填海
                    blockIDs = new CopyOnWriteArrayList();
                    blockIDs.add(Blocks.WATER);
                    blockIDs.add(Blocks.WATER);
                    this.closestBlocks = null;
                    this.setClosestBlocksOfType(constructorPos, blockIDs, 30, false, true, false);
                    this.totalBlockCount = this.closestBlocks.size();
                    if(this.totalBlockCount==0){
                        //没有什么要地球化的！
                        this.folk.status = I18n.format("container.sim.job.terra.farmer.Nothing");
                        //这里没有任何东西能以这种方式被规划
                        ModSimLoader.sendChat(I18n.format("container.sim.job.terra.farmer.terraformed"));
                        //解雇
                        this.folk.fire();
                        return;
                    }
                    //开始地形规划
                    this.folk.status = I18n.format("container.sim.job.terra.farmer.process");
                    //是否是创造模式
                    if (ModSimLoader.states.gameModeNumber!= 1) {
                        //获取周围箱子
                        List<IInventory> inventoriesFindClosest = this.inventoriesFindClosest(this.workPlace, 5);
                        //循环箱子
                        for (IInventory inv : inventoriesFindClosest) {
                            //循环箱子库存
                            for (int i = 0; i < inv.getSizeInventory(); i++) {
                                ItemStack itemStack = inv.getStackInSlot(i);
                                //拿走当前需要的块 泥土
                                if (itemStack != null && itemStack.getItem() == Item.getItemFromBlock(Blocks.DIRT)) {
                                    inv.decrStackSize(i, 1);
                                    fsMissBlock=false;
                                    break;
                                }
                            }
                        }
                        if(fsMissBlock){
                            //我需要更多的泥土！
                            this.folk.status = I18n.format("container.sim.job.terra.farmer.dirt");
                            this.missingBlock=Blocks.DIRT;
                            return;
                        }
                        //计算规划的百分比
                        Double x = (double) this.totalBlockCount;
                        Double y = (double) this.closestBlocks.size();
                        Double percent = (x - y) / x;
                        percent = percent * 100;
                        //环境改造 10% 完成
                        this.folk.status = I18n.format("container.sim.job.terra.farmer.Terraforming") + ", " + percent + " % " + I18n.format("container.sim.job.terra.farmer.complete");
                        V3 v = (V3) this.closestBlocks.get(0);
                        BlockPos blockPos2 = new BlockPos(v.x, v.y, v.z);
                        this.jobWorld.setBlockState(blockPos2, Blocks.DIRT.getDefaultState(), 3);
                        if (ModSimLoader.states.gameModeNumber!= 1) {
                            GameStates var10000 = ModSimLoader.states;
                            ModSimLoader.states.credits = (float) ((double) var10000.credits - 0.009D);
                        }
                    }
                    break;
                case "2":
                    //绿化
                    break;
                case "3":
                    //除草
                    break;
                case "4":
                    //平整化
                    break;
                case "5":
                    //单层泥土
                    break;
                case "6":
                    //冰川
                    break;
                case "7":
                    //湿润
                    break;
                case "8":
                    //炎热
                    break;
                case "9":
                    //除雪
                    break;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * @Author fan
     * @Description //TODO
     * @Date 19:54 2022/11/27 起始位置，方块 距离限制，向上扫描，向下扫描，仅一层
     * @Param [constructorPos, blockIDs, distanceLimit, b, b1, oneLayerOnly]
     * @return void
     **/
    private void setClosestBlocksOfType(BlockPos constructorPos, List<Block> blockIDs, int distanceLimit, boolean needsToSeeSky, boolean scanDownwards, boolean oneLayerOnly) {
        try {
            //创建集合
            HashMap hm = new HashMap();
            //是否跳过
            boolean skip = false;
            //赋值距离限制，半径
            int fsDistanceLimit = distanceLimit;
            if (oneLayerOnly) {
                fsDistanceLimit = 0;
            }
            //循环上下半径 高
            for (int i = 0; i <fsDistanceLimit; i++) {
                //循环宽，平面的
                for (int j = 0; j < distanceLimit; j++) {
                    for (int k = -j; k <= j; k++) {
                        for (int l = -j; l <=j ; l++) {
                            int sx = (int) (constructorPos.getX() + k);
                            int sy;
                            //是否向下扫描
                            if (scanDownwards) {
                                sy = (int) (constructorPos.getY() - i);
                            } else {
                                sy = (int) (constructorPos.getY() + i);
                            }
                            int sz = (int) (constructorPos.getZ() + l);
                            skip = false;
                            //获取方块
                            for (int m = 0; m < blockIDs.size(); m++) {
                                Block blockID = (Block) blockIDs.get(m);
                                if(this.folk.entity.worldObj==null){
                                    return;
                                }
                                BlockPos pos=new BlockPos(sx, sy, sz);
                                //获取当前世界的方块
                                Block blockInWorld =this.folk.entity.worldObj.getBlockState(pos).getBlock();
                                if (blockInWorld == blockID) {
                                    //如果向上扫描
                                    if (needsToSeeSky) {
                                        //方块是空的
                                        boolean canSeeSky;
                                        pos=new BlockPos(sx, sy + 1, sz);
                                        if (this.folk.entity.worldObj.getBlockState(pos).getBlock() == null) {
                                            canSeeSky = true;
                                        } else {
                                            canSeeSky = false;
                                        }

                                        if (canSeeSky) {
                                            skip = false;
                                        } else {
                                            skip = true;
                                        }
                                    }
                                    if(!skip){
                                        V3 v = new V3((double) sx, (double) sy, (double) sz);
                                        if (!hm.containsKey(v.toString())) {
                                            hm.put(v.toString(), v);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            this.closestBlocks=new CopyOnWriteArrayList<>(hm.values());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void onMinute() {
        if (this.missingCheck < 3) {
            ++this.missingCheck;
        } else {
            //谁在规划
            String s1 = I18n.format("container.sim.job.builder_constructor_started_who1");
            if (this.missingBlock != null && this.missingBlock != Blocks.AIR) {

                String s2 = I18n.format("container.sim.job.builder_constructor_started_more");
                //谁在建“”需要更多的“”
                ModSimLoader.sendChat(this.folk.getName() + s1 + "(" + this.terrainType.terrainName + ") " + s2 + this.missingBlock.getLocalizedName());
            }

            if (ModSimLoader.states.credits < 0.02F) {
                //没有足够的资金支付给
                ModSimLoader.sendChat(I18n.format("container.sim.JobBuilder1") + this.folk.getName() + s1 + "( " + this.terrainType.terrainName + ")!");
            }

            this.missingCheck = 0;
        }
    }
    @Override
    public String toString() {
        return I18n.format("container.sim.Vocation16");
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
        this.conBox.terrainFormerJob = this;
        this.conBox.setLocationAndAngles(this.workPlace.x + 2.0D, this.workPlace.y, this.workPlace.z, 0.0F, 0.0F);
        if (!this.jobWorld.isRemote) {
            this.jobWorld.spawnEntityInWorld(this.conBox);
        }

    }
}
