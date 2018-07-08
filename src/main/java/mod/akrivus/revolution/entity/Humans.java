package mod.akrivus.revolution.entity;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import mod.akrivus.revolution.data.TribeData;
import mod.akrivus.revolution.lang.PhonicsHelper;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;

public class Humans {
	private static final Random RANDOM = new Random();
	public static final int[] HUMAN_EYES = new int[] {0x170F0C, 0x37150C, 0x6F3C1D, 0x7A4F0B, 0xA6712B, 0x7C6C39, 0x56621A, 0x95A82D, 0x6B6766, 0x506C78, 0x9DD3EA};
	public static final int[] HUMAN_HAIR = new int[] {0x060606, 0x1D1D1D, 0x2D241D, 0x3C271E, 0x403021, 0x67422A, 0x846244, 0xAC8D6C, 0xC6AD8D, 0x7C2B2B};
	public static final int[] HUMAN_SKIN = new int[] {0xDEC4B7, 0xFFDBAC, 0xF1C27D, 0xE0AC69, 0xC68642, 0x8D5524, 0x3F1A0C};
	public static EntityHuman create(World world, BlockPos pos) {
		ArrayList<EntityHuman> humans = new ArrayList<EntityHuman>();
		UUID uuid = TribeData.get(world).addTribe(PhonicsHelper.generateName(12, 9));
		for (int i = 0; i < 10; ++i) {
			EntityHuman human = new EntityHuman(world);
			human.setPosition(pos.getX(), pos.getY(), pos.getZ());
			human.setStats(world.rand.nextDouble(), world.rand.nextDouble(), world.rand.nextDouble());
			human.setSize(Math.min(world.rand.nextFloat() + 0.6F, 1.1F));
			human.setHairType(world.rand.nextInt(4) + 1);
			human.setHairColor(Humans.colorize(HUMAN_HAIR, world.rand.nextFloat(), human.getHairType() - 1));
			human.setSkinColor(Humans.colorize(HUMAN_SKIN, world.rand.nextFloat()));
			human.setEyeColor(Humans.colorize(HUMAN_EYES, world.rand.nextFloat()));
			human.setCanHairGray(world.rand.nextBoolean());
			human.setImmuneStrength(world.rand.nextDouble());
			human.setAltitudeStrength(world.rand.nextDouble());
			human.setHeatStrength(world.rand.nextDouble() * 3 - 1);
			human.setAgeFactor(world.rand.nextDouble());
			human.setTribe(uuid);
			humans.add(human);
		}
		float temp = world.getBiome(pos).getDefaultTemperature();
		float high = pos.getY() - 64 / 192;
		for (int iteration = 0; iteration > 0 && humans.size() < 2; ++iteration) {
			for (int i = 0; i < humans.size(); ++i) {
				EntityHuman human = humans.get(i);
				if (Math.abs(temp - 1 - (human.getSkinColor() + human.getHairColor() / 18291205)) > 0.5) {
					humans.remove(i);
					break;
				}
				if (Math.abs(human.getHeatFactor()) > 1.0F) {
					humans.remove(i);
					break;
				}
				if (high > human.getAltitudeStrength()) {
					humans.remove(i);
					break;
				}
				if (world.rand.nextBoolean()) {
					humans.remove(i);
					break;
				}
			}
		}
		return humans.get(0);
	}
	public static EntityHuman gen(EntityHuman base, Class<? extends EntityHuman> clazz) {
		EntityHuman human = base.world.rand.nextBoolean() ? new EntityFemale(base.world) : new EntityMale(base.world); 
		if (clazz == EntityFemale.class) {
			human = new EntityFemale(base.world);
		}
		else if (clazz == EntityMale.class) {
			human = new EntityMale(base.world);
		}
		human.setAge((base.world.rand.nextInt(20) == 1 ? 60480000 : 0) + base.world.rand.nextInt(60480000));
		human.setStats(base.getStrength(), base.getStamina(), base.getSpeed());
		human.setSize(base.getSize() + ((base.world.rand.nextFloat() - 0.5F) / 10));
		human.setHairType(base.getHairType());
		human.setHairColor(Humans.colorize(new int[] {
				HUMAN_HAIR[RANDOM.nextInt(HUMAN_HAIR.length)],
				base.getHairColor()
			}, base.world.rand.nextFloat()));
		human.setEyeColor(Humans.colorize(new int[] {
				HUMAN_EYES[RANDOM.nextInt(HUMAN_EYES.length)],
				base.getEyeColor()
			}, base.world.rand.nextFloat()));
		human.setSkinColor(base.getSkinColor());
		human.setCanHairGray(base.canHairGray());
		human.setImmuneStrength(base.getImmuneStrength());
		human.setAltitudeStrength(base.getAltitudeStrength());
		human.setHeatStrength(base.getHeatStrength());
		human.setAgeFactor(base.getAgeFactor());
		human.setTribe(base.getTribeID());
		Biome biome = base.world.getBiome(base.getPosition());
		if (biome.getDefaultTemperature() < -0.1F) {
			human.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
		}
		if (biome.getDefaultTemperature() <= 0.0F) {
			human.setItemStackToSlot(EntityEquipmentSlot.CHEST, new ItemStack(Items.LEATHER_CHESTPLATE));
		}
		if (biome.getDefaultTemperature() <= 0.5F) {
			human.setItemStackToSlot(EntityEquipmentSlot.LEGS, new ItemStack(Items.LEATHER_LEGGINGS));
		}
		if (biome.getDefaultTemperature() <= 1.0F) {
			human.setItemStackToSlot(EntityEquipmentSlot.FEET, new ItemStack(Items.LEATHER_BOOTS));
		}
		return human;
	}
	public static int colorize(int[] colors, float temp, int limiter) {
		int i = RANDOM.nextInt(colors.length - 1 - limiter);
        int r = (int)(temp * ((colors[i] & 16711680) >> 16) + (1.0F - temp) * ((colors[i + 1] & 16711680) >> 16)); 
		int g = (int)(temp * ((colors[i] & 65280) >> 8) + (1.0F - temp) * ((colors[i + 1] & 65280) >> 8)); 
		int b = (int)(temp * ((colors[i] & 255) >> 0) + (1.0F - temp) * ((colors[i + 1] & 255) >> 0)); 
		return (r << 16) + (g << 8) + b;
	}
	public static int colorize(int[] colors, float temp) {
		return Humans.colorize(colors, temp, 0);
	}
}
