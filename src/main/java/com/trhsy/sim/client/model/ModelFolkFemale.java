package com.trhsy.sim.client.model;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.common.EntityFolk;
import com.trhsy.sim.common.entity.FolkData;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * ========================================
 *
 * @ClassName ModelFolkFemale
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027上午 11:15
 * ========================================
 **/
public class ModelFolkFemale extends ModelBiped {
    public ModelRenderer rightTit = new ModelRenderer(this, 19, 19);
    public ModelRenderer leftTit;
    public ModelRenderer pregnant;

    public ModelFolkFemale() {
        this.rightTit.addBox(0.0F, 0.0F, 0.0F, 3, 3, 4);
        this.rightTit.setRotationPoint(0.5F, 1.7F, -4.0F);
        this.leftTit = new ModelRenderer(this, 19, 19);
        this.leftTit.addBox(0.0F, 0.0F, 0.0F, 3, 3, 4);
        this.leftTit.setRotationPoint(-3.5F, 1.7F, -4.0F);
        this.pregnant = new ModelRenderer(this, 18, 20);
        this.pregnant.addBox(0.0F, 0.0F, -1.0F, 5, 6, 5);
        this.pregnant.setRotationPoint(-2.5F, 5.0F, -4.0F);
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        EntityFolk ef = (EntityFolk)entity;
        FolkData fd = ef.theData;
        if (fd != null) {
            if (fd.age < 18) {
                this.isChild = true;
            }

            if (fd.gender == 1 && !this.isChild) {
                this.rightTit.render(f5);
                this.leftTit.render(f5);
            }

            if (fd.pregnancyStage > 0.0F) {
                this.pregnant.render(f5);
            }
        }

        super.render(entity, f, f1, f2, f3, f4, f5);
        this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
    }
}