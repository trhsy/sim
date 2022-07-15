package com.trhsy.sim.client.entity;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.client.entity.model.ModelWindmill;
import com.trhsy.sim.common.entity.*;
import com.trhsy.sim.common.jobs.Job;
import com.trhsy.sim.common.loader.BlockLoader;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * 渲染风车
 */
public class RenderWindmill extends Render<EntityWindmill> {

    private static final ResourceLocation[] myTextures = new ResourceLocation[16];
    EntityWindmill entity = null;
    ModelWindmill modelWindmill;

    public RenderWindmill(RenderManager renderManager) {
        super(renderManager);
        this.modelWindmill = modelWindmill;
        for (int c = 0; c < 16; c++) {
            myTextures[c] = new ResourceLocation(ModSim.MODID, "textures/models/entityWindmill" + c + ".png");
        }
    }

    @Override
    public void doRender(EntityWindmill theEntity, double x, double y, double z,
                         float yaw, float pitch) {
        try {
            entity = (EntityWindmill) theEntity;
            if (this.entity == null) {
                return;
            }

            int meta = -1;

            V3 v3 = Job.findClosestBlockType(new V3((int) entity.posX, (int) entity.posY - 1, (int) entity.posZ), BlockLoader.blockWindmill, 5);
            if (v3 != null) {
                BlockPos blockPos=new BlockPos(v3.x.intValue(), v3.y.intValue(), v3.z.intValue());
                TileEntityWindmill teWindmill = (TileEntityWindmill) entity.worldObj.getTileEntity(blockPos);
                if (teWindmill != null) {
                    //meta=teWindmill.meta;
                }
            }
            if (meta == -1) {
                meta = 0;
            } // not loaded yet, so just default to white

            this.renderManager.renderEngine.bindTexture(myTextures[meta]);
            GL11.glPushMatrix();
            GL11.glTranslatef((float) x, (float) y, (float) z);
            GL11.glScalef(0.0666f, 0.0666f, 0.0666f);
            GL11.glRotatef(180, 1.0f, 0f, 0);        // angle, x, y, z
            GL11.glRotatef(yaw, 0, 1.0f, 0f);
            modelWindmill.Vane1rod.rotateAngleZ = entity.sailRotation;
            modelWindmill.Vane2rod.rotateAngleZ = entity.sailRotation + 1.570796F;
            modelWindmill.Vane3rod.rotateAngleZ = entity.sailRotation + 3.141593F;
            modelWindmill.Vane4rod.rotateAngleZ = entity.sailRotation + 4.712389F;
            modelWindmill.Vane1main.rotateAngleZ = entity.sailRotation;
            modelWindmill.Vane2main.rotateAngleZ = entity.sailRotation + 1.570796F;
            modelWindmill.Vane3main.rotateAngleZ = entity.sailRotation + 3.141593F;
            modelWindmill.Vane4main.rotateAngleZ = entity.sailRotation + 4.712389F;
            modelWindmill.WindmillAxle.rotateAngleZ = entity.sailRotation;
            modelWindmill.render(entity, 0f, 0f, 0f, 0f, 0f, 0f);
            GL11.glPopMatrix();
        } catch (Exception e) {
            ModSimReloaded.log.error("渲染风车出差了：" + e.getMessage());
        }

    }

    @Override
    protected ResourceLocation getEntityTexture(EntityWindmill entity) {
            return null;
    }
}
