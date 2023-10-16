package com.trhsy.sim.npc.task;

import com.trhsy.sim.loader.BlockLoader;
import com.trhsy.sim.loader.ItemLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.V3;
import com.trhsy.sim.npc.build.Building;
import com.trhsy.sim.npc.job.Job;
import com.trhsy.sim.task.JobTask;
import javafx.stage.Stage;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

import java.util.List;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.npc.task
 * @ClassName: JobTaskCheesemaker
 * @Description: 制作奶酪
 * @date 2023/07/31 下午 3:59
 */
public class JobTaskCheesemaker extends JobTask{
    //步
    public int step = 1;
    public String status;
    private Building theCheeseFactory = null;
    //切换位置
    private boolean tubToggle = true;
    /**
     * 当前搅拌位置
     */
    private V3 currentStirPos;
    //搅拌计数
    private int stirCount = 0;
    public JobTaskCheesemaker(Job j, long ms, String status) {
        super(j, ms);
        this.status = status;
        this.theCheeseFactory = ModSimLoader.getBuildingByV3(this.job.workPlace);
        this.step = 1;
    }
    @Override
    public void onTaskBegin() {
        this.job.folk.setStatus(this.status);
    }

    @Override
    public void onUpdate() {
        try {
            if (this.step == 1) {
                this.step = 2;
                //准备加满水槽
                this.job.folk.setStatus(I18n.format("container.sim.job.cheese_maker.Preparing"));
            } else if (this.step == 2) {
                //倒牛奶
                List<V3> milkblocks = this.theCheeseFactory.getSpecialBlocks(0);
                boolean filledOk = false;
                for (V3 milkBlock : milkblocks) {
                    //牛奶
                    BlockPos blockPos = new BlockPos(milkBlock.x, milkBlock.y, milkBlock.z);
                    Block id = this.job.jobWorld.getBlockState(blockPos).getBlock();
                    if (id != Blocks.AIR && id != BlockLoader.milk&& id != BlockLoader.flowing_milk) {
                        this.job.jobWorld.setBlockState(blockPos, BlockLoader.milk.getDefaultState(), 3);
                        filledOk = true;
                    }

                    try {
                        for(int i = 0; i < this.job.folk.inventory.size(); ++i) {
                                this.job.folk.inventory.remove(i);
                        }
                    } catch (Exception e) {
                    }
                }
                if (!filledOk) {
                    this.step = 3;
                }
                //搅拌
            }else if(this.step == 3){
                //检查牛奶粘度
                this.job.folk.setStatus(I18n.format("container.sim.job.cheese_maker.viscosity"));
                List<V3> stirPositions = this.theCheeseFactory.getSpecialBlocks(4);
                if (!stirPositions.isEmpty()) {
                    if (this.tubToggle) {
                        this.job.folk.forceMoveToXYZNoWarp(this.currentStirPos =  stirPositions.get(0));
                    } else {
                        this.job.folk.forceMoveToXYZNoWarp(this.currentStirPos =  stirPositions.get(1));
                    }

                    this.tubToggle = !this.tubToggle;
                    this.stirCount = 0;
                    this.step = 4;
                } else {
                    //奶酪厂出了问题，把建筑构造下来，重新构建它
                    ModSimLoader.sendChat(I18n.format("container.sim.job.cheese_maker.constructor"));
                    this.job.folk.fire();
                }
            }else if(this.step == 4){
                String say = "";
                switch (this.stirCount) {
                    case 0:
                        //搅拌牛奶
                        say = I18n.format("container.sim.job.cheese_maker.Stirring");
                        break;
                    case 1:
                        //加入绝密成分
                        say = I18n.format("container.sim.job.cheese_maker.ingredient");
                        break;
                    case 2:
                        //添加细菌培养
                        say = I18n.format("container.sim.job.cheese_maker.bacterial");
                        break;
                    case 3:
                        //除去不需要的孢子
                        say = I18n.format("container.sim.job.cheese_maker.unwanted");
                        break;
                    case 4:
                        //检查发酵进度
                        say = I18n.format("container.sim.job.cheese_maker.fermentation");
                        break;
                    case 5:
                        //加入凝乳酶
                        say = I18n.format("container.sim.job.cheese_maker.Adding");
                        break;
                    case 6:
                        //网条状奶酪
                        say = I18n.format("container.sim.job.cheese_maker.Reticulating");
                }
                this.job.folk.setStatus(say);
                    //开始转化奶酪
                    this.step = 5;

            }else if(this.step == 5){
                //0是牛奶
                List<V3> milkBlocks = this.theCheeseFactory.getSpecialBlocks(0);
                //1是要放置的奶酪块
                List<V3> cheeseBlocks = this.theCheeseFactory.getSpecialBlocks(1);
                if (!milkBlocks.isEmpty() && !cheeseBlocks.isEmpty()) {
                    boolean placedCheese = false;
                    int milkGotCount = 0;

                    Block id;
                    int dist;
                    for (int m = milkBlocks.size() - 1; m > 0; --m) {
                        V3 cheese = (V3) milkBlocks.get(m);

                        BlockPos blockPos = new BlockPos(cheese.x, cheese.y, cheese.z);
                        id = this.job.jobWorld.getBlockState(blockPos).getBlock();
                        dist = id.getMetaFromState(this.job.jobWorld.getBlockState(blockPos));

                        if (id == BlockLoader.milk && dist == 0) {
                            this.job.jobWorld.setBlockState(blockPos, id.getDefaultState(), 3);
                            ++milkGotCount;
                            if (milkGotCount > 1) {
                                break;
                            }
                        }
                    }

                    if (milkGotCount > 0) {
                        for (V3 cheese : cheeseBlocks) {
                            id = this.job.jobWorld.getBlockState(new BlockPos(cheese.x, cheese.y, cheese.z)).getBlock();
                            dist = cheese.getDistanceTo(currentStirPos);
                            if (id != BlockLoader.blockCheese && dist < 5) {
                                BlockPos blockPos = new BlockPos(cheese.x, cheese.y, cheese.z);
                                this.job.jobWorld.setBlockState(blockPos, BlockLoader.blockCheese.getDefaultState(), 3);
                                placedCheese = true;
                                break;
                            }
                        }
                    }

                    if (milkGotCount == 0 || !placedCheese) {
                        //this.completeTask();
                        this.step = 6;
                    }

                } else {
                    //this.theFolk.selfFire();
                    //有一个与奶酪工厂问题，请尝试重新建立它 - 没有奶块
                    ModSimLoader.sendChat(I18n.format("container.sim.job.cheese_maker.Cheese_factory"));
                }
            }else if(this.step == 6){
                //1是要放置的奶酪块
                List<V3> cheeseBlocks = this.theCheeseFactory.getSpecialBlocks(1);
                for (V3 cheese : cheeseBlocks) {
                    Block id = this.job.jobWorld.getBlockState(new BlockPos(cheese.x, cheese.y, cheese.z)).getBlock();
                    if (id.equals(BlockLoader.blockCheese)) {
                        //替换为空气
                        BlockPos blockPos = new BlockPos(cheese.x, cheese.y, cheese.z);
                        this.job.jobWorld.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 3);
                        this.job.placeInJobChest(new ItemStack(ItemLoader.itemCheese,9));
                    }
                }
                //this.completeTask();
            }
        } catch (Exception e) {
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("stageGoingToTank出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }
    }
    @Override
    public void onTaskComplete() {

    }

}
