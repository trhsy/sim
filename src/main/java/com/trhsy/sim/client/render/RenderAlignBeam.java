package com.trhsy.sim.client.render;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.client.model.ModelAlignBeam;
import com.trhsy.sim.common.EntityAlignBeam;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * ========================================
 *
 * @ClassName RenderAlignBeam
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027上午 11:18
 * ========================================
 **/
public class RenderAlignBeam extends Render {
    private static final ResourceLocation myTexture = new ResourceLocation("satscapesimukraft", "textures/models/entityBeam.png");
    EntityAlignBeam entity = null;
    ModelAlignBeam modelBeam;

    public RenderAlignBeam(ModelAlignBeam modelBeam) {
        this.modelBeam = modelBeam;
    }
    @Override
    public void doRender(Entity theEntity, double x, double y, double z, float yaw, float pitch) {
        this.entity = (EntityAlignBeam)theEntity;
        this.field_76990_c.field_78724_e.func_110577_a(myTexture);
        Float s = 1.0F - (float)Math.sin((double)(System.currentTimeMillis() / 50L)) / 20.0F;
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x + 0.51F, (float)y, (float)z + 0.51F);
        GL11.glRotatef(yaw, 0.0F, 1.0F, 0.0F);
        GL11.glScalef(s, s, s);
        this.modelBeam.func_78088_a(this.entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        GL11.glPopMatrix();
        if (!this.entity.caption.contentEquals("") && yaw == 0.0F) {
            this.displayText(this.entity.caption, 0.03F, -1, (float)x, (float)y + 0.5F, (float)z, this.entity);
        }

    }

    private void displayText(String s, float f, int i, float f1, float f2, float f3, Entity theBeamEntity) {
        FontRenderer fontrenderer = this.func_76983_a();
        GL11.glPushMatrix();
        GL11.glTranslatef(f1, f2, f3);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-this.field_76990_c.field_78735_i, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(this.field_76990_c.field_78732_j, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(-f, -f, f);
        GL11.glDisable(2896);
        GL11.glDepthMask(false);
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        Tessellator tessellator = Tessellator.field_78398_a;
        GL11.glDisable(3553);
        tessellator.func_78382_b();
        int j = fontrenderer.func_78256_a(s) / 2;
        tessellator.func_78369_a(1.0F, 0.0F, 0.0F, 0.25F);
        tessellator.func_78377_a((double)(-j - 1), -1.0D, 0.0D);
        tessellator.func_78377_a((double)(-j - 1), 8.0D, 0.0D);
        tessellator.func_78377_a((double)(j + 1), 8.0D, 0.0D);
        tessellator.func_78377_a((double)(j + 1), -1.0D, 0.0D);
        tessellator.func_78381_a();
        GL11.glEnable(3553);
        fontrenderer.func_78276_b(s, -fontrenderer.func_78256_a(s) / 2, 0, i);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        fontrenderer.func_78276_b(s, -fontrenderer.func_78256_a(s) / 2, 0, -1);
        GL11.glEnable(2896);
        GL11.glDisable(3042);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return entity instanceof EntityAlignBeam ? myTexture : null;
    }


}
