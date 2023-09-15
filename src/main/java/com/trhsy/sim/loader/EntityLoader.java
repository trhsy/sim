package com.trhsy.sim.loader;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.entity.EntityConBox;
import com.trhsy.sim.entity.EntityFolk;
import com.trhsy.sim.entity.TileEntityWindmill;
import com.trhsy.sim.entity.render.RenderConBox;
import com.trhsy.sim.entity.render.RenderEntityFolk;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.loader
 * @ClassName: EntityLoader
 * @Description:
 * @date 2022/10/12 9:42
 */
public class EntityLoader {
    private static int nextID = 0;
    private static final RegistryNamespaced< ResourceLocation, Class <? extends TileEntity >> REGISTRY = net.minecraftforge.fml.common.registry.GameData.getTileEntityRegistry();
    public EntityLoader() {


    }
    public static void init() {
        ResourceLocation var10000 = new ResourceLocation(ModSim.MODID+":folk");
        EntityRegistry.registerModEntity(new ResourceLocation(ModSim.MODID+":folk"), EntityFolk.class, "EntityFolk", nextID++, ModSim.instance, 64, 3, true);
        EntityRegistry.registerModEntity(new ResourceLocation(ModSim.MODID+":ConBox") ,EntityConBox.class, "ConBox", nextID++, ModSim.instance, 64, 3, false);
        //        registerEntity(EntityFolk.class, "EntityFolk", 64, 3, true);
    }
    @SideOnly(Side.CLIENT)
    public static void initModels() {
        try {
            // TODO
            RenderingRegistry.registerEntityRenderingHandler(EntityFolk.class, RenderEntityFolk.FACTORY);
            RenderingRegistry.registerEntityRenderingHandler(EntityConBox.class, RenderConBox.FACTORY);
            REGISTRY.putObject(new ResourceLocation("Windmill"),TileEntityWindmill.class);
            //TileEntity.addMapping(TileEntityWindmill.class,"Windmill");
            //registerEntityRender(EntityAlignBeam.class, RenderAlignBeam.class);
            //registerEntityRender(EntityWindmill.class, RenderWindmill.class);
        } catch (Exception e) {
            StackTraceElement element=e.getStackTrace()[0];ModSimLoader.log.error("registerRenders出错了：" + e.getMessage()+"行数："+element.getLineNumber());
        }
    }
}
