package com.sim.trhsy.common.entity;

import com.sim.trhsy.common.CommonProxy;
import net.minecraft.block.Block;

import java.io.Serializable;

/**
 * @ClassName V3
 * @Description todo
 * @Author Tian
 * @Date 2022/1/2319:55
 **/
public class V3 implements Serializable, Cloneable {
    private static final long serialVersionUID = 3681796724829797704L;
    public Double x;
    public Double y;
    public Double z;
    public String name = "";
    public Block blockID = null;
    public int meta = 0;
    public int theDimension = 0;
    public Double destinationAcc = 1.5D;
    public boolean doNotTimeout = false;

    public V3() {
    }

    public V3 clone() {
        V3 retV = new V3(this.x, this.y, this.z, this.theDimension);
        return retV;
    }

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

    public int getDistanceTo(V3 other) {
        if (other == null) {
            return 0;
        } else {
            double dist = Math.sqrt((other.x - this.x) * (other.x - this.x) + (other.y - this.y) * (other.y - this.y) + (other.z - this.z) * (other.z - this.z));
            return (int)dist;
        }
    }

    public String toString() {
        return this.x.intValue() + "," + this.y.intValue() + "," + this.z.intValue() + "," + this.theDimension;
    }
}
