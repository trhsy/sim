package com.trhsy.sim.entity.render;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.entity.EntityConBox;
import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.npc.build.BlueprintRequirements;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import org.lwjgl.opengl.GL11;

/**
 * @ClassName RenderConBox
 * @Description todo
 * @Author TRHSY
 * @Date 2022/11/418:17
 **/
public class RenderConBox extends Render {
    public static final RenderConBox.Factory FACTORY = new RenderConBox.Factory();
    private static final ResourceLocation myTexture = new ResourceLocation(ModSim.MODID, "textures/models/entityConBox.png");
    EntityConBox entity = null;
    ModelConBox modelBox;
    private int actualCount = -1;
    private boolean displayBox = true;

    public RenderConBox(RenderManager renderManager, ModelConBox modelBox) {
        super(renderManager);
        this.modelBox = modelBox;
    }

    public void doRender(Entity var1, double x, double y, double z, float boxYaw, float TextYaw) {
        this.entity = (EntityConBox)var1;
        this.renderManager.renderEngine.bindTexture(myTexture);
        x += Math.sin((double)(this.entity.boxYaw / 20.0F)) / 10.0D;
        z += Math.cos((double)(this.entity.boxYaw / 20.0F)) / 10.0D;
        y += Math.sin((double)(this.entity.boxYaw / 10.0F)) / 10.0D;
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x + 0.5F, (float)y + 1.5F, (float)z + 0.5F);
        GL11.glRotatef(this.entity.boxYaw, 0.0F, 1.0F, 0.0F);
        GL11.glScalef(0.5F, 0.5F, 0.5F);
        this.modelBox.render(this.entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        GL11.glPopMatrix();
        if (this.entity != null && this.displayBox) {
            BlueprintRequirements requirements = ModSimLoader.getRequirementsByUUID(this.entity.getUniqueID());
            float offset = 2.5F;
            if (requirements != null) {
                String[] var12 = requirements.getRequirements();
                int var13 = var12.length;

                for(int var14 = 0; var14 < var13; ++var14) {
                    String line = var12[var14];
                    this.displayText(line, 0.02F, (float)x + 1.0F, (float)y + offset, (float)z, 13631439);
                    offset -= 0.2F;
                }
            }

        }
    }

    private void displayText(String s, float f, float f1, float f2, float f3, int i) {
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
        tessellator.getBuffer().begin(1, DefaultVertexFormats.POSITION_COLOR);
        int j = fontrenderer.getStringWidth(s) / 2;
        tessellator.getBuffer().color(0.0F, 0.0F, 0.0F, 0.25F);
        tessellator.getBuffer().pos((double)(-j - 1), -1.0D, 0.0D);
        tessellator.getBuffer().pos((double)(-j - 1), 8.0D, 0.0D);
        tessellator.getBuffer().pos((double)(j + 1), 8.0D, 0.0D);
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

    protected ResourceLocation getEntityTexture(Entity entity) {
        return myTexture;
    }

    public static class Factory implements IRenderFactory<EntityConBox> {
        public Factory() {
        }

        public Render<? super EntityConBox> createRenderFor(RenderManager manager) {
            return new RenderConBox(manager, new ModelConBox());
        }
    }
}
