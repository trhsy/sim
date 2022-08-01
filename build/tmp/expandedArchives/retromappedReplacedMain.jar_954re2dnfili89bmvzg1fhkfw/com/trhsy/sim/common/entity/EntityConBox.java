package com.trhsy.sim.common.entity;/**
 * @author trhsy
 * @date 2022/1/26 0026
 * @apiNote
 */

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.jobs.Job;
import com.trhsy.sim.common.loader.BlockLoader;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ========================================
 *
 * @ClassName EntityConBox
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/26 0026下午 5:52
 * ========================================
 **/
public class EntityConBox extends Entity {

    public float boxYaw = 0.0F;
    public int textColor = 11534255;
    public FolkData theFolk = null;
    private Long lastCheck = 0L;
    private EntityPlayer closestPlayer;

    public EntityConBox(World par1World) {
        super(par1World);
        this.field_70145_X = true;
        this.field_70158_ak = true;
        if (!ModSim.proxy.ranStartup) {
            ModSimReloaded.log.info("EntityConBox: 被杀死的系统产生了ConBox");
            this.func_70106_y();
        }

    }


    @Override
    public void func_70071_h_() {
        try {
            if (System.currentTimeMillis() - this.lastCheck > 10000L) {
                if (this.theFolk != null && this.theFolk.theBuilding == null) {
                    ModSimReloaded.log.info("EntityConBox: 建筑完成后移除conBox");
                    this.spawnExplosionParticle(this);
                    this.func_70106_y();
                }

                CopyOnWriteArrayList<V3> conblocks = Job.findClosestBlocks(new V3(this.field_70165_t, this.field_70163_u, this.field_70161_v, this.field_71093_bK), BlockLoader.blockConstructorBox, 5);
                if (conblocks.size() < 1) {
                    this.func_70106_y();
                }

                this.lastCheck = System.currentTimeMillis();
            }

            ++this.boxYaw;
            super.func_70071_h_();
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("onUpdate出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    public static FolkData getFolk(V3 where) {
        //V3 con = Job.findClosestBlockType(where, BlockLoader.blockConstructorBox, 6, false);
        FolkData ret = null;
        try {
            for (int f = 0; f < ModSimReloaded.theFolks.size(); ++f) {
                FolkData fd = (FolkData) ModSimReloaded.theFolks.get(f);
                if (fd.employedAt != null && fd.employedAt.isSameCoordsAs(where, true, false)) {
                    ModSimReloaded.log.info("EntityConBox: 找到人 " + fd.name);
                    ret = fd;
                    break;
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("getFolk出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

        return ret;
    }

    private void spawnExplosionParticle(Entity ent) {
        try {
            Random rand = new Random();
            for (int var1 = 0; var1 < 20; ++var1) {
                double var2 = rand.nextGaussian() * 0.02D;
                double var4 = rand.nextGaussian() * 0.02D;
                double var6 = rand.nextGaussian() * 0.02D;
                double var8 = 10;
                ModSim.proxy.getClientWorld().func_175688_a(EnumParticleTypes.EXPLOSION_NORMAL, ent.field_70165_t + (double) (rand.nextFloat() * 1.0F * 2.0F) - 1.0 - var2 * var8, ent.field_70163_u + (double) (rand.nextFloat() * 1.0F) - var4 * var8, ent.field_70161_v + (double) (rand.nextFloat() * 1.0F * 2.0F) - 1.0 - var6 * var8, var2, var4, var6);
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("spawnExplosionParticle出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }


    }

    @Override
    protected void func_70088_a() {
        // TODO document why this method is empty
    }

    @Override
    public AxisAlignedBB func_70114_g(Entity par1Entity) {
        return null;
    }

    //@Override
    //public AxisAlignedBB getBoundingBox() {
    //    return null;
    //}
    @Override
    public boolean func_70104_M() {
        return false;
    }

    @Override
    public boolean func_70067_L() {
        return false;
    }

    @Override
    protected void func_70037_a(NBTTagCompound var1) {
    }

    @Override
    protected void func_70014_b(NBTTagCompound var1) {
    }
}