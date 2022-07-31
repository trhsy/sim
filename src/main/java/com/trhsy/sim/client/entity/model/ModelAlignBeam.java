package com.trhsy.sim.client.entity.model;

import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * 对齐梁
 */
public class ModelAlignBeam extends ModelBase {

    public ModelRenderer theBeam;
    //public String renderTexture = "";

    public ModelAlignBeam() {
        try {
            //renderTexture = "/mods/sim/textures/models/entityBeam.png";
            theBeam = new ModelRenderer(this, 0, 1); // texture offset:
            theBeam.addBox(-0.4f, 0.0f, -0.4f, 2550, 1, 1);  // len, height, width   , len, height, width
            theBeam.setRotationPoint(-0.0f, 0f, -0.0f);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("初始化对齐梁出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    @Override
    public void render(Entity par1Entity, float par2, float par3, float par4,
                       float par5, float par6, float par7) {
        try {
            theBeam.render(0.1f); //scale
            super.render(par1Entity, par2, par3, par4, par5, par6, par7);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("渲染对齐梁出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }

}