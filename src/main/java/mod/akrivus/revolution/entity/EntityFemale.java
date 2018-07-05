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
	public float getFertilityFactor() {
		return this.fertilityFactor;
	}
	public void setFertilityFactor(float fertilityFactor) {
		this.fertilityFactor = fertilityFactor;
	}
}
