package com.trhsy.buildcraft.api.blueprints;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import com.trhsy.buildcraft.api.core.Position;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * ========================================
 *
 * @ClassName SchematicEntity
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 1:35
 * ========================================
 **/
public class SchematicEntity extends Schematic {
    public Class<? extends Entity> entity;
    public NBTTagCompound entityNBT = new NBTTagCompound();
    public ItemStack[] storedRequirements = new ItemStack[0];

    public SchematicEntity() {
    }

    @Override
    public void getRequirementsForPlacement(IBuilderContext context, LinkedList<ItemStack> requirements) {
        Collections.addAll(requirements, this.storedRequirements);
    }

    public void writeToWorld(IBuilderContext context) {
        Entity e = EntityList.createEntityFromNBT(this.entityNBT, context.world());
        context.world().spawnEntityInWorld(e);
    }

    public void readFromWorld(IBuilderContext context, Entity entity) {
        entity.writeToNBTOptional(this.entityNBT);
    }

    @Override
    public void translateToBlueprint(Translation transform) {
        NBTTagList nbttaglist = this.entityNBT.getTagList("Pos", 6);
        Position pos = new Position(nbttaglist.getDoubleAt(0), nbttaglist.getDoubleAt(1), nbttaglist.getDoubleAt(2));
        pos = transform.translate(pos);
        this.entityNBT.setTag("Pos", this.newDoubleNBTList(pos.x, pos.y, pos.z));
    }

    @Override
    public void translateToWorld(Translation transform) {
        NBTTagList nbttaglist = this.entityNBT.getTagList("Pos", 6);
        Position pos = new Position(nbttaglist.getDoubleAt(0), nbttaglist.getDoubleAt(1), nbttaglist.getDoubleAt(2));
        pos = transform.translate(pos);
        this.entityNBT.setTag("Pos", this.newDoubleNBTList(pos.x, pos.y, pos.z));
    }

    @Override
    public void idsToBlueprint(MappingRegistry registry) {
        registry.scanAndTranslateStacksToRegistry(this.entityNBT);
    }

    @Override
    public void idsToWorld(MappingRegistry registry) {
        try {
            registry.scanAndTranslateStacksToWorld(this.entityNBT);
        } catch (MappingNotFoundException var3) {
            this.entityNBT = new NBTTagCompound();
        }

    }

    @Override
    public void rotateLeft(IBuilderContext context) {
        NBTTagList nbttaglist = this.entityNBT.getTagList("Pos", 6);
        Position pos = new Position(nbttaglist.getDoubleAt(0), nbttaglist.getDoubleAt(1), nbttaglist.getDoubleAt(2));
        pos = context.rotatePositionLeft(pos);
        this.entityNBT.setTag("Pos", this.newDoubleNBTList(pos.x, pos.y, pos.z));
        nbttaglist = this.entityNBT.getTagList("Rotation", 5);
        float yaw = nbttaglist.getFloatAt(0);
        yaw += 90.0F;
        this.entityNBT.setTag("Rotation", this.newFloatNBTList(yaw, nbttaglist.getFloatAt(1)));
    }

    @Override
    public void writeSchematicToNBT(NBTTagCompound nbt, MappingRegistry registry) {
        super.writeSchematicToNBT(nbt, registry);
        nbt.setInteger("entityId", registry.getIdForEntity(this.entity));
        nbt.setTag("entity", this.entityNBT);
        NBTTagList rq = new NBTTagList();
        ItemStack[] arr$ = this.storedRequirements;
        int len$ = arr$.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            ItemStack stack = arr$[i$];
            NBTTagCompound sub = new NBTTagCompound();
            stack.writeToNBT(stack.writeToNBT(sub));
            sub.setInteger("id", registry.getIdForItem(stack.getItem()));
            rq.appendTag(sub);
        }

        nbt.setTag("rq", rq);
    }

    @Override
    public void readSchematicFromNBT(NBTTagCompound nbt, MappingRegistry registry) {
        super.readSchematicFromNBT(nbt, registry);
        this.entityNBT = nbt.getCompoundTag("entity");
        NBTTagList rq = nbt.getTagList("rq", 10);
        ArrayList<ItemStack> rqs = new ArrayList();

        for(int i = 0; i < rq.tagCount(); ++i) {
            try {
                NBTTagCompound sub = rq.getCompoundTagAt(i);
                if (sub.getInteger("id") >= 0) {
                    sub.setInteger("id", Item.itemRegistry.getIDForObject(registry.getItemForId(sub.getInteger("id"))));
                    rqs.add(ItemStack.loadItemStackFromNBT(sub));
                }
            } catch (Throwable var7) {
                var7.printStackTrace();
            }
        }

        this.storedRequirements = (ItemStack[])rqs.toArray(new ItemStack[rqs.size()]);
    }

    protected NBTTagList newDoubleNBTList(double... par1ArrayOfDouble) {
        NBTTagList nbttaglist = new NBTTagList();
        double[] adouble = par1ArrayOfDouble;
        int i = par1ArrayOfDouble.length;

        for(int j = 0; j < i; ++j) {
            double d1 = adouble[j];
            nbttaglist.appendTag(new NBTTagDouble(d1));
        }

        return nbttaglist;
    }

    protected NBTTagList newFloatNBTList(float... par1ArrayOfFloat) {
        NBTTagList nbttaglist = new NBTTagList();
        float[] afloat = par1ArrayOfFloat;
        int i = par1ArrayOfFloat.length;

        for(int j = 0; j < i; ++j) {
            float f1 = afloat[j];
            nbttaglist.appendTag(new NBTTagFloat(f1));
        }

        return nbttaglist;
    }

    public boolean isAlreadyBuilt(IBuilderContext context) {
        NBTTagList nbttaglist = this.entityNBT.getTagList("Pos", 6);
        Position newPosition = new Position(nbttaglist.getDoubleAt(0), nbttaglist.getDoubleAt(1), nbttaglist.getDoubleAt(2));
        Iterator i$ = context.world().loadedEntityList.iterator();

        Position existingPositon;
        do {
            if (!i$.hasNext()) {
                return false;
            }

            Object o = i$.next();
            Entity e = (Entity)o;
            existingPositon = new Position(e.posX, e.posY, e.posZ);
        } while(!existingPositon.isClose(newPosition, 0.1F));

        return true;
    }

    @Override
    public int buildTime() {
        return 5;
    }
}
