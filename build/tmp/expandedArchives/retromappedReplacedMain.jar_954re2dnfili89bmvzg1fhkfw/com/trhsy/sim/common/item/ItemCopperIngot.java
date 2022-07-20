package com.trhsy.sim.common.item;

import com.trhsy.sim.common.loader.CreativeTabsLoader;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * @ClassName ItemCopperIngot
 * @Description todo 铜锭
 * @Author Tian
 * @Date 2022/5/1622:21
 **/
public class ItemCopperIngot extends Item {

    public ItemCopperIngot(){
        super();
        this.field_77777_bU = 64;
        this.func_77655_b("copperIngot");
        this.func_77637_a(CreativeTabsLoader.tabSimU);
    }

}
