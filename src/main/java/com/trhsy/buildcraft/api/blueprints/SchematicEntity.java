package com.trhsy.buildcraft.api.blueprints;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

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

    public void getRequirementsForPlacement(IBuilderContext context, LinkedList<ItemStack> requirements) {
        Collections.addAll(requirements, this.storedRequirements);
    }

    public void writeToWorld(IBuilderContext context) {
        Entity e = EntityList.func_75615_a(this.entityNBT, context.world());
        context.world().func_72838_d(e);
    }

    public void readFromWorld(IBuilderContext context, Entity entity) {
        entity.func_70039_c(this.entityNBT);
    }

    public void translateToBlueprint(Translation transform) {
        NBTTagList nbttaglist = this.entityNBT.func_150295_c("Pos", 6);
        Position pos = new Position(nbttaglist.func_150309_d(0), nbttaglist.func_150309_d(1), nbttaglist.func_150309_d(2));
        pos = transform.translate(pos);
        this.entityNBT.func_74782_a("Pos", this.newDoubleNBTList(pos.x, pos.y, pos.z));
    }

    public void translateToWorld(Translation transform) {
        NBTTagList nbttaglist = this.entityNBT.func_150295_c("Pos", 6);
        Position pos = new Position(nbttaglist.func_150309_d(0), nbttaglist.func_150309_d(1), nbttaglist.func_150309_d(2));
        pos = transform.translate(pos);
        this.entityNBT.func_74782_a("Pos", this.newDoubleNBTList(pos.x, pos.y, pos.z));
    }

    public void idsToBlueprint(MappingRegistry registry) {
        registry.scanAndTranslateStacksToRegistry(this.entityNBT);
    }

    public void idsToWorld(MappingRegistry registry) {
        try {
            registry.scanAndTranslateStacksToWorld(this.entityNBT);
        } catch (MappingNotFoundException var3) {
            this.entityNBT = new NBTTagCompound();
        }

    }

    public void rotateLeft(IBuilderContext context) {
        NBTTagList nbttaglist = this.entityNBT.func_150295_c("Pos", 6);
        Position pos = new Position(nbttaglist.func_150309_d(0), nbttaglist.func_150309_d(1), nbttaglist.func_150309_d(2));
        pos = context.rotatePositionLeft(pos);
        this.entityNBT.func_74782_a("Pos", this.newDoubleNBTList(pos.x, pos.y, pos.z));
        nbttaglist = this.entityNBT.func_150295_c("Rotation", 5);
        float yaw = nbttaglist.func_150308_e(0);
        yaw += 90.0F;
        this.entityNBT.func_74782_a("Rotation", this.newFloatNBTList(yaw, nbttaglist.func_150308_e(1)));
    }

    public void writeSchematicToNBT(NBTTagCompound nbt, MappingRegistry registry) {
        super.writeSchematicToNBT(nbt, registry);
        nbt.func_74768_a("entityId", registry.getIdForEntity(this.entity));
        nbt.func_74782_a("entity", this.entityNBT);
        NBTTagList rq = new NBTTagList();
        ItemStack[] arr$ = this.storedRequirements;
        int len$ = arr$.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            ItemStack stack = arr$[i$];
            NBTTagCompound sub = new NBTTagCompound();
            stack.func_77955_b(stack.func_77955_b(sub));
            sub.func_74768_a("id", registry.getIdForItem(stack.func_77973_b()));
            rq.func_74742_a(sub);
        }

        nbt.func_74782_a("rq", rq);
    }

    public void readSchematicFromNBT(NBTTagCompound nbt, MappingRegistry registry) {
        super.readSchematicFromNBT(nbt, registry);
        this.entityNBT = nbt.func_74775_l("entity");
        NBTTagList rq = nbt.func_150295_c("rq", 10);
        ArrayList<ItemStack> rqs = new ArrayList();

        for(int i = 0; i < rq.func_74745_c(); ++i) {
            try {
                NBTTagCompound sub = rq.func_150305_b(i);
                if (sub.func_74762_e("id") >= 0) {
                    sub.func_74768_a("id", Item.field_150901_e.func_148757_b(registry.getItemForId(sub.func_74762_e("id"))));
                    rqs.add(ItemStack.func_77949_a(sub));
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
            nbttaglist.func_74742_a(new NBTTagDouble(d1));
        }

        return nbttaglist;
    }

    protected NBTTagList newFloatNBTList(float... par1ArrayOfFloat) {
        NBTTagList nbttaglist = new NBTTagList();
        float[] afloat = par1ArrayOfFloat;
        int i = par1ArrayOfFloat.length;

        for(int j = 0; j < i; ++j) {
            float f1 = afloat[j];
            nbttaglist.func_74742_a(new NBTTagFloat(f1));
        }

        return nbttaglist;
    }

    public boolean isAlreadyBuilt(IBuilderContext context) {
        NBTTagList nbttaglist = this.entityNBT.func_150295_c("Pos", 6);
        Position newPosition = new Position(nbttaglist.func_150309_d(0), nbttaglist.func_150309_d(1), nbttaglist.func_150309_d(2));
        Iterator i$ = context.world().field_72996_f.iterator();

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

    public int buildTime() {
        return 5;
    }
}
