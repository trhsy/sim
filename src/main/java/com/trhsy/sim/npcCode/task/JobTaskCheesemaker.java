package com.trhsy.sim.npcCode.task;

import com.trhsy.sim.loader.BlockLoader;
import com.trhsy.sim.loader.ItemLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npcCode.V3;
import com.trhsy.sim.npcCode.build.Building;
import com.trhsy.sim.npcCode.job.Job;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

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
    public int cheesemakerStage = 0;
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
        V3 v3=new V3(this.job.workPlace.x,this.job.workPlace.y-1,this.job.workPlace.z);
        this.theCheeseFactory = ModSimLoader.getBuildingByV3(v3);
        this.cheesemakerStage = 1;
    }
    @Override
    public void onTaskBegin() {
        this.job.folk.setStatus(this.status);
    }

    @Override
    public void onUpdate() {
        try {
            if (this.cheesemakerStage == 1) {
                this.cheesemakerStage = 2;
                //准备加满水槽
                this.job.folk.setStatus(new TextComponentTranslation("container.sim.job.cheese_maker.Preparing",new Object[0]).getUnformattedText());
            } else if (this.cheesemakerStage == 2) {
                if(this.theCheeseFactory==null){}
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
                    this.cheesemakerStage = 3;
                }
                //搅拌
            }else if(this.cheesemakerStage == 3){
                //检查牛奶粘度
                this.job.folk.setStatus(new TextComponentTranslation("container.sim.job.cheese_maker.viscosity",new Object[0]).getUnformattedText());
                List<V3> stirPositions = this.theCheeseFactory.getSpecialBlocks(4);
                if (!stirPositions.isEmpty()) {
                    if (this.tubToggle) {
                        this.job.folk.forceMoveToXYZNoWarp(this.currentStirPos =  stirPositions.get(0));
                    } else {
                        this.job.folk.forceMoveToXYZNoWarp(this.currentStirPos =  stirPositions.get(1));
                    }

                    this.tubToggle = !this.tubToggle;
                    this.stirCount = 0;
                    this.cheesemakerStage = 4;
                } else {
                    //奶酪厂出了问题，把建筑构造下来，重新构建它
                    ModSimLoader.sendChat(new TextComponentTranslation("container.sim.job.cheese_maker.constructor",new Object[0]).getUnformattedText());
                    this.job.folk.fire();
                }
            }else if(this.cheesemakerStage == 4){
                String say = "";
                switch (this.stirCount) {
                    case 0:
                        //搅拌牛奶
                        say = new TextComponentTranslation("container.sim.job.cheese_maker.Stirring",new Object[0]).getUnformattedText();
                        this.stirCount++;
                        break;
                    case 1:
                        //加入绝密成分
                        say = new TextComponentTranslation("container.sim.job.cheese_maker.ingredient",new Object[0]).getUnformattedText();
                        this.stirCount++;
                        break;
                    case 2:
                        //添加细菌培养
                        say = new TextComponentTranslation("container.sim.job.cheese_maker.bacterial",new Object[0]).getUnformattedText();
                        this.stirCount++;
                        break;
                    case 3:
                        //除去不需要的孢子
                        say = new TextComponentTranslation("container.sim.job.cheese_maker.unwanted",new Object[0]).getUnformattedText();
                        this.stirCount++;
                        break;
                    case 4:
                        //检查发酵进度
                        say = new TextComponentTranslation("container.sim.job.cheese_maker.fermentation",new Object[0]).getUnformattedText();
                        this.stirCount++;
                        break;
                    case 5:
                        //加入凝乳酶
                        say = new TextComponentTranslation("container.sim.job.cheese_maker.Adding",new Object[0]).getUnformattedText();
                        this.stirCount++;
                        break;
                    case 6:
                        //网条状奶酪
                        say = new TextComponentTranslation("container.sim.job.cheese_maker.Reticulating",new Object[0]).getUnformattedText();
                        //开始转化奶酪
                        this.cheesemakerStage = 5;
                }
                this.job.folk.setStatus(say);


            }else if(this.cheesemakerStage == 5){
               String say= new TextComponentTranslation("container.sim.job.cheese_maker.Extracting",new Object[0]).getUnformattedText();

                this.job.folk.setStatus(say);
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
                        this.cheesemakerStage = 6;
                    }

                } else {
                    //this.theFolk.selfFire();
                    //有一个与奶酪工厂问题，请尝试重新建立它 - 没有奶块
                    ModSimLoader.sendChat(new TextComponentTranslation("container.sim.job.cheese_maker.Cheese_factory",new Object[0]).getUnformattedText());
                }
            }else if(this.cheesemakerStage == 6){
                String say= new TextComponentTranslation("container.sim.job.cheese_maker.Slicing",new Object[0]).getUnformattedText();
                this.job.folk.setStatus(say);
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
                this.completeTask();
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
