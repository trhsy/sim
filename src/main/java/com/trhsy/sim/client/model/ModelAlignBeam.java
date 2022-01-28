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
 * @ClassName ModelAlignBeam
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027上午 11:12
 * ========================================
 **/
public class ModelAlignBeam extends ModelBase {
    public ModelRenderer theBeam;
    public String renderTexture = "";

    public ModelAlignBeam() {
        this.renderTexture = "/mods/SatscapeSimukraft/textures/models/entityBeam.png";
        this.theBeam = new ModelRenderer(this, 0, 1);
        this.theBeam.addBox(-0.4F, 0.0F, -0.4F, 2550, 1, 1);
        this.theBeam.setRotationPoint(-0.0F, 0.0F, -0.0F);
    }

    @Override
    public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
        this.theBeam.render(0.1F);
        super.render(par1Entity, par2, par3, par4, par5, par6, par7);
    }
}
