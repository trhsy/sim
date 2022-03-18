package com.trhsy.sim.api.buildcraft.api.robots;


import com.trhsy.sim.api.buildcraft.api.core.BlockIndex;
import com.trhsy.sim.api.buildcraft.api.statements.StatementSlot;
import com.trhsy.sim.api.buildcraft.api.transport.IInjectable;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.IFluidHandler;

public abstract class DockingStation {
    public ForgeDirection side;
    public World world;
    private long robotTakingId = 9223372036854775807L;
    private EntityRobotBase robotTaking;
    private boolean linkIsMain = false;
    private BlockIndex index;

    public DockingStation(BlockIndex iIndex, ForgeDirection iSide) {
        this.index = iIndex;
        this.side = iSide;
    }

    public DockingStation() {
    }

    public boolean isMainStation() {
        return this.linkIsMain;
    }

    public int x() {
        return this.index.x;
    }

    public int y() {
        return this.index.y;
    }

    public int z() {
        return this.index.z;
    }

    public ForgeDirection side() {
        return this.side;
    }

    public EntityRobotBase robotTaking() {
        if (this.robotTakingId == 9223372036854775807L) {
            return null;
        } else {
            if (this.robotTaking == null) {
                this.robotTaking = RobotManager.registryProvider.getRegistry(this.world).getLoadedRobot(this.robotTakingId);
            }

            return this.robotTaking;
        }
    }

    public void invalidateRobotTakingEntity() {
        this.robotTaking = null;
    }

    public long linkedId() {
        return this.robotTakingId;
    }

    public boolean takeAsMain( EntityRobotBase robot) {
        if (this.robotTakingId == 9223372036854775807L) {
            IRobotRegistry registry = RobotManager.registryProvider.getRegistry(this.world);
            this.linkIsMain = true;
            this.robotTaking = robot;
            this.robotTakingId = robot.getRobotId();
            registry.registryMarkDirty();
            robot.setMainStation(this);
            registry.take(this, robot.getRobotId());
            return true;
        } else {
            return this.robotTakingId == robot.getRobotId();
        }
    }

    public boolean take( EntityRobotBase robot) {
        if (this.robotTaking == null) {
            IRobotRegistry registry = RobotManager.registryProvider.getRegistry(this.world);
            this.linkIsMain = false;
            this.robotTaking = robot;
            this.robotTakingId = robot.getRobotId();
            registry.registryMarkDirty();
            registry.take(this, robot.getRobotId());
            return true;
        } else {
            return robot.getRobotId() == this.robotTakingId;
        }
    }

    public void release( EntityRobotBase robot) {
        if (this.robotTaking == robot && !this.linkIsMain) {
            IRobotRegistry registry = RobotManager.registryProvider.getRegistry(this.world);
            this.unsafeRelease(robot);
            registry.registryMarkDirty();
            registry.release(this, robot.getRobotId());
        }

    }

    public void unsafeRelease(EntityRobotBase robot) {
        if (this.robotTaking == robot) {
            this.linkIsMain = false;
            this.robotTaking = null;
            this.robotTakingId = 9223372036854775807L;
        }

    }

    public void writeToNBT(NBTTagCompound nbt) {
        NBTTagCompound indexNBT = new NBTTagCompound();
        this.index.writeTo(indexNBT);
        nbt.setTag("index", indexNBT);
        nbt.setByte("side", (byte)this.side.ordinal());
        nbt.setBoolean("isMain", this.linkIsMain);
        nbt.setLong("robotId", this.robotTakingId);
    }

    public void readFromNBT(NBTTagCompound nbt) {
        this.index = new BlockIndex(nbt.getCompoundTag("index"));
        this.side = ForgeDirection.values()[nbt.getByte("side")];
        this.linkIsMain = nbt.getBoolean("isMain");
        this.robotTakingId = nbt.getLong("robotId");
    }

    public boolean isTaken() {
        return this.robotTakingId != 9223372036854775807L;
    }

    public long robotIdTaking() {
        return this.robotTakingId;
    }

    public BlockIndex index() {
        return this.index;
    }

    @Override
    public String toString() {
        return "{" + this.index.x + ", " + this.index.y + ", " + this.index.z + ", " + this.side + " :" + this.robotTakingId + "}";
    }

    public boolean linkIsDocked() {
        if (this.robotTaking() != null) {
            return this.robotTaking().getDockingStation() == this;
        } else {
            return false;
        }
    }

    public boolean canRelease() {
        return !this.isMainStation() && !this.linkIsDocked();
    }

    public boolean isInitialized() {
        return true;
    }

    public abstract Iterable<StatementSlot> getActiveActions();

    public IInjectable getItemOutput() {
        return null;
    }

    public ForgeDirection getItemOutputSide() {
        return ForgeDirection.UNKNOWN;
    }

    public IInventory getItemInput() {
        return null;
    }

    public ForgeDirection getItemInputSide() {
        return ForgeDirection.UNKNOWN;
    }

    public IFluidHandler getFluidOutput() {
        return null;
    }

    public ForgeDirection getFluidOutputSide() {
        return ForgeDirection.UNKNOWN;
    }

    public IFluidHandler getFluidInput() {
        return null;
    }

    public ForgeDirection getFluidInputSide() {
        return ForgeDirection.UNKNOWN;
    }

    public boolean providesPower() {
        return false;
    }

    public IRequestProvider getRequestProvider() {
        return null;
    }

    public void onChunkUnload() {
    }
}
