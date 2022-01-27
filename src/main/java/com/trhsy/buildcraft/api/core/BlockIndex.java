package com.trhsy.buildcraft.api.core;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * ========================================
 *
 * @ClassName BlockIndex
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 1:52
 * ========================================
 **/
public class BlockIndex implements Comparable<BlockIndex> {
    public int x;
    public int y;
    public int z;

    public BlockIndex() {
    }

    public BlockIndex(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public BlockIndex(NBTTagCompound c) {
        this.x = c.func_74762_e("i");
        this.y = c.func_74762_e("j");
        this.z = c.func_74762_e("k");
    }

    public BlockIndex(Entity entity) {
        this.x = (int)Math.floor(entity.posX);
        this.y = (int)Math.floor(entity.posY);
        this.z = (int)Math.floor(entity.posZ);
    }

    public BlockIndex(TileEntity entity) {
        this(entity.field_145851_c, entity.field_145848_d, entity.field_145849_e);
    }

    @Override
    public int compareTo(BlockIndex o) {
        if (o.x < this.x) {
            return 1;
        } else if (o.x > this.x) {
            return -1;
        } else if (o.z < this.z) {
            return 1;
        } else if (o.z > this.z) {
            return -1;
        } else if (o.y < this.y) {
            return 1;
        } else {
            return o.y > this.y ? -1 : 0;
        }
    }

    public void writeTo(NBTTagCompound c) {
        c.func_74768_a("i", this.x);
        c.func_74768_a("j", this.y);
        c.func_74768_a("k", this.z);
    }

    public Block getBlock(World world) {
        return world.getBlock(this.x, this.y, this.z);
    }

    @Override
    public String toString() {
        return "{" + this.x + ", " + this.y + ", " + this.z + "}";
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BlockIndex)) {
            return super.equals(obj);
        } else {
            BlockIndex b = (BlockIndex)obj;
            return b.x == this.x && b.y == this.y && b.z == this.z;
        }
    }

    @Override
    public int hashCode() {
        return (this.x * 37 + this.y) * 37 + this.z;
    }

    public boolean nextTo(BlockIndex blockIndex) {
        return Math.abs(blockIndex.x - this.x) <= 1 && blockIndex.y == this.y && blockIndex.z == this.z || blockIndex.x == this.x && Math.abs(blockIndex.y - this.y) <= 1 && blockIndex.z == this.z || blockIndex.x == this.x && blockIndex.y == this.y && Math.abs(blockIndex.z - this.z) <= 1;
    }
}