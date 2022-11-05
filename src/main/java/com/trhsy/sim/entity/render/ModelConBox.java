package com.trhsy.sim.entity.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * @ClassName ModelConBox
 * @Description todo
 * @Author TRHSY
 * @Date 2022/11/418:21
 **/
public class ModelConBox extends ModelBase {
    public ModelRenderer theConBox = new ModelRenderer(this, 0, 0);
    public String renderTexture = "";

    public ModelConBox() {
        this.theConBox.addBox(-16.0F, 0.0F, -16.0F, 16, 16, 16);
        this.theConBox.setRotationPoint(8.0F, 0.0F, 8.0F);
    }

    public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
        this.theConBox.render(0.0625F);
        super.render(par1Entity, par2, par3, par4, par5, par6, par7);
    }
}
