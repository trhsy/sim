package com.trhsy.sim.client.entity;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.client.entity.model.ModelAlignBeam;
import com.trhsy.sim.client.entity.model.ModelFolkFemale;
import com.trhsy.sim.common.entity.EntityAlignBeam;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * 渲染对齐梁
 */
public class RenderAlignBeam extends Render<EntityAlignBeam> {
    private static final ResourceLocation myTexture = new ResourceLocation(ModSim.MODID, "textures/models/entityBeam.png");
    EntityAlignBeam entity = null;
    ModelAlignBeam modelBeam;

    public RenderAlignBeam(RenderManager renderManager) {
        super(renderManager);
        this.modelBeam = new ModelAlignBeam();
    }

    /**
     * @param entity
     * @return
     */
    @Override
    protected ResourceLocation getEntityTexture(EntityAlignBeam entity) {
        if (entity instanceof EntityAlignBeam) {
            return myTexture;
        } else {
            return null;
        }
    }

    @Override
    public void doRender(EntityAlignBeam theEntity, double x, double y, double z, float yaw, float pitch) {
        try {
            this.entity = theEntity;

            //MC 1.6.2
            //int texture = renderManager.renderEngine.getTexture(modelBeam.renderTexture);
            //renderManager.renderEngine.bindTexture(texture);
            //GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
            this.renderManager.renderEngine.bindTexture(myTexture);
            //脉冲光束
            Float s = 1 - ((float) Math.sin(System.currentTimeMillis() / 50) / 20);

            GL11.glPushMatrix();
            //GL11.glDisable(2896); //禁用照明
            GL11.glTranslatef((float) x + 0.51f, (float) y, (float) z + 0.51f);
            GL11.glRotatef(yaw, 0, 1, 0);
            GL11.glScalef(s, s, s);
            // no idea what the parms do :-)
            modelBeam.render(entity, 0f, 0f, 0f, 0f, 0f, 0f);
            GL11.glPopMatrix();

            if (!entity.caption.contentEquals("") && yaw == 0) {
                displayText(entity.caption, 0.03F, 0xFFFFFFFF, (float) x, (float) y + 0.5f, (float) z, entity);
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("渲染对齐梁出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

    /**
     * 显示文本
     * @param s
     * @param f
     * @param i
     * @param f1
     * @param f2
     * @param f3
     * @param theBeamEntity
     */
    private void displayText(String s, float f, int i, float f1, float f2,
                             float f3, Entity theBeamEntity) {
        try {
            FontRenderer fontrenderer = getFontRendererFromRenderManager();
            GL11.glPushMatrix();
            GL11.glTranslatef(f1, f2, f3);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
            GL11.glScalef(-f, -f, f);
            GL11.glDisable(2896);
            GL11.glDepthMask(false);
            GL11.glDisable(2929);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            Tessellator tessellator = Tessellator.getInstance();
            GL11.glDisable(3553);
            //tessellator.startDrawingQuads();
            WorldRenderer worldRenderer = tessellator.getWorldRenderer();
            worldRenderer.finishDrawing();
            int j = fontrenderer.getStringWidth(s) / 2;
            //红色和部分透明背景色
            worldRenderer.putColorRGB_F(1.0F, 0.0F, 0.0F, (int) 0.25F);
            int[] v1 = {-j - 1, -1, 0};
            worldRenderer.addVertexData(v1);
            int[] v2 = {-j - 1, 8, 0};
            worldRenderer.addVertexData(v2);
            int[] v3 = {-j + 1, 8, 0};
            worldRenderer.addVertexData(v3);
            int[] v4 = {-j + 1, -1, 0};
            worldRenderer.addVertexData(v4);
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
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("渲染对齐梁出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }


}