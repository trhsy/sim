package com.trhsy.sim.common.core.entity;

import net.minecraft.block.Block;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.util.Vec3;

import java.io.Serializable;

/**
 * 在整个mod中用作三维向量，因为Minecraft的Vec3有点奇怪，或者我不太擅长Java：-）
 */
public class V3 extends Vec3 {
    private static final long serialVersionUID = 3681796724829797704L;
    /**x 轴**/
    public double x;
    /**y 轴**/
    public double y;
    /**z 轴**/
    public double z;
    /**名字**/
    public String name = "";
    /**方块**/
    public Block blockID ;
    /**元数据**/
    public int meta = 0;
    /**
     * @Author fan
     * @Description //TODO 维度
     * @Date 11:05 2022/3/26
     * @Param
     * @return
     **/
    public int theDimension = 0;
    /**目的地**/
    public Double destinationAcc = 1.5D;
    /**是否超时**/
    public boolean doNotTimeout = false;
    public V3(double x, double y, double z) {
        super(x,y,z);
    }

    @Override
    public V3 clone() {
        V3 retV=null;
        try{
            retV = new V3(this.x, this.y, this.z, this.theDimension);
        }catch (Exception e){
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error(this.name + "v3 clone出错了" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return retV;
    }

    /**
     * 重载以包含维度0=超世界，-1=虚空1=结束2以上可能是Mystcraft年龄
     * @param x
     * @param y
     * @param z
     * @param dimension
     */
    public V3(Double x, Double y, Double z, int dimension) {
        super(x,y,z);
        this.x = x;
        this.y = y;
        this.z = z;
        this.theDimension = dimension;
    }
    public V3(int x, int y, int z, int dimension) {
        super(x,y,z);
        this.x = (double)x;
        this.y = (double)y;
        this.z = (double)z;
        this.theDimension = dimension;
    }



    public V3(Double x, Double y, Double z, Block id, int meta) {
        super(x,y,z);
        this.x = x;
        this.y = y;
        this.z = z;
        this.blockID = id;
        this.meta = meta;
    }

    public V3(int x, int y, int z) {
        super(x,y,z);
        this.x = (double)x;
        this.y = (double)y;
        this.z = (double)z;
    }

    public void setVals(V3 v) {
        subtractReverse(v);
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }
    /**
     * 坐标相同 比较x、y和z，以查看它们是否相同，并且只有INT值，而不是double还比较维度
     * @param comp
     * @param compareDimension
     * @param exactly
     * @return
     */
    public boolean isSameCoordsAs(V3 comp, boolean compareDimension, boolean exactly) {
        boolean ret = false;
        try {
            if (comp == null) {
                return false;
            } else {
                if (exactly) {
                    if (this.getDistanceTo(comp) == 0 && (this.theDimension == comp.theDimension || !compareDimension)) {
                        ret = true;
                    }
                } else if (this.getDistanceTo(comp) <= 2 && (this.theDimension == comp.theDimension || !compareDimension)) {
                    ret = true;
                }

                return ret;
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];
            ModSimReloaded.log.error("isSameCoordsAs出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return ret;
    }

    public int getDistanceTo(V3 other) {
        int i=0;
        try {
            if (other == null) {
                i= 0;
            } else {
                double dist = Math.sqrt((other.x - this.x) * (other.x - this.x) + (other.y - this.y) * (other.y - this.y) + (other.z - this.z) * (other.z - this.z));
                i= (int)dist;
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("getDistanceTo出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

        return i;
    }
    @Override
    public String toString() {
        String s="";
        try {
            s=this.x + "," + this.y + "," + this.z + "," + this.theDimension;
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("toString出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
        return s;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Block getBlockID() {
        return blockID;
    }

    public void setBlockID(Block blockID) {
        this.blockID = blockID;
    }

    public int getMeta() {
        return meta;
    }

    public void setMeta(int meta) {
        this.meta = meta;
    }

    public int getTheDimension() {
        return theDimension;
    }

    public void setTheDimension(int theDimension) {
        this.theDimension = theDimension;
    }

    public Double getDestinationAcc() {
        return destinationAcc;
    }

    public void setDestinationAcc(Double destinationAcc) {
        this.destinationAcc = destinationAcc;
    }

    public boolean isDoNotTimeout() {
        return doNotTimeout;
    }

    public void setDoNotTimeout(boolean doNotTimeout) {
        this.doNotTimeout = doNotTimeout;
    }
}
