package com.trhsy.sim.client.render;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.client.model.ModelAlignBeam;
import com.trhsy.sim.common.EntityAlignBeam;
import com.trhsy.sim.common.ModSimukraft;
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
    private static final ResourceLocation myTexture = new ResourceLocation(ModSimukraft.MODID + "", "textures/models/entityBeam.png");
    EntityAlignBeam entity = null;
    ModelAlignBeam modelBeam;

    public RenderAlignBeam(ModelAlignBeam modelBeam) {
        this.modelBeam = modelBeam;
    }
    @Override
    public void doRender(Entity theEntity, double x, double y, double z, float yaw, float pitch) {
        this.entity = (EntityAlignBeam)theEntity;
        this.renderManager.renderEngine.bindTexture(myTexture);
        Float s = 1.0F - (float)Math.sin((double)(System.currentTimeMillis() / 50L)) / 20.0F;
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x + 0.51F, (float)y, (float)z + 0.51F);
        GL11.glRotatef(yaw, 0.0F, 1.0F, 0.0F);
        GL11.glScalef(s, s, s);
        this.modelBeam.render(this.entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        GL11.glPopMatrix();
        if (!this.entity.caption.contentEquals("") && yaw == 0.0F) {
            this.displayText(this.entity.caption, 0.03F, -1, (float)x, (float)y + 0.5F, (float)z, this.entity);
        }

    }

    private void displayText(String s, float f, int i, float f1, float f2, float f3, Entity theBeamEntity) {
        FontRenderer fontrenderer = this.getFontRendererFromRenderManager();
        GL11.glPushMatrix();
        GL11.glTranslatef(f1, f2, f3);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(-f, -f, f);
        GL11.glDisable(2896);
        GL11.glDepthMask(false);
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        Tessellator tessellator = Tessellator.instance;
        GL11.glDisable(3553);
        tessellator.startDrawingQuads();
        int j = fontrenderer.getStringWidth(s) / 2;
        tessellator.setColorRGBA_F(1.0F, 0.0F, 0.0F, 0.25F);
        tessellator.addVertex((double)(-j - 1), -1.0D, 0.0D);
        tessellator.addVertex((double)(-j - 1), 8.0D, 0.0D);
        tessellator.addVertex((double)(j + 1), 8.0D, 0.0D);
        tessellator.addVertex((double)(j + 1), -1.0D, 0.0D);
        tessellator.draw();
        GL11.glEnable(3553);
        fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 0, i);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 0, -1);
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
