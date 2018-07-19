package mod.akrivus.revolution.entity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityFemale extends EntityHuman {
	protected float fertilityFactor;
	public int pregnantTicks;
	public EntityFemale(World world) {
		super(world);
		this.fertilityFactor = 0.25F * world.rand.nextInt(4);
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
	@Override
	public boolean isAroused() {
		return this.fertilityFactor == this.world.getCurrentMoonPhaseFactor() && this.isFertile();
	}
	public float getFertilityFactor() {
		return this.fertilityFactor;
	}
	public void setFertilityFactor(float fertilityFactor) {
		this.fertilityFactor = fertilityFactor;
	}
}
