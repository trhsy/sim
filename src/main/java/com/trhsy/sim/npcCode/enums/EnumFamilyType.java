package com.trhsy.sim.npcCode.enums;

/**
 * @author Trhsy
 * @Package: com.trhsy.sim.util
 * @ClassName: EnumFamilyType
 * @Description: 朋友关系类型
 * @date 2022/10/13 15:17
 */
public enum EnumFamilyType {
    /**不相干**/
    UNRELATED,
    /**伴侣**/
    PARTNER,
    /**配偶**/
    SPOUSE,
    /**父亲**/
    PARENT,
    /**孩子**/
    CHILD,
    /**兄弟姐妹**/
    SIBLING,
    /**祖父母**/
    GRANDPARENT,
    /**孙子女**/
    GRANDCHILD,
    /****/
    PARENTSIBLING,
    /****/
    SIBLINGCHILD,
    /**堂兄**/
    COUSIN,
    /**表的**/
    EXTENDED;
    private EnumFamilyType() {
    }
}
