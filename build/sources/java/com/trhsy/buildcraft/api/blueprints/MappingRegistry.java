package com.trhsy.buildcraft.api.blueprints;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.buildcraft.api.core.BCLog;
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
            throw new MappingNotFoundException("位置没有项目映射 " + id);
        } else {
            Item result = (Item)this.idToItem.get(id);
            if (result == null) {
                throw new MappingNotFoundException("位置没有项目映射 " + id);
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
        Item item = Item.getItemById(id);
        return this.getIdForItem(item);
    }

    public int itemIdToWorld(int id) throws MappingNotFoundException {
        Item item = this.getItemForId(id);
        return Item.getIdFromItem(item);
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
        Block block = Block.getBlockById(id);
        return this.getIdForBlock(block);
    }

    public int blockIdToWorld(int id) throws MappingNotFoundException {
        Block block = this.getBlockForId(id);
        return Block.getIdFromBlock(block);
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
        Item item = Item.getItemById(nbt.getShort("id"));
        nbt.setShort("id", (short)this.getIdForItem(item));
    }

    public void stackToWorld(NBTTagCompound nbt) throws MappingNotFoundException {
        Item item = this.getItemForId(nbt.getShort("id"));
        nbt.setShort("id", (short)Item.getIdFromItem(item));
    }

    private boolean isStackLayout(NBTTagCompound nbt) {
        return nbt.hasKey("id") && nbt.hasKey("Count") && nbt.hasKey("Damage") && nbt.getTag("id") instanceof NBTTagShort && nbt.getTag("Count") instanceof NBTTagByte && nbt.getTag("Damage") instanceof NBTTagShort;
    }

    public void scanAndTranslateStacksToRegistry(NBTTagCompound nbt) {
        if (this.isStackLayout(nbt)) {
            this.stackToRegistry(nbt);
        }

        Iterator i$ = nbt.getKeySet().iterator();

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
                    if (nbt.getTag(key) instanceof NBTTagCompound) {
                        this.scanAndTranslateStacksToRegistry(nbt.getCompoundTag(key));
                    }
                } while(!(nbt.getTag(key) instanceof NBTTagList));

                list = (NBTTagList)nbt.getTag(key);
            } while(list.getTagType() != 10);

            for(int i = 0; i < list.tagCount(); ++i) {
                this.scanAndTranslateStacksToRegistry(list.getCompoundTagAt(i));
            }
        }
    }

    public void scanAndTranslateStacksToWorld(NBTTagCompound nbt) throws MappingNotFoundException {
        if (this.isStackLayout(nbt)) {
            this.stackToWorld(nbt);
        }

        Iterator i$ = nbt.getKeySet().iterator();

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
                    if (nbt.getTag(key) instanceof NBTTagCompound) {
                        try {
                            this.scanAndTranslateStacksToWorld(nbt.getCompoundTag(key));
                        } catch (MappingNotFoundException var8) {
                            nbt.removeTag(key);
                        }
                    }
                } while(!(nbt.getTag(key) instanceof NBTTagList));

                list = (NBTTagList)nbt.getTag(key);
            } while(list.getTagType() != 10);

            for(int i = list.tagCount() - 1; i >= 0; --i) {
                try {
                    this.scanAndTranslateStacksToWorld(list.getCompoundTagAt(i));
                } catch (MappingNotFoundException var9) {
                    list.removeTag(i);
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
            sub.setString("name", Block.blockRegistry.getNameForObject(b));
            blocksMapping.appendTag(sub);
        }

        nbt.setTag("blocksMapping", blocksMapping);
        NBTTagList itemsMapping = new NBTTagList();
        Iterator iterator = this.idToItem.iterator();

        while(iterator.hasNext()) {
            Item i = (Item)iterator.next();
            NBTTagCompound sub = new NBTTagCompound();
            sub.setString("name", Item.itemRegistry.getNameForObject(i));
            itemsMapping.appendTag(sub);
        }

        nbt.setTag("itemsMapping", itemsMapping);
        NBTTagList entitiesMapping = new NBTTagList();
        Iterator iterator1 = this.idToEntity.iterator();

        while(iterator1.hasNext()) {
            Class<? extends Entity> e = (Class)iterator1.next();
            NBTTagCompound sub = new NBTTagCompound();
            sub.setString("name", e.getCanonicalName());
            entitiesMapping.appendTag(sub);
        }

        nbt.setTag("entitiesMapping", entitiesMapping);
    }

    public void read(NBTTagCompound nbt) {
        NBTTagList blocksMapping = nbt.getTagList("blocksMapping", 10);

        for(int i = 0; i < blocksMapping.tagCount(); ++i) {
            NBTTagCompound sub = blocksMapping.getCompoundTagAt(i);
            String name = sub.getString("name");
            Block b = null;
            if (Block.blockRegistry.containsKey(name)) {
                b = (Block)Block.blockRegistry.getObject(name);
            }

            if (b != null) {
                this.registerBlock(b);
            } else {
                this.idToBlock.add(null);
                BCLog.logger.log(Level.WARN, "Can't load block " + name);
            }
        }

        NBTTagList itemsMapping = nbt.getTagList("itemsMapping", 10);

        for(int i = 0; i < itemsMapping.tagCount(); ++i) {
            NBTTagCompound sub = itemsMapping.getCompoundTagAt(i);
            String name = sub.getString("name");
            Item item = null;
            if (Item.itemRegistry.containsKey(name)) {
                item = (Item)Item.itemRegistry.getObject(name);
            }

            if (item != null) {
                this.registerItem(item);
            } else {
                this.idToItem.add(null);
                BCLog.logger.log(Level.WARN, "Can't load item " + name);
            }
        }

        NBTTagList entitiesMapping = nbt.getTagList("entitiesMapping", 10);

        for(int i = 0; i < entitiesMapping.tagCount(); ++i) {
            NBTTagCompound sub = entitiesMapping.getCompoundTagAt(i);
            String name = sub.getString("name");
            Class e = null;

            try {
                e = Class.forName(name);
            } catch (ClassNotFoundException var10) {
                var10.printStackTrace();
            }

            if (e != null) {
                this.registerEntity(e);
            } else {
                this.idToEntity.add(null);
                BCLog.logger.log(Level.WARN, "Can't load entity " + name);
            }
        }

    }
}
