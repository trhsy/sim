package com.trhsy.sim.npcCode.job;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.block.BlockConstructorBox;
import com.trhsy.sim.entity.EntityConBox;
import com.trhsy.sim.loader.BlockLoader;
import com.trhsy.sim.loader.ConfigLoader;
import com.trhsy.sim.loader.ItemLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npcCode.NpcData;
import com.trhsy.sim.npcCode.V3;
import com.trhsy.sim.npcCode.build.TerrainType;
import com.trhsy.sim.npcCode.task.JobTask;
import com.trhsy.sim.npcCode.task.JobTaskIdle;
import com.trhsy.sim.npcCode.task.JobTaskTerrain;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

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
    public TerrainType terrainType;
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
     * 缺失的方块
     **/
    private ItemStack missingBlock = null;
    /**
     * 建造位置
     **/
    private BlockPos constructorPos;

    /**
     * 缺失检查
     **/
    private int missingCheck = 0;
    //工作阶段
    public int terrain_stage = 0;
    public JobTerrainFormer(NpcData folk, TerrainType terrainType, BlockPos pos, World world) {
        super(folk, pos, world);
        try {
//手持锡锹
            folk.holding = new ItemStack(ItemLoader.tinSpade);
            this.startPos = pos;
            this.constructorPos = pos;
            this.terrainType = terrainType;
            //规划师
            this.jobName = new TextComponentTranslation("container.sim.Vocation16",new Object[0]).getUnformattedText();
            if (folk.entity != null) {
                IBlockState s = this.jobWorld.getBlockState(pos);
                if (s != null) {
                    Block block = s.getBlock();
                    if (block != null) {
                        //建筑箱
                        this.constructorBlock = (BlockConstructorBox) block;
                        this.constructorBlock.employee = folk;
                    }
                }
            }
            //如果允许 NPC 说话
            if (ConfigLoader.configFolkTalking) {
                //播放 我准备好了
                SoundEvent soundEvent = null;
                //判断性别，发出不一样的声音
                if (this.folk.gender == 0) {
                    soundEvent = new SoundEvent(new ResourceLocation(ModSim.MODID + ":im_read_m"));
                } else {
                    soundEvent = new SoundEvent(new ResourceLocation(ModSim.MODID + ":im_read_y"));
                }
                Minecraft mc = Minecraft.getMinecraft();
                if(mc!=null&&mc.world!=null){
                    for (EntityPlayer entityPlayer : mc.world.playerEntities) {
                        mc.world.playSound(entityPlayer,entityPlayer.posX, entityPlayer.posY, entityPlayer.posZ, soundEvent, SoundCategory.AMBIENT, 1.0F, 1.0F);
                    }
                }
            }
            if (this.folk != null) {
                if (this.folk.entity != null) {
                    if (this.jobWorld != null) {
                        if (!this.jobWorld.isRemote) {
                            //NPC数据为空，并且没有指派员工
                            if (this.folk.entity != null) {
                                //建造位置为空
                                if (this.jobWorld.getBlockState(this.constructorPos) == null) {
                                    return;
                                }
                                Block block = this.jobWorld.getBlockState(this.constructorPos).getBlock();
                                BlockConstructorBox cons = null;
                                if (block == BlockLoader.blockConstructorBox) {
                                    //获取建筑箱的
                                    cons = (BlockConstructorBox) block;
                                }
                                if (cons != null) {
                                    //当前建筑箱的工作人员是
                                    cons.employee = this.folk;
                                    //等待规划类型
                                    if (this.terrainType == null) {
                                        //等待蓝图
                                        this.folk.setStatus(new TextComponentTranslation("container.sim.job.builder_Awaiting_terrainType",new Object[0]).getUnformattedText());
                                    } else {
                                        if(this.jobTasks.size() == 0){
                                            //直接放置方块
                                            this.addJobTask(new JobTaskTerrain(this, -1L,this.missingBlock,this.terrainType,this.constructorPos));

                                        }
                                    }
                                }else{
                                    //等待规划类型
                                    if (this.terrainType == null) {
                                        //等待蓝图
                                        this.folk.setStatus(new TextComponentTranslation("container.sim.job.builder_Awaiting_terrainType",new Object[0]).getUnformattedText());
                                    } else {
                                        if(this.jobTasks.size() == 0){
                                            //直接放置方块
                                            this.addJobTask(new JobTaskTerrain(this, -1L,this.missingBlock,this.terrainType,this.constructorPos));

                                        }
                                    }
                                }
                            }
                            if (this.jobTasks.size() > 0&&this.currentTask==null) {
                                this.currentTask = (JobTask) this.jobTasks.get(0);
                                this.currentTask.begin();
                            }
                        }
                    }
                }
            }
            this.stage = 0;
//        this.createConBox();
        }catch (Exception e){
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("JobTerrainFormer1出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }

    }

    public JobTerrainFormer(NpcData folk, V3 pos, World world) {
        super(folk, pos, world);
        try {
            this.constructorPos = pos.toBlockPos();
            //建筑箱
            this.constructorBlock = (BlockConstructorBox) world.getBlockState(pos.toBlockPos()).getBlock();
            //建筑工
            this.jobName = new TextComponentTranslation("container.sim.Vocation16",new Object[0]).getUnformattedText();
            this.constructorBlock.employee = folk;
            this.stage = 0;
            this.createConBox();
        }catch (Exception e){
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("JobTerrainFormer出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }

    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        try {
            if (this.stage == -1) {
                this.terrain_stage = 0;
                this.stage = 0;
            }else if (this.terrain_stage == 0) {
                this.terrain_stage = 1;
                //去上班
                this.addJobTask(new JobTaskIdle(this, 200L, new TextComponentTranslation("container.sim.job.builder_Arrived",new Object[0]).getUnformattedText()));
            }else if(this.terrain_stage==1){
                //直接放置方块
                this.addJobTask(new JobTaskTerrain(this, -1L,this.missingBlock,this.terrainType,this.constructorPos));
                this.terrain_stage = 2;
            }else{
                if (this.jobTasks.size() > 0&&this.currentTask==null) {
                    this.currentTask = (JobTask) this.jobTasks.get(0);
                    this.currentTask.begin();
                }
            }
        }catch (Exception e){
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("JobTerrainFormer-onUpdate出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }

    }




    @Override
    public void onMinute() {
        try {
            /*if (this.missingCheck < 3) {
                ++this.missingCheck;
            } else {
                //谁在规划
                String s1 = new TextComponentTranslation("container.sim.job.builder_constructor_started_who1",new Object[0]).getUnformattedText();
                if (this.missingBlock != null && this.missingBlock != new ItemStack(Blocks.AIR)) {

                    String s2 = new TextComponentTranslation("container.sim.job.builder_constructor_started_more",new Object[0]).getUnformattedText();
                    //谁在建“”需要更多的“”
                    ModSimLoader.sendChat(this.folk.getName() + s1 + "(" + this.terrainType.terrainName + ",new Object[0]).getUnformattedText() " + s2 + this.missingBlock.getDisplayName());
                }

                if (ModSimLoader.money < 0.02F) {
                    //没有足够的资金支付给
                    ModSimLoader.sendChat(new TextComponentTranslation("container.sim.JobBuilder1",new Object[0]).getUnformattedText() + this.folk.getName() + s1 + "( " + this.terrainType.terrainName + ",new Object[0]).getUnformattedText()!",new Object[0]).getUnformattedText();
                }

                this.missingCheck = 0;
            }*/
        }catch (Exception e){
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("onMinute出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
        }

    }

    @Override
    public String toString() {
        return new TextComponentTranslation("container.sim.Vocation16",new Object[0]).getUnformattedText();
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
            this.jobWorld.spawnEntity(this.conBox);
        }

    }
}
