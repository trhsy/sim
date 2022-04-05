package com.trhsy.sim.api.buildcraft.api.statements;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.sim.api.buildcraft.api.core.BCLog;
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
        Iterator var3 = triggerProviders.iterator();

        while(true) {
            Collection toAdd;
            do {
                if (!var3.hasNext()) {
                    return result;
                }

                ITriggerProvider provider = (ITriggerProvider)var3.next();
                toAdd = provider.getExternalTriggers(side, entity);
            } while(toAdd == null);

            Iterator var6 = toAdd.iterator();

            while(var6.hasNext()) {
                ITriggerExternal t = (ITriggerExternal)var6.next();
                if (!result.contains(t)) {
                    result.add(t);
                }
            }
        }
    }

    public static List<IActionExternal> getExternalActions(ForgeDirection side, TileEntity entity) {
        if (entity instanceof IOverrideDefaultStatements) {
            List<IActionExternal> result = ((IOverrideDefaultStatements)entity).overrideActions();
            if (result != null) {
                return result;
            }
        }

        List<IActionExternal> result = new LinkedList();
        Iterator var3 = actionProviders.iterator();

        while(true) {
            Collection toAdd;
            do {
                if (!var3.hasNext()) {
                    return result;
                }

                IActionProvider provider = (IActionProvider)var3.next();
                toAdd = provider.getExternalActions(side, entity);
            } while(toAdd == null);

            Iterator var6 = toAdd.iterator();

            while(var6.hasNext()) {
                IActionExternal t = (IActionExternal)var6.next();
                if (!result.contains(t)) {
                    result.add(t);
                }
            }
        }
    }

    public static List<ITriggerInternal> getInternalTriggers(IStatementContainer container) {
        List<ITriggerInternal> result = new LinkedList();
        Iterator var2 = triggerProviders.iterator();

        while(true) {
            Collection toAdd;
            do {
                if (!var2.hasNext()) {
                    return result;
                }

                ITriggerProvider provider = (ITriggerProvider)var2.next();
                toAdd = provider.getInternalTriggers(container);
            } while(toAdd == null);

            Iterator var5 = toAdd.iterator();

            while(var5.hasNext()) {
                ITriggerInternal t = (ITriggerInternal)var5.next();
                if (!result.contains(t)) {
                    result.add(t);
                }
            }
        }
    }

    public static List<IActionInternal> getInternalActions(IStatementContainer container) {
        List<IActionInternal> result = new LinkedList();
        Iterator var2 = actionProviders.iterator();

        while(true) {
            Collection toAdd;
            do {
                if (!var2.hasNext()) {
                    return result;
                }

                IActionProvider provider = (IActionProvider)var2.next();
                toAdd = provider.getInternalActions(container);
            } while(toAdd == null);

            Iterator var5 = toAdd.iterator();

            while(var5.hasNext()) {
                IActionInternal t = (IActionInternal)var5.next();
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
        } catch (Error var4) {
            BCLog.logErrorAPI(var4, IStatementParameter.class);
            throw var4;
        }

        return null;
    }

    @SideOnly(Side.CLIENT)
    public static void registerIcons(IIconRegister register) {
        Iterator var1 = statements.values().iterator();

        while(var1.hasNext()) {
            IStatement statement = (IStatement)var1.next();
            statement.registerIcons(register);
        }

        var1 = parameters.values().iterator();

        while(var1.hasNext()) {
            Class<? extends IStatementParameter> parameter = (Class)var1.next();
            createParameter(parameter).registerIcons(register);
        }

    }
}
