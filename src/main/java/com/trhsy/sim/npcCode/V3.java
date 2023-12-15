package com.trhsy.sim.npcCode;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.npc
 * @ClassName: V3
 * @Description:
 * @date 2022/10/11 17:55
 */
public class V3{
        public double x;
        public double y;
        public double z;
        public int dimension;
        /**方块**/
        public Block blockID ;
        /**元数据**/
        public int meta;
        public V3(double x, double y, double z) {
                this.x = x;
                this.y = y;
                this.z = z;
        }

        public V3(double x, double y, double z, int dim) {
                this.x = x;
                this.y = y;
                this.z = z;
                this.dimension = dim;
        }
        public V3(double x, double y, double z, Block id, int meta) {
                this.x = x;
                this.y = y;
                this.z = z;
                this.meta = meta;
        }
        public V3(BlockPos pos) {
                this.x = (double)pos.getX();
                this.y = (double)pos.getY();
                this.z = (double)pos.getZ();
        }

        public V3(BlockPos pos, int dim) {
                this.x = (double)pos.getX();
                this.y = (double)pos.getY();
                this.z = (double)pos.getZ();
                this.dimension = dim;
        }

        @Override
        public String toString() {
                return this.x + "," + this.y + "," + this.z + "," + this.dimension;
        }

        public String toStringByMeta() {
                return this.x + "," + this.y + "," + this.z + "," + this.meta;
        }

        public BlockPos toBlockPos() {
                return new BlockPos(this.x, this.y, this.z);
        }

        public int getDistanceTo(V3 other) {
                if (other == null) {
                        return 0;
                } else {
                        double dist = Math.sqrt((other.x - this.x) * (other.x - this.x) + (other.y - this.y) * (other.y - this.y) + (other.z - this.z) * (other.z - this.z));
                        return (int)dist;
                }
        }

        public static V3 fromString(String v3) {
                String[] values = v3.split(",");
                return new V3(Double.valueOf(values[0]), Double.valueOf(values[1]), Double.valueOf(values[2]), Integer.valueOf(values[3]));
        }

        public static V3 fromBlockPos(BlockPos pos) {
                return new V3((double)pos.getX(), (double)pos.getY(), (double)pos.getZ());
        }

        public static V3 fromVec3d(Vec3d vec3d) {
                return new V3(vec3d.x, vec3d.y, vec3d.z);
        }

        @Override
        public boolean equals(Object o) {
                if (o instanceof V3) {
                        V3 v3 = (V3)o;
                        if (this.toBlockPos().equals(v3.toBlockPos()) && this.dimension == v3.dimension) {
                                return true;
                        }
                }

                return false;
        }
        }
