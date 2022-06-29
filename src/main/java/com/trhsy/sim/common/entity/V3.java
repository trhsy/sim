package com.trhsy.sim.common.entity;

import net.minecraft.block.Block;

import java.io.Serializable;

/**
 * 在整个mod中用作三维向量，因为Minecraft的Vec3有点奇怪，或者我不太擅长Java：-）
 */
public class V3 implements Serializable, Cloneable {
    private static final long serialVersionUID = 3681796724829797704L;
    /**x 轴**/
    public Double x;
    /**y 轴**/
    public Double y;
    /**z 轴**/
    public Double z;
    /**名字**/
    public String name = "";
    /**方块**/
    public Block blockID = null;
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
    public V3() {
    }

    @Override
    public V3 clone() {
        V3 retV = new V3(this.x, this.y, this.z, this.theDimension);
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
        this.x = x;
        this.y = y;
        this.z = z;
        this.theDimension = dimension;
    }
    public V3(int x, int y, int z, int dimension) {
        this.x = (double)x;
        this.y = (double)y;
        this.z = (double)z;
        this.theDimension = dimension;
    }

    /**
     * 重载，用于将基于文本的保存文件作为V3加载到中
     * @param v3
     */
    public V3(String v3) {
        String[] v = v3.split(",");
        this.x = Double.parseDouble(v[0]);
        this.y = Double.parseDouble(v[1]);
        this.z = Double.parseDouble(v[2]);
        this.theDimension = Integer.parseInt(v[3]);
    }

    public V3(Double x, Double y, Double z, Block id, int meta) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.blockID = id;
        this.meta = meta;
    }

    public V3(int i, int j, int k) {
        this.x = (double)i;
        this.y = (double)j;
        this.z = (double)k;
    }

    public void setVals(V3 v) {
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
    }

    /**
     * 获取距离 计算并返回此V3和传入V3之间的距离-假定尺寸相同
     * @param other
     * @return
     */
    public int getDistanceTo(V3 other) {
        if (other == null) {
            return 0;
        } else {
            double dist = Math.sqrt((other.x - this.x) * (other.x - this.x) + (other.y - this.y) * (other.y - this.y) + (other.z - this.z) * (other.z - this.z));
            return (int)dist;
        }
    }

    @Override
    public String toString() {
        return this.x.intValue() + "," + this.y.intValue() + "," + this.z.intValue() + "," + this.theDimension;
    }
}
