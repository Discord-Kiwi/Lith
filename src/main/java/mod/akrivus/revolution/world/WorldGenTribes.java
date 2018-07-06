package mod.akrivus.revolution.world;

import java.util.Random;

import mod.akrivus.revolution.entity.EntityFemale;
import mod.akrivus.revolution.entity.EntityHuman;
import mod.akrivus.revolution.entity.EntityMale;
import mod.akrivus.revolution.entity.Humans;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenTribes implements IWorldGenerator {
	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		if (world.provider.getDimensionType() == DimensionType.OVERWORLD) {
			BlockPos pos = new BlockPos(chunkX * 16 + random.nextInt(16), random.nextInt(96) + 64, chunkZ * 16 + random.nextInt(16));
			if (world.getBlockState(pos).isTopSolid()) {
				int total = random.nextInt(6) + 6;
				EntityHuman base = Humans.create(world, pos);
				for (int count = 0; count < total; ++count) {
					EntityHuman human = Humans.gen(base);
					human.setPosition(pos.getX() + (random.nextInt(total) - 6), world.getTopSolidOrLiquidBlock(pos).getY() + 1, pos.getZ() + (random.nextInt(total) - 6));
					human.onInitialSpawn(world.getDifficultyForLocation(pos), null);
					world.spawnEntity(human);
				}
			}
		}
	}
}
