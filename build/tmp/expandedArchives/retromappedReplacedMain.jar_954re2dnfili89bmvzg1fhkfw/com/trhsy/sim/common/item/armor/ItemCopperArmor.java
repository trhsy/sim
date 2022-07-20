package com.trhsy.sim.common.item.armor;

import com.trhsy.sim.ModSim;
import com.trhsy.sim.common.loader.CreativeTabsLoader;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.util.EnumHelper;

/**
 * @ClassName ItemCopperArmor
 * @Description todo 铜制盔甲
 * @Author Tian
 * @Date 2022/5/1520:31
 **/
public class ItemCopperArmor extends ItemArmor {
    /**
     * @Author fan
     * @Description //TODO
     * LEATHER("leather", 5, new int[]{1, 3, 2, 1}, 15),
     * CHAIN("chainmail", 15, new int[]{2, 5, 4, 1}, 12),
     * IRON("iron", 15, new int[]{2, 6, 5, 2}, 9),
     * GOLD("gold", 7, new int[]{2, 5, 3, 1}, 25),
     * DIAMOND("diamond", 33, new int[]{3, 8, 6, 3}, 10);
     * <p>
     * name参数与该ArmorMaterial的材质所在位置有关，这一部分的稍后面会讲到。这里是“sim:copper”。
     * maxDamage参数和该ArmorMaterial对应的盔甲的耐久成正比。这里跟铁差不多，为15。
     * reductionAmounts参数的四个元素表示对应盔甲的头盔、胸甲、护腿、和靴子抵御伤害的能力，如皮甲分别为1，3，2，1，和为7，钻石甲分别为3，8，6，3，和为20，请不要让四个元素值的和超过这个值。这里为5，5，5，5，和为20。
     * enchantability参数和ToolMaterial一样，和对应盔甲的附魔能力正相关，同样，金盔甲的附魔能力最高。这里为35 比金更容易附魔。
     * @Date 20:35 2022/5/15
     * @Param
     * @return
     **/
    public static final ItemArmor.ArmorMaterial COPPER_ARMOR = EnumHelper.addArmorMaterial("COPPER", ModSim.MODID + ":" + "copper", 15, new int[]{5, 5, 5, 5}, 35);

    public ItemCopperArmor(int armorType) {
        /**
         * @Author fan
         * @Description //TODO
         * 第一个参数表示该盔甲的ArmorMaterial，自然就是我们刚刚创建的那个。
         * 第二个参数的名称为renderIndex，目前在源代码中没有找到对其的引用，作者个人认为其在某个版本中被弃用了，随便填一个就可以了。但是为了保证不同的ArmorMaterial对应不同的值，作者这里使用了该ArmorMaterial的序数值。
         * 第三个参数表示该盔甲的类型，0为头盔，1为胸甲，2为护腿，3为靴子。
         * @Date 20:41 2022/5/15
         * @Param [armorType]
         * @return
         **/
        super(COPPER_ARMOR, COPPER_ARMOR.ordinal(), armorType);
    }

    public static class Helmet extends ItemCopperArmor {
        public Helmet() {
            super(0);
            this.func_77655_b("copperHelmet");
            this.func_77637_a(CreativeTabsLoader.tabSimU);
        }
    }

    public static class Chestplate extends ItemCopperArmor {
        public Chestplate() {
            super(1);
            this.func_77655_b("copperChestplate");
            this.func_77637_a(CreativeTabsLoader.tabSimU);
        }
    }

    public static class Leggings extends ItemCopperArmor {
        public Leggings() {
            super(2);
            this.func_77655_b("copperLeggings");
            this.func_77637_a(CreativeTabsLoader.tabSimU);
        }
    }

    public static class Boots extends ItemCopperArmor {
        public Boots() {
            super(3);
            this.func_77655_b("copperBoots");
            this.func_77637_a(CreativeTabsLoader.tabSimU);
        }
    }
}
