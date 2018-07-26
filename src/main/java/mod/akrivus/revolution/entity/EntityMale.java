package mod.akrivus.revolution.entity;

import mod.akrivus.revolution.entity.ai.EntityAIBreedInter;
import net.minecraft.world.World;

public class EntityMale extends EntityHuman {
	public EntityMale(World world) {
		super(world);
		this.tasks.addTask(4, new EntityAIBreedInter(this, 0.3D));
	}
	@Override
	public boolean isAIDisabled() {
		return false;
	}
	@Override
	public boolean isOldEnoughToBreed() {
		return this.getAge() > 24192000;
	}
}
