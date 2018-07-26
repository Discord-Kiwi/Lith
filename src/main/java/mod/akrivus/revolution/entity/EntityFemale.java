package mod.akrivus.revolution.entity;

import mod.akrivus.revolution.Revolution;
import mod.akrivus.revolution.entity.ai.EntityAIBreedIntra;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class EntityFemale extends EntityHuman {
	protected float fertilityFactor;
	public int pregnantTicks;
	public EntityFemale(World world) {
		super(world);
		this.fertilityFactor = 0.25F * world.rand.nextInt(4);
		this.tasks.addTask(6, new EntityAIBreedIntra(this, 0.5));
	}
	@Override
	public boolean isAIDisabled() {
		return false;
	}
	@Override
	public void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setFloat("fertilityFactor", this.fertilityFactor);
    }
	@Override
    public void readEntityFromNBT(NBTTagCompound compound) {
       super.readEntityFromNBT(compound);
       this.setFertilityFactor(compound.getFloat("fertilityFactor"));
    }
	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand) {
		if (!this.world.isRemote && hand == EnumHand.MAIN_HAND) {
			if (player.getHeldItem(hand).getItem() == Revolution.FERTILIZER) {
				this.setFertilityFactor(this.world.getCurrentMoonPhaseFactor());
				this.setIsFertile(true);
			}
		}
		return super.processInteract(player, hand);
	}
	@Override
	public boolean isOldEnoughToBreed() {
		return this.getAge() > 18144000 * this.getAgeFactor();
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
