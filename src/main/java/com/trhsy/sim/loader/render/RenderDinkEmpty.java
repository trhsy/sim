package com.trhsy.sim.loader.render;

import com.trhsy.sim.entity.EntityDinkEmpty;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.loader.render
 * @ClassName: RenderDinkEmpty
 * @Description:
 * @date 2025/8/22 09:37
 */
@SideOnly(Side.CLIENT)
public class RenderDinkEmpty extends Render<EntityDinkEmpty> {
    // 绑定纹理（确保资源路径正确）
    private static final ResourceLocation TEXTURE = new ResourceLocation(
            "sim", "textures/models/drinkbeerempty.png"); // 替换为你的纹理路径
    // 空酒瓶模型
    private final ModelBase bottleModel;
    protected RenderDinkEmpty(RenderManager renderManager) {
        super(renderManager); // 阴影大小
        // 初始化模型
        this.bottleModel = new ModelBase() {
            private ModelRenderer bottle;

            {
                // 初始化纹理尺寸（与实际纹理图尺寸一致）
                this.textureWidth = 16;
                this.textureHeight = 16;

                // 创建模型部件：参数（纹理偏移U, 纹理偏移V）
                bottle = new ModelRenderer(this, 0, 0);
                // 添加立方体（酒瓶形状）：x, y, z, 宽, 高, 深
                bottle.addBox(-1F, 0F, -1F, 2, 5, 2, 0.0F);
                // 设置旋转点（模型旋转轴心）
                bottle.setRotationPoint(0.0F, 20.0F, 0.0F);
            }

            @Override
            public void render(net.minecraft.entity.Entity entityIn, float limbSwing, float limbSwingAmount,
                               float ageInTicks, float netHeadYaw, float headPitch, float scale) {
                // 应用旋转动画（飞行时旋转）
                bottle.rotateAngleZ = ageInTicks * 0.5F;
                bottle.render(scale);
            }
        };
    }
    @Override
    public void doRender(EntityDinkEmpty entity, double x, double y, double z,
                         float entityYaw, float partialTicks) {
        // 渲染前的状态设置
        GlStateManager.pushMatrix();
        // 平移到实体位置
        GlStateManager.translate((float) x, (float) y, (float) z);
        // 调整旋转（使实体面向飞行方向）
        GlStateManager.rotate(entityYaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(entity.rotationPitch, 1.0F, 0.0F, 0.0F);
        // 缩放模型（根据需要调整大小）
        GlStateManager.scale(0.8F, 0.8F, 0.8F);
        // 绑定纹理
        this.bindEntityTexture(entity);
        // 渲染模型
        this.bottleModel.render(entity, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        // 恢复渲染状态
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
    @Override
    protected ResourceLocation getEntityTexture(EntityDinkEmpty  entity) {
        return TEXTURE; // 返回纹理路径
    }
}
