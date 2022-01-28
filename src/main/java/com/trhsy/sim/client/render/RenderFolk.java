package com.trhsy.sim.client.render;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.common.EntityFolk;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * ========================================
 *
 * @ClassName RenderFolk
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027上午 11:21
 * ========================================
 **/
@SideOnly(Side.CLIENT)
public class RenderFolk extends RenderBiped {
    public RenderFolk(ModelBiped modelbase) {
        super(modelbase, 1.0F);
    }

    protected ResourceLocation func_110775_a(Entity entity) {
        if (entity instanceof EntityFolk) {
            EntityFolk theFolk = (EntityFolk)entity;
            ResourceLocation myTexture = new ResourceLocation(ModSimukraft.MODID + "", "skins/" + theFolk.getTexture());
            return myTexture;
        } else {
            return null;
        }
    }

    public void func_76986_a(Entity par1Entity, double par2, double par4, double par6, float par8, float par9) {
        super.func_76986_a(par1Entity, par2, par4, par6, par8, par9);
        this.doRenderFolk((EntityFolk)par1Entity, par2, par4, par6, par8, par9);
    }

    public void doRenderLiving(EntityLiving entityliving, double d, double d1, double d2, float f, float f1) {
        double d3 = d1 - (double)entityliving.field_70129_M;
        this.doRenderLiving(entityliving, d, d3, d2, f, f1);
        this.doRenderFolk((EntityFolk)entityliving, d, d3, d2, f, f1);
    }

    private void doRenderFolk(EntityFolk entityFolk, double d, double d1, double d2, float f, float f1) {
        float f2 = 1.6F;
        float f3 = 0.01666667F * f2;
        float f6 = 0.2F;
        if (entityFolk.theData != null && entityFolk != null) {
            double dist = (double)entityFolk.getDistanceToEntity(Minecraft.getMinecraft().thePlayer);
            if (dist < 40.0D) {
                if (entityFolk.theData.age < 18) {
                    this.displayText(entityFolk.theData.name + " (" + entityFolk.theData.age + ")", 0.03F, -1, (float)d, (float)d1 + f3 + f6 - 0.4F, (float)d2, entityFolk);
                    this.displayText(entityFolk.theData.statusText, 0.02F, -256, (float)d, (float)d1 + f3 + f6 - 0.7F, (float)d2, entityFolk);
                    this.displayText(entityFolk.theData.status4, 0.02F, -256, (float)d, (float)d1 + f3 + f6 - 1.0F, (float)d2, entityFolk);
                } else if (dist >= 4.0D) {
                    this.displayText(entityFolk.theData.name + " (" + entityFolk.theData.age + ")", 0.03F, -1, (float)d, (float)d1 + f3 + f6, (float)d2, entityFolk);
                    this.displayText(entityFolk.theData.statusText, 0.02F, -256, (float)d, (float)d1 + f3 + f6 - 0.3F, (float)d2, entityFolk);
                } else {
                    this.displayText(entityFolk.theData.name + " (" + entityFolk.theData.age + ")", 0.03F, -1, (float)d, (float)d1 + f3 + f6 + 1.5F, (float)d2, entityFolk);
                    this.displayText(entityFolk.theData.statusText, 0.02F, -256, (float)d, (float)d1 + f3 + f6 + 1.2F, (float)d2, entityFolk);
                    this.displayText(entityFolk.theData.status1, 0.02F, -256, (float)d, (float)d1 + f3 + f6 + 0.9F, (float)d2, entityFolk);
                    this.displayText(entityFolk.theData.status2, 0.02F, -256, (float)d, (float)d1 + f3 + f6 + 0.6F, (float)d2, entityFolk);
                    this.displayText(entityFolk.theData.status3, 0.02F, -256, (float)d, (float)d1 + f3 + f6 + 0.3F, (float)d2, entityFolk);
                    this.displayText(entityFolk.theData.status4, 0.02F, -256, (float)d, (float)d1 + f3 + f6 + 0.0F, (float)d2, entityFolk);
                }
            }
        }

    }

    private void displayText(String s, float f, int i, float f1, float f2, float f3, EntityFolk entitybuilder) {
        FontRenderer fontrenderer = this.func_76983_a();
        GL11.glPushMatrix();
        GL11.glTranslatef(f1, f2 + 2.3F, f3);
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
        tessellator.func_78369_a(0.0F, 0.0F, 0.0F, 0.25F);
        tessellator.func_78377_a((double)(-j - 1), -1.0D, 0.0D);
        tessellator.func_78377_a((double)(-j - 1), 8.0D, 0.0D);
        tessellator.func_78377_a((double)(j + 1), 8.0D, 0.0D);
        tessellator.func_78377_a((double)(j + 1), -1.0D, 0.0D);
        tessellator.func_78381_a();
        GL11.glEnable(3553);
        fontrenderer.func_78276_b(s, -fontrenderer.func_78256_a(s) / 2, 0, i);
        GL11.glEnable(2929);
        GL11.glDepthMask(true);
        fontrenderer.func_78276_b(s, -fontrenderer.func_78256_a(s) / 2, 0, i);
        GL11.glEnable(2896);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }
}
