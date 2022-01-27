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
    public String renderTexture = "/mods/SatscapeLSD/textures/models/entityWindmill.png";

    public ModelWindmill() {
        this.field_78090_t = 512;
        this.field_78089_u = 512;
        this.Vane1rod = new ModelRenderer(this, 200, 500);
        this.Vane1rod.func_78789_a(-96.0F, -1.0F, -1.0F, 96, 2, 2);
        this.Vane1rod.func_78793_a(0.0F, -105.0F, -43.0F);
        this.Vane1rod.func_78787_b(512, 512);
        this.Vane1rod.field_78809_i = true;
        this.setRotation(this.Vane1rod, 0.0F, 0.0F, 0.0F);
        this.Vane2rod = new ModelRenderer(this, 200, 500);
        this.Vane2rod.func_78789_a(-96.0F, -1.0F, -1.0F, 96, 2, 2);
        this.Vane2rod.func_78793_a(0.0F, -105.0F, -43.0F);
        this.Vane2rod.func_78787_b(512, 512);
        this.Vane2rod.field_78809_i = true;
        this.setRotation(this.Vane2rod, 0.0F, 0.0F, 1.570796F);
        this.Vane3rod = new ModelRenderer(this, 200, 500);
        this.Vane3rod.func_78789_a(-96.0F, -1.0F, -1.0F, 96, 2, 2);
        this.Vane3rod.func_78793_a(0.0F, -105.0F, -43.0F);
        this.Vane3rod.func_78787_b(512, 512);
        this.Vane3rod.field_78809_i = true;
        this.setRotation(this.Vane3rod, 0.0F, 0.0F, 3.141593F);
        this.Vane4rod = new ModelRenderer(this, 200, 500);
        this.Vane4rod.func_78789_a(-96.0F, -1.0F, -1.0F, 96, 2, 2);
        this.Vane4rod.func_78793_a(0.0F, -105.0F, -43.0F);
        this.Vane4rod.func_78787_b(512, 512);
        this.Vane4rod.field_78809_i = true;
        this.setRotation(this.Vane4rod, 0.0F, 0.0F, -1.570796F);
        this.Vane1main = new ModelRenderer(this, 0, 490);
        this.Vane1main.func_78789_a(-95.0F, -20.0F, -1.0F, 93, 19, 1);
        this.Vane1main.func_78793_a(0.0F, -105.0F, -43.0F);
        this.Vane1main.func_78787_b(512, 512);
        this.Vane1main.field_78809_i = true;
        this.setRotation(this.Vane1main, 0.0872665F, 0.0F, 0.0F);
        this.Vane2main = new ModelRenderer(this, 0, 490);
        this.Vane2main.func_78789_a(-95.0F, -20.0F, -1.0F, 93, 19, 1);
        this.Vane2main.func_78793_a(0.0F, -105.0F, -43.0F);
        this.Vane2main.func_78787_b(512, 512);
        this.Vane2main.field_78809_i = true;
        this.setRotation(this.Vane2main, 0.0872665F, 0.0F, 1.570796F);
        this.Vane3main = new ModelRenderer(this, 0, 490);
        this.Vane3main.func_78789_a(-95.0F, -20.0F, -1.0F, 93, 19, 1);
        this.Vane3main.func_78793_a(0.0F, -105.0F, -43.0F);
        this.Vane3main.func_78787_b(512, 512);
        this.Vane3main.field_78809_i = true;
        this.setRotation(this.Vane3main, 0.0872665F, 0.0F, 3.141593F);
        this.Vane4main = new ModelRenderer(this, 0, 490);
        this.Vane4main.func_78789_a(-95.0F, -20.0F, -1.0F, 93, 19, 1);
        this.Vane4main.func_78793_a(0.0F, -105.0F, -43.0F);
        this.Vane4main.func_78787_b(512, 512);
        this.Vane4main.field_78809_i = true;
        this.setRotation(this.Vane4main, 0.0872665F, 0.0F, -1.570796F);
        this.WindmillAxle = new ModelRenderer(this, 0, 400);
        this.WindmillAxle.func_78789_a(-8.0F, -8.0F, -1.0F, 16, 16, 26);
        this.WindmillAxle.func_78793_a(0.0F, -105.0F, -41.0F);
        this.WindmillAxle.func_78787_b(512, 512);
        this.WindmillAxle.field_78809_i = true;
        this.setRotation(this.WindmillAxle, 0.0F, 0.0F, 0.0F);
        this.WindmillTop = new ModelRenderer(this, 0, 300);
        this.WindmillTop.func_78789_a(-16.0F, 0.0F, -16.0F, 32, 32, 32);
        this.WindmillTop.func_78793_a(0.0F, -120.0F, 0.0F);
        this.WindmillTop.func_78787_b(512, 512);
        this.WindmillTop.field_78809_i = true;
        this.setRotation(this.WindmillTop, 0.0F, 0.0F, 0.0F);
        this.WindmillMid = new ModelRenderer(this, 0, 140);
        this.WindmillMid.func_78789_a(-32.0F, 0.0F, -32.0F, 64, 64, 64);
        this.WindmillMid.func_78793_a(0.0F, -88.0F, 0.0F);
        this.WindmillMid.func_78787_b(512, 512);
        this.WindmillMid.field_78809_i = true;
        this.setRotation(this.WindmillMid, 0.0F, 0.0F, 0.0F);
        this.WindmillBase = new ModelRenderer(this, 0, 0);
        this.WindmillBase.func_78789_a(-40.0F, 0.0F, -40.0F, 80, 48, 80);
        this.WindmillBase.func_78793_a(0.0F, -24.06667F, 0.0F);
        this.WindmillBase.func_78787_b(512, 512);
        this.WindmillBase.field_78809_i = true;
        this.setRotation(this.WindmillBase, 0.0F, 0.0F, 0.0F);
    }

    public void func_78088_a(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
        this.Vane1rod.func_78785_a(1.0F);
        this.Vane2rod.func_78785_a(1.0F);
        this.Vane3rod.func_78785_a(1.0F);
        this.Vane4rod.func_78785_a(1.0F);
        this.Vane1main.func_78785_a(1.0F);
        this.Vane2main.func_78785_a(1.0F);
        this.Vane3main.func_78785_a(1.0F);
        this.Vane4main.func_78785_a(1.0F);
        this.WindmillAxle.func_78785_a(1.0F);
        this.WindmillTop.func_78785_a(1.0F);
        this.WindmillMid.func_78785_a(1.0F);
        this.WindmillBase.func_78785_a(1.0F);
        super.func_78088_a(par1Entity, par2, par3, par4, par5, par6, par7);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z) {
        model.field_78795_f = x;
        model.field_78796_g = y;
        model.field_78808_h = z;
    }
}