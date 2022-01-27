package com.trhsy.sim.common;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import com.trhsy.sim.common.block.BlockMarker;
import com.trhsy.sim.common.entity.V3;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.apache.logging.log4j.Marker;

/**
 * ========================================
 *
 * @ClassName EntityAlignBeam
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/26 0026下午 5:50
 * ========================================
 **/
public class EntityAlignBeam extends Entity {
    public float yaw = 0.0F;
    public String caption = "x";
    private Marker theMarker = null;

    public EntityAlignBeam(World par1World) {
        super(par1World);
        this.field_70158_ak = true;
        if (!BlockMarker.hasPlaced) {
            this.func_70106_y();
        }

        this.func_70105_a(0.1F, 100.0F);
    }

    @Override
    protected void entityInit() {

    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound p_70037_1_) {

    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound p_70014_1_) {

    }

    public boolean func_70067_L() {
        return false;
    }

    public boolean func_70104_M() {
        return false;
    }

    public void func_70091_d(double par1, double par3, double par5) {
        this.field_70159_w = 0.0D;
        this.field_70181_x = 0.0D;
        this.field_70179_y = 0.0D;
    }

    public void func_70030_z() {
    }

    public void func_70016_h(double par1, double par3, double par5) {
        super.func_70016_h(0.0D, 0.0D, 0.0D);
    }

    public void func_70071_h_() {
        if (this.caption.contentEquals("x")) {
            this.theMarker = BlockMarker.getMarker(new V3(this.posX, this.posY, this.posZ, this.field_71093_bK));
            if (this.theMarker != null) {
                this.caption = this.theMarker.caption;
            }
        }

        if (this.theMarker != null) {
            this.posY = (double)this.theMarker.y;
        }

    }

    protected void func_70088_a() {
        this.field_70145_X = true;
    }

    public void func_70056_a(double par1, double par3, double par5, float par7, float par8, int par9) {
    }

    protected void func_70037_a(NBTTagCompound var1) {
    }

    protected void func_70014_b(NBTTagCompound var1) {
    }
}
