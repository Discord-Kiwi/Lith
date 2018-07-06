package mod.akrivus.revolution.entity;

import java.util.UUID;

import mod.akrivus.revolution.data.Tribe;
import mod.akrivus.revolution.data.TribeData;
import mod.akrivus.revolution.lang.PhonicsHelper;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
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
	protected static final DataParameter<Integer> CLOTHES = EntityDataManager.createKey(EntityHuman.class, DataSerializers.VARINT);
	protected static final DataParameter<Integer> AGE = EntityDataManager.createKey(EntityHuman.class, DataSerializers.VARINT);
	protected static final DataParameter<Float> SIZE = EntityDataManager.createKey(EntityHuman.class, DataSerializers.FLOAT);
	protected static final DataParameter<Integer> HAIR_COLOR = EntityDataManager.createKey(EntityHuman.class, DataSerializers.VARINT);
	protected static final DataParameter<Integer> HAIR_TYPE = EntityDataManager.createKey(EntityHuman.class, DataSerializers.VARINT);
	protected static final DataParameter<Integer> EYE_COLOR = EntityDataManager.createKey(EntityHuman.class, DataSerializers.VARINT);
	protected static final DataParameter<Integer> SKIN_COLOR = EntityDataManager.createKey(EntityHuman.class, DataSerializers.VARINT);
	protected boolean canHairGray;
	protected double immuneStrength;
	protected double altitudeStrength;
	protected double heatStrength;
	protected double voicePitch;
	protected boolean homosexual;
	protected boolean fertile;
	protected double ageFactor;
	protected double foodLevels;
	protected double sickness;
	protected UUID tribe;
	protected InventoryBasic inventory;
	public EntityHuman(World world) {
		super(world);
		this.setCanPickUpLoot(true);
		this.inventory = new InventoryBasic("inventory", false, 36);
		this.dataManager.register(FIRST_NAME, PhonicsHelper.generateName(6, 3));
		this.dataManager.register(TRIBE_NAME, "");
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
        compound.setString("firstName", this.getFirstName());
        compound.setInteger("age", this.getAge());
        compound.setFloat("size", this.getSize());
        compound.setInteger("hairColor", this.getHairColor());
        compound.setInteger("hairType", this.getHairType());
        compound.setInteger("eyeColor", this.getEyeColor());
        compound.setInteger("skinColor", this.getSkinColor());
        compound.setBoolean("canHairGray", this.canHairGray());
        compound.setDouble("immuneStrength", this.getImmuneStrength());
        compound.setDouble("altitudeStrength", this.getAltitudeStrength());
        compound.setDouble("heatStrength", this.getHeatStrength());
        compound.setDouble("voicePitch", this.getVoicePitch());
        compound.setBoolean("homosexual", this.isHomosexual());
        compound.setBoolean("fertile", this.isFertile());
        compound.setDouble("ageFactor", this.getAgeFactor());
        compound.setDouble("foodLevels", this.getFoodLevels());
        compound.setDouble("sickness", this.getSickness());
        compound.setUniqueId("tribe", this.getTribeID());
        NBTTagList list = new NBTTagList();
        for (int i = 0; i < this.inventory.getSizeInventory(); ++i) {
            ItemStack itemstack = this.inventory.getStackInSlot(i);
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("slot", i);
            itemstack.writeToNBT(tag);
            list.appendTag(tag);
        }
        compound.setTag("inventory", list);
    }
	@Override
    public void readEntityFromNBT(NBTTagCompound compound) {
       super.readEntityFromNBT(compound);
       this.setFirstName(compound.getString("firstName"));
       this.setAge(compound.getInteger("age"));
       this.setSize(compound.getFloat("size"));
       this.setHairColor(compound.getInteger("hairColor"));
       this.setHairType(compound.getInteger("hairType"));
       this.setEyeColor(compound.getInteger("eyeColor"));
       this.setSkinColor(compound.getInteger("skinColor"));
       this.setCanHairGray(compound.getBoolean("canHairGray"));
       this.setImmuneStrength(compound.getDouble("immuneStrength"));
       this.setAltitudeStrength(compound.getDouble("altitudeStrength"));
       this.setHeatStrength(compound.getDouble("heatStrength"));
       this.setVoicePitch(compound.getDouble("voicePitch"));
       this.setHomosexual(compound.getBoolean("homosexual"));
       this.setFertile(compound.getBoolean("fertile"));
       this.setAgeFactor(compound.getDouble("ageFactor"));
       this.setFoodLevels(compound.getDouble("foodLevels"));
       this.setSickness(compound.getDouble("sickness"));
       this.setTribe(compound.getUniqueId("tribe"));
       NBTTagList list = compound.getTagList("inventory", 10);
       for (int i = 0; i < list.tagCount(); ++i) {
           NBTTagCompound tag = list.getCompoundTagAt(i);
           int slot = tag.getInteger("slot");
           if (slot >= 0 && slot < this.inventory.getSizeInventory()) {
               this.inventory.setInventorySlotContents(slot, new ItemStack(tag));
           }
       }
    }
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (this.world.isRemote) {
			float size = this.getSize() * (this instanceof EntityFemale ? 0.9F : 1.0F);
			if (0.6F * size != this.width || 1.95F * size != this.height) {
				this.setSize(0.6F * size, 1.95F * size);
			}
		}
		else {
			if (this.getTribeName().isEmpty() && this.getTribe() != null) {
				this.setTribeName(this.getTribe().name);
			}
		}
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
	@Override
	public boolean getAlwaysRenderNameTag() {
		return true;
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
	public int getClothes() {
		return this.dataManager.get(CLOTHES);
	}
	public void setClothes(int clothes) {
		this.dataManager.set(CLOTHES, clothes);
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
	public boolean isHomosexual() {
		return this.homosexual;
	}
	public void setHomosexual(boolean homosexual) {
		this.homosexual = homosexual;
	}
	public boolean isFertile() {
		return this.fertile;
	}
	public void setFertile(boolean fertile) {
		this.fertile = fertile;
	}
	public double getAgeFactor() {
		return this.ageFactor;
	}
	public void setAgeFactor(double ageFactor) {
		this.ageFactor = ageFactor;
	}
	public double getFoodLevels() {
		return this.foodLevels;
	}
	public void setFoodLevels(double foodLevels) {
		this.foodLevels = foodLevels;
	}
	public double getSickness() {
		return this.sickness;
	}
	public void setSickness(double sickness) {
		this.sickness = sickness;
	}
	public UUID getTribeID() {
		return this.tribe;
	}
	public Tribe getTribe() {
		return TribeData.get(this.world).getTribe(this.tribe);
	}
	public void setTribe(UUID tribe) {
		this.tribe = tribe;
	}
	public InventoryBasic getInventory() {
		return this.inventory;
	}
}