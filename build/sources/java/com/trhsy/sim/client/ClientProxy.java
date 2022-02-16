package com.trhsy.sim.client;

import com.trhsy.sim.client.loader.ItemRenderLoader;
import com.trhsy.sim.client.model.ModelAlignBeam;
import com.trhsy.sim.client.model.ModelConBox;
import com.trhsy.sim.client.model.ModelFolkFemale;
import com.trhsy.sim.client.render.RenderAlignBeam;
import com.trhsy.sim.client.render.RenderConBox;
import com.trhsy.sim.client.render.RenderFolk;
import com.trhsy.sim.common.CommonProxy;
import com.trhsy.sim.common.EntityAlignBeam;
import com.trhsy.sim.common.EntityConBox;
import com.trhsy.sim.common.EntityFolk;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

/**
 * @ClassName ClientProxy
 * @Description todo 客户端
 * @Author Tian
 * @Date 2022/1/2223:15
 **/
public class ClientProxy  extends CommonProxy {

    public ClientProxy() {
    }

    @Override
    public void registerRenderInfo() {
        RenderingRegistry.registerEntityRenderingHandler(EntityAlignBeam.class, new RenderAlignBeam(new ModelAlignBeam()));
        RenderingRegistry.registerEntityRenderingHandler(EntityFolk.class, new RenderFolk(new ModelFolkFemale()));
        RenderingRegistry.registerEntityRenderingHandler(EntityConBox.class, new RenderConBox(new ModelConBox()));
    }
    @Override
    public void registerMisc() {
        super.registerMisc();
        FMLCommonHandler.instance().bus().register(this);
    }

    @Override
    public World getClientWorld() {
        return FMLClientHandler.instance().getClient().theWorld;
    }

    @Override
    public void saveObject(String filename, Object o) {
        if (o != null) {
            try {
                FileOutputStream fos = new FileOutputStream(filename);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(o);
                oos.flush();
                oos.close();
            } catch (Exception var5) {
                var5.printStackTrace();
            }

        }
    }

    @Override
    public EntityPlayer getPlayerEntity(MessageContext ctx) {
        return (EntityPlayer)(ctx.side.isClient() ? Minecraft.getMinecraft().thePlayer : super.getPlayerEntity(ctx));
    }
    /**
     * @Author fan
     * @Description //TODO 声明该方法覆盖了父类方法，如果（拼写错误等）没有覆写，编译器会报错
     * @Date 23:16 2022/1/22
     * @Param [event]
     * @return void
     **/
    @Override
    public void preInit(FMLPreInitializationEvent event) {
        //调用父类的方法
        super.preInit(event);
        new ItemRenderLoader();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        super.init(event);
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }
}
