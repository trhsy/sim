package com.trhsy.sim.npc.job;

import com.google.common.collect.UnmodifiableIterator;
import com.trhsy.sim.block.BlockConstructorBox;
import com.trhsy.sim.entity.EntityConBox;
import com.trhsy.sim.loader.BlockLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.loader.NetWorkLoader;
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
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDoor;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.*;

/**
 * @ClassName JobBuilder
 * @Description todo 建筑工作
 * @Author TRHSY
 * @Date 2022/10/2014:25
 **/
public class JobBuilder extends Job{
    /**建筑蓝图**/
    public BuildingBlueprint blueprint;
    /**悬浮的控制箱**/
    public EntityConBox conBox;
    /**要放置的块**/
    List<V3> placedBlocks = new ArrayList();
    /**缺失的方块**/
    Block missingBlock = null;
    /**开始位置**/
    BlockPos startPos;
    /**建造位置**/
    BlockPos constructorPos;
    /**控制箱位置**/
    BlockPos controllerPos;
    /**活动区域**/
    BlockPos livingPos;
    /**建筑箱**/
    public BlockConstructorBox constructorBlock = null;
    /**方向*/
    public int direction;
    //默认方向
    public int defaultDirection;
    int x = 0;
    int y = 0;
    int z = 0;
    /**块的编号**/
    public int blockNumber = 0;
    /**自上一个块位置的时间**/
    private transient long timeSinceLastBlockPlace = 0L;
    /**已重新指派员工**/
    boolean hasReassignedEmployee;
    /**缺失检查**/
    int missingCheck = 0;
    /**
     * @Author fan
     * @Description //TODO 初始化工作
     * @Date 22:12 2022/10/30
     * @Param [folk, bp, pos, direction, world]
     * @return npc 蓝图 块
     **/
    public JobBuilder(NpcData folk, BuildingBlueprint bp, BlockPos pos, int direction, World world) {
        super(folk, pos, world);
        //建筑工
        this.jobName = I18n.format("container.sim.Vocation1");
        this.startPos = pos;
        this.constructorPos = pos;
        this.direction = direction;
        this.blueprint = bp;
        int i = 0;
        boolean hasControlBox = false;
        IBlockState[] var8 = this.blueprint.structure;
        for (int j = 0; j < var8.length; j++) {
            IBlockState st = var8[j];
            if (st.getBlock() == BlockLoader.blockControlBox) {
                hasControlBox = true;
                break;
            }
            ++i;
        }

        if (!hasControlBox) {
            //如果没有找到控制箱，则第一个方块就是建筑箱
            this.blueprint.structure[0] = BlockLoader.blockControlBox.getDefaultState();
        }

        this.constructorBlock = (BlockConstructorBox) folk.entity.worldObj.getBlockState(pos).getBlock();
        this.constructorBlock.employee = folk;
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

    public JobBuilder(NpcData folk, BlockPos pos, int direction, World world) {
        super(folk, pos, world);
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

        this.createConBox();
    }

    public JobBuilder(NpcData folk, V3 pos, int direction, World world) {
        super(folk, pos, world);
        this.jobName = I18n.format("container.sim.Vocation1");
        this.constructorPos = pos.toBlockPos();
        this.startPos = pos.toBlockPos();
        this.direction = direction;
        BlockConstructorBox cons = (BlockConstructorBox)folk.entity.worldObj.getBlockState(pos.toBlockPos()).getBlock();
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

        this.createConBox();
    }

    public void onUpdate() {
        super.onUpdate();
        if (this.folk != null) {
            if (this.folk.entity != null) {
                if (this.folk.entity.worldObj != null) {
                    if (!this.folk.entity.worldObj.isRemote) {
                        if (this.folk.entity != null && !this.hasReassignedEmployee) {
                            if (this.folk.entity.worldObj.getBlockState(this.constructorPos) == null) {
                                return;
                            }

                            BlockConstructorBox cons = (BlockConstructorBox)this.folk.entity.worldObj.getBlockState(this.constructorPos).getBlock();
                            cons.employee = this.folk;
                            this.hasReassignedEmployee = true;
                        }

                        if (this.blueprint == null) {
                            this.folk.setStatus(I18n.format("container.sim.job.builder_Awaiting_blueprint"));
                        } else {
                            new Random();
                            Long now = System.currentTimeMillis();
                            if (ModSimLoader.states.gameModeNumber == 0) {
                                if ((float)(now - this.timeSinceLastBlockPlace) > 1000.0F - 100.0F * this.folk.skillBuilding) {
                                    this.timeSinceLastBlockPlace = now;
                                    NetWorkLoader.net.sendToAll(new PacketSendBuildingRequirements(this.blueprint, this));
                                    this.placeBlock();
                                }
                            } else {
                                this.timeSinceLastBlockPlace = now;
                                if (!this.folk.entity.worldObj.isRemote) {
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
            String s1=I18n.format("container.sim.job.builder_constructor_started_who's");
            if (this.missingBlock != null && this.missingBlock != Blocks.AIR) {

                String s2=I18n.format("container.sim.job.builder_constructor_started_more");
                //谁在建“”需要更多的“”
                ModSimLoader.sendChat(this.folk.getName() + s1+"(" + this.blueprint.name + ") "+s2 + this.missingBlock.getLocalizedName());
            }

            if (ModSimLoader.states.credits < 0.02F) {
                ModSimLoader.sendChat(I18n.format("container.sim.JobBuilder1") + this.folk.getName() + s1+"( " + this.blueprint.name + ")!");
            }

            this.missingCheck = 0;
        }
    }
    /**
     * @Author fan
     * @Description //TODO 放置方块
     * @Date 22:07 2022/10/30
     * @Param []
     * @return void
     **/
    public void placeBlock() {
        try {
            boolean normalBlock = true;
            boolean hasPlaced = false;
            if (this.blockNumber >= this.blueprint.structure.length) {
                this.folk.fire();
                this.constructorBlock.employee = null;
                this.createBuilding();
                ModSimLoader.log.info("从第一次功能检查激发");
                return;
            }

            BlockPos newBP = null;
            IBlockState st = null;
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

            if (ModSimLoader.states.credits < 0.02F) {
                //没有钱付给我！
                this.folk.setStatus(I18n.format("container.sim.JobBuilder2"));
                return;
            }

            if (st.getBlock() instanceof BlockDoor && this.folk.entity.worldObj.getBlockState(newBP.down()).getBlock() != st.getBlock()) {
                normalBlock = false;
                ItemDoor.placeDoor(this.folk.entity.worldObj, newBP, EnumFacing.NORTH, st.getBlock(), false);
                ModSimLoader.addMoney(-0.02F);
                this.folk.entity.swingArm(EnumHand.MAIN_HAND);
            }

            if (st.getBlock() == Blocks.BED) {
            }

            if (st.getBlock() == BlockLoader.blockControlBox) {
                this.controllerPos = newBP;
            } else if (st.getBlock() == BlockLoader.blockLiving) {
                this.livingPos = newBP;
                st = Blocks.AIR.getDefaultState();
            }

            if (normalBlock) {
                if (ModSimLoader.states.gameModeNumber == 0) {
                    boolean hasBlock = false;
                    if (!this.blueprint.isRequiredBlock(st.getBlock())) {
                        //放置方块
                        this.folk.setStatus(I18n.format("container.sim.JobBuilder3"));
                        this.folk.entity.swingArm(EnumHand.MAIN_HAND);
                        hasBlock = true;
                    } else {
                        Block b = st.getBlock();
                        if (this.isSameBlock(this.folk.entity.worldObj.getBlockState(newBP).getBlock(), st.getBlock())) {
                            hasBlock = true;
                        } else {
                            Iterator var7 = this.inventoriesFindClosest(this.workPlace, 2).iterator();

                            while(var7.hasNext()) {
                                IInventory inv = (IInventory)var7.next();

                                for(int i = 0; i < inv.getSizeInventory(); ++i) {
                                    if (inv.getStackInSlot(i).getItem() == Item.getItemFromBlock(b)) {
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
                        this.missingBlock = st.getBlock();
                        return;
                    }

                    this.missingBlock = null;
                    this.folk.entity.swingArm(EnumHand.MAIN_HAND);
                    //放置方块
                    this.folk.setStatus(I18n.format("container.sim.JobBuilder3"));
                }

                if (st.getProperties().size() == 0) {
                    //放置方块
                    this.folk.setStatus(I18n.format("container.sim.JobBuilder3"));
                    this.folk.entity.swingArm(EnumHand.MAIN_HAND);
                    this.folk.entity.worldObj.setBlockState(newBP, st);
                    ModSimLoader.addMoney(-0.02F);
                } else if (st.getBlock() instanceof BlockFence) {
                    //放置方块
                    this.folk.setStatus(I18n.format("container.sim.JobBuilder3"));
                    this.folk.entity.swingArm(EnumHand.MAIN_HAND);
                    this.folk.entity.worldObj.setBlockState(newBP, st);
                    ModSimLoader.addMoney(-0.02F);
                } else {
                    UnmodifiableIterator var15 = st.getProperties().keySet().iterator();

                    label287:
                    while(true) {
                        while(true) {
                            while(true) {
                                IProperty prop;
                                EnumFacing facing;
                                Collection validFacings;
                                do {
                                    while(true) {
                                        if (!var15.hasNext()) {
                                            break label287;
                                        }

                                        prop = (IProperty)var15.next();
                                        if (prop.getName().equals("facing")) {
                                            this.folk.setStatus(I18n.format("container.sim.JobBuilder3"));
                                            ModSimLoader.log.info("================================================================");
                                            ModSimLoader.log.info(this.direction + " : " + this.blueprint.direction);
                                            ModSimLoader.log.info("得到方块" + st.getBlock().getUnlocalizedName() + "面向");
                                            facing = (EnumFacing)st.getValue(prop);
                                            validFacings = prop.getAllowedValues();
                                            ModSimLoader.log.info("块当前面向" + facing.toString());
                                            break;
                                        }

                                        this.folk.setStatus(I18n.format("container.sim.JobBuilder3"));
                                        this.folk.entity.swingArm(EnumHand.MAIN_HAND);
                                        this.folk.entity.worldObj.setBlockState(newBP, st);
                                        ModSimLoader.addMoney(-0.02F);
                                    }
                                } while(validFacings.size() < 4);

                                if (facing != EnumFacing.UP && facing != EnumFacing.DOWN) {
                                    IBlockState newState;
                                    if (this.direction == 0 && this.blueprint.direction == 3 || this.direction == 1 && this.blueprint.direction == 0 || this.direction == 2 && this.blueprint.direction == 1 || this.direction == 3 && this.blueprint.direction == 2) {
                                        newState = st.withProperty(prop, facing.rotateY());
                                        st = st.withRotation(Rotation.CLOCKWISE_90);
                                        this.folk.entity.worldObj.setBlockState(newBP, st);
                                        ModSimLoader.addMoney(-0.02F);
                                        ModSimLoader.log.info("方块应该面向 " + facing.rotateY().toString() + " (顺时针方向的)");
                                        ModSimLoader.log.info("方块实际面向 " + ((EnumFacing)newState.getValue(prop)).toString());
                                    } else if ((this.direction != 0 || this.blueprint.direction != 2) && (this.direction != 1 || this.blueprint.direction != 3) && (this.direction != 2 || this.blueprint.direction != 0) && (this.direction != 3 || this.blueprint.direction != 1)) {
                                        if (this.direction == 0 && this.blueprint.direction == 1 || this.direction == 1 && this.blueprint.direction == 2 || this.direction == 2 && this.blueprint.direction == 3 || this.direction == 3 && this.blueprint.direction == 0) {
                                            newState = st.withProperty(prop, facing.rotateYCCW());
                                            st = st.withRotation(Rotation.COUNTERCLOCKWISE_90);
                                            this.folk.entity.worldObj.setBlockState(newBP, st);
                                            ModSimLoader.addMoney(-0.02F);
                                            ModSimLoader.log.info("方块应该面向 " + facing.rotateYCCW().toString() + " (逆时针方向)");
                                            ModSimLoader.log.info("方块实际面向 " + ((EnumFacing)newState.getValue(prop)).toString());
                                        } else if (this.direction == this.blueprint.direction) {
                                            this.folk.entity.worldObj.setBlockState(newBP, st);
                                            ModSimLoader.addMoney(-0.02F);
                                                ModSimLoader.log.info("方块应该面向 " + facing.toString() + " (无旋转)");
                                        } else {
                                            ModSimLoader.log.info("出了问题(无旋转)");
                                            ModSimLoader.log.error(this.direction + ":" + this.blueprint.direction);
                                        }
                                    } else {
                                        st = st.withRotation(Rotation.CLOCKWISE_180);
                                        this.folk.entity.worldObj.setBlockState(newBP, st);
                                        ModSimLoader.addMoney(-0.02F);
                                        ModSimLoader.log.info("方块应该面向 " + facing.rotateY().rotateY().toString() + " (快速翻转)");
                                        ModSimLoader.log.info("方块实际面向 " + ((EnumFacing)st.getValue(prop)).toString());
                                    }
                                } else {
                                    this.folk.entity.worldObj.setBlockState(newBP, st);
                                }
                            }
                        }
                    }
                }
            }

            this.placedBlocks.add(V3.fromBlockPos(newBP));
            int b4 = (int)Math.floor((double)this.folk.skillBuilding);
            if (this.folk.skillBuilding < 10.0F) {
                NpcData var10000 = this.folk;
                var10000.skillBuilding = (float)((double)var10000.skillBuilding + 0.001D / (double)b4);
            }

            int aft = (int)Math.floor((double)this.folk.skillBuilding);
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
                this.constructorBlock.employee = null;
                ModSimLoader.log.info("从中间功能检查中激发");
                this.createBuilding();
                return;
            }

            if (this.blockNumber >= this.blueprint.structure.length) {
                this.folk.fire();
                this.constructorBlock.employee = null;
                ModSimLoader.log.info("Fired from intermediate function check");
                this.createBuilding();
                return;
            }

            try {
                BlockPos tempBP;
                for(; this.blueprint.structure[this.blockNumber].getBlock().getUnlocalizedName().contentEquals("tile.air"); ++this.blockNumber) {
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

                IBlockState var20;
                if (this.direction == 0) {
                    tempBP = new BlockPos(this.startPos.getX() + this.x, this.startPos.getY() + this.y, this.startPos.getZ() - this.z);
                    var20 = this.blueprint.structure[this.blockNumber];
                } else if (this.direction == 1) {
                    tempBP = new BlockPos(this.startPos.getX() + this.z, this.startPos.getY() + this.y, this.startPos.getZ() + this.x);
                    var20 = this.blueprint.structure[this.blockNumber];
                } else if (this.direction == 2) {
                    tempBP = new BlockPos(this.startPos.getX() - this.x, this.startPos.getY() + this.y, this.startPos.getZ() + this.z);
                    var20 = this.blueprint.structure[this.blockNumber];
                } else {
                    tempBP = new BlockPos(this.startPos.getX() - this.z, this.startPos.getY() + this.y, this.startPos.getZ() - this.x);
                    var20 = this.blueprint.structure[this.blockNumber];
                }

                while(this.folk.entity.worldObj.getBlockState(tempBP).getBlock().getUnlocalizedName().contentEquals(this.blueprint.structure[this.blockNumber].getBlock().getUnlocalizedName()) && this.blueprint.structure[this.blockNumber].getBlock().getUnlocalizedName() != "tile.air" && !this.blueprint.structure[this.blockNumber].getBlock().getUnlocalizedName().contentEquals("tile.air")) {
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
                    if (this.direction == 0) {
                        tempBP = new BlockPos(this.startPos.getX() + this.x, this.startPos.getY() + this.y, this.startPos.getZ() - this.z);
                        var20 = this.blueprint.structure[this.blockNumber];
                    } else if (this.direction == 1) {
                        tempBP = new BlockPos(this.startPos.getX() + this.z, this.startPos.getY() + this.y, this.startPos.getZ() + this.x);
                        var20 = this.blueprint.structure[this.blockNumber];
                    } else if (this.direction == 2) {
                        tempBP = new BlockPos(this.startPos.getX() - this.x, this.startPos.getY() + this.y, this.startPos.getZ() + this.z);
                        var20 = this.blueprint.structure[this.blockNumber];
                    } else {
                        tempBP = new BlockPos(this.startPos.getX() - this.z, this.startPos.getY() + this.y, this.startPos.getZ() - this.x);
                        var20 = this.blueprint.structure[this.blockNumber];
                    }
                }
            } catch (Exception var11) {
                this.folk.fire();
                this.constructorBlock.employee = null;
                this.createBuilding();
                return;
            }
        } catch (Exception var12) {
            var12.printStackTrace();
            ModSimLoader.log.info("因错误而解雇的员工");
            this.folk.fire();
            this.constructorBlock.employee = null;
        }

    }

    boolean isSameBlock(Block b1, Block b2) {
        if (b1 == b2) {
            return true;
        } else {
            return b1 == Blocks.DIRT && b2 == Blocks.GRASS || b1 == Blocks.GRASS && b2 == Blocks.DIRT;
        }
    }

    void createBuilding() {
        if (this.livingPos == null) {
            this.livingPos = this.controllerPos;
        }

        Building build = new Building(this.blueprint.name, 10.0F, V3.fromBlockPos(this.controllerPos), V3.fromBlockPos(this.livingPos));
        build.buildingType = this.blueprint.buildingType;
        build.structure = new ArrayList(this.placedBlocks);
        build.jobType = this.blueprint.jobType;
        build.rent = (float)this.blueprint.structure.length * 0.01F;
        ModSimLoader.buildings.add(build);
        build.saveBuilding();
        FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().sendChatMsg(new TextComponentString(this.folk.getName() + I18n.format("container.sim.job.builder_constructor_completed") + this.blueprint.name));
    }

    void createConBox() {
        this.conBox = new EntityConBox(this.jobWorld, this);
        this.conBox.folk = this.folk;
        this.conBox.job = this;
        this.conBox.setLocationAndAngles(this.workPlace.x + 2.0D, this.workPlace.y, this.workPlace.z, 0.0F, 0.0F);
        if (!this.jobWorld.isRemote) {
            this.jobWorld.spawnEntityInWorld(this.conBox);
        }

    }

    public void onArrive() {
    }

    public String toString() {
        return I18n.format("container.sim.Vocation1");
    }
}
