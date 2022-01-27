package com.trhsy.sim.common;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.entity.V3;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Random;

/**
 * ========================================
 *
 * @ClassName EntityConBox
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/26 0026下午 5:52
 * ========================================
 **/
public class EntityConBox extends Entity{
    public float boxYaw = 0.0F;
    public int textColor = 11534255;
    public FolkData theFolk = null;
    private Long lastCheck = 0L;
    private EntityPlayer closestPlayer;

    public EntityConBox(World par1World) {
        super(par1World);
        this.field_70145_X = true;
        this.field_70158_ak = true;
        if (!ModSimukraft.proxy.ranStartup) {
            ModSimukraft.log.info("EntityConBox: Killed system spawned ConBox");
            this.func_70106_y();
        }

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

    public void func_70071_h_() {
        if (System.currentTimeMillis() - this.lastCheck > 10000L) {
            if (this.theFolk != null && this.theFolk.theBuilding == null) {
                ModSimukraft.log.info("EntityConBox: Removing conBox as building is done");
                this.spawnExplosionParticle(this);
                this.func_70106_y();
            }

            ArrayList<V3> conblocks = Job.findClosestBlocks(new V3(this.posX, this.posY, this.posZ, this.field_71093_bK), ModSimukraft.buildingConstructor, 5);
            if (conblocks.size() < 1) {
                this.func_70106_y();
            }

            this.lastCheck = System.currentTimeMillis();
        }

        ++this.boxYaw;
        super.func_70071_h_();
    }

    public static FolkData getFolk(V3 where) {
        V3 con = Job.findClosestBlockType(where, ModSimukraft.buildingConstructor, 6, false);
        FolkData ret = null;

        for(int f = 0; f < ModSimukraft.theFolks.size(); ++f) {
            FolkData fd = (FolkData)ModSimukraft.theFolks.get(f);
            if (fd.employedAt != null && fd.employedAt.isSameCoordsAs(where, true, false)) {
                ModSimukraft.log.info("EntityConBox: found folk " + fd.name);
                ret = fd;
                break;
            }
        }

        return ret;
    }

    private void spawnExplosionParticle(Entity ent) {
        Random rand = new Random();

        for(int var1 = 0; var1 < 20; ++var1) {
            double var2 = rand.nextGaussian() * 0.02D;
            double var4 = rand.nextGaussian() * 0.02D;
            double var6 = rand.nextGaussian() * 0.02D;
            double var8 = 10.0D;

            try {
                ModSimukraft.proxy.getClientWorld().func_72869_a("explode", ent.posX + (double)(rand.nextFloat() * 1.0F * 2.0F) - 1.0D - var2 * var8, ent.posY + (double)(rand.nextFloat() * 1.0F) - var4 * var8, ent.posZ + (double)(rand.nextFloat() * 1.0F * 2.0F) - 1.0D - var6 * var8, var2, var4, var6);
            } catch (Exception var13) {
            }
        }

    }

    protected void func_70088_a() {
    }

    public AxisAlignedBB func_70114_g(Entity par1Entity) {
        return null;
    }

    public AxisAlignedBB func_70046_E() {
        return null;
    }

    public boolean func_70104_M() {
        return false;
    }

    public boolean func_70067_L() {
        return false;
    }

    protected void func_70037_a(NBTTagCompound var1) {
    }

    protected void func_70014_b(NBTTagCompound var1) {
    }
}