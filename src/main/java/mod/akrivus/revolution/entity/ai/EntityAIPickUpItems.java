package mod.akrivus.revolution.entity.ai;

import java.util.List;

import mod.akrivus.revolution.entity.EntityHuman;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.SoundEvents;

public class EntityAIPickUpItems extends EntityAIBase {
	private final EntityHuman human;
	private final double movementSpeed;
	private EntityItem item;
	private int blockTick = 0;
	private int delay = 0;
	public EntityAIPickUpItems(EntityHuman entityIn, double movementSpeedIn) {
		this.human = entityIn;
		this.movementSpeed = movementSpeedIn;
		this.setMutexBits(1);
	}
	@Override
	public boolean shouldExecute() {
		if (this.delay > 100) {
			List<EntityItem> list = this.human.world.<EntityItem>getEntitiesWithinAABB(EntityItem.class, this.human.getEntityBoundingBox().grow(8.0D, 2.0D, 8.0D));
			double maxDistance = Double.MAX_VALUE;
			for (EntityItem item : list) {
				double newDistance = this.human.getDistanceSq(item);
				if (newDistance <= maxDistance && !item.isDead) {
					maxDistance = newDistance;
					this.item = item;
				}
			}
	 		return this.item != null && !this.item.isDead && this.human.canPickUpLoot() && this.human.canEntityBeSeen(this.item)
	 				&& this.human.getNavigator().tryMoveToEntityLiving(this.item, this.movementSpeed) && this.human.ticksExisted % 20 == 0;
		}
		++this.delay;
		return false;
	}
	@Override
	public boolean shouldContinueExecuting() {
		return this.item != null && !this.item.isDead && this.human.canEntityBeSeen(this.item);
	}
	@Override
	public void startExecuting() {
		this.human.getLookHelper().setLookPositionWithEntity(this.item, 30.0F, 30.0F);
	}
	@Override
	public void updateTask() {
		++this.blockTick;
		if (this.blockTick > 600) {
			this.item.setPosition(this.human.posX, this.human.posY, this.human.posZ);
		}
		else {
			this.human.getNavigator().tryMoveToEntityLiving(this.item, this.movementSpeed);
		}
		if (this.human.getDistanceSq(this.item.getPosition()) < 1.0D) {
			this.delay = 0;
		}
	}
	@Override
	public void resetTask() {
		this.item = null;
		this.blockTick = 0;
	}
}
