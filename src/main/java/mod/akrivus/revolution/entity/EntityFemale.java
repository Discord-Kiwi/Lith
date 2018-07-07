package mod.akrivus.revolution.entity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityFemale extends EntityHuman {
	protected float fertilityFactor;
	public int pregnantTicks;
	public EntityFemale(World world) {
		super(world);
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
	public void setSize(float size) {
		this.dataManager.set(SIZE, size);
	}
	@Override
	public boolean isOldEnoughToBreed() {
		return this.getAge() > 18144000;
	}
	public float getFertilityFactor() {
		return this.fertilityFactor;
	}
	public void setFertilityFactor(float fertilityFactor) {
		this.fertilityFactor = fertilityFactor;
	}
}
