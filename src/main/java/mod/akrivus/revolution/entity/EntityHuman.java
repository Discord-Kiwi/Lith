package mod.akrivus.revolution.entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import mod.akrivus.revolution.Revolution;
import mod.akrivus.revolution.data.LearnedData;
import mod.akrivus.revolution.data.Memory;
import mod.akrivus.revolution.data.Tribe;
import mod.akrivus.revolution.data.TribeData;
import mod.akrivus.revolution.entity.ai.EntityAIAvoidFromMemory;
import mod.akrivus.revolution.entity.ai.EntityAICraftItems;
import mod.akrivus.revolution.entity.ai.EntityAIEat;
import mod.akrivus.revolution.entity.ai.EntityAIFindHome;
import mod.akrivus.revolution.entity.ai.EntityAIFollowMom;
import mod.akrivus.revolution.entity.ai.EntityAIFollowOldest;
import mod.akrivus.revolution.entity.ai.EntityAIForage;
import mod.akrivus.revolution.entity.ai.EntityAIGoHome;
import mod.akrivus.revolution.entity.ai.EntityAIGoToMemory;
import mod.akrivus.revolution.entity.ai.EntityAIGroceryList;
import mod.akrivus.revolution.entity.ai.EntityAIHarvestFarmland;
import mod.akrivus.revolution.entity.ai.EntityAIHaveIdeas;
import mod.akrivus.revolution.entity.ai.EntityAIPickUpItems;
import mod.akrivus.revolution.entity.ai.EntityAISleep;
import mod.akrivus.revolution.entity.ai.EntityAISpeak;
import mod.akrivus.revolution.entity.ai.EntityAIStepAroundMemory;
import mod.akrivus.revolution.entity.ai.EntityAITargetFromMemory;
import mod.akrivus.revolution.entity.ai.EntityAITillFarmland;
import mod.akrivus.revolution.lang.PhonicsHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.Item;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemSeedFood;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

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
	protected static final DataParameter<Integer> BEARD_TYPE = EntityDataManager.createKey(EntityHuman.class, DataSerializers.VARINT);
	protected static final DataParameter<Boolean> HAIR_GRAYS = EntityDataManager.createKey(EntityHuman.class, DataSerializers.BOOLEAN);
	protected static final DataParameter<Boolean> HAIR_LOSS = EntityDataManager.createKey(EntityHuman.class, DataSerializers.BOOLEAN);
	protected static final DataParameter<Boolean> SLEEPING = EntityDataManager.createKey(EntityHuman.class, DataSerializers.BOOLEAN);
	
	public Block lastBlockBreak = Blocks.AIR;
	public List<Item> groceryList;
	
	protected double immuneStrength;
	protected double altitudeStrength;
	protected double heatStrength;
	protected double voicePitch;
	protected boolean homosexual;
	protected boolean fertile;
	protected double ageFactor;
	protected double foodLevels;
	protected double sickness;
	
	protected int blockTicks;
	
	protected UUID tribe;
	protected InventoryBasic inventory;
	protected List<UUID> memories;
	
	public EntityHuman(World world) {
		super(world);
		this.setCanPickUpLoot(true);
		this.tasks.addTask(0, new EntityAISleep(this));
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAISpeak(this, 2));
		this.tasks.addTask(1, new EntityAIFollowMom(this, 1.0D));
		this.tasks.addTask(2, new EntityAIGoHome(this));
		this.tasks.addTask(2, new EntityAIStepAroundMemory(this));
		this.tasks.addTask(2, new EntityAIAvoidFromMemory(this, 8, 1.0D));
		this.tasks.addTask(3, new EntityAIPickUpItems(this, 1.0D));
		this.tasks.addTask(3, new EntityAIAttackMelee(this, 1.0F, false));
		this.tasks.addTask(4, new EntityAIFindHome(this));
		this.tasks.addTask(4, new EntityAIEat(this, 8));
		this.tasks.addTask(5, new EntityAIGroceryList(this));
		this.tasks.addTask(6, new EntityAIHarvestFarmland(this, 0.8D));
		this.tasks.addTask(6, new EntityAITillFarmland(this, 0.8D));
		this.tasks.addTask(7, new EntityAIForage(this));
		this.tasks.addTask(7, new EntityAIGoToMemory(this));
		this.tasks.addTask(8, new EntityAICraftItems(this));
		this.tasks.addTask(8, new EntityAIHaveIdeas(this));
		this.tasks.addTask(9, new EntityAIFollowOldest(this, 1.0D));
		this.targetTasks.addTask(1, new EntityAITargetFromMemory(this));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, true, new Class[0]));
		this.inventory = new InventoryBasic("inventory", false, 36);
		this.memories = new ArrayList<UUID>();
		this.groceryList = new ArrayList<Item>();
		this.dataManager.register(FIRST_NAME, PhonicsHelper.generateName(6, 3));
		this.dataManager.register(TRIBE_NAME, "");
		this.dataManager.register(AGE, 0);
		this.dataManager.register(SIZE, 1.0F);
		this.dataManager.register(HAIR_COLOR, 0);
		this.dataManager.register(HAIR_TYPE, 0);
		this.dataManager.register(EYE_COLOR, 0);
		this.dataManager.register(SKIN_COLOR, 0);
		this.dataManager.register(BEARD_TYPE, 0);
		this.dataManager.register(HAIR_GRAYS, false);
		this.dataManager.register(HAIR_LOSS, false);
		this.dataManager.register(SLEEPING, false);
		this.experienceValue = 0;
		this.foodLevels = 20;
	}
	@Override
	public boolean isAIDisabled() {
		return false;
	}
	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setDouble("strength", this.getStrength());
        compound.setDouble("stamina", this.getStrength());
        compound.setDouble("speed", this.getStrength());
        compound.setString("firstName", this.getFirstName());
        compound.setInteger("age", this.getAge());
        compound.setFloat("size", this.getSize());
        compound.setInteger("hairColor", this.getHairColor());
        compound.setInteger("hairType", this.getHairType());
        compound.setInteger("eyeColor", this.getEyeColor());
        compound.setInteger("skinColor", this.getSkinColor());
        compound.setInteger("beardType", this.getBeardType());
        compound.setBoolean("canHairGray", this.canHairGray());
        compound.setBoolean("canLoseHair", this.canLoseHair());
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
        compound.setBoolean("isSleeping", this.isSleeping());
        NBTTagList inv = new NBTTagList();
        for (int i = 0; i < this.inventory.getSizeInventory(); ++i) {
            ItemStack itemstack = this.inventory.getStackInSlot(i);
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("slot", i);
            itemstack.writeToNBT(tag);
            inv.appendTag(tag);
        }
        compound.setTag("inventory", inv);
        NBTTagList mem = new NBTTagList();
        for (int i = 0; i < this.memories.size(); ++i) {
        	NBTTagCompound tag = new NBTTagCompound();
        	tag.setString("id", this.memories.get(i).toString());
        	mem.appendTag(tag);
        }
        compound.setTag("memories", mem);
    }
	@Override
    public void readEntityFromNBT(NBTTagCompound compound) {
       super.readEntityFromNBT(compound);
       this.setStats(compound.getDouble("strength"), compound.getDouble("stamina"), compound.getDouble("speed"));
       this.setFirstName(compound.getString("firstName"));
       this.setAge(compound.getInteger("age"));
       this.setSize(compound.getFloat("size"));
       this.setHairColor(compound.getInteger("hairColor"));
       this.setHairType(compound.getInteger("hairType"));
       this.setEyeColor(compound.getInteger("eyeColor"));
       this.setBeardType(compound.getInteger("beardType"));
       this.setSkinColor(compound.getInteger("skinColor"));
       this.setCanHairGray(compound.getBoolean("canHairGray"));
       this.setCanLoseHair(compound.getBoolean("canLoseHair"));
       this.setImmuneStrength(compound.getDouble("immuneStrength"));
       this.setAltitudeStrength(compound.getDouble("altitudeStrength"));
       this.setHeatStrength(compound.getDouble("heatStrength"));
       this.setVoicePitch(compound.getDouble("voicePitch"));
       this.setIsHomosexual(compound.getBoolean("homosexual"));
       this.setIsFertile(compound.getBoolean("fertile"));
       this.setAgeFactor(compound.getDouble("ageFactor"));
       this.setFoodLevels(compound.getDouble("foodLevels"));
       this.setSickness(compound.getDouble("sickness"));
       this.setTribe(compound.getUniqueId("tribe"));
       this.setIsSleeping(compound.getBoolean("isSleeping"));
       NBTTagList inv = compound.getTagList("inventory", 10);
       for (int i = 0; i < inv.tagCount(); ++i) {
           NBTTagCompound tag = inv.getCompoundTagAt(i);
           int slot = tag.getInteger("slot");
           if (slot >= 0 && slot < this.inventory.getSizeInventory()) {
               this.inventory.setInventorySlotContents(slot, new ItemStack(tag));
           }
       }
       NBTTagList mem = compound.getTagList("memories", 10);
       for (int i = 0; i < mem.tagCount(); ++i) {
           NBTTagCompound tag = mem.getCompoundTagAt(i);
           UUID id = UUID.fromString(tag.getString("id"));
           this.memories.add(id);
       }
    }
	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingdata) {
		livingdata = super.onInitialSpawn(difficulty, livingdata);
		if (this.getTribeID() == null) {
			EntityHuman base = Humans.gen(Humans.create(this.world, this.getPosition()), this.getClass());
			this.setAge(70560000);
			this.setStats(base.getStrength(), base.getStamina(), base.getSpeed());
			this.setSize(base.getSize() + ((base.world.rand.nextFloat() - 0.5F) / 10));
			this.setHairType(base.getHairType());
			this.setHairColor(base.getHairColor());
			this.setEyeColor(base.getEyeColor());
			this.setSkinColor(base.getSkinColor());
			this.setCanHairGray(base.canHairGray());
			this.setCanLoseHair(base.canLoseHair());
			this.setBeardType(base.getBeardType());
			this.setImmuneStrength(base.getImmuneStrength());
			this.setAltitudeStrength(base.getAltitudeStrength());
			this.setHeatStrength(base.getHeatStrength());
			this.setAgeFactor(base.getAgeFactor());
			this.setTribe(base.getTribeID());
		}
		this.setHealth(this.getMaxHealth());
		return livingdata;
	}
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if ((this.world.getWorldTime() % 24000) < 14000 && this.isSleeping()) {
			this.setIsSleeping(false);
		}
		if (!this.world.isRemote) {
			++this.blockTicks;
			if (this.getAttackTarget() != null && this.getHeldItemMainhand().isEmpty() && this.ticksExisted % 20 == 0) {
				List<ItemStack> stacks = this.getStackList();
        		for (ItemStack stack : stacks) {
        			if (stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemAxe) {
        				this.setHeldItem(EnumHand.MAIN_HAND, stack.copy());
        			}
        		}
			}
			if (this.getTribeName().isEmpty() && this.getTribe() != null) {
				this.setTribeName(this.getTribe().getName());
			}
			if (!this.isSleeping()) {
				if (this.ticksExisted % 2400 == 0) {
					if (this.getImmuneFactor() > 0) {
						this.addPotionEffect(new PotionEffect(MobEffects.HUNGER, 4800));
					}
					Iterator<PotionEffect> it = this.getActivePotionEffects().iterator();
					while (it.hasNext()) {
						if (it.next().getPotion() == MobEffects.HUNGER) {
							this.depleteFoodLevels(1.0F + this.getImmuneFactor());
						}
					}
					if (this.world.rand.nextBoolean()) {
						this.setImmuneStrength(this.getImmuneStrength() + this.getSickness());
					}
					if (this.foodLevels <= 0) {
						this.attackEntityFrom(DamageSource.STARVE, 1.0F);
						this.foodLevels = 0;
					}
					else if (this.foodLevels > 18.0F && this.getHealth() < 20.0F) {
						this.heal(1.0F);
						this.depleteFoodLevels(0.25F);
					}
					else {
						this.depleteFoodLevels(0.01F);
					}
					this.setIsFertile(true);
					if (this.getAttackTarget() == null && !(this.getHeldItemMainhand().getItem() instanceof ItemHoe)) {
						List<ItemStack> stacks = this.getStackList();
		        		for (ItemStack stack : stacks) {
		        			if (stack.getItem() instanceof ItemHoe) {
		        				this.setHeldItem(EnumHand.MAIN_HAND, stack.copy());
		        			}
		        		}
					}
				}
			}
			else {
				if (this.ticksExisted % 2400 == 0) {
					List<String> values = new ArrayList<String>();
					List<UUID> removals = new ArrayList<UUID>();
					for (UUID memory : this.getMemories()) {
						Memory mem = LearnedData.get(this.world).memories.get(memory);
						if (values.contains(mem.getData())) {
							removals.add(memory);
						}
					}
					for (int i = 0; i < removals.size(); ++i) {
						this.getMemories().remove(removals.get(i));
					}
				}
			}
		}
		this.setAge(this.getAge() + (int)(Math.ceil(2.0 * this.getAgeFactor())));
		if (!this.isOldEnoughToBreed() && this.getAge() < 60480000) {
			if (this.ticksExisted % 20 == 0) {
				float growth = Math.min(this.getSize(), this.getSize() * (this.getAge() / (this instanceof EntityFemale ? 18144000.0F : 24192000.0F) * (this.getSize() / 3.0F)) + (this.getSize() / 4.0F));
				if (this instanceof EntityFemale) {
					this.setSize(0.6F * 0.9F * growth, 1.95F * 0.9F * growth);
				}
				else {
					this.setSize(0.6F * growth, 1.95F * growth);
				}
			}
		}
		else {
			float size = this.getSize() * (this instanceof EntityFemale ? 0.9F : 1.0F);
			if (0.6F * size != this.width || 1.95F * size != this.height) {
				this.setSize(0.6F * size, 1.95F * size);
			}
		}
	}
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount) {
		boolean hurt = super.attackEntityFrom(source, amount);
		if (hurt) {
			if (source == DamageSource.STARVE) {
				this.getTribe().setHomeless(this.world);
			}
			else {
				this.depleteFoodLevels(0.1F);
				if (source.getTrueSource() instanceof EntityLivingBase) {
					EntityLivingBase target = (EntityLivingBase)(source.getTrueSource());
					boolean learned = true;
					Map<UUID, Memory> collective = LearnedData.get(this.world).memories;
					for (UUID id : this.memories) {
						if (target.getClass().getSimpleName().equals(collective.get(id).getData())) {
							learned = false;
							break;
						}
					}
					if (learned) {
						int factor = this.getAge() - 24192000;
						if (factor > 48384000 || factor < 0) {
							this.addMemory("FEAR", target);
						}
						else {
							if (factor / 48384000.0F < this.world.rand.nextDouble()) {
								this.addMemory("FIGHT", target);
							}
							else {
								this.addMemory("FEAR", target);
							}
						}
					}
					List<EntityHuman> list = this.world.<EntityHuman>getEntitiesWithinAABB(EntityHuman.class, this.getEntityBoundingBox().grow(8.0D, 4.0D, 8.0D));
					for (EntityHuman human : list) {
						if (human.getTribeID().equals(this.getTribeID())) {
							human.setRevengeTarget(target);
						}
					}
					List<ItemStack> stacks = this.getStackList();
	        		for (ItemStack stack : stacks) {
	        			if (stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemAxe) {
	        				this.setHeldItem(EnumHand.MAIN_HAND, stack.copy());
	        			}
	        		}
				}
				else {
					boolean learned = true;
					Map<UUID, Memory> collective = LearnedData.get(this.world).memories;
					for (UUID id : this.memories) {
						if (this.getPosition().equals(collective.get(id).getAvoid())) {
							learned = false;
							break;
						}
					}
					if (learned) {
						this.addMemory("AVOID", this.getPosition());
					}
				}
				if (!this.getTribe().isHomeless()) {
					if (this.getPosition().distanceSq(this.getTribe().getHome()) < 16.0F) {
						this.getTribe().setHomeless(this.world);
					}
				}
			}
			this.setIsSleeping(false);
		}
		return hurt;
	}
	@Override
	public void onKillEntity(EntityLivingBase target) {
		super.onKillEntity(target);
		boolean learned = true;
		Map<UUID, Memory> collective = LearnedData.get(this.world).memories;
		for (UUID id : this.memories) {
			if (target.getClass().getSimpleName().equals(collective.get(id).getFight())) {
				learned = false;
				break;
			}
		}
		if (learned) {
			boolean dropLoot = !this.world.<EntityItem>getEntitiesWithinAABB(EntityItem.class, this.getEntityBoundingBox().grow(2.0D, 2.0D, 2.0D)).isEmpty();
			if (this.getAttackTarget().equals(target) || dropLoot) {
				this.addMemory("FIGHT", target);
			}
			else {
				this.addMemory("IGNORE", target);
			}
		}
	}
	@Override
	public void onDeath(DamageSource cause) {
		if (!this.world.isRemote) {
			this.dropItem(Items.BONE, 2 + this.rand.nextInt(4));
			if (this.isBurning()) {
				this.dropItem(Revolution.COOKED_MAN_MEAT, 3 + this.rand.nextInt(6));
			}
			else {
				this.dropItem(Revolution.MAN_MEAT, 3 + this.rand.nextInt(6));
			}
			for (int i = 0; i < this.inventory.getSizeInventory(); ++i) {
				this.entityDropItem(this.inventory.getStackInSlot(i), 0.0F);
			}
		}
		super.onDeath(cause);
	}
	@Override
	public void heal(float healAmount) {
		super.heal(healAmount);
		if (healAmount > 1.0F) {
			this.addMemory("GOTO", this.getPosition());
		}
	}
	@Override
	public boolean attackEntityAsMob(Entity target) {
		boolean hit = super.attackEntityAsMob(target);
		if (hit) {
			this.depleteFoodLevels(0.1F);
		}
		return hit;
	}
	@Override
	public void jump() {
		this.depleteFoodLevels(0.2F);
		super.jump();
	}
	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand) {
		if (hand == EnumHand.MAIN_HAND) {
			if (player.isSneaking()) {
				// TODO: This activates the trade interface.
				player.displayGUIChest(this.inventory);
			}
			else if (!this.world.isRemote) {
				if (player.getHeldItem(hand).getItem() == Items.NAME_TAG) {
					this.setFirstName(player.getHeldItem(hand).getDisplayName());
				}
				else if (player.getHeldItem(hand).getItem() == Revolution.GENERATOR) {
					this.generate();
				}
				else if (player.getHeldItem(hand).getItem() == Revolution.MUTATOR) {
					this.mutate();
				}
				else if (player.getHeldItem(hand).getItem() == Revolution.AMPLIFIER) {
					if (this.getAge() > 48384000) {
						List<String> values = new ArrayList<String>();
						List<UUID> removals = new ArrayList<UUID>();
						for (UUID memory : this.getMemories()) {
							Memory mem = LearnedData.get(this.world).memories.get(memory);
							if (values.contains(mem.getData())) {
								removals.add(memory);
							}
						}
						for (int i = 0; i < removals.size(); ++i) {
							this.getMemories().remove(removals.get(i));
						}
					}
					else {
						this.setAge(48384000);
					}
				}
				else if (player.getHeldItem(hand).getItem() != Revolution.FERTILIZER) {
					player.sendMessage(new TextComponentString(this.getFirstName() + " of the " + this.getTribeName() + " tribe:"));
					player.sendMessage(new TextComponentString("Approximately " + (int)(this.getAge() / 2016000.0F) + " years old."));
					player.sendMessage(new TextComponentString((this.getImmuneFactor() > 0 ? "Sick, " : "Not sick, ") + (int)((this.getHealth() / this.getMaxHealth()) * 100) + "% healthy, " + (int)((this.foodLevels / 20) * 100) + "% full."));
					player.sendMessage(new TextComponentString("Has about " + this.getMemories().size() + " memories."));
				}
			}
		}
		return super.processInteract(player, hand);
	}
	@Override
	public void onUpdate() {
        super.onUpdate();
        if (!this.world.isRemote && this.world.getDifficulty() == EnumDifficulty.PEACEFUL) {
            this.dead = this.getHealth() > 0;
        }
    }
	@Override
	protected void updateEquipmentIfNeeded(EntityItem item) {
        ItemStack itemstack = item.getItem();
        ItemStack other = this.inventory.addItem(itemstack);
        Item itemItem = itemstack.getItem();
        if (itemItem instanceof ItemFood || itemItem instanceof ItemSeeds || itemItem instanceof ItemSeedFood) {
        	if (this.blockTicks > 20) {
        		boolean learned = true;
				Map<UUID, Memory> collective = LearnedData.get(this.world).memories;
				for (UUID id : this.getMemories()) {
					if (this.getPosition().equals(collective.get(id).getGoto())) {
						learned = false;
						break;
					}
				}
				if (learned) {
					this.addMemory("GOTO", this.getPosition());
				}
        	}
        	else {
        		boolean learned = true;
				Map<UUID, Memory> collective = LearnedData.get(this.world).memories;
				for (UUID id : this.getMemories()) {
					if (this.lastBlockBreak.getUnlocalizedName().equals(collective.get(id).getBreak())) {
						learned = false;
						break;
					}
				}
				if (learned) {
					this.addMemory("BREAK", this.lastBlockBreak);
				}
        	}
		}
        this.reverseEngineer(itemstack);
        if (!other.isEmpty()) {
        	itemstack.setCount(other.getCount());
        }
        else {
            item.setDead();
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
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(1.0D * Math.min(18.0D, strength) + 1.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(Math.max(20.0D, Math.min(120.0D, stamina * 20.0D + 20.0D)));
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.2D * Math.min(3.0D, speed) + 0.3D);
	}
	public double getStrength() {
		return (this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue() - 1.0D) / 1.0D;
	}
	public double getStamina() {
		return (this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue() - 20.0D) / 20.0D;
	}
	public double getSpeed() {
		return (this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getBaseValue() - 0.3D) / 0.2D;
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
		return this.dataManager.get(HAIR_GRAYS);
	}
	public void setCanHairGray(boolean canHairGray) {
		this.dataManager.set(HAIR_GRAYS, canHairGray);
	}
	public int getBeardType() {
		return this.dataManager.get(BEARD_TYPE);
	}
	public void setBeardType(int beardType) {
		this.dataManager.set(BEARD_TYPE, beardType);
	}
	public boolean canLoseHair() {
		return this.dataManager.get(HAIR_LOSS);
	}
	public void setCanLoseHair(boolean canLoseHair) {
		this.dataManager.set(HAIR_LOSS, canLoseHair);
	}
	public double getImmuneStrength() {
		return this.immuneStrength;
	}
	public double getImmuneFactor() {
		return Math.max(0, this.getSickness() - this.getImmuneStrength());
	}
	public void setImmuneStrength(double immuneStrength) {
		this.immuneStrength = immuneStrength;
	}
	public double getAltitudeStrength() {
		return this.altitudeStrength;
	}
	public double getAltitudeFactor() {
		return this.getAltitudeStrength() / this.posY - 64 / 192;
	}
	public void setAltitudeStrength(double altitudeStrength) {
		this.altitudeStrength = altitudeStrength;
	}
	public double getHeatStrength() {
		return this.heatStrength;
	}
	public double getHeatFactor() {
		return this.world.getBiome(this.getPosition()).getDefaultTemperature() - this.getHeatStrength() - (this.getTotalArmorValue() / 4);
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
	public void setIsHomosexual(boolean homosexual) {
		this.homosexual = homosexual;
	}
	public boolean isFertile() {
		return this.fertile;
	}
	public void setIsFertile(boolean fertile) {
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
	public void depleteFoodLevels(double base) {
		double orig = base;
		base *= this.getSize()
				+ this.getSickness()
				+ this.getAltitudeFactor()
				+ this.getHeatFactor()
				+ this.getImmuneFactor();
		this.foodLevels -= Math.max(orig, base);
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
	public void learnMemory(UUID id) {
		this.memories.add(id);
	}
	public void addMemory(String type, Entity data) {
		this.learnMemory(LearnedData.get(this.world).addMemory(new Memory(type, data)));
	}
	public void addMemory(String type, Block data) {
		this.learnMemory(LearnedData.get(this.world).addMemory(new Memory(type, data)));
	}
	public void addMemory(String type, BlockPos data) {
		this.learnMemory(LearnedData.get(this.world).addMemory(new Memory(type, data)));
	}
	public void addMemory(String type, Item data) {
		this.learnMemory(LearnedData.get(this.world).addMemory(new Memory(type, data)));
	}
	public void addMemory(String type, String data) {
		this.learnMemory(LearnedData.get(this.world).addMemory(new Memory(type, data)));
	}
	public List<UUID> getMemories() {
		return this.memories;
	}
	public void reverseEngineer(ItemStack itemstack) {
		boolean learned = true;
		Map<UUID, Memory> collective = LearnedData.get(this.world).memories;
		for (UUID id : this.getMemories()) {
			if (collective.get(id).getCraft() != null) {
				if (itemstack.getItem().getRegistryName().toString().equals(collective.get(id).getCraft().split(";")[0].split("/")[0])) {
					learned = false;
					break;
				}
			}
		}
		if (learned) {
			Iterator<Map.Entry<ResourceLocation, IRecipe>> it = ForgeRegistries.RECIPES.getEntries().iterator();
			while (it.hasNext()) {
				IRecipe recipe = it.next().getValue();
				if (itemstack.getItem().getRegistryName().equals(recipe.getRecipeOutput().getItem().getRegistryName())) {
					String data = recipe.getRecipeOutput().getItem().getRegistryName() + "/" + recipe.getRecipeOutput().getCount() + ";";
					for (Ingredient in : recipe.getIngredients()) {
						for (ItemStack stack : in.getMatchingStacks()) {
							data += stack.getItem().getRegistryName() + ";";
						}
					}
					data.replace(";$", "");
					this.addMemory("CRAFT", data);
				}
			}
		}
	}
	public ItemStack canCraft(Item item) {
		String[] recipe = new String[] { };
		boolean learned = false;
		Map<UUID, Memory> collective = LearnedData.get(this.world).memories;
		for (UUID id : this.getMemories()) {
			if (collective.get(id).getCraft() != null) {
				recipe = collective.get(id).getCraft().split(";");
				if (item.getRegistryName().equals(new ResourceLocation(recipe[0].split("/")[0]))) {
					learned = true;
					break;
				}
			}
		}
		if (learned) {
			for (int in = 1; in < recipe.length; ++in) {
				boolean found = false;
				for (int slot = 0; slot < this.inventory.getSizeInventory(); ++slot) {
					if (this.inventory.getStackInSlot(slot).getItem().getRegistryName().toString().equals(recipe[in])) {
						found = true;
						break;
					}
				}
				if (!found) {
					return new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(recipe[in])));
				}
			}
			return ItemStack.EMPTY;
		}
		return new ItemStack(Items.CAKE);
	}
	public void craft(Item item) {
		String[] recipe = new String[] { };
		boolean learned = false;
		Map<UUID, Memory> collective = LearnedData.get(this.world).memories;
		for (UUID id : this.getMemories()) {
			if (collective.get(id).getCraft() != null) {
				recipe = collective.get(id).getCraft().split(";");
				if (item.getRegistryName().equals(new ResourceLocation(recipe[0].split("/")[0]))) {
					learned = true;
					break;
				}
			}
		}
		if (learned) {
			for (int in = 1; in < recipe.length; ++in) {
				for (int slot = 0; slot < this.inventory.getSizeInventory(); ++slot) {
					if (this.inventory.getStackInSlot(slot).getItem().getRegistryName().toString().equals(recipe[in])) {
						this.inventory.getStackInSlot(slot).shrink(1);
					}
				}
			}
		}
		this.inventory.addItem(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(recipe[0].split("/")[0])), Integer.parseInt(recipe[0].split("/")[1])));
	}
	public List<ItemStack> getStackList() {
		List<ItemStack> list = new ArrayList<ItemStack>();
		for (int slot = 0; slot < this.inventory.getSizeInventory(); ++slot) {
			list.add(this.inventory.getStackInSlot(slot));
		}
		return list;
	}
	public boolean isSleeping() {
		return this.dataManager.get(SLEEPING);
	}
	public void setIsSleeping(boolean sleeping) {
		this.dataManager.set(SLEEPING, sleeping);
	}
	public boolean isOldEnoughToBreed() {
		return false;
	}
	public boolean isAroused() {
		return this.isFertile();
	}
	public void resetBlockTicks() {
		this.blockTicks = 0;
	}
	public void mutate() {
		EntityHuman human = Humans.gen(this, this.getClass());
		human = this.createChild(human);
		this.setStats(human.getStrength(), human.getStamina(), human.getSpeed());
		this.setSize(human.getSize());
		this.setHairType(human.getHairType());
		this.setHairColor(Humans.colorize(new int[] {
				Humans.HUMAN_HAIR[human.world.rand.nextInt(Humans.HUMAN_HAIR.length)],
				human.getHairColor()
			}, 1 + human.world.rand.nextFloat()));
		this.setSkinColor(Humans.colorize(new int[] {
				Humans.HUMAN_SKIN[human.world.rand.nextInt(Humans.HUMAN_SKIN.length)],
				human.getSkinColor()
			}, 1 + human.world.rand.nextFloat()));
		this.setEyeColor(Humans.colorize(new int[] {
				Humans.HUMAN_EYES[human.world.rand.nextInt(Humans.HUMAN_EYES.length)],
				human.getEyeColor()
			}, 1 + human.world.rand.nextFloat()));
		this.setCanHairGray(human.canHairGray());
		this.setImmuneStrength(human.getImmuneStrength());
		this.setAltitudeStrength(human.getAltitudeStrength());
		this.setHeatStrength(human.getHeatStrength());
		this.setAgeFactor(human.getAgeFactor());
	}
	public void generate() {
		EntityHuman human = Humans.gen(this, this.getClass());
		human = this.createChild(human);
		this.setStats(human.getStrength(), human.getStamina(), human.getSpeed());
		this.setSize(human.getSize());
		this.setHairType(human.getHairType());
		this.setHairColor(human.getHairColor());
		this.setSkinColor(human.getSkinColor());
		this.setEyeColor(human.getEyeColor());
		this.setCanHairGray(human.canHairGray());
		this.setImmuneStrength(human.getImmuneStrength());
		this.setAltitudeStrength(human.getAltitudeStrength());
		this.setHeatStrength(human.getHeatStrength());
		this.setAgeFactor(human.getAgeFactor());
	}
	public EntityHuman createChild(EntityHuman other) {
		EntityHuman human = new EntityHuman(this.world);
		human.setStats((this.getStrength() + other.getStrength()) / 2, (this.getStamina() + other.getStamina()) / 2, (this.getSpeed() + other.getSpeed()) / 2);
		human.setSize((this.getSize() + other.getSize()) / 2);
		human.setHairType((this.getHairType() + other.getHairType()) / 2);
		human.setHairColor(Humans.colorize(new int[] { this.getHairColor(), other.getHairColor() }, this.rand.nextFloat()));
		human.setSkinColor(Humans.colorize(new int[] { this.getSkinColor(), other.getSkinColor() }, this.rand.nextFloat()));
		human.setEyeColor(Humans.colorize(new int[] { this.getEyeColor(), other.getEyeColor() }, this.rand.nextFloat()));
		human.setCanHairGray(other.canHairGray());
		human.setImmuneStrength((this.getImmuneStrength() + other.getImmuneStrength()));
		human.setAltitudeStrength((this.getAltitudeStrength() + other.getAltitudeStrength()));
		human.setHeatStrength((this.getHeatStrength() + other.getHeatStrength()));
		human.setAgeFactor((this.getAgeFactor() + other.getAgeFactor()) / 2);
		human.setTribe(this.getTribeID());
		human = Humans.gen(human, EntityHuman.class);
		human.setPosition(this.posX, this.posY, this.posZ);
		human.onInitialSpawn(this.world.getDifficultyForLocation(this.getPosition()), null);
		Iterator<ItemStack> it = this.getArmorInventoryList().iterator();
		while (it.hasNext()) {
			ItemStack stack = it.next(); human.setItemStackToSlot(EntityHuman.getSlotForItemStack(stack), stack);
		}
		return human;
	}
	public double getGeneticDistance(EntityHuman other) {
		double distance = Math.abs(this.getMaxHealth() - other.getMaxHealth());
		distance += (this.getMemories().size() - Math.abs(this.getMemories().size() - other.getMemories().size())) / (this.getMemories().size() + 1);
		distance += (this.getSkinColor() - Math.abs(this.getSkinColor() - other.getSkinColor())) / (this.getSkinColor() + 1);
		distance += (this.getHairColor() - Math.abs(this.getHairColor() - other.getSkinColor())) / (this.getHairColor() + 1);
		distance += (this.getHairType() - Math.abs(this.getHairType() - other.getHairType())) / (this.getHairType() + 1);
		distance += (this.getStrength() - Math.abs(this.getStrength() - other.getStrength())) / (this.getStrength() + 1);
		distance += (this.getSpeed() - Math.abs(this.getSpeed() - other.getSpeed())) / (this.getSpeed() + 1);
		distance += (this.getSize() - Math.abs(this.getSize() - other.getSize())) / (this.getSize() + 1);
		distance += (this.getImmuneStrength() - Math.abs(this.getImmuneStrength() - other.getImmuneStrength())) / (this.getImmuneStrength() + 1);
		distance += (this.getAltitudeStrength() - Math.abs(this.getAltitudeStrength() - other.getAltitudeStrength())) / (this.getAltitudeStrength() + 1);
		distance += (this.getHeatStrength() - Math.abs(this.getHeatStrength() - other.getHeatStrength())) / (this.getHeatStrength() + 1);
		distance += (this.getAgeFactor() - Math.abs(this.getAgeFactor() - other.getAgeFactor())) / (this.getAgeFactor() + 1);
		return distance;
	}
}