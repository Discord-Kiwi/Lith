package mod.akrivus.revolution.entity;

import mod.akrivus.revolution.lang.PhonicsHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityHuman extends EntityMob implements IAnimals {
	protected static final DataParameter<String> FIRST_NAME = EntityDataManager.createKey(EntityHuman.class, DataSerializers.STRING);
	protected static final DataParameter<String> TRIBE_NAME = EntityDataManager.createKey(EntityHuman.class, DataSerializers.STRING);
	protected static final DataParameter<Integer> AGE = EntityDataManager.createKey(EntityHuman.class, DataSerializers.VARINT);
	protected static final DataParameter<Float> SIZE = EntityDataManager.createKey(EntityHuman.class, DataSerializers.FLOAT);
	protected static final DataParameter<Integer> HAIR_COLOR = EntityDataManager.createKey(EntityHuman.class, DataSerializers.VARINT);
	protected static final DataParameter<Integer> HAIR_TYPE = EntityDataManager.createKey(EntityHuman.class, DataSerializers.VARINT);
	protected static final DataParameter<Integer> EYE_COLOR = EntityDataManager.createKey(EntityHuman.class, DataSerializers.VARINT);
	protected static final DataParameter<Integer> SKIN_COLOR = EntityDataManager.createKey(EntityHuman.class, DataSerializers.VARINT);
	protected int skinBaseColor;
	protected boolean canTan;
	protected boolean canHairGray;
	protected double immuneStrength;
	protected double altitudeStrength;
	protected double heatStrength;
	protected double voicePitch;
	protected boolean homosexual;
	protected double maxAgeFactor;
	protected double foodLevels;
	
	public EntityHuman(World world) {
		super(world);
		this.dataManager.register(FIRST_NAME, PhonicsHelper.generateName(6, 3));
		this.dataManager.register(TRIBE_NAME, PhonicsHelper.generateName());
		this.dataManager.register(AGE, 0);
		this.dataManager.register(SIZE, 1.0F);
		this.dataManager.register(HAIR_COLOR, 0);
		this.dataManager.register(HAIR_TYPE, 0);
		this.dataManager.register(EYE_COLOR, 0);
		this.dataManager.register(SKIN_COLOR, 0);
		this.experienceValue = 0;
	}
	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
    }
	@Override
    public void readEntityFromNBT(NBTTagCompound compound) {
       super.readEntityFromNBT(compound);
    }
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
	}
	@Override
	public void onUpdate() {
        super.onUpdate();
        if (!this.world.isRemote && this.world.getDifficulty() == EnumDifficulty.PEACEFUL) {
            this.dead = this.getHealth() > 0;
        }
    }
	@Override
	public boolean isPreventingPlayerRest(EntityPlayer playerIn) {
        return false;
    }
	@Override
	public float getBlockPathWeight(BlockPos pos) {
        return 0.5F;
    }
	@Override
	public SoundCategory getSoundCategory() {
        return SoundCategory.NEUTRAL;
    }
	@Override
	public boolean canDespawn() {
		return false;
	}
	
	public void setStats(double strength, double stamina, double speed) {
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D * strength);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(20.0D * stamina);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.1D * speed);
	}
	public double getStrength() {
		return this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue() / 1.0D;
	}
	public double getStamina() {
		return this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue() / 20.0D;
	}
	public double getSpeed() {
		return this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue() / 0.1D;
	}
	public String getFirstName() {
		return this.dataManager.get(FIRST_NAME);
	}
	public void setFirstName(String firstName) {
		this.dataManager.set(FIRST_NAME, firstName);
	}
	public String getTribeName() {
		return this.dataManager.get(TRIBE_NAME);
	}
	public void setTribeName(String tribeName) {
		this.dataManager.set(TRIBE_NAME, tribeName);
	}
	public int getAge() {
		return this.dataManager.get(AGE);
	}
	public void setAge(int age) {
		this.dataManager.set(AGE, age);
	}
	public float getSize() {
		return this.dataManager.get(SIZE);
	}
	public void setSize(float size) {
		this.setSize(0.6F * size, 1.95F * size);
		this.dataManager.set(SIZE, size);
	}
	public int getHairColor() {
		return this.dataManager.get(HAIR_COLOR);
	}
	public void setHairColor(int hairColor) {
		this.dataManager.set(HAIR_COLOR, hairColor);
	}
	public int getHairType() {
		return this.dataManager.get(HAIR_TYPE);
	}
	public void setHairType(int hairType) {
		this.dataManager.set(HAIR_TYPE, hairType);
	}
	public int getEyeColor() {
		return this.dataManager.get(EYE_COLOR);
	}
	public void setEyeColor(int eyeColor) {
		this.dataManager.set(EYE_COLOR, eyeColor);
	}
	public int getSkinColor() {
		return this.dataManager.get(SKIN_COLOR);
	}
	public void setSkinColor(int skinColor) {
		this.dataManager.set(SKIN_COLOR, skinColor);
	}
	public int getBaseSkinColor() {
		return this.skinBaseColor;
	}
	public void setBaseSkinColor(int skinColor) {
		this.skinBaseColor = skinColor;
	}
	public boolean canTan() {
		return this.canTan;
	}
	public void setCanTan(boolean canTan) {
		this.canTan = canTan;
	}
	public boolean canHairGray() {
		return this.canHairGray;
	}
	public void setCanHairGray(boolean canHairGray) {
		this.canHairGray = canHairGray;
	}
	public double getImmuneStrength() {
		return this.immuneStrength;
	}
	public void setImmuneStrength(double immuneStrength) {
		this.immuneStrength = immuneStrength;
	}
	public double getAltitudeStrength() {
		return this.altitudeStrength;
	}
	public void setAltitudeStrength(double altitudeStrength) {
		this.altitudeStrength = altitudeStrength;
	}
	public double getHeatStrength() {
		return this.heatStrength;
	}
	public void setHeatStrength(double heatStrength) {
		this.heatStrength = heatStrength;
	}
	public double getVoicePitch() {
		return this.voicePitch;
	}
	public void setVoicePitch(double voicePitch) {
		this.voicePitch = voicePitch;
	}
	public double getAgeFactor() {
		return this.maxAgeFactor;
	}
	public void setAgeFactor(double maxAgeFactor) {
		this.maxAgeFactor = maxAgeFactor;
	}
	public double getFoodLevels() {
		return this.foodLevels;
	}
	public void setFoodLevels(int foodLevels) {
		this.foodLevels = foodLevels;
	}
}