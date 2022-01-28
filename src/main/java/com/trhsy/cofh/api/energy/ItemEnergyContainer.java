package com.trhsy.cofh.api.energy;/**
 * @author trhsy
 * @date 2022/1/27 0027
 * @apiNote
 */

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * ========================================
 *
 * @ClassName ItemEnergyContainer
 * @Description todo
 * @Author Administrator
 * @Date 2022/1/27 0027下午 1:00
 * ========================================
 **/
public class ItemEnergyContainer extends Item implements IEnergyContainerItem {
    protected int capacity;
    protected int maxReceive;
    protected int maxExtract;

    public ItemEnergyContainer() {
    }

    public ItemEnergyContainer(int capacity) {
        this(capacity, capacity, capacity);
    }

    public ItemEnergyContainer(int capacity, int maxTransfer) {
        this(capacity, maxTransfer, maxTransfer);
    }

    public ItemEnergyContainer(int capacity, int maxReceive, int maxExtract) {
        this.capacity = capacity;
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
    }

    public ItemEnergyContainer setCapacity(int capacity) {
        this.capacity = capacity;
        return this;
    }

    public void setMaxTransfer(int maxTransfer) {
        this.setMaxReceive(maxTransfer);
        this.setMaxExtract(maxTransfer);
    }

    public void setMaxReceive(int maxReceive) {
        this.maxReceive = maxReceive;
    }

    public void setMaxExtract(int maxExtract) {
        this.maxExtract = maxExtract;
    }

    @Override
    public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
        if (container.field_77990_d == null) {
            container.field_77990_d = new NBTTagCompound();
        }

        int energy = container.field_77990_d.getInteger("Energy");
        int energyReceived = Math.min(this.capacity - energy, Math.min(this.maxReceive, maxReceive));
        if (!simulate) {
            energy += energyReceived;
            container.field_77990_d.setInteger("Energy", energy);
        }

        return energyReceived;
    }

    @Override
    public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
        if (container.field_77990_d != null && container.field_77990_d.hasKey("Energy")) {
            int energy = container.field_77990_d.getInteger("Energy");
            int energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));
            if (!simulate) {
                energy -= energyExtracted;
                container.field_77990_d.setInteger("Energy", energy);
            }

            return energyExtracted;
        } else {
            return 0;
        }
    }

    @Override
    public int getEnergyStored(ItemStack container) {
        return container.field_77990_d != null && container.field_77990_d.hasKey("Energy") ? container.field_77990_d.getInteger("Energy") : 0;
    }

    @Override
    public int getMaxEnergyStored(ItemStack container) {
        return this.capacity;
    }
}
