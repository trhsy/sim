package com.trhsy.sim.entity.render;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.entity.EntityFolk;
import com.trhsy.sim.loader.ModSimClientLoader;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.entity.util.NpcIdentity;
import com.trhsy.sim.npc.DynamicSkin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;
import net.minecraft.world.storage.loot.LootTableManager;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.Iterator;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.entity.render
 * @ClassName: RenderEntityFolk
 * @Description:
 * @date 2022/10/12 9:45
 */
public class RenderEntityFolk extends RenderBiped<EntityFolk> {
    public static final RenderEntityFolk.Factory FACTORY = new RenderEntityFolk.Factory();

    public RenderEntityFolk(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelBiped(), 1.0F);
        this.addLayer(new LayerHeldItem(this));
    }

    @Override
    @Nonnull
    protected ResourceLocation getEntityTexture(@Nonnull EntityFolk entity) {
        ResourceLocation myTexture=null;
        try{
            String cfi = ModSimClientLoader.getPathFromUUID(entity.getUniqueID());
            if(!StringUtils.isNullOrEmpty(cfi)){
                return new ResourceLocation(ModSim.MODID, "skins/" + cfi);
            }else{
                entity.setDead();
            }
        }catch (Exception e){
            StackTraceElement element = e.getStackTrace()[0];
            ModSimLoader.log.error("渲染实体出错了：" + e.getMessage() + "行数：" + element.getLineNumber());
            try {
                String gend = "";
                //女
                if (entity.theData.gender == 0) {
                    gend = "male0.png";
                } else {
                    gend = "female0.png";
                }

                myTexture = new ResourceLocation(ModSim.MODID, "skins/" + gend);
//                ModSimLoader.skins.add(new DynamicSkin(myTexture,gend));
                return myTexture;
            } catch (Exception var6) {
                return new ResourceLocation("minecraft:steve");
            }
        }
        return  myTexture;
    }
    /**
     * @Author fan
     * @Description //TODO 
     * @Date 16:35 2022/11/3 实体   偏移量       部分刻度
     * @Param [par1Entity, x, y, z, entityYaw, partialTicks]
     * @return void
     **/
    @Override
    public void doRender(EntityFolk par1Entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(par1Entity, x, y, z, entityYaw, partialTicks);
        this.doRenderFolk(par1Entity, x, y, z, entityYaw, partialTicks);
    }
    public void doRenderLiving(EntityLiving entityliving, double d, double d1, double d2, float f, float f1) {
        double d3 = d1 - entityliving.getYOffset();
        this.doRenderLiving(entityliving, d, d3, d2, f, f1);
        this.doRenderFolk((EntityFolk)entityliving, d, d3, d2, f, f1);
    }
    /**
     * 渲染npc
     *
     * @param entityFolk
     * @param x
     * @param y
     * @param z
     * @param entityYaw
     * @param partialTicks
     */
    private void doRenderFolk(EntityFolk entityFolk, double x, double y, double z, float entityYaw, float partialTicks) {
        try {
            float f2 = 1.6F;
            float f3 = 0.01666667F * f2;
            float f6 = 0.2F;
            if (entityFolk != null) {
                double dist = (double) entityFolk.getDistanceToEntity(Minecraft.getMinecraft().thePlayer);
                NpcIdentity data=ModSimClientLoader.getFolkByUUID(entityFolk.getUniqueID());
                if (dist < 20.0D && data != null) {
                    if (Integer.parseInt(data.age) <  Integer.parseInt(data.maturityAge)) {
                        this.displayText(data.name + " (" + data.age + ")", 0.03F, -1, (float) x, (float) y + f3 + f6 - 0.4F, (float) z, entityFolk);
                        this.displayText(data.status, 0.02F, -256, (float) x, (float) y + f3 + f6 - 0.7F, (float) z, entityFolk);
                        this.displayText(data.hunger, 0.02F, -256, (float) x, (float) y + f3 + f6 - 1, (float) z, entityFolk);
                    } else if (dist >= 4) {
                        this.displayText(data.name + " (" + data.age + ")", 0.03F, -1, (float) x, (float) y + f3 + f6, (float) z, entityFolk);
                        this.displayText(data.status, 0.02F, -256, (float) x, (float) y + f3 + f6 - 0.3F, (float) z, entityFolk);
                    } else {
                        this.displayText(data.name + " (" + data.age + ")", 0.03F, -1, (float) x, (float) y + f3 + f6 + 1.5F, (float) z, entityFolk);
                        this.displayText(data.status, 0.02F, -256, (float) x, (float) y + f3 + f6 + 1.2F, (float) z, entityFolk);
                        this.displayText(data.job, 0.02F, -256, (float) x, (float) y + f3 + f6 + 0.9F, (float) z, entityFolk);
                        this.displayText(data.house, 0.02F, -256, (float) x, (float) y + f3 + f6 + 0.6F, (float) z, entityFolk);
                        this.displayText(data.relationship, 0.02F, -256, (float) x, (float) y + f3 + f6 + 0.3F, (float) z, entityFolk);
                        this.displayText(data.hunger, 0.02F, -256, (float) x, (float) y + f3 + f6 + 0.0F, (float) z, entityFolk);
                    }
                }
                ModelBiped biped = (ModelBiped)this.getMainModel();
                if (entityFolk.getHeldItemMainhand()!=null) {
                    biped.rightArmPose = ModelBiped.ArmPose.ITEM;
                } else {
                    biped.rightArmPose = ModelBiped.ArmPose.EMPTY;
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];
            ModSimLoader.log.error("初始化对齐梁出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }
    /**
     * 显示文字
     *
     * @param s
     * @param f
     * @param i
     * @param f1
     * @param f2
     * @param f3
     * @param entitybuilder
     */
    private void displayText(String s, float f, int i, float f1, float f2, float f3, EntityFolk entitybuilder) {
        try {
            FontRenderer fontrenderer = this.getFontRendererFromRenderManager();
            GL11.glPushMatrix();
            GL11.glTranslatef(f1, f2 + 2.3F, f3);
            GL11.glNormal3f(0.0F, 1, 0.0F);
            GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1, 0.0F);
            GL11.glRotatef(this.renderManager.playerViewX, 1, 0.0F, 0.0F);
            GL11.glScalef(-f, -f, f);
            GL11.glDisable(2896);
            GL11.glDepthMask(false);
            GL11.glDisable(2929);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            Tessellator tessellator = Tessellator.getInstance();
            GL11.glDisable(3553);
            tessellator.getBuffer().begin(1, DefaultVertexFormats.POSITION_COLOR);
            int j = fontrenderer.getStringWidth(s) / 2;
            tessellator.getBuffer().putColorRGB_F(0.0F, 0.0F, 0.0F, 0);
            tessellator.getBuffer().sortVertexData((float) (-j - 1), -1, 0);
            tessellator.getBuffer().sortVertexData((float) (-j - 1), 8, 0);
            tessellator.getBuffer().sortVertexData((float) (j + 1), 8, 0);
            tessellator.getBuffer().sortVertexData((float) (j + 1), -1, 0);
            tessellator.draw();
            GL11.glEnable(3553);
            fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 0, i);
            GL11.glEnable(2929);
            GL11.glDepthMask(true);
            fontrenderer.drawString(s, -fontrenderer.getStringWidth(s) / 2, 0, i);
            GL11.glEnable(2896);
            GL11.glDisable(3042);
            GL11.glPopMatrix();
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("初始化对齐梁出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    public static class Factory implements IRenderFactory<EntityFolk> {
        public Factory() {
        }

        @Override
        public Render<? super EntityFolk> createRenderFor(RenderManager manager) {
            return new RenderEntityFolk(manager);
        }
    }
}
