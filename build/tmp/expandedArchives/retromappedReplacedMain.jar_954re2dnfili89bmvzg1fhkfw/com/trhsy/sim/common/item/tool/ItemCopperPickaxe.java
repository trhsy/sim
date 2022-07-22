package com.trhsy.sim.common.item.tool;

import com.trhsy.sim.common.loader.CreativeTabsLoader;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPickaxe;
import net.minecraftforge.common.util.EnumHelper;

/**
 * @ClassName ItemCopperPickaxe
 * @Description todo 铜镐
 * @Author Tian
 * @Date 2022/5/1414:46
 **/
public class ItemCopperPickaxe extends ItemPickaxe {
    /**
     * @Author fan
     * @Description //TODO 枚举 WOOD，STONE，IRON，EMERALD，GOLD
     * 木头、石头、铁、钻石、金
     * WOOD(0, 59, 2.0F, 0.0F, 15),
     * STONE(1, 131, 4.0F, 1.0F, 5),
     * IRON(2, 250, 6.0F, 2.0F, 14),
     * EMERALD(3, 1561, 8.0F, 3.0F, 10),
     * GOLD(0, 32, 12.0F, 0.0F, 22);
     * harvestLevel参数表示制作出的工具等级。这一点在镐中尤其明显，如木头为0，只能挖掘对应等级为0的方块才能掉落物品，如石头等，而钻石为3，就可以挖掘出对应等级为3的，其他镐挖不出物品的方块，如黑曜石。这里使用了最高等级3
     * maxUses参数表示制作出的工具对应耐久。如钻石工具就是1561，耐久最高，而木工具为59，耐久最低。这里刻意250 与铁相同
     * efficiency参数表示制作出的工具使用效率。使用效率和该参数的值成正比。这里刻意提高了该数值，为16.0F
     * damageVsEntity参数表示攻击伤害力度。同样该力度和该参数的值成正相关。这里为2.0F，表示攻击力很低，与铁相同
     * enchantability参数与附魔等级相关。Minecraft中关于附魔等级的系统十分复杂。但是有一点，就是该值越高，对应的附魔就越容易得到高等级。这也是为何金更容易得到高等级附魔，而石头得到的附魔就相当低。这里为22，和金相同
     * @Date 14:48 2022/5/14
     * @Param
     * @return
     **/
    public static final Item.ToolMaterial REDSTONE = EnumHelper.addToolMaterial("COPPER", 3, 500, 16.0F, 2.0F, 22);

    public ItemCopperPickaxe() {
        super(REDSTONE);
        this.func_77655_b("copperPickaxe");
        this.func_77637_a(CreativeTabsLoader.tabSimU);
    }
}
