package com.trhsy.buildcraft.api.blueprints;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagShort;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * ========================================
 *
 * @ClassName MappingRegistry
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 1:31
 * ========================================
 **/
public class MappingRegistry {
    public HashMap<Block, Integer> blockToId = new HashMap();
    public ArrayList<Block> idToBlock = new ArrayList();
    public HashMap<Item, Integer> itemToId = new HashMap();
    public ArrayList<Item> idToItem = new ArrayList();
    public HashMap<Class<? extends Entity>, Integer> entityToId = new HashMap();
    public ArrayList<Class<? extends Entity>> idToEntity = new ArrayList();

    public MappingRegistry() {
    }

    private void registerItem(Item item) {
        if (!this.itemToId.containsKey(item)) {
            this.idToItem.add(item);
            this.itemToId.put(item, this.idToItem.size() - 1);
        }

    }

    private void registerBlock(Block block) {
        if (!this.blockToId.containsKey(block)) {
            this.idToBlock.add(block);
            this.blockToId.put(block, this.idToBlock.size() - 1);
        }

    }

    private void registerEntity(Class<? extends Entity> entityClass) {
        if (!this.entityToId.containsKey(entityClass)) {
            this.idToEntity.add(entityClass);
            this.entityToId.put(entityClass, this.idToEntity.size() - 1);
        }

    }

    public Item getItemForId(int id) throws MappingNotFoundException {
        if (id >= this.idToItem.size()) {
            throw new MappingNotFoundException("no item mapping at position " + id);
        } else {
            Item result = (Item)this.idToItem.get(id);
            if (result == null) {
                throw new MappingNotFoundException("no item mapping at position " + id);
            } else {
                return result;
            }
        }
    }

    public int getIdForItem(Item item) {
        if (!this.itemToId.containsKey(item)) {
            this.registerItem(item);
        }

        return (Integer)this.itemToId.get(item);
    }

    public int itemIdToRegistry(int id) {
        Item item = Item.func_150899_d(id);
        return this.getIdForItem(item);
    }

    public int itemIdToWorld(int id) throws MappingNotFoundException {
        Item item = this.getItemForId(id);
        return Item.func_150891_b(item);
    }

    public Block getBlockForId(int id) throws MappingNotFoundException {
        if (id >= this.idToBlock.size()) {
            throw new MappingNotFoundException("no block mapping at position " + id);
        } else {
            Block result = (Block)this.idToBlock.get(id);
            if (result == null) {
                throw new MappingNotFoundException("no block mapping at position " + id);
            } else {
                return result;
            }
        }
    }

    public int getIdForBlock(Block block) {
        if (!this.blockToId.containsKey(block)) {
            this.registerBlock(block);
        }

        return (Integer)this.blockToId.get(block);
    }

    public int blockIdToRegistry(int id) {
        Block block = Block.func_149729_e(id);
        return this.getIdForBlock(block);
    }

    public int blockIdToWorld(int id) throws MappingNotFoundException {
        Block block = this.getBlockForId(id);
        return Block.func_149682_b(block);
    }

    public Class<? extends Entity> getEntityForId(int id) throws MappingNotFoundException {
        if (id >= this.idToEntity.size()) {
            throw new MappingNotFoundException("no entity mapping at position " + id);
        } else {
            Class<? extends Entity> result = (Class)this.idToEntity.get(id);
            if (result == null) {
                throw new MappingNotFoundException("no entity mapping at position " + id);
            } else {
                return result;
            }
        }
    }

    public int getIdForEntity(Class<? extends Entity> entity) {
        if (!this.entityToId.containsKey(entity)) {
            this.registerEntity(entity);
        }

        return (Integer)this.entityToId.get(entity);
    }

    public void stackToRegistry(NBTTagCompound nbt) {
        Item item = Item.func_150899_d(nbt.func_74765_d("id"));
        nbt.func_74777_a("id", (short)this.getIdForItem(item));
    }

    public void stackToWorld(NBTTagCompound nbt) throws MappingNotFoundException {
        Item item = this.getItemForId(nbt.func_74765_d("id"));
        nbt.func_74777_a("id", (short)Item.func_150891_b(item));
    }

    private boolean isStackLayout(NBTTagCompound nbt) {
        return nbt.func_74764_b("id") && nbt.func_74764_b("Count") && nbt.func_74764_b("Damage") && nbt.func_74781_a("id") instanceof NBTTagShort && nbt.func_74781_a("Count") instanceof NBTTagByte && nbt.func_74781_a("Damage") instanceof NBTTagShort;
    }

    public void scanAndTranslateStacksToRegistry(NBTTagCompound nbt) {
        if (this.isStackLayout(nbt)) {
            this.stackToRegistry(nbt);
        }

        Iterator i$ = nbt.func_150296_c().iterator();

        while(true) {
            NBTTagList list;
            do {
                String key;
                do {
                    if (!i$.hasNext()) {
                        return;
                    }

                    Object keyO = i$.next();
                    key = (String)keyO;
                    if (nbt.func_74781_a(key) instanceof NBTTagCompound) {
                        this.scanAndTranslateStacksToRegistry(nbt.func_74775_l(key));
                    }
                } while(!(nbt.func_74781_a(key) instanceof NBTTagList));

                list = (NBTTagList)nbt.func_74781_a(key);
            } while(list.func_150303_d() != 10);

            for(int i = 0; i < list.func_74745_c(); ++i) {
                this.scanAndTranslateStacksToRegistry(list.func_150305_b(i));
            }
        }
    }

