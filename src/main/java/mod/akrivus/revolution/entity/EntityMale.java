package mod.akrivus.revolution.entity;

import mod.akrivus.revolution.entity.ai.EntityAIBreedInter;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;

public class EntityMale extends EntityHuman {
	protected static final DataParameter<Integer> BEARD_TYPE = EntityDataManager.createKey(EntityHuman.class, DataSerializers.VARINT);
	protected boolean canLoseHair;
	public EntityMale(World world) {
		super(world);
		this.tasks.addTask(6, new EntityAIBreedInter(this, 0.3D));
	}
	@Override
	public boolean isAIDisabled() {
		return false;
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
	public boolean isOldEnoughToBreed() {
		return this.getAge() > 24192000;
	}
	public int getBeardType() {
		return this.dataManager.get(BEARD_TYPE);
	}
	public void setBeardType(int beardType) {
		this.dataManager.set(BEARD_TYPE, beardType);
	}
	public boolean canLoseHair() {
		return this.canLoseHair;
	}
	public void setCanLoserHair(boolean canLoseHair) {
		this.canLoseHair = canLoseHair;
	}
}
