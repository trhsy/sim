package com.trhsy.sim.npcCode.task;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npcCode.NpcData;
import com.trhsy.sim.npcCode.V3;
import com.trhsy.sim.npcCode.job.Job;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @ClassName JobTaskChopTrees
 * @Description todo 砍树任务
 * @Author TRHSY
 * @Date 2023/4/1518:54
 **/
public class JobTaskChopTrees extends JobTask {
    //要开采的方块
    List<BlockPos> toMine = new CopyOnWriteArrayList<BlockPos>();
    //开始地点
    V3 startPoint;
    //半径
    int radius;
    //上次检查后的时间
    long timeSinceLastCheck = 0L;
    //上次砍树时间
    long timeSinceLastChop = 0L;
    private ExecutorService executorService= Executors.newCachedThreadPool();
    public JobTaskChopTrees(Job j, long ms, V3 startPoint, int radius) {
        super(j, ms);
        this.startPoint = startPoint;
        this.radius = radius;
    }

    @Override
    public void onTaskBegin() {
        findTree();
    }

    /**
     * 找树
     */
    public void findTree() {
        //开始坐标的Y轴
        double startY = this.startPoint.y;
        //先找XZ坐标的地点，半径为60的地方
        for (double x = this.startPoint.x - (double) this.radius; x < this.startPoint.x + (double) this.radius; ++x) {
            for (double z = this.startPoint.z - (double) this.radius; z < this.startPoint.z + (double) this.radius; ++z) {
                //Y轴为高度，则循环到最高
                double sY1 = startY;
                BlockPos bp = new BlockPos(x, sY1, z);
                Block b = this.job.folk.entity.world.getBlockState(bp).getBlock();
                //找到树了
                if(b.isWood(this.job.folk.entity.world, bp)){
                    while (b.isWood(this.job.folk.entity.world, bp)) {
                        V3 v3 = new V3(x, startY, z);
                        this.folk.forceMoveToXYZ(v3);

                        //砍树
                        this.folk.setStatus(new TextComponentTranslation("container.sim.CHOPPINGTREE", new Object[0]).getUnformattedText());
                        BlockPos bp1 = new BlockPos(x, sY1, z);
                        //是树叶清空
                        if (this.job.folk.entity.world.getBlockState(bp1).getBlock().isLeaves(this.job.folk.entity.world.getBlockState(bp1), this.job.folk.entity.world, bp1)) {
                            this.job.folk.entity.world.setBlockToAir(bp1);
                        }
                        IBlockState woodState = this.job.folk.entity.world.getBlockState(bp1);
                        //将物品放到箱子
                        this.job.placeInJobChest(new ItemStack(woodState.getBlock(), 1));
                        this.job.folk.entity.world.setBlockToAir(bp1);
                        ModSimLoader.addMoney(-0.02F);
                        NpcData var10000 = this.folk;
                        var10000.skillMining += 0.001F;
                        this.addMiningLevel();
                        this.timeSinceLastChop = System.currentTimeMillis();
                        //加一层
                        sY1++;
                        bp = new BlockPos(x, sY1, z);
                        b = this.job.folk.entity.world.getBlockState(bp).getBlock();
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void onUpdate() {
        //当前时间
        long now = System.currentTimeMillis();
        //游戏模式是正常模式
        if (ModSimLoader.gamemode != 1) {
            if (ModSimLoader.money < 0.02F) {
                //没有钱付给我！
                this.folk.setStatus(new TextComponentTranslation("container.sim.JobBuilder2",new Object[0]).getUnformattedText());
                //this.completed = true;
                return;
            }
            if ((float) (now - this.timeSinceLastCheck) > 1000.0F - 100.0F * folk.skillMining) {
                //找树
                this.folk.setStatus(new TextComponentTranslation("container.sim.GOTOTREE", new Object[0]).getUnformattedText());
                findTree();
                /*if(executorService.isTerminated()){
                    executorService.submit(new Runnable() {
                        @Override
                        public void run() {
                            findTree();
                        }
                    });
                }*/


            }
        }else{
            //找树
            this.folk.setStatus(new TextComponentTranslation("container.sim.GOTOTREE", new Object[0]).getUnformattedText());
            findTree();
            /*if(executorService.isTerminated()) {
                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        findTree();
                    }
                });
            }*/
        }
        /*if (System.currentTimeMillis() - this.timeSinceLastCheck > 5L) {
            this.timeSinceLastCheck = System.currentTimeMillis();
            if (this.toMine.size() < 1) {
//                this.job.folk.forceMoveToXYZNoWarp(this.startPoint);
                //找树
                this.folk.setStatus(new TextComponentTranslation("container.sim.GOTOTREE", new Object[0]).getUnformattedText());
                findTree();
                return;
            }
            /*
            BlockPos p = this.toMine.get(0);
            V3 v3 = new V3(p.getX(), p.getY(), p.getZ());
            if (!this.job.folk.forceMoveToXYZ(v3)) {
                //this.job.folk.forceMoveToXYZNoWarp(v3);
            }
            //this.job.folk.forceMoveToXYZNoWarp(V3.fromBlockPos((BlockPos)this.toMine.get(0)));
            if (this.toMine.size() > 0) {
                //砍树
                this.folk.setStatus(new TextComponentTranslation("container.sim.CHOPPINGTREE", new Object[0]).getUnformattedText());
                if (this.folk.entity.motionX == 0.0D && this.folk.entity.motionZ == 0.0D) {
                    for (double x = this.folk.entity.posX - 1.0D; x <= this.folk.entity.posX + 1.0D; ++x) {
                        for (double y = this.folk.entity.posY; y <= this.folk.entity.posY + 1.0D; ++y) {
                            for (double z = this.folk.entity.posZ - 1.0D; z <= this.folk.entity.posZ + 1.0D; ++z) {
                                BlockPos bp = new BlockPos(x, y, z);
                                //是树叶清空
                                if (this.job.folk.entity.world.getBlockState(bp).getBlock().isLeaves(this.job.folk.entity.world.getBlockState(bp), this.job.folk.entity.world, bp)) {
                                    this.job.folk.entity.world.setBlockToAir(bp);
                                }
                            }
                        }
                    }
                }

                if (this.job.folk.isAtLocation(V3.fromBlockPos((BlockPos) this.toMine.get(0)), 5)) {
                    if ((float) (System.currentTimeMillis() - this.timeSinceLastChop) > 1500.0F - 100.0F * this.folk.skillMining) {
                        //砍树
                        this.folk.setStatus(new TextComponentTranslation("container.sim.CHOPPINGTREE", new Object[0]).getUnformattedText());
                        IBlockState woodState = this.job.folk.entity.world.getBlockState((BlockPos) this.toMine.get(0));
                        //将物品放到箱子
                        this.job.placeInJobChest(new ItemStack(woodState.getBlock().getItemDropped(woodState, new Random(), 0), woodState.getBlock().quantityDropped(new Random())));
                        this.job.folk.entity.world.setBlockToAir((BlockPos) this.toMine.get(0));
                        ModSimLoader.addMoney(-0.02F);
                        this.toMine.remove(0);
                        NpcData var10000 = this.folk;
                        var10000.skillMining += 0.001F;
                        this.addMiningLevel();
                        this.timeSinceLastChop = System.currentTimeMillis();
                    }
                } else {
                    //找树
                    this.folk.setStatus(new TextComponentTranslation("container.sim.GOTOTREE", new Object[0]).getUnformattedText());
                    //findTree();
                }
            }


        }*/
    }

    @Override
    public void onTaskComplete() {

    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 增加挖矿等级
     * @Date 19:01 2023/4/15
     * @Param []
     **/
    public void addMiningLevel() {
        int b4 = (int) Math.floor((double) this.folk.skillMining);
        if (this.folk.skillMining < 10.0F) {
            NpcData var10000 = this.folk;
            var10000.skillMining = (float) ((double) var10000.skillMining + 0.001D / (double) b4);
        }

        int aft = (int) Math.floor((double) this.folk.skillMining);
        if (b4 != aft) {
            //张三 刚刚升级到矿工等级 1
            ModSimLoader.sendChat(this.folk.getName() + " " + new TextComponentTranslation("container.sim.job.miner.farmer.levelled", new Object[0]).getUnformattedText() + " " + aft);
        }

    }

    /**
     * @return void
     * @Author fan
     * @Description //TODO 检查是否有要砍的方块
     * @Date 19:02 2023/4/15
     * @Param [bp, toMine]
     **/
    void checkNeighbours(BlockPos bp, List<BlockPos> toMine) {
        //东南西北
        EnumFacing[] var3 = new EnumFacing[]{EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST};
        int var4 = var3.length;

        for (int var5 = 0; var5 < var4; ++var5) {
            EnumFacing facing = var3[var5];
            BlockPos neighbour = bp.offset(facing);
            BlockPos upNeighbour = bp.up().offset(facing);
            if (!((double) neighbour.getX() > this.startPoint.x + (double) this.radius) && !((double) neighbour.getX() < this.startPoint.x - (double) this.radius) && !((double) neighbour.getZ() > this.startPoint.z + (double) this.radius) && !((double) neighbour.getZ() < this.startPoint.z - (double) this.radius)) {
                if (this.job.folk.entity.world.getBlockState(neighbour).getBlock().isWood(this.job.folk.entity.world, neighbour) && !toMine.contains(neighbour)) {
                    toMine.add(neighbour);
                    this.checkNeighbours(neighbour, toMine);
                }

                if (this.job.folk.entity.world.getBlockState(upNeighbour).getBlock().isWood(this.job.folk.entity.world, upNeighbour) && !toMine.contains(upNeighbour)) {
                    toMine.add(upNeighbour);
                    this.checkNeighbours(upNeighbour, toMine);
                }
            }
        }

    }

    public BlockPos findClosestBlockType(BlockPos startXYZ, int searchDistance) {
        if (this.job.folk.entity.world.getBlockState(startXYZ).getBlock().isLeaves(this.job.folk.entity.world.getBlockState(startXYZ), this.job.folk.entity.world, startXYZ)) {
            return startXYZ;
        } else {
            for (int d = 1; d < searchDistance; ++d) {
                for (int xo = -d; xo <= d; ++xo) {
                    for (int zo = -d; zo <= d; ++zo) {
                        BlockPos s = new BlockPos(startXYZ.getX() + xo, startXYZ.getY(), startXYZ.getZ() + zo);
                        if (this.job.folk.entity.world.getBlockState(s).getBlock().isLeaves(this.job.folk.entity.world.getBlockState(startXYZ), this.job.folk.entity.world, startXYZ)) {
                            return s;
                        }
                    }
                }
            }

            return null;
        }
    }
}