    public void scanAndTranslateStacksToWorld(NBTTagCompound nbt) throws MappingNotFoundException {
        if (this.isStackLayout(nbt)) {
            this.stackToWorld(nbt);
        }

        Iterator i$ = nbt.func_150296_c().iterator();

        while(true) {
            NBTTagList list;
            do {
                String key;
                do {
                    if (!i$.hasNext()) {
                        return;
                    }

                    Object keyO = i$.next();
                    key = (String)keyO;
                    if (nbt.func_74781_a(key) instanceof NBTTagCompound) {
                        try {
                            this.scanAndTranslateStacksToWorld(nbt.func_74775_l(key));
                        } catch (MappingNotFoundException var8) {
                            nbt.func_82580_o(key);
                        }
                    }
                } while(!(nbt.func_74781_a(key) instanceof NBTTagList));

                list = (NBTTagList)nbt.func_74781_a(key);
            } while(list.func_150303_d() != 10);

            for(int i = list.func_74745_c() - 1; i >= 0; --i) {
                try {
                    this.scanAndTranslateStacksToWorld(list.func_150305_b(i));
                } catch (MappingNotFoundException var9) {
                    list.func_74744_a(i);
                }
            }
        }
    }

    public void write(NBTTagCompound nbt) {
        NBTTagList blocksMapping = new NBTTagList();
        Iterator i$ = this.idToBlock.iterator();

        while(i$.hasNext()) {
            Block b = (Block)i$.next();
            NBTTagCompound sub = new NBTTagCompound();
            sub.func_74778_a("name", Block.field_149771_c.func_148750_c(b));
            blocksMapping.func_74742_a(sub);
        }

        nbt.func_74782_a("blocksMapping", blocksMapping);
        NBTTagList itemsMapping = new NBTTagList();
        Iterator i$ = this.idToItem.iterator();

        while(i$.hasNext()) {
            Item i = (Item)i$.next();
            NBTTagCompound sub = new NBTTagCompound();
            sub.func_74778_a("name", Item.field_150901_e.func_148750_c(i));
            itemsMapping.func_74742_a(sub);
        }

        nbt.func_74782_a("itemsMapping", itemsMapping);
        NBTTagList entitiesMapping = new NBTTagList();
        Iterator i$ = this.idToEntity.iterator();

        while(i$.hasNext()) {
            Class<? extends Entity> e = (Class)i$.next();
            NBTTagCompound sub = new NBTTagCompound();
            sub.func_74778_a("name", e.getCanonicalName());
            entitiesMapping.func_74742_a(sub);
        }

        nbt.func_74782_a("entitiesMapping", entitiesMapping);
    }

    public void read(NBTTagCompound nbt) {
        NBTTagList blocksMapping = nbt.func_150295_c("blocksMapping", 10);

        for(int i = 0; i < blocksMapping.func_74745_c(); ++i) {
            NBTTagCompound sub = blocksMapping.func_150305_b(i);
            String name = sub.func_74779_i("name");
            Block b = null;
            if (Block.field_149771_c.func_148741_d(name)) {
                b = (Block)Block.field_149771_c.func_82594_a(name);
            }

            if (b != null) {
                this.registerBlock(b);
            } else {
                this.idToBlock.add((Object)null);
                BCLog.logger.log(Level.WARN, "Can't load block " + name);
            }
        }

        NBTTagList itemsMapping = nbt.func_150295_c("itemsMapping", 10);

        for(int i = 0; i < itemsMapping.func_74745_c(); ++i) {
            NBTTagCompound sub = itemsMapping.func_150305_b(i);
            String name = sub.func_74779_i("name");
            Item item = null;
            if (Item.field_150901_e.func_148741_d(name)) {
                item = (Item)Item.field_150901_e.func_82594_a(name);
            }

            if (item != null) {
                this.registerItem(item);
            } else {
                this.idToItem.add((Object)null);
                BCLog.logger.log(Level.WARN, "Can't load item " + name);
            }
        }

        NBTTagList entitiesMapping = nbt.func_150295_c("entitiesMapping", 10);

        for(int i = 0; i < entitiesMapping.func_74745_c(); ++i) {
            NBTTagCompound sub = entitiesMapping.func_150305_b(i);
            String name = sub.func_74779_i("name");
            Class e = null;

            try {
                e = Class.forName(name);
            } catch (ClassNotFoundException var10) {
                var10.printStackTrace();
            }

            if (e != null) {
                this.registerEntity(e);
            } else {
                this.idToEntity.add((Object)null);
                BCLog.logger.log(Level.WARN, "Can't load entity " + name);
            }
        }

    }
}
