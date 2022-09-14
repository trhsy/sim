package com.trhsy.sim.client.entity;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.client.entity.model.ModelConBox;
import com.trhsy.sim.common.core.entity.EntityConBox;
import com.trhsy.sim.common.core.entity.V3;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.Map;

/**
 * 渲染 漂浮的建筑箱
 */
@SideOnly(Side.CLIENT)
public class RenderConBox extends Render<EntityConBox> {
    private static final ResourceLocation myTexture = new ResourceLocation(ModSim.MODID, "textures/models/entityConBox.png");

    EntityConBox entity = null;
    ModelConBox modelBox;
    private int actualCount = -1;
    private boolean displayBox = true;

    public RenderConBox(RenderManager renderManager) {
        super(renderManager);
        this.modelBox = new ModelConBox();
    }

    @Override
    public void doRender(EntityConBox var1, double x, double y, double z,
                         float boxYaw, float TextYaw) {
        try {
            this.entity = var1;
            this.renderManager.renderEngine.bindTexture(myTexture);
            x = x + Math.sin(this.entity.boxYaw / 20) / 10;
            z = z + Math.cos(this.entity.boxYaw / 20) / 10;
            y = y + Math.sin(this.entity.boxYaw / 10) / 10;
            GL11.glPushMatrix();
            GL11.glTranslatef((float) x + 0.5f, (float) y + 1.5f, (float) z + 0.5f);
            GL11.glRotatef(this.entity.boxYaw, 0, 1, 0);
            GL11.glScalef(0.5f, 0.5f, 0.5f);
            modelBox.render(this.entity, 0f, 0f, 0f, 0f, 0f, 0f); // no idea what the parms do :-)
            GL11.glPopMatrix();

            if (this.entity.theFolk == null) {
                this.entity.theFolk = EntityConBox.getFolk(new V3(this.entity.posX, this.entity.posY, this.entity.posZ, this.entity.dimension));
            }

            if (this.entity.theFolk != null) {
                if (displayBox) {
                    if (this.entity.theFolk.theBuilding != null) {
                        if (this.entity.theFolk.theBuilding.requirements != null && this.entity.theFolk.theBuilding.requirements.size() > 0) {
                            if (actualCount == -1) {
                                actualCount = 0;
                                for (Map.Entry pairs : this.entity.theFolk.theBuilding.requirements.entrySet()) {

                                    if (pairs.getKey() != null) {
                                        actualCount++;
                                    }
                                }
                            }

                            float offset = (actualCount * 0.2f) + 2.5f;
                            //所需的块
                            displayText(I18n.format("container.sim.render_1") + this.entity.theFolk.theBuilding.displayNameWithoutPK, 0.02F, (float) x + 1, (float) y
                                    + offset, (float) z, 0xEFFFEF);
                            //offset-=0.2f;

                            try {
                                for (Map.Entry pairs : this.entity.theFolk.theBuilding.requirements.entrySet()) {
                                    try {
                                        if (pairs.getValue() != null) {
                                            String st = pairs.getValue().toString();
                                            double stacks = Math.ceil((Double.parseDouble(st)) / 64);
                                            String ss = "";

                                            if ((int) stacks == 0) {
                                                //小于1个堆栈
                                                ss = I18n.format("container.sim.render_2");
                                            } else if ((int) stacks == 1) {
                                                //1堆
                                                ss = I18n.format("container.sim.render_3");
                                            } else {
                                                //堆
                                                ss = (int) stacks + I18n.format("container.sim.render_4");
                                            }

                                            ItemStack is = (ItemStack) pairs.getKey();

                                            if (is.stackSize > 0) {
                                                String itemName = is.getDisplayName();
                                                //橡木
                                                if (itemName.toLowerCase().contentEquals(I18n.format("container.sim.sim_gui_BC9"))) {
                                                    //原木
                                                    itemName = I18n.format("container.sim.sim_gui_BC10");
                                                }
                                                //橡木木板
                                                if (itemName.toLowerCase().contains(I18n.format("container.sim.sim_gui_BC11"))) {
                                                    //木板
                                                    itemName = I18n.format("container.sim.sim_gui_BC12");
                                                }

                                                String line = pairs.getValue() + " x " + itemName
                                                        + " (" + ss + ")";
                                                displayText(line, 0.02F, (float) x + 1, (float) y
                                                        + offset - (actualCount * 0.2f), (float) z, 0xCFFFCF);
                                                offset -= 0.2f;
                                            }
                                        }
                                    } catch (Exception e) {
                                        //e.printStackTrace();
                                        displayBox = false;
                                    }
                                }
                            } catch (Exception e) {
                                if (this.entity != null) {
                                    this.entity.setDead();
                                    return;
                                } // random NPE, no pattern yet
                            }
                        } else {
                            if (this.entity.theFolk.theBuilding.buildingComplete) {
                                displayText(I18n.format("container.sim.render_Building_complete"), 0.02F, (float) x + 1, (float) y + 2, (float) z, 0xAFFFAF);
                            } else {
                                displayText(I18n.format("container.sim.render_No_further_requirements"), 0.02F, (float) x + 1, (float) y + 2, (float) z, 0xAFFFAF);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("渲染漂浮的建筑箱出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    private void displayText(String theString, float scale, float xpos, float ypos, float zpos, int col) {
        try {
            double dist = this.entity.getDistanceToEntity(Minecraft.getMinecraft().thePlayer);
            if (dist > 15) {
                return;
            }

            FontRenderer fontrenderer = getFontRendererFromRenderManager();
            GL11.glPushMatrix();
            GL11.glTranslatef(xpos, ypos, zpos);
            GL11.glNormal3f(0.0F, 1, 0.0F);
            GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1, 0.0F);
            //GL11.glRotatef(renderManager.playerViewX, 1, 0.0F, 0.0F);
            GL11.glScalef(-scale, -scale, scale);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDepthMask(false);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDepthMask(true);
            fontrenderer.drawString(theString, 0, 0, col);
            GL11.glPopMatrix();
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("初始化对齐梁出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }


    @Override
    protected ResourceLocation getEntityTexture(EntityConBox entity) {
        if (this.entity instanceof EntityConBox) {
            return myTexture;
        } else {
            return null;
        }
    }
}

