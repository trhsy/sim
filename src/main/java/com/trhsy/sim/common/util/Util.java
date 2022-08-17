package com.trhsy.sim.common.util;

import com.google.common.collect.Lists;
import com.trhsy.sim.ModSim;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fml.common.registry.GameData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

import java.util.List;
import java.util.Locale;

/**
 * @ClassName Util
 * @Description todo
 * @Author Tian
 * @Date 2022/4/3020:41
 **/
public class Util {
    public static final Logger log = Util.getLogger("Util");
    public static final String RESOURCE;

    public Util() {
    }

    public static Logger getLogger(String type) {
        String log = "sim";
        return LogManager.getLogger(log + "-" + type);
    }

    public static String sanitizeLocalizationString(String string) {
        return string.toLowerCase(Locale.US).replaceAll(" ", "");
    }

    public static String resource(String res) {
        return String.format("%s:%s", RESOURCE, res);
    }

    public static ResourceLocation getResource(String res) {
        return new ResourceLocation(RESOURCE, res);
    }

    public static ModelResourceLocation getModelResource(String res, String variant) {
        return new ModelResourceLocation(resource(res), variant);
    }

    public static String prefix(String name) {
        return String.format("%s", name.toLowerCase(Locale.US));
    }

    public static String translate(String key, Object... pars) {
        return StatCollector.translateToLocal(StatCollector.translateToLocal(String.format(key, (Object[])pars)).trim()).trim();
    }

    public static String translateFormatted(String key, Object... pars) {
        return StatCollector.translateToLocal(StatCollector.translateToLocalFormatted(key, (Object[])pars).trim()).trim();
    }

    public static List<String> getTooltips(String text) {
        List<String> list = Lists.newLinkedList();
        if (text == null) {
            return list;
        } else {
            int j;
            int k;
            for(j = 0; (k = text.indexOf("\\n", j)) >= 0; j = k + 2) {
                list.add(text.substring(j, k));
            }

            list.add(text.substring(j, text.length()));
            return list;
        }
    }

    public static String convertNewlines(String line) {
        if (line == null) {
            return null;
        } else {
            int j;
            while((j = line.indexOf("\\n")) >= 0) {
                line = line.substring(0, j) + '\n' + line.substring(j + 2);
            }

            return line;
        }
    }

    public static ResourceLocation getItemLocation(Item item) {
        Object o = GameData.getItemRegistry().getNameForObject(item);
        if (o == null) {
            log.error("Item %s is not registered!" + item.getUnlocalizedName());
            return null;
        } else {
            return (ResourceLocation)o;
        }
    }

    public static ItemStack[] copyItemStackArray(ItemStack[] in) {
        return RecipeMatchRegistry.copyItemStackArray(in);
    }

    public static boolean isCtrlKeyDown() {
        boolean isCtrlKeyDown = Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157);
        if (!isCtrlKeyDown && Minecraft.isRunningOnMac) {
            isCtrlKeyDown = Keyboard.isKeyDown(219) || Keyboard.isKeyDown(220);
        }

        return isCtrlKeyDown;
    }

    public static boolean isShiftKeyDown() {
        return Keyboard.isKeyDown(42) || Keyboard.isKeyDown(54);
    }

    public static int enumChatFormattingToColor(EnumChatFormatting color) {
        int i = color.getColorIndex();
        int j = (i >> 3 & 1) * 85;
        int k = (i >> 2 & 1) * 170 + j;
        int l = (i >> 1 & 1) * 170 + j;
        int i1 = (i >> 0 & 1) * 170 + j;
        if (i == 6) {
            k += 85;
        }

        if (i >= 16) {
            k /= 4;
            l /= 4;
            i1 /= 4;
        }

        return (k & 255) << 16 | (l & 255) << 8 | i1 & 255;
    }

    static {
        RESOURCE = ModSim.MODID.toLowerCase(Locale.US);
    }
}
