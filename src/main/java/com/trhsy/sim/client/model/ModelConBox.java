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
 * @ClassName ModelConBox
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027上午 11:15
 * ========================================
 **/
public class ModelConBox extends ModelBase {
    public ModelRenderer theConBox = new ModelRenderer(this, 0, 0);
    public String renderTexture = "";

    public ModelConBox() {
        this.theConBox.func_78789_a(-16.0F, 0.0F, -16.0F, 16, 16, 16);
        this.theConBox.func_78793_a(8.0F, 0.0F, 8.0F);
    }

    public void func_78088_a(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
        this.theConBox.func_78785_a(0.0625F);
        super.func_78088_a(par1Entity, par2, par3, par4, par5, par6, par7);
    }
}