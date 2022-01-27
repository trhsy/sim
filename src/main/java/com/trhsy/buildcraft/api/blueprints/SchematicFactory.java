package com.trhsy.buildcraft.api.blueprints;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;

/**
 * ========================================
 *
 * @ClassName SchematicFactory
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 1:36
 * ========================================
 **/
public abstract class SchematicFactory<S extends Schematic> {
    private static final HashMap<String, SchematicFactory<?>> factories = new HashMap();
    private static final HashMap<Class<? extends Schematic>, SchematicFactory<?>> schematicToFactory = new HashMap();

    public SchematicFactory() {
    }

    protected abstract S loadSchematicFromWorldNBT(NBTTagCompound var1, MappingRegistry var2) throws MappingNotFoundException;

    public void saveSchematicToWorldNBT(NBTTagCompound nbt, S object, MappingRegistry registry) {
        nbt.func_74778_a("factoryID", this.getClass().getCanonicalName());
    }

    public static Schematic createSchematicFromWorldNBT(NBTTagCompound nbt, MappingRegistry registry) throws MappingNotFoundException {
        String factoryName = nbt.func_74779_i("factoryID");
        return factories.containsKey(factoryName) ? ((SchematicFactory)factories.get(factoryName)).loadSchematicFromWorldNBT(nbt, registry) : null;
    }

    public static void registerSchematicFactory(Class<? extends Schematic> clas, SchematicFactory<?> factory) {
        schematicToFactory.put(clas, factory);
        factories.put(factory.getClass().getCanonicalName(), factory);
    }

    public static SchematicFactory getFactory(Class<? extends Schematic> clas) {
        Class superClass = clas.getSuperclass();
        if (schematicToFactory.containsKey(clas)) {
            return (SchematicFactory)schematicToFactory.get(clas);
        } else {
            return superClass != null ? getFactory(superClass) : null;
        }
    }
}

