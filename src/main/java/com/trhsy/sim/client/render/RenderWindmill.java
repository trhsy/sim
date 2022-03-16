package com.trhsy.sim.client.render;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.client.model.ModelWindmill;
import com.trhsy.sim.common.ModSim;
import com.trhsy.sim.common.TileEntityWindmill;
import com.trhsy.sim.common.entity.EntityWindmill;
import com.trhsy.sim.common.entity.V3;
import com.trhsy.sim.common.jobs.Job;
import com.trhsy.sim.common.loader.BlockLoader;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * ========================================
 *
 * @ClassName RenderWindmill3
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027上午 11:22
 * ========================================
 **/
public class RenderWindmill extends Render {
    private static final ResourceLocation[] myTextures = new ResourceLocation[16];
    EntityWindmill entity = null;
    ModelWindmill modelWindmill;

    public RenderWindmill(ModelWindmill modelWindmill) {
        this.modelWindmill = modelWindmill;

        for(int c = 0; c < 16; ++c) {
            myTextures[c] = new ResourceLocation(ModSim.MODID + "", "textures/models/entityWindmill" + c + ".png");
        }

    }
    @Override
    public void doRender(Entity theEntity, double x, double y, double z, float yaw, float pitch) {
        this.entity = (EntityWindmill)theEntity;
        if (this.entity != null) {
            int meta = -1;
            V3 v3 = Job.findClosestBlockType(new V3((int) this.entity.posX, (int) this.entity.posY - 1, (int) this.entity.posZ), BlockLoader.blockWindmill, 5);
            if (v3 != null) {
                TileEntityWindmill teWindmill = (TileEntityWindmill)this.entity.worldObj.getTileEntity(v3.x.intValue(), v3.y.intValue(), v3.z.intValue());
                if (teWindmill != null) {
                }
            }

            if (meta == -1) {
                meta = 0;
            }

            this.renderManager.renderEngine.bindTexture(myTextures[meta]);
            GL11.glPushMatrix();
            GL11.glTranslatef((float)x, (float)y, (float)z);
            GL11.glScalef(0.0666F, 0.0666F, 0.0666F);
            GL11.glRotatef(180.0F, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(yaw, 0.0F, 1.0F, 0.0F);
            this.modelWindmill.Vane1rod.rotateAngleZ = this.entity.sailRotation;
            this.modelWindmill.Vane2rod.rotateAngleZ = this.entity.sailRotation + 1.570796F;
            this.modelWindmill.Vane3rod.rotateAngleZ = this.entity.sailRotation + 3.141593F;
            this.modelWindmill.Vane4rod.rotateAngleZ = this.entity.sailRotation + 4.712389F;
            this.modelWindmill.Vane1main.rotateAngleZ = this.entity.sailRotation;
            this.modelWindmill.Vane2main.rotateAngleZ = this.entity.sailRotation + 1.570796F;
            this.modelWindmill.Vane3main.rotateAngleZ = this.entity.sailRotation + 3.141593F;
            this.modelWindmill.Vane4main.rotateAngleZ = this.entity.sailRotation + 4.712389F;
            this.modelWindmill.WindmillAxle.rotateAngleZ = this.entity.sailRotation;
            this.modelWindmill.render(this.entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
            GL11.glPopMatrix();
        }
    }
    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return null;
    }

}
