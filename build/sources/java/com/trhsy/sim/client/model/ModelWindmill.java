package com.trhsy.sim.client.model;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * ========================================
 *
 * @ClassName ModelWindmill
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027上午 11:16
 * ========================================
 **/
public class ModelWindmill extends ModelBase {
    public ModelRenderer Vane1rod;
    public ModelRenderer Vane2rod;
    public ModelRenderer Vane3rod;
    public ModelRenderer Vane4rod;
    public ModelRenderer Vane1main;
    public ModelRenderer Vane2main;
    public ModelRenderer Vane3main;
    public ModelRenderer Vane4main;
    public ModelRenderer WindmillAxle;
    ModelRenderer WindmillTop;
    ModelRenderer WindmillMid;
    ModelRenderer WindmillBase;
    public String renderTexture = "/mods/sim_u/textures/models/entityWindmill.png";

    public ModelWindmill() {
        this.textureWidth = 512;
        this.textureHeight = 512;
        this.Vane1rod = new ModelRenderer(this, 200, 500);
        this.Vane1rod.addBox(-96.0F, -1.0F, -1.0F, 96, 2, 2);
        this.Vane1rod.setRotationPoint(0.0F, -105.0F, -43.0F);
        this.Vane1rod.setTextureSize(512, 512);
        this.Vane1rod.mirror = true;
        this.setRotation(this.Vane1rod, 0.0F, 0.0F, 0.0F);
        this.Vane2rod = new ModelRenderer(this, 200, 500);
        this.Vane2rod.addBox(-96.0F, -1.0F, -1.0F, 96, 2, 2);
        this.Vane2rod.setRotationPoint(0.0F, -105.0F, -43.0F);
        this.Vane2rod.setTextureSize(512, 512);
        this.Vane2rod.mirror = true;
        this.setRotation(this.Vane2rod, 0.0F, 0.0F, 1.570796F);
        this.Vane3rod = new ModelRenderer(this, 200, 500);
        this.Vane3rod.addBox(-96.0F, -1.0F, -1.0F, 96, 2, 2);
        this.Vane3rod.setRotationPoint(0.0F, -105.0F, -43.0F);
        this.Vane3rod.setTextureSize(512, 512);
        this.Vane3rod.mirror = true;
        this.setRotation(this.Vane3rod, 0.0F, 0.0F, 3.141593F);
        this.Vane4rod = new ModelRenderer(this, 200, 500);
        this.Vane4rod.addBox(-96.0F, -1.0F, -1.0F, 96, 2, 2);
        this.Vane4rod.setRotationPoint(0.0F, -105.0F, -43.0F);
        this.Vane4rod.setTextureSize(512, 512);
        this.Vane4rod.mirror = true;
        this.setRotation(this.Vane4rod, 0.0F, 0.0F, -1.570796F);
        this.Vane1main = new ModelRenderer(this, 0, 490);
        this.Vane1main.addBox(-95.0F, -20.0F, -1.0F, 93, 19, 1);
        this.Vane1main.setRotationPoint(0.0F, -105.0F, -43.0F);
        this.Vane1main.setTextureSize(512, 512);
        this.Vane1main.mirror = true;
        this.setRotation(this.Vane1main, 0.0872665F, 0.0F, 0.0F);
        this.Vane2main = new ModelRenderer(this, 0, 490);
        this.Vane2main.addBox(-95.0F, -20.0F, -1.0F, 93, 19, 1);
        this.Vane2main.setRotationPoint(0.0F, -105.0F, -43.0F);
        this.Vane2main.setTextureSize(512, 512);
        this.Vane2main.mirror = true;
        this.setRotation(this.Vane2main, 0.0872665F, 0.0F, 1.570796F);
        this.Vane3main = new ModelRenderer(this, 0, 490);
        this.Vane3main.addBox(-95.0F, -20.0F, -1.0F, 93, 19, 1);
        this.Vane3main.setRotationPoint(0.0F, -105.0F, -43.0F);
        this.Vane3main.setTextureSize(512, 512);
        this.Vane3main.mirror = true;
        this.setRotation(this.Vane3main, 0.0872665F, 0.0F, 3.141593F);
        this.Vane4main = new ModelRenderer(this, 0, 490);
        this.Vane4main.addBox(-95.0F, -20.0F, -1.0F, 93, 19, 1);
        this.Vane4main.setRotationPoint(0.0F, -105.0F, -43.0F);
        this.Vane4main.setTextureSize(512, 512);
        this.Vane4main.mirror = true;
        this.setRotation(this.Vane4main, 0.0872665F, 0.0F, -1.570796F);
        this.WindmillAxle = new ModelRenderer(this, 0, 400);
        this.WindmillAxle.addBox(-8.0F, -8.0F, -1.0F, 16, 16, 26);
        this.WindmillAxle.setRotationPoint(0.0F, -105.0F, -41.0F);
        this.WindmillAxle.setTextureSize(512, 512);
        this.WindmillAxle.mirror = true;
        this.setRotation(this.WindmillAxle, 0.0F, 0.0F, 0.0F);
        this.WindmillTop = new ModelRenderer(this, 0, 300);
        this.WindmillTop.addBox(-16.0F, 0.0F, -16.0F, 32, 32, 32);
        this.WindmillTop.setRotationPoint(0.0F, -120.0F, 0.0F);
        this.WindmillTop.setTextureSize(512, 512);
        this.WindmillTop.mirror = true;
        this.setRotation(this.WindmillTop, 0.0F, 0.0F, 0.0F);
        this.WindmillMid = new ModelRenderer(this, 0, 140);
        this.WindmillMid.addBox(-32.0F, 0.0F, -32.0F, 64, 64, 64);
        this.WindmillMid.setRotationPoint(0.0F, -88.0F, 0.0F);
        this.WindmillMid.setTextureSize(512, 512);
        this.WindmillMid.mirror = true;
        this.setRotation(this.WindmillMid, 0.0F, 0.0F, 0.0F);
        this.WindmillBase = new ModelRenderer(this, 0, 0);
        this.WindmillBase.addBox(-40.0F, 0.0F, -40.0F, 80, 48, 80);
        this.WindmillBase.setRotationPoint(0.0F, -24.06667F, 0.0F);
        this.WindmillBase.setTextureSize(512, 512);
        this.WindmillBase.mirror = true;
        this.setRotation(this.WindmillBase, 0.0F, 0.0F, 0.0F);
    }

    @Override
    public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
        this.Vane1rod.render(1.0F);
        this.Vane2rod.render(1.0F);
        this.Vane3rod.render(1.0F);
        this.Vane4rod.render(1.0F);
        this.Vane1main.render(1.0F);
        this.Vane2main.render(1.0F);
        this.Vane3main.render(1.0F);
        this.Vane4main.render(1.0F);
        this.WindmillAxle.render(1.0F);
        this.WindmillTop.render(1.0F);
        this.WindmillMid.render(1.0F);
        this.WindmillBase.render(1.0F);
        super.render(par1Entity, par2, par3, par4, par5, par6, par7);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }
}