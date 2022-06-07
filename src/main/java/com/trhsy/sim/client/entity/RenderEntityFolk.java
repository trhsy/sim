package com.trhsy.sim.client.entity;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.entity.EntityFolk;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

/**
 * @ClassName RenderEntityFolk
 * @Description todo 男性NPC
 * @Author Tian
 * @Date 2022/5/2216:56
 **/
@SideOnly(Side.CLIENT)
public class RenderEntityFolk extends RenderBiped<EntityFolk> {
    public RenderEntityFolk(RenderManager renderManager) {
        /**
         * @Author fan
         * @Description //TODO
         * Minecraft的渲染管理器
         * 实体生物的实体渲染模型
         * @Date 17:44 2022/5/22
         * @Param [renderManager]
         * @return 
         **/
        super(renderManager, new ModelBiped(), 0.5F);
    }

    protected ResourceLocation getEntityTexture(EntityFolk entity) {
        if (entity instanceof EntityFolk) {
            EntityFolk theFolk = (EntityFolk) entity;
            ResourceLocation myTexture = new ResourceLocation(ModSim.MODID + "", "skins/" + theFolk.getTexture());
            return myTexture;
        } else {
            return null;
        }
    }

    @Override
    protected void preRenderCallback(EntityFolk entity, float partialTickTime) {
        GlStateManager.scale(1F, 1F, 1F);
    }

    @Override
    public void doRender(EntityFolk par1Entity, double par2, double par4, double par6, float par8, float par9) {
        super.doRender(par1Entity, par2, par4, par6, par8, par9);
        this.doRenderFolk((EntityFolk) par1Entity, par2, par4, par6, par8, par9);
    }

    public void doRenderLiving(EntityLiving entityliving, double d, double d1, double d2, float f, float f1) {
        double d3 = d1 - (double) entityliving.getYOffset();
        this.doRenderLiving(entityliving, d, d3, d2, f, f1);
        this.doRenderFolk((EntityFolk) entityliving, d, d3, d2, f, f1);
    }

    private void doRenderFolk(EntityFolk entityFolk, double d, double d1, double d2, float f, float f1) {
        float f2 = 1.6F;
        float f3 = 0.01666667F * f2;
        float f6 = 0.2F;
        if (entityFolk.theData != null && entityFolk != null) {
            double dist = (double) entityFolk.getDistanceToEntity(Minecraft.getMinecraft().thePlayer);
            if (dist < 40) {
                if (entityFolk.theData.age < 18) {
                    this.displayText(entityFolk.theData.name + " (" + entityFolk.theData.age + ")", 0.03F, -1, (float) d, (float) d1 + f3 + f6 - 0.4F, (float) d2, entityFolk);
                    this.displayText(entityFolk.theData.statusText, 0.02F, -256, (float) d, (float) d1 + f3 + f6 - 0.7F, (float) d2, entityFolk);
                    this.displayText(entityFolk.theData.status4, 0.02F, -256, (float) d, (float) d1 + f3 + f6 - 1.0F, (float) d2, entityFolk);
                } else if (dist >= 4) {
                    this.displayText(entityFolk.theData.name + " (" + entityFolk.theData.age + ")", 0.03F, -1, (float) d, (float) d1 + f3 + f6, (float) d2, entityFolk);
                    this.displayText(entityFolk.theData.statusText, 0.02F, -256, (float) d, (float) d1 + f3 + f6 - 0.3F, (float) d2, entityFolk);
                } else {
                    this.displayText(entityFolk.theData.name + " (" + entityFolk.theData.age + ")", 0.03F, -1, (float) d, (float) d1 + f3 + f6 + 1.5F, (float) d2, entityFolk);
                    this.displayText(entityFolk.theData.statusText, 0.02F, -256, (float) d, (float) d1 + f3 + f6 + 1.2F, (float) d2, entityFolk);
                    this.displayText(entityFolk.theData.status1, 0.02F, -256, (float) d, (float) d1 + f3 + f6 + 0.9F, (float) d2, entityFolk);
                    this.displayText(entityFolk.theData.status2, 0.02F, -256, (float) d, (float) d1 + f3 + f6 + 0.6F, (float) d2, entityFolk);
                    this.displayText(entityFolk.theData.status3, 0.02F, -256, (float) d, (float) d1 + f3 + f6 + 0.3F, (float) d2, entityFolk);
                    this.displayText(entityFolk.theData.status4, 0.02F, -256, (float) d, (float) d1 + f3 + f6 + 0.0F, (float) d2, entityFolk);
                }
            }
        }

    }

    private void displayText(String s, float f, int i, float f1, float f2, float f3, EntityFolk entitybuilder) {
        FontRenderer fontrenderer = this.getFontRendererFromRenderManager();
        GL11.glPushMatrix();
        GL11.glTranslatef(f1, f2 + 2.3F, f3);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(-f, -f, f);
        GL11.glDisable(2896);
        GL11.glDepthMask(false);
        GL11.glDisable(2929);
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        Tessellator tessellator = Tessellator.getInstance();
        GL11.glDisable(3553);
        /*tessellator.startDrawingQuads();
        int j = fontrenderer.getStringWidth(s) / 2;
        tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
        tessellator.addVertex((double)(-j - 1), -1, 0);
        tessellator.addVertex((double)(-j - 1), 8, 0);
        tessellator.addVertex((double)(j + 1), 8, 0);
        tessellator.addVertex((double)(j + 1), -1, 0);*/
        tessellator.draw();
        GL11.glEnable(3553);
        fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 0, i);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 0, i);
        GL11.glEnable(2896);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
}
