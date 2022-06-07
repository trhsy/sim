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
        this.noClip = true;
        this.ignoreFrustumCheck = true;
        if (!ModSim.proxy.ranStartup) {
            ModSim.log.info("EntityConBox: 被杀死的系统产生了ConBox");
            this.setDead();
        }

    }


    @Override
    public void onUpdate() {
        if (System.currentTimeMillis() - this.lastCheck > 10000L) {
            if (this.theFolk != null && this.theFolk.theBuilding == null) {
                ModSim.log.info("EntityConBox: 建筑完成后移除conBox");
                this.spawnExplosionParticle(this);
                this.setDead();
            }

            ArrayList<V3> conblocks = Job.findClosestBlocks(new V3(this.posX, this.posY, this.posZ, this.dimension), BlockLoader.constructorBox, 5);
            if (conblocks.size() < 1) {
                this.setDead();
            }

            this.lastCheck = System.currentTimeMillis();
        }

        ++this.boxYaw;
        super.onUpdate();
    }

    public static FolkData getFolk(V3 where) {
        V3 con = Job.findClosestBlockType(where, BlockLoader.constructorBox, 6, false);
        FolkData ret = null;

        for (int f = 0; f < ModSimReloaded.theFolks.size(); ++f) {
            FolkData fd = (FolkData) ModSimReloaded.theFolks.get(f);
            if (fd.employedAt != null && fd.employedAt.isSameCoordsAs(where, true, false)) {
                ModSim.log.info("EntityConBox: 找到人 " + fd.name);
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
            double var8 = 10;

            try {
                ModSim.proxy.getClientWorld().spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, ent.posX + (double) (rand.nextFloat() * 1.0F * 2.0F) - 1.0 - var2 * var8, ent.posY + (double) (rand.nextFloat() * 1.0F) - var4 * var8, ent.posZ + (double) (rand.nextFloat() * 1.0F * 2.0F) - 1.0 - var6 * var8, var2, var4, var6);
            } catch (Exception var13) {
            }
        }

    }

    @Override
    protected void entityInit() {
        // TODO document why this method is empty
    }
    @Override
    public AxisAlignedBB getCollisionBox(Entity par1Entity) {
        return null;
    }
    @Override
    public AxisAlignedBB getBoundingBox() {
        return null;
    }
    @Override
    public boolean canBePushed() {
        return false;
    }
    @Override
    public boolean canBeCollidedWith() {
        return false;
    }
    @Override
    protected void readEntityFromNBT(NBTTagCompound var1) {
    }
    @Override
    protected void writeEntityToNBT(NBTTagCompound var1) {
    }
}