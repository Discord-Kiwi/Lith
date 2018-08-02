package mod.akrivus.revolution.entity.ai;

import mod.akrivus.revolution.entity.EntityHuman;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAISleep extends EntityAIBase {
	protected EntityHuman human;
    public EntityAISleep(EntityHuman human) {
        this.human = human;
        this.setMutexBits(4);
    }
    @Override
    public boolean shouldExecute() {
    	this.human.setIsSleeping(false);
    	if (!this.human.getTribe().isHomeless() && this.human.getDistanceSq(this.human.getTribe().getHome()) < 16) {
    		if ((this.human.world.getWorldTime() % 24000) > 14000 && this.human.getRevengeTarget() == null && this.human.getAttackTarget() == null) {
    			return true;
    		}
    	}
    	return false;
    }
    @Override
    public boolean shouldContinueExecuting() {
    	return (this.human.world.getWorldTime() % 24000) > 14000 && this.human.getRevengeTarget() == null && this.human.getAttackTarget() == null;
    }
    @Override
    public void startExecuting() {
    	this.human.getNavigator().clearPath();
    	this.human.setIsSleeping(true);
    }
    @Override
    public void updateTask() {
    	this.startExecuting();
    }
    @Override
    public void resetTask() {
    	this.human.setIsSleeping(false);
    }
}
