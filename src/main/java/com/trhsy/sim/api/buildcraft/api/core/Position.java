package com.trhsy.sim.api.buildcraft.api.core;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * ========================================
 *
 * @ClassName Position
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:06
 * ========================================
 **/
public class Position implements ISerializable {
    public double x;
    public double y;
    public double z;
    public ForgeDirection orientation;

    public Position() {
        this.x = 0.0D;
        this.y = 0.0D;
        this.z = 0.0D;
        this.orientation = ForgeDirection.UNKNOWN;
    }

    public Position(double ci, double cj, double ck) {
        this.x = ci;
        this.y = cj;
        this.z = ck;
        this.orientation = ForgeDirection.UNKNOWN;
    }

    public Position(double ci, double cj, double ck, ForgeDirection corientation) {
        this.x = ci;
        this.y = cj;
        this.z = ck;
        this.orientation = corientation;
        if (this.orientation == null) {
            this.orientation = ForgeDirection.UNKNOWN;
        }

    }

    public Position(Position p) {
        this.x = p.x;
        this.y = p.y;
        this.z = p.z;
        this.orientation = p.orientation;
    }

    public Position(NBTTagCompound nbttagcompound) {
        this.readFromNBT(nbttagcompound);
    }

    public Position(TileEntity tile) {
        this.x = (double)tile.xCoord;
        this.y = (double)tile.yCoord;
        this.z = (double)tile.zCoord;
        this.orientation = ForgeDirection.UNKNOWN;
    }

    public Position(BlockIndex index) {
        this.x = (double)index.x;
        this.y = (double)index.y;
        this.z = (double)index.z;
        this.orientation = ForgeDirection.UNKNOWN;
    }

    public void moveRight(double step) {
        switch(this.orientation) {
        case SOUTH:
            this.x -= step;
            break;
        case NORTH:
            this.x += step;
            break;
        case EAST:
            this.z += step;
            break;
        case WEST:
            this.z -= step;
        }

    }

    public void moveLeft(double step) {
        this.moveRight(-step);
    }

    public void moveForwards(double step) {
        switch(this.orientation) {
        case SOUTH:
            this.z += step;
            break;
        case NORTH:
            this.z -= step;
            break;
        case EAST:
            this.x += step;
            break;
        case WEST:
            this.x -= step;
            break;
        case UP:
            this.y += step;
            break;
        case DOWN:
            this.y -= step;
        }

    }

    public void moveBackwards(double step) {
        this.moveForwards(-step);
    }

    public void moveUp(double step) {
        switch(this.orientation) {
        case SOUTH:
        case NORTH:
        case EAST:
        case WEST:
            this.y += step;
        default:
        }
    }

    public void moveDown(double step) {
        this.moveUp(-step);
    }

    public void writeToNBT(NBTTagCompound nbttagcompound) {
        if (this.orientation == null) {
            this.orientation = ForgeDirection.UNKNOWN;
        }

        nbttagcompound.setDouble("i", this.x);
        nbttagcompound.setDouble("j", this.y);
        nbttagcompound.setDouble("k", this.z);
        nbttagcompound.setByte("orientation", (byte)this.orientation.ordinal());
    }

    public void readFromNBT(NBTTagCompound nbttagcompound) {
        this.x = nbttagcompound.getDouble("i");
        this.y = nbttagcompound.getDouble("j");
        this.z = nbttagcompound.getDouble("k");
        this.orientation = ForgeDirection.values()[nbttagcompound.getByte("orientation")];
    }

    @Override
    public String toString() {
        return "{" + this.x + ", " + this.y + ", " + this.z + "}";
    }

    public Position min(Position p) {
        return new Position(p.x > this.x ? this.x : p.x, p.y > this.y ? this.y : p.y, p.z > this.z ? this.z : p.z);
    }

    public Position max(Position p) {
        return new Position(p.x < this.x ? this.x : p.x, p.y < this.y ? this.y : p.y, p.z < this.z ? this.z : p.z);
    }

    public boolean isClose(Position newPosition, float f) {
        double dx = this.x - newPosition.x;
        double dy = this.y - newPosition.y;
        double dz = this.z - newPosition.z;
        double sqrDis = dx * dx + dy * dy + dz * dz;
        return !(sqrDis > (double)(f * f));
    }

    @Override
    public void readData(ByteBuf stream) {
        this.x = stream.readDouble();
        this.y = stream.readDouble();
        this.z = stream.readDouble();
        this.orientation = ForgeDirection.getOrientation(stream.readByte());
    }

    @Override
    public void writeData(ByteBuf stream) {
        stream.writeDouble(this.x);
        stream.writeDouble(this.y);
        stream.writeDouble(this.z);
        stream.writeByte(this.orientation.ordinal());
    }

    @Override
    public int hashCode() {
        return 51 * (int)this.x + 13 * (int)this.y + (int)this.z;
    }
}
