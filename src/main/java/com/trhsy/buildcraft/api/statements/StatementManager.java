package com.trhsy.buildcraft.api.statements;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.*;

/**
 * ========================================
 *
 * @ClassName StatementManager
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 2:54
 * ========================================
 **/
public final class StatementManager {
    public static Map<String, IStatement> statements = new HashMap();
    public static Map<String, Class<? extends IStatementParameter>> parameters = new HashMap();
    private static List<ITriggerProvider> triggerProviders = new LinkedList();
    private static List<IActionProvider> actionProviders = new LinkedList();

    private StatementManager() {
    }

    public static void registerTriggerProvider(ITriggerProvider provider) {
        if (provider != null && !triggerProviders.contains(provider)) {
            triggerProviders.add(provider);
        }

    }

    public static void registerActionProvider(IActionProvider provider) {
        if (provider != null && !actionProviders.contains(provider)) {
            actionProviders.add(provider);
        }

    }

    public static void registerStatement(IStatement statement) {
        statements.put(statement.getUniqueTag(), statement);
    }

    public static void registerParameterClass(Class<? extends IStatementParameter> param) {
        parameters.put(createParameter(param).getUniqueTag(), param);
    }

    /** @deprecated */
    @Deprecated
    public static void registerParameterClass(String name, Class<? extends IStatementParameter> param) {
        parameters.put(name, param);
    }

    public static List<ITriggerExternal> getExternalTriggers(ForgeDirection side, TileEntity entity) {
        if (entity instanceof IOverrideDefaultStatements) {
            List<ITriggerExternal> result = ((IOverrideDefaultStatements)entity).overrideTriggers();
            if (result != null) {
                return result;
            }
        }

        List<ITriggerExternal> result = new LinkedList();
        Iterator i$ = triggerProviders.iterator();

        while(true) {
            Collection toAdd;
            do {
                if (!i$.hasNext()) {
                    return result;
                }

                ITriggerProvider provider = (ITriggerProvider)i$.next();
                toAdd = provider.getExternalTriggers(side, entity);
            } while(toAdd == null);

            Iterator iterator = toAdd.iterator();

            while(iterator.hasNext()) {
                ITriggerExternal t = (ITriggerExternal)iterator.next();
                if (!result.contains(t)) {
                    result.add(t);
                }
            }
        }
    }

    public static List<IActionExternal> getExternalActions(ForgeDirection side, TileEntity entity) {
        List<IActionExternal> result = new LinkedList();
        if (entity instanceof IOverrideDefaultStatements) {
            result = ((IOverrideDefaultStatements)entity).overrideActions();
            if (result != null) {
                return result;
            }

            result = new LinkedList();
        }

        Iterator iterator = actionProviders.iterator();

        while(true) {
            Collection toAdd;
            do {
                if (!iterator.hasNext()) {
                    return result;
                }

                IActionProvider provider = (IActionProvider)iterator.next();
                toAdd = provider.getExternalActions(side, entity);
            } while(toAdd == null);

            Iterator i$ = toAdd.iterator();

            while(i$.hasNext()) {
                IActionExternal t = (IActionExternal)i$.next();
                if (!result.contains(t)) {
                    result.add(t);
                }
            }
        }
    }

    public static List<ITriggerInternal> getInternalTriggers(IStatementContainer container) {
        List<ITriggerInternal> result = new LinkedList();
        Iterator i$ = triggerProviders.iterator();

        while(true) {
            Collection toAdd;
            do {
                if (!i$.hasNext()) {
                    return result;
                }

                ITriggerProvider provider = (ITriggerProvider)i$.next();
                toAdd = provider.getInternalTriggers(container);
            } while(toAdd == null);

            Iterator iterator = toAdd.iterator();

            while(iterator.hasNext()) {
                ITriggerInternal t = (ITriggerInternal)iterator.next();
                if (!result.contains(t)) {
                    result.add(t);
                }
            }
        }
    }

    public static List<IActionInternal> getInternalActions(IStatementContainer container) {
        List<IActionInternal> result = new LinkedList();
        Iterator i$ = actionProviders.iterator();

        while(true) {
            Collection toAdd;
            do {
                if (!i$.hasNext()) {
                    return result;
                }

                IActionProvider provider = (IActionProvider)i$.next();
                toAdd = provider.getInternalActions(container);
            } while(toAdd == null);

            Iterator iterator = toAdd.iterator();

            while(iterator.hasNext()) {
                IActionInternal t = (IActionInternal)iterator.next();
                if (!result.contains(t)) {
                    result.add(t);
                }
            }
        }
    }

    public static IStatementParameter createParameter(String kind) {
        return createParameter((Class)parameters.get(kind));
    }

    private static IStatementParameter createParameter(Class<? extends IStatementParameter> param) {
        try {
            return (IStatementParameter)param.newInstance();
        } catch (InstantiationException var2) {
            var2.printStackTrace();
        } catch (IllegalAccessException var3) {
            var3.printStackTrace();
        }

        return null;
    }

    @SideOnly(Side.CLIENT)
    public static void registerIcons(IIconRegister register) {
        Iterator i$ = statements.values().iterator();

        while(i$.hasNext()) {
            IStatement statement = (IStatement)i$.next();
            statement.registerIcons(register);
        }

        i$ = parameters.values().iterator();

        while(i$.hasNext()) {
            Class<? extends IStatementParameter> parameter = (Class)i$.next();
            createParameter(parameter).registerIcons(register);
        }

    }
}
