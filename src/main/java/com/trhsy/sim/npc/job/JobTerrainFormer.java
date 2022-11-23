package com.trhsy.sim.npc.job;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.block.BlockConstructorBox;
import com.trhsy.sim.entity.EntityConBox;
import com.trhsy.sim.loader.ConfigLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.NpcData;
import com.trhsy.sim.npc.V3;
import com.trhsy.sim.npc.build.TerrainType;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;

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
    public BlockPos startPos;
    /**
     * 悬浮的控制箱
     **/
    public EntityConBox conBox;
    /**
     * 建筑箱
     **/
    public BlockConstructorBox constructorBlock = null;
    /**
     * 自上一个块位置的时间
     **/
    private transient long timeSinceLastBlockPlace = 0L;
    /**
     * 缺失的方块
     **/
    Block missingBlock = null;
    /**
     * 建造位置
     **/
    BlockPos constructorPos;
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
                            if(this.terrainType==null){
                                //等待蓝图
                                this.folk.setStatus(I18n.format("container.sim.job.builder_Awaiting_terrainType"));
                            }else{
                                //当前时间
                                Long now = System.currentTimeMillis();

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
        } catch (Exception e) {
            e.printStackTrace();
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
