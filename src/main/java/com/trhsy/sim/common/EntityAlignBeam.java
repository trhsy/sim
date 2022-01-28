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
        this.ignoreFrustumCheck = true;
        if (!BlockMarker.hasPlaced) {
            this.setDead();
        }

        this.setSize(0.1F, 100.0F);
    }


    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public void moveEntity(double par1, double par3, double par5) {
        this.motionX = 0.0D;
        this.motionY = 0.0D;
        this.motionZ = 0.0D;
    }

    @Override
    public void onEntityUpdate() {
    }

    @Override
    public void setVelocity(double par1, double par3, double par5) {
        super.setVelocity(0.0D, 0.0D, 0.0D);
    }

    @Override
    public void onUpdate() {
        if (this.caption.contentEquals("x")) {
            this.theMarker = BlockMarker.getMarker(new V3(this.posX, this.posY, this.posZ, this.dimension));
            if (this.theMarker != null) {
                this.caption = this.theMarker.caption;
            }
        }

        if (this.theMarker != null) {
            this.posY = (double)this.theMarker.y;
        }

    }

    @Override
    protected void entityInit() {
        this.noClip = true;
    }

    @Override
    public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9) {
    }
    @Override
    protected void readEntityFromNBT(NBTTagCompound var1) {
    }
    @Override
    protected void writeEntityToNBT(NBTTagCompound var1) {
    }
}
