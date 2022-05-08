package com.trhsy.sim.common.block;

import com.trhsy.sim.common.loader.CreativeTabsLoader;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;

/**
 * @ClassName BlockMarker
 * @Description todo 标记棒
 * @Author Tian
 * @Date 2022/5/523:22
 **/
public class BlockMarker extends Block implements IExtendedEntityProperties {
    public BlockMarker() {
        super(Material.wood);
        this.setStepSound(Block.soundTypeWood);
        this.setHardness(2.0F);
        this.setResistance(1.0F);
        this.setUnlocalizedName("markerBar");
        this.setBlockBounds(0.4F, 0.0F, 0.4F, 0.6F, 0.9F, 0.6F);
        this.setLightLevel(0.1F);
        //this.setTextureName(ModSim.MODID + ":" + "marker_bar_block");
        this.setCreativeTab(CreativeTabsLoader.tabSimU);

    }

    @Override
    public void setBlockBoundsForItemRender() {
        this.setBlockBounds(0.4F, 0.0F, 0.4F, 0.6F, 0.9F, 0.6F);
    }
    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public void saveNBTData(NBTTagCompound compound) {

    }

    @Override
    public void loadNBTData(NBTTagCompound compound) {

    }

    @Override
    public void init(Entity entity, World world) {

    }
}
