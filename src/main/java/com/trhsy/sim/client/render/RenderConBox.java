package com.trhsy.sim.client.render;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.client.model.ModelConBox;
import com.trhsy.sim.common.ModSim;
import com.trhsy.sim.common.entity.EntityConBox;
import com.trhsy.sim.common.entity.V3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.Iterator;
import java.util.Map;

/**
 * ========================================
 *
 * @ClassName RenderConBox
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027上午 11:20
 * ========================================
 **/
public class RenderConBox extends Render{
    private static final ResourceLocation myTexture = new ResourceLocation(ModSim.MODID + "", "textures/models/entityConBox.png");
    EntityConBox entity = null;
    ModelConBox modelBox;
    private int actualCount = -1;
    private boolean displayBox = true;

    public RenderConBox(ModelConBox modelBox) {
        this.modelBox = modelBox;
    }

    @Override
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
        if (this.entity.theFolk == null) {
            this.entity.theFolk = EntityConBox.getFolk(new V3(this.entity.posX, this.entity.posY, this.entity.posZ, this.entity.dimension));
        }

        if (this.entity.theFolk != null && this.displayBox && this.entity.theFolk.theBuilding != null) {
            if (this.entity.theFolk.theBuilding.requirements != null && this.entity.theFolk.theBuilding.requirements.size() > 0) {
                if (this.actualCount == -1) {
                    Iterator it = this.entity.theFolk.theBuilding.requirements.entrySet().iterator();
                    this.actualCount = 0;

                    while(it.hasNext()) {
                        Map.Entry pairs = (Map.Entry)it.next();
                        if (pairs.getKey() != null) {
                            ++this.actualCount;
                        }
                    }
                }

                float offset = (float)this.actualCount * 0.2F + 2.5F;
                this.displayText("Blocks required for " + this.entity.theFolk.theBuilding.displayNameWithoutPK, 0.02F, (float)x + 1.0F, (float)y + offset, (float)z, 15728623);

                try {
                    Iterator it = this.entity.theFolk.theBuilding.requirements.entrySet().iterator();

                    while(it.hasNext()) {
                        try {
                            Map.Entry pairs = (Map.Entry)it.next();
                            if (pairs.getValue() != null) {
                                String st = pairs.getValue().toString();
                                double stacks = Math.ceil(Double.parseDouble(st) / 64.0D);
                                String ss = "";
                                if ((int)stacks == 0) {
                                    ss = "less than 1 stack";
                                } else if ((int)stacks == 1) {
                                    ss = "1 stack";
                                } else {
                                    ss = (int)stacks + " stacks";
                                }

                                ItemStack is = (ItemStack)pairs.getKey();
                                if (is.stackSize > 0) {
                                    String itemName = is.getDisplayName();
                                    if (itemName.toLowerCase().contentEquals("oak wood")) {
                                        itemName = "Logs";
                                    }

                                    if (itemName.toLowerCase().contains("oak wood planks")) {
                                        itemName = "Planks";
                                    }

                                    String line = pairs.getValue() + " x " + itemName + " (" + ss + ")";
                                    this.displayText(line, 0.02F, (float)x + 1.0F, (float)y + offset - (float)this.actualCount * 0.2F, (float)z, 13631439);
                                    offset -= 0.2F;
                                }
                            }
                        } catch (Exception var20) {
                            var20.printStackTrace();
                            this.displayBox = false;
                        }
                    }
                } catch (Exception var21) {
                    if (this.entity != null) {
                        this.entity.setDead();
                        return;
                    }
                }
            } else if (this.entity.theFolk.theBuilding.buildingComplete) {
                this.displayText("Building complete", 0.02F, (float)x + 1.0F, (float)y + 2.0F, (float)z, 11534255);
            } else {
                this.displayText("No further requirements", 0.02F, (float)x + 1.0F, (float)y + 2.0F, (float)z, 11534255);
            }
        }

    }

    private void displayText(String theString, float scale, float xpos, float ypos, float zpos, int col) {
        double dist = (double)this.entity.getDistanceToEntity(Minecraft.getMinecraft().thePlayer);
        if (!(dist > 15.0D)) {
            FontRenderer fontrenderer = this.getFontRendererFromRenderManager();
            GL11.glPushMatrix();
            GL11.glTranslatef(xpos, ypos, zpos);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
            GL11.glScalef(-scale, -scale, scale);
            GL11.glDisable(2896);
            GL11.glDepthMask(false);
            GL11.glDisable(2929);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glEnable(3553);
            GL11.glEnable(2929);
            GL11.glDepthMask(true);
            fontrenderer.drawString(theString, 0, 0, col);
            GL11.glPopMatrix();
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return entity instanceof EntityConBox ? myTexture : null;
    }


}
