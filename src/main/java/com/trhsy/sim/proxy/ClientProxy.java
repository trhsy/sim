package com.trhsy.sim.proxy;

import com.trhsy.sim.loader.ModSimLoader;
import com.trhsy.sim.loader.render.ItemRenderLoader;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.Display;

/**
 * 客户端代理
 */
public class ClientProxy extends CommonProxy{
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        super.preInit(event);
    }
    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
        String title= I18n.format("container.sim.title");
        Display.setTitle(Display.getTitle() +title);
    }
    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }

    public void renderTick(TickEvent.RenderTickEvent renderTickEvent){

    }
}
