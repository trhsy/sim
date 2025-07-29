package com.trhsy.sim.block.enums;


import net.minecraft.block.material.Material;

public class ModMaterials {
    public static final Material SOFT_FOOD;

    static {
        // 在模组预初始化时注册材质
        SOFT_FOOD = new ModMaterial(new Material.Properties()
                .setRequiresTool(false)  // 是否需要工具挖掘
                .setOpaque((bs, br, bp) -> false)  // 是否不透明
                .setTranslucent(false)  // 是否半透明
        );
        ForgeRegistries.MATERIALS.register(SOFT_FOOD.setRegistryName(ModSim.MODID, "soft_food"));
    }
}
