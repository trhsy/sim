package com.trhsy.sim.npc;

import net.minecraft.client.renderer.texture.DynamicTexture;

/**
 * @ClassName DynamicSkin
 * @Description todo 皮肤
 * @Author TRHSY
 * @Date 2022/10/1520:17
 **/
public class DynamicSkin {
    public DynamicTexture texture;
    public String skinPath;

    public DynamicSkin(DynamicTexture texture, String skinPath) {
        this.texture = texture;
        this.skinPath = skinPath;
    }
}
