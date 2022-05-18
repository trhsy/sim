package com.trhsy.sim.common.worldgen;

import com.trhsy.sim.common.loader.BlockLoader;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.OreGenEvent;
import net.minecraftforge.event.terraingen.TerrainGen;

import java.util.Random;

/**
 * 铜矿物生成
 */
public class WorldGeneratorCopperOre extends WorldGenerator {
    /**
     * 第一个参数表示生成的矿物的方块状态，方块状态的相关内容在高级部分会有所讲解，这里只需要知道通过方块的getDefaultState方法获取这个方块状态就可以了。比如这里我们需要生成萤石，我们就可以通过Blocks.glowstone.getDefaultState()的方式获取到这个方块状态
     * 第二个参数表示生成的矿物的大小，当然实际大小是会有出入的，这里我们定为16
     */
    private final WorldGenerator glowstoneGenerator = new WorldGenMinable(BlockLoader.blockCopperOre.getDefaultState(), 16);

    /**
     * 这个方法的返回值代表该生成是否成功，如果不成功游戏可能会试图重新调用这个方法生成一次，这里我们让它永远为true
     *
     * @param world 当前待生成的世界
     * @param rand  一个和当前世界种子、当前区块的x坐标（第二个参数）、和当前区块的z坐标（第三个参数）相关的随机数发生器，换言之，如果当前即将生成的是同一个世界种子和同一个区块，这个随机数发生器总会是一致的。所以，为了保证相同的种子生成相同的世界，请在世界生成的时候只使用这个随机数发生器。
     * @param pos   当前待生成的区块，一般而言，传入该方法的该参数，其Y坐标永远为0，X坐标和Z坐标代表该区块的西北方向，也就是X坐标和Z坐标最小的地方，同时X坐标和Z坐标都是16的倍数
     * @return
     */
    @Override
    public boolean generate(World world, Random rand, BlockPos pos) {
        // TODO
        if (TerrainGen.generateOre(world, rand, this, pos, OreGenEvent.GenerateMinable.EventType.CUSTOM)) {
            System.out.println("开始生成铜矿");
            for (int i = 0; i < 4; ++i) {
                //通过循环四次的方式在当前区块进行四次矿物生成
                int posX = pos.getX() + rand.nextInt(32);
                int posY = 16 + rand.nextInt(32);
                int posZ = pos.getZ() + rand.nextInt(32);
                BlockPos blockpos = new BlockPos(posX, posY, posZ);
                //随机在当前区块内生成XYZ三个坐标值，当然这里我们需要使用Forge提供的随机数生成器，不难看出，这里我们设定萤石的生成范围是Y坐标（也就是纵坐标）从16到32，X坐标和Z坐标也没有超出一个区块的范围。
                BiomeGenBase biomeGenBase = world.getBiomeGenForCoords(blockpos);
                //在世界上生成的矿物，还需要依赖于生物群系，比如绿宝石的生成就和生物群系密切相关
                //System.out.println("铜矿降雨量："+biomeGenBase.getIntRainfall());
                //if (biomeGenBase.getIntRainfall() < rand.nextInt(65536)) {
                    glowstoneGenerator.generate(world, rand, blockpos);
                //}
            }
        }
        return true;
    }
}
