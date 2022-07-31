package com.trhsy.sim.client.entity.model;

import com.trhsy.sim.common.entity.EntityFolk;
import com.trhsy.sim.common.entity.FolkData;
import com.trhsy.sim.common.loader.ModSimReloaded;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

/**
 * @ClassName ModelFolkFemale
 * 此类也用于男性模型，但不会渲染乳房！：-）
 * @Description todo 女性npc
 * @Author Tian
 * @Date 2022/6/515:43
 **/
public class ModelFolkFemale extends ModelBiped {
    //女性右侧乳房
    public ModelRenderer rightTit;
    //女性左侧乳房
    public ModelRenderer leftTit;
    //怀孕
    public ModelRenderer pregnant;

    public ModelFolkFemale() {
        super();
        try {
            //变量名：-）
            this.rightTit = new ModelRenderer(this, 19, 19);
            this.rightTit.addBox(0.0F, 0.0F, 0.0F, 3, 3, 5);
            //设置旋转点
            this.rightTit.setRotationPoint(0.5F, 1.7F, -4.0F);

            this.leftTit = new ModelRenderer(this, 19, 19);
            this.leftTit.addBox(0.0F, 0.0F, 0.0F, 3, 3, 5);
            this.leftTit.setRotationPoint(-3.5F, 1.7F, -4.0F);
            this.pregnant = new ModelRenderer(this, 18, 20);
            this.pregnant.addBox(0.0F, 0.0F, -1.0F, 5, 6, 5);
            this.pregnant.setRotationPoint(-2.5F, 5.0F, -4.0F);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("初始化NPC出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
        try {
            EntityFolk ef = (EntityFolk)entity;
            FolkData fd = ef.theData;
            if (fd != null) {
                //如果未成年返回儿童
                if (fd.age < 18) {
                    this.isChild = true;
                }
                //性别是女并且不是儿童
                if (fd.gender == 1 && !this.isChild) {
                    this.rightTit.render(f5);
                    this.leftTit.render(f5);
                }
                //妊娠期
                if (fd.pregnancyStage > 0.0F) {
                    this.pregnant.render(f5);
                }
            }
            //  设置模型的各种旋转角度，然后渲染模型。
            super.render(entity, f, f1, f2, f3, f4, f5);
            //旋转角度
            this.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimReloaded.log.error("渲染NPC出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }

    }
}
