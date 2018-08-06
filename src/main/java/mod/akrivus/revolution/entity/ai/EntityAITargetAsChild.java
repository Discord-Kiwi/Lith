package mod.akrivus.revolution.entity.ai;

import mod.akrivus.revolution.entity.EntityHuman;
import net.minecraft.entity.ai.EntityAITarget;

public class EntityAITargetAsChild extends EntityAITarget {
	public EntityHuman human;
	public EntityAITargetAsChild(EntityHuman human) {
		super(human, true, true);
		this.human = human;
	}
	@Override
	public boolean shouldExecute() {
		return !this.human.isOldEnoughToBreed();
	}
	@Override
	public void startExecuting() {
        this.human.setAttackTarget(null);
        super.startExecuting();
        this.human.setAttackTarget(null);
    }
}
